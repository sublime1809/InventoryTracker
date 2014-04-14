/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.product.Product;
import application.product.ProductsManager;
import application.reports.builders.Alignment;
import application.reports.builders.FontSize;
import application.reports.dataObjects.ProductSupplyData;
import application.reports.visitors.SupplyVisitor;
import application.storage.StorageUnitsManager;
import application.reports.builders.Row;
import application.reports.dataObjects.ProdContSupplyData;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import common.Size;
import common.SizeUnit;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author owner
 */
public class SupplyReportDirector extends ReportDirector {
    
    private int monthSupply = -1;
    private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();
    private ProductsManager products = ProductsManager.getInstance();
    private ProductGroup ProductGroup;
    private StorageUnit StorageUnit;
    /**
     * This will populate the report will all it's necessary information 
     * based on the data in the system
     */
    @Override
    public String printReport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * This will call buildReport if it wasn't already built and print the file
     * @return String location of the file
     */
    @Override
    public void buildReport() {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        SupplyVisitor visitor = null;
        try {
            visitor = new SupplyVisitor(monthSupply);
        } catch (Exception ex) {
            Logger.getLogger(SupplyReportDirector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assert visitor != null;
        storageUnits.visitContainersPreOrder(visitor);
        products.accept(visitor);
        
        Iterator<ProductSupplyData> productSupply = visitor.getProductSupply();
        List<Row> info = new ArrayList<Row>();
        while(productSupply.hasNext()){
            ProductSupplyData productSupplyData = productSupply.next();
            info.add(new Row(this.getProductInfo(productSupplyData)));
        }
        
        this.getBuilder().addTitle(FontSize.large, monthSupply + "-Month Supply");
        this.getBuilder().addTitle(FontSize.medium, "Products", Alignment.left);
        this.getBuilder().buildTable(getProductHeaders().size(), getProductHeaders(), info);
        
        Iterator<ProdContSupplyData> prodContSupply = visitor.getProdContSupply();
        List<Row> info2 = new ArrayList<Row>();
        while(prodContSupply.hasNext()){
            ProdContSupplyData prodContSupplyData = prodContSupply.next();
            info2.add(new Row(this.getProdContInfo(prodContSupplyData)));
        }
        
        this.getBuilder().addTitle(FontSize.medium, "Product Groups", Alignment.left);
        this.getBuilder().buildTable(getProdContHeaders().size(), getProdContHeaders(), info2);
        
        String location = this.getBuilder().printReport();
        
        try{
            Desktop.getDesktop().open(new File(location));
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public void setMonthSupply(int _monthSupply){
        monthSupply = _monthSupply;
    }
    
    private List<String> getProductHeaders(){
        List<String> prodHeaders = new ArrayList<String>();
        
        prodHeaders.add("Description");
        prodHeaders.add("Barcode");
        prodHeaders.add(monthSupply + "-Month Supply");
        prodHeaders.add("Current Supply");
        
        return prodHeaders;
    }
    
    private List<String> getProdContHeaders(){
        List<String> prodContHeaders = new ArrayList<String>();
        
        prodContHeaders.add("Product Group");
        prodContHeaders.add("Storage Unit");
        prodContHeaders.add(monthSupply + "-Month Supply");
        prodContHeaders.add("Current Supply");
        
        return prodContHeaders;
    }
    
    private List<String> getProductInfo(ProductSupplyData productSupplyData){
        List<String> info = new ArrayList<String>();
        
        Product product = productSupplyData.getProduct();
        Size quota = productSupplyData.getQuota();
        Size count = productSupplyData.getCount();
        
        double quotaSize = (double)quota.getSize();
        
        info.add(product.getProdDesc());
        info.add(product.getBarcode().getValue());
        if(quotaSize == (int)quotaSize){
            info.add((int)quotaSize + " " + quota.getUnitString());
        }else{
            Double quotaDSize = (double)quota.getSize();
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            quotaDSize = Double.valueOf(twoDForm.format(quotaDSize));
            info.add(quotaDSize + " " + quota.getUnitString());
        }
        info.add((int)count.getSize() + " " + count.getUnitString());
        
        return info;
    }

    private List<String> getProdContInfo(ProdContSupplyData prodContSupplyData) {
        //throw new UnsupportedOperationException("Not yet implemented");
        
        List<String> info = new ArrayList<String>();
        
        ProductGroup pg = prodContSupplyData.getProductGroup();
        StorageUnit su = prodContSupplyData.getStorageUnit();
        Size quota = prodContSupplyData.getQuota();
        Size count = prodContSupplyData.getCount();
        
        Double quotaSize = (double)quota.getSize();
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        quotaSize = Double.valueOf(twoDForm.format(quotaSize));
        
        info.add(pg.getName());
        info.add(su.getName());
        info.add(quotaSize + " " + quota.getUnitString());
        if(count.getUnit() == SizeUnit.Count){
            info.add((int)count.getSize() + " " + count.getUnitString());
        }
        else if(SizeUnit.isVolume(count.getUnit())){
            double countSize = count.getSize();
            countSize = Double.valueOf(twoDForm.format(countSize));
            if(countSize == (int)countSize){
                info.add((int)countSize + " " + count.getUnitString());
            }
            else{
                info.add(countSize + " " + count.getUnitString());
            }
        }
        else if(SizeUnit.isWeight(count.getUnit())){
            double countSize = count.getSize();
            countSize = Double.valueOf(twoDForm.format(countSize));
            if(countSize == (int)countSize){
                info.add((int)countSize + " " + count.getUnitString());
            }
            else{
                info.add(countSize + " " + count.getUnitString());
            }
        }
        else{
            info.add("SupplyReportDirector.getProdContInfo is broken... tell leckie");
        }
        return info;
    }
}

