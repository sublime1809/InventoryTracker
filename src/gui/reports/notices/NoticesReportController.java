package gui.reports.notices;

import application.reports.builders.HTMLBuilder;
import application.reports.builders.PDFBuilder;
import application.reports.directors.NoticesReportDirector;
import com.itextpdf.text.DocumentException;
import gui.common.*;

/**
 * Controller class for the notices report view.
 */
public class NoticesReportController extends Controller implements
		INoticesReportController {

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the notices report view
	 */	
	public NoticesReportController(IView view) {
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
	protected INoticesReportView getView() {
		return (INoticesReportView)super.getView();
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
	 * notices report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the notices report view.
	 */
	@Override
	public void display() {
		NoticesReportDirector director = new NoticesReportDirector();
		
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

