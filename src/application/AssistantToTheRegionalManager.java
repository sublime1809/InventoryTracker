/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import application.item.ItemsManager;
import application.product.ProductsManager;
import application.storage.RemovedItemsManager;
import application.storage.StorageUnitsManager;
import java.io.Serializable;

/**
 * This is a singleton class that holds the instances of the managers
 * @author Stephen Kitto
 * @author Leckie Gunter
 */
public class AssistantToTheRegionalManager implements Serializable {
	
	private static AssistantToTheRegionalManager instance;
	private StorageUnitsManager SUM;
	private ItemsManager IM;
	private ProductsManager PM;
	private RemovedItemsManager RIM;
	
	/**
	 * Gets the instance of this singleton class
	 * 
	 * @return	the one and only instance of this class
	 */
	public static AssistantToTheRegionalManager getInstance(){
		if(instance == null){
			instance = new AssistantToTheRegionalManager();
		}
		return instance;
	}
	
	private AssistantToTheRegionalManager(){
		SUM = new StorageUnitsManager();
		PM = new ProductsManager();
		IM = new ItemsManager();
		RIM = new RemovedItemsManager();
	}
	
	/**
	 * sets this instance to the one given
	 *  used for serialization purposes
	 * @param instance	the new instance of this class
	 */
	public void setInstance(AssistantToTheRegionalManager instance){
		this.instance = instance;
	}

	/**
	 * Gets the StorageUnitsManager
	 * 
	 * @return	the one and only instance of StorageUnitsManager
	 */
	public StorageUnitsManager getSUM() {
		return SUM;
	}

	/**
	 * Gets the ItemsManager
	 * 
	 * @return	the one and only instance of ItemsManager
	 */
	public ItemsManager getIM() {
		return IM;
	}

	/**
	 * Gets the ProductsManager
	 * 
	 * @return	the one and only instance of ProductsManager
	 */
	public ProductsManager getPM() {
		return PM;
	}

	/**
	 * Gets the RemovedItemsManager
	 * 
	 * @return	the one and only instance of RemovedItemsManager
	 */
	public RemovedItemsManager getRIM() {
		return RIM;
	}

    public void reset() {
        SUM = new StorageUnitsManager();
        PM = new ProductsManager();
        IM = new ItemsManager();
        RIM = new RemovedItemsManager();
    }
}
