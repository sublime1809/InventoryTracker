/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.item;

import application.product.Product;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidDateException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
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
public class ItemsManagerTest
{

	private ItemsManager manager;

	public ItemsManagerTest()
	{
		manager = ItemsManager.getInstance();
	}

	@BeforeClass
	public static void setUpClass()
	{
		
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp() throws InvalidDateException
	{
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testCanAdd()
	{
		Barcode validBarcode = null;
		
		GregorianCalendar today = new GregorianCalendar();
		today.clear(GregorianCalendar.HOUR);
		today.clear(GregorianCalendar.MINUTE);
		today.clear(GregorianCalendar.SECOND);
		today.clear(GregorianCalendar.MILLISECOND);
		
		Date validEntryDate1 = new GregorianCalendar(2000, GregorianCalendar.JANUARY, 1).getTime();
		Date validEntryDate2 = today.getTime();
		Date validEntryDate3 = new GregorianCalendar(2005, GregorianCalendar.MARCH, 2).getTime();
		
		Date invalidEntryDate1 = new GregorianCalendar(1999, GregorianCalendar.DECEMBER, 31).getTime();
		Date invalidEntryDate2 = new Date(validEntryDate1.getTime() - 1000);
		Date invalidEntryDate3 = new GregorianCalendar(2013, 5, 5).getTime();
		Date invalidEntryDate4 = new GregorianCalendar(1995, 1, 24).getTime();
		
//		//These valid dates are for validEntryDate3
//		Date validExitDate1 = new Date(validEntryDate3.getTime());
//		Date validExitDate2 = new Date(validEntryDate3.getTime() + 1000);
//		Date validExitDate3 = new Date();
		Date validExitDate = null;
//		
		//These are invalid dates for validEntryDate3
		Date invalidExitDate = new Date(validEntryDate3.getTime() - 1000);
//		Date invalidExitDate2 = new Date(new Date().getTime() + 1000);
		
		ProductGroup validProductGroup = new ProductGroup("Name", null);
		ProductGroup invalidProductGroup = null;
		
		Product validProduct = new Product();
		Product invalidProduct = null;
		
		Item validItem = new Item(validBarcode, validEntryDate1, validExitDate, null, validProductGroup, validProduct);
		assertTrue(manager.canAdd(validItem));
		
		validItem.setEntryDate(validEntryDate2);
		assertTrue(manager.canAdd(validItem));
		
		validItem.setEntryDate(validEntryDate3);
		assertTrue(manager.canAdd(validItem));
		
		Item invalidItem2 = new Item(validItem);
		invalidItem2.setEntryDate(invalidEntryDate1);
		assertFalse(manager.canAdd(invalidItem2));
		
		Item invalidItem3 = new Item(validItem);
		invalidItem3.setEntryDate(invalidEntryDate2);
		assertFalse(manager.canAdd(invalidItem3));
		
		Item invalidItem4 = new Item(validItem);
		invalidItem4.setEntryDate(invalidEntryDate3);
		assertFalse(manager.canAdd(invalidItem4));
		
		Item invalidItem5 = new Item(validItem);
		invalidItem5.setEntryDate(invalidEntryDate4);
		assertFalse(manager.canAdd(invalidItem5));
		
		Item invalidItem6 = new Item(validItem);
		invalidItem6.setExitTime(invalidExitDate);
		assertFalse(manager.canAdd(invalidItem6));
		
		Item invalidItem8 = new Item(validItem);
		invalidItem8.setProduct(invalidProduct);
		assertFalse(manager.canAdd(invalidItem8));
		
		Item invalidItem9 = new Item(validItem);
		invalidItem9.setProductContainer(invalidProductGroup);
		assertFalse(manager.canAdd(invalidItem9));
		
		//Product must be non empty
		//Barcode must be vaild and unique
		//Entry date must be non empty
		//Entry date must be between 1/1/2000 and today
		//Exit time must be between entry data at 12:00 am and today
		//Non empty product container
		
		
		
	}

//	/**
//	 * Test of getInstance method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetInstance()
//	{
//		System.out.println("getInstance");
//		ItemsManager expResult = null;
//		ItemsManager result = ItemsManager.getInstance();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of reset method, of class ItemsManager.
//	 */
//	@Test
//	public void testReset()
//	{
//		System.out.println("reset");
//		ItemsManager instance = new ItemsManager();
//		instance.reset();
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of add method, of class ItemsManager.
//	 */
//	@Test
//	public void testAdd() throws Exception
//	{
//		System.out.println("add");
//		Item item = null;
//		ItemsManager instance = new ItemsManager();
//		instance.add(item);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of remove method, of class ItemsManager.
//	 */
//	@Test
//	public void testRemove()
//	{
//		System.out.println("remove");
//		Item item = null;
//		ItemsManager instance = new ItemsManager();
//		Item expResult = null;
//		Item result = instance.remove(item);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of getItem method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetItem()
//	{
//		System.out.println("getItem");
//		Barcode barcode = null;
//		ItemsManager instance = new ItemsManager();
//		Item expResult = null;
//		Item result = instance.getItem(barcode);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of getProduct method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetProduct()
//	{
//		System.out.println("getProduct");
//		Item item = null;
//		ItemsManager instance = new ItemsManager();
//		Product expResult = null;
//		Product result = instance.getProduct(item);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of getDirectContainer method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetDirectContainer()
//	{
//		System.out.println("getDirectContainer");
//		Item item = null;
//		ItemsManager instance = new ItemsManager();
//		ProductContainer expResult = null;
//		ProductContainer result = instance.getDirectContainer(item);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of updateDirectContainer method, of class ItemsManager.
//	 */
//	@Test
//	public void testUpdateDirectContainer()
//	{
//		System.out.println("updateDirectContainer");
//		Item item = null;
//		ProductContainer container = null;
//		ItemsManager instance = new ItemsManager();
//		boolean expResult = false;
//		boolean result = instance.updateDirectContainer(item, container);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of printBarcodes method, of class ItemsManager.
//	 */
//	@Test
//	public void testPrintBarcodes() throws Exception
//	{
//		System.out.println("printBarcodes");
//		List<Item> items = null;
//		ItemsManager instance = new ItemsManager();
//		String expResult = "";
//		String result = instance.printBarcodes(items);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
//
//	/**
//	 * Test of deleteItems method, of class ItemsManager.
//	 */
//	@Test
//	public void testDeleteItems()
//	{
//		System.out.println("deleteItems");
//		Collection<Item> items = null;
//		ItemsManager instance = new ItemsManager();
//		instance.deleteItems(items);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
	
}

//	/**
//	 * Test of getInstance method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetInstance()
//	{
//		System.out.println("getInstance");
//		ItemsManager expResult = ItemsManager.getInstance();
//		ItemsManager result = ItemsManager.getInstance();
//		assertEquals(expResult, result);
//	}
//
//	/**
//	 * Test of updateItemEntryDate method, of class ItemsManager.
//	 */
//	@Test
//	public void testUpdate() throws Exception
//	{
////		System.out.println("update");
////		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
////		ItemsManager instance = ItemsManager.getInstance();
////		Barcode barcode = instance.add(item1);
////
////		//Before the updateItemEntryDate
////		assertFalse(instance.getItem(item1).getEntryDate().equals(entryDate));
////
////		item1.setEntryDate(entryDate);
////		item1.setBarcode(barcode);
////		instance.updateItemEntryDate(item1);
////
////		//After the updateItemEntryDate
////		assertTrue(instance.getItem(item1).getEntryDate().equals(entryDate));
//	}
//
//	/**
//	 * Test of add method, of class ItemsManager.
//	 */
//	@Test
//	public void testAdd() throws InsufficientBarcodesException
//	{
////		System.out.println("add");
////		ItemsManager instance = ItemsManager.getInstance();
////		Barcode result = instance.add(item1);
////
////		item1.setBarcode(result);
////
////		Item item = instance.getItem(item1);
////
////		assertTrue(item.equals(item));
//	}
//
//	/**
//	 * Test of canAdd method, of class ItemsManager.
//	 */
//	@Test
//	public void testCanAdd()
//	{
//		System.out.println("canAdd");
//		ItemsManager instance = ItemsManager.getInstance();
//
//		//Good
//		assertTrue(instance.canAdd(item1));
//
//		//Bad Product
//		Product tempProduct = item1.getProduct();
//		item1.setProduct(null);
//		assertFalse(instance.canAdd(item1));
//		item1.setProduct(tempProduct);
//		//double check that it is valid again.
//		assertTrue(instance.canAdd(item1));
//
//		//Barcode is set when item is added so it is not checked here.
//
//		//Bad (null) entry date
//		Item item3 = new Item();
//		item3.setBarcode(item1.getBarcode());
//		item3.setProductContainer(item1.getProductContainer());
//		assertFalse(instance.canAdd(item3));
//
//		//Bad (before 1/1/2000) entry date (item protects against this)
//
//		//Bad (future) entry date (item protects against this)
//
//	}
//
//	/**
//	 * Test of remove method, of class ItemsManager.
//	 */
//	@Test
//	public void testRemove()
//	{
//		System.out.println("remove");
//
//		//Test that the item removed date is set
//		//Test that the item is no longer in any map
//	}
//
//	/**
//	 * Test of getItem method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetItem() throws InsufficientBarcodesException
//	{
////		System.out.println("getItem");
////		ItemsManager instance = ItemsManager.getInstance();
////		instance.add(item1);
////		Item result = instance.getItem(item1);
////		assertEquals(item1, result);
////
////		Item noBarcode = new Item();
////		noBarcode.setBarcode(new Barcode());
////		Item result2 = instance.getItem(noBarcode);
////		assertEquals(result2, null);
//	}
//
//	/**
//	 * Test of getProduct method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetProduct() throws InsufficientBarcodesException
//	{
////		System.out.println("getProduct");
////		ItemsManager instance = ItemsManager.getInstance();
////		Barcode barcode = instance.add(item1);
////		item1.setBarcode(barcode);
////		Product result = instance.getProduct(item1);
////		assertEquals(item1.getProduct(), result);
////
////		Item badItem = new Item();
////		badItem.setBarcode(new Barcode());
////
////		Product result2 = instance.getProduct(badItem);
////		assertEquals(result2, null);
//	}
//
//	/**
//	 * Test of getProductContainer method, of class ItemsManager.
//	 */
//	@Test
//	public void testGetProductContainer() throws InsufficientBarcodesException
//	{
////		System.out.println("getProductContainer");
////		ItemsManager instance = ItemsManager.getInstance();
////		Barcode barcode = instance.add(item1);
////		item1.setBarcode(barcode);
////		ProductContainer result = instance.getProductContainer(item1);
////		assertEquals(item1.getProductContainer(), result);
////
////		Item badItem = new Item();
////		badItem.setBarcode(new Barcode());
////
////		ProductContainer result2 = instance.getProductContainer(badItem);
////		assertEquals(result2, null);
//	}
//}
