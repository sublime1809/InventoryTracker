/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import common.exceptions.InvalidContainerException;
import java.util.ArrayList;
import java.util.Iterator;
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
public class ProductContainerTest
{

	private HomeStorage root;

	public ProductContainerTest()
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
	public void setUp() throws Exception
	{
		root = new HomeStorage();
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getName method, of class ProductContainer.
	 */
	@Test
	public void testGetName() throws Exception
	{
		System.out.println("getName");

		ProductContainer instance = new StorageUnit("test", null);
		ProductContainer instance2 = new ProductGroup("test2", instance);

		assertEquals("rootUnit", root.getName());
		assertEquals("test", instance.getName());
		assertEquals("test2", instance2.getName());
	}

	/**
	 * Test of setName method, of class ProductContainer.
	 */
	@Test
	public void testSetName() throws Exception
	{
		System.out.println("setName");

		ProductContainer instance = new StorageUnit("test", null);
		instance.setName("newTest");

		assertEquals("newTest", instance.getName());
	}

	/**
	 * Test of getParent method, of class ProductContainer.
	 */
	@Test
	public void testGetParent() throws Exception
	{
		System.out.println("getParent");

		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer pg1 = new StorageUnit("StorageUnit1", su1);

		assertEquals(null, root.getParent());
		assertEquals(root, su1.getParent());
		assertEquals(su1, pg1.getParent());
	}

	/**
	 * Test of setParent method, of class ProductContainer.
	 */
	@Test
	public void testSetParent() throws Exception
	{
		System.out.println("setParent");

		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer su2 = new StorageUnit("StorageUnit2", root);
		ProductContainer pg1 = new StorageUnit("StorageUnit1", su1);
		pg1.setParent(su2);

		assertEquals(su2, pg1.getParent());

		boolean failed = false;
		try
		{
			pg1.setParent(null);
		}
		catch (InvalidContainerException e)
		{
			failed = true;
		}

		assertTrue(failed);

		failed = false;
		pg1 = new ProductGroup("StorageUnit1", null);
	}

	/**
	 * Test of getChildCound method, of class ProductContainer.
	 */
	@Test
	public void testGetChildCount() throws Exception
	{
		System.out.println("getChildCount");

		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer su2 = new StorageUnit("StorageUnit2", root);

		assertEquals(0, root.getChildCount());

		try
		{
			root.addSubContainer(su1);
			root.addSubContainer(su2);
		}
		catch (Exception e)
		{
		}
		assertEquals(2, root.getChildCount());
	}

	/**
	 * Test of getSubContainers method, of class ProductContainer.
	 */
	@Test
	public void testGetSubContainers() throws Exception
	{
		System.out.println("getSubContainers");

		ArrayList<ProductContainer> expectedChildren = new ArrayList<>();
		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer su2 = new StorageUnit("StorageUnit2", root);
		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);

		try
		{
			root.addSubContainer(su1);
			su1.addSubContainer(pg1);
			root.addSubContainer(su2);
			expectedChildren.add(su1);
			expectedChildren.add(su2);
		}
		catch (Exception e)
		{
		}

		Iterator<ProductContainer> actualItr = root.getSubContainers();
		int childrenFound = 0;

		while (actualItr.hasNext())
		{
			ProductContainer cur = actualItr.next();
			assertTrue(expectedChildren.contains(cur));
			childrenFound++;
		}

		assertEquals(2, childrenFound);
	}

	/**
	 * Test of addSubContainer method, of class ProductContainer.
	 */
	@Test
	public void testAddSubContainer() throws Exception
	{
		System.out.println("addSubContainer");

		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);

		//try {
		root.addSubContainer(su1);
		su1.addSubContainer(pg1);
		// }
       /* catch (InvalidContainerException e){
		 fail();
		 }        
		 try {
		 root.addSubContainer(pg1);
		 fail();
		 }
		 catch (InvalidContainerException e){
		 }        
		 try {
		 su1.addSubContainer(su1);
		 fail();
		 }
		 catch (InvalidContainerException e){
		 }        
		 try {
		 pg1.addSubContainer(pg1);
		 }
		 catch (InvalidContainerException e){
		 fail();
		 }*/
	}

	/**
	 * Test of removeSubContainer method, of class ProductContainer.
	 */
	@Test
	public void testRemoveSubContainer() throws Exception
	{
		System.out.println("removeSubContainer");

		ArrayList<ProductContainer> expectedChildren = new ArrayList<>();
		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer su2 = new StorageUnit("StorageUnit2", root);
		ProductContainer su3 = new StorageUnit("StorageUnit3", su1);

		try
		{
			root.addSubContainer(su1);
			root.addSubContainer(su2);
			root.addSubContainer(su3);
			expectedChildren.add(su1);
			expectedChildren.add(su2);
			expectedChildren.add(su3);

			root.removeSubContainer(su2);
			expectedChildren.remove(su2);
		}
		catch (Exception e)
		{
		}

		Iterator<ProductContainer> actualItr = root.getSubContainers();
		int childrenFound = 0;

		while (actualItr.hasNext())
		{
			ProductContainer cur = actualItr.next();
			assertTrue(expectedChildren.contains(cur));
			assertFalse(cur.equals(su2));
			childrenFound++;
		}

		assertEquals(2, childrenFound);
	}

	/**
	 * Test of findContainer method, of class ProductContainer.
	 */
	@Test
	public void testFindContainer() throws Exception
	{
		System.out.println("findContainer");

		ProductContainer su1 = new StorageUnit("StorageUnit1", root);
		ProductContainer su2 = new StorageUnit("StorageUnit2", root);
		ProductContainer pg1 = new ProductGroup("ProductGroup1", su1);
		ProductContainer pg2 = new ProductGroup("ProductGroup2", su1);
		ProductContainer pg3 = new ProductGroup("ProductGroup1", su2);
		ProductContainer pg4 = new ProductGroup("ProductGroup4", su2);
		ProductContainer pg5 = new ProductGroup("ProductGroup5", pg1);

		try
		{
			root.addSubContainer(su1);
			root.addSubContainer(su2);
			su1.addSubContainer(pg1);
			su1.addSubContainer(pg2);
			su2.addSubContainer(pg3);
			su2.addSubContainer(pg4);
			pg1.addSubContainer(pg5);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Test of equals method, of class ProductContainer.
	 */
	@Test
	public void testEquals() throws Exception
	{
		System.out.println("equals");

		ProductContainer cont1 = new StorageUnit("StorageUnit1", root);
		ProductContainer cont2 = new StorageUnit("StorageUnit2", root);
		ProductContainer cont3 = new ProductGroup("ProductGroup1", cont1);
		ProductContainer cont4 = new ProductGroup("ProductGroup2", cont1);


		assertFalse(cont1.equals(cont2));
		assertFalse(cont3.equals(cont4));
		assertTrue(cont3.equals(cont3));
	}
	/*public class ProductContainerImpl extends ProductContainer {

	 public ProductContainerImpl() {
	 super("", null);
	 }
	 }*/
}
