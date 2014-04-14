/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.visitors;

import application.item.Item;
import application.storage.HomeStorage;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import application.storage.StorageUnitsManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class ExpiredItemsVisitor implements Visitor {
	
	private List<Item> expiredItems = new ArrayList<Item>();
	private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();

    @Override
    public String visitHomeStorage(HomeStorage hs) {
            //System.out.println("getting the expired items from: " + hs.getName());
            return "";
    }
    
    @Override
    public String visitStorageUnit(StorageUnit su) {
		addExpiredItems(su);
		return "";
    }
    
    @Override
    public String visitProductGroup(ProductGroup pg) {
		addExpiredItems(pg);
		return "";
    }
	
    public Iterator<Item> getExpiredItems(){
            return expiredItems.iterator();
    }

    private void addExpiredItems(ProductContainer container){
            //System.out.println("getting the expired items from: " + container.getName());

            Iterator<Item> items = storageUnits.getAllItemsInContainers(container);
            while(items.hasNext()){
                    Item cur = items.next();
                    Date expirationDate = cur.getExpirationDate();
                    if(expirationDate != null && expirationDate.before(new Date())){
                            expiredItems.add(cur);
                    }
            }
    }
    
}
