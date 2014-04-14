/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence.SER;

import application.AssistantToTheRegionalManager;
import application.item.ItemsManager;
import application.product.ProductsManager;
import application.storage.RemovedItemsManager;
import application.storage.StorageUnitsManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author skitto
 */
public class SerializableDatabase
{

	/**
	 * Saves all the data in the application using the AssistantToTheRegionalManager
	 */
	public static void saveApplication() {
		try
		{
			OutputStream out = new FileOutputStream("savedData/HIT.ser");
			ObjectOutputStream objectOut = new ObjectOutputStream(out);
			objectOut.writeObject(AssistantToTheRegionalManager.getInstance());
			objectOut.close();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all the data into the application
	 */
	public static void loadApplication() {
		try
		{
			InputStream in = new FileInputStream("savedData/HIT.ser");
			ObjectInputStream objectIn = new ObjectInputStream(in);
			
			AssistantToTheRegionalManager temp = AssistantToTheRegionalManager.getInstance();
			try {
				AssistantToTheRegionalManager data = 
						(AssistantToTheRegionalManager) objectIn.readObject();
				temp.setInstance(data);
			} catch (ClassNotFoundException ex) {
			}

			objectIn.close();
			in.close();
		}
		catch (IOException e)
		{
		}	
	}
}
