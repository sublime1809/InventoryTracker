package gui.batches;

import gui.common.IController;

/**
 * Controller interface for add item batch view.
 */
public interface IAddItemBatchController extends IController {

	/**
	 * This method is called when the "Entry Date" field in the
	 * add item batch view is changed by the user.
	 */
	void entryDateChanged();
	
	/**
	 * This method is called when the "Count" field in the
	 * add item batch view is changed by the user.
	 */
	void countChanged();
	
	/**
	 * This method is called when the "Product Barcode" field in the
	 * add item batch view is changed by the user.
	 */
	void barcodeChanged();
	
	/**
	 * This method is called when the "Use Barcode Scanner" setting in the
	 * add item batch view is changed by the user.
	 */
	void useScannerChanged();
	
	/**
	 * This method is called when the selected product changes
	 * in the add item batch view.
	 */
	void selectedProductChanged();
	
	/**
	 * This method is called when the user clicks the "Add Item" button
	 * in the add item batch view.
	 */
	void addItem();
	
	/**
	 * This method is called when the user clicks the "Undo" button
	 * in the add item batch view.
	 */
	void undo();
	
	/**
	 * This method is called when the user clicks the "Redo" button
	 * in the add item batch view.
	 */
	void redo();
	
	/**
	 * This method is called when the user clicks the "Done" button
	 * in the add item batch view.
	 */
	void done();

}

