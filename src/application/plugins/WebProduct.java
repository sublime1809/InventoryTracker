/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

/**
 * Holds data for the Web Product found via plugin search. 
 * @author Odin
 */
public class WebProduct {
    String description;
    String barcode;

	public WebProduct(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
}
