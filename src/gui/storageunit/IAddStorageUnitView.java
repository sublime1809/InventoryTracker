package gui.storageunit;

import gui.common.*;

/**
 * View interface for the add storage unit view.
 */
public interface IAddStorageUnitView extends IView {

	/**
	 * Returns the value of the "Storage Unit Name" field.
	 */
	String getStorageUnitName();
	
	/**
	 * Sets the value of the "Storage Unit Name" field.
	 * 
	 * @param value New "Storage Unit Name" value
	 */
	void setStorageUnitName(String value);
	
	/**
	 * Sets the enable/disable state of the "Storage Unit Name" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableStorageUnitName(boolean value);
	
	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);

}

