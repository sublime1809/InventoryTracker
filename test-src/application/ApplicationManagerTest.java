///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package application;
//
//import application.item.Item;
//import application.product.Product;
//import application.storage.ProductContainer;
//import application.storage.ProductGroup;
//import application.storage.StorageUnit;
//import common.Barcode;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import org.junit.After;
//import org.junit.AfterClass;
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// *
// * @author skitto
// */
//public class ApplicationManagerTest
//{
//
//	private ApplicationManager manager;
//	private ProductContainer su1;
//	private ProductContainer su2;
//	private ProductContainer pg1;
//	private ProductContainer pg2;
//	private ProductContainer pg3;
//	private ProductContainer pg4;
//	private Product p1 = new Product(null, new Barcode(), null, 0, null);
//	private Product p2 = new Product(null, new Barcode(), null, 0, null);
//	private Product p3 = new Product(null, new Barcode(), null, 0, null);
//	private Product p4 = new Product(null, new Barcode(), null, 0, null);
//	private Item i1 = new Item();
//	private Item i2 = new Item();
//	private Item i3 = new Item();
//	private Item i4 = new Item();
//	private Item i5 = new Item();
//	private Item i6 = new Item();
//
//	public ApplicationManagerTest()
//	{
//		manager = new ApplicationManager();
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
//	public void setUp() throws Exception
//	{
//		su1 = new StorageUnit("StorageUnit1", null);
//		su2 = new StorageUnit("StorageUnit2", null);
//		pg1 = new ProductGroup("ProductGroup1", su1);
//		pg2 = new ProductGroup("ProductGroup2", su1);
//		pg3 = new ProductGroup("ProductGroup1", su2);
//		pg4 = new ProductGroup("ProductGroup2", su2);
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(su2, null);
//		manager.addProductContainer(pg1, su1);
//		manager.addProductContainer(pg2, su1);
//		manager.addProductContainer(pg3, su2);
//		manager.addProductContainer(pg4, su2);
//		Date date = new Date();
//		i1 = new Item();
//		i1.setBarcode(new Barcode());
//		i1.setProduct(p1);
//		i1.setEntryDate(date);
//		i1.setProductContainer(pg1);
//		i2 = new Item();
//		i2.setBarcode(new Barcode());
//		i2.setProduct(p1);
//		i2.setEntryDate(date);
//		i2.setProductContainer(pg1);
//		i3 = new Item();
//		i3.setBarcode(new Barcode());
//		i3.setProduct(p2);
//		i3.setEntryDate(date);
//		i3.setProductContainer(pg2);
//		i4 = new Item();
//		i4.setBarcode(new Barcode());
//		i4.setProduct(p2);
//		i4.setEntryDate(date);
//		i4.setProductContainer(pg2);
//		i5 = new Item();
//		i5.setBarcode(new Barcode());
//		i5.setProduct(p2);
//		i5.setEntryDate(date);
//		i5.setProductContainer(pg2);
//		i6 = new Item();
//		i6.setBarcode(new Barcode());
//		i6.setProduct(p4);
//		i6.setEntryDate(date);
//		i6.setProductContainer(pg3);
//
//		List<Item> items1 = new ArrayList<Item>();
//		items1.add(i1);
//		items1.add(i2);
//		List<Item> items2 = new ArrayList<Item>();
//		items2.add(i3);
//		items2.add(i4);
//		items2.add(i5);
//		List<Item> items3 = new ArrayList<Item>();
//		items3.add(i6);
//
//		manager.addProduct(pg1, p1, items1);
//		manager.addProduct(pg2, p2, items2);
//		manager.addProduct(pg3, p3, items3);
//	}
//
//	@After
//	public void tearDown()
//	{
//		manager.reset();
//	}
//
//	/**
//	 * tests to make sure that the setup worked properly
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void testSetUp() throws Exception
//	{
//		System.out.println("setUp");
//
//		Iterator<ProductContainer> subContainers = manager.getSubContainers(su1);
//		Set<ProductContainer> expected = new HashSet<ProductContainer>();
//		expected.add(pg1);
//		expected.add(pg2);
//		int found = 0;
//
//		while (subContainers.hasNext())
//		{
//			ProductContainer cur = subContainers.next();
//			assertTrue(expected.contains(cur));
//			found++;
//		}
//
//		assertEquals(2, found);
//	}
//
////	/**
////	 * Test of canAddProduct method, of class ApplicationManager.
////	 */
////	@Test
////	public void testCanAddProduct() throws Exception
////	{
////		System.out.println("canAddProduct");
////		Product product = null;
////		ProductContainer container = null;
////		ApplicationManager instance = new ApplicationManager();
////		boolean expResult = false;
////		boolean result = instance.canAddProduct(product, container);
////		assertEquals(expResult, result);
////		// TODO review the generated test code and remove the default call to fail.
////		fail("The test case is a prototype.");
////	}
////
////	/**
////	 * Test of canAddProductContainer method, of class ApplicationManager.
////	 */
////	@Test
////	public void testCanAddProductContainer() throws Exception
////	{
////		System.out.println("canAddProductContainer");
////		ProductContainer newContainer = new ProductGroup("New Container", pg1);
////		ProductContainer parentContainer = pg1;
////		ApplicationManager instance = new ApplicationManager();
////		boolean expResult = false;
////		boolean result = instance.canAddProductContainer(newContainer, parentContainer);
////		assertEquals(expResult, result);
////		// TODO review the generated test code and remove the default call to fail.
////		fail("The test case is a prototype.");
////	}
////
////	/**
////	 * Test of canRemoveProductContainer method, of class ApplicationManager.
////	 */
////	@Test
////	public void testCanRemoveProductContainer()
////	{
////		System.out.println("canRemoveProductContainer");
////		ProductContainer container = null;
////
////		boolean expResult = false;
////		boolean result = manager.canRemoveProductContainer(container);
////		assertEquals(expResult, result);
////
////		result = manager.canRemoveProductContainer(pg4);
////		assertTrue(result);
////		result = manager.canRemoveProductContainer(pg1);
////		assertFalse(result);
////		result = manager.canRemoveProductContainer(pg2);
////		assertFalse(result);
////		result = manager.canRemoveProductContainer(su1);
////		assertFalse(result);
////	}
//
//	}
