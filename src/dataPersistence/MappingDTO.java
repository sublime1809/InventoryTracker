/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

/**
 *
 * @author Stephen Kitto
 */
public class MappingDTO {
	
	private int ProductID;
	private int ContainerID;

	/**
	 * constructor: accepts values for all fields in this DTO
	 * @param ProductID
	 * @param ContainerID 
	 */
	public MappingDTO(int ProductID, int ContainerID) {
		this.ProductID = ProductID;
		this.ContainerID = ContainerID;
	}

	/**
	 * returns the id of the product
	 * @return the id of the product
	 */
	public int getProductID() {
		return ProductID;
	}

	/**
	 * sets the id of the product
	 * @param ProductID		the new id
	 */
	public void setProductID(int ProductID) {
		this.ProductID = ProductID;
	}

	/**
	 * returns the id of the container
	 * @return the id of the container
	 */
	public int getContainerID() {
		return ContainerID;
	}

	/**
	 * sets the id of the container
	 * @param ContainerID	the new id
	 */
	public void setContainerID(int ContainerID) {
		this.ContainerID = ContainerID;
	}
}
