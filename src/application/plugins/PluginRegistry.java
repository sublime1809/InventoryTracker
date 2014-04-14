/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.plugins;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Odin
 */
public class PluginRegistry {
    private BarcodePlugin firstPlugin = null;
    
    public PluginRegistry(){
		loadPlugins();
	}
	
    /**
     * initially loads the plugins for use
     * 
     * @return
     */

    public void loadPlugins(){
        if(firstPlugin != null) return;
		try {
			FileInputStream fis = new FileInputStream("pluginNames.txt");
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader input = new BufferedReader(new InputStreamReader(dis));
			String name;
			BarcodePlugin newPlugin;
			BarcodePlugin lastPlugin = null;
			while ((name = input.readLine()) != null){
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
				try {
					Class curClass = classLoader.loadClass(name);
					newPlugin = (BarcodePlugin)curClass.newInstance();
					if(lastPlugin == null){
						firstPlugin = newPlugin;
					}
					else{
						lastPlugin.setSuccessor(newPlugin);
					}
					lastPlugin = newPlugin;
				} catch (ClassNotFoundException ex) {
				} catch (InstantiationException ex) {
				} catch (IllegalAccessException ex) {
				}
			}
		} catch (IOException e){
		}
    } 
    
     /**
     * finds the product of the given barcode string using all the plugins
     * 
     * @param barcode
     * @return
     */
    public WebProduct findProduct(String barcode){
        if(firstPlugin == null){
			loadPlugins();
			if(firstPlugin == null){
				return null;
			}
		}
        return firstPlugin.findProduct(barcode);
    }
    
    
}
