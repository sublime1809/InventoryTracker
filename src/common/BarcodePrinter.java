/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import application.item.Item;
import common.exceptions.InvalidItemException;
import java.util.Set;

/**
 *
 * @author Stephen Kitto
 */
public class BarcodePrinter {
	
	/**
	 * Constructor
	 */
	public BarcodePrinter(){
		
	}
	
	/**
	 * Prints out a barcode for all of the items given
	 * 
	 * @param items			The items that need a barcode
	 * @throws InvalidItemException	  if any of the items passed in are invalid
	 */
	public void printBarcodes(Set<Item> items) throws InvalidItemException{
		
	}
}
