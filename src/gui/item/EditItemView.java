package gui.item;

import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class EditItemView extends ItemView implements IEditItemView {

	public EditItemView(GUI parent, DialogBox dialog, ItemData target) {
		super(parent, dialog);

		construct();		

		_controller = new EditItemController(this, target);
	}

	@Override
	public IEditItemController getController() {
		return (IEditItemController)super.getController();
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
		getController().editItem();
	}

}

