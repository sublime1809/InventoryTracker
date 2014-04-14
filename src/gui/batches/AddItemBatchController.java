package gui.batches;

import application.ContainersFacade;
import application.ItemsFacade;
import application.ProductFacade;
import application.history.AddItemsCommand;
import application.history.CommandHistory;
import application.item.Item;
import application.product.Product;
import application.storage.StorageUnit;
import com.itextpdf.text.DocumentException;
import common.Barcode;
import common.exceptions.CommandFailedException;
import gui.common.*;
import gui.inventory.*;
import gui.item.ItemData;
import gui.product.*;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;

/**
 * Controller class for the add item batch view.
 */
public class AddItemBatchController extends Controller implements
        IAddItemBatchController {

    private ProductFacade productFacade = ProductFacade.getInstance();
    private ItemsFacade itemsFacade = ItemsFacade.getInstance();
	private ContainersFacade containersFacade = ContainersFacade.getInstance();
    private Collection<Item> newItemsCollection;
    private StorageUnit targetStorageUnit;
    private Timer addItemsTimer;
    private ArrayList<Item> addedItems = new ArrayList();
    private ArrayList<Product> addedProducts = new ArrayList();
	private CommandHistory commandHistory;

    /**
     * Constructor.
     *
     * @param view Reference to the add item batch view.
     * @param target Reference to the storage unit to which items are being
     * added.
     */
    public AddItemBatchController(IView view, ProductContainerData target) {
        super(view);

        this.addItemsTimer = new Timer(500, delayedAddItem);
        this.addItemsTimer.setRepeats(false);

        this.targetStorageUnit = (StorageUnit) target.getTag();
        this.getView().setUseScanner(true);
		this.commandHistory = new CommandHistory();

        construct();

    }

    /**
     * Returns a reference to the view for this controller.
     */
    @Override
    protected IAddItemBatchView getView() {
        return (IAddItemBatchView) super.getView();
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
        getView().setCount("1");
        getView().setBarcode("");
        getView().giveBarcodeFocus();
        getView().setEntryDate(new Date());

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
        try {
            if (getView().getBarcode().length() > 0
                    && Integer.valueOf(getView().getCount()) > 0
                    && getView().getUseScanner() == false) {
                getView().enableItemAction(true);
            } else {
                getView().enableItemAction(false);
            }
        } catch (NumberFormatException e) {
            getView().enableItemAction(false);
        }

        getView().enableRedo(commandHistory.canRedo());
        getView().enableUndo(commandHistory.canUndo());

    }

    /**
     * This method is called when the "Entry Date" field in the add item batch
     * view is changed by the user.
     */
    @Override
    public void entryDateChanged() {
        enableComponents();
    }

    /**
     * This method is called when the "Count" field in the add item batch view
     * is changed by the user.
     */
    @Override
    public void countChanged() {
        enableComponents();
    }

    /**
     * This method is called when the "Product Barcode" field in the add item
     * batch view is changed by the user.
     */
    @Override
    public void barcodeChanged() {

        if (getView().getUseScanner()) {
            //Sonmething needs to be done about the delay

            addItemsTimer.restart();
        } else {
            enableComponents();
        }
    }
    ActionListener delayedAddItem = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
			if(getView().getBarcode().length() > 0)
			{
				addItem();
			}
        }
    };

    /**
     * This method is called when the "Use Barcode Scanner" setting in the add
     * item batch view is changed by the user.
     */
    @Override
    public void useScannerChanged() {
        if (getView().getUseScanner()) {
            getView().setBarcode("");
        }
        enableComponents();
    }

    /**
     * This method is called when the selected product changes in the add item
     * batch view.
     */
    @Override
    public void selectedProductChanged() {
        //Load the items for that product
        /*ProductData selectedProduct = getView().getSelectedProduct();
         ItemData[] itemDatas = new ItemData[newItems.get(selectedProduct).size()];

         newItems.get(selectedProduct).toArray(itemDatas);

         getView().setItems(itemDatas);
         */
        loadItemValues();
    }

    /**
     * This method is called when the user clicks the "Add Item" button in the
     * add item batch view.
     */
    @Override
    public void addItem() {

        int itemCount = 0;
        try {
            itemCount = Integer.parseInt(getView().getCount());
        } catch (NumberFormatException e) {
            getView().displayErrorMessage("Invalid Count");
            getView().setBarcode("");
            return;
        }
        if (itemCount <= 0) {
            getView().displayErrorMessage("Invalid Count");
            getView().setBarcode("");
            return;
        }

        Barcode productBarcode = new Barcode(getView().getBarcode());		
        Product product = productFacade.getProduct(productBarcode);
		boolean productAddedToSystem = false;
		if (product == null)
		{
			getView().displayAddProductView();
			product = productFacade.getProduct(productBarcode);
			productAddedToSystem = true;
		}

		if (product == null)
		{
			//The cancel button was pushed.
			loadValues();
			return;
		}
//		else
//		{
//			productFacade.addProductToContainer(product, targetStorageUnit);
//		}

		boolean firstOfProduct = false;
		if (!addedProducts.contains(product))
		{
			addedProducts.add(product);
			firstOfProduct = true;
		}
		//now create the item(s)
        this.newItemsCollection = new ArrayList();
		for (int i = 0; i < itemCount; i++)
		{
			Item newItem = new Item(null, getView().getEntryDate(), null,
					targetStorageUnit, null, product);

			addedItems.add(newItem);
			newItemsCollection.add(newItem);
		}

		
		boolean newProduct = false;
		newProduct = containersFacade.storageUnitHasProduct(targetStorageUnit, product);
		
		AddItemsCommand currentCommand = new AddItemsCommand(newItemsCollection,
				product, targetStorageUnit, newProduct, firstOfProduct, productAddedToSystem);

		try
		{
			currentCommand.execute();
			commandHistory.addNew(currentCommand);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}


        getView().setBarcode("");
        getView().setEntryDate(new Date());
        getView().setCount("1");
        enableComponents();

        loadValues();
    }

    private void resetItem(String error) {
        getView().displayErrorMessage(error);
        getView().setBarcode("");
    }

    /**
     * This method is called when the user clicks the "Redo" button in the add
     * item batch view.
     */
    @Override
    public void redo() {
		try
		{
			AddItemsCommand command = (AddItemsCommand) commandHistory.getNextCommandToRedo();
			command.execute();
			
			addedItems.addAll(command.getItems());
			if(command.firstOfProduct())
			{
				addedProducts.add(command.getProduct());
			}
		}
		catch (CommandFailedException ex)
		{
			getView().displayErrorMessage("The command could not be redone");
		}
		
		enableComponents();
		loadValues();
    }

    /**
     * This method is called when the user clicks the "Undo" button in the add
     * item batch view.
     */
    @Override
    public void undo() {
		try
		{
			AddItemsCommand command = (AddItemsCommand) commandHistory.getNextCommandToUndo();
			command.undo();
			
			addedItems.removeAll(command.getItems());
			if(command.firstOfProduct())
			{
				addedProducts.remove(command.getProduct());
			}
		}
		catch (CommandFailedException ex)
		{
			getView().displayErrorMessage("The command could not be undone");
		}
		
		enableComponents();
		loadValues();
    }

    /**
     * This method is called when the user clicks the "Done" button in the add
     * item batch view.
     */
    @Override
    public void done() {
        try {
            if (this.addedItems.size() > 0) {
                String file = itemsFacade.printItems((List<Item>) this.addedItems);
                Desktop.getDesktop().open(new File(file));
            }
        } catch (IOException ex) {
            getView().displayErrorMessage("Cannot open file. "
                    + "Make sure that it is not currently open.");
        } catch (DocumentException ex) {
            getView().displayErrorMessage("Invalid writing permissions.");
        }
        getView().close();
    }

    private void loadProductValues() {
        ProductData selectedProduct = getView().getSelectedProduct();
        Iterator<Product> products = addedProducts.iterator();
        List<ProductData> productDatas = new ArrayList();
        while (products.hasNext()) {
            Product curProduct = products.next();
            ProductData curData = new ProductData();
            curData.setBarcode(curProduct.getBarcode().toString());
            curData.setDescription(curProduct.getProdDesc());
            curData.setShelfLife(curProduct.getShelfLifeString() + "");
            curData.setSize(curProduct.getSize().toString());
            curData.setSupply(curProduct.getMonthSupply().toString());
            curData.setTag(curProduct);

            Iterator<Item> items = addedItems.iterator();
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
        ProductData selectedProductData = getView().getSelectedProduct();
        if (selectedProductData == null) {
			getView().setItems(new ItemData[0]);
            return;
        }
        Product selectedProduct = (Product) selectedProductData.getTag();
		ItemData selectedItem = getView().getSelectedItem();
		
		Collections.sort(addedItems);
		
        Iterator<Item> items = addedItems.iterator();
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
                curData.setStorageUnit(containersFacade.
						getStorageUnit(curItem.getProductContainer()).getName());
				
                curData.setTag(curItem);

                itemDatas.add(curData);
				
				if (selectedItem != null && 
						selectedItem.getBarcode().equals(curItem.getBarcode().toString()))
				{
					selectedItem = curData;
				}
            }
			
			
        }		
        getView().setItems(itemDatas.toArray(new ItemData[0]));
		getView().selectItem(selectedItem);
    }
}
