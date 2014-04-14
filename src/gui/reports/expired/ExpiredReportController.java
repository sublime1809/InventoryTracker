package gui.reports.expired;


import application.reports.builders.HTMLBuilder;
import application.reports.builders.PDFBuilder;
import application.reports.directors.ExpiredReportDirector;
import com.itextpdf.text.DocumentException;
import gui.common.*;


/**
 * Controller class for the expired items report view.
 */
public class ExpiredReportController extends Controller implements
										IExpiredReportController {

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the expired items report view
	 */
	public ExpiredReportController(IView view) {
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
	protected IExpiredReportView getView() {
		return (IExpiredReportView)super.getView();
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
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * expired items report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the expired items report view.
	 */
	@Override
	public void display() {
		ExpiredReportDirector director = new ExpiredReportDirector();
		
		FileFormat desiredFormat = getView().getFormat();
		if(desiredFormat == FileFormat.PDF){
			try {
				director.setBuilder(new PDFBuilder());
			} catch (DocumentException ex) {}
		}
		else if(desiredFormat == FileFormat.HTML){
			director.setBuilder(new HTMLBuilder());
		}
		
		director.buildReport();
	}

}

