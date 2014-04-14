/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.printing;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author owner
 */
public abstract class PDFGenerator {

    protected Document document;
    private PdfWriter writer;
    private String fileName;
    private boolean hasBeenPrinted = false;

    /**
     * Constructor: creates a new PDFGenerator
     *
     * @param fileName	the file that the pdf should be saved to
     * @throws DocumentException
     */
    protected PDFGenerator(String fileName) throws DocumentException {
        try {
            this.fileName = fileName + ".pdf";
            document = new Document();
            writer = PdfWriter.getInstance(document, new FileOutputStream(new File(this.fileName)));
        } catch (FileNotFoundException ex) {
            constructorHelper(fileName, 0);
        }
    }

    protected void constructorHelper(String fileName, int current) throws DocumentException {
        try {
            this.fileName = fileName + "_" + current + ".pdf";
            document = new Document();
            writer = PdfWriter.getInstance(document, new FileOutputStream(new File(this.fileName)));
        } catch (FileNotFoundException ex) {
            constructorHelper(fileName, current + 1);
        }
    }

    /**
     * Prints the content that has been added to the document to the file given
     * previously
     */
    protected void print() {
        document.open();
        printContent(document);
        document.close();
        this.hasBeenPrinted = true;
    }

    /**
     * Checks to see if it was printed successfully
     *
     * @return	true if it has printed successfully
     */
    protected boolean hasBeenPrinted() {
        return this.hasBeenPrinted;
    }

    /**
     * Gets the writer of this generator
     *
     * @return	the generator's writer
     */
    protected PdfWriter getWriter() {
        return this.writer;
    }

    /**
     * gets the name of the file that the generator will same to
     *
     * @return	the filename where the generator will same its finished product
     */
    public String getLocation() {
        if (this.hasBeenPrinted() == false) {
            this.print();
        }
        return this.fileName;
    }

    /**
     * Prints the content of the document
     *
     * @param doc	the document that should be printed
     */
    protected abstract void printContent(Document doc);
}
