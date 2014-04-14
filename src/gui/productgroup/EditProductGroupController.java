package gui.productgroup;

import application.ContainersFacade;
import application.storage.ProductContainer;
import application.storage.ProductGroup;
import common.Size;
import common.SizeUnit;
import gui.common.*;
import gui.inventory.*;

/**
 * Controller class for the edit product group view.
 */
public class EditProductGroupController extends Controller 
										implements IEditProductGroupController {
	
	private ContainersFacade containerFacade = ContainersFacade.getInstance();
	private ProductGroup targetGroup;
	/**
	 * Constructor.
	 * 
	 * @param view Reference to edit product group view
	 * @param target Product group being edited
	 */
	public EditProductGroupController(IView view, ProductContainerData target) {
		super(view);

		targetGroup = (ProductGroup)target.getTag();
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
	protected IEditProductGroupView getView() {
		return (IEditProductGroupView)super.getView();
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
		
		getView().enableOK(containerFacade.canEditProductContainer(targetGroup, getView().getProductGroupName()));
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
		getView().setProductGroupName(targetGroup.getName());
		getView().setSupplyUnit(DataGenerator.convertSizeUnit
				(targetGroup.getThreeMonthSupply().getUnit()));
		String supplyValue = Float.toString(targetGroup.getThreeMonthSupply().getSize());
		if((int)targetGroup.getThreeMonthSupply().getSize() == 
				targetGroup.getThreeMonthSupply().getSize())
		{
			getView().setSupplyValue(supplyValue.substring(0, supplyValue.indexOf('.')));
		}
		else
		{
			getView().setSupplyValue(supplyValue);
		}
	}

	//
	// IEditProductGroupController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * edit product group view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit product group view.
	 */
	@Override
	public void editProductGroup() {
		String name = getView().getProductGroupName();
		containerFacade.editProductContainer(targetGroup, name);
	}

}

