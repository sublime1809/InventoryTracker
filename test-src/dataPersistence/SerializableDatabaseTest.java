/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import application.storage.ProductContainer;
import application.storage.StorageUnit;
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
public class SerializableDatabaseTest
{

	public SerializableDatabaseTest()
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
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of saveObject method, of class SerializableDatabase.
	 */
	@Test
	public void testSerialization() throws Exception
	{
//		System.out.println("testSerialization");
//
//		ProductContainer su1 = new StorageUnit("StorageUnit", null);
//		ProductContainer testEquivalent = new StorageUnit(su1.getProductContainerID());
//		SerializableDatabase.saveObject(su1, "testFile.ser");
//
//		assertTrue(su1.equals(testEquivalent));
//
//		su1 = new StorageUnit("badStorageUnit", null);
//
//		assertFalse(su1.equals(testEquivalent));
//
//		su1 = (StorageUnit) SerializableDatabase.loadObject(su1, "testFile.ser");
//
//		assertTrue(su1.equals(testEquivalent));
//		assertTrue(su1.getName().equals("StorageUnit"));
	}
}
