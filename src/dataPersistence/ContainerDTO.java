/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

/**
 *
 * @author Stephen Kitto
 */
public class ContainerDTO {
	
	private int ID;
	private String name;
	private int parentID;
	private String supplyUnit;
	private float supplyValue;

	/**
	 * constructor: accepts a value for each field
	 * @param ID
	 * @param name
	 * @param parentID
	 * @param supplyUnit
	 * @param supplyValue 
	 */
	public ContainerDTO(int ID, String name, int parentID, String supplyUnit, float supplyValue) {
		this.ID = ID;
		this.name = name;
		this.parentID = parentID;
		this.supplyUnit = supplyUnit;
		this.supplyValue = supplyValue;
	}

	/**
	 * returns the id of this container
	 * @return the id of this container
	 */
	public int getID() {
		return ID;
	}

	/**
	 * sets a new value for the id
	 * @param ID	the new value for id
	 */
	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * returns the name of the container
	 * @return the name of the container
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets a new name
	 * @param name	the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * returns the id of the container that contains this container
	 * @return the id of the container that contains this container
	 */
	public int getParentID() {
		return parentID;
	}

	/**
	 * sets the id of the container that contains this one
	 * @param parentID	the new id for the parent
	 */
	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	/**
	 * returns the unit of the three month supply
	 * @return the unit of the three month supply
	 */
	public String getSupplyUnit() {
		return supplyUnit;
	}

	/**
	 * sets a new value for the unit of the three month supply
	 * @param supplyUnit	the new value for the three months supply unit
	 */
	public void setSupplyUnit(String supplyUnit) {
		this.supplyUnit = supplyUnit;
	}

	/**
	 * returns the value for three month supply
	 * @return the value for three month supply
	 */
	public float getSupplyValue() {
		return supplyValue;
	}

	/**
	 * sets the value of the three month supply
	 * @param supplyValue	the new value
	 */
	public void setSupplyValue(int supplyValue) {
		this.supplyValue = supplyValue;
	}
	
	
}
