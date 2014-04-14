///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package application.item;
//
//import application.product.Product;
//import application.storage.ProductContainer;
//import application.storage.StorageUnit;
//import common.Barcode;
//import common.Size;
//import common.SizeUnit;
//import common.exceptions.InvalidDateException;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.UUID;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
///**
// *
// * @author Trevor
// */
//public class ItemTest
//{
//
//	private Barcode barcode1;
//	private Barcode barcode2;
//	private Barcode barcode3;
//
//	public ItemTest()
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
//		barcode1 = new Barcode();
//		barcode2 = new Barcode();
//		barcode3 = new Barcode(barcode1);
////        
////        Date entryDate = new Date();
////        Date exitDate = new Date();
////        Date expirationDate = new Date();
////        
////        Product product = new Product(entryDate, barcode1, "Prduct Descri", _shelfLife, null)
////        
////        item1 = new Item();
////        item1.setBarcode(barcode1);
////        item1.setEntryDate(entryDate);
////        item1.setExitTime(exitDate);
////        item1.setExpirationDate(expirationDate);
////        item1.setProduct(null);
////        item2 = new Item();
////        item3 = new Item();
////        
////        
//	}
//
//	@After
//	public void tearDown()
//	{
//	}
//
//	/**
//	 * Test of equals method, of class Item.
//	 */
//	@Test
//	public void testEquals()
//	{
//		System.out.println("equals");
//
//		Item other = null;
//
//		Item instance = new Item();
//		instance.setBarcode(barcode1);
//
//		Item notEqual = new Item();
//		notEqual.setBarcode(barcode2);
//
//		Item equal = new Item();
//		equal.setBarcode(barcode3);
//
//		assertFalse(instance.equals(other));
//		assertFalse(instance.equals(notEqual));
//		assertTrue(instance.equals(equal));
//	}
//
//	/**
//	 * Test of compareTo method, of class Item.
//	 */
//	@Test
//	public void testCompareTo()
//	{
//		System.out.println("compareTo");
//
//		Item item1 = new Item();
//		item1.setBarcode(new Barcode("400000000467"));
//
//		Item item2 = new Item();
//		item2.setBarcode(new Barcode("400000000477"));
//
//		Item item3 = new Item();
//		item3.setBarcode(new Barcode("400000000477"));
//
//		assertTrue(item1.compareTo(item2) < 0);
//		assertTrue(item2.compareTo(item1) > 0);
//		assertTrue(item2.compareTo(item3) == 0);
//
//	}
//
//	/**
//	 * Test of getBarcode method, of class Item.
//	 */
//	@Test
//	public void testGetSetBarcode()
//	{
//		System.out.println("getBarcode");
//		Item instance = new Item();
//		instance.setBarcode(barcode1);
//		assertTrue(barcode1.equals(instance.getBarcode()));
//	}
//
//	/**
//	 * Test of getEntryDate method, of class Item.
//	 */
//	@Test
//	public void testGetEntryDate()
//	{
//		System.out.println("getEntryDate");
//		Item instance = new Item();
//		Date entryDate = new GregorianCalendar(2005, 1, 1).getTime();
//		instance.setEntryDate(entryDate);
//
//		assertTrue(entryDate.equals(instance.getEntryDate()));
//	}
//
//	/**
//	 * Test of setEntryDate method, of class Item.
//	 */
//	@Test
//	public void testSetEntryDate()
//	{
//		System.out.println("setEntryDate");
//
//		Item instance = new Item();
//		Date entryDate = new Date();
//		instance.setEntryDate(entryDate);
//		Date badEntryDate = new GregorianCalendar(1999, GregorianCalendar.DECEMBER, 31).getTime();
//		instance.setEntryDate(badEntryDate);
//		fail("setEntryDate should not accept dates prior to 1/1/2000");
//		Date futureEntryDate = new Date(new Date().getTime() + 100000);
//		instance.setEntryDate(futureEntryDate);
//		fail("setEntryDate should not accept futute dates");
//		Date nullDate = null;
//		instance.setEntryDate(nullDate);
//		fail("setEntryDate should not accept null dates.");
//	}
//
//	/**
//	 * Test of getExitTime method, of class Item.
//	 */
//	@Test
//	public void testGetSetExitTime()
//	{
//		System.out.println("getExitTime, setExitTime");
//
//		Item instance = new Item();
//		Date entryDate = new Date();
//		instance.setEntryDate(entryDate);
//
//		try
//		{
//			Date exitTime = new Date();
//			instance.setExitTime(exitTime);
//		}
//		catch (InvalidDateException ex)
//		{
//			fail("Date was valid but was rejected.");
//		}
//
//		try
//		{
//			Date badExitTime = new GregorianCalendar(2012, GregorianCalendar.SEPTEMBER, 20).getTime();
//			instance.setExitTime(badExitTime);
//			System.out.println(instance.getEntryDate() + " " + instance.getExitTime());
//			fail("setExitTime should not accept dates prior to the entry date at 12:00 am");
//		}
//		catch (InvalidDateException ex)
//		{
//		}
//
//		try
//		{
//			Date futureExitTime = new Date(new Date().getTime() + 100000);
//			instance.setExitTime(futureExitTime);
//			fail("setExitTime should not accept futute times");
//		}
//		catch (InvalidDateException ex)
//		{
//		}
//
//		try
//		{
//			Date nullDate = null;
//			instance.setExitTime(nullDate);
//			fail("setExitTime should not accept null dates.");
//		}
//		catch (InvalidDateException ex)
//		{
//		}
//	}
//
//	/**
//	 * Test of getExpirationDate method, of class Item.
//	 */
//	@Test
//	public void testGetSetExpirationDate()
//	{
////		System.out.println("getExpirationDate");
////		Item instance = new Item();
////		Date expResult = new Date();
////		instance.setExpirationDate(expResult);
////		Date result = instance.getExpirationDate();
////		assertEquals(expResult, result);
//	}
//
//	/**
//	 * Test of getProductContainer method, of class Item.
//	 */
//	@Test
//	public void testGetSetProductContainer()
//	{
////		System.out.println("getProductContainer");
////		Item instance = new Item();
////		UUID productContainerID = UUID.randomUUID();
////		ProductContainer expResult = new StorageUnit(productContainerID);
////		instance.setProductContainer(expResult);
////		ProductContainer result = instance.getProductContainer();
////		assertEquals(expResult, result);
//	}
//
//	/**
//	 * Test of getProduct method, of class Item.
//	 */
//	@Test
//	public void testGetSetProduct()
//	{
////		System.out.println("getProduct");
////		Item instance = new Item();
////		Product expResult = new Product(new Date(), barcode1, "Description", 3, new Size(3, SizeUnit.Count));
////		instance.setProduct(expResult);
////		Product result = instance.getProduct();
////		assertEquals(expResult, result);
//	}
//}
