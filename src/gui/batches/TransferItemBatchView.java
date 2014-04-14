package gui.batches;

import gui.common.*;
import gui.inventory.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class TransferItemBatchView extends ItemBatchView implements ITransferItemBatchView {

	@SuppressWarnings("unused")
	private ProductContainerData _target;
	
	public TransferItemBatchView(GUI parent, DialogBox dialog, ProductContainerData target) {
		super(parent, dialog);
	
		_target = target;
		
		construct();

		_controller = new TransferItemBatchController(this, target);
	}

	////////////////////////////
	// ItemBatchView Overrides
	////////////////////////////

	@Override
	protected String getBarcodeLabel() {
		return "Item Barcode:";
	}

	@Override
	public ITransferItemBatchController getController() {
		return (ITransferItemBatchController)super.getController();
	}
	
	@Override
	protected void done() {
		getController().done();
	}

	@Override
	protected void itemAction() {
		getController().transferItem();
	}

	@Override
	protected String getItemActionName() {
		return "Transfer Item";
	}
	
	@Override
	protected void barcodeChanged() {
		getController().barcodeChanged();
	}
	
	@Override
	protected void useScannerChanged() {
		getController().useScannerChanged();
	}

	@Override
	protected void selectedProductChanged() {
		getController().selectedProductChanged();
	}

	@Override
	protected void selectedItemChanged() {
		return;
	}
	
	@Override
	protected void redo() {
		getController().redo();
	}

	@Override
	protected void undo() {
		getController().undo();
	}

}

