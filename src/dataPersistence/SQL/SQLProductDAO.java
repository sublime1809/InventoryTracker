/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SQL;

import dataPersistence.IProductDAO;
import dataPersistence.PersistenceManager;
import dataPersistence.ProductDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stephen Kitto
 */
public class SQLProductDAO extends SqlDao implements IProductDAO {
        
	
        private String tableName = "Product";
	private String[] columns = { "ID", "Barcode", "Description", "ShelfLife", "MSValue", 
		"SizeUnit", "SizeValue" };
        private String[] createColumns = { "Barcode", "Description", "ShelfLife", "MSValue", 
		"SizeUnit", "SizeValue" };
        private SQLTypes[] types = { SQLTypes.Integer, SQLTypes.String, SQLTypes.String, 
			SQLTypes.Integer, SQLTypes.Float, SQLTypes.String, SQLTypes.Float };
        private SQLTypes[] createTypes = { SQLTypes.String, SQLTypes.String, 
			SQLTypes.Integer, SQLTypes.Float, SQLTypes.String, SQLTypes.Float };
        
        public SQLProductDAO() {
            super.setTableName(this.tableName);
            super.setColumns(this.columns);
            super.setCreateColumns(this.createColumns);
        }
        
	/**
	 * adds a product to the database
	 * @param product	the product to be added
	 */
	@Override
	public boolean createProduct(ProductDTO product){
		
			//System.out.println("Creating a new product!");
		
            Object[] values = new Object[this.createColumns.length];
            values[0] = product.getBarcode();
            values[1] = product.getDescription();
            values[2] = product.getShelflife();
            values[3] = product.getThreeMonthSupply();
            values[4] = product.getSizeUnit();
            values[5] = product.getSizeValue();
            
	   return super.create(product, createTypes, values);
	}
	
	/**
	 * gets all the products in the system
	 * @return	an iterator with a dto for all the products
	 */
	@Override
	public Iterator<ProductDTO> getProducts(){
		//System.out.println("Getting products!");
            ResultSet rs = this.read();
            List<ProductDTO> products = new ArrayList<ProductDTO>();
//            { "ID", "Barcode", "Description", "ShelfLife", "3MSValue", 
            //"SizeUnitID", "SizeValue" }
            try {
                while(rs.next()) {
                    int id = rs.getInt("ID");
                    String barcode = rs.getString("Barcode");
                    String description = rs.getString("Description");
                    int shelfLife = rs.getInt("ShelfLife");
                    int threeMonthValue = rs.getInt("MSValue");
                    String sizeUnit = rs.getString("SizeUnit");
                    int sizeValue = rs.getInt("SizeValue");
                    
                    ProductDTO temp = new ProductDTO(id, description, barcode, shelfLife, sizeValue,
							sizeUnit, threeMonthValue);
                    products.add(temp);
                }
                return products.iterator();
            } catch(SQLException e) {
                return null;
            }
	}
	
	/**
	 * updates a product in the database
	 * @param product	the product with new info
	 */
	@Override
	public boolean updateProduct(ProductDTO product){
		
		//System.out.println("Updating a product!");
            Object[] values = new Object[this.createColumns.length];
            values[0] = product.getBarcode();
            values[1] = product.getDescription();
            values[2] = product.getShelflife();
            values[3] = product.getThreeMonthSupply();
            values[4] = product.getSizeUnit();
            values[5] = product.getSizeValue();
               
            return super.edit(product, this.createTypes, values);
            
	}
	
	/**
	 * removes a product from the database
	 * @param product	the product to be removed
	 */
	@Override
	public boolean removeProduct(ProductDTO product){
		//System.out.println("Removing a product!");
	    return super.delete(product);
	}
}
