package gui.batches;

import gui.common.IController;

/**
 * Controller interface for transfer item batch view.
 */
public interface ITransferItemBatchController extends IController {

	/**
	 * This method is called when the "Item Barcode" field in the
	 * transfer item batch view is changed by the user.
	 */
	void barcodeChanged();
	
	/**
	 * This method is called when the "Use Barcode Scanner" setting in the
	 * transfer item batch view is changed by the user.
	 */
	void useScannerChanged();
	
	/**
	 * This method is called when the selected product changes
	 * in the transfer item batch view.
	 */
	void selectedProductChanged();
	
	/**
	 * This method is called when the user clicks the "Transfer Item" button
	 * in the transfer item batch view.
	 */
	void transferItem();
	
	/**
	 * This method is called when the user clicks the "Undo" button
	 * in the transfer item batch view.
	 */
	void undo();
	
	/**
	 * This method is called when the user clicks the "Redo" button
	 * in the transfer item batch view.
	 */
	void redo();
	
	/**
	 * This method is called when the user clicks the "Done" button
	 * in the transfer item batch view.
	 */
	void done();

}

