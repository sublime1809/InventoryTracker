/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.visitors;

import application.ProductFacade;
import application.product.Product;
import application.reports.dataObjects.ProdContSupplyData;
import application.reports.dataObjects.ProductSupplyData;
import application.storage.HomeStorage;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import application.storage.StorageUnitsManager;
import common.Size;
import common.SizeUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class SupplyVisitor implements Visitor {

    private int monthSupply = 0;
    private List<Product> products;
    private List<ProductGroup> pgs;
    private List<ProductSupplyData> productSupplies;
    private List<ProdContSupplyData> prodContSupplies;
    private StorageUnitsManager storageUnitManager;
    
    public SupplyVisitor(int _monthSupply) throws Exception {
        assert _monthSupply >= 1;
        assert _monthSupply <= 100;
        
        this.monthSupply = _monthSupply;
        products = new ArrayList<Product>();
        pgs = new ArrayList<ProductGroup>();
        productSupplies = new ArrayList<ProductSupplyData>();
        prodContSupplies = new ArrayList<ProdContSupplyData>();
        storageUnitManager = StorageUnitsManager.getInstance();
    }
    
    @Override
    public String visitHomeStorage(HomeStorage hs) {
//        throw new UnsupportedOperationException("Not supported yet.");
        return "";
    }
    
    @Override
    public String visitStorageUnit(StorageUnit su) {
//        throw new UnsupportedOperationException("Not supported yet.");
        //System.out.println("Visiting Storage Unit: " + su.getName());
        //getNSupply(su);
        return "";
    }
    
    @Override
    public String visitProductGroup(ProductGroup pg) {
//        throw new UnsupportedOperationException("Not supported yet.");
        //System.out.println("Visiting Product Group: " + pg.getName());
        getNSupply(pg);
        return "";
    }
    
    /**
     * This will get the supply based on the n - but only add to the 
     * list if the current supply < n-month supply
     */
    private void getProdNSupply(Product product) {
        assert(monthSupply > 0);
        assert(monthSupply <= 100);
        
        Size productSize = product.getSize();
        //if the the month supply is undefined aka the size is 0
        if(productSize.getSize() <= 0){
            return;
        }
        //number of items of a product currently in the system
        int numberOfItems = ProductFacade.getInstance().getItemCount(product);
        
        float productNSupply = product.getNSupply(monthSupply).getSize();
        
        Size productQuota = new Size(productNSupply, SizeUnit.Count);
        Size prodCount = new Size(numberOfItems, SizeUnit.Count);
        if(numberOfItems < (int)productNSupply) {
            this.productSupplies.add(new ProductSupplyData(product, productQuota, prodCount));
        }
                
    }
    
    private void getNSupply(ProductGroup container) {
        assert(monthSupply > 0);
        assert(monthSupply <= 100);
        assert container != null;
        
        float currentSupply = 0;
        
        SizeUnit productUnit = container.getThreeMonthSupply().getUnit();
        
        if(productUnit == SizeUnit.Count)
        {
            currentSupply = getCurrentCountSupply(container);
        }else if(SizeUnit.isVolume(productUnit))
        {
            currentSupply = getCurrentVolumeSupply(container);
        }else if(SizeUnit.isWeight(productUnit))
        {
            currentSupply = getCurrentWeightSupply(container);
        }else
        {
            //System.out.println("There is a glitch in the MATRIX");
            assert false;
        }
        
        StorageUnit storageUnit = (StorageUnit)storageUnitManager.getStorageUnit(container);
        Size contQuota = new Size(container.getNSupply(monthSupply).getSize(), 
				container.getThreeMonthSupply().getUnit());
        Size contCount = new Size(currentSupply, container.getThreeMonthSupply().getUnit());
        if(currentSupply < container.getNSupply(monthSupply).getSize()){
            this.prodContSupplies.add(new ProdContSupplyData(container, 
					storageUnit, contQuota, contCount));
        }
        
    }
    
    public Iterator<ProductSupplyData> getProductSupply(){
        return productSupplies.iterator();
    }

    public void visitProduct(Product product) {
        //throw new UnsupportedOperationException("Not yet implemented");
        
        getProdNSupply(product);
        
    }

    public Iterator<ProdContSupplyData> getProdContSupply() {
        //throw new UnsupportedOperationException("Not yet implemented");
        return prodContSupplies.iterator();
    }

    private int getCurrentCountSupply(ProductContainer container) {
        //throw new UnsupportedOperationException("Not yet implemented");
        int supplyCount = 0;
        
        Iterator<Product> localProducts = storageUnitManager.getProducts(container);
        while(localProducts.hasNext()){
            Product p = localProducts.next();
            if(p.getSize().getUnit() == SizeUnit.Count){
                supplyCount = supplyCount + 
						storageUnitManager.getItemCountInContainer(container, p);
            }
        }
        
        Iterator<ProductContainer> subGroups = storageUnitManager.getSubContainers(container);
        
        while(subGroups.hasNext()){
            ProductGroup pg = (ProductGroup)subGroups.next();
            if(pg.getThreeMonthSupply().getUnit() == SizeUnit.Count){
                supplyCount = supplyCount + getCurrentCountSupply(pg);
            }
        }
        //System.out.println(container.getName()+"'s Total Prducts: "+supplyCount);
        //iterate over sub containers/
        return supplyCount;
    }

    private float getCurrentVolumeSupply(ProductGroup container) {
        //throw new UnsupportedOperationException("Not yet implemented");
        float supplyCount = 0;
        
        Iterator<Product> localProducts = storageUnitManager.getProducts(container);
        while(localProducts.hasNext()){
            Product p = localProducts.next();
            if(SizeUnit.isVolume(p.getSize().getUnit())){
                assert p.getSize().getUnit().isCompatible
                        (container.getThreeMonthSupply().getUnit());
                int numberOfProduct = storageUnitManager.getItemCountInContainer(container, p);
                double amount = p.getSize().getSize() / 
						p.getSize().getUnit().convertTo(container.getThreeMonthSupply().getUnit());
                supplyCount = (float) (supplyCount + numberOfProduct * amount);
            }
        }
        
        Iterator<ProductContainer> subGroups = storageUnitManager.getSubContainers(container);
        
        while(subGroups.hasNext()){
            ProductGroup pg = (ProductGroup) subGroups.next();
            if(SizeUnit.isVolume(pg.getThreeMonthSupply().getUnit())){
            float convertFactor = (float)pg.getThreeMonthSupply().getUnit().convertTo(
                                            container.getThreeMonthSupply().getUnit());
            supplyCount = supplyCount + getCurrentVolumeSupply(pg) / convertFactor;
            }
            //System.out.println(pg.getName() + "'s Conversion Factor: " + convertFactor);
        }
        //System.out.println(container.getName()+"'s Total Prducts: "+supplyCount);
        //iterate over sub containers/
        return supplyCount;
    }

    private float getCurrentWeightSupply(ProductGroup container) {
        //throw new UnsupportedOperationException("Not yet implemented");
        float supplyCount = 0;
        
        Iterator<Product> localProducts = storageUnitManager.getProducts(container);
        while(localProducts.hasNext()){
            Product p = localProducts.next();
            if(SizeUnit.isWeight(p.getSize().getUnit())){
                //System.out.println(p.getProdDesc() + "'s Size Unit: " + 
				//p.getSize().getUnitString());
                //System.out.println(p.getProdDesc() + "'s Size: " + 
				//p.getSize().getSizeValueString());
                assert p.getSize().getUnit().isCompatible
                            (container.getThreeMonthSupply().getUnit());
                int numberOfProduct = storageUnitManager.getItemCountInContainer(container, p);
                double amount = p.getSize().getSize() / 
						p.getSize().getUnit().convertTo(container.getThreeMonthSupply().getUnit());
                //System.out.println("Amount: "+amount);
                supplyCount = (float) (supplyCount + numberOfProduct * amount);
            }
        }
        
        Iterator<ProductContainer> subGroups = storageUnitManager.getSubContainers(container);
        
        while(subGroups.hasNext()){
            ProductGroup pg = (ProductGroup)subGroups.next();
            if(SizeUnit.isWeight(pg.getThreeMonthSupply().getUnit())){
            float convertFactor = (float)pg.getThreeMonthSupply().getUnit().convertTo(
                                            container.getThreeMonthSupply().getUnit());
            supplyCount = supplyCount + getCurrentWeightSupply(pg) / convertFactor;
            }
        }
        //System.out.println(container.getName()+"'s Total Prducts: "+supplyCount);
        //iterate over sub containers/
        return supplyCount;
    }
}
