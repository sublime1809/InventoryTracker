package gui.product;

import application.ContainersFacade;
import application.ProductFacade;
import application.plugins.WebProduct;
import application.product.Product;
import common.Barcode;
import common.Size;
import common.SizeUnit;
import common.exceptions.InvalidProductException;
import gui.common.*;
import java.util.Date;

/**
 * Controller class for the add item view.
 */
public class AddProductController extends Controller implements
		IAddProductController {
	
	private ProductFacade productFacade = ProductFacade.getInstance();
    private ContainersFacade containerFacade = ContainersFacade.getInstance();
	private Product newProduct;
	private SizeUnits prevSizeUnit;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the add product view
	 * @param barcode Barcode for the product being added
	 */
	public AddProductController(IView view, String barcode) {
		super(view);
		
		newProduct = new Product();
		newProduct.setBarcode(new Barcode(barcode));
                
        // setting defaults
		newProduct.setMonthSupply(new Size((float)1, SizeUnit.Count));
		newProduct.setShelfLife(0);
                
		prevSizeUnit = SizeUnits.Count; 
		(new Thread(new ProductFinder())).start();
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
	protected IAddProductView getView() {
		return (IAddProductView)super.getView();
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
            getView().enableBarcode(false);
            
            // Determine if size should be enabled
            if(getView().getSizeUnit() == SizeUnits.Count) {
                getView().enableSizeValue(false);
            } else {
                getView().enableSizeValue(true);
            }
            try {
                if(getView().getDescription().length() > 0 
                        && isValidShelfLife()
                        && isValidSupply()
                        && isValidSize()) 
                {
                    getView().enableOK(true);
                } else {
                    getView().enableOK(false);
                }
            } catch(NumberFormatException e) {
                getView().enableOK(false);
            }
	}
	
	private boolean isValidShelfLife(){
		return (getView().getShelfLife().length() > 0 
				&& Integer.valueOf(getView().getShelfLife()) >= 0);
	}
	
	private boolean isValidSupply(){
		return (getView().getSupply().length() > 0 
				&& Integer.valueOf(getView().getSupply()) >= 0);
	}
	
	private boolean isValidSize(){
		return (getView().getSizeValue().length() > 0 
				&& Float.valueOf(getView().getSizeValue()) > 0);
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
		getView().setBarcode(newProduct.getBarcode().getValue());
                if(getView().getSizeUnit() == SizeUnits.Count){
                    getView().setSizeValue("1");
                }
                else if(prevSizeUnit == SizeUnits.Count){
                    getView().setSizeValue(Float.toString(newProduct.getSize().getSize()));
                }
		
		getView().setSupply("0");
		getView().setShelfLife(Integer.toString(newProduct.getShelfLife()));
	}

	//
	// IAddProductController overrides
	//
	
	/**
	 * This method is called when any of the fields in the
	 * add product view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		if(prevSizeUnit != getView().getSizeUnit())
		{
			if(getView().getSizeUnit() == SizeUnits.Count)
			{
				getView().setSizeValue("1");
			}
			else if(prevSizeUnit == SizeUnits.Count)
			{
				getView().setSizeValue("0");
			}
			
			prevSizeUnit = getView().getSizeUnit();
		}
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add product view.
	 */
	@Override
	public void addProduct() {
		
            try {
				Date today = new Date();
				Date toBePassedIn = new Date(today.getYear(), today.getMonth(), today.getDate());
            	newProduct = new Product(toBePassedIn, 
            			getView().getBarcode(), 
            			getView().getDescription(), 
            			Integer.parseInt(getView().getShelfLife()), 
            			Float.parseFloat(getView().getSupply()),
            			new Size(Float.parseFloat(getView().getSizeValue()), 
            			SizeUnit.parse(getView().getSizeUnit().toString())));
				
				if(productFacade.canAddProduct(newProduct))
				{
					productFacade.addProduct(newProduct, null);
				}
            } catch (InvalidProductException ex) {
                getView().displayErrorMessage("That product was invalid.");
            } 
	}

	private class ProductFinder implements Runnable{

		@Override
		public void run() {
			getView().setDescription("Identifying Product, please wait");
			getView().enableDescription(false);
			getView().enableOK(false);

			WebProduct product = productFacade.identifyProduct(newProduct.getBarcode().toString());
			
			if(product == null){
				getView().setDescription("");	
				getView().enableOK(false);		
			}
			else{
				getView().setDescription(product.getDescription());	
				getView().enableOK(true);			
			}
			getView().enableDescription(true);
		}
		
	}
}

