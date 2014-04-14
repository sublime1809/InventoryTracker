package gui.inventory;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import common.util.*;

import gui.main.GUI;
import gui.product.*;
import gui.productgroup.*;
import gui.storageunit.*;
import gui.item.*;
import gui.batches.*;
import gui.common.*;


@SuppressWarnings("serial")
public class InventoryView extends View implements IInventoryView {

	//-------------------------------
	// Product Container Tree members
	//-------------------------------
	private DefaultTreeModel _productContainerTreeModel;
	private JTree _productContainerTree;
	private JScrollPane _productContainerTreeScrollPane;
	private JPopupMenu _allStorageUnitsMenu;
	private JMenuItem _removeItemsMenuItem;
	private JMenuItem _addStorageUnitMenuItem;
	private JPopupMenu _storageUnitMenu;
	private JMenuItem _addItemsMenuItem;
	private JMenuItem _transferItemsMenuItem;
	private JMenuItem _addProductGroupMenuItem;
	private JMenuItem _editStorageUnitMenuItem;
	private JMenuItem _deleteStorageUnitMenuItem;
	private JPopupMenu _productGroupMenu;
	private JMenuItem _addProductSubgroupMenuItem;
	private JMenuItem _editProductGroupMenuItem;
	private JMenuItem _deleteProductGroupMenuItem;
	
	//--------------------------
	// Context Info members
	//--------------------------
	private JPanel _contextInfoPanel;
	private JLabel _contextInfoUnitLabel;
	private JTextField _contextInfoUnitField;
	private JLabel _contextInfoGroupLabel;
	private JTextField _contextInfoGroupField;
	private JLabel _contextInfoSupplyLabel;
	private JTextField _contextInfoSupplyField;
	
	//--------------------------
	// Product Table members
	//--------------------------
	private JTable _productTable;
	private DefaultTableModel _productTableModel;
	private DefaultTableColumnModel _productTableColumnModel;
	private JTableHeader _productTableHeader;
	private JScrollPane _productTableScrollPane;
	private JPopupMenu _productMenu;
	private JMenuItem _editProductMenuItem;
	private JMenuItem _deleteProductMenuItem;
	
	//--------------------------
	// Item Table members
	//--------------------------
	private JTable _itemTable;
	private DefaultTableModel _itemTableModel;
	private DefaultTableColumnModel _itemTableColumnModel;
	private JTableHeader _itemTableHeader;
	private JScrollPane _itemTableScrollPane;
	private JPopupMenu _itemMenu;
	private JMenuItem _editItemMenuItem;
	private JMenuItem _removeItemMenuItem;
	
	//--------------------------
	// Other members
	//--------------------------
	private JPanel _productPanel;
	private JSplitPane _midSplitPane;	
	private JSplitPane _rightSplitPane;
	
	
	private HashMap<ProductContainerData, ProductContainerTreeNode> _productContainers;

	
	public InventoryView(GUI parent) {
		super(parent);
	
		construct();
		
		_controller = new InventoryController(this);
	}
	
	@Override
	public IInventoryController getController() {
		return (IInventoryController)super.getController();
	}

	@Override
	protected void createComponents() {
		createProductContainerTree();
		createContextInfoPanel();
		createProductTable();
		createItemTable();
	}
	
	@Override
	protected void layoutComponents() {

		final int LabelSpacing = 2;
		final int FieldSpacing = 5;
		
		_contextInfoPanel = new JPanel();
		_contextInfoPanel.setLayout(new BoxLayout(_contextInfoPanel, BoxLayout.X_AXIS));
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(FieldSpacing, FieldSpacing)));
		_contextInfoPanel.add(_contextInfoUnitLabel);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(LabelSpacing, LabelSpacing)));
		_contextInfoPanel.add(_contextInfoUnitField);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(FieldSpacing, FieldSpacing)));
		_contextInfoPanel.add(_contextInfoGroupLabel);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(LabelSpacing, LabelSpacing)));
		_contextInfoPanel.add(_contextInfoGroupField);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(FieldSpacing, FieldSpacing)));
		_contextInfoPanel.add(_contextInfoSupplyLabel);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(LabelSpacing, LabelSpacing)));
		_contextInfoPanel.add(_contextInfoSupplyField);
		_contextInfoPanel.add(Box.createRigidArea(new Dimension(FieldSpacing, FieldSpacing)));
		
		_productPanel = new JPanel();
		_productPanel.setLayout(new BoxLayout(_productPanel, BoxLayout.Y_AXIS));
		_productPanel.setPreferredSize(new Dimension(600, 300));

		_productPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		_productPanel.add(_contextInfoPanel);
		_productPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		_productPanel.add(_productTableScrollPane);
		
		_rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
										_productPanel, _itemTableScrollPane);

		_midSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				_productContainerTreeScrollPane, _rightSplitPane);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(_midSplitPane);
	}

	private void createProductContainerTree() {
		
		TreeSelectionListener treeSelectionListener = 
			createProductContainerTreeSelectionListener();

		MouseListener mouseListener = createProductContainerTreeMouseListener();
		
		_productContainerTree = new JTree();
		_productContainerTree.setCellRenderer(new ProductContainerTreeCellRenderer());
		_productContainerTree.setFont(createFont(_productContainerTree.getFont(), ContentFontSize));
		_productContainerTree.setRootVisible(true);
		_productContainerTree.setExpandsSelectedPaths(true);
		_productContainerTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		_productContainerTree.setDropMode(DropMode.ON);
		_productContainerTree.setTransferHandler(new ProductContainerTransferHandler());

		_productContainerTreeScrollPane = new JScrollPane(_productContainerTree, 
											JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
											JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_productContainerTreeScrollPane.setPreferredSize(new Dimension(250, 175));
		_productContainerTreeScrollPane.setBorder(createTitledBorder("", BorderFontSize));
		_productContainerTree.addTreeSelectionListener(treeSelectionListener);
		_productContainerTree.addMouseListener(mouseListener);
		
		ActionListener actionListener = createProductContainerTreeActionListener();

		_allStorageUnitsMenu = new JPopupMenu();
		_removeItemsMenuItem = new JMenuItem("Remove Items...");
		_removeItemsMenuItem.addActionListener(actionListener);
		_allStorageUnitsMenu.add(_removeItemsMenuItem);
		_allStorageUnitsMenu.addSeparator();
		_addStorageUnitMenuItem = new JMenuItem("Add Storage Unit...");
		_addStorageUnitMenuItem.addActionListener(actionListener);
		_allStorageUnitsMenu.add(_addStorageUnitMenuItem);

		_storageUnitMenu = new JPopupMenu();
		_addItemsMenuItem = new JMenuItem("Add Items...");
		_addItemsMenuItem.addActionListener(actionListener);
		_storageUnitMenu.add(_addItemsMenuItem);
		_transferItemsMenuItem = new JMenuItem("Transfer Items...");
		_transferItemsMenuItem.addActionListener(actionListener);
		_storageUnitMenu.add(_transferItemsMenuItem);
		_storageUnitMenu.addSeparator();
		_addProductGroupMenuItem = new JMenuItem("Add Product Group...");
		_addProductGroupMenuItem.addActionListener(actionListener);
		_storageUnitMenu.add(_addProductGroupMenuItem);
		_storageUnitMenu.addSeparator();
		_editStorageUnitMenuItem = new JMenuItem("Edit Storage Unit...");
		_editStorageUnitMenuItem.addActionListener(actionListener);
		_storageUnitMenu.add(_editStorageUnitMenuItem);
		_deleteStorageUnitMenuItem = new JMenuItem("Delete Storage Unit");
		_deleteStorageUnitMenuItem.addActionListener(actionListener);
		_storageUnitMenu.add(_deleteStorageUnitMenuItem);
		
		_productGroupMenu = new JPopupMenu();
		_addProductSubgroupMenuItem = new JMenuItem("Add Product Group...");
		_addProductSubgroupMenuItem.addActionListener(actionListener);
		_productGroupMenu.add(_addProductSubgroupMenuItem);
		_productGroupMenu.addSeparator();
		_editProductGroupMenuItem = new JMenuItem("Edit Product Group...");
		_editProductGroupMenuItem.addActionListener(actionListener);
		_productGroupMenu.add(_editProductGroupMenuItem);
		_deleteProductGroupMenuItem = new JMenuItem("Delete Product Group");
		_deleteProductGroupMenuItem.addActionListener(actionListener);
		_productGroupMenu.add(_deleteProductGroupMenuItem);
	}
	
	private TreeSelectionListener createProductContainerTreeSelectionListener() {
		return new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				getController().productContainerSelectionChanged();
		    }			
		};
	}
	
	private MouseListener createProductContainerTreeMouseListener() {
		return new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
    		}

			@Override
    		public void mouseReleased(MouseEvent e) {
    			handleMouseEvent(e);
    		}
    	    private void handleMouseEvent(MouseEvent e) {
    			if (eventsAreDisabled()) {
    				return;
    			}
				ProductContainerTreeNode node = 
					(ProductContainerTreeNode)TreeOperations.getSelectedTreeNode(_productContainerTree);
				if (node != null) {
					if (node.isAllStorageUnits()) {
						if (e.isPopupTrigger()) {
			    			_productContainerTree.requestFocus();
			    			enableAllStorageUnitsMenuItems();
			    			_allStorageUnitsMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
					else if (node.isStorageUnit()) {
						if (e.isPopupTrigger()) {
			    			_productContainerTree.requestFocus();
			    			enableStorageUnitMenuItems();
			    			_storageUnitMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
					else if (node.isProductGroup()) {
						if (e.isPopupTrigger()) {
			    			_productContainerTree.requestFocus();
			    			enableProductGroupMenuItems();
			    			_productGroupMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
    	    }
		};
	}
	
	private ActionListener createProductContainerTreeActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == _addStorageUnitMenuItem) {
					addStorageUnit();
				}
				else if (evt.getSource() == _removeItemsMenuItem) {
					removeItems();
				}
				else if (evt.getSource() == _addItemsMenuItem) {
					addItems();
				}
				else if (evt.getSource() == _transferItemsMenuItem) {
					transferItems();
				}
				else if (evt.getSource() == _addProductGroupMenuItem) {
					addProductGroup();
				}
				else if (evt.getSource() == _editStorageUnitMenuItem) {
					editStorageUnit();
				}
				else if (evt.getSource() == _deleteStorageUnitMenuItem) {
					deleteStorageUnit();
				}
				else if (evt.getSource() == _addProductSubgroupMenuItem) {
					addProductGroup();
				}
				else if (evt.getSource() == _editProductGroupMenuItem) {
					editProductGroup();
				}
				else if (evt.getSource() == _deleteProductGroupMenuItem) {
					deleteProductGroup();
				}
			}
		};
	}
	
	private void setMaximumSize(JComponent c) {
		Dimension preferred = c.getPreferredSize();
		Dimension maximum = new Dimension(Integer.MAX_VALUE, (int)preferred.getHeight());
		c.setMaximumSize(maximum);
	}

	private void createContextInfoPanel() {
		_contextInfoUnitLabel = new JLabel("UNIT:");
		Font font = createFont(_contextInfoUnitLabel.getFont(), ContextInfoFontSize);
		_contextInfoUnitLabel.setFont(font);
		
		_contextInfoUnitField = new JTextField(15);
		_contextInfoUnitField.setFont(font);
		_contextInfoUnitField.setEditable(false);
		setMaximumSize(_contextInfoUnitField);
		
		_contextInfoGroupLabel = new JLabel("GROUP:");
		_contextInfoGroupLabel.setFont(font);
		
		_contextInfoGroupField = new JTextField(15);
		_contextInfoGroupField.setFont(font);
		_contextInfoGroupField.setEditable(false);
		setMaximumSize(_contextInfoGroupField);
		
		_contextInfoSupplyLabel = new JLabel("3-MONTH SUPPLY:");
		_contextInfoSupplyLabel.setFont(font);
		
		_contextInfoSupplyField = new JTextField(15);
		_contextInfoSupplyField.setFont(font);
		_contextInfoSupplyField.setEditable(false);
		setMaximumSize(_contextInfoSupplyField);
	}
	
	private void createProductTable() {
		
		MouseAdapter mouseListener = new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
    		}
			
			@Override
    		public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
    		}
    	    
			private void handleMouseEvent(MouseEvent e) {
    	 		if (eventsAreDisabled()) {
    				return;
    			}
    			if (e.getSource() == _productTable) {
    	        	if (e.isPopupTrigger()) {
    	    			enableProductMenuItems();
    	    			_productMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
    			}
    			else if (e.getSource() == _productTableHeader) {
    	        	if (e.isPopupTrigger()) {
    	        		enableProductMenuItems();
    	    			_productMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
//    	    		else if (e.getButton() == MouseEvent.BUTTON1 &&
//    						e.getID() == MouseEvent.MOUSE_PRESSED) {
//    					int clickedColumnIndex = 
//    						commentsColumnModel.getColumnIndexAtX(e.getX());
//    					if (clickedColumnIndex >= 0) {
//    						updateCommentSortOrder(clickedColumnIndex);
//    					}
//    	    		}
    			}
    	    }
		};

		_productTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0, "Description", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		column = createTableColumn(1, "Size", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		column = createTableColumn(2, "Count", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		column = createTableColumn(3, "Shelf Life", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		column = createTableColumn(4, "3-Month Supply", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		column = createTableColumn(5, "Product Barcode", ContentFontSize);
		_productTableColumnModel.addColumn(column);
		
		_productTableModel = new DefaultTableModel(0, 6) {
			@SuppressWarnings("serial")
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		_productTable = new JTable(_productTableModel, _productTableColumnModel);
		_productTable.setDragEnabled(true);
		_productTable.setTransferHandler(new ProductTransferHandler());
		_productTable.setFont(createFont(_productTable.getFont(), ContentFontSize));
		_productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_productTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						if (eventsAreDisabled()) {
							return;
						}
						if (evt.getValueIsAdjusting()) {
							return;
						}
						getController().productSelectionChanged();
					}
				});
		_productTable.addMouseListener(mouseListener);
		
		_productTableHeader = _productTable.getTableHeader();
		_productTableHeader.setReorderingAllowed(false);
		_productTableHeader.addMouseListener(mouseListener);
		_productTableHeader.setBackground(Color.blue);
		
		_productTableScrollPane = new JScrollPane(_productTable, 
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//_productTableScrollPane.setPreferredSize(new Dimension(600, 300));
		_productTableScrollPane.setBorder(createTitledBorder("Products", BorderFontSize));

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == _editProductMenuItem) {
					editProduct();
				}
				else if (evt.getSource() == _deleteProductMenuItem) {
					deleteProduct();
				}
			}
		};

		_productMenu = new JPopupMenu();
		_editProductMenuItem = new JMenuItem("Edit Product...");
		_editProductMenuItem.addActionListener(actionListener);
		_productMenu.add(_editProductMenuItem);
		_deleteProductMenuItem = new JMenuItem("Delete Product");
		_deleteProductMenuItem.addActionListener(actionListener);
		_productMenu.add(_deleteProductMenuItem);
	}

	private void createItemTable() {
		
		MouseAdapter mouseListener = new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
    		}
			
			@Override
    		public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
    		}
    	    
			private void handleMouseEvent(MouseEvent e) {
    	 		if (eventsAreDisabled()) {
    				return;
    			}
    			if (e.getSource() == _itemTable) {
    	        	if (e.isPopupTrigger()) {
    	    			enableItemMenuItems();
    	    			_itemMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
    			}
    			else if (e.getSource() == _itemTableHeader) {
    	        	if (e.isPopupTrigger()) {
    	        		enableItemMenuItems();
    	        		_itemMenu.show(e.getComponent(), e.getX(), e.getY());
    				}
//    	    		else if (e.getButton() == MouseEvent.BUTTON1 &&
//    						e.getID() == MouseEvent.MOUSE_PRESSED) {
//    					int clickedColumnIndex = 
//    						commentsColumnModel.getColumnIndexAtX(e.getX());
//    					if (clickedColumnIndex >= 0) {
//    						updateCommentSortOrder(clickedColumnIndex);
//    					}
//    	    		}
    			}
    	    }
		};
		
		_itemTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0, "Entry Date", ContentFontSize);
		_itemTableColumnModel.addColumn(column);
		column = createTableColumn(1, "Expiration Date", ContentFontSize);
		_itemTableColumnModel.addColumn(column);
		column = createTableColumn(2, "Item Barcode", ContentFontSize);
		_itemTableColumnModel.addColumn(column);
		column = createTableColumn(3, "Storage Unit", ContentFontSize);
		_itemTableColumnModel.addColumn(column);
		column = createTableColumn(4, "Product Group", ContentFontSize);
		_itemTableColumnModel.addColumn(column);
		
		_itemTableModel = new DefaultTableModel(0, 5) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		_itemTable = new JTable(_itemTableModel, _itemTableColumnModel);
		_itemTable.setDragEnabled(true);
		_itemTable.setTransferHandler(new ItemTransferHandler());
		_itemTable.setFont(createFont(_itemTable.getFont(), ContentFontSize));
		_itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_itemTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						if (eventsAreDisabled()) {
							return;
						}
						if (evt.getValueIsAdjusting()) {
							return;
						}
						getController().itemSelectionChanged();
					}
				});
		_itemTable.addMouseListener(mouseListener);
		
		_itemTableHeader = _itemTable.getTableHeader();
		_itemTableHeader.setReorderingAllowed(false);
		_itemTableHeader.addMouseListener(mouseListener);
		
		_itemTableScrollPane = new JScrollPane(_itemTable, 
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		_itemTableScrollPane.setPreferredSize(new Dimension(600, 300));
		_itemTableScrollPane.setBorder(createTitledBorder("Items", BorderFontSize));

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == _editItemMenuItem) {
					editItem();
				}
				else if (evt.getSource() == _removeItemMenuItem) {
					removeItem();
				}
			}
		};

		_itemMenu = new JPopupMenu();
		_editItemMenuItem = new JMenuItem("Edit Item");
		_editItemMenuItem.addActionListener(actionListener);
		_itemMenu.add(_editItemMenuItem);
		_removeItemMenuItem = new JMenuItem("Remove Item");
		_removeItemMenuItem.addActionListener(actionListener);
		_itemMenu.add(_removeItemMenuItem);
	}
	
	private void enableAllStorageUnitsMenuItems() {
		_addStorageUnitMenuItem.setEnabled(getController().canAddStorageUnit());
	}
	
	private void enableStorageUnitMenuItems() {
		_addItemsMenuItem.setEnabled(getController().canAddItems());
		_addProductGroupMenuItem.setEnabled(getController().canAddProductGroup());
		_editStorageUnitMenuItem.setEnabled(getController().canEditStorageUnit());
		_deleteStorageUnitMenuItem.setEnabled(getController().canDeleteStorageUnit());
	}
	
	private void enableProductGroupMenuItems() {
		_addProductSubgroupMenuItem.setEnabled(getController().canAddProductGroup());
		_editProductGroupMenuItem.setEnabled(getController().canEditProductGroup());
		_deleteProductGroupMenuItem.setEnabled(getController().canDeleteProductGroup());
	}
	
	private void enableProductMenuItems() {
		_editProductMenuItem.setEnabled(getController().canEditProduct());
		_deleteProductMenuItem.setEnabled(getController().canDeleteProduct());
	}
	
	private void enableItemMenuItems() {
		_editItemMenuItem.setEnabled(getController().canEditItem());
		_removeItemMenuItem.setEnabled(getController().canRemoveItem());
	}
	
	//////////////////
	// Action Methods
	//////////////////

	private void addStorageUnit() {
		getController().addStorageUnit();
	}
	
	private void addItems() {
		getController().addItems();
	}
	
	private void transferItems() {
		getController().transferItems();
	}
	
	private void removeItems() {
		getController().removeItems();
	}

	private void editStorageUnit() {
		getController().editStorageUnit();
	}

	private void deleteStorageUnit() {
		getController().deleteStorageUnit();
	}
	
	private void addProductGroup() {
		getController().addProductGroup();
	}

	private void editProductGroup() {
		getController().editProductGroup();
	}

	private void deleteProductGroup() {
		getController().deleteProductGroup();
	}
	
	private void editProduct() {
		getController().editProduct();
	}
	
	private void deleteProduct() {
		getController().deleteProduct();
	}
	
	private void editItem() {
		getController().editItem();
	}
	
	private void removeItem() {
		getController().removeItem();
	}

	//////////////////
	// IInventoryView
	//////////////////

	@Override
	public void setProductContainers(ProductContainerData rootData) {
		boolean disabledEvents = disableEvents();
		try {
			_productContainers = new HashMap<ProductContainerData, ProductContainerTreeNode>();
			DefaultMutableTreeNode rootNode = null;
			if (rootData == null) {
				rootNode = new ProductContainerTreeNode(null);
			}
			else {
				rootNode = buildProductContainerTree(rootData);
			}
			_productContainerTreeModel = new DefaultTreeModel(rootNode);
			_productContainerTree.setModel(_productContainerTreeModel);	
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	/**
	 * Builds the item container tree that will be displayed in the JTree.
	 */
	private DefaultMutableTreeNode buildProductContainerTree(ProductContainerData data) {
		ProductContainerTreeNode node = new ProductContainerTreeNode(data);
		_productContainers.put(data, node);
		for (int i = 0; i < data.getChildCount(); ++i) {
			node.add(buildProductContainerTree(data.getChild(i)));
		}
		return node;
	}
	
	@Override
	public void insertProductContainer(ProductContainerData parent, 
										ProductContainerData newContainer,
										int index) {
		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode parentNode = _productContainers.get(parent);
			assert parentNode != null;
			if (parentNode != null) {
				parent.insertChild(newContainer, index);
				
				ProductContainerTreeNode newNode = new ProductContainerTreeNode(newContainer);
				parentNode.insert(newNode, index);
				_productContainers.put(newContainer, newNode);
				
				// notify the tree model of the change
				_productContainerTreeModel.nodesWereInserted(parentNode, new int[]{index});
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public ProductContainerData getSelectedProductContainer() {
		ProductContainerTreeNode selectedNode = getSelectedProductContainerNode();
		return((selectedNode != null) ? selectedNode.getProductContainer() : null);		
	}

	@Override
	public void selectProductContainer(ProductContainerData container) {
		boolean disabledEvents = disableEvents();
		try {
			if (container != null) {
				if (_productContainers.containsKey(container)) {
					selectProductContainerNode(_productContainers.get(container));
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	/**
	 * Selects the specified node in the product container tree.
	 * 
	 * @param node The node to be selected
	 */
	private void selectProductContainerNode(ProductContainerTreeNode node) {
		TreeOperations.selectTreeNode(_productContainerTree, node);
	}
	
	/**
	 * Returns the currently selected node in the product container tree.
	 */
	private ProductContainerTreeNode getSelectedProductContainerNode() {
		return (ProductContainerTreeNode)
					TreeOperations.getSelectedTreeNode(_productContainerTree);
	}

	@Override
	public void deleteProductContainer(ProductContainerData deletedContainer) {
		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode deletedNode = _productContainers.get(deletedContainer);
			assert deletedNode != null;
			
			if (deletedNode != null) {	
				
				ProductContainerTreeNode parentNode = 
					(ProductContainerTreeNode)deletedNode.getParent();
				assert parentNode != null;
	
				if (parentNode != null) {
					
					ProductContainerData parentContainer = 
						parentNode.getProductContainer();
					parentContainer.deleteChild(deletedContainer);
		
					int childIndex = parentNode.getIndex(deletedNode);
					assert childIndex >= 0;
					if (childIndex >= 0) {
						parentNode.remove(childIndex);				
						// notify the tree model of the change
						_productContainerTreeModel.nodesWereRemoved(parentNode, 
															new int[]{childIndex},
															new Object[]{deletedNode});
					}
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	@Override
	public void renameProductContainer(ProductContainerData renamedContainer, String newName, 
										int newIndex) {
		
		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode renamedNode = _productContainers.get(renamedContainer);
			assert renamedNode != null;
			
			if (renamedNode != null) {
		
				ProductContainerTreeNode parentNode = 
					(ProductContainerTreeNode)renamedNode.getParent();
				assert parentNode != null;
				
				if (parentNode != null) {
					
					ProductContainerData parentContainer = 
						parentNode.getProductContainer();
					parentContainer.renameChild(renamedContainer, newName, newIndex);
					
					TreePath renamedPath = new TreePath(renamedNode.getPath());
		
					// remember which descendant nodes were expanded so they
					// can be re-expanded after the tree is updated
					ArrayList<TreePath> expandedList = null;
					Enumeration<TreePath> expandedEnum = 
						_productContainerTree.getExpandedDescendants(renamedPath);
					if (expandedEnum != null) {
						expandedList = new ArrayList<TreePath>();
						while (expandedEnum.hasMoreElements()) {
							TreePath path = expandedEnum.nextElement();
							expandedList.add(path);
						}
					}
			
					// update the tree
					_productContainerTreeModel.removeNodeFromParent(renamedNode);
					_productContainerTreeModel.insertNodeInto(renamedNode, parentNode, newIndex);
					
					// re-expand descendant nodes that were expanded before the update
					if (expandedList != null) {
						for (TreePath path : expandedList) {
							_productContainerTree.expandPath(path);
						}
					}
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextUnit(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_contextInfoUnitField.setText(value);		
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextGroup(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_contextInfoGroupField.setText(value);		
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextSupply(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_contextInfoSupplyField.setText(value);		
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public ProductData getSelectedProduct() {
		int selectedIndex = _productTable.getSelectedRow();
		if (selectedIndex >= 0) {
			ProductFormatter formatter = 
				(ProductFormatter)_productTableModel.getValueAt(selectedIndex, 0);
			return (ProductData)formatter.getTag();
		}
		return null;
	}

	@Override
	public void selectProduct(ProductData product) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < _productTableModel.getRowCount(); ++i) {
				ProductFormatter formatter = (ProductFormatter)_productTableModel.getValueAt(i, 0);
				ProductData id = (ProductData)formatter.getTag();
				if (id == product) {
					TableOperations.selectTableRow(_productTable, i);
					return;
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setProducts(ProductData[] products) {
		boolean disabledEvents = disableEvents();
		try {
			_productTableModel.setRowCount(0);
			for (ProductData pd : products) {			
				ProductFormatter[] row = new ProductFormatter[6];
				row[0] = new ProductFormatter(0);
				row[0].setTag(pd);
				row[1] = new ProductFormatter(1);
				row[1].setTag(pd);
				row[2] = new ProductFormatter(2);
				row[2].setTag(pd);
				row[3] = new ProductFormatter(3);
				row[3].setTag(pd);
				row[4] = new ProductFormatter(4);
				row[4].setTag(pd);
				row[5] = new ProductFormatter(5);
				row[5].setTag(pd);
				_productTableModel.addRow(row);
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	@Override
	public ItemData getSelectedItem() {
		int selectedIndex = _itemTable.getSelectedRow();
		if (selectedIndex >= 0) {
			ItemFormatter formatter = 
				(ItemFormatter)_itemTableModel.getValueAt(selectedIndex, 0);
			return (ItemData)formatter.getTag();
		}
		return null;
	}

	@Override
	public void selectItem(ItemData item) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < _itemTableModel.getRowCount(); ++i) {
				ItemFormatter formatter = 
					(ItemFormatter)_itemTableModel.getValueAt(i, 0);
				ItemData id = (ItemData)formatter.getTag();
				if (id == item) {
					TableOperations.selectTableRow(_itemTable, i);
					return;
				}
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setItems(ItemData[] items) {
		boolean disabledEvents = disableEvents();
		try {
			_itemTableModel.setRowCount(0);
			for (ItemData id : items) {			
				ItemFormatter[] row = new ItemFormatter[5];
				row[0] = new ItemFormatter(0);
				row[0].setTag(id);
				row[1] = new ItemFormatter(1);
				row[1].setTag(id);
				row[2] = new ItemFormatter(2);
				row[2].setTag(id);
				row[3] = new ItemFormatter(3);
				row[3].setTag(id);
				row[4] = new ItemFormatter(4);
				row[4].setTag(id);
				_itemTableModel.addRow(row);
			}
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void displayAddProductGroupView() {
		ProductContainerData container = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(_parent, "Add Product Group");
		AddProductGroupView dialogView = new AddProductGroupView(_parent, dialogBox, container);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayAddStorageUnitView() {
		DialogBox dialog = new DialogBox(_parent, "Add Storage Unit");
		dialog.display(new AddStorageUnitView(_parent, dialog), false);
	}

	@Override
	public void displayEditProductGroupView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Product Group");
		EditProductGroupView dialogView = new EditProductGroupView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditStorageUnitView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Storage Unit");
		EditStorageUnitView dialogView = new EditStorageUnitView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}
	
	@Override
	public void displayAddItemBatchView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(_parent, "Add Items to " + target.getName());
		AddItemBatchView dialogView = new AddItemBatchView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayTransferItemBatchView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(_parent, "Transfer Items to " + target.getName());
		TransferItemBatchView dialogView = new TransferItemBatchView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayRemoveItemBatchView() {
		DialogBox dialogBox = new DialogBox(_parent, "Remove Items");
		RemoveItemBatchView dialogView = new RemoveItemBatchView(_parent, dialogBox);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditProductView() {
		ProductData target = getSelectedProduct();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Product");
		EditProductView dialogView = new EditProductView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditItemView() {
		ItemData target = getSelectedItem();
		DialogBox dialogBox = new DialogBox(_parent, "Edit Item");
		EditItemView dialogView = new EditItemView(_parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	//
	//
	//

	private class ProductFormatter extends Tagable
	{
		private int column;
		
		public ProductFormatter(int column) {
			this.column = column;
		}
		
		@Override
		public String toString() {
			ProductData data = (ProductData)getTag();
			if (data != null) {
				switch (column) {
				case 0: return data.getDescription();
				case 1: return data.getSize();
				case 2: return data.getCount();
				case 3: return data.getShelfLife();
				case 4: return data.getSupply();
				case 5: return data.getBarcode();
				default: assert false;
				}
			}
			return "";
		}
	}


	private class ItemFormatter extends Tagable
	{
		private int column;
		
		public ItemFormatter(int column) {
			this.column = column;
		}
		
		@Override
		public String toString() {
			ItemData data = (ItemData)getTag();
			if (data != null) {
				switch (column) {
				case 0: return DateUtils.formatDate(data.getEntryDate());
				case 1: return (data.getExpirationDate() != null ?
									DateUtils.formatDate(data.getExpirationDate()) :
									"");
				case 2: return data.getBarcode();
				case 3: return data.getStorageUnit();
				case 4: return data.getProductGroup();
				default: assert false;
				}
			}
			return "";
		}
	}
	
	
	private static DataFlavor InventoryFlavor = 
		new DataFlavor(InventoryTransferable.class, "Inventory Flavor");
	
	
	private class ProductTransferHandler extends TransferHandler {

		@Override
		protected Transferable createTransferable(JComponent component) {
			ProductData data = getSelectedProduct();
			if (data != null) {
				return new InventoryTransferable(data);
			}
			else {
				return null;
			}
		}

		@Override
		protected void exportDone(JComponent component, Transferable transferable, int action) {
			return;
		}

		@Override
		public int getSourceActions(JComponent component) {
			return COPY_OR_MOVE;
		}
		
	}
	
	
	private class ItemTransferHandler extends TransferHandler {

		@Override
		protected Transferable createTransferable(JComponent component) {
			ItemData data = getSelectedItem();
			if (data != null) {
				return new InventoryTransferable(data);
			}
			else {
				return null;
			}
		}

		@Override
		protected void exportDone(JComponent component, Transferable transferable, int action) {
			return;
		}

		@Override
		public int getSourceActions(JComponent component) {
			return COPY_OR_MOVE;
		}
		
	}
	
	
	private class ProductContainerTransferHandler extends TransferHandler {

		@Override
		public boolean canImport(TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}
			
			if (!support.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}
			
			javax.swing.JTree.DropLocation dropLoc = 
				(javax.swing.JTree.DropLocation)support.getDropLocation();
			
			if (dropLoc == null) {
				return false;
			}
			
			TreePath path = dropLoc.getPath();
			
			if (path == null) {
				return false;
			}
			
			ProductContainerTreeNode dropNode = 
				(ProductContainerTreeNode)path.getLastPathComponent();
			
			if (dropNode == null) {
				return false;
			}
			
			if (dropNode.isAllStorageUnits()) {
				return false;
			}
			
			return true;
		}

		@Override
		public boolean importData(TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}
			
			if (!support.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}
			
			javax.swing.JTree.DropLocation dropLoc = 
				(javax.swing.JTree.DropLocation)support.getDropLocation();
			
			if (dropLoc == null) {
				return false;
			}
			
			TreePath path = dropLoc.getPath();
			
			if (path == null) {
				return false;
			}
			
			ProductContainerTreeNode dropNode = 
				(ProductContainerTreeNode)path.getLastPathComponent();
			
			if (dropNode == null) {
				return false;
			}
			
			if (dropNode.isAllStorageUnits()) {
				return false;
			}
			
			Transferable transferable = support.getTransferable();
			
			if (transferable == null) {
				return false;
			}
			
			Object data = null;
			try {
				data = transferable.getTransferData(InventoryFlavor);
			}
			catch (Exception e) {
			}
			
			if (data == null) {
				return false;
			}

			ProductContainerData container = dropNode.getProductContainer();
			assert (container != null);
			
			if (data instanceof ProductData) {
				getController().addProductToContainer((ProductData)data, container);
			}
			else if (data instanceof ItemData) {
				getController().moveItemToContainer((ItemData)data, container);
			}
			else {
				return false;
			}
			
			return true;
		}
		
	}
	

	private class InventoryTransferable implements Transferable {

		private Object _data;
		
		public InventoryTransferable(Object data) {
			_data = data;
		}
		
		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (InventoryFlavor.equals(flavor)) {
				return _data;
			}
			else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{ InventoryFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return InventoryFlavor.equals(flavor);
		}
		
	}
	
}

@SuppressWarnings("serial")
class ProductContainerTreeCellRenderer extends DefaultTreeCellRenderer {

	private Icon _storageUnitIcon;
	
    public ProductContainerTreeCellRenderer() {
        _storageUnitIcon = new ImageIcon("images" + java.io.File.separator + "door-icon.png");
    }

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        if (value instanceof ProductContainerTreeNode) {
	        ProductContainerTreeNode node = (ProductContainerTreeNode)value;
	        if (node.isAllStorageUnits() || node.isStorageUnit()) {
	        	setIcon(_storageUnitIcon);
	        }
	        else {
	        	setIcon(closedIcon);
	        }
        }

        return this;
    }
	
}


