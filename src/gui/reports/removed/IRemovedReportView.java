package gui.reports.removed;

import java.util.*;

import gui.common.*;

/**
 * View interface for the removed items report view.
 */
public interface IRemovedReportView extends IView {

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
	 * Returns the value of the "Since Last" radio button.
	 */
	boolean getSinceLast();
	
	/**
	 * Sets the value of the "Since Last" radio button.
	 * 
	 * @param value New "Since Last" value
	 */
	void setSinceLast(boolean value);
	
	/**
	 * Sets the enable/disable state of the "Since Last" radio button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableSinceLast(boolean value);
	
	/**
	 * Sets the value of the "Since Last Value" field.
	 * (This is the date/time at which the report was last run.)
	 * 
	 * @param value New "Since Last Value"
	 */
	void setSinceLastValue(Date value);
	
	/**
	 * Returns the value of the "Since Date" radio button.
	 */
	boolean getSinceDate();
	
	/**
	 * Sets the value of the "Since Date" radio button.
	 * 
	 * @param value New "Since Date" value
	 */
	void setSinceDate(boolean value);
	
	/**
	 * Sets the enable/disable state of the "Since Date" radio button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableSinceDate(boolean value);
	
	/**
	 * Returns the value of the "Since Date Value" field, or null if 
	 * the field's value is invalid.
	 * (This is the date/time entered by the user.)
	 */
	Date getSinceDateValue();
	
	/**
	 * Sets the value of the "Since Date Value" field.
	 * (This is the date/time entered by the user.)
	 * 
	 * @param value New "Since Date Value"
	 */
	void setSinceDateValue(Date value);
	
	/**
	 * Sets the enable/disable state of the "Since Date Value" field.
	 * (This is the date/time entered by the user.)
	 * 
	 * @param value New enable/disable value
	 */
	void enableSinceDateValue(boolean value);
	
	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);
	
}

