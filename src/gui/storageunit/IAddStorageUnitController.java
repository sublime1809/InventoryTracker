package gui.storageunit;

import gui.common.IController;

/**
 * Controller interface for add storage unit view.
 */
public interface IAddStorageUnitController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * add storage unit view is changed by the user.
	 */
	void valuesChanged();

	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add storage unit view.
	 */
	void addStorageUnit();

}

