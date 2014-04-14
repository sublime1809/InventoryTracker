/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SER;

import dataPersistence.IContainerDAO;
import dataPersistence.IFactory;
import dataPersistence.IItemDAO;
import dataPersistence.IProductDAO;
import java.io.Serializable;
import java.sql.Connection;

/**
 *
 * @author Stephen Kitto
 */
public class SERFactory implements IFactory, Serializable{
	
	private IItemDAO itemDAO;
	private IProductDAO productDAO;
	private IContainerDAO containerDAO;
	
	public SERFactory(){
		itemDAO = new SERItemDAO();
		productDAO = new SERProductDAO();
		containerDAO = new SERContainerDAO();
	}
	
	/**
	 * Gets the ItemDAO that is currently being used by the system now
	 * @return the current itemdao
	 */
	@Override
	public IItemDAO getItemDAO(){
		return itemDAO;
	}
	
	/**
	 * Gets the ProductDAO that is currently being used by the system now
	 * @return the current productdao
	 */
	@Override
	public IProductDAO getProductDAO(){
		return productDAO;
	}
	
	/**
	 * Gets the ContainerDAO that is currently being used by the system now
	 * @return the current containerdao
	 */	
	@Override
	public IContainerDAO getContainerDAO(){
		return containerDAO;
	}
	
	/**
	 * saves the entire model
	 */
	@Override
	public void saveModel(){
		SerializableDatabase.saveApplication();		
	}
	
	/**
	 * loads the entire model
	 */
	@Override
	public void loadModel(){
		SerializableDatabase.loadApplication();	
	}
	
	/**
	 * does nothing (no transactions are used in serialization)
	 */
	@Override
	public void startTransaction(){
		
	}
	
	/**
	 * does nothing (no transactions are used in serialization)
	 */
	@Override
	public void finishTransaction(boolean commit){
		
	}
	
	/**
	 * returns null (no connections are used in serialization)
	 */
	@Override
	public Connection getConnection(){
		return null;
	}	
}
