/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.ProductFacade;
import application.product.Product;
import application.reports.builders.FontSize;
import application.reports.builders.Row;
import application.reports.dataObjects.ProductStatisticsData;
import common.util.DateUtils;
import java.awt.Desktop;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author owner
 */
public class StatisticsReportDirector extends ReportDirector {

    private int months;
	private Date begin;
	private Date end;
    
    public StatisticsReportDirector(int _months) {
        this.months = _months;
		begin = DateUtils.removeTimeFromDate(DateUtils.addMonths(new Date(), _months * -1));	
		end = new Date();
    }
    
    public StatisticsReportDirector(Date begin, Date end) {
		this.begin = begin;
		this.end = end;
    }
    
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
        Date begin = getBegin();
        
        populate(begin, end);
        String location = this.getBuilder().printReport();
        try {
            Desktop.getDesktop().open(new File(location));
        } catch(Exception e) {
//            e.printStackTrace();
        }
    }
    
    private Date getBegin() {
       return begin;
    }
    
    private void populate(Date begin, Date end) {
//        Date begin = new Date(112, 8, 1);
        
        this.getBuilder().addTitle(FontSize.large, "Product Report (" + this.months + " " + 
				((this.months == 1)? "Month" : "Months") + ")");
        
        // add data
        List<Row> dataA = new ArrayList<Row>();
        ProductFacade instance = ProductFacade.getInstance();
        Iterator<Product> iter = instance.getProducts();
        List<ProductStatisticsData> toSort = new ArrayList<ProductStatisticsData>();
        while(iter.hasNext()) {
			Product next = iter.next();
			Date prodBeginDate = (next.getDate().after(begin) ? next.getDate() : begin);
            ProductStatisticsData data = new ProductStatisticsData(next, prodBeginDate, end);
            toSort.add(data);
        }
        Collections.sort(toSort);
        
        for(ProductStatisticsData data : toSort) {
            Row row = new Row();
            row.addColumn(data.getDescription());
            row.addColumn(data.getBarcode());
            row.addColumn(data.getSize());
            row.addColumn(data.getThreeMonthSupply());
			DecimalFormat dm = new DecimalFormat("#.#");
            row.addColumn(data.getCurrentSupply() + "/ " + dm.format(data.getAverageSupply()));
            row.addColumn(data.getMinSupply() + "/" + data.getMaxSupply());
            row.addColumn(data.getUsedSupply() + "/" + data.getAddedSupply());
            row.addColumn(data.getShelfLife());
            row.addColumn(dm.format(data.getAvgUsedAge()) 
					+ " days/ " + data.getMaxUsedAge() + " days");
            row.addColumn(dm.format(data.getAvgCurAge())
					+ " days/ " + data.getMaxCurAge() + " days" );
            
            dataA.add(row);
        }
        
        
        this.getBuilder().buildTable(10, this.getHeaders(), dataA);
    }
    
    private List<String> getHeaders() {
        List<String> headers = new ArrayList<String>();
        headers.add("Description");
        headers.add("Barcode");
        headers.add("Size");
        headers.add("3-Month Supply");
        headers.add("Supply: Cur/Avg");
        headers.add("Supply: Min/Max");
        headers.add("Supply: Used/Added");
        headers.add("Shelf Life");
        headers.add("Used Age: Avg/Max");
        headers.add("Cur Age: Avg/Max");
        
        return headers;
    }
    
}

