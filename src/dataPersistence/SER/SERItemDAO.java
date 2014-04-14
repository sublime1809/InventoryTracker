/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SER;

import dataPersistence.IItemDAO;
import dataPersistence.ItemDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public class SERItemDAO implements IItemDAO, Serializable{
	
	/**
	 * does nothing
	 */
	@Override
	public boolean createItem(ItemDTO item){
		return true;
	}
	
	/**
	 * does nothing
	 */
	@Override
	public Iterator<ItemDTO> getItems(){
		return null;
	}
	
	/**
	 * does nothing
	 */
	@Override
	public boolean updateItem(ItemDTO item){
		return true;
	}
	
	/**
	 * does nothing
	 */
	@Override
	public boolean removeItem(ItemDTO item){
		return true;
	}

	@Override
	public Date getRemovedReportDate() {
		return new Date();
	}

	@Override
	public void updateRemovedReportDate(Date date) {
		
	}
	
}
