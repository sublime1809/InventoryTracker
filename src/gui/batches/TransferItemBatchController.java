package gui.batches;

import application.ItemsFacade;
import application.history.CommandHistory;
import application.history.TransferItemsCommand;
import application.item.Item;
import application.product.Product;
import application.storage.ProductContainer;
import common.Barcode;
import common.exceptions.CommandFailedException;
import gui.common.*;
import gui.inventory.*;
import gui.item.ItemData;
import gui.product.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Timer;

/**
 * Controller class for the transfer item batch view.
 */
public class TransferItemBatchController extends Controller implements
        ITransferItemBatchController {

    private ItemsFacade itemsFacade = ItemsFacade.getInstance();
    private ProductContainer newContainer;
    private Set<Product> productsMoved;
    private Set<Item> itemsMoved;
    private Timer addItemsTimer;
	private CommandHistory history;

    /**
     * Constructor.
     *
     * @param view Reference to the transfer item batch view.
     * @param target Reference to the storage unit to which items are being
     * transferred.
     */
    public TransferItemBatchController(IView view, ProductContainerData target) {
        super(view);
        newContainer = (ProductContainer) target.getTag();
        productsMoved = new TreeSet<Product>();
        itemsMoved = new HashSet<Item>();
        this.addItemsTimer = new Timer(500, delayedTransferItem);
        this.addItemsTimer.setRepeats(false);
		this.history = new CommandHistory();
		getView().setUseScanner(true);
        construct();
    }

    /**
     * Returns a reference to the view for this controller.
     */
    @Override
    protected ITransferItemBatchView getView() {
        return (ITransferItemBatchView) super.getView();
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
        if (getView().getUseScanner()) {
            getView().enableItemAction(false);
        }
		
		getView().enableRedo(history.canRedo());
		getView().enableUndo(history.canUndo());
		
        Barcode barcode = new Barcode(getView().getBarcode());
		if(barcode.toString().equals("")){
			getView().enableItemAction(false);
		}
		else{
			getView().enableItemAction(true);
		}
    }

    /**
     * This method is called when the "Item Barcode" field in the transfer item
     * batch view is changed by the user.
     */
    @Override
    public void barcodeChanged() {
        if (getView().getUseScanner()) {
            addItemsTimer.restart();
        } else {
            enableComponents();
        }
    }
    ActionListener delayedTransferItem = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
			if(getView().getBarcode().length() > 0){
				transferItem();
			}
        }
    };

    /**
     * This method is called when the "Use Barcode Scanner" setting in the
     * transfer item batch view is changed by the user.
     */
    @Override
    public void useScannerChanged() {
        enableComponents();
    }

    /**
     * This method is called when the selected product changes in the transfer
     * item batch view.
     */
    @Override
    public void selectedProductChanged() {
        loadItemValues();
    }

    /**
     * This method is called when the user clicks the "Transfer Item" button in
     * the transfer item batch view.
     */
    @Override
    public void transferItem() {
		Barcode barcode = new Barcode(getView().getBarcode());
        Item item = itemsFacade.getItem(barcode);
        if (item == null) {
            getView().displayErrorMessage("That Item Does Not Exist");
			getView().setBarcode("");
			enableComponents();
			return;
        }
		if(itemsMoved.contains(item)){
			getView().displayErrorMessage("Item already transfered");
			getView().setBarcode("");
			enableComponents();
			return;
		}
		
		ProductContainer oldContainer = item.getProductContainer();
		boolean productWasInContainer = 
				itemsFacade.containerHasProduct(newContainer, item.getProduct());
		
        try {
			TransferItemsCommand command = 
					new TransferItemsCommand(item, oldContainer, newContainer);
			command.productWasInContainer(productWasInContainer);
			command.execute();
			history.addNew(command);
//            itemsFacade.moveItemToStorageUnit(item, newContainer);
            if (!productsMoved.contains(item.getProduct())) {
                productsMoved.add(item.getProduct());
				command.setFirstOfProduct(true);
            }
            itemsMoved.add(item);
        } catch (CommandFailedException ex) {
			getView().displayErrorMessage("There was an error executing the command.");
        }
        loadValues();
        getView().setBarcode("");
		enableComponents();
    }

    /**
     * This method is called when the user clicks the "Redo" button in the
     * transfer item batch view.
     */
    @Override
    public void redo() {
		TransferItemsCommand command = (TransferItemsCommand) history.getNextCommandToRedo();
		try
		{
			command.execute();
			this.itemsMoved.add(command.getItem());
			if(command.wasFirstOfProduct())
			{
				this.productsMoved.add(command.getItem().getProduct());
			}
		}
		catch (CommandFailedException ex)
		{
			getView().displayErrorMessage("Could not redo command.");
		}
		
		loadValues();
		enableComponents();
        getView().setBarcode("");
    }

    /**
     * This method is called when the user clicks the "Undo" button in the
     * transfer item batch view.
     */
    @Override
    public void undo() 
	{
		TransferItemsCommand command = (TransferItemsCommand) history.getNextCommandToUndo();
		try
		{
			command.undo();
			this.itemsMoved.remove(command.getItem());
//			if(!command.productWasInContainer())
//			{
//				//Remove product from container
//				this.
//			}
			if(command.wasFirstOfProduct())
			{
				//remove product from view
				this.productsMoved.remove(command.getItem().getProduct());
			}
		}
		catch (CommandFailedException ex)
		{
			getView().displayErrorMessage("Could not redo command.");
		}
		
		loadValues();
        getView().setBarcode("");
		enableComponents();
    }

    /**
     * This method is called when the user clicks the "Done" button in the
     * transfer item batch view.
     */
    @Override
    public void done() {
        getView().close();
    }

    private void loadProductValues() {
        ProductData selectedProduct = getView().getSelectedProduct();
        Iterator<Product> products = productsMoved.iterator();
        List<ProductData> productDatas = new ArrayList<ProductData>();
        while (products.hasNext()) {
            Product curProduct = products.next();
            ProductData curData = new ProductData();
            curData.setBarcode(curProduct.getBarcode().toString());
            curData.setDescription(curProduct.getProdDesc());
            curData.setShelfLife(curProduct.getShelfLifeString() + "");
            curData.setSize(curProduct.getSize().toString());
            curData.setSupply(curProduct.getMonthSupply().toString());
            curData.setTag(curProduct);

            Iterator<Item> items = itemsMoved.iterator();
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

        Iterator<Item> items = itemsMoved.iterator();
        List<ItemData> itemDatas = new ArrayList<ItemData>();
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
}
