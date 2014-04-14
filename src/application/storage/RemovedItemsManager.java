/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.storage;

import application.AssistantToTheRegionalManager;
import application.item.Item;
import application.product.Product;
import application.product.ProductsManager;
import common.DTOConverter;
import common.exceptions.*;
import dataPersistence.IItemDAO;
import dataPersistence.ItemDTO;
import dataPersistence.PersistenceManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Trevor
 */
public class RemovedItemsManager implements Serializable
{

	private static RemovedItemsManager INSTANCE = null;
	private Date lastReportRun;
	private PersistenceManager persistenceManager = PersistenceManager.getInstance();

	/**
	 * gets the instance of this singleton class
	 * @return	the one and only instance of this class
	 */
	public static RemovedItemsManager getInstance()
	{		
//		if (INSTANCE == null)
//		{
//			INSTANCE = AssistantToTheRegionalManager.getInstance().getRIM();
//		}
//		
//		return INSTANCE;
		return AssistantToTheRegionalManager.getInstance().getRIM();
	}
	private Map<Product, List<Item>> productToItems;

	public RemovedItemsManager()
	{
		productToItems = new HashMap();
		lastReportRun = new Date();
	}

	public Date getLastReportRun() {
		return lastReportRun;
	}

	public void runReport() {
		lastReportRun = new Date();
		persistenceManager.startTransaction();
		persistenceManager.getItemDAO().updateRemovedReportDate(lastReportRun);
		persistenceManager.finishTransaction(true);
	}

	public void setLastReportRun(Date lastReportRun) {
		this.lastReportRun = lastReportRun;
	}

	/**
	 * Checks to see if the given item can be added to the system
	 * @param item		the item to be checked
	 * @return			true if the item can be added to the system
	 */
	public boolean canAddItem(Item item)
	{
		assert (item != null);
		return true;
	}

	/**
	 * This will add the item to the removedItem and set the ExitDate of the item
	 *
	 * @param item Item to be added
	 *
	 * @return
	 *
	 * @throws InvalidAddException
	 */
	public boolean addItem(Item item) throws InvalidAddException
	{
		assert (item != null);
		assert (canAddItem(item));
		Product product = item.getProduct();
		assert (product != null);

		item.setExitTime(new Date());
                
		if (productToItems.containsKey(product))
		{
			return productToItems.get(product).add(item);
		}
		else
		{
			List<Item> tempItems = new ArrayList<Item>();
			tempItems.add(item);

			productToItems.put(product, tempItems);
			return true;
		}
	}

	/**
	 * removes the given product from the removedItemsHistory
	 * @param product	the product to be removed
	 * @return			true if it worked
	 */
	public boolean removeProduct(Product product)
	{
		assert (product != null);
		assert (ProductsManager.getInstance().hasItems(product) == false);
		if (ProductsManager.getInstance().hasItems(product))
		{
			return false;
		}
		else
		{
                    IItemDAO itemDAO = PersistenceManager.getInstance().getItemDAO();
                    List<Item> itemsToRemove = productToItems.get(product);
                    for(Item i : itemsToRemove){
                        ItemDTO itemDTO = DTOConverter.itemToItemDTO(i);
                        itemDAO.removeItem(itemDTO);
                    }
                    productToItems.remove(product);
                    return true;
		}
	}

	/**
	 * This will give you an iterator for all the removed products
	 *
	 * @return Removed products
	 */
	public Iterator<Product> getProductIterator()
	{
		return productToItems.keySet().iterator();
	}

	/**
	 * This will return the removed items that are associated with a product
	 *
	 * @param product The product to search for the items
	 *
	 * @return An iterator for the items associated with the given product
	 */
	public Iterator<Item> getItemIterator(Product product)
	{
            if(productToItems.containsKey(product)) {
		return productToItems.get(product).iterator();
            } else {
                return null;
            }
	}

	/**
	 * Checks to see if the given item is in the removedItemsHistory
	 * @param item	the item that is being looked for
	 * @return		true if it is in the history
	 */
	public boolean containsItem(Item item)
	{
		assert (item != null);
		for (Product product : productToItems.keySet())
		{
			List<Item> items = productToItems.get(product);
			if (items.contains(item))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * clears the entire removedItemsHistory
	 */
	public void clearAll()
	{
		this.productToItems.clear();
	}
}
