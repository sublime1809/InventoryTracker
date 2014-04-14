package gui.item;

import application.ItemsFacade;
import application.item.Item;
import application.product.Product;
import common.Barcode;
import gui.common.*;

/**
 * Controller class for the edit item view.
 */
public class EditItemController extends Controller 
										implements IEditItemController {
    private ItemData target;
    private ItemsFacade itemsFacade = ItemsFacade.getInstance();
    private Item currentItem;
    private Product currentProduct;
    
	/**
	 * Constructor.
	 * 
	 * @param view Reference to edit item view
	 * @param _target 
	 */
	public EditItemController(IView view, ItemData _target) {
		super(view);

                target = _target;
		construct();
                
          
	}

	//
	// Controller overrides
	//
	
	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a reference to the view for this controller.}
	 */
	@Override
	protected IEditItemView getView() {
		return (IEditItemView)super.getView();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently
	 * allowed to interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's view
	 * have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
                      
            getView().enableOK(itemsFacade.isValidDate(getView().getEntryDate()));
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {
            Barcode barcode = new Barcode(target.getBarcode());
            currentItem = itemsFacade.getItem(barcode);
            currentProduct = itemsFacade.getProduct(currentItem);
            this.getView().setDescription(currentProduct.getProdDesc());
            this.getView().enableDescription(false);
            this.getView().setBarcode(target.getBarcode());
            this.getView().enableBarcode(false);
            this.getView().setEntryDate(target.getEntryDate());
            
	}

	//
	// IEditItemController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * edit item view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit item view.
	 */
	@Override
	public void editItem() {
		itemsFacade.editItem(currentItem, getView().getEntryDate());
        //currentItem.setEntryDate(getView().getEntryDate());
	}

}

