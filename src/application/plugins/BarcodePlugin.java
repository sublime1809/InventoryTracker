/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

/**
 *
 * @author Odin
 */
public interface BarcodePlugin {
    
    
    /**
     * finds the product of the given barcode string
     * 
     * @param barcode
     * @return
     */
    public WebProduct findProduct(String barcode);
    
    /**
     *  sets the next barcodePlugin to use next.
     * @param plugin
     */
    public void setSuccessor(BarcodePlugin plugin);
}
