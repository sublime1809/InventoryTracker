package gui.product;

import gui.common.*;

/**
 * Controller interface for edit product view.
 */
public interface IEditProductController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * edit product view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit product view.
	 */
	void editProduct();

}

