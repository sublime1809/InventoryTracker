package gui.productgroup;

import application.ContainersFacade;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import common.Size;
import common.SizeUnit;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidContainerException;
import gui.common.*;
import gui.inventory.*;

/**
 * Controller class for the add product group view.
 */
public class AddProductGroupController extends Controller implements
		IAddProductGroupController {
	
    private ProductContainer parentContainer;
    private ContainersFacade containersFacade = ContainersFacade.getInstance();
	/**
	 * Constructor.
	 * 
	 * @param view Reference to add product group view
	 * @param container Product container to which the new product group is being added
	 */
	public AddProductGroupController(IView view, ProductContainerData container) {
		super(view);
                
		parentContainer = (ProductContainer)container.getTag();
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
	protected IAddProductGroupView getView() {
		return (IAddProductGroupView)super.getView();
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
		if(getView().getSupplyValue().equals("")){		// no value entered
			getView().enableOK(false);
			return;
		}
		else if(!Character.isDigit(getView().getSupplyValue().charAt(0))){	//value not a number
			getView().enableOK(false);
			return;
		}
		
		try
		{
			float curValue = Float.parseFloat(getView().getSupplyValue());
			if (getView().getSupplyUnit() == SizeUnits.Count && (int) curValue != curValue)
			{
				getView().enableOK(false);
				return;
			}

			ProductContainer newProductGroup =
					new ProductGroup(getView().getProductGroupName(), parentContainer);
			getView().enableOK(containersFacade.canAddProductContainer(newProductGroup, 
					parentContainer) && curValue >= 0);
		}
		catch (NumberFormatException ex)
		{
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
		getView().setSupplyValue("0");
	}

	//
	// IAddProductGroupController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * add product group view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add product group view.
	 */
	@Override
	public void addProductGroup() {
			SizeUnit unit = DataGenerator.convertSizeUnits(getView().getSupplyUnit());
			Size size = new Size(Float.valueOf(getView().getSupplyValue()), unit);
            ProductContainer newProductGroup = 
					new ProductGroup(getView().getProductGroupName(), parentContainer, size);
            
            try
            {
                containersFacade.addProductContainer(newProductGroup, parentContainer);
            }
            catch (InvalidAddException e)
            {
                _view.displayErrorMessage("Adding the Product Group was unsuccessful.");
            }
            catch (InvalidContainerException e)
            {
                _view.displayErrorMessage("The container(ProductGroup) was invalid.");
            }
        }   

}

