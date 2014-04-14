package gui.storageunit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gui.common.*;
import gui.main.*;

@SuppressWarnings("serial")
public abstract class StorageUnitView extends DialogView {

	private JPanel _addPanel;
	private JLabel _nameLabel;
	private JTextField _nameField;		
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;
	
	public StorageUnitView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
	}
	
	@Override
	protected void createComponents() {
		createAddPanel();
		createButtonsPanel();
	}
	
	private void createAddPanel() {
		_addPanel = new JPanel();
		
		_nameLabel = new JLabel("Storage Unit Name:");
		
		_nameField = new JTextField(20);
		_nameField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				valuesChanged();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
		});
	}
	
	private void createButtonsPanel() {
		_buttonsPanel = new ButtonBankPanel(new String[]{"OK", "Cancel"},
				new ButtonBankListener() {
					public void buttonPressed(int index, String label) {
						switch (index) {
							case 0:
								ok();
								_dialog.dispose();
								break;
							case 1:
								cancel();
								_dialog.dispose();
								break;
							default:
								assert false;
								break;
						}
					}
				}
			);

		_okButton = _buttonsPanel.getButtons()[0];
		_dialog.getRootPane().setDefaultButton(_okButton);
	}
	
	protected abstract void valuesChanged();
	
	protected abstract void ok();
	
	protected abstract void cancel();

	@Override
	protected void layoutComponents() {
		layoutAddPanel();	

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(_addPanel);
		add(Box.createHorizontalStrut(5));
		add(_buttonsPanel);
	}

	private void layoutAddPanel() {
		_addPanel.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5,5,5,5);

		c.place(0, 0, 1, 1);
		_addPanel.add(_nameLabel, c);

		c.place(1, 0, 3, 1);
		_addPanel.add(_nameField, c);
	}
	

	public String getStorageUnitName() {
		return _nameField.getText();
	}

	public void setStorageUnitName(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_nameField.setText(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	public void enableStorageUnitName(boolean value) {
		_nameField.setEnabled(value);
	}
	
	public void enableOK(boolean value) {
		_okButton.setEnabled(value);
	}

}

