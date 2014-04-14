package gui.reports.expired;

import gui.common.*;

/**
 * Controller interface for the expired items report view.
 */
public interface IExpiredReportController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * expired items report view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the expired items report view.
	 */
	void display();
	
}

