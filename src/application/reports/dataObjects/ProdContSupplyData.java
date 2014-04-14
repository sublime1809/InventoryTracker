/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.dataObjects;

import application.product.Product;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import common.Size;

/**
 *
 * @author lgunter
 */
public class ProdContSupplyData {
    
    private ProductGroup pg;
    private StorageUnit su;
    private Size quota;
    private Size count;
    
    public ProdContSupplyData(ProductGroup _pg, StorageUnit _su,
                                Size _quota, Size _count){
        assert _pg != null;
        assert _su != null;
        assert _quota != null;
        assert _count != null;
        
        pg = _pg;
        su = _su;
        quota = _quota;
        count = _count;
    }

    
    public ProductGroup getProductGroup(){
        return pg;
    }
    
    public StorageUnit getStorageUnit(){
        return su;
    }
    
    public Size getQuota(){
        return quota;
    }
    
    public Size getCount(){
        return count;
    }
}
