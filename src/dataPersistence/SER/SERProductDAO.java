/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SER;

import dataPersistence.IProductDAO;
import dataPersistence.ProductDTO;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Stephen Kitto
 */
public class SERProductDAO implements IProductDAO, Serializable{
	
		
	/**
	 * does nothing
	 */
	public boolean createProduct(ProductDTO product){
		return true;
	}
	
	/**
	 * does nothing
	 */
	public Iterator<ProductDTO> getProducts(){
		return null;
	}
	
	/**
	 * does nothing
	 */
	public boolean updateProduct(ProductDTO product){
		return true;
	}
	
	/**
	 * does nothing
	 */
	public boolean removeProduct(ProductDTO product){
		return true;
	}
	
}
