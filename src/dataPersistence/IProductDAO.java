/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public interface IProductDAO {
	
	/**
	 * adds a product to the database
	 * @param product	the product to be added
	 */
	public boolean createProduct(ProductDTO product);
	
	/**
	 * gets all the products in the system
	 * @return	an iterator with a dto for all the products
	 */
	public Iterator<ProductDTO> getProducts();
	
	/**
	 * updates a product in the database
	 * @param product	the product with new info
	 */
	public boolean updateProduct(ProductDTO product);
	
	/**
	 * removes a product from the database
	 * @param product	the product to be removed
	 */
	public boolean removeProduct(ProductDTO product);
}
