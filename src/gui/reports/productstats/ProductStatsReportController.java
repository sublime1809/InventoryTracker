package gui.reports.productstats;

import application.reports.builders.HTMLBuilder;
import application.reports.builders.PDFBuilder;
import application.reports.directors.StatisticsReportDirector;
import com.itextpdf.text.DocumentException;
import gui.common.*;

/**
 * Controller class for the product statistics report view.
 */
public class ProductStatsReportController extends Controller implements
		IProductStatsReportController {

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the item statistics report view
	 */
	public ProductStatsReportController(IView view) {
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
	protected IProductStatsReportView getView() {
		return (IProductStatsReportView)super.getView();
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
            try
            {
                int monthSupply = Integer.parseInt(getView().getMonths());
                //System.out.println(monthSupply);
                if(monthSupply >= 1 && monthSupply <= 100)
                {
                    getView().enableOK(true);
                }else{
                    getView().enableOK(false);
                }
            }
            catch(NumberFormatException e)
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
		getView().setMonths("3");
	}

	//
	// IProductStatsReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * product statistics report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the product statistics report view.
	 */
	@Override
	public void display() {
            try {
                if(this.getView().getMonths().isEmpty()) {
                    throw new NumberFormatException();
                }
                StatisticsReportDirector director = new 
						StatisticsReportDirector(Integer.parseInt(this.getView().getMonths()));
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
            } catch(NumberFormatException e) {
                this.getView().displayErrorMessage("Invalid Months");
            }
	}

}

