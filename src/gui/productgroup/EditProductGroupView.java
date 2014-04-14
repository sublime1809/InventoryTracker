package gui.productgroup;

import gui.common.*;
import gui.inventory.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class EditProductGroupView extends ProductGroupView implements IEditProductGroupView {

	public EditProductGroupView(GUI parent, DialogBox dialog, ProductContainerData target) {
		super(parent, dialog);

		construct();		

		_controller = new EditProductGroupController(this, target);
	}

	@Override
	public IEditProductGroupController getController() {
		return (IEditProductGroupController)super.getController();
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
		getController().editProductGroup();
	}

}


