package gui.storageunit;

import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class AddStorageUnitView extends StorageUnitView implements IAddStorageUnitView {

	public AddStorageUnitView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
		
		construct();		

		_controller = new AddStorageUnitController(this);
	}

	@Override
	public IAddStorageUnitController getController() {
		return (IAddStorageUnitController)super.getController();
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
		getController().addStorageUnit();
	}

}


