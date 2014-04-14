package gui.inventory;

import gui.common.*;

import gui.item.*;
import gui.product.*;

/**
 * Controller interface for inventory view.
 */
public interface IInventoryController extends IController {

	/**
	 * Returns true if and only if the "Add Storage Unit" menu item should be enabled.
	 */
	boolean canAddStorageUnit();
	
	/**
	 * Returns true if and only if the "Add Items" menu item should be enabled.
	 */
	boolean canAddItems();
	
	/**
	 * Returns true if and only if the "Transfer Items" menu item should be enabled.
	 */
	boolean canTransferItems();
	
	/**
	 * Returns true if and only if the "Remove Items" menu item should be enabled.
	 */
	boolean canRemoveItems();
	
	/**
	 * Returns true if and only if the "Edit Storage Unit" menu item should be enabled.
	 */
	boolean canEditStorageUnit();
	
	/**
	 * Returns true if and only if the "Delete Storage Unit" menu item should be enabled.
	 */
	boolean canDeleteStorageUnit();
	
	/**
	 * Returns true if and only if the "Add Product Group" menu item should be enabled.
	 */
	boolean canAddProductGroup();
	
	/**
	 * Returns true if and only if the "Edit Product Group" menu item should be enabled.
	 */
	boolean canEditProductGroup();
	
	/**
	 * Returns true if and only if the "Delete Product Group" menu item should be enabled.
	 */
	boolean canDeleteProductGroup();
	
	/**
	 * Returns true if and only if the "Edit Product" menu item should be enabled.
	 */
	boolean canEditProduct();
	
	/**
	 * Returns true if and only if the "Delete Product" menu item should be enabled.
	 */
	boolean canDeleteProduct();
	
	/**
	 * Returns true if and only if the "Edit Item" menu item should be enabled.
	 */
	boolean canEditItem();
	
	/**
	 * Returns true if and only if the "Remove Item" menu item should be enabled.
	 */
	boolean canRemoveItem();
	
	/**
	 * This method is called when the user selects the "Add Storage Unit" menu item.
	 */
	void addStorageUnit();
	
	/**
	 * This method is called when the user selects the "Add Product Group" menu item.
	 */
	void addProductGroup();
	
	/**
	 * This method is called when the user selects the "Add Items" menu item.
	 */
	void addItems();
	
	/**
	 * This method is called when the user selects the "Transfer Items" menu item.
	 */
	void transferItems();
	
	/**
	 * This method is called when the user selects the "Remove Items" menu item.
	 */
	void removeItems();
	
	/**
	 * This method is called when the user selects the "Edit Storage Unit" menu item.
	 */
	void editStorageUnit();
	
	/**
	 * This method is called when the user selects the "Edit Product Group" menu item.
	 */
	void editProductGroup();
	
	/**
	 * This method is called when the user selects the "Edit Product" menu item.
	 */
	void editProduct();
	
	/**
	 * This method is called when the user selects the "Delete Storage Unit" menu item.
	 */
	void deleteStorageUnit();
	
	/**
	 * This method is called when the user selects the "Delete Product Group" menu item.
	 */
	void deleteProductGroup();
	
	/**
	 * This method is called when the user selects the "Delete Product" menu item.
	 */
	void deleteProduct();
	
	/**
	 * This method is called when the user selects the "Edit Item" menu item.
	 */
	void editItem();
	
	/**
	 * This method is called when the user selects the "Remove Item" menu item.
	 */
	void removeItem();
	
	/**
	 * This method is called when the selected product container changes.
	 */
	void productContainerSelectionChanged();

	/**
	 * This method is called when the selected product changes.
	 */
	void productSelectionChanged();
	
	/**
	 * This method is called when the selected item changes.
	 */
	void itemSelectionChanged();
	
	/**
	 * This method is called when the user drags a product into a
	 * product container.
	 * 
	 * @param product Product dragged into the target product container
	 * @param container Target product container
	 */
	void addProductToContainer(ProductData product, ProductContainerData container);
	
	/**
	 * This method is called when the user drags and item into
	 * a product container.
	 * 
	 * @param item Item dragged into the target product container
	 * @param container Target product container
	 */
	void moveItemToContainer(ItemData item, ProductContainerData container);
	
}

