package gui.main;

import gui.common.IController;


/**
 * Controller interface for the main view.  The main view allows the user
 * to print reports and exit the program.
 */
public interface IMainController extends IController {

	/**
	 * Returns true if and only if the "Exit" menu item should be enabled.
	 */
	boolean canExit();
	
	/**
	 * This method is called when the user exits the application.
	 */
	void exit();
	
	/**
	 * Returns true if and only if the "Expired Items" menu item should be enabled.
	 */
	boolean canPrintExpiredReport();
	
	/**
	 * This method is called when the user selects the "Expired Items" 
	 * menu item.
	 */
	void printExpiredReport();
	
	/**
	 * Returns true if and only if the "N-Month Supply" menu item should be enabled.
	 */
	boolean canPrintSupplyReport();
	
	/**
	 * This method is called when the user selects the "N-Month Supply" menu 
	 * item.
	 */
	void printSupplyReport();
	
	/**
	 * Returns true if and only if the "Removed Items" menu item should be enabled.
	 */
	boolean canPrintRemovedReport();
	
	/**
	 * This method is called when the user selects the "Removed Items" menu 
	 * item.
	 */
	void printRemovedReport();
	
	/**
	 * Returns true if and only if the "Product Statistics" menu item should be enabled.
	 */
	boolean canPrintProductReport();
	
	/**
	 * This method is called when the user selects the "Product Statistics" menu 
	 * item.
	 */
	void printProductReport();
	
	/**
	 * Returns true if and only if the "Notices" menu item should be enabled.
	 */
	boolean canPrintNoticesReport();
	
	/**
	 * This method is called when the user selects the "Notices" menu 
	 * item.
	 */
	void printNoticesReport();

}

