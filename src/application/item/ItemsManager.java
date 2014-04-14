package application.item;

import application.AssistantToTheRegionalManager;
import application.printing.BarcodeGenerator;
import application.product.Product;
import application.storage.ProductContainer;
import com.itextpdf.text.DocumentException;
import common.Barcode;
import common.DTOConverter;
import common.exceptions.InsufficientBarcodesException;
import dataPersistence.IItemDAO;
import dataPersistence.ItemDTO;
import dataPersistence.PersistenceManager;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A class to manage all actions that can be done to items.
 *
 * @author Trevor
 */
public class ItemsManager implements Serializable
{
	private PersistenceManager persistenceManager;
	private static ItemsManager INSTANCE;
	private Map<Barcode, Item> barcodeToItemMap;
	private HashSet<Barcode> usedBarcodes;

	public ItemsManager()
	{
		barcodeToItemMap = new HashMap();
		usedBarcodes = new HashSet();
		persistenceManager = PersistenceManager.getInstance();
	}
	
	/**
	 * gets the instance of this singleton class
	 * @return		the one and only instance of this class
	 */
	public static ItemsManager getInstance()
	{		
//            if (INSTANCE == null)
//            {
//                INSTANCE = AssistantToTheRegionalManager.getInstance().getIM();
//            }
//
//            return INSTANCE;
		return AssistantToTheRegionalManager.getInstance().getIM();
	}

	/**
	 * used to reset the manager mostly just for testing purposes
	 */
	public void reset()
	{
		//no pre-condition for this method
		INSTANCE = new ItemsManager();
	}
	
	/**
	 * Adds a new item to the system and gives it a barcode
	 *
	 * @param item the new item to be added
	 *
	 * @throws InsufficientBarcodesException
	 */
	public boolean add(Item item) throws InsufficientBarcodesException
	{
		assert (item != null);
		assert (canAdd(item));
		
		if (item == null)
		{
			throw new IllegalArgumentException();
		}
		
		if(item.getBarcode() == null)
		{
			Barcode barcode = Barcode.generate(usedBarcodes);
			item.setBarcode(barcode);
		}
		
		boolean result = false;
		ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
		IItemDAO itemDAO = persistenceManager.getItemDAO();
		if (item.getID() > 0)
		{
			result = itemDAO.updateItem(itemDTO);
		}
		else
		{
			result = itemDAO.createItem(itemDTO);
			item.setID(itemDTO.getID());
		}
		if(result)
		{
			barcodeToItemMap.put(item.getBarcode(), item);
			usedBarcodes.add(item.getBarcode());
			if(item.getProduct().getDate() == null || 
					item.getEntryDate().before(item.getProduct().getDate()))
			{
				item.getProduct().setDate(item.getEntryDate());
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * loads an item into memory without doing persistence
	 * @param item
	 * @throws InsufficientBarcodesException 
	 */
	public void load(Item item) throws InsufficientBarcodesException
	{
		assert (item != null);
//		assert (canAdd(item));
		
		if (item == null)
		{
			throw new IllegalArgumentException();
		}
		
		if(item.getBarcode() == null)
		{
			Barcode barcode = Barcode.generate(usedBarcodes);
			item.setBarcode(barcode);
		}
		
		barcodeToItemMap.put(item.getBarcode(), item);
		usedBarcodes.add(item.getBarcode());
	}

	/**
	 * Checks whether an item has valid values.
	 *
	 * @param item
	 *
	 * @return true if the item is valid and can be added to the system. False otherwise
	 */
	public boolean canAdd(Item item)
	{
		assert (item != null);
		
		Date entryDate = item.getEntryDate();
		Date exitDate = item.getExitTime();
		GregorianCalendar now = new GregorianCalendar();
		Date beginningOfTime = new GregorianCalendar(2000, 0, 1).getTime();
		
		if (item == null)
		{
			return false;
		}
		
		if (item.getProduct() == null
				|| entryDate == null
				|| entryDate.before(beginningOfTime)
				|| entryDate.after(now.getTime())
				|| exitDate != null
				|| item.getProductContainer() == null)
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Removes the item with this itemID from wherever it is and returns the item. Returns null if
	 * the item does not exist.
	 *
	 * @param item item to be removed
	 *
	 * @return Item the item that was removed or null if the item was not found
	 */
	public Item remove(Item item)
	{
		assert (item != null);
		
		if (item == null)
		{
			throw new IllegalArgumentException();
		}
		
		Item realItem = barcodeToItemMap.remove(item.getBarcode());
		
		realItem.setExitTime(new Date());
		//realItem.setProductGroup(null);
		//realItem.setStorageUnit(null);
		
		return realItem;
	}
        
       /**
	 * Gets and item based on the item barcode.
	 *
	 * @param barcode
	 *
	 * @return the item for this item barcode.
	 */
	public Item getItem(Barcode barcode)
	{
		return barcodeToItemMap.get(barcode);
	}

	/**
	 * Gets the product of the item where item.equals(otherItem)
	 *
	 * @param item
	 *
	 * @return an item's product
	 *
	 */
	public Product getProduct(Item item)
	{
		assert (item != null);
		
		if (item == null)
		{
			throw new NullPointerException();
		}
		
		Item realItem = barcodeToItemMap.get(item.getBarcode());
		if (realItem != null)
		{
			return realItem.getProduct();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Get the container that directly holds the given item
	 * @param item		the item in question
	 * @return			the container that holds it
	 */
	public ProductContainer getDirectContainer(Item item)
	{
		return item.getProductContainer();
	}

	/**
	 * Changes the container that is holding the given item
	 * @param item			the item that needs to have un updated container
	 * @param container		the container that is now holding the item
	 * @return				true if the change was successful
	 */
	public boolean updateDirectContainer(Item item, ProductContainer container) {
            ItemDTO itemDTO = DTOConverter.itemToItemDTO(item);
            IItemDAO itemDAO = persistenceManager.getItemDAO();
            itemDTO.setContainerID(container.getID());
            if(itemDAO.updateItem(itemDTO)) {
		item.setProductContainer(container);
		return true; 
            } else {
                return false;
            }
	}

	/**
	 * Prints barcodes of the given items
	 * 
	 * @param items		the items that need their barcode printed
	 * @return			filename that holds the printed barcodes
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public String printBarcodes(List<Item> items) 
			throws DocumentException, FileNotFoundException {
		BarcodeGenerator gen = new BarcodeGenerator(items);
		return gen.getLocation();
	}

	public void deleteItems(Collection<Item> items)
	{
		for(Item item : items)
		{
			if(barcodeToItemMap.containsKey(item.getBarcode()))
			{
				barcodeToItemMap.remove(item.getBarcode());
				usedBarcodes.remove(item.getBarcode());
			}
		}
	}

	/**
	 * Edits an existing item. The only editable field of an item is the entry date. The barcode of
	 * the item must be defined.
	 *
	 * @param item the item with the
	 *
	 * @throws IllegalArgumentException
	 */
//	public boolean updateItemEntryDate(Item item) throws InvalidDateException
//	{
//		assert (item != null);
//		assert (item.getBarcode() != null);
//		assert (item.getEntryDate() != null);
//		
//		if (item == null || item.getBarcode() == null || item.getEntryDate() == null)
//		{
//			throw new IllegalArgumentException();
//		}
//		
//		Item realItem = barcodeToItemMap.get(item.getBarcode());
//		
//		if (realItem == null)
//		{
//			realItem.setEntryDate(item.getEntryDate());
//		}
//		
//		return true;
//	}

	/**
	 * Updates the ProductContainer that is holding this item.
	 *
	 * @param item
	 * @param newContainer
	 *
	 * @return
	 *
	 * @throws InvalidItemException
	 */
//	public boolean updateItemProductGroup(Item item, ProductGroup newGroup) 
//			throws InvalidItemException
//	{
//		assert (item != null);
//		assert (newGroup != null);
//		
//		
//		if (item == null || newGroup == null)
//		{
//			throw new NullPointerException();
//		}
//		
//		item = barcodeToItemMap.get(item.getBarcode());
//		if (item == null)
//		{
//			throw new InvalidItemException();
//		}
//		
//		item.setProductGroup(newGroup);
//		return true;
//	}

	/**
	 * Updates the ProductContainer that is holding this item.
	 *
	 * @param item
	 * @param newContainer
	 *
	 * @return
	 *
	 * @throws InvalidItemException
	 */
//	public boolean updateItemStorageUnit(Item item, StorageUnit newUnit) 
//			throws InvalidItemException
//	{
//		assert (item != null);
//		assert (newUnit != null);
//		
//		
//		if (item == null || newUnit == null)
//		{
//			throw new NullPointerException();
//		}
//		
//		item = barcodeToItemMap.get(item.getBarcode());
//		if (item == null)
//		{
//			throw new InvalidItemException();
//		}
//		
//		item.setStorageUnit(newUnit);
//		return true;
//	}

	/**
	 * Checks whether an item exists
	 *
	 * @param item
	 *
	 * @return
	 */
//	public boolean exists(Item item)
//	{
//		assert (item != null);
//		
//		if (item != null)
//		{
//			return barcodeToItemMap.containsKey(item.getBarcode());
//		}
//		
//		return false;		
//		
//	}

//	public boolean canMove(Item item, ProductContainer newContainer)
//	{
//		assert(item != null);
//		assert(newContainer != null);
//		
//		if(item != null && newContainer != null)
//		{
//			if()
//		}
//		
//		return false;
//	}
	/**
	 * Gets the ProductContainer of the item where item.equals(otherItem)
	 *
	 * @param item the item
	 *
	 * @return the ProductContainer of the item.
	 */
//	public ProductGroup getProductGroup(Item item)
//	{
//		assert (item != null);
//		
//		if (item == null)
//		{
//			throw new NullPointerException();
//		}
//		
//		Item realItem = barcodeToItemMap.get(item.getBarcode());
//		if (realItem != null)
//		{
//			return barcodeToItemMap.get(item.getBarcode()).getProductGroup();
//		}
//		
//		return null;
//	}
	
//	public StorageUnit getStorageUnit(Item item)
//	{
//		assert (item != null);
//		
//		if (item == null)
//		{
//			throw new NullPointerException();
//		}
//		
//		Item realItem = barcodeToItemMap.get(item.getBarcode());
//		if (realItem != null)
//		{
//			return barcodeToItemMap.get(item.getBarcode()).getStorageUnit();
//		}
//		
//		return null;
//	}
	
//	public boolean canRemove(Item item)
//	{
//		assert (item != null);
//		return item.getStorageUnit() != null;
//	}
	
//	public boolean removeFromContainer(Item item)
//	{
//		assert (item != null);
//		
//		item.setProductContainer(null);
//		
//		return true;
//	}
	
//	public Iterator<Item> getAllItems()
//	{
//		return barcodeToItemMap.values().iterator();
//	}

	/**
	 * Gets all of the expired items in the whole system as of right now.
	 *
	 * @return
	 */
//	public Iterator<Item> getExpiredItems()
//	{
//		Date now = new Date();
//		
//		Set<Item> expiredItems = new HashSet();
//		Iterator<Item> allItems = barcodeToItemMap.values().iterator();
//		
//		while (allItems.hasNext())
//		{
//			Item next = allItems.next();
//			if (next.getExpirationDate().compareTo(now) <= 0)
//			{
//				expiredItems.add(next);
//			}
//		}
//		
//		return expiredItems.iterator();
//	}

	/**
	 * Gets the item that is equal to this item, more specifically, gets the item where
	 * item.equals(otherItem). If no match is found null is returned.
	 *
	 * @param item the item to be found
	 *
	 * @return an Item
	 *
	 */
//	public Item getItem(Barcode barcode)
//	{
//		assert (barcode != null);
//		
//		if (barcode == null)
//		{
//			throw new NullPointerException();
//		}
//		
//		Item realItem = barcodeToItemMap.get(barcode);
//		return realItem;
//	}
}
