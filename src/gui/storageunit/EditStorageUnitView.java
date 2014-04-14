package gui.storageunit;

import gui.common.*;
import gui.inventory.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class EditStorageUnitView extends StorageUnitView implements IEditStorageUnitView {

	public EditStorageUnitView(GUI parent, DialogBox dialog, ProductContainerData target) {
		super(parent, dialog);

		construct();		

		_controller = new EditStorageUnitController(this, target);
	}

	@Override
	public IEditStorageUnitController getController() {
		return (IEditStorageUnitController)super.getController();
	}

	@Override
	protected void valuesChanged() {
		getController().valuesChanged();
	}

	@Override
	protected void cancel() {
		return;
	}

	@Override
	protected void ok() {
		getController().editStorageUnit();
	}

}


