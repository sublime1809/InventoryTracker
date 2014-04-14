package gui.product;

import application.ProductFacade;
import application.product.Product;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.InvalidProductException;
import gui.common.*;

/**
 * Controller class for the edit product view.
 */
public class EditProductController extends Controller 
										implements IEditProductController {
	Product target;
        ProductData before;
        ProductFacade productFacade;
        
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the edit product view
	 * @param _target 
	 */
	public EditProductController(IView view, ProductData _target) {
		super(view);

		construct();
		before = _target;
		target = (Product)_target.getTag();
		this.getView().setBarcode(target.getBarcode().getValue());
		this.getView().enableBarcode(false);
		this.getView().setDescription(target.getProdDesc());
		this.getView().setShelfLife(Integer.toString(target.getShelfLife()));
		if(target.getSize().getSize() == 0.0){
			this.getView().setSizeValue("0");
		}
		else
		{
			this.getView().setSizeValue(target.getSize().getSizeValueString());
		}
		if(target.getMonthSupply().getSize() == 0.0){
			this.getView().setSupply("0");
		}
		else{
			this.getView().setSupply(target.getMonthSupply().getSizeValueString());
		}
		this.getView().setSizeUnit(DataGenerator.convertSizeUnit(target.getSize().getUnit()));

		productFacade = ProductFacade.getInstance();
		enableComponents();
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
	protected IEditProductView getView() {
		return (IEditProductView)super.getView();
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
            // enable ok only if description, shelf life, and 3 month supply to be valid
            try {
                if(getView().getDescription().length() > 0 
                        && Integer.valueOf(getView().getShelfLife()) >= 0 
                        && Integer.valueOf(getView().getSupply()) >= 0
						&& Integer.valueOf(getView().getSizeValue()) >= 0)
                {
                    getView().enableOK(true);
                } else {
                    getView().enableOK(false);
                }
                if(getView().getSizeUnit() != SizeUnits.Count) {
                    getView().enableSizeValue(true);
                } else {
					getView().setSizeValue("1");
                    getView().enableSizeValue(false);
                }
            } catch (NumberFormatException e) {
                getView().enableOK(false);
            }
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
	}

	//
	// IEditProductController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * edit product view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit product view.
	 */
	@Override
	public void editProduct() {
            
            try {            
                // update products attributes
                Product edited = new Product();
                edited.setBarcode(new Barcode(this.getView().getBarcode()));
                edited.setProdDesc(this.getView().getDescription());
                edited.setShelfLife(Integer.parseInt(this.getView().getShelfLife()));
                edited.setSize(new Size(Float.parseFloat(this.getView().getSizeValue()), 
						DataGenerator.convertSizeUnits(getView().getSizeUnit())));
                edited.setMonthSupply(new Size(Float.parseFloat(this.getView().getSupply()), 
						SizeUnit.Count));
                productFacade.editProduct((Product)before.getTag(), edited);
                before.setTag(edited);

                // re-sort product table to relect changes in the Product's Description

                // keep modified product the same

                // product's attributes are updated anywhere they appear in the UI

                // Edit Product View closes
            } catch (InvalidProductException ex) {
                getView().displayErrorMessage("Cannot edit product.");
            } 
	}

}

