/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chrystal
 */
public class BarcodeTest
{

	public BarcodeTest()
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
	 * Test of generate method, of class Barcode.
	 */
	@Test
	public void testGenerate()
	{
		System.out.println("generate");
		Barcode result = Barcode.generate();
		assertNotNull(result);

		boolean isValid = Barcode.isValid(result);
		assertTrue(isValid);
//         TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
	}

	/**
	 * Test of isValidValue method, of class Barcode.
	 */
	@Test
	public void testIsValid()
	{
		System.out.println("isValid");
		String value = "036000291452";
		boolean result = Barcode.isValid(new Barcode(value));
		assertTrue(result);

		String value2 = "036000291453";
		boolean result2 = Barcode.isValid(new Barcode(value2));
		assertFalse(result2);
	}

	/**
	 * Test of compareTo method, of class Barcode.
	 */
	@Test
	public void testCompareTo()
	{
		System.out.println("compareTo");
		Barcode other = new Barcode();
		Barcode instance = new Barcode();
		int expResult = 1;
		int result = instance.compareTo(other);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
	}
}
