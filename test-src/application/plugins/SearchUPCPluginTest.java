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
 * @author owner
 */
public class SearchUPCPluginTest {
    
    public SearchUPCPluginTest() {
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
     * Test of findProduct method, of class SearchUPCPlugin.
     */
    @Test
    public void testFindProduct() {
        System.out.println("findProduct");
        String barcode = "";
        SearchUPCPlugin instance = new SearchUPCPlugin();
        
        WebProduct result = instance.findProduct("030000410004");
        
        assertTrue(result.getDescription().startsWith("Quaker oats"));
    }

    /**
     * Test of setSuccessor method, of class SearchUPCPlugin.
     */
    @Test
    public void testSetSuccessor() {
//        System.out.println("setSuccessor");
//        BarcodePlugin plugin = null;
//        SearchUPCPlugin instance = new SearchUPCPlugin();
//        instance.setSuccessor(plugin);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
