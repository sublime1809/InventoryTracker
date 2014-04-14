/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.visitors;

import application.storage.HomeStorage;
import application.storage.ProductGroup;
import application.storage.StorageUnit;

/**
 *
 * @author owner
 */
public interface Visitor {
	
    public String visitHomeStorage(HomeStorage hs);
    
    public String visitStorageUnit(StorageUnit su);
    
    public String visitProductGroup(ProductGroup pg);
}
