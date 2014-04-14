package gui.product;

import gui.common.*;

/**
 * View interface for the add product view.
 */
public interface IAddProductView extends IView {

	/**
	 * Returns the value of the "Product Barcode" field.
	 */
	String getBarcode();
	
	/**
	 * Sets the value of the "Product Barcode" field.
	 * 
	 * @param value New "Product Barcode" value
	 */
	void setBarcode(String value);
	
	/**
	 * Sets the enable/disable state of the "Product Barcode" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableBarcode(boolean value);

	/**
	 * Returns the value of the "Description" field.
	 */
	String getDescription();
	
	/**
	 * Sets the value of the "Description" field.
	 * 
	 * @param value New "Description" value
	 */
	void setDescription(String value);
	
	/**
	 * Sets the enable/disable state of the "Description" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableDescription(boolean value);

	/**
	 * Returns the value of the "Size Value" field.
	 */
	String getSizeValue();
	
	/**
	 * Sets the value of the "Size Value" field.
	 * 
	 * @param value New "Size Value"
	 */
	void setSizeValue(String value);
	
	/**
	 * Sets the enable/disable state of the "Size Value" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableSizeValue(boolean value);
	
	/**
	 * Returns the value of the "Size Unit" field.
	 */
	SizeUnits getSizeUnit();
	
	/**
	 * Sets the value of the "Size Unit" field.
	 * 
	 * @param value New "Size Unit" value
	 */
	void setSizeUnit(SizeUnits value);
	
	/**
	 * Sets the enable/disable state of the "Size Unit" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableSizeUnit(boolean value);
	
	/**
	 * Returns the value of the "Shelf Life" field.
	 */
	String getShelfLife();
	
	/**
	 * Sets the value of the "Shelf Life" field.
	 * 
	 * @param value New "Shelf Life" value
	 */
	void setShelfLife(String value);
	
	/**
	 * Sets the enable/disable state of the "Shelf Life" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableShelfLife(boolean value);
	
	/**
	 * Returns the value of the "3-Month Supply" field.
	 */
	String getSupply();
	
	/**
	 * Sets the value of the "3-Month Supply" field.
	 * 
	 * @param value New "3-Month Supply" value
	 */
	void setSupply(String value);
	
	/**
	 * Sets the enable/disable state of the "3-Month Supply" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableSupply(boolean value);
	
	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);

}

