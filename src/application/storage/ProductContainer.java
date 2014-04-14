/**
 * ProductContainer.java
 *
 * @author Stephen Kitto
 * @author Trevor Burbidge
 * @author Christina Ramos
 * @author Leckie Gunter
 *
 * Abstract class that will be used in a composite structure to store items of various products will
 * be used to represent either Storage units or product groups.
 *
 */
package application.storage;

import application.reports.visitors.Visitor;
import common.exceptions.InvalidContainerException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * An Abstract class that is used to represent any object that can hold items or sub-containers 
 * @author Stephen Kitto
 */
public abstract class ProductContainer implements Serializable, Comparable
{

	protected String name;
	protected Set<ProductContainer> children;
	protected ProductContainer parent;
	protected UUID productContainerID;
	protected int ID;

	/**
	 * Constructor: creates a new Product Container
	 *
	 * @param _name		 Name of the new ProductContainer
	 * @param _parent	ProductContainer that holds this ProductContainer
	 */
	public ProductContainer(String _name, ProductContainer _parent)
	{
		name = _name;
		parent = _parent;
		children = new HashSet<ProductContainer>();
		productContainerID = UUID.randomUUID();
	}

	/**
	 * constructor: creates a new productContainer that will be empty except for the UUID to be used
	 * to find the real productContainer
	 *
	 * @param productContainerID the id of the product
	 */
	public ProductContainer(UUID productContainerID)
	{
		assert (productContainerID != null);
		this.productContainerID = productContainerID;
	}

	/**
	 * gets the id of the productContainer
	 *
	 * @return the id of the container
	 */
	public UUID getProductContainerID()
	{
		//there are no pre-conditions for this method
		return productContainerID;
	}

	/**
	 * Gets the name of the container
	 *
	 * @return name of the container
	 */
	public String getName()
	{
		//there are no pre-conditions for this method
		return name;
	}

	/**
	 * changes the name of the container
	 *
	 * @param name the new name of the container
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * gets the parent container
	 *
	 * @return parent of this container
	 */
	public ProductContainer getParent()
	{
		//there are no pre-conditions for this method
		return parent;
	}

	/**
	 * changes the parent of the container
	 *
	 * @param parent new parent of the container
	 * @throws InvalidContainerException
	 */
	protected void setParent(ProductContainer parent) throws InvalidContainerException
	{
		assert (parent != null);

		if (parent == null)
		{
			throw new InvalidContainerException();
		}

		this.parent = parent;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	/**
	 * returns the number of subcontainers this container has
	 *
	 * @return number of subcontainers
	 */
	public int getChildCount()
	{
		//there are no pre-conditions for this method
		return children.size();
	}

	/**
	 * Recursive Method that finds all the other ProductContainers that are contained within this
	 * container
	 *
	 * @return	a set of all the ProductContainers within this one
	 */
	protected Iterator<ProductContainer> getSubContainers()
	{
		//there are no pre-conditions for this method
		return children.iterator();
	}

	/**
	 * Add a child container to this container.
	 *
	 * @param container ProductContainer to add to children of this container
	 */
	protected void addSubContainer(ProductContainer container)
	{
		assert (container != null);
		children.add(container);
	}

	/**
	 * Removes the given container from the children of the container
	 *
	 * @param container the container to be removed
	 */
	protected void removeSubContainer(ProductContainer container)
	{
		assert (container != null);
		children.remove(container);
	}

	/**
	 * Finds the container the matches the Data
	 *
	 * @param container the container we are looking for
	 *
	 * @return the actual container if it is found, null if not
	 */
	protected ProductContainer findContainer(ProductContainer container)
	{
		assert (container != null);

		if (this.equals(container))
		{
			return this;
		}
		//this is not the container you are looking for
		//look among its children

		for (ProductContainer child : children)
		{
			ProductContainer temp = child.findContainer(container);

			//one of his children is or contains the container that is being looked for
			if (temp != null)
			{
				return temp;
			}
		}

		//none of the children are either
		return null;
	}

	/**
	 * checks to see if two productContainers are the same container
	 *
	 * @param other the container that this one should be compared to
	 *
	 * @return true if they are the same container, false if not
	 */
	public boolean equals(ProductContainer other)
	{
		assert (other != null);
		
		if (!this.name.equals(other.name)){
			return false;
		}
		else if(this.getChildCount() != other.getChildCount()){
			return false;
		}
		
		if(this.getParent() == null){
			if(other.getParent() != null){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			if(other.getParent() == null){
				return false;
			}
		}
		
		if(!this.getParent().equals(other.getParent())){
			return false;
		}
		
		return true;
	}

	public int compareTo(Object other){
		return name.compareTo(((ProductContainer)other).name);
	}
	
	public void acceptPre(Visitor visitor){
		Iterator<ProductContainer> subContainers = sortContainers(children.iterator());
		while(subContainers.hasNext()){
			subContainers.next().acceptPre(visitor);
		}
	}
	
	public void acceptPost(Visitor visitor){
		Iterator<ProductContainer> subContainers = sortContainers(children.iterator());
		List<ProductContainer> sContainers = new ArrayList<ProductContainer>();
		while(subContainers.hasNext()){
			sContainers.add(subContainers.next());
		}
		for(int i = sContainers.size() - 1; i >= 0; i--){
			sContainers.get(i).acceptPost(visitor);
		}
	}
	
	/**
	 * finds the container with the id passed in, checks their children if it is not this one
	 * @param id
	 * @return 
	 */
	public ProductContainer findContainer(int id){
		if (this.ID == id){
			return this;
		}
		
		Iterator<ProductContainer> subContainers = children.iterator();
		while(subContainers.hasNext()){
			ProductContainer result = subContainers.next().findContainer(id);
			if(result != null){
				return result;
			}
		}
		
		return null;
	}
	
	private Iterator<ProductContainer> sortContainers(Iterator<ProductContainer> containers){
		List<ProductContainer> cont = new ArrayList<ProductContainer>();
		while(containers.hasNext()){
			cont.add(containers.next());
		}
		Collections.sort(cont);
		return cont.iterator();
	}
}
