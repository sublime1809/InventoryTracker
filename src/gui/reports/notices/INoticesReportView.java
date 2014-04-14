package gui.reports.notices;

import gui.common.*;

/**
 * View interface for the notices report view.
 */
public interface INoticesReportView extends IView {

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

