///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package application.product;
//
//import common.Barcode;
//import common.Size;
//import common.SizeUnit;
//import common.exceptions.InvalidBarcodeException;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.junit.*;
//import static org.junit.Assert.*;
//
///**
// *
// * @author lgunter
// */
//public class ProductTest
//{
//
//	public ProductTest()
//	{
//	}
//
//	@BeforeClass
//	public static void setUpClass() throws Exception
//	{
//	}
//
//	@AfterClass
//	public static void tearDownClass() throws Exception
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
//	 * Test of getDate method, of class Product.
//	 */
//	@Test
//	public void testGetDate()
//	{
//		System.out.println("getDate");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		Date expResult = new GregorianCalendar(2010, 5, 1).getTime();
//		Date result = instance.getDate();
//		assertEquals(expResult, result);
//
//	}
//
//	/**
//	 * Test of getBarcode method, of class Product.
//	 */
//	@Test
//	public void testGetBarcode()
//	{
//		System.out.println("getBarcode");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		Barcode expResult = testBarcode;
//		Barcode result = instance.getBarcode();
//		assertEquals(expResult, result);
//
//	}
//
//	/**
//	 * Test of getProdDesc method, of class Product.
//	 */
//	@Test
//	public void testGetProdDesc()
//	{
//		System.out.println("getProdDesc");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		String expResult = "Test Product";
//		String result = instance.getProdDesc();
//		assertEquals(expResult, result);
//
//	}
//
//	/**
//	 * Test of getShelfLife method, of class Product.
//	 */
//	@Test
//	public void testGetShelfLife()
//	{
//		System.out.println("getShelfLife");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		int expResult = 7;
//		int result = instance.getShelfLife();
//		assertEquals(expResult, result);
//
//	}
//
//	/**
//	 * Test of getMonthSupply method, of class Product.
//	 */
//	@Test
//	public void testGetMonthSupply()
//	{
//		System.out.println("getMonthSupply");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		Size expResult = size;
//		Size result = instance.getMonthSupply();
//		assertEquals(expResult, result);
//
//	}
//
//	/**
//	 * Test of setDate method, of class Product.
//	 */
//	@Test
//	public void testSetDate()
//	{
//		System.out.println("setDate");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Date _date = new GregorianCalendar(2012, 5, 1).getTime();
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		instance.setDate(_date);
//
//	}
//
//	/**
//	 * Test of setBarcode method, of class Product.
//	 */
//	@Test
//	public void testSetBarcode()
//	{
//		System.out.println("setBarcode");
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode _barcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		
//		instance.setBarcode(_barcode);
//		 
//
//	}
//
//	/**
//	 * Test of setProdDesc method, of class Product.
//	 */
//	@Test
//	public void testSetProdDesc()
//	{
//		System.out.println("setProdDesc");
//		String _newProductDescription = "New Description";
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		instance.setProdDesc(_newProductDescription);
//
//	}
//
//	/**
//	 * Test of setShelfLife method, of class Product.
//	 */
//	@Test
//	public void testSetShelfLife()
//	{
//		System.out.println("setShelfLife");
//		int _shelfLife = 0;
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		instance.setShelfLife(_shelfLife);
//
//	}
//
//	/**
//	 * Test of setMonthSupply method, of class Product.
//	 */
//	@Test
//	public void testSetMonthSupply()
//	{
//		System.out.println("setMonthSupply");
//		Size _threeMonthSupply = new Size(20, SizeUnit.Count);
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//		instance.setMonthSupply(_threeMonthSupply);
//
//		assertTrue(instance.getMonthSupply().equals(_threeMonthSupply));
//	}
//
//	/**
//	 * Test of equals method, of class Product.
//	 */
//	@Test
//	public void testEquals()
//	{
//		System.out.println("equals");
//
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size);
//
//		Product toCompare = new Product(entryDate, prodDesc, prodDesc, shelfLife, shelfLife, size)entryDate, new Barcode(), "new Product", 6, size);
//		boolean expResult = false;
//		boolean result = instance.equals(toCompare);
//		assertEquals(expResult, result);
//
//		expResult = true;
//		result = instance.equals(instance);
//		assertEquals(expResult, result);
//	}
//
//	/**
//	 * Test of compareTo method, of class Product.
//	 */
//	@Test
//	public void testCompareTo()
//	{
//		System.out.println("compareTo");
//
//		Date entryDate = new GregorianCalendar(2010, 5, 1).getTime();
//		Barcode testBarcode = new Barcode();
//		String prodDesc = "Test Product";
//		int shelfLife = 7;
//		Size size = new Size(2, SizeUnit.Gallons);
//
//		Product instance = new Product(entryDate, testBarcode, prodDesc, shelfLife, size);
//
//		Product other = instance;
//		int expResult = 0;
//		int result = instance.compareTo(other);
//		assertEquals(expResult, result);
//
//	}
//}
