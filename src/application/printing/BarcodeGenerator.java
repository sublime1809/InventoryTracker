/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.printing;

import application.item.Item;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author owner
 */
public class BarcodeGenerator extends PDFGenerator {
   
    private List<Item> items;
    private static final String DEFAULT_FILENAME = "barcode";
    
    /**
	 * Constructor: creates a new BarcodeGenerator
	 * 
	 * @param fileName		the file that the generator should print the barcodes to
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public BarcodeGenerator(String fileName) throws DocumentException, FileNotFoundException {
        super(fileName);
        items = new ArrayList<Item>();
    }
    
    /**
     * Constructor: creates a new BarcodeGenerator
     * @param items		a list of items to print in a future time
	 * @param fileName	the file that the generator should print the barcodes to
	 * @throws DocumentException
	 * @throws FileNotFoundException  
     */
    public BarcodeGenerator(List<Item> items, String fileName) 
			throws DocumentException,FileNotFoundException {
        super(fileName);
        this.items = items;
    }
    
    /**
	 * Constructor: creates a new BarcodeGenerator
	 * @param items		a list of items to print in a future time
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public BarcodeGenerator(List<Item> items) throws DocumentException,FileNotFoundException {
        super(DEFAULT_FILENAME);
        this.items = items;
    }
    
    /**
	 * Constructor: creates a new BarcodeGenerator
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public BarcodeGenerator()throws DocumentException, FileNotFoundException {
        super(DEFAULT_FILENAME);
        items = new ArrayList<Item>();
    }
    
    /**
	 * Adds an item that should be printed at a later time
	 * @param item		the item to be printed
	 */
	public void addItem(Item item) {
        items.add(item);
    }
    
    /**
	 * Prints all the items that have previously been added to the given doc.
	 * @param doc		the output for the printed barcodes
	 */
	@Override
    protected void printContent(Document doc) {
        
        try {
            
            PdfPTable table;
            table = new PdfPTable(6);
            
            PdfPCell cell; 
            Font font = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
            for(Item item : items) {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER); 
                
                Paragraph name = new Paragraph(8, item.getProduct().getProdDesc(), font);
                
                String datesString = "";
                if(item.getEntryDate() != null) {
                    datesString += formatDate(item.getEntryDate());
                } 
                if(item.getExpirationDate() != null) {
                    datesString += " exp " + formatDate(item.getExpirationDate());
                }
                Paragraph dates = new Paragraph(8, datesString, font);
                
                cell.addElement(name);
                cell.addElement(dates);
                BarcodeEAN barcode = new BarcodeEAN();
                barcode.setCodeType(Barcode.UPCA);
                barcode.setCode(item.getBarcode().getValue());

                Image img = barcode.createImageWithBarcode
						(getWriter().getDirectContent(), null, null);
                cell.addElement(img);
                table.addCell(cell);
            }
            for(int i = (items.size() % 6); i < 6; i++) {
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
            }
            document.add(table); 
        } catch (DocumentException ex) {
            Logger.getLogger(BarcodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String formatDate(Date date) {
        return Integer.toString(date.getMonth() + 1) + "/" + Integer.toString(date.getDate()) 
				+"/"+ Integer.toString(date.getYear() % 100);
    }
    
}
