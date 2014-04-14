/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Odin
 */
public class SearchUPCPlugin implements BarcodePlugin {

    private static final String BASE_SITE = "http://searchupc.com/default.aspx";
    private static final String barcode_variable = "q";
    
    private BarcodePlugin next;
    
    @Override
    /**
     * finds the product of the given barcode string using SearchUPC website
     * 
     * @param barcode
     * @return
     */
    public WebProduct findProduct(String barcode) {
        BufferedReader reader = null;
        try {
            //        throw new UnsupportedOperationException("Not supported yet.");
            URL page = new URL(BASE_SITE + "?" + this.barcode_variable + "=" + barcode);
//            URLConnection conn = page.openConnection();
//            conn.setRequestProperty(this.barcode_variable, barcode);
//
//            conn.connect();
            reader = new BufferedReader(new InputStreamReader(page.openStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            reader.close();
            
//            System.out.println(sb.toString());
//            System.out.println("name: " + this.getName(sb.toString()));
            String name = this.getName(sb.toString());
            if(name != null) {
                return new WebProduct(name);
            } else if (next != null){
                return next.findProduct(barcode);
            } 
        } catch (Exception ex) {
            if (next != null){
                return next.findProduct(barcode);
            }
        } 
        return null;
    }

    
    private String getName(String content) {
        String regex = "<td align=\"left\"><a href=\"/rd\\.aspx\\?u=[^\\s]* "
				+ "target=\"_blank\">(.*?)</a></td>";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(content);
        if(match.find()) {
            return match.group(1);
        }
        return null;
    }
    
    @Override
    /**
     *  sets the next barcodePlugin to use next.
     * @param plugin
     */
    public void setSuccessor(BarcodePlugin plugin) {
        this.next = plugin;
    }
    
}
