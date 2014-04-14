///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package application.storage;
//
//import application.item.Item;
//import application.product.Product;
//import java.util.Iterator;
//import java.util.Date;
//import common.exceptions.*;
//import common.Barcode;
//import common.Size;
//import common.SizeUnit;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author chrystal
// */
//public class RemovedItemsManagerTest
//{
//
//	public RemovedItemsManagerTest()
//	{
//	}
//
//	@BeforeClass
//	public static void setUpClass()
//	{
//	}
//
//	@AfterClass
//	public static void tearDownClass()
//	{
//	}
//
//	@Before
//	public void setUp()
//	{
//	}
//
//	@After
//	public void tearDown()
//	{
//	}
//
//	/**
//	 * Test of getInstance method, of class RemovedItemsManager.
//	 */
//	@Test
//	public void testGetInstance()
//	{
//		System.out.println("getInstance");
//		RemovedItemsManager expResult = RemovedItemsManager.getInstance();
//		RemovedItemsManager result = RemovedItemsManager.getInstance();
//
//		assertNotNull(expResult);
//		assertSame(expResult, result);
//	}
//
//	/**
//	 * Test of canAddItem method, of class RemovedItemsManager.
//	 */
//	@Test
//	public void testCanAddItem()
//	{
//		System.out.println("canAddItem");
//
//		Item item = new Item();
//
//		Date today = new Date();
//		Date date = new Date();
//		date.setDate(today.getDate() - 1);
//		date.setMonth(today.getMonth() - 1);
//
//		item.setEntryDate(date);
//		RemovedItemsManager instance = RemovedItemsManager.getInstance();
//		boolean result = instance.canAddItem(item);
//		assertTrue(result);
//
//		Item item2 = new Item();
//		item2.setEntryDate(new Date());
//
//	}
//
//	/**
//	 * Test of addItem method, of class RemovedItemsManager.
//	 */
//	@Test
//	public void testAddItem() throws Exception
//	{
//		System.out.println("addItem");
//		RemovedItemsManager instance = RemovedItemsManager.getInstance();
//		Product product = new Product(new Date(), new Barcode(), "Test product", 3, new Size(3, SizeUnit.Gallons));
//		Item item = new Item();
//		item.setBarcode(new Barcode());
//		item.setProduct(product);
//
//		Date today = new Date();
//		Date date = new Date();
//		date.setDate(today.getDate() - 1);
//		date.setMonth(today.getMonth() - 1);
//
//		item.setEntryDate(date);
//		instance.addItem(item);
//
//		instance.addItem(item);
//		boolean hasItem = instance.containsItem(item);
//
//		assertTrue(hasItem);
//	}
//
//	/**
//	 * Test of getProductIterator method, of class RemovedItemsManager.
//	 */
//	@Test
//	public void testGetProductIterator()
//	{
//		System.out.println("getProductIterator");
//		try
//		{
//			RemovedItemsManager instance = RemovedItemsManager.getInstance();
//			instance.clearAll();
//			Product product = new Product(new Date(), new Barcode(), "Test product", 3, new Size(3, SizeUnit.Gallons));
//			Item item = new Item();
//			item.setBarcode(new Barcode());
//			item.setProduct(product);
//
//			Date today = new Date();
//			Date date = new Date();
//			date.setDate(today.getDate() - 1);
//			date.setMonth(today.getMonth() - 1);
//
//			item.setEntryDate(date);
//			instance.addItem(item);
//
//			Iterator result = instance.getProductIterator();
//			Product test = (Product) result.next();
//			assertEquals(product, test);
//		}
//		catch (InvalidAddException e)
//		{
//			fail("InvalidAddException");
//		}
//	}
//
//	/**
//	 * Test of getItemIterator method, of class RemovedItemsManager.
//	 */
//	@Test
//	public void testGetItemIterator()
//	{
//		System.out.println("getItemIterator");
//		try
//		{
//			RemovedItemsManager instance = RemovedItemsManager.getInstance();
//			Product product = new Product(new Date(), new Barcode(), "Test product", 3, new Size(3, SizeUnit.Gallons));
//			Item item = new Item();
//			item.setBarcode(new Barcode());
//			item.setProduct(product);
//
//			Date today = new Date();
//			Date date = new Date();
//			date.setDate(today.getDate() - 1);
//			date.setMonth(today.getMonth() - 1);
//
//			item.setEntryDate(new Date());
//			instance.addItem(item);
//
//			Iterator result = instance.getItemIterator(product);
//			assertEquals(item, result.next());
//		}
//		catch (InvalidAddException e)
//		{
//			fail("InvalidAddException");
//		}
//	}
//}
