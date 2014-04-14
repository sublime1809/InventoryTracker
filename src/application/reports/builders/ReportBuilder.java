/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

import java.util.List;

/**
 *
 * @author owner
 */
public interface ReportBuilder {
    
    /**
     * This will add a table to the HTML document
     * @param columns - number of columns the table will have
     * @param headers - the titles of each columns (order specific)
     * @param rows - the information to put in the table
     */
    public void buildTable(int columns, List<String> headers, List<Row> rows);

    /**
     * This will add an unordered list to the HTML document
     * @param items - the items that are to be added to the list
     */
    public void buildList(List<String> items);
    
    /**
     * This will add an unordered list to the HTML document
     * @param items - the items that are to be added to the list
     * 
     */
    public void buildList(List<String> items, BulletType bullets);

    /**
     * This will add a title to the HTML document that will be center aligned
     * @param size - the size of the header
     * @param title - the content of the header
     */
    public void addTitle(FontSize size, String title);

    /**
     * This will add a title to the HTML document that has the alignment specified
     * @param size - the size of the header
     * @param title - the content of the header
     * @param align - how the header will be aligned
     */
    public void addTitle(FontSize size, String title, Alignment align);

    /**
     * This will add a paragraph (text) to the HTML document with no spacing before or after
     * @param paragraph - the content of the paragraph to be added
     */
    public void addParagraph(String paragraph);

    /**
     * This will print the report and return the location of the file
     * @return String that contains the location of the file
     */
    public String printReport();
    
    /**
     * For testing purposes only
     * @return 
     */
    public String getContent();
}
