package gui.reports.removed;

import application.ItemsFacade;
import application.reports.builders.HTMLBuilder;
import application.reports.builders.PDFBuilder;
import application.reports.directors.RemovedReportDirector;
import application.storage.RemovedItemsManager;
import com.itextpdf.text.DocumentException;
import gui.common.*;

/**
 * Controller class for the removed items report view.
 */
public class RemovedReportController extends Controller implements
		IRemovedReportController {
	
	private ItemsFacade itemsFacade = ItemsFacade.getInstance();
	private RemovedItemsManager removedItems = RemovedItemsManager.getInstance();

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the removed items report view
	 */
	public RemovedReportController(IView view) {
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
	protected IRemovedReportView getView() {
		return (IRemovedReportView) super.getView();
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
		if(getView().getSinceLast()){
			getView().enableOK(true);
			getView().enableSinceDateValue(false);
		}
		else if(getView().getSinceDate()){
			getView().enableOK(itemsFacade.isValidDate(getView().getSinceDateValue()));
			getView().enableSinceDateValue(true);
		}
		else{
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
		getView().setSinceLast(true);
		getView().setSinceLastValue(removedItems.getLastReportRun());
	}

	//
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * removed items report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the user clicks the "OK"
	 * button in the removed items report view.
	 */
	@Override
	public void display() {
		RemovedReportDirector director = new RemovedReportDirector();
				
		FileFormat desiredFormat = getView().getFormat();
		if(desiredFormat == FileFormat.PDF){
			try {
				director.setBuilder(new PDFBuilder());
			} catch (DocumentException ex) {}
		}
		else if(desiredFormat == FileFormat.HTML){
			director.setBuilder(new HTMLBuilder());
		}
		
		if(getView().getSinceDate()){
			director.setDate(getView().getSinceDateValue());
		}
		else{
			director.setDate(removedItems.getLastReportRun());
		}
		director.buildReport();
	}

}

