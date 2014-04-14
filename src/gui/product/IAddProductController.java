package gui.product;

import gui.common.*;

/**
 * Controller interface for add product view.
 */
public interface IAddProductController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * add product view is changed by the user.
	 */
	void valuesChanged();

	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add product view.
	 */
	void addProduct();

}

