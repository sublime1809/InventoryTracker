/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.util.Date;

/**
 *
 * @author Stephen Kitto
 */
public class ItemDTO {
	
	private int ID;
	private String barcode;
	private Date entryDate;
	private Date exitDate;
	private int productID;
	private int containerID;

	/**
	 * Constructor: accepts all fields in this item
	 * 
	 * @param ID
	 * @param barcode
	 * @param entryDate
	 * @param exitDate
	 * @param productID
	 * @param containerID 
	 */
	public ItemDTO
			(int ID, String barcode, Date entryDate, Date exitDate, int productID, int containerID){
		this.ID = ID;
		this.barcode = barcode;
		this.entryDate = entryDate;
		this.exitDate = exitDate;
		this.productID = productID;
		this.containerID = containerID;
	}

	/**
	 * returns the id of the item
	 * @return	the id of the item
	 */
	public int getID() {
		return ID;
	}

	/**
	 * sets the id of the item to the value given
	 * @param ID	the new value of id
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * returns the barcode of the item
	 * @return the barcode of the item
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * sets the barcode of the item to the value given
	 * @param barcode	the new barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * returns the entry date of the item
	 * @return the entry date of the item
	 */
	public Date getEntryDate() {
		return entryDate;
	}

	/**
	 * sets the entry date to the given date
	 * @param entryDate		the new entry date
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * returns the exit date of the item
	 * @return the exit date of the item
	 */
	public Date getExitDate() {
		return exitDate;
	}

	/**
	 * sets the exit date of the item
	 * @param exitDate	the new entry date
	 */
	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	/**
	 * returns the id of the product that this item is a member of
	 * @return the id of this item's product
	 */
	public int getProductID() {
		return productID;
	}

	/**
	 * sets the id of this item's product
	 * @param productID		the new value of the product id
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}

	/**
	 * returns the id of the container that contains this item
	 * @return the id of the container that contains this item
	 */
	public int getContainerID() {
		return containerID;
	}

	/**
	 * sets the value of the container that contains this item
	 * @param containerID new value for containerID
	 */
	public void setContainerID(int containerID) {
		this.containerID = containerID;
	}
}
