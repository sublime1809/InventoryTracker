/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.PersistenceManager;
import dataPersistence.ProductDTO;
import java.util.Iterator;
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
public class SQLProductDAOTest {
    
    public SQLProductDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
//        PersistenceManager persist = PersistenceManager.getInstance();
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        PersistenceManager.getInstance().makeNewFactory("-sql");
        PersistenceManager.getInstance().startTransaction();
    }
    
    @After
    public void tearDown() {
        PersistenceManager.getInstance().finishTransaction(true);
    }

    /**
     * Test of createProduct method, of class SQLProductDAO.
     */
    @Test
    public void testCreateProduct() {
        System.out.println("createProduct");
        ProductDTO product = new ProductDTO(1, "SOME STUFF", "123456788765", 12, 2.4f, "count", 3.4f);
        SQLProductDAO instance = new SQLProductDAO();
        instance.createProduct(product);
        
        String exp = "SOME STUFF";
        
        PersistenceManager.getInstance().finishTransaction(true);
        PersistenceManager.getInstance().startTransaction();
        Iterator<ProductDTO> result = instance.getProducts();
        boolean found = false;
        while(result.hasNext()) {
            if(exp.equals(result.next().getDescription())) {
                found = true;
            }
        }
        assertTrue(found);
    }

    /**
     * Test of getProducts method, of class SQLProductDAO.
     */
    @Test
    public void testGetProducts() {
        System.out.println("getProducts");
        SQLProductDAO instance = new SQLProductDAO();
        
//        Iterator expResult = null;
        Iterator result = instance.getProducts();
        while(result.hasNext()) {
            System.out.println(result.next().toString());
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProduct method, of class SQLProductDAO.
     */
    @Test
    public void testUpdateProduct() {
        System.out.println("updateProduct");
        ProductDTO product = new ProductDTO(1, "SOME STUFF", "123456788765", 12, 2.4f, "count", 3.4f);
        SQLProductDAO instance = new SQLProductDAO();
        instance.createProduct(product);
        
        PersistenceManager.getInstance().finishTransaction(true);
        PersistenceManager.getInstance().startTransaction();
        Iterator<ProductDTO> result = instance.getProducts();

        ProductDTO update = result.next();
        update.setDescription("Something new");
        
        instance.updateProduct(update);
        PersistenceManager.getInstance().finishTransaction(true);
        PersistenceManager.getInstance().startTransaction();
        Iterator<ProductDTO> resultremoved = instance.getProducts();
        boolean updated = false;
        while(resultremoved.hasNext()) {
            ProductDTO temp = resultremoved.next();
            if(update.getID() == temp.getID()) {
                if(temp.getDescription().equals("Something new")) {
                    updated = true;
                }
            }
        }
        assertTrue(updated);
    }

    /**
     * Test of removeProduct method, of class SQLProductDAO.
     */
    @Test
    public void testRemoveProduct() {
        System.out.println("removeProduct");
        ProductDTO product = new ProductDTO(1, "SOME STUFF", "123456788765", 12, 2.4f, "count", 3.4f);
        SQLProductDAO instance = new SQLProductDAO();
        instance.createProduct(product);
        
        PersistenceManager.getInstance().finishTransaction(true);
        PersistenceManager.getInstance().startTransaction();
        Iterator<ProductDTO> result = instance.getProducts();

        ProductDTO remove = result.next();
        
        instance.removeProduct(remove);
        PersistenceManager.getInstance().finishTransaction(true);
        PersistenceManager.getInstance().startTransaction();
        Iterator<ProductDTO> resultremoved = instance.getProducts();
        boolean found = false;
        while(resultremoved.hasNext()) {
            if(remove.getID() == resultremoved.next().getID()) {
                found = true;
            }
        }
        assertFalse(found);
    }
}
