/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author owner
 */
public class HTMLBuilder implements ReportBuilder {

    private StringBuilder content;
    private static final String DEFAULT_FILE = "reports/index.html";
    
    public HTMLBuilder() {
        content = new StringBuilder();
        content.append("<html><head>");
        content.append("<link rel='stylesheet' href='includes/main.css' />");
        content.append("</head><body>");
    }
    
    /**
     * This will add a table to the HTML document
     * @param columns - number of columns the table will have
     * @param headers - the titles of each columns (order specific)
     * @param rows - the information to put in the table
     */
    @Override
    public void buildTable(int columns, List<String> headers, List<Row> rows) {
//        throw new UnsupportedOperationException("Not supported yet.");
        assert(columns == headers.size());
         
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        
        // build header row
        sb.append("<tr>");
            for(String heading : headers) {
                sb.append("<th>");
                sb.append(heading);
                sb.append("</th>");
            }
        sb.append("</tr>");
        
        for(Row row : rows) {
            assert(row.getSize() == columns);
            sb.append("<tr>");
            
            Iterator<String> cols = row.getColumns();
            while(cols.hasNext()) {
                sb.append("<td>");
                sb.append(cols.next());
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        
        sb.append("</table>");
        
        content.append(sb.toString());
    }

    /**
     * This will add an unordered list to the HTML document
     * @param items - the items that are to be added to the list
     */
    @Override
    public void buildList(List<String> items) {
//        throw new UnsupportedOperationException("Not supported yet.");
        buildList(items, BulletType.dash);
    }
    
    /**
     * This will add an unordered list to the HTML document
     * @param items - the items that are to be added to the list
     */
    @Override
    public void buildList(List<String> items, BulletType bullet) {
//        throw new UnsupportedOperationException("Not supported yet.");
        StringBuilder sb = new StringBuilder();
        sb.append("<ul class='"+bullet.toString()+"'>");
        for(String item : items) {
            sb.append("<li>");
            sb.append(item);
            sb.append("</li>");
        }
        sb.append("</ul>");
        
        content.append(sb.toString());
    }

    /**
     * This will add a title to the HTML document that will be center aligned
     * @param size - the size of the header
     * @param title - the content of the header
     */
    @Override
    public void addTitle(FontSize size, String title) {
//        throw new UnsupportedOperationException("Not supported yet.");
        addTitle(size, title, Alignment.center);
    }

    /**
     * This will add a title to the HTML document that has the alignment specified
     * @param size - the size of the header
     * @param title - the content of the header
     * @param align - how the header will be aligned
     */
    @Override
    public void addTitle(FontSize size, String title, Alignment align) {
//        throw new UnsupportedOperationException("Not supported yet.");
        StringBuilder sb = new StringBuilder();
        String tag;
        switch(size) {
            case large:
                tag = "h1";
                break;
            case medium:
                tag = "h3";
                break;
            case average:
                tag = "h4";
                break;
            case small:
                tag = "h6";
                break;
            default:
                tag = "h4";
                break;
        }
        
        String style = "text-align:";
        switch(align) {
            case left:
                style += "left";
                break;
            case center:
                style += "center";
                break;
            case right:
                style += "right";
                break;
            default:
                break;
        }
        style += ";";
        
        sb.append("<" + tag + " style=\"" + style + "\">");
        sb.append(title);
        sb.append("</" + tag + ">");
        
        content.append(sb.toString());
    }

    /**
     * This will add a paragraph (text) to the HTML document with no spacing before or after
     * @param paragraph - the content of the paragraph to be added
     */
    @Override
    public void addParagraph(String paragraph) {
//        throw new UnsupportedOperationException("Not supported yet.");
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        sb.append(paragraph);
        sb.append("</p>");
        content.append(sb.toString());
    }

    /**
     * This will print the report and return the location of the file
     * @return String that contains the location of the file or null if there was an error
     */
    @Override
    public String printReport() {
        String fileName = DEFAULT_FILE;
        File index = new File(fileName);
        this.closeHtml();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(index));
            bw.write(this.content.toString());
            bw.close();
            return fileName;
        } catch (IOException ex) {
            Logger.getLogger(HTMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public String getContent() {
        this.closeHtml();
        return this.content.toString();
    }
    
    
    private void closeHtml() {
        content.append("</body></html>");
    }
}
