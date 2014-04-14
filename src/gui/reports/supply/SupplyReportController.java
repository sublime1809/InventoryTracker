package gui.reports.supply;

import application.reports.builders.HTMLBuilder;
import application.reports.builders.PDFBuilder;
import application.reports.directors.SupplyReportDirector;
import com.itextpdf.text.DocumentException;
import gui.common.*;

/**
 * Controller class for the N-month supply report view.
 */
	public class SupplyReportController extends Controller implements
		ISupplyReportController {
//	STUDENT-INCLUDE-BEGIN

        private int monthSupply = 0;
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the N-month supply report view
	 */	
	public SupplyReportController(IView view) {
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
	protected ISupplyReportView getView() {
		return (ISupplyReportView)super.getView();
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
            getView().enableOK(true);
            String tempSupply = getView().getMonths();
            if(tempSupply.isEmpty()){
                getView().enableOK(false);
                return;
            }
            try
            {
                monthSupply = Integer.parseInt(getView().getMonths());
            }
            catch(NumberFormatException e)
            {
                getView().enableOK(false);
                return;
            }
            if(monthSupply < 1 || monthSupply > 100)
            {
                getView().enableOK(false);
            }else{
                getView().enableOK(true);
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
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * N-month supply report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the N-month supply report view.
	 */
	@Override
	public void display() {
            
            SupplyReportDirector director = new SupplyReportDirector();
            
            FileFormat desiredFormat = getView().getFormat();
            if(desiredFormat == FileFormat.PDF){
                try {
                    director.setBuilder(new PDFBuilder());                 
                }catch(DocumentException e){}
            }
            else if(desiredFormat == FileFormat.HTML){
                director.setBuilder(new HTMLBuilder());
            }
            director.setMonthSupply(monthSupply);
            director.buildReport();
	}

}

