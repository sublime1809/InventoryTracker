/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import dataPersistence.DTO.DTOBase;

/**
 *
 * @author Stephen Kitto
 */
public class ProductDTO extends DTOBase {
	
	private String description;
	private String barcode;
	private int shelflife;
	private float sizeValue;
	private String sizeUnit;
	private float threeMonthSupply;

	/**
	 * Constructor: accepts values for all fields in the class
	 * @param ID
	 * @param description
	 * @param barcode
	 * @param shelflife
	 * @param sizeValue
	 * @param sizeUnit
	 * @param threeMonthSupply 
	 */
	public ProductDTO(int ID, String description, String barcode, 
			int shelflife, float sizeValue, String sizeUnit, float threeMonthSupply) {
		super.setID(ID);
		this.description = description;
		this.barcode = barcode;
		this.shelflife = shelflife;
		this.sizeValue = sizeValue;
		this.sizeUnit = sizeUnit;
		this.threeMonthSupply = threeMonthSupply;
	}

	/**
	 * returns the description of this product
	 * @return the description of this product
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * sets the description of this DTO
	 * @param description	the new value for description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * returns the barcode of this product
	 * @return the barcode of this product
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * sets the value of the barcode for this DTO
	 * @param barcode	the new barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * returns the shelflife
	 * @return the shelflife
	 */
	public int getShelflife() {
		return shelflife;
	}

	/**
	 * sets the value for the shelflife
	 * @param shelflife		the new value for shelflife
	 */
	public void setShelflife(int shelflife) {
		this.shelflife = shelflife;
	}

	/**
	 * returns the value of size for this product
	 * @return the value of size for this product
	 */
	public float getSizeValue() {
		return sizeValue;
	}

	/**
	 * sets a new value for size
	 * @param sizeValue		the new value
	 */
	public void setSizeValue(int sizeValue) {
		this.sizeValue = sizeValue;
	}

	/**
	 * returns the unit for size
	 * @return the unit for size
	 */
	public String getSizeUnit() {
		return sizeUnit;
	}

	/**
	 * sets a new value for the unit for size
	 * @param sizeUnit the new value the unit of size
	 */
	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	/**
	 * returns the three month supply
	 * @return the three month supply
	 */
	public float getThreeMonthSupply() {
		return threeMonthSupply;
	}

	/**
	 * sets a new value for the three month supply
	 * @param threeMonthSupply	the new value
	 */
	public void setThreeMonthSupply(int threeMonthSupply) {
		this.threeMonthSupply = threeMonthSupply;
	}
        
        @Override
        public String toString() {
            return this.barcode + " : " + this.description;
        }
}
