package gui.reports.expired;

import gui.common.*;

/**
 * View interface for the expired items report view.
 */
public interface IExpiredReportView extends IView {

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
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);

}

