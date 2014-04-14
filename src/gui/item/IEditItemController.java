package gui.item;

import gui.common.*;

/**
 * Controller interface for edit item view.
 */
public interface IEditItemController extends IController {
	
	/**
	 * This method is called when any of the fields in the
	 * edit item view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit item view.
	 */
	void editItem();

}

