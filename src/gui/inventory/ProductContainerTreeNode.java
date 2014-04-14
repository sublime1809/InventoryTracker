package gui.inventory;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * ProductContainerTreeNode is a subclass of Swing's DefaultMutableTreeNode class
 * that is used to represent nodes in the product container tree.
 */
@SuppressWarnings("serial")
public class ProductContainerTreeNode extends DefaultMutableTreeNode {

	/**
	 * Constructs a new ProductContainerTreeNode object.
	 * 
	 * @param productContainer ProductContainerData object associated with
	 * this tree node.  This value becomes the "user object" of the node.
	 * 
	 * {@pre productContainer != null}
	 * 
	 * {@post getUserObject() == productContainer}
	 * {@post New ProductContainerTreeNode object is fully initialized}
	 */
	public ProductContainerTreeNode(ProductContainerData productContainer) {
		super(productContainer);
	}
	
	/**
	 * Returns the "user object" of the node as a ProductContainerData.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the "user object" of the node as a ProductContainerData}
	 */
	public ProductContainerData getProductContainer() {
		return (ProductContainerData)getUserObject();
	}
	
	/**
	 * Returns true if this node is the "all storage units" node and false otherwise.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if this node is the "all storage units" node and false otherwise.}
	 */
	public boolean isAllStorageUnits() {
		return this.isRoot();
	}
	
	/**
	 * Returns true if this node is a "storage unit" node and false otherwise.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if this node is a "storage unit" node and false otherwise.}
	 */
	public boolean isStorageUnit() {
		ProductContainerTreeNode parent = (ProductContainerTreeNode)getParent();
		return (parent != null && parent.isRoot());
	}
	
	/**
	 * Returns true if this node is a "product group" node and false otherwise.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if this node is an "product group" node and false otherwise.}
	 */
	public boolean isProductGroup() {
		ProductContainerTreeNode parent = (ProductContainerTreeNode)getParent();
		return (parent != null && !parent.isRoot());
	}
	
	@Override
	public String toString() {
		if (isAllStorageUnits()) {
			return "Storage Units";
		}
		else {
			return getProductContainer().getName();
		}
	}
	
}

