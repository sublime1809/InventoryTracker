package gui.common;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * TreeOperations provides useful utility functions for operating on
 * {@link javax.swing.JTree}s.
 */
public final class TreeOperations {

	/**
	 * Private Constructor.
	 */
	private TreeOperations() {
		assert false;
	}

	/**
	 * Selects the tree node that occupies the specified (x,y) point.
	 * This is used to select the tree node clicked on by the user.
	 * 
	 * @param tree tree object in which the mouse click occurred
	 * @param point coordinates of the mouse click event
	 * 
	 * {@pre tree != null}
	 * {@pre point != null}
	 * 
	 * {@post The tree node corresponding to the specified point is selected.
	 * If the specified point is not within the bounds of a tree node,
	 * nothing is done.}
	 */
	public static void selectTreeNode(JTree tree, Point point) {
		int itemRow = tree.getRowForLocation((int)point.getX(), (int)point.getY());
		if (itemRow >= 0) {
			Rectangle rowBounds = tree.getRowBounds(itemRow);
			if (rowBounds.contains(point)) {
				tree.setSelectionInterval(itemRow, itemRow);
			}
		}
	}

	/**
	 * Selects a tree node and makes it visible.
	 * 
	 * @param tree tree object in which the selection is to be made
	 * @param node node to be selected
	 * 
	 * {@pre tree != null}
	 * {@pre node != null AND node is a member of tree}
	 * 
	 * {@post The specified tree node is selected and visible.}
	 */
	public static void selectTreeNode(JTree tree, DefaultMutableTreeNode node) {
		TreePath nodePath = new TreePath(node.getPath());
		tree.setSelectionPath(nodePath);
		tree.scrollPathToVisible(nodePath);
	}
	
	/**
	 * Returns the currently-selected node in the specified tree, or null
	 * if no node is selected.
	 * 
	 * @param tree the tree for which the currently-selected node is desired
	 * 
	 * {@pre tree != null}
	 * 
	 * {@post The return value contains the currently-selected node, or null if no
	 * node is selected.}
	 */
	public static DefaultMutableTreeNode getSelectedTreeNode(JTree tree) {
		return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();	
	}
	
}


