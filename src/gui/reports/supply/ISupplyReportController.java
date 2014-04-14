package gui.reports.supply;

import gui.common.*;

/**
 * Controller interface for the N-month supply report view.
 */
public interface ISupplyReportController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * N-month supply report view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the N-month supply report view.
	 */
	void display();
	
}

