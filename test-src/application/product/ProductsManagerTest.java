/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.product;

import application.item.Item;
import application.storage.ProductContainer;
import application.storage.StorageUnit;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InvalidContainerException;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author leckie
 */
public class ProductsManagerTest
{

	public ProductsManagerTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	@Before
	public void setUp()
	{
		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
		Barcode testBarcode = new Barcode();
		String prodDesc = "Test Product";
		int shelfLife = 7;
		Size size = new Size(2, SizeUnit.Gallons);

		Product testProduct = new Product(entryDate, testBarcode.getValue(), prodDesc, shelfLife, 3, size);
		ProductsManager prodManager = ProductsManager.getInstance();

		ArrayList<Item> itemsTest = new ArrayList<Item>();
		Item item1 = new Item();
		Item item2 = new Item();
		Item item3 = new Item();
		Item item4 = new Item();

		itemsTest.add(item1);
		itemsTest.add(item2);
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getInstance method, of class ProductsManager.
	 */
	/**
	 * Test of addNewProduct method, of class ProductsManager.
	 */
	@Test
	public void testAddNewProduct() throws InvalidContainerException
	{
//		System.out.println("addNewProduct");
//
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product testProduct = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//		ProductsManager prodManager = ProductsManager.getInstance();
//
//		ArrayList<Item> itemsTest = new ArrayList<Item>();
//		Item item1 = new Item();
//		Item item2 = new Item();
//		Item item3 = new Item();
//		Item item4 = new Item();
//
//		itemsTest.add(item1);
//		itemsTest.add(item2);
//
//		Product product = testProduct;
//		Barcode barcode = testBarcode;
//		ArrayList<Item> items = itemsTest;
//		ProductContainer container = new StorageUnit("Storage Unit", null);
//		ProductsManager instance = prodManager;
//
//		instance.addNewProduct(product, barcode, items, container);
//
//		Product result = instance.getProduct(barcode);
//
//		assertEquals(product, result);
	}

	/**
	 * Test of canAddProduct method, of class ProductsManager.
	 */
	@Test
	public void testCanAddProduct() throws InvalidContainerException
	{
//		System.out.println("canAddProduct");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product product = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//		ProductsManager instance = ProductsManager.getInstance();
//		boolean expResult = true;
//		boolean result = instance.canAddProduct(product);
//		assertEquals(expResult, result);
//
//		expResult = false;
//		ArrayList<Item> items = new ArrayList<Item>();
//		Item item1 = new Item();
//		items.add(item1);
//		instance.addNewProduct(product, testBarcode, items, new StorageUnit("storage Unit", null));
//		result = instance.canAddProduct(product);
//		assertEquals(expResult, result);

	}

	/**
	 * Test of hasItems method, of class ProductsManager.
	 */
	@Test
	public void testHasItems() throws InvalidContainerException
	{
//		System.out.println("hasItems");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product product = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//		ProductsManager instance = ProductsManager.getInstance();
//
//		ArrayList<Item> items = new ArrayList<Item>();
//		instance.addNewProduct(product, testBarcode, items, new StorageUnit("storage Unit", null));
//
//		boolean expResult = false;
//		boolean result = instance.hasItems(product);
//		assertEquals(expResult, result);
//
//		ArrayList<Item> newItems = new ArrayList<Item>();
//		Item item1 = new Item();
//		newItems.add(item1);
//		//instance.updateProductItemPair(product, items, newItems);
//
//		expResult = true;
//		result = instance.hasItems(product);
//		assertEquals(expResult, result);

	}

	/**
	 * Test of removeEntirely method, of class ProductsManager.
	 */
	@Test
	public void testRemoveEntirely() throws InvalidContainerException, CannotBeRemovedException
	{
//		System.out.println("removeEntirely");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product product = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//		ProductsManager instance = ProductsManager.getInstance();
//
//		ArrayList<Item> items = new ArrayList<Item>();
//		Item item1 = new Item();
//		items.add(item1);
//		instance.addNewProduct(product, testBarcode, items, new StorageUnit("storage Unit", null));
//		boolean result = false;
//		boolean expResult = true;
//
//		try
//		{
//			instance.removeEntirely(product);
//		}
//		catch (CannotBeRemovedException ex)
//		{
//			result = true;
//		}
//
//		assertEquals(expResult, result);
	}

	/**
	 * Test of canRemove method, of class ProductsManager.
	 */
	@Test
	public void testCanRemove() throws InvalidContainerException
	{
//		System.out.println("canRemove");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product product = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//		ProductsManager instance = ProductsManager.getInstance();
//
//		ArrayList<Item> items = new ArrayList<Item>();
//		Item item1 = new Item();
//		items.add(item1);
//		instance.addNewProduct(product, testBarcode, items, new StorageUnit("storage Unit", null));
//
//		boolean expResult = false;
//		boolean result = instance.canRemove(product);
//		assertEquals(expResult, result);
	}
}
