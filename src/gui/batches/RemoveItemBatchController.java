package gui.batches;

import application.ItemsFacade;
import application.item.Item;
import application.product.Product;
import common.Barcode;
import application.history.Command;
import application.history.CommandHistory;
import application.history.RemoveCommand;
import common.exceptions.CommandFailedException;
import gui.common.*;
import gui.item.ItemData;
import gui.product.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 * Controller class for the remove item batch view.
 */
public class RemoveItemBatchController extends Controller implements
        IRemoveItemBatchController {

    private ItemsFacade itemsFacade = ItemsFacade.getInstance();
    private ArrayList<Product> removedProducts = new ArrayList();
    private ArrayList<Item> removedItems = new ArrayList();
    private Timer removeItemsTimer;
	private CommandHistory history;

    /**
     * Constructor.
     *
     * @param view Reference to the remove item batch view.
     */
    public RemoveItemBatchController(IView view) {
        super(view);

        this.removeItemsTimer = new Timer(500, delayedRemoveItem);
        this.removeItemsTimer.setRepeats(false);
        getView().setUseScanner(true);
        getView().enableItemAction(false);
		history = new CommandHistory();

        construct();
    }

    /**
     * Returns a reference to the view for this controller.
     */
    @Override
    protected IRemoveItemBatchView getView() {
        return (IRemoveItemBatchView) super.getView();
    }

    /**
     * Loads data into the controller's view.
     *
     * {
     *
     * @pre None}
     *
     * {
     * @post The controller has loaded data into its view}
     */
    @Override
    protected void loadValues() {

        getView().enableItemAction(false);
        getView().setBarcode("");

        loadProductValues();
        loadItemValues();

    }

    /**
     * Sets the enable/disable state of all components in the controller's view.
     * A component should be enabled only if the user is currently allowed to
     * interact with that component.
     *
     * {
     *
     * @pre None}
     *
     * {
     * @post The enable/disable state of all components in the controller's view
     * have been set appropriately.}
     */
    @Override
    protected void enableComponents() {
/*
        Barcode barcode = new Barcode(getView().getBarcode());
        Item item = itemsFacade.getItem(barcode);
        if (getView().getBarcode().length() > 0
                && getView().getUseScanner() == false
                && item != null) {
            getView().enableItemAction(true);
        } else {
            getView().enableItemAction(false);
        }
*/ 
        if(!getView().getBarcode().equals("")){
            getView().enableItemAction(true);
        }else{
            getView().enableItemAction(false);
        }
		if(history.canRedo()){
			getView().enableRedo(true);
		}
		else{
			getView().enableRedo(false);
		}
		if(history.canUndo()){
			getView().enableUndo(true);
		}
		else{
			getView().enableUndo(false);
		}
    }

    /**
     * This method is called when the "Item Barcode" field is changed in the
     * remove item batch view by the user.
     */
    @Override
    public void barcodeChanged() {
        if (getView().getUseScanner()) {

            removeItemsTimer.restart();

        } else {
            enableComponents();
        }
    }
    ActionListener delayedRemoveItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
			if(getView().getBarcode().length() > 0){
				removeItem();
			}
        }
    };

    /**
     * This method is called when the "Use Barcode Scanner" setting is changed
     * in the remove item batch view by the user.
     */
    @Override
    public void useScannerChanged() {
        if (getView().getUseScanner()) {
            getView().setBarcode("");
        }
        enableComponents();
    }

    /**
     * This method is called when the selected product changes in the remove
     * item batch view.
     */
    @Override
    public void selectedProductChanged() {
        loadItemValues();
    }

    /**
     * This method is called when the user clicks the "Remove Item" button in
     * the remove item batch view.
     */
    @Override
    public void removeItem() {
		Barcode barcode = new Barcode(getView().getBarcode());
        Item item = itemsFacade.getItem(barcode);
        if (getView().getBarcode().length() > 0
                && item != null) {
			
            addToBatchHistory(item);

            Command command = new RemoveCommand(item, item.getProductContainer());
			try {
				command.execute();
			} catch (CommandFailedException ex) {
			}
			history.addNew(command);
		} 
		else {
            //pop up error message
            getView().displayErrorMessage("Item barcode is invalid. Cannot remove Item");
        }
        
        //rememberDeletedItems(deletedItemsCollection);
        getView().setBarcode("");
        loadValues();
		enableComponents();
    }

    /**
     * This method is called when the user clicks the "Redo" button in the
     * remove item batch view.
     */
    @Override
    public void redo() {
		try {
			RemoveCommand command = (RemoveCommand)history.getNextCommandToRedo();
			command.execute();
			Item item = command.getItemRemoved();
			addToBatchHistory(item);
		} catch (CommandFailedException ex) {
		}
		
		loadValues();
		enableComponents();
    }

    /**
     * This method is called when the user clicks the "Undo" button in the
     * remove item batch view.
     */
    @Override
    public void undo() {
		try {
			RemoveCommand command = (RemoveCommand)history.getNextCommandToUndo();
			command.undo();
			Item item = command.getItemRemoved();
			removeFromBatchHistory(item);
		} catch (CommandFailedException ex) {
		}
		
		loadValues();
		enableComponents();
    }

    /**
     * This method is called when the user clicks the "Done" button in the
     * remove item batch view.
     */
    @Override
    public void done() {
        getView().close();
    }

    private void loadProductValues() {
        ProductData selectedProduct = getView().getSelectedProduct();
        Iterator<Product> products = removedProducts.iterator();
        List<ProductData> productDatas = new ArrayList();
        while (products.hasNext()) {
            Product curProduct = products.next();
            ProductData curData = new ProductData();
            curData.setBarcode(curProduct.getBarcode().toString());
            curData.setDescription(curProduct.getProdDesc());
            curData.setShelfLife(curProduct.getShelfLifeString() + "");
            curData.setSize(curProduct.getSize().toString());
            curData.setSupply(curData.getSupply().toString());
            curData.setTag(curProduct);

            Iterator<Item> items = removedItems.iterator();
            int count = 0;
            while (items.hasNext()) {
                if (items.next().getProduct().equals(curProduct)) {
                    count++;
                }
            }
            curData.setCount(count + "");

            productDatas.add(productDatas.size(), curData);
            if (selectedProduct == null) {
                selectedProduct = null;
            } else if (selectedProduct.getDescription().equals(curData.getDescription())) {
                selectedProduct = curData;
            }
        }
        getView().setProducts(productDatas.toArray(new ProductData[0]));
        getView().selectProduct(selectedProduct);
    }

    private void loadItemValues() {
        ProductData selected = getView().getSelectedProduct();
        if (selected == null) {
			getView().setItems(new ItemData[0]);
            return;
        }
        Product selectedProduct = (Product) selected.getTag();

        Iterator<Item> items = removedItems.iterator();
        List<ItemData> itemDatas = new ArrayList();
        while (items.hasNext()) {
            Item curItem = items.next();
            if (curItem.getProduct().equals(selectedProduct)) {
                ItemData curData = new ItemData();
                curData.setBarcode(curItem.getBarcode().toString());
                curData.setEntryDate(curItem.getEntryDate());
                curData.setExpirationDate(curItem.getExpirationDate());
                if (curItem.getProductGroup() != null) {
                    curData.setProductGroup(curItem.getProductGroup().getName());
                }
                curData.setStorageUnit(curItem.getStorageUnit().getName());
                curData.setTag(curItem);

                itemDatas.add(curData);
            }
        }
        getView().setItems(itemDatas.toArray(new ItemData[0]));
    }

	private void addToBatchHistory(Item item) {
		Product product = item.getProduct();

		if (!removedProducts.contains(product)) {
			removedProducts.add(product);
		}
		removedItems.add(item);
	}

	private void removeFromBatchHistory(Item item) {
		Product product = itemsFacade.getProduct(item);
		
		removedItems.remove(item);
		
		Iterator<Item> allItems = removedItems.iterator();
		boolean productRemoved = false;
		while(allItems.hasNext()){
			if(product.equals(allItems.next().getProduct())){
				productRemoved = true;
			}
		}
		
		if(!productRemoved){
			removedProducts.remove(product);
		}
	}
}
