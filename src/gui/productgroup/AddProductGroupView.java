package gui.productgroup;

import gui.common.*;
import gui.inventory.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class AddProductGroupView extends ProductGroupView implements IAddProductGroupView {

	public AddProductGroupView(GUI parent, DialogBox dialog, ProductContainerData container) {
		super(parent, dialog);
		
		construct();		

		_controller = new AddProductGroupController(this, container);
	}

	@Override
	public IAddProductGroupController getController() {
		return (IAddProductGroupController)super.getController();
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
		getController().addProductGroup();
	}

}


