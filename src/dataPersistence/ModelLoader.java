/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataPersistence;

import application.item.Item;
import application.item.ItemsManager;
import application.product.Product;
import application.product.ProductsManager;
import application.storage.ProductContainer;
import application.storage.RemovedItemsManager;
import application.storage.StorageUnitsManager;
import common.DTOConverter;
import common.Mapping;
import common.exceptions.InsufficientBarcodesException;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidContainerException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stephen Kitto
 */
public class ModelLoader {
	
	StorageUnitsManager storageUnits = StorageUnitsManager.getInstance();
	ProductsManager productsManager = ProductsManager.getInstance();
	ItemsManager itemsManager = ItemsManager.getInstance();
	RemovedItemsManager removedItems = RemovedItemsManager.getInstance();
	
	public ModelLoader(){
		
	}
	
	/**
	 * loads a list of items into the system
	 * @param items 
	 */
	public void loadItems(List<ItemDTO> items){
		//System.out.println("I am going to load items");
		Date oldestItem = new Date();
				
		Iterator<ItemDTO> allItems = items.iterator();
		while(allItems.hasNext()){
			Item cur = DTOConverter.itemDTOToItem(allItems.next());
			//System.out.println(cur.getBarcode().toString());
			try {
				if(cur.getExitTime() == null){
					itemsManager.load(cur);
					productsManager.addProductItemPair(cur.getProduct(), cur);
					storageUnits.addItem(cur.getProductContainer(), cur);
				}
				else{
					removedItems.addItem(cur);
				}
			} catch (InsufficientBarcodesException ex) {
			} catch (InvalidAddException ex){
			}
		
			if (cur.getEntryDate().before(oldestItem)){
				cur.getProduct().setDate(cur.getEntryDate());
			}
			
		}
	}
	
	/**
	 * loads a list of products into the system
	 * @param products 
	 */
	public void loadProducts(List<ProductDTO> products){
		//System.out.println("I am going to load products");
		
		Iterator<ProductDTO> allProducts = products.iterator();
		while(allProducts.hasNext()){
			Product cur = DTOConverter.productDTOToProduct(allProducts.next());
			//System.out.println(cur.getProdDesc());
			
			productsManager.loadProduct(cur, cur.getBarcode());
		}
	}
	
	/**
	 * loads a list of productContainers into the system
	 * @param containers 
	 */
	public void loadContainers(List<ContainerDTO> containers){
		//System.out.println("I am going to load containers");
		
		Iterator<ContainerDTO> allContainers = containers.iterator();
		while(allContainers.hasNext()){
			ProductContainer cur = DTOConverter.containerDTOToContainer(allContainers.next());
			//System.out.println("Adding: " + cur.getName());
			try {
				storageUnits.addProductContainer(cur, cur.getParent());
			} catch (InvalidContainerException ex) {
			}
		}
	}
	
	/**
	 * loads a list of product-productcontainer mappings into the system
	 * @param mappings 
	 */
	public void loadMappings(List<MappingDTO> mappings){
		//System.out.println("I am going to load mappings");
		
		Iterator<MappingDTO> allMappings = mappings.iterator();
		while(allMappings.hasNext()){
			Mapping cur = DTOConverter.mappingDTOToMapping(allMappings.next());
			ProductContainer container = cur.getContainer();
			Product product = cur.getProduct();
			//System.out.println(container + ", " + product);
			
			storageUnits.loadProduct(container, product);
			productsManager.addProductContainerPair(product, container);
		}
	}
	
}
