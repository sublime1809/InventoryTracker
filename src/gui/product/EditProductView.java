package gui.product;

import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class EditProductView extends ProductView implements IEditProductView {

	public EditProductView(GUI parent, DialogBox dialog, ProductData target) {
		super(parent, dialog);

		construct();		

		_controller = new EditProductController(this, target);
	}

	@Override
	public IEditProductController getController() {
		return (IEditProductController)super.getController();
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
		getController().editProduct();
	}

}

