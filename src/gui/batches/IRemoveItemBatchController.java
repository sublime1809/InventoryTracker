package gui.batches;

import gui.common.IController;

/**
 * Controller interface for remove item batch view.
 */
public interface IRemoveItemBatchController extends IController {

	/**
	 * This method is called when the "Item Barcode" field is changed
	 * in the remove item batch view by the user.
	 */
	void barcodeChanged();
	
	/**
	 * This method is called when the "Use Barcode Scanner" setting is changed
	 * in the remove item batch view by the user.
	 */
	void useScannerChanged();
	
	/**
	 * This method is called when the selected product changes
	 * in the remove item batch view.
	 */
	void selectedProductChanged();
	
	/**
	 * This method is called when the user clicks the "Remove Item" button
	 * in the remove item batch view.
	 */
	void removeItem();
	
	/**
	 * This method is called when the user clicks the "Undo" button
	 * in the remove item batch view.
	 */
	void undo();
	
	/**
	 * This method is called when the user clicks the "Redo" button
	 * in the remove item batch view.
	 */
	void redo();
	
	/**
	 * This method is called when the user clicks the "Done" button
	 * in the remove item batch view.
	 */
	void done();

}

