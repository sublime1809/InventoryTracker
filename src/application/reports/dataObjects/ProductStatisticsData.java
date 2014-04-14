 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.dataObjects;

import application.ProductFacade;
import application.item.Item;
import application.product.Product;
import application.storage.RemovedItemsManager;
import common.util.DateUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author owner
 */
public class ProductStatisticsData implements Comparable {
    private Map<Date, List<Item>> dateInfo;
    private Collection<Item> usedItems;
    private Collection<Item> addedItems;
    private Collection<Item> allItems;
    private Date today;
    private Date todayTime;
    private Date begin;
    
    private int currentSupply;
    private double avgSupply;
    private int minSupply;
    private int maxSupply;
    private double avgUsedAge;
    private int maxUsedAge;
    private double avgCurAge;
    private int maxCurAge;
    private String description;
    private String barcode;
    private String size;
    private String shelfLife;
    private String threeMonthSupply;
    
    public ProductStatisticsData(Product product, Date begin, Date end) {
        dateInfo = new TreeMap<Date, List<Item>>();
        usedItems = new HashSet<Item>();
        addedItems = new HashSet<Item>();
        allItems = new HashSet<Item>();
        description = product.getProdDesc();
        barcode = product.getBarcode().getValue();
        size = product.getSize().getSizeValueString() + " " 
				+ product.getSize().getUnit().toString();
        shelfLife = product.getShelfLifeString();
        threeMonthSupply = ((int)product.getMonthSupply().getSize()) + " " 
                                + product.getMonthSupply().getUnitString();
        
        this.today = new Date(end.getYear(), end.getMonth(), end.getDate(), 0, 0, 0);
        this.todayTime = end;
        this.begin = begin;
        this.minSupply = -1;
        this.maxSupply = 0;
        
        populateMap(product);
        this.doCalculations();
        this.calculateAges();
    }
    
    private void populateMap(Product product) {
        generateKeys(product);
        Iterator<Item> current = ProductFacade.getInstance().getItems(product);
        Iterator<Item> removed = RemovedItemsManager.getInstance().getItemIterator(product);
        
        int currentItemCount = 0;
        while(current.hasNext()) {
            Item item = current.next();
            findAddedAndUsed(item);
            insertItem(item);
            currentItemCount++;
        }
        this.currentSupply = currentItemCount;
        if(removed != null) {
            while(removed.hasNext()) {
                Item item = removed.next();
                findAddedAndUsed(item);
                insertItem(item);
            }
        }
    }
    
    private void findAddedAndUsed(Item item) {
        if(item.getEntryDate().after(begin) || item.getEntryDate().equals(begin)) {
            this.addedItems.add(item);
        }
        if(item.getExitTime() != null && 
				(DateUtils.removeTimeFromDate(
            item.getExitTime()).before(todayTime) || item.getExitTime().equals(todayTime))) {
            this.usedItems.add(item);
        }
    }
    
    private void insertItem(Item item) {
        this.allItems.add(item);
		int totalDays = 0;
        Date date;
        if(item.getEntryDate().after(begin)) {
            date = new Date(item.getEntryDate().getTime());
        } else {
            date = new Date(begin.getTime());
            
        }		
		
        while((compareDays(date,today) <= 0 && item.getExitTime() == null) || 
				(item.getExitTime() != null && compareDays(date, item.getExitTime()) < 0)) {
			totalDays++;
            dateInfo.get(date).add(item); 
            date = incrementDate(date);
        }
		totalDays=totalDays;
    }
    
    private void generateKeys(Product product) {
        Date date = new Date(begin.getTime());
//        if(product.getDate().before(begin)){
//            date = new Date(begin.getTime());
//        }
//        else{
//            date = new Date(product.getDate().getYear(), 
//	    		product.getDate().getMonth(), product.getDate().getDate(), 0,0,0);
//        }
        while(compareDays(date,today) <= 0) {
            dateInfo.put(new Date(date.getTime()), new ArrayList<Item>());
            date = incrementDate(date);
        }
    }
    
    /**
     * 
     * @param one
     * @param two
     * @return -1 if one is less than two, 0 if they are equal and 1 if one is greater than 2
     */
    private int compareDays(Date one, Date two) {
//        if(one.getYear() < two.getYear() || one.getMonth() < two.getMonth() || 
//				one.getDate() < two.getDate()) {
//            return -1;
//        } else if(one.getYear() == two.getYear() && 
//				one.getMonth() == two.getMonth() && one.getDate() == two.getDate()) {
//            return 0;
//        } else {
//            return 1;
//        }
		one = DateUtils.removeTimeFromDate(one);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(two);
		GregorianCalendar newCal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DATE));
		int result = one.compareTo(newCal.getTime());
		return result;
    }
    
    private Date incrementDate(Date date) {
        date.setDate(date.getDate()+1);
        return new Date(date.getTime());
    }
    
    private void doCalculations() {
        
        int sumOfSupply = 0;
        Date oldestDate = begin;
        
        for(Date date : this.dateInfo.keySet()) {
            List<Item> items = this.dateInfo.get(date);
            
            sumOfSupply += items.size();
            
            if((items.size() < minSupply || minSupply == -1) && items.size() > 0) {
//                if(oldestDate == null || date.before(oldestDate)) {
////                    oldestDate = new Date(date.getTime());
//                }
                minSupply = items.size();
            } 
            if(items.size() > maxSupply) {
                maxSupply = items.size();
            }
        }
		
		//This is in case the product didn't have any items 
		//	in the system during the entire report period
		if (oldestDate == null){
			oldestDate = new Date(today.getTime());
			minSupply = 0;
			maxSupply = usedItems.size();
		}
        
        if(this.compareDays(oldestDate, today) == 0) {
            this.avgSupply = sumOfSupply;
        } else {
            this.avgSupply = 
                ((((double)sumOfSupply/((double)dateDiff(oldestDate, today) + 1)) * 10)/10);
        }
        //this.currentSupply = this.dateInfo.get(today).size();
    }
    
    private double dateDiff(Date one, Date two) {
        return (two.getTime() - one.getTime()) / (1000 * 60 * 60 * 24);
    }
    
    private void calculateAges() {
        // used ages
        int ages = 0;
        for(Item item : this.usedItems) {
            int age = item.getAge(this.todayTime);
            ages += age;
            if(age > this.maxUsedAge) {
                this.maxUsedAge = age;
            }
        }
        if(usedItems.isEmpty()) {
            this.avgUsedAge = 0;
        } else {
            this.avgUsedAge = ((double)ages) / ((double)this.usedItems.size());
        }
        
        // current ages
        List<Item> current = this.dateInfo.get(today);
        int curAges = 0;
        for(Item item : current) {
            int age = item.getAge(this.todayTime);
            curAges += age;
            if(age > this.maxCurAge) {
                this.maxCurAge = age;
            }
        }
        
        if(current.isEmpty()) {
            this.avgCurAge = 0;
        } else {
            this.avgCurAge = ((double)curAges) / ((double)current.size());
        }
    }
    
    public double getAverageSupply() {
        return this.avgSupply;
    }
    
    public int getCurrentSupply() {
        return this.currentSupply;
    }
    
    public int getMaxSupply() {
        return this.maxSupply;
    }
    
    public int getMinSupply() {
        return this.minSupply;
    }
    
    public int getUsedSupply() {
        return this.usedItems.size();
    }
    
    public int getAddedSupply() {
        return this.addedItems.size();
    }
    
    public double getAvgUsedAge() {
        return this.avgUsedAge;
    }
    
    public int getMaxUsedAge() {
        return this.maxUsedAge;
    }
    
    public double getAvgCurAge() {
        return this.avgCurAge;
    }
    
    public int getMaxCurAge() {
        return this.maxCurAge;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @return the shelfLife
     */
    public String getShelfLife() {
        return shelfLife;
    }

    @Override
    public int compareTo(Object o) {
        ProductStatisticsData other = (ProductStatisticsData) o;
        if(this.description.compareTo(other.description) != 0) {
            return this.description.compareTo(other.description);
        } else {
            return this.barcode.compareTo(other.barcode);
        }
    }
    
    public String getThreeMonthSupply() {
        return this.threeMonthSupply;
    }
}
