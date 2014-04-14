/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import dataPersistence.SER.SERFactory;
import dataPersistence.SQL.SQLFactory;
import java.io.Serializable;
import java.sql.Connection;

/**
 *
 * @author Stephen Kitto
 */
public class PersistenceManager implements Serializable
{

	private static PersistenceManager instance = null;
	private IFactory factory;
	private Connection connection = null;

	/**
	 * returns the singleton instance of this class
	 *
	 * @return the one and only instance of this class
	 */
	public static PersistenceManager getInstance()
	{
		if(instance == null)
		{
			instance = new PersistenceManager();
		}
		
		return instance;
	}

	private PersistenceManager()
	{
		//in the event that the user does not latter specify which type he wants, do the default
		makeNewFactory("");
	}

	/**
	 * returns the itemdao being used by the system
	 *
	 * @return the itemdao being used by the system
	 */
	public IItemDAO getItemDAO()
	{
		return factory.getItemDAO();
	}

	/**
	 * returns the productdao being used by the system
	 *
	 * @return the productdao being used by the system
	 */
	public IProductDAO getProductDAO()
	{
		return factory.getProductDAO();
	}

	/**
	 * returns the containerdao being used by the system
	 *
	 * @return the containerdao being used by the system
	 */
	public IContainerDAO getContainerDAO()
	{
		return factory.getContainerDAO();
	}

	/**
	 * returns the connection being used by the system
	 *
	 * @return the connection being used by the system
	 */
	public Connection getConnection()
	{
		return factory.getConnection();
	}

	/**
	 * starts a transaction
	 */
	public void startTransaction()
	{
		factory.startTransaction();
	}

	/**
	 * finish the current transaction (commit changes, undo them)
	 */
	public void finishTransaction(boolean commit)
	{
		factory.finishTransaction(commit);
	}

	/**
	 * creates a new factory based on the type given
	 *
	 * @param type the type of factory that should be used
	 */
	public void makeNewFactory(String type)
	{
		if(type.equals("-sql")){
			this.factory = new SQLFactory();
		}
		else{
			this.factory = new SERFactory();			
		}
	}
	
	/**
	 * saves the entire model
	 */
	public void saveModel(){
		factory.saveModel();
	}
	
	/**
	 * loads the entire model
	 */
	public void loadModel(){
		factory.loadModel();
	}
}
