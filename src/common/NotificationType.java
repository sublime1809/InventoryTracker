/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 * Enumeration for the types of notifications that can be passed from an Observable 
 *	class to an observer class
 * 
 * @author Stephen Kitto
 */
public enum NotificationType {
	
	//notifications about items
	ItemsAdded("ItemsAdded"),
	ItemsRemoved("ItemsRemoved"),
	ItemsChanged("ItemsChanged"),
	//notifications about products
	ProductAdded("ProductsAdded"),
	ProductRemoved("ProductsRemoved"),
	ProductChanged("ProductsChanged"),
	ProductMoved("ProductsMoved"),
	//notifications about containers
	ContainerAdded("ContainersAdded"),
	ContainerRemoved("ContainersRemoved"),
	ContainerChanged("ContainersChanged");
	
	/**
	 * String to be returned by to string
	 */
	private String _string;
	
	/**
	 * constructor
	 * 
	 * @param s		String value to be returned by to string
	 */
	private NotificationType(String s){
		_string = s;
	}
	
	/**
	 * returns a string to represent the object
	 * @return		a string representing the NotificationType
	 */
	@Override
	public String toString(){
		return _string;
	}
	
}
