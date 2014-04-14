package gui.storageunit;

import application.ContainersFacade;
import application.storage.ProductContainer;
import application.storage.StorageUnit;
import common.exceptions.InvalidAddException;
import common.exceptions.InvalidContainerException;
import gui.common.*;

/**
 * Controller class for the add storage unit view.
 */
public class AddStorageUnitController extends Controller implements
		IAddStorageUnitController {
	
	private ContainersFacade containersFacade = ContainersFacade.getInstance();
	/**
	 * Constructor.
	 * 
	 * @param view Reference to add storage unit view
	 */
	public AddStorageUnitController(IView view) {
		super(view);
		
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
	protected IAddStorageUnitView getView() {
		return (IAddStorageUnitView)super.getView();
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
		
		ProductContainer newStorageUnit = new StorageUnit(getView().getStorageUnitName(), null);
		getView().enableOK(containersFacade.canAddProductContainer(newStorageUnit, null));
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
	// IAddStorageUnitController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * add storage unit view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();		
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add storage unit view.
	 */
	@Override
	public void addStorageUnit() {
		ProductContainer newStorageUnit = new StorageUnit(getView().getStorageUnitName(), null);
		try
		{
			containersFacade.addProductContainer(newStorageUnit, null);
		}
		catch (InvalidAddException e)
		{
			_view.displayErrorMessage("The adding was unsuccessful.");
		}
		catch (InvalidContainerException e)
		{
			_view.displayErrorMessage("The container was invalid.");
		}
	}

}

