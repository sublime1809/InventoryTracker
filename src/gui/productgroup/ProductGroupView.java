package gui.productgroup;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import gui.common.*;
import gui.main.*;


@SuppressWarnings("serial")
public abstract class ProductGroupView extends DialogView {

	private JPanel _addPanel;
	private JLabel _nameLabel;
	private JTextField _nameField;
	private JLabel _supplyLabel;
	private JTextField _supplyField;
	private JComboBox _supplyBox;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;
	
	public ProductGroupView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
	}
	
	@Override
	protected void createComponents() {
		createAddPanel();
		createButtonsPanel();
	}
	
	private void createAddPanel() {
		KeyListener keyListener = new KeyListener() {
			
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
		};
		
		_addPanel = new JPanel();
		
		_nameLabel = new JLabel("Product Group Name:");
		
		_nameField = new JTextField(20);
		_nameField.addKeyListener(keyListener);

		_supplyLabel = new JLabel("3 Month Supply:");
		
		_supplyField = new JTextField(8);
		_supplyField.addKeyListener(keyListener);
		
		_supplyBox = new JComboBox();
		_supplyBox.addItem(SizeUnits.Count);
		_supplyBox.addItem(SizeUnits.Pounds);
		_supplyBox.addItem(SizeUnits.Ounces);
		_supplyBox.addItem(SizeUnits.Grams);
		_supplyBox.addItem(SizeUnits.Kilograms);
		_supplyBox.addItem(SizeUnits.Gallons);
		_supplyBox.addItem(SizeUnits.Quarts);
		_supplyBox.addItem(SizeUnits.Pints);
		_supplyBox.addItem(SizeUnits.FluidOunces);
		_supplyBox.addItem(SizeUnits.Liters);
		_supplyBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
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
		
		c.place(0, 1, 1, 1);
		_addPanel.add(_supplyLabel, c);
		
		c.place(1, 1, 1, 1);
		_addPanel.add(_supplyField, c);
		
		c.place(2, 1, 2, 1);
		_addPanel.add(_supplyBox, c);
	}
	
	public String getProductGroupName() {
		return _nameField.getText();
	}

	public void setProductGroupName(String value) {
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
	
	public void enableProductGroupName(boolean value) {
		_nameField.setEnabled(value);
	}

	public String getSupplyValue() {
		return _supplyField.getText();
	}

	public void setSupplyValue(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_supplyField.setText(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSupplyValue(boolean value) {
		_supplyField.setEnabled(value);
	}

	public SizeUnits getSupplyUnit() {
		return (SizeUnits)_supplyBox.getSelectedItem();
	}

	public void setSupplyUnit(SizeUnits value) {
		boolean disabledEvents = disableEvents();
		try {
			_supplyBox.setSelectedItem(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSupplyUnit(boolean value) {
		_supplyBox.setEnabled(value);
	}
	
	public void enableOK(boolean value) {
		_okButton.setEnabled(value);
	}

}

