package gui.inventory;

import gui.common.*;
import gui.item.ItemData;
import gui.product.*;

/**
 * View interface for inventory view.
 */
public interface IInventoryView extends IView {

	/**
	 * Sets the contents of the product container tree.
	 * 
	 * @param root Hierarchy of product containers to be displayed
	 */
	void setProductContainers(ProductContainerData root);

	/**
	 * Inserts a new product container into the product container tree.
	 * 
	 * @param parent Parent of new product container
	 * @param newContainer Product container being inserted
	 * @param index Zero-based index at which newContainer will be
	 * inserted under parent
	 */
	void insertProductContainer(ProductContainerData parent, 
								ProductContainerData newContainer, int index);

	/**
	 * Selects the specified product container in the product container tree.
	 * 
	 * @param container Product container to be selected.  Must be one of the
	 * product containers currently in the product container tree.
	 */
	void selectProductContainer(ProductContainerData container);
	
	/**
	 * Returns the selected product container, or null if no product
	 * container is selected.
	 */
	ProductContainerData getSelectedProductContainer();
	
	/**
	 * Deletes the specified product container and its descendants
	 * from the product container tree.
	 * 
	 * @param container Product container to be deleted
	 */
	void deleteProductContainer(ProductContainerData container);
	
	/**
	 * Renames a product container that is already in the product
	 * container tree.  (Because product containers are sorted
	 * alphabetically, renaming a container might move it to
	 * a different position in its parent's child list.)
	 * 
	 * @param renamedContainer Product container being renamed
	 * @param newName New name for product container
	 * @param newIndex New index of renamed product container in its parent's child list
	 */
	void renameProductContainer(ProductContainerData renamedContainer, 
								String newName, int newIndex);

	/**
	 * Sets the "UNIT" context field.
	 * 
	 * @param value New "UNIT" value
	 */
	void setContextUnit(String value);
	
	/**
	 * Sets the "GROUP" context field.
	 * 
	 * @param value New "GROUP" value
	 */
	void setContextGroup(String value);

	/**
	 * Sets the "3-MONTH SUPPLY" context field.
	 * 
	 * @param value New "3-MONTH SUPPLY" value
	 */
	void setContextSupply(String value);
	
	/**
	 * Sets the products displayed in the "Products" table.
	 * 
	 * @param products Array of products to display
	 */
	void setProducts(ProductData[] products);
	
	/**
	 * Returns the currently selected product in the "Products" table,
	 * or null if no product is selected.
	 */
	ProductData getSelectedProduct();
	
	/**
	 * Selects the specified product in the "Products" table, or selects
	 * nothing if product is null.
	 * 
	 * @param product The product to be selected.  This must be one of the products
	 * previously passed to setProducts
	 * , or null.
	 */
	void selectProduct(ProductData product);
	
	/**
	 * Sets the items displayed in the "Items" table.
	 * 
	 * @param items Array of items to display
	 */
	void setItems(ItemData[] items);
	
	/**
	 * Returns the currently selected item in the "Item" table,
	 * or null if no item is selected.
	 */
	ItemData getSelectedItem();
	
	/**
	 * Selects the specified item in the "Items" table, or
	 * selects nothing if item is null.
	 *  
	 * @param item The item to be selected.  This must be one
	 * of the items previously passed to setItems, or null.
	 */
	void selectItem(ItemData item);
	
	/**
	 * Displays the add storage unit view.
	 */
	void displayAddStorageUnitView();
	
	/**
	 * Displays the add product group view.
	 */
	void displayAddProductGroupView();
	
	/**
	 * Displays the add item batch view.
	 */
	void displayAddItemBatchView();
	
	/**
	 * Displays the transfer item batch view.
	 */
	void displayTransferItemBatchView();
	
	/**
	 * Displays the remove item batch view.
	 */
	void displayRemoveItemBatchView();
	
	/**
	 * Displays the edit storage unit view.
	 */
	void displayEditStorageUnitView();
	
	/**
	 * Displays the edit product group view.
	 */
	void displayEditProductGroupView();
	
	/**
	 * Displays the edit product view.
	 */
	void displayEditProductView();
	
	/**
	 * Displays the edit item view.
	 */
	void displayEditItemView();
	
}

