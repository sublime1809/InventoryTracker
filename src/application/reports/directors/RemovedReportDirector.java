/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.item.Item;
import application.product.Product;
import application.product.ProductsManager;
import application.reports.builders.Alignment;
import application.reports.builders.FontSize;
import application.reports.builders.Row;
import application.storage.RemovedItemsManager;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class RemovedReportDirector extends ReportDirector {
	
	private RemovedItemsManager removedItems = RemovedItemsManager.getInstance();
	private ProductsManager products = ProductsManager.getInstance();
	private Date dateToCheck;

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
		//System.out.println("I'm going to find all the removed items since: " + dateToCheck);

		//get all the products that have ever been removed and then sort them by description
		Iterator<Product> productsRemoved = removedItems.getProductIterator();
		List<Product> products = new ArrayList();
		while(productsRemoved.hasNext()){
			products.add(productsRemoved.next());
		}
		Collections.sort(products);
		productsRemoved = products.iterator();
		
		//get the info that is need for each product that has been removed
		List<Row> info = new ArrayList<Row>();
		while(productsRemoved.hasNext()){
				Product cur = productsRemoved.next();
				List<String> productInfo = getProductInfo(cur);
				if(productInfo.size() > 0){
					info.add(new Row(productInfo));
				}
		}
	
		getBuilder().addTitle(FontSize.large, "Items Removed since " 
				+ DateFormat.getInstance().format(dateToCheck)); 
		if(info.size() > 0){
			this.getBuilder().buildTable(getHeaders().size(), getHeaders(), info);
		}
		else{
			getBuilder().addTitle(FontSize.average, 
					"No items were removed during the reported period", Alignment.left);
		}
	           
        // populate file
        String location = this.getBuilder().printReport();
        // to print
        try {
            Desktop.getDesktop().open(new File(location));
        } catch(IOException e) {
            e.printStackTrace();
        }
		//tell the manager that the report was run
		removedItems.runReport();
    }
	
	public void setDate(Date date){
		dateToCheck = date;
	}
    
    private List<String> getHeaders() { 
        List<String> headers = new ArrayList<String>();
        
        headers.add("Description");
        headers.add("Size");
        headers.add("Product Barcode");
        headers.add("Removed");
        headers.add("Current Supply");
        
        return headers;
    }
    
    private List<String> getProductInfo(Product product) {
        List<String> info = new ArrayList<String>();
		
		Iterator<Item> items = removedItems.getItemIterator(product);
		int itemCount = 0;
		while(items.hasNext()){
			Item cur = items.next();
			Date exitDate = cur.getExitTime();
			if(exitDate != null && exitDate.after(dateToCheck)){
				itemCount++;
			}
		}
		
		if(itemCount > 0){
			info.add(product.getProdDesc());
			info.add(product.getSize().toString());
			info.add(product.getBarcode().toString());
			info.add(itemCount + "");
			info.add(products.getItemCount(product) + "");
		}
        
//        info.add(item.getProduct().getProdDesc());
//        info.add(this.getSize(item));
//        info.add(item.getProduct().getBarcode().getValue());
//        info.add("");
//        info.add("");
        
        return info;
    }
    
    private String getSize(Item item) {
        return item.getProduct().getSize().getSizeValueString() + " " 
				+ item.getProduct().getSize().getUnit().toString();
    }
}
