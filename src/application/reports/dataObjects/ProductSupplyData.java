/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.dataObjects;

import application.product.Product;
import common.Size;

/**
 *
 * @author lgunter
 */
public class ProductSupplyData {
    
    private Product product;
    private Size quota;
    private Size count;
    
    public ProductSupplyData(Product _product, Size _quota, Size _count){
        assert _product != null;
        assert _quota != null;
        assert _count != null;
        
        product = _product;
        quota = _quota;
        count = _count;
    }
    
    public Product getProduct(){
        return product;
    }
    
    public Size getQuota(){
        return quota;
    }
    
    public Size getCount(){
        return count;
    }
    
}
