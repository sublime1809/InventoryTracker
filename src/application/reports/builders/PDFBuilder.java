/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

import application.printing.PDFGenerator;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author owner
 */
public class PDFBuilder extends PDFGenerator implements ReportBuilder {

    private static final String DEFAULT_FILE = "reports/report";
    private List<Element> elements = new ArrayList<Element>();
    
    public PDFBuilder() throws DocumentException {
        super(DEFAULT_FILE);
    }
    
    /**
     * This will add a table to the PDF document
     * @param columns - number of columns the table will have
     * @param headers - the titles of each columns (order specific)
     * @param rows - the information to put in the table
     */
    @Override
    public void buildTable(int columns, List<String> headers, List<Row> rows) {
        assert(headers.size() == columns);
        PdfPTable table = new PdfPTable(columns);
        
        Font header = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
        for(String head : headers) {
            PdfPCell cell = new PdfPCell();
            cell.addElement(new Paragraph(head, header));
            table.addCell(cell);
        }
        
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 6);
        for(Row row : rows) {
            assert(row.getSize() == columns);
            
            Iterator<String> info = row.getColumns();
            while(info.hasNext()) {
                PdfPCell cell = new PdfPCell();
                cell.addElement(new Paragraph(info.next(), infoFont));
                table.addCell(cell);
            }
        }
        elements.add(table);
        
    }

    /**
     * This will add an unordered list to the PDF document
     * @param items - the items that are to be added to the list
     */
    @Override
    public void buildList(List<String> items) {
        com.itextpdf.text.List list = new com.itextpdf.text.List();
        for(String item : items) {
            ListItem li = new ListItem(item);
            list.add(li);
        }
        elements.add(list);
    }
    
    /**
     * This will add an unordered list to the PDF document
     * @param items - the items that are to be added to the list
     * @param type
     */
    @Override
    public void buildList(List<String> items, BulletType type) {
        com.itextpdf.text.List list = new com.itextpdf.text.List();
        for(String item : items) {
            ListItem li = new ListItem(item);
            list.add(li);
        }
        elements.add(list);
    }

    /**
     * This will add a title to the PDF document that will be center aligned
     * @param size - the size of the header
     * @param title - the content of the header
     */
    @Override
    public void addTitle(FontSize size, String title) {
        addTitle(size, title, Alignment.center);
//        throw new UnsupportedOperationException("Not ssupported yet.");
    }

    /**
     * This will add a title to the PDF document that has the alignment specified
     * @param size - the size of the header
     * @param title - the content of the header
     * @param align - how the header will be aligned
     */
    @Override
    public void addTitle(FontSize size, String titleS, Alignment align) {
        
        Font font = new Font(Font.FontFamily.HELVETICA, size.getSize(), Font.BOLD);
        
        int alignment = Element.ALIGN_CENTER;
        switch(align) {
            case left:
                alignment = Element.ALIGN_LEFT;
                break;
            case center:
                alignment = Element.ALIGN_CENTER;
                break;
            case right:
                alignment = Element.ALIGN_MIDDLE;
                break;
            default:
                break;
        }
        
        Paragraph title = new Paragraph(titleS, font);
        title.setAlignment(alignment);
        title.setSpacingAfter(5);
        
        elements.add(title);
        
    }

    /**
     * This will add a paragraph (text) to the PDF document with no spacing before or after
     * @param paragraph - the content of the paragraph to be added
     */
    @Override
    public void addParagraph(String paragraph) {
//        throw new UnsupportedOperationException("Not supported yet.");
        Paragraph p = new Paragraph(paragraph);
        elements.add(p);
    }

    /**
     * This will print the report and return the location of the file
     * @return String that contains the location of the file
     */
    @Override
    public String printReport() {
        this.print();
        return this.getLocation();
    }
    
    @Override
    public String getContent() {
        throw new UnsupportedOperationException("Not supported for PDF.");
    }

    @Override
    protected void printContent(Document doc) {
        
        for(Element el : elements) {
            try {
                this.document.add(el);
            } catch (DocumentException ex) {
                Logger.getLogger(PDFBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
