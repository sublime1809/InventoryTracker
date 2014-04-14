
package gui.common;


/**
 * ButtonBankListener is the callback interface for {@link ButtonBankPanel}.
 *
 */
public interface ButtonBankListener {

	/**
	 * Callback method indicating that a button was pressed in the
	 * owning {@link ButtonBankPanel}.
	 *  
	 * @param index zero-based index of the button that was pressed
	 * @param label label of the button that was pressed
	 * 
	 * {@pre index is a valid button index}
	 * {@pre label is the label of the button at position index}
	 * 
	 * {@post Button press has been processed.}
	 */
	void buttonPressed(int index, String label);
}



