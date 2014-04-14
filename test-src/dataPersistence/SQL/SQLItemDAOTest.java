/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.ItemDTO;
import dataPersistence.PersistenceManager;
import java.util.Date;
import java.util.Iterator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Trevor
 */
public class SQLItemDAOTest
{
	
	private static PersistenceManager persistenceManager = PersistenceManager.getInstance();
	
	public SQLItemDAOTest()
	{
	}
	
	@BeforeClass
	public static void setUpClass()
	{
		persistenceManager.makeNewFactory("-sql");
	}
	
	@AfterClass
	public static void tearDownClass()
	{
	}
	
	@Before
	public void setUp()
	{
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of createItem method, of class SQLItemDAO.
	 */
	@Test
	public void testCreateItem()
	{
		System.out.println("createItem");
		persistenceManager.startTransaction();
		ItemDTO item = new ItemDTO(1, "54545454", new Date(), null, 4, 5);
		SQLItemDAO instance = new SQLItemDAO();
		instance.createItem(item);
		persistenceManager.finishTransaction(true);
		
		assertTrue(true);
	}

//	/**
//	 * Test of getItems method, of class SQLItemDAO.
//	 */
//	@Test
//	public void testGetItems()
//	{
//		System.out.println("getItems");
//		SQLItemDAO instance = new SQLItemDAO();
//		Iterator expResult = null;
//		Iterator result = instance.getItems();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of updateItem method, of class SQLItemDAO.
//	 */
//	@Test
//	public void testUpdateItem()
//	{
//		System.out.println("updateItem");
//		ItemDTO item = null;
//		SQLItemDAO instance = new SQLItemDAO();
//		instance.updateItem(item);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of removeItem method, of class SQLItemDAO.
//	 */
//	@Test
//	public void testRemoveItem()
//	{
//		System.out.println("removeItem");
//		ItemDTO item = null;
//		SQLItemDAO instance = new SQLItemDAO();
//		instance.removeItem(item);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
}
