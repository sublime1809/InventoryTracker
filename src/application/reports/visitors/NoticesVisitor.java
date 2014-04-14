/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.visitors;

import application.product.Product;
import application.storage.HomeStorage;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import application.storage.StorageUnitsManager;
import common.Notice;
import common.SizeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class NoticesVisitor implements Visitor {
	
    private List<Notice> notices = new ArrayList<Notice>();
    private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();

    public List<Notice> getNotices() {
            return notices;
    }

    @Override
    public String visitHomeStorage(HomeStorage hs) {
            //System.out.println("Visiting, but not doing anything with: " + hs.getName());
            return "";
    }
    
    @Override
    public String visitStorageUnit(StorageUnit su) {
		//System.out.println("Visiting, but not doing anything with: " + su.getName());
        return "";
    }
    
    @Override
    public String visitProductGroup(ProductGroup pg) {
		analyzeEverything(pg);
		
        return "";
    }
	
	private void analyzeProducts(ProductGroup pg, List<Product> p){
		//System.out.println("Getting notices from: " + pg.getName() 
		//		+ "\nNumber of products: " + p.size());
		SizeUnit pgUnit = pg.getThreeMonthSupply().getUnit();
		//no notices should be generated if the product groups three month supply is a count
		if(pgUnit == SizeUnit.Count){
			return;
		}
		Notice thisNotice = null;
		Iterator<Product> products = p.iterator();
		while(products.hasNext()){
			Product cur = products.next();
			SizeUnit curUnit = cur.getSize().getUnit();
			if(SizeUnit.isVolume(pgUnit) && !SizeUnit.isVolume(curUnit)){
				if(thisNotice == null){
					thisNotice = new Notice(pg, cur);
				}
				else{
					thisNotice.addProduct(cur);
				}
			}
			else if(SizeUnit.isWeight(pgUnit) && !SizeUnit.isWeight(curUnit)){
								if(thisNotice == null){
					thisNotice = new Notice(pg, cur);
				}
				else{
					thisNotice.addProduct(cur);
				}
			}
		}
	
		if(thisNotice != null){
			notices.add(thisNotice);
		}
	}
	
	private List<Product> analyzeEverything(ProductGroup pg){
		
		List<Product> allProducts = new ArrayList<Product>();
		Iterator<Product> products = storageUnits.getProducts(pg);
		while(products.hasNext()){
			allProducts.add(products.next());
		}
		
		Iterator<ProductContainer> children = storageUnits.getSubContainers(pg);
		while(children.hasNext()){
			allProducts.addAll(analyzeEverything((ProductGroup)children.next()));
		}
		analyzeProducts(pg, allProducts);
		
		return allProducts;
	}
    
}
