/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.directors;

import application.AssistantToTheRegionalManager;
import application.ContainersFacade;
import application.ItemsFacade;
import application.ProductFacade;
import application.item.Item;
import application.product.Product;
import application.reports.builders.Row;
import application.reports.builders.StubBuilder;
import application.storage.ProductGroup;
import application.storage.StorageUnit;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidItemException;
import common.util.DateUtils;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author owner
 */
public class StatisticsReportDirectorTest {

    private final int YEAR = 365;
    
	private StatisticsReportDirector director;
	private AssistantToTheRegionalManager attrm;
	private ContainersFacade containers;
	private ItemsFacade items;
	private ProductFacade products;
	
	private StorageUnit unitA;
	private StorageUnit unitB;
	private ProductGroup groupA;
	private ProductGroup groupB1;
	private ProductGroup groupB2;
	
	private Product productA;
	private Product productB;
	private Product productC;
	private Product productD;
	private Product productE;
	
	private Item itemA1;
	private Item itemA2;
	private Item itemA3;
	private Item itemB1;
	private Item itemC1;
	private Item itemC2;
	private Item itemC3;
	private Item itemC4;
	private Item itemC5;
	private Item itemC6;
	private Item itemD1;
	private Item itemD2;
	private Item itemD3;
	private Item itemD4;
	private Item itemD5;
	private Item itemD6;
	private Item itemD7;
	private Item itemD8;
	private Item itemD9;
	private Item itemD10;
	private Item itemD11;
	private Item itemD12;
	private Item itemE1;
	private Item itemE2;
	private Item itemE3;
	private Item itemE4;
	
	private List<Item> allItems = new ArrayList<>();
	
	private final int DESCRIPTION = 0;
	private final int BARCODE = 1;
	private final int SIZE = 2;
	private final int SUPPLY = 3;
	private final int SUPPLY_CUR_AVG = 4;
	private final int SUPPLY_MIN_MAX = 5;
	private final int SUPPLY_USED_ADDED = 6;
	private final int SHELFLIFE = 7;
	private final int USEDAGE = 8;
	private final int CURAGE = 9;

	private final int PRODUCTAROW = 0;
	private final int PRODUCTBROW = 1;
	private final int PRODUCTCROW = 2;
	private final int PRODUCTDROW = 3;
	private final int PRODUCTEROW = 4;
	
    public StatisticsReportDirectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        
        attrm = AssistantToTheRegionalManager.getInstance();
        
        containers = ContainersFacade.getInstance();
        items = ItemsFacade.getInstance();
        products = ProductFacade.getInstance();
        
        unitA = new StorageUnit("UnitA", null);
		unitB = new StorageUnit("UnitB", null);
		
		containers.addProductContainer(unitA, null);
		containers.addProductContainer(unitB, null);
		
		Size size = new Size(0f, SizeUnit.Count);
		
		groupA = new ProductGroup("GroupA", unitA, size);
		groupB1 = new ProductGroup("GroupB1", unitB, size);
		groupB2 = new ProductGroup("GroupB2", unitB, size);
		
		containers.addProductContainer(groupA, unitA);
		containers.addProductContainer(groupB1, unitB);
		containers.addProductContainer(groupB2, unitB);
		
		productA = new Product(null, "1234567890", "Product A", 0, 0, size);
		productB = new Product(null, "1", "Product B", 0, 0, size);
		productC = new Product(null, "2", "Product C", 0, 0, size);
		productD = new Product(null, "3", "Product D", 0, 0, size);
		productE = new Product(null, "4", "Product E", 0, 0, size);
		
		products.addProduct(productA, unitA);
		products.addProduct(productB, groupA);
		products.addProduct(productC, groupB1);
		products.addProduct(productD, unitA);
		products.addProductToContainer(productD, unitB);
		products.addProduct(productE, groupB2);
		
		GregorianCalendar t = new GregorianCalendar();
		GregorianCalendar t2 = new GregorianCalendar(t.get(GregorianCalendar.YEAR), 
				t.get(GregorianCalendar.MONTH), t.get(GregorianCalendar.DATE));
		Date today = t2.getTime();
		
		itemA1 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productA);
		itemA2 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productA);
		itemA3 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productA);
		itemB1 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupA, productB);
		itemC1 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemC2 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemC3 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemC4 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemC5 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemC6 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB1, productC);
		itemD1 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productD);
		itemD2 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productD);
		itemD3 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitA, productD);
		itemD4 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemD5 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemD6 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemD7 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemD8 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemD9 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
	   itemD10 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
	   itemD11 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
	   itemD12 = new Item(Barcode.generate(), new Date(today.getTime()), null, unitB, productD);
		itemE1 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB2, productE);
		itemE2 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB2, productE);
		itemE3 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB2, productE);
		itemE4 = new Item(Barcode.generate(), new Date(today.getTime()), null, groupB2, productE);
		
		items.addItem(itemA1);
		items.addItem(itemA2);
		items.addItem(itemA3);
		items.addItem(itemB1);
		items.addItem(itemC1);
		items.addItem(itemC2);
		items.addItem(itemC3);
		items.addItem(itemC4);
		items.addItem(itemC5);
		items.addItem(itemC6);
		items.addItem(itemD1);
		items.addItem(itemD2);
		items.addItem(itemD3);
		items.addItem(itemD4);
		items.addItem(itemD5);
		items.addItem(itemD6);
		items.addItem(itemD7);
		items.addItem(itemD8);
		items.addItem(itemD9);
		items.addItem(itemD10);
		items.addItem(itemD11);
		items.addItem(itemD12);
		items.addItem(itemE1);
		items.addItem(itemE2);
		items.addItem(itemE3);
		items.addItem(itemE4);
		
		allItems.add(itemA1);
		allItems.add(itemA2);
		allItems.add(itemA3);
		allItems.add(itemB1);
		allItems.add(itemC1);
		allItems.add(itemC2);
		allItems.add(itemC3);
		allItems.add(itemC4);
		allItems.add(itemC5);
		allItems.add(itemC6);
		allItems.add(itemD1);
		allItems.add(itemD2);
		allItems.add(itemD3);
		allItems.add(itemD4);
		allItems.add(itemD5);
		allItems.add(itemD6);
		allItems.add(itemD7);
		allItems.add(itemD8);
		allItems.add(itemD9);
		allItems.add(itemD10);
		allItems.add(itemD11);
		allItems.add(itemD12);
		allItems.add(itemE1);
		allItems.add(itemE2);
		allItems.add(itemE3);
		allItems.add(itemE4);
	}

	private void changeEntryDates(Date date)
	{
		for(Item item : allItems)
		{
			items.editItem(item, clearTime(date));
		}
	}
	
	private void changeToConsecutiveEntryDates(Date startDate, int incrementAmount)
	{
		for(int i = 0; i < allItems.size(); i++)
		{
			items.editItem(allItems.get(i), clearTime(addDays(startDate, incrementAmount * i)));
		}
	}

    @After
    public void tearDown() {
        attrm.reset();
        containers.reset();
        items.reset();
        products.reset();
    }
	
	private Date clearTime(Date date)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		GregorianCalendar newCal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DATE));
		return newCal.getTime();
	}
	
	private Date addDays(Date date, int amount)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DATE, amount);
		return cal.getTime();
	}
	
	/**
	 * Gets the number of days between two dates. The time part of the dates is disregarded. 
	 * Calling this method on two equal dates will return 1. If d1 is one day and d2 is the very next day
	 * the result would be 2.
	 * 
	 * @param d1
	 * @param d2
	 * @return 
	 */
	private int getDifferenceInDays(Date first, Date second)
	{
		long lengthOfDay = 1000 * 60 * 60 * 24;
		
		Date date1 = clearTime(first);
		Date date2 = clearTime(second);
		
		return (int)((date2.getTime() - date1.getTime()) / lengthOfDay) + 1;
	}
	
	private List<Date> getDaysBetween(Date start, Date end)
	{
		ArrayList<Date> list = new ArrayList<>();
		
		int daysInPeriod = getDifferenceInDays(start, end);
		
		Date curDate = new Date(start.getTime());
		
		while(curDate.compareTo(end) <= 0)
		{
			list.add(curDate);
			curDate = addDays(curDate, 1);
		}
		
		return list;
	}
	
	private void printItems()
	{
		for(Item item : allItems)
		{
			System.out.println(item.getBarcode() + ": " + item.getEntryDate() + " - " + item.getExitTime());
		}
	}
	
	private void printProducts()
	{
		System.out.println("Product A: " + productA.getDate());
		System.out.println("Product B: " + productB.getDate());
		System.out.println("Product C: " + productC.getDate());
		System.out.println("Product D: " + productD.getDate());
		System.out.println("Product E: " + productE.getDate());
	}
	
	private List<Row> generateResults(Date startDate, Date endDate)
	{
		//System.out.println("Report period: " + startDate + " - " + endDate);
		//printItems();
		//printProducts();
		
		int daysInPeriod = getDifferenceInDays(startDate, endDate);
				
		TreeMap<Date, TreeMap<Product, HashSet<Item>>> map = 
				new TreeMap<Date, TreeMap<Product, HashSet<Item>>>();
		
		for(int i = 0; i <= daysInPeriod; i++)
		{
			map.put(addDays(startDate, i), new TreeMap<Product, HashSet<Item>>());
		}
		
		for(Item item : allItems)
		{
			Date lastDayOfItem = 
					(item.getExitTime() == null ? endDate : clearTime(item.getExitTime()));
			
			Date itemStart = item.getEntryDate();
			if(itemStart.before(startDate)){
				itemStart = startDate;
			}
			List<Date> itemDays = getDaysBetween(itemStart, lastDayOfItem);
			
			for(Date date : itemDays)
			{
				TreeMap<Product, HashSet<Item>> dateEntry = map.get(date);
				if(!dateEntry.containsKey(item.getProduct()))
				{
					dateEntry.put(item.getProduct(), new HashSet<Item>());
				}
//				if(item.getExitTime() == null){
					dateEntry.get(item.getProduct()).add(item);
//				}
//				else{
//					Date exitCleared = this.clearTime(item.getExitTime());
//					if(date.before(exitCleared)){
//						dateEntry.get(item.getProduct()).add(item);
//					}
//				}
			}
		}
		
		TreeMap<Product, ProductStats> statsMap = new TreeMap<Product, ProductStats>();
		for(Date date : map.keySet())
		{
			for(Product prod : map.get(date).keySet())
			{
				if(!statsMap.containsKey(prod))
				{
					Date productStartDate = (prod.getDate().after(startDate) ? prod.getDate() : startDate);
					statsMap.put(prod, new ProductStats(productStartDate, endDate));
				}
				
				ProductStats stats = statsMap.get(prod);
				stats.calculateStuff(map.get(date).get(prod), date);
			}
		}
		
		ArrayList<Row> rows = new ArrayList<Row>();
		
		for(Map.Entry<Product, ProductStats> entry : statsMap.entrySet())
		{
			Product product = entry.getKey();
			ProductStats productStats = entry.getValue();
			Row row = new Row();
			row.addColumn(product.getProdDesc());
			row.addColumn(product.getBarcode().getValue());
			row.addColumn(product.getSize().toString());
			row.addColumn(product.getMonthSupply().toString());
			row.addColumn(productStats.getCurSupply() + "/ " + doubleToString(productStats.getAvgSupply()));
			row.addColumn(productStats.getMinSupply() + "/" + productStats.getMaxSupply());
			row.addColumn(productStats.getUsedSupply() + "/" + productStats.getAddedSupply());
			row.addColumn(product.getShelfLifeString());
			String hi = doubleToString(productStats.getAvgUsedAge()) + " days/ " + productStats.getMaxUsedAge() + " days";
			row.addColumn(hi);
			row.addColumn(doubleToString(productStats.getAvgCurAge()) + " days/ " + productStats.getMaxCurAge() + " days");
			
			rows.add(row);
		}
		
		return rows;
	}
	
	private String doubleToString(double d)
	{
		DecimalFormat format = new DecimalFormat("#.#");
		
		return format.format(d);
	}

	private class ProductStats {

		private int totalItemDays;
		private Date start;
		private Date end;
		private int periodLength;
		HashSet<Item> addedItems = new HashSet<Item>();
		HashSet<Item> usedItems = new HashSet<Item>();
		HashSet<Item> curItems = new HashSet<Item>();
		private int minSupply = Integer.MAX_VALUE;
		private int maxSupply = Integer.MIN_VALUE;

		public ProductStats(Date startDate, Date endDate) {
			this.start = startDate;
			this.end = endDate;

			this.periodLength = getDifferenceInDays(start, end);
		}

		public void calculateStuff(Collection<Item> dailyItems, Date date) {
			
			int itemSupplyCountForDay = 0;
			curItems = new HashSet<Item>();

			for (Item item : dailyItems) {
				if (item.getEntryDate().equals(date)) {
					addedItems.add(item);
				}
				if (item.getExitTime() != null && date.equals(clearTime(item.getExitTime()))) {
					usedItems.add(item);
				} else {
					curItems.add(item);
					itemSupplyCountForDay++;
				}
			}
			
			this.totalItemDays += itemSupplyCountForDay;
			this.setMaxSupply(itemSupplyCountForDay);
			this.setMinSupply(itemSupplyCountForDay);

		}

		public int getCurSupply() {
			return curItems.size();
		}

		public double getAvgSupply() {
			return ((double)totalItemDays) / ((double)periodLength);
		}

		public int getMinSupply() {
			return minSupply;
		}

		private void setMinSupply(int minSupply) {
			if (this.minSupply > minSupply) {
				this.minSupply = minSupply;
			}
		}

		public int getMaxSupply() {
			return maxSupply;
		}

		private void setMaxSupply(int maxSupply) {
			if (this.maxSupply < maxSupply) {
				this.maxSupply = maxSupply;
			}
		}

		public int getUsedSupply() {
			return usedItems.size();
		}

		public int getAddedSupply() {
			return addedItems.size();
		}

		public double getAvgUsedAge() {
			int totalUsedItemDays = 0;
			for (Item item : usedItems) {
				totalUsedItemDays += getDifferenceInDays(item.getEntryDate(), item.getExitTime()) - 1;
			}

			if (usedItems.size() == 0) {
				return 0;
			}
			double result = ((double)totalUsedItemDays) / ((double)usedItems.size());
			return result;
		}

		public int getMaxUsedAge() {
			int maxUsedAge = 0;
			for (Item item : usedItems) {
				int itemAge = getDifferenceInDays(item.getEntryDate(), item.getExitTime()) - 1;
				if (itemAge > maxUsedAge) {
					maxUsedAge = itemAge;
				}
			}

			return maxUsedAge;
		}

		public double getAvgCurAge() {
			int totalCurItemDays = 0;
			for (Item item : curItems) {
				totalCurItemDays += getDifferenceInDays(item.getEntryDate(), end) - 1;
			}

			if (curItems.size() == 0) {
				return 0;
			}

			return ((double)totalCurItemDays) / ((double)curItems.size());
		}

		public int getMaxCurAge() {
			int maxCurAge = 0;
			for (Item item : curItems) {
				int itemAge = getDifferenceInDays(item.getEntryDate(), end) - 1;
				if (itemAge > maxCurAge) {
					maxCurAge = itemAge;
				}
			}

			return maxCurAge;
		}
	}

	/**
	 * Compares all of the rows with each other
	 * @param rows
	 * @param expRows
	 * @return 
	 */
	private boolean compareRows(List<Row> rows, List<Row> expRows)
	{
		ArrayList<Integer> columns = new ArrayList<Integer>();
		columns.add(BARCODE);
		columns.add(SIZE);
		columns.add(SUPPLY);
		columns.add(SUPPLY_CUR_AVG);
		columns.add(SUPPLY_MIN_MAX);
		columns.add(SUPPLY_USED_ADDED);
		columns.add(SHELFLIFE);
		columns.add(USEDAGE);
		columns.add(CURAGE);
		
		return compareRows(rows, expRows, columns);
	}
	
	/**
	 * Compares the specified rows with each other
	 * @param rows
	 * @param expRows
	 * @param columnsToCheck
	 * @return 
	 */
	private boolean compareRows(List<Row> rows, List<Row> expRows, List<Integer> columnsToCheck)
	{
		boolean result = true;
		
		if(rows.size() != expRows.size())
		{
			result = false;
			System.out.println("The expected number of rows does not match the number of rows returned!");
			System.out.println("\t# of given rows: " + rows.size());
			System.out.println("\t# of expected rows: " + expRows.size());
		}
		
		for(Row row : rows)
		{
			for(Row expRow : expRows)
			{
				if(expRow.getColumn(DESCRIPTION).equals(row.getColumn(DESCRIPTION)))
				{
					for(int col : columnsToCheck)
					{
						boolean curResult = expRow.getColumn(col).equals(row.getColumn(col));
						
						if(curResult == false)
						{
							System.out.println("Mismatch in column " + col + " of " + expRow.getColumn(DESCRIPTION) + ":");
							System.out.println("\tTrevor's result: " + expRow.getColumn(col));
							System.out.println("\tChrys's result: " + row.getColumn(col));
							result = false;
						}
						
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Takes the date and subtracts the proper number of months
	 * @param date		the start date
	 * @param months	the number of months the date should be rolled back
	 * @return 
	 */
	private Date goBackMonths(Date date, int months){
		Date result = new Date(date.getYear(), date.getMonth(), date.getDate());
		
		while(result.getMonth() <= months){
			int subtracted = result.getMonth();
			months -= (subtracted + 1);
			result.setMonth(11);
			result.setYear(result.getYear() - 1);
		}
		
		result.setMonth(result.getMonth() - months);
		
		return result;
	}
        
        private Date goBackDays(Date date, int days){
		Date result = new Date(date.getYear(), date.getMonth(), date.getDate());
		
		while(result.getDay() <= days){
			int subtracted = result.getDay();
			days -= (subtracted + 1);
			result.setMonth(11);
		}
		
		result.setDate(result.getDay() - days);
		
		return result;
	}
	
    /**
     * Test of buildReport method, of class StatisticsReportDirector.
     */
    @Test
    public void testBuildReport() {
        System.out.println("buildReport");
		
        StatisticsReportDirector instance = new StatisticsReportDirector(3);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
        instance.buildReport();
		
		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(goBackMonths(new Date(), 3), new Date());
		
		assertTrue(compareRows(rows, expRows));
		
    }
	
	//	Test a report period that includes a transition from Standard Time to Daylight Savings Time
	//The transition occurs in the middle of the report period
	@Test
	public void test1A() throws Exception
        {
            System.out.println("Test 1A");
            Date start = DateUtils.parseDate("08/04/2012");
            Date end = DateUtils.parseDate("02/04/2013");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
	}
	
	//The transition occurs on the first day of the report period
	@Test
	public void test1B() throws Exception
	{
            System.out.println("Test 1B");
            Date start = DateUtils.parseDate("11/04/2012");
            Date end = DateUtils.parseDate("02/04/2013");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
            
	}
	
	//The transition occurs on the last day of the report period
	@Test
	public void test1C() throws Exception
	{
            System.out.println("Test 1C");
            Date start = DateUtils.parseDate("08/04/2012");
            Date end = DateUtils.parseDate("11/04/2012");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
	}
	
	//Test a report period that includes a transition from Daylight Savings Time to Standard Time
	//The transition occurs in the middle of the report period
	@Test
	public void test2A() throws Exception
	{
            System.out.println("Test 2A");
            Date start = DateUtils.parseDate("01/01/2011");
            Date end = DateUtils.parseDate("06/05/2012");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
	}
	
	//The transition occurs on the first day of the report period
	@Test
	public void test2B() throws Exception
	{
            System.out.println("Test 2B");
            Date start = DateUtils.parseDate("03/11/2012");
            Date end = DateUtils.parseDate("06/11/2012");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
	}
	
	//The transition occurs on the last day of the report period
	@Test
	public void test2C() throws Exception
	{
            System.out.println("Test 2C");
            Date start = DateUtils.parseDate("01/11/2012");
            Date end = DateUtils.parseDate("03/11/2012");
            changeEntryDates(addDays(start, 4));

            StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
            StubBuilder stub = new StubBuilder();
            instance.setBuilder(stub);
            instance.buildReport();

            //Here are the results.
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(start, end);
            assertTrue(compareRows(rows, expRows));
	}
	
	//Test a product that was initially added to the system during the reporting period (to make sure that the days before the product was created are not counted in the product’s statistics)
	//The product was added in the middle of the reporting period\
	@Test
	public void test3A()
	{
            System.out.println("test3A");

            Date today = new Date();
            this.clearTime(today);
            Date beginDate = goBackMonths(today, 3);
            Date itemDate = goBackMonths(today, 2);

            this.changeEntryDates(itemDate);

            director = new StatisticsReportDirector(beginDate, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            List<Row> rows = stub.getRows();
            Row productERow = rows.get(PRODUCTEROW);
            assertEquals("61 days/ 61 days", productERow.getColumn(CURAGE));

            director = new StatisticsReportDirector(beginDate, today);
            stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            rows = stub.getRows();
            Row productBRow = rows.get(PRODUCTBROW);
            assertEquals("61 days/ 61 days", productBRow.getColumn(CURAGE));
            
	}
	
	//The product was added on the first day of the reporting period
	@Test
	public void test3B()
	{
            System.out.println("test3B");

            Date today = new Date();
            this.clearTime(today);
            Date beginDate = goBackMonths(today, 3);

            this.changeEntryDates(beginDate);

            director = new StatisticsReportDirector(beginDate, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            List<Row> rows = stub.getRows();
            Row productERow = rows.get(PRODUCTEROW);
            assertEquals("91 days/ 91 days", productERow.getColumn(CURAGE));

            director = new StatisticsReportDirector(beginDate, today);
            stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            rows = stub.getRows();
            Row productBRow = rows.get(PRODUCTBROW);
            assertEquals("91 days/ 91 days", productBRow.getColumn(CURAGE));
	}
	
	//The product was added on the last day of the reporting period
	@Test
	public void test3C()
	{
            System.out.println("test3C");

            Date today = new Date();
            this.clearTime(today);
            
            Date beginDate = goBackMonths(today, 3);
            Date itemDate = goBackDays(today, 1);
            this.changeEntryDates(itemDate);

            director = new StatisticsReportDirector(beginDate, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            List<Row> rows = stub.getRows();
            Row productERow = rows.get(PRODUCTEROW);
            assertEquals("1 days/ 1 days", productERow.getColumn(CURAGE));

            director = new StatisticsReportDirector(beginDate, today);
            stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            rows = stub.getRows();
            Row productBRow = rows.get(PRODUCTBROW);
            assertEquals("1 days/ 1 days", productBRow.getColumn(CURAGE));
	}
	
	//Test a product that was initially added to the system before the reporting period (to make sure that the days before the reporting period are not counted in the product’s statistics)
	//The product was added many days before the reporting period
	@Test
	public void test4A()
	{
            System.out.println("test3C");

            Date today = new Date();
            this.clearTime(today);
            
            Date beginDate = goBackMonths(today, 3);
            Date itemDate = goBackMonths(today, 5);
            this.changeEntryDates(itemDate);

            director = new StatisticsReportDirector(beginDate, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            List<Row> rows = stub.getRows();
            Row productERow = rows.get(PRODUCTEROW);
            assertEquals("153 days/ 153 days", productERow.getColumn(CURAGE));

            director = new StatisticsReportDirector(beginDate, today);
            stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            rows = stub.getRows();
            Row productBRow = rows.get(PRODUCTBROW);
            assertEquals("153 days/ 153 days", productBRow.getColumn(CURAGE));
	}
	
	//The product was added the day before the reporting period
	@Test
	public void test4B()
	{
            System.out.println("test3C");

            Date today = new Date();
            this.clearTime(today);
            
            Date beginDate = goBackMonths(today, 3);
            Date itemDate = goBackMonths(today, 3);
            this.changeEntryDates(itemDate);

            director = new StatisticsReportDirector(beginDate, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            List<Row> rows = stub.getRows();
            Row productERow = rows.get(PRODUCTEROW);
            assertEquals("91 days/ 91 days", productERow.getColumn(CURAGE));

            director = new StatisticsReportDirector(beginDate, today);
            stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();

            rows = stub.getRows();
            Row productBRow = rows.get(PRODUCTBROW);
            assertEquals("91 days/ 91 days", productBRow.getColumn(CURAGE));
	}
	
	//Test a report period that includes leap day.
	//Leap day is the first day of the period
	@Test
	public void test5A() throws ParseException
	{
		System.out.println("test5A");
		Date start = DateUtils.parseDate("02/29/2012");
		Date end = DateUtils.parseDate("05/29/2012");
		changeEntryDates(addDays(start, 4));
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		assertTrue(compareRows(rows, expRows));
	}
	
	//Leap day is the last day of the period
	@Test
	public void test5B() throws ParseException
	{
		System.out.println("test5B");
		Date start = DateUtils.parseDate("11/29/2011");
		Date end = DateUtils.parseDate("02/29/2012");
		changeEntryDates(addDays(start, 40));
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		assertTrue(compareRows(rows, expRows));
	}
	
	//Leap day is in the middle of the period.\
	@Test
	public void test5C() throws ParseException
	{
		System.out.println("test5C");
		Date start = DateUtils.parseDate("02/01/2012");
		Date end = DateUtils.parseDate("03/01/2012");
		changeEntryDates(addDays(start, 10));
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		
		assertTrue(compareRows(rows, expRows));
	}
	
	//Test a product that was added on leap day.
	@Test
	public void test6() throws ParseException
	{
		System.out.println("test6");
		Date start = DateUtils.parseDate("02/20/2012");
		Date end = DateUtils.parseDate("04/01/2012");
		changeEntryDates(addDays(start, 9));
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		
		assertTrue(compareRows(rows, expRows));
	}
	
	//Test a product that is in more than one storage unit
	//The product has items in multiple storage units
	@Test
	public void test7A() throws ParseException, InsufficientBarcodesException, InvalidAddException, InvalidItemException
	{
		System.out.println("test7A");
		Date start = DateUtils.parseDate("02/20/2012");
		Date end = DateUtils.parseDate("04/01/2012");
		changeEntryDates(addDays(start, 9));
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rowsBeforeTransfer = stub.getRows();
		
		items.moveItemToContainer(itemA1, unitB);
		instance.buildReport();
		
		List<Row> rowsAfterTransfer = stub.getRows();
		
		assertTrue(compareRows(rowsBeforeTransfer, rowsAfterTransfer));
	}
	
	//The product has items only in one storage unit
	@Test
	public void test7B()
	{
		//All tests test this.
	}
	
	//Test a product that had items added and removed on the same day (to make sure the supply 
	//	counts and age counts are correct)
	//The items added were the items removed
	@Test
	public void test8A() throws ParseException
	{
		System.out.println("test8A");
		Date start = DateUtils.parseDate("11/01/2012");
		Date end = DateUtils.parseDate("01/01/2013");
		
		//Change all items entry dates to today.
		//changeEntryDates(new Date());
		
		//Remove some items today.
		items.removeItem(itemA1);
		items.removeItem(itemA2);
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		
		assertTrue(compareRows(rows, expRows));
	}
	
	//The items added were not the items removed
	@Test
	public void test8B() throws ParseException
	{
		System.out.println("test8B");
		Date start = DateUtils.parseDate("11/01/2012");
		Date end = DateUtils.parseDate("01/01/2013");
		
		//Change all items entry dates to be one a day starting on today
		this.changeToConsecutiveEntryDates(addDays(new Date(), -1), 1);
		
		//Remove the item that was added today.
		items.removeItem(itemA2);
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		
		assertTrue(compareRows(rows, expRows));
	}
	
	//Test long report periods
	//One year
	@Test
	public void test9A()
	{
            Date yearAgo = this.goBackDays(new Date(), YEAR);
            Date today = new Date();
            today = this.clearTime(today);
            
            director = new StatisticsReportDirector(yearAgo, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(yearAgo, today);
            assertTrue(compareRows(rows, expRows));
	}
	
	//One decade
	@Test
	public void test9B()
	{
            Date yearAgo = this.goBackDays(new Date(), YEAR * 10);
            Date today = new Date();
            today = this.clearTime(today);
            
            director = new StatisticsReportDirector(yearAgo, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(yearAgo, today);
            assertTrue(compareRows(rows, expRows));
	}
	
	//One century
	@Test
	public void test9C()
	{
            Date yearAgo = this.goBackDays(new Date(), YEAR * 100);
            Date today = new Date();
            today = this.clearTime(today);
            
            director = new StatisticsReportDirector(yearAgo, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            List<Row> rows = stub.getRows();
            List<Row> expRows = generateResults(yearAgo, today);
            assertTrue(compareRows(rows, expRows));
	}
	
	//Test a normal report period with normal products
	@Test
	public void test10() throws ParseException
	{
		System.out.println("test10");
		Date start = DateUtils.parseDate("01/20/2012");
		Date end = DateUtils.parseDate("12/01/2013");
		
		//Change items to be added one every four days.
		changeToConsecutiveEntryDates(start, 4);
		
		//Remove some items
		items.removeItem(itemC6);
		items.removeItem(itemA3);
		items.removeItem(itemD2);
		items.removeItem(itemE1);
		items.removeItem(itemE2);
		
		
		StatisticsReportDirector instance = new StatisticsReportDirector(start, end);
		StubBuilder stub = new StubBuilder();
		instance.setBuilder(stub);
		instance.buildReport();

		//Here are the results.
		List<Row> rows = stub.getRows();
		List<Row> expRows = generateResults(start, end);
		
		assertTrue(compareRows(rows, expRows));
	}
	
	//Test a report with edited product
	//barcode change
//	@Test
//	public void test11A() throws Exception
//	{
//            boolean result = true;
//            
//            Barcode newBarcode = new Barcode();
//            Product temp = new Product(productB);
//            temp.setBarcode(newBarcode);
//            products.editProduct(productB, temp);
//            
//            director = new StatisticsReportDirector(3);
//            StubBuilder stub = new StubBuilder();
//            director.setBuilder(stub);
//            this.director.buildReport();
//            
//            for(Row row : stub.getRows()) {
//                if(row.getColumn(this.DESCRIPTION).equals(productB.getProdDesc())) {
//                    if(!row.getColumn(this.BARCODE).equals(newBarcode.getValue())) {
//                        result = false;
//                        break;
//                    }
//                }
//            }
//            
//            assertTrue(result);
//	}
	
	//description change
	@Test
	public void test11B() throws Exception
	{
            boolean result = true;
            
            String newDesc = "Testing new description";
            Product temp = new Product(productC);
            temp.setProdDesc(newDesc);
            products.editProduct(productC, temp);
            
            director = new StatisticsReportDirector(3);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            for(Row row : stub.getRows()) {
                if(row.getColumn(this.BARCODE).equals(productC.getBarcode().getValue())) {
                    if(!row.getColumn(this.DESCRIPTION).equals(newDesc)) {
                        result = false;
                    }
                    break;
                }
            }
            
            assertTrue(result);
	}
	
	//items switched between product type
        // not valid - cannot change items between products
//	@Test
//	public void test11C()
//	{
//            boolean result = true;
//            
//            director = new StatisticsReportDirector(3);
//            StubBuilder stub = new StubBuilder();
//            director.setBuilder(stub);
//            this.director.buildReport();
//            
//            for(Row row : stub.getRows()) {
//                if(row.getColumn(this.DESCRIPTION).equals(productA.getProdDesc())) {
//                    result = false;
//                    break;
//                }
//            }
//            
//            assertTrue(result);
//	}
	
	//Test removing a product from the system
	//remove a product and check to make sure it isn’t on the report
	@Test
	public void test12A() throws Exception
	{
            boolean result = true;
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            director = new StatisticsReportDirector(3);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            for(Row row : stub.getRows()) {
                if(row.getColumn(this.DESCRIPTION).equals(productA.getProdDesc())) {
                    result = false;
                    break;
                }
            }
            
            assertTrue(result);
	}
	
	//add the same product and make sure it only spans the time since it was re-added
	@Test
	public void test12B() throws Exception
	{
            boolean result = true;
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            products.addProduct(productA, unitA);
            Date entry = this.goBackMonths(new Date(), 1);
            itemA1.setEntryDate(entry);
            itemA1.setProduct(productA);
            itemA1.setExitTime(null);
            items.addItem(itemA1);
            
            director = new StatisticsReportDirector(3);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            // if it's only calculated from the day it was added, the average supply would be 1
            String exp = "1/ 1";
            
            for(Row row : stub.getRows()) {
                if(row.getColumn(this.DESCRIPTION).equals(productA.getProdDesc())) {
                    if(!row.getColumn(this.SUPPLY_CUR_AVG).equals(exp)) {
                        result = false;
                        break;
                    }
                }
            }
            
            assertTrue(result);
	}
	
	//Test the maximum supply of a product.
	//Maximum supply was on first day of report (decrease in supply)
	@Test
	public void test13A() throws Exception
	{
            Date today = new Date();
            today = this.clearTime(today);
            Date beginningOfReport = this.goBackMonths(today, 3);
            
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            products.addProduct(productA, unitA);
            
            itemA1.setEntryDate(beginningOfReport);
            itemA1.setProduct(productA);
            itemA1.setExitTime(null);
            itemA2.setEntryDate(beginningOfReport);
            itemA2.setProduct(productA);
            itemA2.setExitTime(null);
            itemA3.setEntryDate(beginningOfReport);
            itemA3.setProduct(productA);
            itemA3.setExitTime(null);
            items.addItem(itemA1);
            items.addItem(itemA2);
            items.addItem(itemA3);
            
            director = new StatisticsReportDirector(beginningOfReport, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            assertTrue(this.compareRows(this.generateResults(beginningOfReport, today), stub.getRows()));
	}
	
	//Maximum supply was on last day of report (increase in supply)
	@Test
	public void test13B() throws Exception
	{
            
            Date today = new Date();
            today = this.clearTime(today);
            Date lastOfReport = this.goBackDays(today, 0);
            
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            products.addProduct(productA, unitA);
            
            itemA1.setEntryDate(lastOfReport);
            itemA1.setProduct(productA);
            itemA1.setExitTime(null);
            itemA2.setEntryDate(lastOfReport);
            itemA2.setProduct(productA);
            itemA2.setExitTime(null);
            itemA3.setEntryDate(lastOfReport);
            itemA3.setProduct(productA);
            itemA3.setExitTime(null);
            items.addItem(itemA1);
            items.addItem(itemA2);
            items.addItem(itemA3);
            
            director = new StatisticsReportDirector(lastOfReport, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            assertTrue(this.compareRows(this.generateResults(lastOfReport, today), stub.getRows()));
	}
	
	//Max supply was on another day of report (increase and then decrease)
	@Test
	public void test13C() throws Exception
	{
            
            Date today = new Date();
            today = this.clearTime(today);
            Date middleOfReport = this.goBackMonths(today, 1);
            
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            products.addProduct(productA, unitA);
            
            itemA1.setEntryDate(middleOfReport);
            itemA1.setProduct(productA);
            itemA1.setExitTime(null);
            itemA2.setEntryDate(middleOfReport);
            itemA2.setProduct(productA);
            itemA2.setExitTime(null);
            itemA3.setEntryDate(middleOfReport);
            itemA3.setProduct(productA);
            itemA3.setExitTime(null);
            items.addItem(itemA1);
            items.addItem(itemA2);
            items.addItem(itemA3);
            
            director = new StatisticsReportDirector(middleOfReport, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            assertTrue(this.compareRows(this.generateResults(middleOfReport, today), stub.getRows()));
	}
	
	//Max supply is constant throughout the reporting period (constant supply)
	@Test
	public void test13D() throws Exception
	{
            Date today = new Date();
            today = this.clearTime(today);
            Date beginningOfReport = this.goBackMonths(today, 3);
            Date beforeReport = this.goBackMonths(today, 4);
            
            // need to remove the items before you can remove the product
            items.removeItem(itemA1);
            items.removeItem(this.itemA2);
            items.removeItem(this.itemA3);
            products.deleteProduct(productA, this.containers.getRoot());
            
            products.addProduct(productA, unitA);
            
            itemA1.setEntryDate(beforeReport);
            itemA1.setProduct(productA);
            itemA1.setExitTime(null);
            itemA2.setEntryDate(beforeReport);
            itemA2.setProduct(productA);
            itemA2.setExitTime(null);
            itemA3.setEntryDate(beforeReport);
            itemA3.setProduct(productA);
            itemA3.setExitTime(null);
            items.addItem(itemA1);
            items.addItem(itemA2);
            items.addItem(itemA3);
            
            director = new StatisticsReportDirector(beginningOfReport, today);
            StubBuilder stub = new StubBuilder();
            director.setBuilder(stub);
            this.director.buildReport();
            
            assertTrue(this.compareRows(this.generateResults(beginningOfReport, today), stub.getRows()));
	}
	
	//Test the minimum supply of a product
	//Min supply was on the first day of report period
	@Test
	public void test14A()
	{
		System.out.println("test14A");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		Date dayAfter = new Date(beginDate.getTime());
		dayAfter.setDate(dayAfter.getDate() + 1);
		
		items.editItem(itemD1, beginDate);
		items.editItem(itemD2, beginDate);
		items.editItem(itemD3, dayAfter);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("2/12", productDRow.getColumn(SUPPLY_MIN_MAX));
		
		director = new StatisticsReportDirector(dayAfter, today);
		stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		rows = stub.getRows();
		productDRow = rows.get(PRODUCTDROW);
		assertEquals("3/12", productDRow.getColumn(SUPPLY_MIN_MAX));
	}
	
	//Min supply was on the last day
	@Test
	public void test14B()
	{
		System.out.println("test14B");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 1);
		
		this.changeEntryDates(beginDate);
		items.removeItem(itemA1);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productARow = rows.get(PRODUCTAROW);
		assertEquals("2/3", productARow.getColumn(SUPPLY_MIN_MAX));
	}
	
	//Min supply was on another day
	@Test
	public void test14C()
	{
		System.out.println("test14C");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		Date oneMonthBack = goBackMonths(today, 1);
		
		items.editItem(itemD1, beginDate);
		items.editItem(itemD2, beginDate);
		items.editItem(itemD3, beginDate);
		items.removeItem(itemD1);
		itemD1.setExitTime(oneMonthBack);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("2/11", productDRow.getColumn(SUPPLY_MIN_MAX));
	}
	
	//Min supply is constant throughout the reporting period
	@Test
	public void test14D()
	{
		System.out.println("test14D");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 2);
		Date entry = goBackMonths(beginDate, 1);
		
		this.changeEntryDates(entry);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productBRow = rows.get(PRODUCTBROW);
		assertEquals("1/1", productBRow.getColumn(SUPPLY_MIN_MAX));	
	}
	
	//Test used supply
	//Items were used on first day of report
	@Test
	public void test15A()
	{
		System.out.println("test15A");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		Date oneMonthBack = goBackMonths(today, 1);
		
		this.changeEntryDates(beginDate);
		items.removeItem(itemE1);
		items.removeItem(itemE2);
		itemE1.setExitTime(beginDate);
		itemE2.setExitTime(beginDate);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productERow = rows.get(PRODUCTEROW);
		assertEquals("2/4", productERow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Items were used on last day of report
	@Test
	public void test15B()
	{
		System.out.println("test15B");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		
		this.changeEntryDates(beginDate);
		items.removeItem(itemC1);
		items.removeItem(itemC2);
		items.removeItem(itemC3);
		items.removeItem(itemC4);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productCRow = rows.get(PRODUCTCROW);
		assertEquals("4/6", productCRow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Items were used some other day
	@Test
	public void test15C()
	{
		System.out.println("test15C");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 5);
		Date threeMonthsBack = goBackMonths(today, 3);
		Date twoMonthsBack = goBackMonths(today, 2);
		
		this.changeEntryDates(beginDate);
		items.removeItem(itemD1);
		items.removeItem(itemD2);
		items.removeItem(itemD3);
		items.removeItem(itemD4);
		itemD1.setExitTime(threeMonthsBack);
		itemD2.setExitTime(threeMonthsBack);
		itemD3.setExitTime(threeMonthsBack);
		itemD4.setExitTime(threeMonthsBack);
		itemD5.setExitTime(threeMonthsBack);
		itemD8.setExitTime(twoMonthsBack);
		itemD9.setExitTime(twoMonthsBack);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("7/12", productDRow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Test Added supply
	//Items were added on the first day
	@Test
	public void test16A()
	{
		System.out.println("test16A");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		
		this.changeEntryDates(beginDate);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productERow = rows.get(PRODUCTEROW);
		assertEquals("0/4", productERow.getColumn(SUPPLY_USED_ADDED));
		
		director = new StatisticsReportDirector(beginDate, today);
		stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		rows = stub.getRows();
		Row productBRow = rows.get(PRODUCTBROW);
		assertEquals("0/1", productBRow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Items were added on the last day
	@Test
	public void test16B()
	{
		System.out.println("test16B");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		
		director = new StatisticsReportDirector(3);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("0/12", productDRow.getColumn(SUPPLY_USED_ADDED));
		
		director = new StatisticsReportDirector(beginDate, today);
		stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		rows = stub.getRows();
		Row productARow = rows.get(PRODUCTAROW);
		assertEquals("0/3", productARow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Other day
	@Test
	public void test16C()
	{
		System.out.println("test16C");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		Date oneMonthBack = goBackMonths(today, 1);
		Date twoMonthsBack = goBackMonths(today, 2);
		items.editItem(itemD1, twoMonthsBack);
		items.editItem(itemD2, twoMonthsBack);
		items.editItem(itemD3, twoMonthsBack);
		items.editItem(itemD4, twoMonthsBack);
		items.editItem(itemD5, oneMonthBack);
		items.editItem(itemD6, oneMonthBack);
		items.editItem(itemD7, oneMonthBack);
		items.editItem(itemD8, oneMonthBack);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("0/12", productDRow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Before Test period (not included in added, but in average)
	@Test
	public void test16D()
	{
		System.out.println("test16D");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 2);
		Date oneMonthBack = goBackMonths(today, 1);
		Date threeMonthsBack = goBackMonths(today, 3);
		this.changeEntryDates(threeMonthsBack);
		items.editItem(itemD5, oneMonthBack);
		items.editItem(itemD6, oneMonthBack);
		items.editItem(itemD7, oneMonthBack);
		items.editItem(itemD8, oneMonthBack);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("0/4", productDRow.getColumn(SUPPLY_USED_ADDED));
	}
	
	//Test the Age of the items
	//The product was added on the last day of the test period
	@Test
	public void test17A()
	{
		System.out.println("test17A");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 2);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		Row productDRow = rows.get(PRODUCTDROW);
		assertEquals("0 days/ 0 days", productDRow.getColumn(USEDAGE));	
		assertEquals("0 days/ 0 days", productDRow.getColumn(CURAGE));			
	}
	
	//The product was added on the first day of the test period, and all item have also been removed
	@Test
	public void test17B()
	{
		System.out.println("test17B");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 4);
		Date twoMonthsBack = goBackMonths(today, 2);
		Date threeMonthsBack = goBackMonths(today, 3);
		this.changeEntryDates(beginDate);
		items.removeItem(itemA1);
		items.removeItem(itemA2);
		items.removeItem(itemA3);
		items.removeItem(itemB1);
		itemA1.setEntryDate(twoMonthsBack);
		itemA2.setEntryDate(threeMonthsBack);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		List<Integer> columnsToCheck = new ArrayList();
		columnsToCheck.add(USEDAGE);
		columnsToCheck.add(CURAGE);
		List<Row> expRows = this.generateResults(beginDate, today);
		assertTrue(compareRows(rows, expRows, columnsToCheck));
	}
	
	//No items were used during the test period
	@Test
	public void test17C()
	{
		System.out.println("test17C");
		
		Date today = new Date();
		this.clearTime(today);
		Date beginDate = goBackMonths(today, 3);
		Date twoMonthsBack = goBackMonths(today, 2);
		Date fourMonthsBack = goBackMonths(today, 4);
		this.changeEntryDates(fourMonthsBack);
		items.editItem(itemA1, twoMonthsBack);
		items.editItem(itemA2, twoMonthsBack);
		items.editItem(itemA3, twoMonthsBack);
		items.editItem(itemB1, twoMonthsBack);
		items.editItem(itemC1, beginDate);
		items.editItem(itemC2, beginDate);
		items.editItem(itemC3, beginDate);
		items.editItem(itemC4, beginDate);
		items.editItem(itemC5, beginDate);
		items.editItem(itemC6, beginDate);
		
		director = new StatisticsReportDirector(beginDate, today);
		StubBuilder stub = new StubBuilder();
		director.setBuilder(stub);
		this.director.buildReport();
		
		List<Row> rows = stub.getRows();
		List<Integer> columnsToCheck = new ArrayList();
		columnsToCheck.add(USEDAGE);
		columnsToCheck.add(CURAGE);
		List<Row> expRows = this.generateResults(beginDate, today);
		assertTrue(compareRows(rows, expRows, columnsToCheck));
		assertTrue(true);
	}

}
