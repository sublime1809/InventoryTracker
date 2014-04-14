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
 * @author skitto
 */
public class PluginRegistryTest {
	
	public PluginRegistryTest() {
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
	 * Test of loadPlugins method, of class PluginRegistry.
	 */
	@Test
	public void testLoadPlugins() {
		System.out.println("loadPlugins");
		PluginRegistry instance = new PluginRegistry();
		instance.loadPlugins();
	}
}
