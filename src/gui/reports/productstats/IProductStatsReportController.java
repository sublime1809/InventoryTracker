package gui.reports.productstats;

import gui.common.*;

/**
 * Controller interface for the product statistics report view.
 */
public interface IProductStatsReportController extends IController {

	/**
	 * This method is called when any of the fields in the
	 * product statistics report view is changed by the user.
	 */
	void valuesChanged();
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the product statistics report view.
	 */
	void display();
	
}

