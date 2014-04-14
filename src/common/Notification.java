/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import application.item.Item;
import application.product.Product;
import application.storage.ProductContainer;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Stephen Kitto
 */
public class Notification {
	
	private NotificationType type;
	private Set<Item> itemsInvolved;
	private Product productInvolved;
	private ProductContainer containerInvolved;
	
	/**
	 * constructor 
	 * @param t		the type of notification that it will be
	 */
	public Notification(NotificationType t){
		type = t;
		itemsInvolved = null;
		productInvolved = null;
		containerInvolved = null;
	}

	/**
	 * gets the type of notification
	 * @return		the notificationType
	 */
	public NotificationType getType() {
		return type;
	}

	/**
	 * checks to see if any items were involved with this notification
	 * @return		true if items were involved, false if not
	 */
	public boolean areItemsInvolved(){
		return (type == NotificationType.ItemsAdded || 
				type == NotificationType.ItemsRemoved || 
				type == NotificationType.ItemsChanged);
	}
	
	/**
	 * gets all the items that were associated with this notification
	 * @return		an iterator of all the items associated with this notification
	 *					null if none were involved
	 */
	public Iterator<Item> getItemsInvolved() {
		return itemsInvolved.iterator();
	}

	/**
	 * sets the items involved
	 * @param itemsInvolved		a set of items
	 */
	public void setItemsInvolved(Set<Item> itemsInvolved) {
		this.itemsInvolved = itemsInvolved;
	}

	/**
	 * checks to see if a product was involved with this notification
	 * @return		true if a product was involved, false if not
	 */
	public boolean isProductInvolved(){
		return (type == NotificationType.ProductAdded ||
				type == NotificationType.ProductChanged ||
				type == NotificationType.ProductMoved ||
				type == NotificationType.ProductRemoved);
	}
	
	/**
	 * returns the product involved
	 * @return		the product involved
	 */
	public Product getProductInvolved() {
		return productInvolved;
	}

	/**
	 * sets the product involved
	 * @param productInvolved	the new product involved
	 */
	public void setProductInvolved(Product productInvolved) {
		this.productInvolved = productInvolved;
	}

	/**
	 * checks to see if a container was involved with this notification
	 * @return		true if a container was involved, false if not
	 */
	public boolean isContainerInvolved(){
		return (type == NotificationType.ContainerAdded ||
				type == NotificationType.ContainerRemoved ||
				type == NotificationType.ContainerChanged);
	}

	/**
	 * gets the container involved
	 * @return		the container involved
	 */
	public ProductContainer getContainerInvolved() {
		return containerInvolved;
	}

	/**
	 * sets the container involved
	 * @param containerInvolved		new container involved
	 */
	public void setContainerInvolved(ProductContainer containerInvolved) {
		this.containerInvolved = containerInvolved;
	}
	
}
