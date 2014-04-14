package application;

import application.item.Item;
import application.item.ItemsManager;
import application.plugins.PluginRegistry;
import application.plugins.WebProduct;
import application.product.Product;
import application.product.ProductsManager;
import application.storage.ProductContainer; 
import application.storage.RemovedItemsManager;
import application.storage.StorageUnitsManager;
import common.Barcode;
import common.Notification;
import common.NotificationType;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InvalidProductException;
import dataPersistence.PersistenceManager;
import java.util.Iterator;
import java.util.Observable; 

/**
 *
 * @author Trevor
 */
public class ProductFacade extends Observable
{
	private static ProductFacade _instance = new ProductFacade();
	private ProductsManager productsManager;
	private StorageUnitsManager containerManager;
	private RemovedItemsManager removedManager;
	private ItemsManager itemsManager;
	private PersistenceManager persistenceManager;
	private PluginRegistry productIdentifier;
	
	private ProductFacade()
	{
		productsManager = ProductsManager.getInstance();
		containerManager = StorageUnitsManager.getInstance();
		itemsManager = ItemsManager.getInstance();
		removedManager = RemovedItemsManager.getInstance();
		persistenceManager = PersistenceManager.getInstance();
		productIdentifier = new PluginRegistry();
	}
	
	/**
	 * Gets the instance of this singleton class
	 * @return		the one and only instance of this class
	 */
	public static ProductFacade getInstance()
	{
		return _instance;
	}
	
	/**
	 * Moves a product from one container to another.
	 * Products may always be moved to another container but there are some things that must happen:
	 * If the target storage unit has the product in it somewhere then the product and all of it's
	 *  items are moved into the target container. 
	 * If the target storage unit does not have the product in it somewhere then the product is
	 *  added to the target container
	 * 
	 * @param product the product that is being moved
	 * @param oldContainer the container the product is currently in
	 * @param newContainer the ProductContainer where the item is being moved to. 
	 * 
	 * @return true if successful
	 * @throws CannotBeRemovedException  
	 */
	public boolean moveProduct(Product product, ProductContainer oldContainer, 
			ProductContainer newContainer) throws CannotBeRemovedException
	{
		assert(product != null);
		assert(newContainer != null);
                assert (productsManager.canAddProductContainerPair(product, newContainer));
//                assert(!newContainer.getName().equals(HomeStorage.ROOT_NAME));

		//Get the storage unit of the containers.
		ProductContainer oldStorageUnit = containerManager.getStorageUnit(oldContainer);
		ProductContainer newStorageUnit = containerManager.getStorageUnit(newContainer);
		if (oldStorageUnit.equals(newStorageUnit))
		{
			moveProduct(oldContainer, product, newContainer);
		}
		else
		{
			if (containerManager.getContainerThatHasProduct(newStorageUnit, product) != null)
			{
				ProductContainer oldContainerOfNewSU =
						containerManager.getContainerThatHasProduct(newStorageUnit, product);
				moveProduct(oldContainer, product, oldContainerOfNewSU);
			}
			else
			{
                            PersistenceManager.getInstance().startTransaction();
                            if(containerManager.addProduct(newContainer, product)) {
                                PersistenceManager.getInstance().finishTransaction(true);
                            } else {
                                PersistenceManager.getInstance().finishTransaction(false);
                            }
			}
		}

		Notification notification = new Notification(NotificationType.ProductMoved);
		notification.setProductInvolved(product);
		setChanged();
		notifyObservers(notification);

		return true;
	}
        
      private void moveProduct(ProductContainer oldContainer, Product product,
			ProductContainer newContainer) throws CannotBeRemovedException
	{
		PersistenceManager.getInstance().startTransaction();
		// Update items
		Iterator<Item> items =
				this.containerManager.getItemsOfProduct(oldContainer, product);
		while (items.hasNext())
		{
			Item item = items.next();
			if (!itemsManager.updateDirectContainer(item, newContainer))
			{
				PersistenceManager.getInstance().finishTransaction(false);
				return;
			}

			// update containers/items
			this.containerManager.removeItem(oldContainer, item);
			this.containerManager.addItem(newContainer, item);
		}

		// update products
		if(!this.productsManager.removeProductToContainerPair(product, oldContainer)) { 
                    PersistenceManager.getInstance().finishTransaction(false); 
                    return; 
                }
		if(!this.productsManager.addProductContainerPair(product, newContainer)) { 
                    PersistenceManager.getInstance().finishTransaction(false); 
                    return; 
                }

		// update containers/products
                if(this.containerManager.canRemoveProduct(oldContainer, product)){
                    if(!this.containerManager.removeProduct(oldContainer, product)) { 
                        PersistenceManager.getInstance().finishTransaction(false); 
                        return; 
                    }
                }
		if(!this.containerManager.addProduct(newContainer, product)) { 
                    PersistenceManager.getInstance().finishTransaction(false); 
                    return; 
                }
                //System.out.println("Moved");
            PersistenceManager.getInstance().finishTransaction(true);
	}
	
	/**
	 * Adds a new product to a container
	 * 
	 * @param product		the product being added
	 * @param container		the container the product will be added to
	 * @return				true if it was successful, false if not
	 */
	public boolean addProductToContainer(Product product, ProductContainer container) {
		
		boolean result = false;
		this.persistenceManager.startTransaction();
		result = this.containerManager.addProduct(container, product);
		this.productsManager.addProductContainerPair(product, container);
		this.persistenceManager.finishTransaction(result);

		Notification notification = new Notification(NotificationType.ProductAdded);
		notification.setProductInvolved(product);
		setChanged();
		notifyObservers(notification);

		return result;
	}
	
	/**
	 * Determines whether or not a Product can be deleted from the selected ProductContainer
	 * If no ProductContainer is selected then the product cannot be deleted.
	 * If a ProductContainer is selected then there can be no items of that product within the 
	 * selected container.
	 * 
	 * @param product
	 * @param selectedContainer
	 * @return true if the product can be deleted
	 */
	public boolean canDeleteProduct(Product product, ProductContainer selectedContainer)
	{
		//ProductContainer can be null here now taken care of in productsmanager
		assert(product != null);
       	return productsManager.canRemove(product, selectedContainer);
	}
	
	
	/**
	 * Deletes the product from the selected ProductContainer
	 * If the root "Storage Units" is the selectedContainer then:
	 * The product is deleted from the system entirely including all historical data about the
	 *  product
	 * 
	 * If any other ProductContainer is selected then the product is deleted from that container.
	 * 
	 * @param product 
	 * @param selectedContainer
	 * 
	 * @return true if successful
	 * 
	 * @throws CannotBeRemovedException
	 */
	public boolean deleteProduct(Product product, ProductContainer selectedContainer) 
			throws CannotBeRemovedException
	{
		assert(product != null);
		assert(selectedContainer != null);
		assert(canDeleteProduct(product, selectedContainer));
        
		this.persistenceManager.startTransaction();
		if (selectedContainer.getParent() == null)
		{
			if (removedManager.removeProduct(product)
					&& containerManager.removeProduct(null, product)
					&& productsManager.removeEntirely(product))
			{
				this.persistenceManager.finishTransaction(true);
			}
			else
			{
				this.persistenceManager.finishTransaction(false);
				return false;
			}
		}
		else
		{
			if (productsManager.removeProductToContainerPair(product, selectedContainer)
					&& containerManager.removeProduct(selectedContainer, product))
			{
				this.persistenceManager.finishTransaction(true);
			}
			else
			{
				this.persistenceManager.finishTransaction(false);
				return false;
			}
		}
		
		
		Notification notification = new Notification(NotificationType.ProductRemoved);
		notification.setProductInvolved(product);
		setChanged();
		notifyObservers(notification);
		return true;
	}
	
	/**
	 * Determines whether or not the given product is valid and can be added to the system.
	 * 
	 * @param product
	 * @return 
	 */
	public boolean canAddProduct(Product product)
	{
		return productsManager.canAddProduct(product);
	}
	
	/**
	 * Adds a new product to the selected Storage Unit. 
	 * 
	 * @param product
	 * @param selectedStorageUnit 
	 * 
	 * @return true if successful
	 * 
	 * @throws InvalidProductException
	 */
	public boolean addProduct(Product product, ProductContainer selectedStorageUnit) 
			throws InvalidProductException
	{
		assert(product != null);
		assert(canAddProduct(product));
		
		PersistenceManager.getInstance().startTransaction();
		if (selectedStorageUnit == null)
		{
			if (productsManager.addNewProduct(product, product.getBarcode()))
			{
				PersistenceManager.getInstance().finishTransaction(true);
			}
			else
			{
				PersistenceManager.getInstance().finishTransaction(false);
				return false;
			}
		}
		else
		{
			if (productsManager.addNewProduct(product, product.getBarcode(), selectedStorageUnit)
					&& containerManager.addProduct(selectedStorageUnit, product))
			{
				PersistenceManager.getInstance().finishTransaction(true);
			}
			else
			{
				PersistenceManager.getInstance().finishTransaction(false);
				return false;
			}
		}
		
		Notification notification = new Notification(NotificationType.ProductAdded);
		notification.setProductInvolved(product);
		setChanged();
		notifyObservers(notification);
		return false;
	}
	
	/**
	 * Determines whether or not the values of the product are valid
	 * 
	 * @param product
	 * @return 
	 */
	public boolean canEditProduct(Product product)
	{
		return true;
	}
	
	/**
	 * Edits the attributes of a product.
	 * 
	 * @param oldProduct
	 * @param newProduct 
	 * 
	 * @return true if successful
	 * 
	 * @throws InvalidProductException
	 */
	public boolean editProduct(Product oldProduct, Product newProduct)
			throws InvalidProductException
	{
		assert(oldProduct != null);
		assert(newProduct != null);
		assert (canEditProduct(newProduct));

                PersistenceManager.getInstance().startTransaction();
                if(this.productsManager.updateProduct(oldProduct, newProduct)) {
                    PersistenceManager.getInstance().finishTransaction(true);
                } else {
                    PersistenceManager.getInstance().finishTransaction(false);
                    return false;
                }
		Notification notification = new Notification(NotificationType.ProductChanged);
		notification.setProductInvolved(oldProduct);
		setChanged();
		notifyObservers(notification);
		return false;
	}
	
	
	/**
	 * Gets an iterator of all the items that are of the given product in the entire system
	 * 
	 * @param product the product
	 * @return 
	 */
	public Iterator<Item> getItems(Product product)
	{
		return productsManager.getItems(product);
	}

	/**
	 * Gets the product that is represented by the given barcode.
	 * @param productBarcode	the barcode of the desired product
	 * @return					the product that has the barcode
	 */
	public Product getProduct(Barcode productBarcode)
	{
		return productsManager.getProduct(productBarcode);
	}	
	
	/**
	 * gets all the products in the system
	 * @return		an iterator with all the products in the system
	 */
	public Iterator<Product> getProducts(){
		return productsManager.getProducts();
	}
	
	/**
	 * gets the number of items of the product in the system
	 * @param product	the product of interest
	 * @return			the numbers of items of this product in the system
	 */
	public int getItemCount(Product product){
		return productsManager.getItemCount(product);
	}

	

	public void undoAddProduct(Product _product)
	{
		this.persistenceManager.startTransaction();
		boolean result = false;
		try
		{
			boolean result1 = this.containerManager.removeProduct(null, _product);
			boolean result2 = this.productsManager.removeEntirely(_product);
			result = true;
			if(result1 && result2) {
				this.persistenceManager.finishTransaction(true);
			} else {
				this.persistenceManager.finishTransaction(false);
				return;
			}

			Notification notification = new Notification(NotificationType.ProductRemoved);
			notification.setProductInvolved(_product);
			setChanged();
			notifyObservers(notification);
		}
		catch (CannotBeRemovedException ex)
		{
		}
	}
	
	public boolean productExists(Product product)
	{
		return productsManager.getProduct(product.getBarcode()) != null;
	}
	
	public WebProduct identifyProduct(String barcode){
		return this.productIdentifier.findProduct(barcode);
	}

    public void reset() {
        productsManager = ProductsManager.getInstance();
        containerManager = StorageUnitsManager.getInstance();
        itemsManager = ItemsManager.getInstance();
        removedManager = RemovedItemsManager.getInstance();
        persistenceManager = PersistenceManager.getInstance();
    }
}
