/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.item.Item;
import application.item.ItemsManager;
import application.product.Product;
import application.product.ProductsManager;
import application.storage.ProductContainer;
import application.storage.RemovedItemsManager;
import application.storage.StorageUnitsManager;
import com.itextpdf.text.DocumentException;
import common.Barcode;
import common.DTOConverter;
import common.Notification;
import common.NotificationType;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidItemException;
import common.exceptions.InvalidProductException;
import dataPersistence.IItemDAO;
import dataPersistence.ItemDTO;
import dataPersistence.PersistenceManager;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import javax.swing.text.html.parser.DTD;

/**
 *
 * @author Leckie
 */
public class ItemsFacade extends Observable {

    private RemovedItemsManager removedItems = RemovedItemsManager.getInstance();
    private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();
    private ItemsManager items = ItemsManager.getInstance();
    private ProductsManager products = ProductsManager.getInstance();
    private PersistenceManager persistenceManager = PersistenceManager.getInstance();
    private static ItemsFacade instance = null;

    /**
     * Get the instance of the facade
     *
     * @return the instance of this facade
     */
    public static ItemsFacade getInstance() {
        if (instance == null) {
            instance = new ItemsFacade();
        }
        return instance;
    }
    //

    /**
     * Constructor
     */
    private ItemsFacade() {
    }

    /**
     * Gets the item associated to that barcode
     *
     * @param barcode
     * @return an item
     */
    public Item getItem(Barcode barcode) {
        return items.getItem(barcode);
    }

    /**
     * Check whether or not an Item can be added
     *
     * @param item
     * @return
     */
    public boolean canAddItem(Item item) {
        return items.canAdd(item);
    }

    /**
     * Add and Item to the system
     *
     * @param item
     * @returns the item added
     * @throws InvalidAddException
     * @throws InvalidItemException
     * @throws InvalidProductException
     * @throws InsufficientBarcodesException
     */
    public boolean addItem(Item item)
            throws InvalidAddException, InvalidItemException,
            InvalidProductException, InsufficientBarcodesException {
        boolean result = false;
        this.persistenceManager.startTransaction();

		// if container.getParentStorageUnit() has item's product
		ProductContainer pc = storageUnits.getStorageUnit(item.getProductContainer());
		ProductContainer productContainer =
				storageUnits.getContainerThatHasProduct(pc, item.getProduct());
		assert (productContainer != null);
		//the producut is added to the container in the controller through the product
		// facade, so it should never be null
//        if (productContainer == null) {
//            // add product to container
//            //instance.addProductToContainer(item.getProduct(), item.getStorageUnit());
//        } else 
		if (!storageUnits.isStorageUnit(productContainer)) {
			item.setProductContainer(productContainer);
		}
        result = this.items.add(item);

        if (result) {
            // add item to container
            instance.addItemToContainer(item);
            // add item to product
            instance.addItemToProduct(item);
        }
        Notification notification = new Notification(NotificationType.ItemsAdded);
        setChanged();
        notifyObservers(notification);

        this.persistenceManager.finishTransaction(result);
        return result;
    }

    /**
     * Adds a collection of items to the system
     *
     * @param items	the items that should be added
     * @throws InvalidAddException
     * @throws InvalidItemException
     * @throws InvalidProductException
     * @throws InsufficientBarcodesException
     */
    public boolean addItems(Collection<Item> items)
            throws InvalidAddException, InvalidItemException,
            InvalidProductException, InsufficientBarcodesException {
        boolean result = true;
        for (Item item : items) {
            result = this.addItem(item) & result;
        }
        return result;
    }

    /**
     * Move and Item from one container to another finds the container in the
     * given storage unit that holds the same product as this item and puts it
     * in the same container
     *
     * @param item
     * @param container
     * @throws InvalidAddException
     * @throws InvalidItemException
     * @throws InsufficientBarcodesException
     */
    public boolean moveItemToStorageUnit(Item item, ProductContainer container)
            throws InvalidAddException, InvalidItemException, InsufficientBarcodesException {
        boolean result = false;
        persistenceManager.startTransaction();
        // get oldContainer
        ProductContainer oldContainer = items.getDirectContainer(item);
        // if container.getParentStorageUnit() has item's product
        ProductContainer productContainer = storageUnits.getContainerThatHasProduct(storageUnits.getStorageUnit(container), items.getProduct(item));
        if (productContainer == null) {
            result = storageUnits.addProduct(container, items.getProduct(item));
            productContainer = container;
        } else {
            result = true;
        }
//        else
//        {
//            instance.addProduct(container, items.getProduct(item), new ArrayList<Item>());
//        }

        // remove item from oldContainer
        //instance.removeItemFromContainer(item, oldContainer);

        // tell the item that it has changed locations
        if (result) {
            moveItem(item, oldContainer, productContainer);
        }

        persistenceManager.finishTransaction(result);
        return result;
    }

    /**
     * Move and Item from one container to another if a different container in
     * the same storage unit holds the same product all of those items are moved
     * to the same container
     *
     * @param item
     * @param targetContainer
     * @throws InvalidAddException
     * @throws InvalidItemException
     * @throws InsufficientBarcodesException
     */
    public void moveItemToContainer(Item item, ProductContainer targetContainer)
            throws InvalidAddException, InvalidItemException, InsufficientBarcodesException {

        boolean result = false;
        persistenceManager.startTransaction();

        ProductContainer storageUnit = storageUnits.getStorageUnit(targetContainer);
        //the other container in the storageUnit that has the same product
        ProductContainer containerThatHasProduct =
                storageUnits.getContainerThatHasProduct(storageUnit, item.getProduct());

        Iterator<Item> otherItemsToMove = null;

        //the product does not exist in the storage unit
        if (containerThatHasProduct == null) {
            //create the product in the container, then move the item
            result = storageUnits.addProduct(targetContainer, item.getProduct());
        } //the product is already in the targetcontainer
        else {
            if (containerThatHasProduct.equals(targetContainer)) {
                result = true;
                //there is nothing that needs to be done for this case other than move the item
            } //the product is in another container and must be moved to the target as well
            else {
                //create the product in the container, remove it from the other (done later)
                //	move the item and all items of the same product in containerThatHasProduct
                otherItemsToMove =
                        storageUnits.getItemsOfProduct(containerThatHasProduct, item.getProduct());
                result = storageUnits.addProduct(targetContainer, item.getProduct());
            }
        }

        //move the first item
        ProductContainer oldContainer;
        if (item.getProductGroup() == null) {
            oldContainer = item.getStorageUnit();
        } else {
            oldContainer = item.getProductGroup();
        }
        if (result) {
            moveItem(item, oldContainer, targetContainer);

            //move all the others
            while (otherItemsToMove != null && otherItemsToMove.hasNext()) {
                moveItem(otherItemsToMove.next(), containerThatHasProduct, targetContainer);
            }

            //if the product was elsewhere, remove it from the other container
            if (containerThatHasProduct!= null && !containerThatHasProduct.equals(targetContainer)) {
                try {
                    storageUnits.removeProduct(containerThatHasProduct, item.getProduct());
                } catch (CannotBeRemovedException ex) {
                }
            }
        }
        persistenceManager.finishTransaction(result);

        Notification notification = new Notification(NotificationType.ProductMoved);
        setChanged();
        notifyObservers(notification);
    }

    private boolean moveItem(Item item, ProductContainer oldContainer, ProductContainer newContainer) {

        ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
        itemDTO.setContainerID(newContainer.getID());
        IItemDAO itemDAO = persistenceManager.getItemDAO();
        boolean result = itemDAO.updateItem(itemDTO);

        if (result) {

            Item movedItem = items.getItem(item.getBarcode());
            movedItem.setProductContainer(newContainer);

            // remove item from old container and put it in the new one
            storageUnits.removeItem(oldContainer, movedItem);
            storageUnits.addItem(newContainer, movedItem);

        }

        Notification notification = new Notification(NotificationType.ItemsChanged);
        setChanged();
        notifyObservers(notification);

        return result;
    }

    /**
     * Adds an Item to an Container
     *
     * @param item
     * @param container
     * @throws InvalidAddException
     * @throws InvalidItemException
     */
    private void addItemToContainer(Item item)
            throws InvalidAddException, InvalidItemException {

        storageUnits.addItem(item.getProductContainer(), item);
    }

    /**
     * Add an Item to a Product
     *
     * @param item
     * @param product
     */
    private void addItemToProduct(Item item) {
        products.addProductItemPair(item.getProduct(), item);
    }

    /**
     * Retrieves the product of the given item
     *
     * @param currentItem	the item being inquired of
     * @return	the product of the given item
     */
    public Product getProduct(Item currentItem) {
        return items.getProduct(currentItem);
    }

    /**
     * Checks the given date to see if it is a legal entry date (after Jan 1
     * 2001 and before today)
     *
     * @param _entryDate	the date being checked
     * @return	true if it is legal, false if not
     */
    public boolean isValidDate(Date _entryDate) {
        if (_entryDate == null) {
            return false;
        }

        Date earliestDate = new Date(100, 0, 1);

        if (_entryDate.before(earliestDate)) {
            return false;
        }

        return true;
    }

    /**
     * Removes the given item from the system
     *
     * @param item	the item to be removed
     */
    public boolean removeItem(Item item) {

        boolean result = false;
        persistenceManager.startTransaction();

        ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
        IItemDAO itemDAO = persistenceManager.getItemDAO();
        itemDTO.setExitDate(new Date());
        result = itemDAO.updateItem(itemDTO);

        if (result) {
            ProductContainer container = items.getDirectContainer(item);
            item = items.remove(item);
            products.removeProductToItem(item.getProduct(), item);
            storageUnits.removeItem(container, item);
            try {
                removedItems.addItem(item);
            } catch (InvalidAddException ex) {
            }
        }

        Notification notification = new Notification(NotificationType.ItemsRemoved);
        setChanged();
        notifyObservers(notification);

        persistenceManager.finishTransaction(result);
        return result;
    }

    /**
     * Prints the list of items to a PDF and displays it on the screen
     *
     * @param itemsToPrint	a list of all the items to be printed
     * @return
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public String printItems(List<Item> itemsToPrint)
            throws DocumentException, FileNotFoundException {
        return items.printBarcodes(itemsToPrint);
    }

    /**
     * Changes the entry date of the given item
     *
     * @param item	the item to be changed
     * @param date	the new entry date of the item
     */
    public boolean editItem(Item item, Date date) {

        boolean result = false;
        persistenceManager.startTransaction();


        IItemDAO itemDAO = persistenceManager.getItemDAO();
        ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
        itemDTO.setEntryDate(date);
        result = itemDAO.updateItem(itemDTO);

        if (result) {
            item.setEntryDate(date);

            if (date.before(item.getProduct().getDate())) {
                item.getProduct().setDate(date);
            }
        }

        persistenceManager.finishTransaction(result);
        Notification notification = new Notification(NotificationType.ItemsChanged);
        setChanged();
        notifyObservers(notification);

        return result;
    }

    public boolean undoAddItems(Collection<Item> items) {
        boolean result = false;
        persistenceManager.startTransaction();

        for (Item item : items) {
            ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
            IItemDAO itemDAO = persistenceManager.getItemDAO();
            result = itemDAO.removeItem(itemDTO);
        }

        if (result) {
            this.items.deleteItems(items);
            for (Item item : items) {
                this.products.removeProductToItem(item.getProduct(), item);
                this.storageUnits.removeItem(item.getProductContainer(), item);
            }
        }

        persistenceManager.finishTransaction(result);
        Notification notification = new Notification(NotificationType.ItemsRemoved);
        setChanged();
        notifyObservers(notification);

        return result;
    }

    public boolean containerHasProduct(ProductContainer pc, Product product) {
        return this.storageUnits.getContainerThatHasProduct(pc, product) != null;
    }

    public void reset() {
        removedItems = RemovedItemsManager.getInstance();
        storageUnits = StorageUnitsManager.getInstance();
        items = ItemsManager.getInstance();
        products = ProductsManager.getInstance();
        persistenceManager = PersistenceManager.getInstance();
    }
//	public void getCurrentContainer(Item item)
//	{
//		this(item);
//	}
    /**
     * Adds brand new items to the storage unit which will add them to the
     * correct group. If the product of the item does not exist in the system it
     * will be created. The items MUST have their product set.
     *
     * @param su
     * @param items
     * @throws InvalidAddException
     */
//	public void newAddItems(StorageUnit su, Product p, Collection<Item> items)
//	throws InvalidAddException,
//			InsufficientBarcodesException
//			
//	{
//		if(items == null || items.size() == 0)
//		{
//			return;
//		}
//		
//		if(su == null || p == null)
//		{
//			throw new InvalidAddException("No storage unit was selected.");
//		}
//		
//		if(products.canAddProduct(p))
//		{
//			products.addNewProduct(p, p.getBarcode());
//		}
//		
//		if(!products(su, p))
//		{
//			storageUnits.addProduct(su, p);
//		}
//		
//		for(Item item : items)
//		{
//			this.items.add(item);
//		}
//		
//		
//	}
    /**
     * Check whether or not an Item can be removed
     *
     * @param item
     * @return boolean
     */
//    public boolean canRemoveItem(Item item) {
//        return items.canRemove(item);
//    }
    /**
     * moves a product to a different container
     *
     * @param product
     * @param container
     * @throws InvalidAddException
     */
//    public void moveProduct(Product product, ProductContainer container) 
//                    throws InvalidAddException
//    {
//        // get oldContainer from product
//        ProductContainer oldContainer = storageUnits.getContainerThatHasProduct
//                    (storageUnits.getStorageUnit(container), product);
//
//        // if container.getParentStorageUnit() has product
//        if (oldContainer != null)
//        {
//            // move product from oldContainer to container
//            products.removeProductToContainerPair(product, oldContainer);
//            products.addProductContainerPair(product, container);
//        }
//        else
//        {
//            // remove product from oldContainer	// we can't tell which container 
//            //	we're moving from since a product may have multiple containers
//            // add product to container
//            products.addProductContainerPair(product, container);
//
//        }
//    }
    /**
     * Add a Product to a container Part of moving an Item
     *
     * @param product
     * @param container
     * @throws InvalidProductException
     */
//    private void addProductToContainer(Product product, ProductContainer container)
//            throws InvalidProductException {
//        Iterator<ProductContainer> currentContainers = products.getContainers(product);
//        boolean canAddProdToContainer = true;
//        while (currentContainers.hasNext()) {
//            ProductContainer p = currentContainers.next();
//            if (p.getParent() != null) {
//                if (storageUnits.getStorageUnit(p) == storageUnits.getStorageUnit(container)) {
//                    canAddProdToContainer = false;
//                }
//
//            }
//        }
//        //if(!p.isStorageUnit()){
//        //  if(p.getRootStorageUnit() == container.getRootStorageUnit()) throw error or something
//        //  else proceed normally
//        if (canAddProdToContainer) {
//            products.addProductContainerPair(product, container);
//            storageUnits.addProduct(container, product);
//        }
//    }
}
