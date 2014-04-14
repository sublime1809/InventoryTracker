package gui.batches;

import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class RemoveItemBatchView extends ItemBatchView implements IRemoveItemBatchView {
	
	public RemoveItemBatchView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
		
		construct();

		_controller = new RemoveItemBatchController(this);
	}

	////////////////////////////
	// ItemBatchView Overrides
	////////////////////////////

	@Override
	protected String getBarcodeLabel() {
		return "Item Barcode:";
	}
	
	@Override
	public IRemoveItemBatchController getController() {
		return (IRemoveItemBatchController)super.getController();
	}
	
	@Override
	protected void done() {
		getController().done();
	}

	@Override
	protected void itemAction() {
		getController().removeItem();
	}

	@Override
	protected String getItemActionName() {
		return "Remove Item";
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

