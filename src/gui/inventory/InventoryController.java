package gui.inventory;

import application.ContainersFacade;
import application.ItemsFacade;
import application.ProductFacade;
import application.ReportsFacade;
import application.item.Item;
import application.product.Product;
import application.storage.HomeStorage;
import application.storage.ProductContainer;
import application.storage.StorageUnit;
import common.Notification;
import common.NotificationType;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidItemException;
import gui.common.*;
import gui.item.*;
import gui.product.*;
import java.util.*;

/**
 * Controller class for inventory view.
 */
public class InventoryController extends Controller 
									implements Observer, IInventoryController {
	
	private ContainersFacade containersFacade = ContainersFacade.getInstance();
	private ItemsFacade itemsFacade = ItemsFacade.getInstance();
	private ProductFacade productFacade = ProductFacade.getInstance();
	private ReportsFacade reportsFacade = ReportsFacade.getInstance();

	/**
	 * Constructor.
	 *  
	 * @param view Reference to the inventory view
	 */
	public InventoryController(IInventoryView view) {
		super(view);
		
		containersFacade.addObserver(this);
		itemsFacade.addObserver(this);
		productFacade.addObserver(this);
		reportsFacade.addObserver(this);

		construct();
	}

	/**
	 * Returns a reference to the view for this controller.
	 */
	@Override
	protected IInventoryView getView() {
		return (IInventoryView)super.getView();
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {
		loadProductContainerValues();
		loadProductValues();
		loadItemsValues();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently
	 * allowed to interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's view
	 * have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
		return;
	}
	
	//
	// IInventoryController overrides
	//

	/**
	 * Returns true if and only if the "Add Storage Unit" menu item should be enabled.
	 */
	@Override
	public boolean canAddStorageUnit() {
		return true;
	}
	
	/**
	 * Returns true if and only if the "Add Items" menu item should be enabled.
	 */
	@Override
	public boolean canAddItems() {
		return true;
	}
	
	/**
	 * Returns true if and only if the "Transfer Items" menu item should be enabled.
	 */
	@Override
	public boolean canTransferItems() {
		return true;
	}
	
	/**
	 * Returns true if and only if the "Remove Items" menu item should be enabled.
	 */
	@Override
	public boolean canRemoveItems() {
		return true;
	}

	/**
	 * Returns true if and only if the "Delete Storage Unit" menu item should be enabled.
	 */
	@Override
	public boolean canDeleteStorageUnit() {
		ProductContainerData selectedUnit = getView().getSelectedProductContainer();
		ProductContainer selectedContainer = (StorageUnit)selectedUnit.getTag();		
		return containersFacade.canRemoveProductContainer(selectedContainer);
	}
	
	/**
	 * This method is called when the user selects the "Delete Storage Unit" menu item.
	 */
	@Override
	public void deleteStorageUnit() {
		ProductContainerData selectedUnit = getView().getSelectedProductContainer();
		ProductContainer selectedContainer = (StorageUnit)selectedUnit.getTag();		
		containersFacade.removeProductContainer(selectedContainer);
	}

	/**
	 * Returns true if and only if the "Edit Storage Unit" menu item should be enabled.
	 */
	@Override
	public boolean canEditStorageUnit() {
		return true;
	}

	/**
	 * Returns true if and only if the "Add Product Group" menu item should be enabled.
	 */
	@Override
	public boolean canAddProductGroup() {
		return true;
	}

	/**
	 * Returns true if and only if the "Delete Product Group" menu item should be enabled.
	 */
	@Override
	public boolean canDeleteProductGroup() {
		return containersFacade.canRemoveProductContainer
				((ProductContainer)getView().getSelectedProductContainer().getTag());
	}

	/**
	 * Returns true if and only if the "Edit Product Group" menu item should be enabled.
	 */
	@Override
	public boolean canEditProductGroup() {
		return true;
	}
	
	/**
	 * This method is called when the user selects the "Delete Product Group" menu item.
	 */
	@Override
	public void deleteProductGroup() {
		containersFacade.removeProductContainer
				((ProductContainer)getView().getSelectedProductContainer().getTag());
	}

	private Random rand = new Random();
	
	private String getRandomBarcode() {
		Random rand = new Random();
		StringBuilder barcode = new StringBuilder();
		for (int i = 0; i < 12; ++i) {
			barcode.append(((Integer)rand.nextInt(10)).toString());
		}
		return barcode.toString();
	}

	/**
	 * This method is called when the selected item container changes.
	 */
	@Override
	public void productContainerSelectionChanged() {
            getView().setProducts(new ProductData[0]);
            loadProductValues();
            loadContextView();
            getView().selectProduct(null);
            getView().setItems(new ItemData[0]);
	}

	/**
	 * This method is called when the selected item changes.
	 */
	@Override
	public void productSelectionChanged() {
		loadItemsValues();
	}

	/**
	 * This method is called when the selected item changes.
	 */
	@Override
	public void itemSelectionChanged() {
		return;
	}

	/**
	 * Returns true if and only if the "Delete Product" menu item should be enabled.
	 */
	@Override
	public boolean canDeleteProduct() {
            ProductData currentProduct = (ProductData) getView().getSelectedProduct();
			if(currentProduct == null){
				return false;
			}
            Product product = (Product) getView().getSelectedProduct().getTag();
            ProductContainer container = 
					(ProductContainer) getView().getSelectedProductContainer().getTag();
            
            if(container.getName().equals(HomeStorage.ROOT_NAME)) {
                return this.productFacade.canDeleteProduct(product, null);
            } else {
                return this.productFacade.canDeleteProduct(product, container);
            }
	}

	/**
	 * This method is called when the user selects the "Delete Product" menu item.
	 */
	@Override
	public void deleteProduct() {
            Product product = (Product) getView().getSelectedProduct().getTag();
            ProductContainer container = 
					(ProductContainer) getView().getSelectedProductContainer().getTag();
            try {
                this.productFacade.deleteProduct(product, container);
            } catch(CannotBeRemovedException e) {
                getView().displayErrorMessage("Could not delete this product (" 
						+ product.getProdDesc() + ").");
            }
	}

	/**
	 * Returns true if and only if the "Edit Item" menu item should be enabled.
	 */
	@Override
	public boolean canEditItem() {
		return (getView().getSelectedItem() != null);
	}

	/**
	 * This method is called when the user selects the "Edit Item" menu item.
	 */
	@Override
	public void editItem() {
		getView().displayEditItemView();
	}

	/**
	 * Returns true if and only if the "Remove Item" menu item should be enabled.
	 */
	@Override
	public boolean canRemoveItem() {
		return (getView().getSelectedItem() != null);
	}

	/**
	 * This method is called when the user selects the "Remove Item" menu item.
	 */
	@Override
	public void removeItem() {
		Item item = (Item)getView().getSelectedItem().getTag();
		itemsFacade.removeItem(item);
	}

	/**
	 * Returns true if and only if the "Edit Product" menu item should be enabled.
	 */
	@Override
	public boolean canEditProduct() {
		return getView().getSelectedProduct() != null;
	}

	/**
	 * This method is called when the user selects the "Add Product Group" menu item.
	 */
	@Override
	public void addProductGroup() {
		getView().displayAddProductGroupView();
	}
	
	/**
	 * This method is called when the user selects the "Add Items" menu item.
	 */
	@Override
	public void addItems() {
		getView().displayAddItemBatchView();
	}
	
	/**
	 * This method is called when the user selects the "Transfer Items" menu item.
	 */
	@Override
	public void transferItems() {
		getView().displayTransferItemBatchView();
	}
	
	/**
	 * This method is called when the user selects the "Remove Items" menu item.
	 */
	@Override
	public void removeItems() {
		getView().displayRemoveItemBatchView();
	}

	/**
	 * This method is called when the user selects the "Add Storage Unit" menu item.
	 */
	@Override
	public void addStorageUnit() {
		getView().displayAddStorageUnitView();
	}

	/**
	 * This method is called when the user selects the "Edit Product Group" menu item.
	 */
	@Override
	public void editProductGroup() {
		getView().displayEditProductGroupView();
	}

	/**
	 * This method is called when the user selects the "Edit Storage Unit" menu item.
	 */
	@Override
	public void editStorageUnit() {
		getView().displayEditStorageUnitView();
	}

	/**
	 * This method is called when the user selects the "Edit Product" menu item.
	 */
	@Override
	public void editProduct() {
            ProductData selected = getView().getSelectedProduct();
            getView().displayEditProductView();
            if(selected != null) {
                getView().selectProduct(selected);
            }
	}
	
	/**
	 * This method is called when the user drags a product into a
	 * product container.
	 * 
	 * @param productData Product dragged into the target product container
	 * @param containerData Target product container
	 */
	@Override
	public void addProductToContainer(ProductData productData, ProductContainerData containerData) {
            
            ProductContainer oldContainer = 
					(ProductContainer) getView().getSelectedProductContainer().getTag();
            ProductContainer newContainer = (ProductContainer) containerData.getTag();
            Product product = (Product) productData.getTag();
            try {            
                this.productFacade.moveProduct(product, oldContainer, newContainer);
            } catch (CannotBeRemovedException ex) {
                getView().displayErrorMessage("Cannot move " + productData.getDescription() + 
						" into " + newContainer.getName());
            } 
	}

	/**
	 * This method is called when the user drags an item into
	 * a product container.
	 * 
	 * @param itemData Item dragged into the target product container
	 * @param containerData Target product container
	 */
	@Override
	public void moveItemToContainer(ItemData itemData,
									ProductContainerData containerData) {
		Item toBeMoved = (Item)itemData.getTag();
		ProductContainer target = (ProductContainer)containerData.getTag();
		try {
			itemsFacade.moveItemToContainer(toBeMoved, target);
		} catch (InvalidAddException ex) {
		} catch (InvalidItemException ex) {
		} catch (InsufficientBarcodesException ex) {
		}
	}

	/**
	 * This will update the interface using the Model classes
	 *
	 * @param o
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		Notification note = (Notification)arg;
		
		if(note.isContainerInvolved()){
			if(note.getType() == NotificationType.ContainerAdded){
				ProductContainerData parent = getView().getSelectedProductContainer();
				ProductContainerData newContainer = new ProductContainerData();
				newContainer.setName(note.getContainerInvolved().getName());
				newContainer.setTag(note.getContainerInvolved());
				int index = getChildIndexAdd(parent, newContainer);
				getView().insertProductContainer(parent, newContainer, index);
				getView().selectProductContainer(newContainer);
				productContainerSelectionChanged();
			}
			else if(note.getType() == NotificationType.ContainerRemoved){
				getView().deleteProductContainer(getView().getSelectedProductContainer());
				getView().setProducts(new ProductData[0]);
			}
			else if(note.getType() == NotificationType.ContainerChanged){
				ProductContainerData altered = getView().getSelectedProductContainer();
				ProductContainer container = note.getContainerInvolved();
				int index = getChildIndexEdit(container);
				getView().renameProductContainer(altered, container.getName(), index);	
				getView().selectProductContainer(altered);
			}
			loadContextView();
		}
		else if(note.areItemsInvolved()){
			loadProductValues();
			loadItemsValues();
		}
		else if(note.isProductInvolved()){
			loadProductValues();
			loadItemsValues();
		}
	}

	
	private void loadProductContainerValues(){
		ProductContainerData selected = getView().getSelectedProductContainer();
		ProductContainerData root = new ProductContainerData();
		root.setName(HomeStorage.ROOT_NAME);
		
		root = generateData(containersFacade.getRoot());
		
		getView().setProductContainers(root);
		getView().selectProductContainer(selected);
		
		loadContextView();
	}
	
	private void loadProductValues(){
		ProductData selectedProduct = getView().getSelectedProduct();
		ProductContainerData selectedContainer = getView().getSelectedProductContainer();
		if(selectedContainer == null){
			return;
		}
		
		ProductContainer curContainer = (ProductContainer)selectedContainer.getTag();
		if(curContainer == null){
			return;
		}
		
		Iterator<Product> products;
		
		if(selectedContainer.getName().equals("rootUnit")){
			Set<Product> allProducts = new TreeSet<Product>();
			products = productFacade.getProducts();
			while(products.hasNext()){
				allProducts.add(products.next());
			}
			products = allProducts.iterator();
		}
		else{
			products = containersFacade.getProducts(curContainer);
		}
		List<ProductData> productDataList = new ArrayList<ProductData>();
		while(products.hasNext()){
			Product curProduct = products.next();
			ProductData productData = new ProductData();
					productData.setBarcode(curProduct.getBarcode().getValue());
			if (selectedContainer.getName().equals("rootUnit")){
				productData.setCount("" + 
					productFacade.getItemCount(curProduct));
			}
			else {
				productData.setCount("" + 
					containersFacade.getItemCountInContainer(curContainer, curProduct));
			}
			productData.setDescription(curProduct.getProdDesc());
			productData.setShelfLife("" + curProduct.getShelfLifeString());
			productData.setSize("" + curProduct.getSize());
			productData.setSupply("" + curProduct.getMonthSupply());
			productData.setTag(curProduct);

			productDataList.add(productData);
			if(selectedProduct != null && selectedProduct.equals(productData)){
				selectedProduct = productData;
			}
		}

		getView().setProducts(productDataList.toArray(new ProductData[0]));
		getView().selectProductContainer(selectedContainer);
		getView().selectProduct(selectedProduct);
	}	
	
	private void loadItemsValues(){
		ProductContainerData curContainer = getView().getSelectedProductContainer();
		ProductData curProduct = getView().getSelectedProduct();
		if(curContainer == null || curProduct == null){
			getView().setItems(new ItemData[0]);
			return;
		}
		
		ProductContainer container = (ProductContainer)curContainer.getTag();
		Product product = (Product)curProduct.getTag();
		Iterator<Item> items;
		if(container.getName().equals("rootUnit")){
			items = productFacade.getItems(product);
		}
		else{
			items = containersFacade.getItemsOfProduct(container, product);
		}
		
		//sort the items
		List<Item> allItems = new ArrayList();
		while(items.hasNext()){
			allItems.add(items.next());
		}
		Collections.sort(allItems);
		items = allItems.iterator();
		
		List<ItemData> itemDataList = new ArrayList<ItemData>();	
		while(items.hasNext()){
			Item curItem = items.next();
			ItemData itemData = new ItemData();
			itemData.setBarcode(curItem.getBarcode().toString());
			itemData.setEntryDate(curItem.getEntryDate());
			itemData.setExpirationDate(curItem.getExpirationDate());
			if(curItem.getProductGroup() != null)
			{
				itemData.setProductGroup(curItem.getProductGroup().getName());
			}
			itemData.setStorageUnit
					(containersFacade.getStorageUnit(curItem.getProductContainer()).getName());
			itemData.setTag(curItem);

			itemDataList.add(itemData);
		}
		getView().setItems(itemDataList.toArray(new ItemData[0]));
	}
	
	private void loadContextView(){
		ProductContainerData selected = getView().getSelectedProductContainer();
		if(selected == null){
			return;
		}
		
		if(selected.getName().equals("rootUnit")){
			getView().setContextUnit("All");	
			getView().setContextGroup("");
			getView().setContextSupply("");	
		}
		else{
			ProductContainer container = (ProductContainer)selected.getTag();
			if(containersFacade.isStorageUnit(container)){
				getView().setContextUnit(container.getName());	
				getView().setContextGroup("");
				getView().setContextSupply("");
			}
			else{
				getView().setContextUnit(containersFacade.getStorageUnit(container).getName());
				getView().setContextGroup(container.getName());
				getView().setContextSupply
						(containersFacade.getThreeMonthSupply(container).toString());
			}
		}
	}
	
	private ProductContainerData generateData(ProductContainer container){
		ProductContainerData data = new ProductContainerData();
		data.setName(container.getName());
		data.setTag(container);
		
		Iterator<ProductContainer> children = containersFacade.getSubContainers(container);
		Set<ProductContainer> sorting = new TreeSet<ProductContainer>();
		while(children.hasNext()){
			sorting.add(children.next());
		}
		
		children = sorting.iterator();		
		while(children.hasNext()){
			ProductContainer cur = children.next();
			ProductContainerData childData = generateData(cur);
			data.addChild(childData);
		}
		
		return data;
	}
	
	/**
	 * Finds the index that the new child should be inserted into the parent
	 *
	 * @param parent	the parent container
	 * @param newChild	the new child
	 * @return	the index that the child must be put into to keep the children
	 * sorted
	 */
	private int getChildIndexAdd(ProductContainerData parent, ProductContainerData newChild) {
		int i;
		
		for (i = 0; i < parent.getChildCount(); i++) {
			ProductContainerData curChild = parent.getChild(i);
			if (newChild.getName().compareTo(curChild.getName()) < 0) {
				return i;
			}
		}

		return i;
	}
	
	/**
	 * Finds the index that the new child should be inserted into the parent
	 *
	 * @param newChild	the child with it's new name
	 * @return	the index that the child must be put into to keep the children
	 * sorted
	 */
	private int getChildIndexEdit(ProductContainer child) {
		int i = 0;
		
		ProductContainer parent = child.getParent();
		Iterator<ProductContainer> siblings = containersFacade.getSubContainers(parent);
		Set<ProductContainer> sorting = new TreeSet<ProductContainer>();
		while(siblings.hasNext()){
			sorting.add(siblings.next());
		}
		
		siblings = sorting.iterator();
		
		while(siblings.hasNext()){
			ProductContainer curSibling = siblings.next();
			if(curSibling.getName().equals(child.getName())){
				return i;
			}
			else{
				i++;
			}
		}

		return i;
	}
}

