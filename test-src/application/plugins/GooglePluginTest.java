/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

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
public class GooglePluginTest
{

	GooglePlugin plugin;

	public GooglePluginTest()
	{
		plugin = new GooglePlugin();
	}

	@BeforeClass
	public static void setUpClass()
	{
		String me = "trevor";
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
	 * Test of findProduct method, of class GooglePlugin.
	 */
	@Test
	public void testFindProduct()
	{
		System.out.println("findProduct");
		assertTrue(testFindProductHelper("00846042000505", "Tcl - 40\" Class - Lcd - 1080p - 60hz - Hdtv"));
	}

	@Test
	public void testFindProduct2()
	{
		assertTrue(testFindProductHelper("00180530000616", "Cytosport Muscle Milk RTD, Chocolate, 12 ea"));
	}

	@Test
	public void testFindProduct3()
	{
		assertTrue(testFindProductHelper("000000000", null));
	}
	
	@Test
	public void testFindProduct4()
	{
		assertTrue(testFindProductHelper("B000KKEMXG", null));
	}
	
	@Test
	public void testFindProduct5()
	{
		assertTrue(testFindProductHelper("3474700048", null));
	}
	
	@Test
	public void testFindProduct6()
	{
		assertTrue(testFindProductHelper("3485602898", null));
	}
	
	@Test
	public void testFindProduct7()
	{
		assertTrue(testFindProductHelper("5150006659", null));
	}
	
	@Test
	public void testFindProduct8()
	{
		assertTrue(testFindProductHelper("032903085396", "Akro-mils 1092793 30420 Dividable Plastic Storage Nestable"));
	}

	private boolean testFindProductHelper(String barcode, String descrip)
	{
		WebProduct expResult = new WebProduct(descrip);
		WebProduct result = plugin.findProduct(barcode);

		if (result != null)
		{
			System.out.println("Looking for: " + descrip);
			System.out.println("in: " + result.description);
			return expResult.description.indexOf(result.description) != -1;
		}
		else
		{
			return expResult.description == null;
		}
	}
}
