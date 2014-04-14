/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

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
public class UPCDatabasePluginORGTest {
	
	public UPCDatabasePluginORGTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of findProduct method, of class UPCDatabasePluginORG.
	 */
	@Test
	public void testFindProduct() {
		System.out.println("findProduct");
		String barcode = "0097363485049";
		UPCDatabasePluginORG instance = new UPCDatabasePluginORG();
		WebProduct expResult = new WebProduct(" Widescreen DVD Star Trek (2009)");
		WebProduct result = instance.findProduct(barcode);
		//System.out.println(instance.getClass().getName());
		assertEquals(expResult.description, result.description);
	}
}
