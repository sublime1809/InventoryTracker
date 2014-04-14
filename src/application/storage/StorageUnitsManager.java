/*
 * This is a Singleton class used to keep track of all the ProductContainers
 * in the system as well as all the products and items they hold
 */
package application.storage;

import application.AssistantToTheRegionalManager;
import application.item.Item;
import application.product.Product;
import application.reports.visitors.Visitor;
import common.DTOConverter;
import common.Mapping;
import common.Size;
import common.SizeUnit;
import common.exceptions.CannotBeRemovedException;
import common.exceptions.InvalidContainerException;
import dataPersistence.IContainerDAO;
import dataPersistence.MappingDTO;
import dataPersistence.PersistenceManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Trevor
 * @author Stephen Kitto
 */
public class StorageUnitsManager implements Serializable
{

	private Map<ProductContainer, Set<Item>> itemsInContainers;
	private Map<ProductContainer, Set<Product>> productsInContainers;
	private HomeStorage storageUnits;
	//These elements are for use as a singleton class
	//this is the instance of this class
	private static StorageUnitsManager INSTANCE;
	private PersistenceManager persistenceManager;

	/**
	 * Gets the one and only instance of this class
	 *
	 * @return the single instance of this class
	 */
	public static StorageUnitsManager getInstance()
	{
//		//no pre-condition for this method
//		if (INSTANCE == null)
//		{
//			INSTANCE = AssistantToTheRegionalManager.getInstance().getSUM();
//		}
//
//		return INSTANCE;
		return AssistantToTheRegionalManager.getInstance().getSUM();
	}

	/**
	 * Constructor: create a new storageUnitsManager
	 */
	public StorageUnitsManager()
	{
		try
		{
			itemsInContainers = new HashMap();
			productsInContainers = new HashMap();
			storageUnits = new HomeStorage();
			persistenceManager = PersistenceManager.getInstance();
		}
		catch (InvalidContainerException ex)
		{
		}
	}

	/**
	 * used to reset the manager mostly just for testing purposes
	 */
	public void reset()
	{
		//no pre-condition for this method
		INSTANCE = new StorageUnitsManager();
	}

	/**
	 * Checks to see if the given container can change its name to the given name
	 *
	 * @param container	the container that might change its name
	 * @return	true if the string given is a valid name for this container
	 */
	public boolean canChangeName(ProductContainer container, String newName)
	{
		assert (container != null);
		if (container.getName().length() == 0)
		{
			return false;
		}
		
		Iterator<ProductContainer> potentialSiblings = container.getParent().getSubContainers();
		while (potentialSiblings.hasNext())
		{
			ProductContainer cur = potentialSiblings.next();
			if (!container.getName().equals(newName) && cur.getName().equals(newName))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks to see if a container can be added to another container
	 *
	 * @param newContainer    the container that we need to know if it can be added
	 * @param parentContainer where the new container would be added too
	 *
	 * @return true if the first container can be added into the second
	 */
	public boolean canBeAdded(ProductContainer newContainer, ProductContainer parentContainer)
	{
		assert (newContainer != null);
		if (newContainer.getName().equals(""))
		{
			return false;
		}

		if (parentContainer == null)
		{
			if(newContainer instanceof ProductGroup){
				return false;
			}
			parentContainer = storageUnits;
		}

		Iterator<ProductContainer> potentialSiblings = parentContainer.getSubContainers();
		while (potentialSiblings.hasNext())
		{
			ProductContainer cur = potentialSiblings.next();
			if (cur.getName().equals(newContainer.getName()))
			{
				return false;
			}
		}

		//none of the potential siblings have the same name as the potential new comer
		return true;
	}

	/**
	 * Adds a new ProductContainer to the manager
	 *
	 * @param newContainer		the new container that will be added to the system
	 * @param parentContainer	the parent of the new container, null if it is a storage unit
	 * @throws InvalidContainerException
	 */
	public void addProductContainer(ProductContainer newContainer, ProductContainer parentContainer)
			throws InvalidContainerException
	{
		assert (newContainer != null);
		assert (canBeAdded(newContainer, parentContainer));

		if (!canBeAdded(newContainer, parentContainer))
		{
			throw new InvalidContainerException();
		}

		if (parentContainer == null)
		{
			newContainer.setParent(storageUnits);
			storageUnits.addSubContainer(newContainer);
		}
		else
		{
			newContainer.setParent(parentContainer);
			parentContainer.addSubContainer(newContainer);
		}
		Set<Product> products = new HashSet();
		Set<Item> items = new HashSet();
		productsInContainers.put(newContainer, products);
		itemsInContainers.put(newContainer, items);
	}

	/**
	 * Checks to see if the given ProductContainer can be removed
	 *
	 * @param container the ProductContainer that we desire to remove
	 *
	 * @return true if the container can be removed, false if not
	 */
	public boolean canBeRemoved(ProductContainer container)
	{
		assert (container != null);

		if (container == null)
		{
			return false;
		}

		// if this contianer has items, it can't be removed
		if (itemsInContainers.get(container).size() > 0)
		{
			return false;
		}
		
		// check all of it's children, if none of them have items, it can be removed
		Iterator<ProductContainer> children = this.getSubContainers(container);
		boolean result = true;
		while(children.hasNext()){
			//if current child cannot be removed, this container cannot be removed
			if(!canBeRemoved(children.next())){
				result = false;
			}
		}

		return result;
	}

	/**
	 * removes the given container from the tree of containers
	 *
	 * @param container the container to be removed
	 * @throws CannotBeRemovedException
	 */
	public void removeContainer(ProductContainer container)
			throws CannotBeRemovedException
	{
		assert (container != null);
		assert (container.getParent() != null);
		assert (canBeRemoved(container));

		if (!canBeRemoved(container))
		{
			throw new CannotBeRemovedException();
		}
		
		//remove all of it's subcontainers first
		Iterator<ProductContainer> children = getSubContainers(container);
		Set<ProductContainer> toBeRemoved = new TreeSet();
		while(children.hasNext()){
			toBeRemoved.add(children.next());
		}
		children = toBeRemoved.iterator();
		while(children.hasNext()){
			removeContainer(children.next());
		}

		ProductContainer parent = container.getParent();
		itemsInContainers.remove(container);
		productsInContainers.remove(container);
		parent.removeSubContainer(container);
	}

	/**
	 * Gets the subcontainers of the given container
	 *
	 * @param container the container that we want to get the children of null if we want to get all
	 *                     the storageUnits
	 *
	 * @return the children of the given container
	 */
	public Iterator<ProductContainer> getSubContainers(ProductContainer container)
	{
		//no pre-condition for this method
		if (container == null)
		{
			return storageUnits.getSubContainers();
		}
//		container = update(container);
		return container.getSubContainers();
	}

	/**
	 * gets the storageUnit of the given container
	 *
	 * @param container	the container whose storage unit we want to find
	 *
	 * @return	the storage unit the holds the container
	 */
	public ProductContainer getStorageUnit(ProductContainer container)
	{
		assert (container != null);
		if (container instanceof StorageUnit)
		{
			return container;
		}

		ProductContainer cur = container.getParent();
		while (cur != null)
		{
			if (cur instanceof StorageUnit)
			{
				return cur;
			}
			cur = cur.getParent();
		}
		assert (false);
		return null;
	}

	/**
	 * Looks for the given product within the entire ProductContainer
	 *
	 * @param product		 the product that is being looked for
	 * @param container the container that might have the product
	 *
	 * @return	true if the product container holds an item of this product or one of its children
	 *            does
	 */
	public boolean hasProduct(ProductContainer container, Product product)
	{
		assert (container != null);
		assert (product != null);

		Iterator<Product> products = productsInContainers.get(container).iterator();

		while (products.hasNext())
		{
			Product cur = products.next();
			if (cur.equals(product))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds a product to the ProductContainer
	 *
	 * @param container container the product should be placed in
	 * @param product		 the product to be added to the container
	 */
	public boolean addProduct(ProductContainer container, Product product)
	{
		boolean result = false;
		assert (container != null);
		assert (product != null);

		Iterator<Product> products = productsInContainers.get(container).iterator();
		while(products.hasNext()){
			if(products.next().equals(product)){
				return true;
			}
		}
		
		Mapping mapping = new Mapping(container, product);
		MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(mapping);
		IContainerDAO containerDAO = this.persistenceManager.getContainerDAO();
		result = containerDAO.createContainerProductMapping(mappingDTO);
		
		if(result)
		{
			productsInContainers.get(container).add(product);
                        
		}
		
		return result;
	}
	
	/**
	 * adds a product to memory without doing data persistence
	 * @param container
	 * @param product
	 * @return 
	 */
	public boolean loadProduct(ProductContainer container, Product product)
	{
		boolean result = false;
		assert (container != null);
		assert (product != null);

		Iterator<Product> products = productsInContainers.get(container).iterator();
		while(products.hasNext()){
			if(products.next().equals(product)){
				return true;
			}
		}
		
		productsInContainers.get(container).add(product);
		
		return result;
	}

	/**
	 * checks to see if the product can in fact be removed from the container
	 *
	 * @param container	the container that holds the product
	 * @param product		 the product that wants to be removed
	 *
	 * @return	true if the product can be removed
	 */
	public boolean canRemoveProduct(ProductContainer container, Product product)
	{
		assert (container != null);
		assert (product != null);
		assert (product.getBarcode() != null);

		Iterator<Item> items = itemsInContainers.get(container).iterator();

		while (items.hasNext())
		{
			Item cur = items.next();
			if (cur.getProduct().equals(product))
			{
				return false;
			}
		}

		//gets through all the items and none were the same product
		return true;
	}

	/**
	 * Removes a product to the ProductContainer
	 *
	 * @param container container the product should be removed From pass in null if you want to
	 *                     remove the product from all containers
	 * @param product		 the product to be removed from the container
	 * @throws CannotBeRemovedException
	 */
	public boolean removeProduct(ProductContainer container, Product product)
			throws CannotBeRemovedException
	{
		assert (product != null);
		removeProductEntirely(container, product);

		if (container == null)
		{
			Iterator<ProductContainer> containers = productsInContainers.keySet().iterator();
			while (containers.hasNext())
			{
				ProductContainer cur = containers.next();
				assert (canRemoveProduct(cur, product));
				if (productsInContainers.get(cur).contains(product))
				{
					productsInContainers.get(cur).remove(product);
				}
			}
			return true;
		}

		assert (canRemoveProduct(container, product));
		if (!canRemoveProduct(container, product))
		{
			throw new CannotBeRemovedException();
		}
		productsInContainers.get(container).remove(product);
                return true;
	}

	/**
	 * returns all the products that are contained in the given container
	 *
	 * @param container the container that we want to know about
	 *
	 * @return the products within the given container
	 */
	public Iterator<Product> getProducts(ProductContainer container)
	{
		assert (container != null);
		
		ArrayList<Product> products = new ArrayList<Product>(productsInContainers.get(container));
		Collections.sort(products);
		
		return products.iterator();
	}

	/**
	 * gets the container within the given StorageUnit that contains the given product
	 * @param container		the storage unit that we are looking in
	 * @param product		the product that is being looked for
	 * @return				the container in the storageunit that contains the given product
	 */
	public ProductContainer getContainerThatHasProduct(ProductContainer container, Product product)
	{
		assert (container != null);
		assert (product != null);

		if (hasProduct(container, product))
		{
			return container;
		}

		Iterator<ProductContainer> children = container.getSubContainers();

		while (children.hasNext())
		{
			ProductContainer curChild = children.next();
			ProductContainer temp = getContainerThatHasProduct(curChild, product);
			if (temp != null)
			{
				return temp;
			}
		}

		// the product was not found in this container or any of it's children
		return null;
	}

	/**
	 * Adds an item to the ProductContainer. If there is already another item of the same product in
	 * the container it will find the subcontainer that the other item is in and put it in the same
	 * subcontainer. Otherwise it will just add it to the root container.
	 *
	 * @param container container the item should be placed in
	 * @param item		    item to be added to the ProductContainer
	 */
	public void addItem(ProductContainer container, Item item)
	{
		assert (container != null);
		assert (item != null);

		itemsInContainers.get(container).add(item);
	}

	/**
	 * removes the given item from the ProductContainer
	 *
	 * @param container container the item should be removed From
	 * @param item			   the item to be removed from the container
	 */
	public void removeItem(ProductContainer container, Item item)
	{
		assert (container != null);
		assert (item != null);
		
		Set<Item> items = itemsInContainers.get(container);
		items.remove(item);
		//itemsInContainers.get(container).remove(item);
	}

	/**
	 * returns all the items that are contained in the given container
	 *
	 * @param container the container that we want to know about
	 *
	 * @return the items within the given container
	 */
	private Iterator<Item> getItems(ProductContainer container)
	{
		assert (container != null);

		return itemsInContainers.get(container).iterator();
	}

	/**
	 * Gets all this items in the given container of the given product
	 * @param container		the container we are looking in
	 * @param product		the product we are looking for
	 * @return				an iterator of all the items of the given product in the given container
	 */
	public Iterator<Item> getItemsOfProduct(ProductContainer container, Product product)
	{
		assert (container != null);
		assert (product != null);

		Set<Item> itemsOfProduct = new HashSet();
		Iterator<Item> allItems = getItems(container);

		while (allItems.hasNext())
		{
			Item cur = allItems.next();
			if (cur.getProduct().equals(product))
			{
				itemsOfProduct.add(cur);
			}
		}

		return itemsOfProduct.iterator();
	}
	
	public Iterator<Item> getAllItemsInContainers(ProductContainer container){
		return getItems(container);
	}

	/**
	 * gets the three month supply of a container
	 *
	 * @param container	the container that we want the three month supply of
	 * @return			the three month supply of the given container 
	 */
	public Size getThreeMonthSupply(ProductContainer container)
	{
		assert (container != null);
		assert (container instanceof ProductGroup);
		
		return ((ProductGroup) container).getThreeMonthSupply();
	}
	
	/**
	 * checks to see if the given container is a storageUnit
	 * @param container		the container to be checked
	 * @return				true if it is a storageunit, false if not
	 */
	public boolean isStorageUnit(ProductContainer container){
            if(container.getName().equals(HomeStorage.ROOT_NAME)) {
                return false;
            }
            return container.getParent().getName().equals(HomeStorage.ROOT_NAME);
	}
	
	/**
	 * Gets the root of the tree of containers
	 * @return		the root of all the containers
	 */
	public ProductContainer getRoot(){
		return storageUnits;
	}
	
	/**
	 * counts up the items of the given product in the given container
	 * @param container		the container to look in
	 * @param product		the product of items we want
	 * @return				the number of items in the container
	 */
	public int getItemCountInContainer(ProductContainer container, Product product){
		int itemCount = 0;
		Iterator<Item> items = getItemsOfProduct(container, product);
		
		while(items.hasNext()){
			items.next();
			itemCount++;
		}
		
		return itemCount;
	}

	/**
	 * checks to see if the given container can be edited
	 * @param container		the container we may wish to edit
	 * @return				true if it can be edited
	 */
	public boolean canEditProductContainer(ProductContainer container, String newName)
	{
		if(canChangeName(container, newName))
		{
			if(isStorageUnit(container))
			{
				return true;
			}
			else
			{
				Size supply = ((ProductGroup)container).getThreeMonthSupply();
				if(supply.getUnit() == SizeUnit.Count)
				{
					return supply.getSize() >= 0 && supply.getSize() % 1 == 0;
				}
				else
				{
					return supply.getSize() >= 0;
				}
			}
		}
		return false;
	}
	
	public void visitContainersPreOrder(Visitor visitor){
		storageUnits.acceptPre(visitor);
	}
	
	public void visitContainersPostOrder(Visitor visitor){
		storageUnits.acceptPost(visitor);
	}
	
	public ProductContainer findContainer(int id){
		return this.storageUnits.findContainer(id);
	}

	private boolean removeProductEntirely(ProductContainer container, Product product) {
		boolean result = false;
		if(container == null){
			Iterator<ProductContainer> storageUnits = this.getSubContainers(null);
			while(storageUnits.hasNext()){
				Mapping mapping = new Mapping(storageUnits.next(), product);
				MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(mapping);
				IContainerDAO containerDAO = persistenceManager.getContainerDAO();
				result = containerDAO.deleteContainerProductMapping(mappingDTO);
			}
		}
		else{
				Mapping mapping = new Mapping(container, product);
				MappingDTO mappingDTO = DTOConverter.mappingToMappingDTO(mapping);
				IContainerDAO containerDAO = persistenceManager.getContainerDAO();
				result = containerDAO.deleteContainerProductMapping(mappingDTO);		
		}
		return result;
	}

//	public ProductContainer getContainerThatHasItem(Item item)
//	{
//		this.
//	}

	/**
	 * changes the three month supply of the given container
	 *
	 * @param container	the container that needs a new supply
	 * @param size			   the new supply
	 */
//	public void setThreeMonthSupply(ProductContainer container, Size size)
//	{
//		assert (container != null);
//		assert (size != null);
//		assert (container instanceof ProductGroup);
//
//		((ProductGroup) container).setThreeMonthSupply(size);
//	}
}
