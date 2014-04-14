package gui.common;

/**
 * IView defines functionality that is supported by all views in the program.
 * Controllers invoke this functionality on their views.
 */
public interface IView {
	
	/**
	 * Displays an "information" message to the user.
	 * 
	 * @param message message text
	 * 
	 * {@pre message != null}
	 * 
	 * {@post The user has viewed the information message.}
	 */
	void displayInformationMessage(String message);

	/**
	 * Displays a "warning" message to the user.
	 * 
	 * @param message message text
	 * 
	 * {@pre message != null}
	 * 
	 * {@post The user has viewed the warning message.}
	 */
	void displayWarningMessage(String message);
	
	/**
	 * Displays an "error" message to the user.
	 * 
	 * @param message message text
	 * 
	 * {@pre message != null}
	 * 
	 * {@post The user has viewed the error message.}
	 */
	void displayErrorMessage(String message);
}

