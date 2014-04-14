/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.item.Item;
import application.reports.builders.FontSize;
import application.reports.builders.Row;
import application.reports.visitors.ExpiredItemsVisitor;
import application.storage.StorageUnitsManager;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class ExpiredReportDirector extends ReportDirector {
	
	private StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();

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
        ExpiredItemsVisitor visitor = new ExpiredItemsVisitor();
        storageUnits.visitContainersPreOrder(visitor);
        Iterator<Item> expiredItems = visitor.getExpiredItems();
		expiredItems = sortItems(expiredItems);
        int items = 0;
        List<Row> info = new ArrayList<Row>();
        while(expiredItems.hasNext()){
                Item item = expiredItems.next();
                info.add(new Row(this.getItemInfo(item)));
                items++;
        }
        
        // add to builder
        this.getBuilder().addTitle(FontSize.large, "Expired Items");
        this.getBuilder().buildTable(getHeaders().size(), getHeaders(), info);
        
        // populate file
        String location = this.getBuilder().printReport();
        // to print
        try {
            Desktop.getDesktop().open(new File(location));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private List<String> getHeaders() { 
        List<String> headers = new ArrayList<String>();
        
        headers.add("Description");
        headers.add("Storage Unit");
        headers.add("Product Group");
        headers.add("Entry Date");
        headers.add("Expire Date");
        headers.add("Item Barcode");
        
        return headers;
    }
    
    private List<String> getItemInfo(Item item) {
        List<String> info = new ArrayList<String>();
        
        info.add(item.getProduct().getProdDesc());
        if(item.getStorageUnit() != null) {
            info.add(item.getStorageUnit().getName());
        } else {
            info.add(StorageUnitsManager.getInstance().
					getStorageUnit(item.getProductGroup()).getName());
        }
        if(item.getProductGroup() != null) {
            info.add(item.getProductGroup().getName());
        } else {
            info.add("");
        }
        info.add(formatDate(item.getEntryDate()));
        info.add(formatDate(item.getExpirationDate()));
        info.add(item.getBarcode().getValue());
        
        return info;
    }
    
    private String formatDate(Date date) {
        return (date.getMonth() + 1) + "/" + date.getDate() + "/" + (date.getYear() + 1900);
    }

	private Iterator<Item> sortItems(Iterator<Item> expiredItems) {
		List<Item> items = new ArrayList();
		while(expiredItems.hasNext()){
			items.add(expiredItems.next());
		}
		Collections.sort(items, new customItemComparator());

		return items.iterator();
	}
	
	private class customItemComparator implements Comparator<Item>{

		@Override
		public int compare(Item item1, Item item2) {
			String description1 = item1.getProduct().getProdDesc();
			String description2 = item2.getProduct().getProdDesc();
			return description1.compareTo(description2);
		}
	
		
	}
}

