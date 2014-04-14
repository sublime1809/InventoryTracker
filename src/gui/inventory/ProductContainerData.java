package gui.inventory;

import java.util.*;
import gui.common.*;

/**
 * Display data class for product containers (i.e., storage units
 * and product groups). 
 */
public class ProductContainerData extends Tagable {

	/**
	 * Name property.
	 */
	private String _name;
	
	/**
	 * List of children.
	 */
	private List<ProductContainerData> _children;

	/**
	 * Constructs a new ProductContainerData object.
	 * 
	 * {@pre None}
	 * 
	 * {@post getName() == ""}
	 * {@post getChildCount() == 0}
	 */
	public ProductContainerData() {
		this("");
	}

	/**
	 * Constructs a new ProductContainerData object with the specified name.
	 * 
	 * {@pre name != null && name.length() > 0}
	 * 
	 * {@post getName() == name}
	 * {@post getChildCount() == 0}
	 */
	public ProductContainerData(String name) {
		super();
		
		this._name = name;
		_children = new ArrayList<ProductContainerData>();
	}
	
	/**
	 * Returns the current value of the Name property.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the current value of the Name property.}
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the Name property.
	 * 
	 * @param name New name value
	 * 
	 * {@pre name != null}
	 * 
	 * {@post getName() == name}
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	/**
	 * Returns the number of children.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the number of children.}
	 */
	public int getChildCount() {
		return _children.size();
	}
	
	/**
	 * Returns the child with the specified index.
	 * 
	 * @param index Index of the child to return
	 * 
	 * {@pre 0 <= index < getChildCount()}
	 * 
	 * {@post Returns the child with the specified index.}
	 */
	public ProductContainerData getChild(int index) {
		return _children.get(index);
	}
	
	/**
	 * Appends a new child.
	 * 
	 * @param child New child
	 * 
	 * {@pre child != null}
	 * 
	 * {@post getChildCount() == old(getChildCount()) + 1}
	 * {@post getChild(getChildCount() - 1) == child}
	 */
	public void addChild(ProductContainerData child) {
		_children.add(child);
	}
	
	@Override
	public String toString() {
		return _name;
	}
	
	//
	// FOR INTERNAL USE ONLY - STUDENTS SHOULD NOT NEED TO CALL THESE METHODS
	//

	/**
	 * Inserts a new child at the specified index.
	 * 
	 * @param child New child
	 * @param index Index at which the new child is inserted
	 * 
	 * {@pre child != null}
	 * {@pre 0 <= index <= getChildCount()}
	 * 
	 * {@post getChildCount() == old(getChildCount()) + 1}
	 * {@post getChild(index) == child}
	 * {@post All children at or following the insertion point have been moved
	 *  down one position}
	 */
	protected void insertChild(ProductContainerData child, int index) {
		int i = _children.indexOf(child);
		if (i < 0) {
			_children.add(index, child);
		}
		else {
			// Child was already added
			assert i == index;
		}
	}
	
	/**
	 * Deletes the specified child.
	 * 
	 * @param child Child to be deleted.
	 * 
	 * {@pre child != null}
	 * {@pre child is a valid child}
	 * 
	 * {@post getChildCount() == old(getChildCount()) - 1}
	 * {@post All children at or following the deleted child have been moved
	 *  up one position}
	 */
	protected void deleteChild(ProductContainerData child) {
		_children.remove(child);
	}
	
	/**
	 * Renames the specified child.
	 * <ol>
	 * <li>Renames the specified child.</li>
	 * <li>Deletes the specified child.</li>
	 * <li>Re-inserts the specified child at the specified position.</li>
	 * </ol>
	 * Equivalent to:<br><br>
	 * <code>
	 * child.setName(newName);<br>
	 * deleteChild(child);<br>
	 * insertChild(child, newIndex);<br>
	 * </code>
	 * @param child Child to be renamed
	 * @param newName New name for the child
	 * @param newIndex New position for the child
	 * 
	 * {@pre child != null}
	 * {@pre child is a valid child}
	 * {@pre newName != null}
	 * {@pre 0 <= newIndex < getChildCount()}
	 * 
	 * {@post child.getName() == newName}
	 * {@post getChildCount() == old(getChildCount())}
	 * {@post getChild(newIndex) == child}
	 * {@post All children with original indexes in the range
	 * oldIndex < index <= newIndex have been moved up one position}
	 */
	protected void renameChild(ProductContainerData child, String newName, int newIndex) {
		int oldIndex = _children.indexOf(child);
		assert oldIndex >= 0;
		if (oldIndex >= 0) {
			_children.remove(oldIndex);
		}
		child.setName(newName);
		_children.add(newIndex, child);
	}
	
}

