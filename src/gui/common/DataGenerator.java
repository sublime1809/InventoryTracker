/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.common;

import gui.product.ProductData;
import application.product.Product;
import common.Barcode;
import common.Size;
import common.SizeUnit;

/**
 *
 * @author owner
 */
public class DataGenerator {
    
    private DataGenerator instance;
    /**
	 * Gets the instance of this singleton class
	 * @return	the one and only instance of this class
	 */
	public DataGenerator getInstance() {
        if(instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }
    
    private DataGenerator() {
        
    }
    
    /**
	 * Generates a productData object based on the given product
	 * @param product	the product to be replicated
	 * @return			a productData object representing the product passed in
	 */
	public static ProductData generateProductData(Product product) {
        return null;
    }
	
	/**
	 * Converts a SizeUnits Object to a SizeUnit Object
	 * @param unit
	 * @return
	 */
	public static SizeUnit convertSizeUnits(SizeUnits unit){
            return SizeUnit.parse(unit.toString());
	}
        
	/**
	 * Converts a SizeUnit Object to a SizeUnits Object
	 * @param unit
	 * @return
	 */
	public static SizeUnits convertSizeUnit(SizeUnit unit) {
		switch (unit){
			case Pounds: 
					return SizeUnits.Pounds;
			case Ounces:
					return SizeUnits.Ounces;
			case Grams:
					return SizeUnits.Grams;
			case Kilograms:
					return SizeUnits.Kilograms;
			case Gallons:
					return SizeUnits.Gallons;
			case Quarts:
					return SizeUnits.Quarts;
			case Pints:
					return SizeUnits.Pints;
			case FluidOunces:
					return SizeUnits.FluidOunces;
			case Liters:
					return SizeUnits.Liters;
			default:
				return SizeUnits.Count;
		}
	}
}
