package gui.main;

import gui.common.*;


/**
 * View interface for the main view.  The main view allows the user to
 * print reports and exit the program.
 */
public interface IMainView extends IView {

	/**
	 * Displays the expired items report view, which allows the user to print
	 * the expired items report.
	 */
	void displayExpiredReportView();
	
	/**
	 * Displays the N-month supply report view, which allows the user to print
	 * the N-month supply report.
	 */
	void displaySupplyReportView();
	
	/**
	 * Displays the removed items report view, which allows the user to print
	 * the removed items report.
	 */
	void displayRemovedReportView();
	
	/**
	 * Displays the product statistics report view, which allows the user to print
	 * the product statistics report.
	 */
	void displayProductReportView();
	
	/**
	 * Displays the notices report view, which allows the user to print
	 * the notices report.
	 */
	void displayNoticesReportView();

}

