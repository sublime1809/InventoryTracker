package gui.reports.supply;

import gui.common.*;

/**
 * View interface for the N-month supply report view.
 */
public interface ISupplyReportView extends IView {

	/**
	 * Returns the value of the "Format" field.
	 */
	FileFormat getFormat();
	
	/**
	 * Sets the value of the "Format" field.
	 * 
	 * @param value New "Format" value
	 */
	void setFormat(FileFormat value);
	
	/**
	 * Sets the enable/disable state of the "Format" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableFormat(boolean value);
	
	/**
	 * Returns the value of the "Months" field.
	 */
	String getMonths();
	
	/**
	 * Sets the value of the "Months" field.
	 * 
	 * @param value New "Months" value
	 */
	void setMonths(String value);
	
	/**
	 * Sets the enable/disable state of the "Months" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableMonths(boolean value);

	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);

}

