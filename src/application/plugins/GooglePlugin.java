/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Odin
 */
public class GooglePlugin implements BarcodePlugin {
    
    BarcodePlugin successor;
    
    public GooglePlugin(){
        
    }
    
    @Override
    /**
     * finds the product of the given barcode string using a google plugin
     * 
     * @param barcode
     * @return
     */
    public WebProduct findProduct(String barcode) {
		String baseURL = "https://www.googleapis.com/shopping/search/v1/public/products?"
				+ "country=US&restrictBy=gtin=" + barcode 
				+ "&key=AIzaSyCQGbnB9EAmutaU5PaCsYroRmq0iBFpeKI";
		
		WebProduct result = null;

		try {
			String response = "";
			URL url = new URL(baseURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = "";
			while ((line = in.readLine()) != null){
				//System.out.println(line);
				response += line;
			}
			
			JSONObject jsonResult = (JSONObject)JSONValue.parse(response);
			JSONArray items = (JSONArray) jsonResult.get("items");
			
			for(int i = 0; i < items.size() && i < 10; i++)
			{
				JSONObject item = (JSONObject)items.get(i);
				JSONObject product = (JSONObject)item.get("product");
				String gtin = (String)product.get("gtin");
				if(gtin.endsWith(barcode))
				{
					result = new WebProduct((String)product.get("title"));
					break;
				}
			}
			
		} catch (Exception ex) {
			if (successor != null){
				return successor.findProduct(barcode);
			}
		}
		if (successor != null && result == null){
			return successor.findProduct(barcode);
		}	
		return result;
    }

    @Override
    /**
     *  sets the next barcodePlugin to use next.
     * @param plugin
     */
    public void setSuccessor(BarcodePlugin plugin) {
		this.successor = plugin;
    }
    
}
