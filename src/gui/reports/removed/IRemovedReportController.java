package gui.reports.removed;

import gui.common.*;

/**
 * Controller interface for the removed items report view.
 */
public interface IRemovedReportController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * removed items report view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the removed items report view.
	 */
	void display();
	
}

