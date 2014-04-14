package gui.common;

import gui.main.GUI;

/**
 * Base class for all views that are displayed in dialog boxes. 
 */
@SuppressWarnings("serial")
public class DialogView extends View {

	/**
	 * Dialog box that is displaying this view.
	 */
	protected DialogBox _dialog;
	
	/**
	 * Constructor.
	 * 
	 * @param parent Parent GUI object.
	 * @param dialog Dialog box that is displaying this view.
	 * 
	 * {@pre parent != null, dialog != null}
	 * 
	 * {@post DialogView has been initialized with the specified
	 * parent and dialog.} 
	 */
	public DialogView(GUI parent, DialogBox dialog) {
		super(parent);
		
		_dialog = dialog;
	}

}

