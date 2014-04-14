package gui.common;

/**
 * Controller is a base class that provides common functionality required
 * by all controllers in the program (i.e., all controller classes extend Controller).
 * <P>
 * The functionality provided by Controller to its subclasses includes:
 * <UL>
 * <LI>A reference to the controller's view object
 * <LI>An abstract method interface that must be supported by all subclasses
 * <LI>A template method that defines a standard algorithm for constructing 
 * subclass instances
 * </UL>
 * <P>
 * Subclasses should override the following methods:
 * <UL>
 * <LI>{@link #enableComponents() enableComponents}
 * <LI>{@link #loadValues() loadValues}
  * </UL>
 * <P>
 * Subclasses should also call {@link #construct() construct} from their
 * constructors.
 */public abstract class Controller implements IController {

	/**
	 * Reference to the view for this controller.
	 */
	protected IView _view;

	/**
	 * Constructor.
	 * 
	 * @param view The view for this controller
	 * 
	 * {@pre view != null}
	 * 
	 * {@post getView() == view}
	 */
	protected Controller(IView view) {
		this._view = view;
	}
	
	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a reference to the view for this controller.}
	 */
	protected IView getView() {
		return _view;
	}
	
	/**
	 * This is a template method that defines the construction process 
	 * for all subclasses.  Subclasses should call this method from their 
	 * constructors in order to properly initialize themselves.
	 * 
	 * {@pre None}
	 * 
	 * {@post The controller has loaded data into its view, and set the enable/disable
	 * state of its view's components}
	 */
	protected void construct() {
		loadValues();
		enableComponents();
	}
	
	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	protected void loadValues() {
		return;
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
	protected void enableComponents() {
		return;
	}
	
}

