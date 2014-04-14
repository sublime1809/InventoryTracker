/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.sql.Connection;

/**
 *
 * @author Stephen Kitto
 */
public interface IFactory {
	
	/**
	 * Gets the ItemDAO that is currently being used by the system now
	 * @return the current itemdao
	 */
	public IItemDAO getItemDAO();
	
	/**
	 * Gets the ProductDAO that is currently being used by the system now
	 * @return the current productdao
	 */
	public IProductDAO getProductDAO();
	
	/**
	 * Gets the ContainerDAO that is currently being used by the system now
	 * @return the current containerdao
	 */	
	public IContainerDAO getContainerDAO();
	
	/**
	 * saves the entire model
	 */
	public void saveModel();
	
	/**
	 * loads the entire model
	 */
	public void loadModel();
	
	/**
	 * starts a transaction
	 */
	public void startTransaction();
	
	/**
	 * finishes a transaction, commits or rollback changes
	 */
	public void finishTransaction(boolean commit);
	
	/**
	 * gets the connection being used by the system
	 * @return the connection being used by the system
	 */
	public Connection getConnection();
}
