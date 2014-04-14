/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public interface IItemDAO {
	
	/**
	 * adds an item into the item table
	 * @param item	the new item
	 */
	public boolean createItem(ItemDTO item);
	
	/**
	 * gets all the items in the database
	 * @return	an iterator with a dto for all the items in the database
	 */
	public Iterator<ItemDTO> getItems();
	
	/**
	 * updates an item in the database
	 * @param item	the item with new information
	 */
	public boolean updateItem(ItemDTO item);
	
	/**
	 * removes an item from the database
	 * @param item	the item to be removed
	 */
	public boolean removeItem(ItemDTO item);
	
	public Date getRemovedReportDate();
	
	public void updateRemovedReportDate(Date date);
}
