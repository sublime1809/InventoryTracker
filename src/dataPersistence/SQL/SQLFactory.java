/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import application.storage.RemovedItemsManager;
import dataPersistence.ContainerDTO;
import dataPersistence.IContainerDAO;
import dataPersistence.IFactory;
import dataPersistence.IItemDAO;
import dataPersistence.IProductDAO;
import dataPersistence.ItemDTO;
import dataPersistence.MappingDTO;
import dataPersistence.ModelLoader;
import dataPersistence.ProductDTO;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stephen Kitto
 */
public class SQLFactory implements IFactory{
	
	private IItemDAO itemDAO;
	private IProductDAO productDAO;
	private IContainerDAO containerDAO;
	private Connection connection;
	
	public SQLFactory(){
		try
		{
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch (ClassNotFoundException e)
		{
			// ERROR! Could not load database driver
			//System.out.println("hey you");
		}
                itemDAO = new SQLItemDAO();
                productDAO = new SQLProductDAO();
                containerDAO = new SQLContainerDAO();
		createDataBase();
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
		
	}
	
	/**
	 * loads the entire model
	 */
	@Override
	public void loadModel(){
		//System.out.println("loading the model sql style");
		
		startTransaction();
		ModelLoader modelLoader = new ModelLoader();
		
		loadContainers(modelLoader);
		loadProducts(modelLoader);
		loadMappings(modelLoader);
		loadItems(modelLoader);
		
		//load date the removed items report was most recently run
		Date lastReport = itemDAO.getRemovedReportDate();
		if(lastReport == null){
			lastReport = new GregorianCalendar (2000, 0, 1).getTime();
		}
		RemovedItemsManager.getInstance().setLastReportRun(lastReport);

		finishTransaction(true);
	}
	
	/**
	 * starts a transaction
	 */
	@Override
	public void startTransaction(){
		String dbName = "savedData" + File.separator + "HIT.sqlite";
		String connectionURL = "jdbc:sqlite:" + dbName;
		try
		{
			// Open a database connection
			connection = DriverManager.getConnection(connectionURL);
                        
			// Start a transaction
			connection.setAutoCommit(false);
		}
		catch (SQLException e)
		{
			// ERROR
                        //System.out.println("SQL error, the data base might not have been found");
		}		
	}
	
	/**
	 * finishes a transaction, commits or rollback changes
	 */
	@Override
	public void finishTransaction(boolean commit){
		if(commit)
		{
			try
			{
				connection.commit();
			}
			catch (SQLException ex)
			{
			}
		}
		else
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException ex)
			{
			}
		}
		connection = null;
	}
	
	/**
	 * gets the connection being used by the system
	 * @return the connection being used by the system
	 */
	@Override
	public Connection getConnection(){
		return connection;
	}	
	
	private void loadContainers(ModelLoader loader) {
		List<ContainerDTO> containers = new ArrayList();
		Iterator<ContainerDTO> allContainers = containerDAO.getContainers();
		while(allContainers.hasNext()){
			containers.add(allContainers.next());
		}
		loader.loadContainers(containers);
	}	
	
	private void loadProducts(ModelLoader loader){
		List<ProductDTO> products = new ArrayList();
		Iterator<ProductDTO> allProducts = productDAO.getProducts();
		while(allProducts.hasNext()){
			products.add(allProducts.next());
		}
		loader.loadProducts(products);
	}	
	
	private void loadMappings(ModelLoader loader){
		List<MappingDTO> mappings = new ArrayList();
		Iterator<MappingDTO> allMappings = containerDAO.getMappings();
		while(allMappings.hasNext()){
			mappings.add(allMappings.next());
		}
		loader.loadMappings(mappings);
	}	
	
	private void loadItems(ModelLoader loader){
		List<ItemDTO> items = new ArrayList();
		Iterator<ItemDTO> allItems = itemDAO.getItems();
		while(allItems.hasNext()){
			items.add(allItems.next());
		}
		loader.loadItems(items);
	}
	
	/*
	 * checks to see if there is a sql data base there.  if not it will make it.
	 */
	private void createDataBase(){
		startTransaction();
		Statement stmt;
		boolean setupOK = true;
		try {
			//just do a query that should fail if the database is not set up properly
			stmt = connection.prepareStatement("select * from ProductContainer");
		} catch (SQLException ex) {
			setupOK = false;
            //System.out.println("The database is not set up yet");
		}
		
		if(!setupOK){
			try {
				stmt = connection.createStatement();
				String query = "CREATE TABLE \"Item\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  "
						+ "NOT NULL  UNIQUE , \"ProductID\" INTEGER NOT NULL , "
						+ "\"Barcode\" VARCHAR(50) NOT NULL , "
						+ "\"EntryDate\" DATETIME NOT NULL , \"ExitDate\" DATETIME, "
						+ "\"ProductContainerID\" INTEGER)";
				stmt.executeUpdate(query);
				query = "CREATE TABLE \"Product\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  "
						+ "NOT NULL  UNIQUE , \"Barcode\" VARCHAR(50) NOT NULL , "
						+ "\"Description\" VARCHAR(50) NOT NULL , \"ShelfLife\" INTEGER, "
						+ "\"MSValue\" FLOAT, \"SizeUnit\" VARCHAR(50) NOT NULL , "
						+ "\"SizeValue\" FLOAT NOT NULL )";
				stmt.executeUpdate(query);
				query = "CREATE TABLE \"ProductContainer\" (\"ID\" INTEGER PRIMARY KEY  "
						+ "AUTOINCREMENT  NOT NULL  UNIQUE , \"Name\" VARCHAR(50) NOT NULL , "
						+ "\"ParentID\" INTEGER, \"MSUnit\" VARCHAR(10), \"MSValue\" FLOAT)";
				stmt.executeUpdate(query);
				query = "CREATE TABLE \"Product_ProductContainer\" (\"ID\" INTEGER PRIMARY "
						+ "KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"ProductID\" INTEGER NOT NULL , "
						+ "\"ProductContainerID\" INTEGER NOT NULL )";
				stmt.executeUpdate(query);
				query = "CREATE TABLE \"RemovedReport\" (\"ID\" INTEGER PRIMARY KEY  NOT NULL , "
						+ "\"DateRun\" DATETIME)";
				stmt.executeUpdate(query);
				query = "INSERT INTO RemovedReport (ID, DateRun) VALUES (1, null)";
				stmt.executeUpdate(query);
			} catch (SQLException ex) {
				//System.out.println("SQL exception in creating Tables");
			}
		}
            try {
                connection.commit();
            } catch (SQLException ex) {
            }
		this.finishTransaction(false);
	}
}
