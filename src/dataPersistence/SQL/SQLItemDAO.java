/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.IItemDAO;
import dataPersistence.ItemDTO;
import dataPersistence.PersistenceManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stephen Kitto
 */
public class SQLItemDAO implements IItemDAO{
	
	private PersistenceManager persistenceManager = PersistenceManager.getInstance();
	
	/**
	 * adds an item into the item table
	 * @param item	the new item
	 */
	@Override
	public boolean createItem(ItemDTO item)
	{
		//System.out.println("Creating Item in database.");
		boolean result = false;
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		try
		{
			String query = "INSERT INTO Item "
					+ "(ProductID, Barcode, EntryDate, ProductContainerID) VALUES (?, ?, ?, ?);";
			stmt = this.persistenceManager.getConnection().prepareStatement(query);	
			stmt.setInt(1, item.getProductID());
			stmt.setString(2, item.getBarcode());
			stmt.setDate(3, new java.sql.Date(item.getEntryDate().getTime()));
			stmt.setInt(4, item.getContainerID());
			
			if(stmt.executeUpdate() == 1)
			{
				keyStmt = this.persistenceManager.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);   // ID of the new book
				item.setID(id);
				result = true;
				//System.out.println("got it!" + id);
			}
			else
			{
				//System.out.println("Something went wrong creating the item.");
			}
		}
		catch (SQLException ex)
		{
			//System.out.println("An exception occured creating the item.");
		}
		finally
		{
			try
			{
				if (stmt != null) stmt.close();
				if (keyRS != null) keyRS.close();
				if (keyStmt != null) keyStmt.close();
			}
			catch (SQLException ex)
			{
				//youre screwed
			}
		}
		return result;
	}
	
	/**
	 * gets all the items in the database
	 * @return	an iterator with a dto for all the items in the database
	 */
	@Override
	public Iterator<ItemDTO> getItems(){
		//System.out.println("Getting items");
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		
		try {
			String query = "select * from Item";
			stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
			List<ItemDTO> items = new ArrayList();
			
			keyRS = stmt.executeQuery();
			while(keyRS.next()){
				int id = keyRS.getInt("ID");
				String barcode = keyRS.getString("Barcode");
				int productID = keyRS.getInt("ProductID");
				int productContainerID = keyRS.getInt("ProductContainerID");
				Date entryDate = keyRS.getDate("EntryDate");
				Date exitDate = keyRS.getDate("ExitDate");
				
				items.add(new ItemDTO(id, barcode, entryDate, exitDate, 
						productID, productContainerID));
			}

			return items.iterator();	
			
		} catch (SQLException ex) {
			//System.out.println("Exception occured getting items: " + ex.getMessage());
			return null;
		}	
	}
	
	/**
	 * updates an item in the database
	 * @param item	the item with new information
	 */
	@Override
	public boolean updateItem(ItemDTO item){
		//System.out.println("updating item");
		boolean result = false;
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		try
		{
			String query = "UPDATE Item SET "
					+ "EntryDate=?, "
					+ "ExitDate=?, "
					+ "ProductContainerID=? "
					+ "WHERE ID=?;";

			stmt = this.persistenceManager.getConnection().prepareStatement(query);	
			stmt.setDate(1, new java.sql.Date(item.getEntryDate().getTime()));
			stmt.setDate(2, (item.getExitDate() == null ? null : 
					new java.sql.Date(item.getExitDate().getTime())));
			stmt.setInt(3, item.getContainerID());
			stmt.setInt(4, item.getID());
			
			if(stmt.executeUpdate() == 1)
			{
				keyStmt = this.persistenceManager.getConnection().createStatement();
				result = true;
			}
			else
			{
				//System.out.println("something went wrong updating the item");
			}
		}
		catch (SQLException ex)
		{
			//System.out.println("exception occured during updating item: " + ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null) stmt.close();
				if (keyStmt != null) keyStmt.close();
			}
			catch (SQLException ex)
			{
				//youre screwed
			}
		}
		return result;
	}
	
	/**
	 * removes an item from the database
	 * @param item	the item to be removed
	 */
	@Override
	public boolean removeItem(ItemDTO item){
		
		//System.out.println("removing an item");
		boolean result = false;
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		try
		{
			String query = "DELETE FROM Item WHERE ID = ?;";
			stmt = this.persistenceManager.getConnection().prepareStatement(query);
			stmt.setInt(1, item.getID());
			
			if(stmt.executeUpdate() == 1)
			{
				keyStmt = this.persistenceManager.getConnection().createStatement();
				result = true;
			}
			else
			{
				//System.out.println("something went wrong removing the item");
			}
		}
		catch (SQLException ex)
		{
			//System.out.println("exception occured during removing: " + ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null) stmt.close();
				if (keyStmt != null) keyStmt.close();
			}
			catch (SQLException ex)
			{
				//youre screwed
			}
		}
		return result;
	}

	@Override
	public Date getRemovedReportDate() {
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		
		try {
			String query = "SELECT * FROM RemovedReport WHERE ID = 1";
			stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
			List<ItemDTO> items = new ArrayList();
			Date resultDate = null;
			
			keyRS = stmt.executeQuery();
			while(keyRS.next()){
				resultDate = keyRS.getDate("DateRun");
			}	
			return resultDate;
			
		} catch (SQLException ex) {
			System.out.println("Exception occured getting items: " + ex.getMessage());
			return null;
		}	
	}

	@Override
	public void updateRemovedReportDate(Date date) {
		PreparedStatement stmt = null;
		Statement keyStmt = null;
		ResultSet keyRS = null;
		
		try {
			String query = "UPDATE RemovedReport SET DateRun = ? WHERE ID = 1";
			stmt = PersistenceManager.getInstance().getConnection().prepareStatement(query);
			stmt.setDate(1, new java.sql.Date(date.getTime()));
			stmt.executeUpdate();
			
		} catch (SQLException ex) {
			//System.out.println("Exception occured: " + ex.getMessage());
		}	
	}
}
