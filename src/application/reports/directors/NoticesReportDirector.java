/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.product.Product;
import application.reports.builders.Alignment;
import application.reports.builders.FontSize;
import application.reports.visitors.NoticesVisitor;
import application.storage.ProductGroup;
import application.storage.StorageUnitsManager;
import common.Notice;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class NoticesReportDirector extends ReportDirector {
	
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
        NoticesVisitor visitor = new NoticesVisitor();
		storageUnits.visitContainersPostOrder(visitor);
		List<Notice> notices = visitor.getNotices();
		//System.out.println("There are " + notices.size() + " notices.");
		
		this.getBuilder().addTitle(FontSize.large, "Notices");
		if(notices.size()> 0){
			this.getBuilder().addTitle(FontSize.medium, "\n3-Month Supply Notices", Alignment.left);
		}
		else{
			this.getBuilder().addTitle
					(FontSize.medium, "There are no notices at this time", Alignment.left);
		}
		

		for(int i = notices.size() - 1; i >= 0; i--){		
			List<String> toPrint = new ArrayList<String>();
			Notice cur = notices.get(i);
			ProductGroup pg = cur.getProductGroup();
			getBuilder().addTitle(FontSize.small, "\nProduct Group " 
					+ storageUnits.getStorageUnit(pg).getName() + "::" + pg.getName() 
					+ " has a 3-month supply (" + pg.getThreeMonthSupply().toString()
					+ ") that is inconsistent with the following products:", Alignment.left);
			Iterator<Product> badProducts = cur.getProducts().iterator();
			while(badProducts.hasNext()){
				Product curProduct = badProducts.next();
				toPrint.add(storageUnits.getContainerThatHasProduct
						(pg, curProduct).getName() + "::" + curProduct.getProdDesc() 
						+ "(size: " + curProduct.getSize() + ")");
			}
		
			this.getBuilder().buildList(toPrint);
		}
        
        // populate file
        String location = this.getBuilder().printReport();
        // to print
        try {
            Desktop.getDesktop().open(new File(location));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}

