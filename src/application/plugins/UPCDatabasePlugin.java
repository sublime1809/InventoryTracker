/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Stephen Kitto
 */
public class UPCDatabasePlugin implements BarcodePlugin {
	
	private BarcodePlugin next;
	
	public UPCDatabasePlugin(){
		next = null;
	}

    @Override
    /**
     * finds the product of the given barcode string using UPCDatabase website
     * 
     * @param barcode
     * @return
     */
    public WebProduct findProduct(String barcode) {
		final String baseURL = "http://www.upcdatabase.com/item/";
		WebProduct result = null;

		try {
			String html = "";
			URL url = new URL(baseURL + barcode);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = "";
			while ((line = in.readLine()) != null){
				//System.out.println(line);
				html += line;
			}
			
			String exp = "<tr><td>Description</td><td></td><td>(.*?)</td></tr>";
			Pattern pattern = Pattern.compile(exp);
			Matcher match = pattern.matcher(html);
			while(match.find()){
				result = new WebProduct(match.group(1));
			}
			//System.out.println(html);
		} catch (Exception ex) {
			if (next != null){
				return next.findProduct(barcode);
			}
		}
		if (next != null && result == null){
			return next.findProduct(barcode);
		}	
		return result;
    }

    @Override
    /**
     *  sets the next barcodePlugin to use next.
     * @param plugin
     */
    public void setSuccessor(BarcodePlugin plugin) {
        next = plugin;
    }
    
}
