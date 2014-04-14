/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import application.item.Item;
import application.product.Product;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InvalidContainerException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author skitto
 */
public class StorageUnitsManagerTest
{

	private StorageUnitsManager manager;
	private StorageUnit su1;
	private StorageUnit su2;
	private StorageUnit su3;
	private ProductGroup pg1;
	private ProductGroup pg2;
	private ProductGroup pg3;
	private ProductGroup pg4;
	private ProductGroup pg5;
	private ProductGroup pg6;
	private ProductGroup pg7;

	public StorageUnitsManagerTest()
	{
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
	public void setUp()
	{
		try {
			manager = StorageUnitsManager.getInstance();
			su1 = new StorageUnit("StorageUnit1", null);
			su2 = new StorageUnit("StorageUnit2", null);
			su3 = new StorageUnit("StorageUnit3", null);
			pg1 = new ProductGroup("ProductGroup1", null);
			pg2 = new ProductGroup("ProductGroup2", null);
			pg3 = new ProductGroup("ProductGroup3", null);
			pg4 = new ProductGroup("ProductGroup4", null);
			pg5 = new ProductGroup("ProductGroup5", null);
			pg6 = new ProductGroup("ProductGroup6", null);
			pg7 = new ProductGroup("ProductGroup7", null);
			manager.addProductContainer(su1, null);
			manager.addProductContainer(su2, null);
			manager.addProductContainer(su3, null);
			manager.addProductContainer(pg1, su1);
			manager.addProductContainer(pg2, su1);
			manager.addProductContainer(pg3, pg1);
			manager.addProductContainer(pg4, su2);
			manager.addProductContainer(pg5, su2);
			manager.addProductContainer(pg6, pg5);
			manager.addProductContainer(pg7, su3);
		} catch (InvalidContainerException ex) {
			System.out.println("Set up failed");
		}
	}

	@After
	public void tearDown()
	{
		manager.reset();
	}
	
	
	//*********************Constraints checking*******************
	/**
	 * Checks the first constraint for Storage Units: 
	 *	name must be non-empty and unique
	 */
	@Test
	public void testConstraintSU1() throws Exception{
		System.out.println("Testing Constraint: Storage Unit name must be non-empty");

		StorageUnit testStorageUnit = new StorageUnit("", null);
		assertFalse(manager.canBeAdded(testStorageUnit, null));
		
		System.out.println("Testing Constraint: Storage Unit name must be unique");
		
		testStorageUnit.setName("StorageUnit1");
		assertFalse(manager.canBeAdded(testStorageUnit, null));
		try{
			manager.addProductContainer(testStorageUnit, null);
			assertTrue(false);
		}
		catch (InvalidContainerException e){
		}
		testStorageUnit.setName("ThisIsUnique");
		try{
			manager.addProductContainer(testStorageUnit, null);
		}
		catch (InvalidContainerException e){
			assertTrue(false);
		}
	}	

	/**
	 * Checks the second constraint for Storage Units: 
	 *	may not have two product groups with the same name
	 */
	@Test
	public void testConstraintSU2() throws Exception{
		System.out.println("Testing Constraint: "
				+ "Storage Unit may not have two Product Groups with the same name");
		
		ProductGroup testProductGroup = new ProductGroup("ProductGroup1", su1);
		assertFalse(manager.canBeAdded(testProductGroup, su1));
		assertTrue(manager.canBeAdded(testProductGroup, su2));
		try{
			manager.addProductContainer(testProductGroup, su1);
			assertTrue(false);
		}
		catch(InvalidContainerException e){
		}
		try{
			manager.addProductContainer(testProductGroup, su2);
		}
		catch(InvalidContainerException e){
			assertTrue(false);
		}
		testProductGroup.setName("ThisIsUnique");

		try{
			manager.addProductContainer(testProductGroup, su1);
		}
		catch(InvalidContainerException e){
			assertTrue(false);
		}
	}	

	/**
	 * Checks the first constraint for Product Groups: 
	 *	name must be non-empty and unique
	 */
	@Test
	public void testConstraintPG1() throws Exception{
		System.out.println("Testing Constraint: Product Group name must be non-empty");

		ProductGroup testProductGroup = new ProductGroup("", su2);
		assertFalse(manager.canBeAdded(testProductGroup, su2));
		
		System.out.println("Testing Constraint: Product Group name must be unique");
		
		testProductGroup.setName("ProductGroup4");
		assertFalse(manager.canBeAdded(testProductGroup, su2));
		testProductGroup.setName("ThisIsUnique");
		assertTrue(manager.canBeAdded(testProductGroup, su2));
	}

	/**
	 * Checks the first constraint for Product Groups: 
	 *	Parent Product Container must be non-empty
	 */
	@Test
	public void testConstraintPG2() throws Exception{
		System.out.println("Testing Constraint: Product Group's parent must be non-empty");

		ProductGroup testProductGroup = new ProductGroup("testProductGroup", null);
		assertFalse(manager.canBeAdded(testProductGroup, null));
		testProductGroup.setParent(su1);
		assertTrue(manager.canBeAdded(testProductGroup, su1));
		assertTrue(manager.canBeAdded(testProductGroup, su2));
	}

	/**
	 * Checks the first constraint for Product Groups: 
	 *	Three-month supply
	 */
	@Test
	public void testConstraintPG3() throws Exception{
		System.out.println("Testing Constraint: Three month supply");

		ProductGroup testProductGroup = new ProductGroup("testProductGroup", null);
		Size supply = new Size(-1, SizeUnit.Count);
		try{
			testProductGroup.setThreeMonthSupply(supply);
			assertTrue(false);
		}
		catch(IllegalArgumentException e){
		}
	}	

	/**
	 * Checks the first constraint for Product Groups: 
	 *	Not two children may have the same name
	 */
	@Test
	public void testConstraintPG4() throws Exception{
		System.out.println("Testing Constraint: "
				+ "Product Group many not have two children with the same name");
		
		ProductGroup testProductGroup = new ProductGroup("ProductGroup3", pg1);

		assertFalse(manager.canBeAdded(testProductGroup, pg1));
		testProductGroup.setName("ThisIsUnique");
		assertTrue(manager.canBeAdded(testProductGroup, pg1));
	}
}








//
//	/**
//	 * Test the name changing methods of class StorageUnitManager
//	 */
//	@Test
//	public void testNameChanging() throws Exception
//	{
//		System.out.println("nameChanges");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(pg1, su1);
//		manager.addProductContainer(pg2, su1);
//
//		assertTrue(manager.canChangeNameTo(pg2, pg2.getName()));
//		assertFalse(manager.canChangeNameTo(pg2, pg1.getName()));
//
//		assertEquals("ProductGroup2", pg2.getName());
//
//		manager.changeName(pg2, "ProductGroup3");
//
//		assertEquals("ProductGroup3", pg2.getName());
//
//		assertFalse(manager.canChangeNameTo(pg2, ""));
//	}
//
//	/**
//	 * Test of canBeAdded method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testCanBeAdded() throws Exception
//	{
//		System.out.println("CanBeAdded");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(pg1, su1);
//		manager.addProductContainer(pg2, su1);
//
//		ProductContainer pg3 = new ProductGroup("ProdctGroup3", su1);
//		assertTrue(manager.canBeAdded(pg3, su1));
//		pg3.setName("ProductGroup1");
//		assertFalse(manager.canBeAdded(pg3, su1));
//		//pg3.setName("");
//		//assertFalse(manager.canBeAdded(pg3, su1));
//	}
//
//	/**
//	 * Test of addProductContainer method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testAddProductContainer() throws Exception
//	{
//		System.out.println("AddProductContainer");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer su2 = new StorageUnit("StorageUnit2", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(su2, null);
//		manager.addProductContainer(pg1, su1);
//		manager.addProductContainer(pg2, su1);
//
//		Set<ProductContainer> expectedChildren = new HashSet<ProductContainer>();
//		expectedChildren.add(pg2);
//		expectedChildren.add(pg1);
//
//		Iterator<ProductContainer> temp = manager.getSubContainers(su1);
//		int subContainersFound = 0;
//
//		while (temp.hasNext())
//		{
//			ProductContainer cur = temp.next();
//			assertTrue(expectedChildren.contains(cur));
//			subContainersFound++;
//		}
//
//		assertEquals(2, subContainersFound);
//
//		ProductContainer su3 = new StorageUnit("StorageUnit1", null);
//		boolean failed = false;
//		try
//		{
//			manager.addProductContainer(su3, null);
//		}
//		catch (InvalidContainerException e)
//		{
//			failed = true;
//		}
//
//		assertTrue(failed);
//	}
//
//	/**
//	 * Test of canBeRemoved method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testCanBeRemoved() throws Exception
//	{
//		System.out.println("CanBeRemoved");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Item i1 = new Item();
//		i1.setBarcode(new Barcode());
//		manager.addProductContainer(su1, null);
//
//		assertTrue(manager.canBeRemoved(su1));
//
//		manager.addProduct(su1, p1);
//		assertFalse(manager.canBeRemoved(su1));
//		manager.removeProduct(su1, p1);
//		assertTrue(manager.canBeRemoved(su1));
//
//		manager.addItem(su1, i1);
//		assertFalse(manager.canBeRemoved(su1));
//		manager.removeItem(su1, i1);
//		assertTrue(manager.canBeRemoved(su1));
//	}
//
//	/**
//	 * Test of removeContainer method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testRemoveContainer() throws Exception
//	{
//		System.out.println("RemoveContainer");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer su12 = new StorageUnit("StorageUnit1", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		ProductContainer pg3 = new ProductGroup("ProductGroup3", su1);
//
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(pg1, su1);
//		manager.addProductContainer(pg2, su1);
//		manager.addProductContainer(pg3, su1);
//		manager.removeContainer(pg2);
//
//		Iterator<ProductContainer> temp = manager.getSubContainers(su1);
//		int subContainersFound = 0;
//
//		while (temp.hasNext())
//		{
//			ProductContainer cur = temp.next();
//			assertFalse(cur.equals(pg2));
//			subContainersFound++;
//		}
//
//		assertEquals(2, subContainersFound);
//
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		manager.addProduct(pg3, p1);
//		boolean failed = false;
//		try
//		{
//			manager.removeContainer(pg3);
//		}
//		catch (CannotBeRemovedException e)
//		{
//			failed = true;
//		}
//
//		assertTrue(failed);
//	}
//
//	/**
//	 * Test of getStorageUnit method, of class ProductContainer.
//	 */
//	@Test
//	public void testGetStorageUnit() throws Exception
//	{
//		System.out.println("getStorageUnit");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer su2 = new StorageUnit("StorageUnit2", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		ProductContainer pg3 = new ProductGroup("ProductGroup1", su2);
//		ProductContainer pg4 = new ProductGroup("ProductGroup4", su2);
//		ProductContainer pg5 = new ProductGroup("ProductGroup5", pg1);
//
//		try
//		{
//			manager.addProductContainer(su1, null);
//			manager.addProductContainer(su2, null);
//			manager.addProductContainer(pg1, su1);
//			manager.addProductContainer(pg2, su1);
//			manager.addProductContainer(pg3, su2);
//			manager.addProductContainer(pg4, su2);
//			manager.addProductContainer(pg5, pg1);
//		}
//		catch (Exception e)
//		{
//		}
//
//		assertTrue(su1.equals(manager.getStorageUnit(pg5)));
//		assertTrue(su1.equals(manager.getStorageUnit(su1)));
//		assertFalse(su2.equals(manager.getStorageUnit(pg2)));
//		assertTrue(su2.equals(manager.getStorageUnit(pg4)));
//	}
//
//	/**
//	 * Test of hasProduct method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testHasProduct() throws Exception
//	{
//		System.out.println("hasProduct");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		manager.addProductContainer(su1, null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Product p2 = new Product(null, new Barcode(), null, 0, null);
//		Product p3 = new Product(null, new Barcode(), null, 0, null);
//
//		assertFalse(manager.hasProduct(su1, p1));
//
//		manager.addProduct(su1, p1);
//		manager.addProduct(su1, p2);
//
//		assertTrue(manager.hasProduct(su1, p1));
//		assertFalse(manager.hasProduct(su1, p3));
//	}
//
//	/**
//	 * Test of addProduct method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testAddProduct() throws Exception
//	{
//		System.out.println("addProduct");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		manager.addProductContainer(su1, null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Product p2 = new Product(null, new Barcode(), null, 0, null);
//		manager.addProduct(su1, p1);
//		manager.addProduct(su1, p2);
//
//		Iterator<Product> productsAdded = manager.getProducts(su1);
//		Set<Product> expectedProducts = new HashSet<Product>();
//		expectedProducts.add(p1);
//		expectedProducts.add(p2);
//		int productsFound = 0;
//
//		while (productsAdded.hasNext())
//		{
//			productsFound++;
//			Product cur = productsAdded.next();
//			assertTrue(expectedProducts.contains(cur));
//		}
//
//		assertEquals(2, productsFound);
//	}
//
//	/**
//	 * Test of canRemoveProduct method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testCanRemoveProduct() throws Exception
//	{
//		System.out.println("canRemoveProduct");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit", null);
//		manager.addProductContainer(su1, null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Product p2 = new Product(null, new Barcode(), null, 0, null);
//		Item i1 = new Item();
//		i1.setBarcode(new Barcode());
//		i1.setProduct(p1);
//		manager.addProduct(su1, p1);
//		manager.addProduct(su1, p2);
//		manager.addItem(su1, i1);
//
//		assertTrue(manager.canRemoveProduct(su1, p2));
//		assertFalse(manager.canRemoveProduct(su1, p1));
//	}
//
//	/**
//	 * Test of removeProduct method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testRemoveProduct() throws Exception
//	{
//		System.out.println("removeProduct");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		manager.addProductContainer(su1, null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Product p2 = new Product(null, new Barcode(), null, 0, null);
//		Product p3 = new Product(null, new Barcode(), null, 0, null);
//		manager.addProduct(su1, p1);
//		manager.addProduct(su1, p2);
//		manager.addProduct(su1, p3);
//		manager.removeProduct(su1, p2);
//
//		Iterator<Product> productsAdded = manager.getProducts(su1);
//		Set<Product> expectedProducts = new HashSet<Product>();
//		expectedProducts.add(p1);
//		expectedProducts.add(p3);
//		int productsFound = 0;
//
//		while (productsAdded.hasNext())
//		{
//			productsFound++;
//			Product cur = productsAdded.next();
//			assertTrue(expectedProducts.contains(cur));
//			assertFalse(cur.equals(p2));
//		}
//
//		assertEquals(2, productsFound);
//
//		ProductContainer su2 = new StorageUnit("StorageUnit2", null);
//		ProductContainer su3 = new StorageUnit("StorageUnit3", null);
//		manager.addProductContainer(su2, null);
//		manager.addProductContainer(su3, null);
//		manager.addProduct(su3, p1);
//		manager.removeProduct(null, p1);
//
//		assertFalse(manager.hasProduct(su1, p1));
//		assertFalse(manager.hasProduct(su3, p1));
//	}
//
//	/**
//	 * Test of getContainerThatHasProduct method, of class ProductContainer.
//	 */
//	@Test
//	public void testGetContainerThatHasProduct() throws Exception
//	{
//		System.out.println("getStorageUnit");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer su2 = new StorageUnit("StorageUnit2", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		ProductContainer pg3 = new ProductGroup("ProductGroup1", su2);
//		ProductContainer pg4 = new ProductGroup("ProductGroup4", su2);
//		ProductContainer pg5 = new ProductGroup("ProductGroup5", pg1);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//
//		try
//		{
//			manager.addProductContainer(su1, null);
//			manager.addProductContainer(su2, null);
//			manager.addProductContainer(pg1, su1);
//			manager.addProductContainer(pg2, su1);
//			manager.addProductContainer(pg3, su2);
//			manager.addProductContainer(pg4, su2);
//			manager.addProductContainer(pg5, pg1);
//			manager.addProduct(pg1, p1);
//
//			assertTrue(manager.hasProduct(pg1, p1));
//		}
//		catch (Exception e)
//		{
//			System.out.println("hello");
//		}
//
//		ProductContainer test = manager.getContainerThatHasProduct(su1, p1);
//		assertTrue(test != null);
//		assertTrue(pg1.equals(test));
//	}
//
//	/**
//	 * Test of addItem method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testAddItem() throws Exception
//	{
//		System.out.println("addItem");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		manager.addProductContainer(su1, null);
//		Item i1 = new Item();
//		Item i2 = new Item();
//		i1.setBarcode(new Barcode());
//		i2.setBarcode(new Barcode());
//		manager.addItem(su1, i1);
//		manager.addItem(su1, i2);
//
//		Iterator<Item> itemsAdded = manager.getItems(su1);
//		Set<Item> expectedItems = new HashSet<Item>();
//		expectedItems.add(i1);
//		expectedItems.add(i2);
//		int itemsFound = 0;
//
//		while (itemsAdded.hasNext())
//		{
//			itemsFound++;
//			Item cur = itemsAdded.next();
//			assertTrue(expectedItems.contains(cur));
//		}
//
//		assertEquals(2, itemsFound);
//	}
//
//	/**
//	 * Test of removeItem method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testRemoveItem() throws Exception
//	{
//		System.out.println("removeItem");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		manager.addProductContainer(su1, null);
//		Item i1 = new Item();
//		Item i2 = new Item();
//		Item i3 = new Item();
//		i1.setBarcode(new Barcode());
//		i2.setBarcode(new Barcode());
//		i3.setBarcode(new Barcode());
//		manager.addItem(su1, i1);
//		manager.addItem(su1, i2);
//		manager.addItem(su1, i3);
//		manager.removeItem(su1, i2);
//
//		Iterator<Item> itemsAdded = manager.getItems(su1);
//		Set<Item> expectedItems = new HashSet<Item>();
//		expectedItems.add(i1);
//		expectedItems.add(i3);
//		int itemsFound = 0;
//
//		while (itemsAdded.hasNext())
//		{
//			itemsFound++;
//			Item cur = itemsAdded.next();
//			assertTrue(expectedItems.contains(cur));
//			assertFalse(cur.equals(i2));
//		}
//
//		assertEquals(2, itemsFound);
//	}
//
//	/**
//	 * Test of getItemsOfProduct method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testGetItemsOfProduct() throws Exception
//	{
//		System.out.println("getItemsOfProduct");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		Product p1 = new Product(null, new Barcode(), null, 0, null);
//		Product p2 = new Product(null, new Barcode(), null, 0, null);
//		manager.addProductContainer(su1, null);
//		Item i1 = new Item();
//		Item i2 = new Item();
//		Item i3 = new Item();
//		i1.setBarcode(new Barcode());
//		i2.setBarcode(new Barcode());
//		i3.setBarcode(new Barcode());
//		i1.setProduct(p1);
//		i2.setProduct(p2);
//		i3.setProduct(p1);
//		manager.addItem(su1, i1);
//		manager.addItem(su1, i2);
//		manager.addItem(su1, i3);
//
//		Iterator<Item> items = manager.getItemsOfProduct(su1, p1);
//		Set<Item> expectedItems = new HashSet<Item>();
//		expectedItems.add(i1);
//		expectedItems.add(i3);
//		int itemsFound = 0;
//
//		while (items.hasNext())
//		{
//			itemsFound++;
//			Item cur = items.next();
//			assertTrue(expectedItems.contains(cur));
//			assertFalse(cur.equals(i2));
//		}
//
//		assertEquals(2, itemsFound);
//	}
//
//	/**
//	 * tests the getting and setting functionality of the class
//	 */
//	@Test
//	public void testThreeMonthSupply() throws Exception
//	{
//		System.out.println("threeMonthSupply");
//
//		float mag = 57;
//		Size size = new Size(mag, SizeUnit.Pounds);
//		ProductContainer su1 = new StorageUnit("StorageUnit", null);
//		ProductContainer cont = new ProductGroup("ProductGroup", su1, size);
//		manager.addProductContainer(su1, null);
//		manager.addProductContainer(cont, su1);
//
//		assertTrue(mag == ((ProductGroup) cont).getThreeMonthSupply().getSize());
//		assertTrue(mag == manager.getThreeMonthSupply(cont).getSize());
//		assertEquals(SizeUnit.Pounds, manager.getThreeMonthSupply(cont).getUnit());
//
//		mag = 357;
//		Size size2 = new Size(mag, SizeUnit.Count);
//		manager.setThreeMonthSupply(cont, size2);
//		assertTrue(mag == manager.getThreeMonthSupply(cont).getSize());
//		assertEquals(SizeUnit.Count, manager.getThreeMonthSupply(cont).getUnit());
//
//		size.setSize(-1);
//		boolean failed = false;
//		try
//		{
//			manager.setThreeMonthSupply(cont, size);
//		}
//		catch (IllegalArgumentException e)
//		{
//			failed = true;
//		}
//
//		assertTrue(failed);
//	}
//
//	/**
//	 * Test of update method, of class StorageUnitsManager.
//	 */
//	@Test
//	public void testUpdate() throws Exception
//	{
//		System.out.println("update");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit1", null);
//		ProductContainer su2 = new StorageUnit("StorageUnit2", null);
//		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
//		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
//		ProductContainer pg3 = new ProductGroup("ProductGroup1", su2);
//		ProductContainer pg4 = new ProductGroup("ProductGroup4", su2);
//		ProductContainer pg5 = new ProductGroup("ProductGroup5", pg1);
//
//		try
//		{
//			manager.addProductContainer(su1, null);
//			manager.addProductContainer(su2, null);
//			manager.addProductContainer(pg1, su1);
//			manager.addProductContainer(pg2, su1);
//			manager.addProductContainer(pg3, su2);
//			manager.addProductContainer(pg4, su2);
//			manager.addProductContainer(pg5, pg1);
//		}
//		catch (Exception e)
//		{
//			System.out.println("hello");
//		}
//
//		ProductContainer testContainer = new ProductGroup(pg5.getProductContainerID());
//		testContainer = manager.update(testContainer);
//		assertTrue(testContainer.equals(pg5));
//		assertTrue(testContainer.getName().equals(pg5.getName()));
//		assertTrue(testContainer.getParent().equals(pg5.getParent()));
//		assertEquals(testContainer.getChildCount(), pg5.getChildCount());
//	}
//}
