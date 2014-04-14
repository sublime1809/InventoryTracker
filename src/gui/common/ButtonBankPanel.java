
package gui.common;

import javax.swing.*;
import java.awt.event.*;


/**
 * ButtonBankPanel can be used to easily create a horizontal bank of buttons, 
 * and to respond to the action events generated when the buttons are pressed.
 */
@SuppressWarnings("serial")
public class ButtonBankPanel extends JPanel {

	/**
	 * Listener object that is notified when one of the buttons is pressed
	 */
	private ButtonBankListener _listener;
	
	/**
	 * List of labels for the buttons in the button bank
	 */
	private String[] _labels;
	
	/**
	 * List of buttons in the bank
	 */
	private JButton[] _buttons;

	/**
	 * Constructs a new ButtonBankPanel with the specified buttons and listener,
	 * and lays the buttons out horizontally.
	 * 
	 * @param labels list of labels for the buttons to be created
	 * @param listener listener object to be notified when one of the 
	 * 	buttons is pressed
	 * 
	 * {@pre labels != null AND labels.length > 0}
	 * {@pre listener != null}
	 * 
	 * {@post ButtonBankPanel with the specified buttons and listener has been
	 *  initialized}
	 * {@post Buttons have been lain out horizontally}
	 * {@post labels array has been cloned to avoid sharing with the caller}
	 */
	public ButtonBankPanel(String[] labels, ButtonBankListener listener) {
		this._labels = labels.clone();
		this._listener = listener;

		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		createComponents();
		layoutComponents();
	}

	/**
	 * Creates the buttons.
	 */
	private void createComponents() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (int i=0; i < _buttons.length; ++i) {
					if (evt.getSource() == _buttons[i]) {
						_listener.buttonPressed(i, _labels[i]);
						break;
					}
				}
			}			
		};	
		_buttons = new JButton[_labels.length];
		for (int i=0; i < _labels.length; ++i) {
			_buttons[i] = new JButton(_labels[i]);
			_buttons[i].addActionListener(actionListener);
		}
	}

	/**
	 * Lays out the buttons in the panel.
	 */
	private void layoutComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		add(Box.createHorizontalGlue());

		for (int i=0; i < _buttons.length; ++i) {
			if (i > 0) {
				add(Box.createHorizontalStrut(5));
			}
			add(_buttons[i]);
		}
		
		add(Box.createHorizontalGlue());
	}
	
	/**
	 * Returns the buttons in the panel.
	 */
	public JButton[] getButtons() {
		return _buttons;
	}

}



