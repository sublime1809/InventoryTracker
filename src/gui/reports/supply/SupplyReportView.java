package gui.reports.supply;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import gui.common.*;
import gui.main.*;


@SuppressWarnings("serial")
public class SupplyReportView extends DialogView implements ISupplyReportView {

	private JPanel _valuesPanel;
	private JLabel _formatLabel;
	private JComboBox _formatBox;
	private JLabel _monthsLabel;
	private JTextField _monthsField;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;
	
	public SupplyReportView(GUI parent, DialogBox dialog) {
		super(parent, dialog);

		construct();
		
		_controller = new SupplyReportController(this);
	}

	@Override
	public ISupplyReportController getController() {
		return (ISupplyReportController)super.getController();
	}
	
	@Override
	protected void createComponents() {
		createValuesPanel();
		createButtonsPanel();
	}
	
	private void createValuesPanel() {
		_valuesPanel = new JPanel();
		
		_formatLabel = new JLabel("Format:");
		
		_formatBox = new JComboBox();
		_formatBox.addItem(FileFormat.PDF);
		_formatBox.addItem(FileFormat.HTML);
		_formatBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
			}
		});
		
		_monthsLabel = new JLabel("Months:");
		
		_monthsField = new JTextField(4);
		_monthsField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				return;
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
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

	@Override
	protected void layoutComponents() {
		layoutValuesPanel();	

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(15));
		add(_valuesPanel);
		add(Box.createVerticalStrut(15));
		add(_buttonsPanel);
		add(Box.createVerticalStrut(15));
	}

	private void layoutValuesPanel() {
		_valuesPanel.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5,5,5,5);

		c.place(0, 0, 1, 1);
		_valuesPanel.add(Box.createHorizontalStrut(20), c);

		c.place(1, 0, 1, 1);
		_valuesPanel.add(_formatLabel, c);

		c.place(2, 0, 3, 1);
		_valuesPanel.add(_formatBox, c);

		c.place(5, 0, 1, 1);
		_valuesPanel.add(Box.createHorizontalStrut(20), c);

		c.place(1, 1, 1, 1);
		_valuesPanel.add(_monthsLabel, c);

		c.place(2, 1, 1, 1);
		_valuesPanel.add(_monthsField, c);
	}
	
	@Override
	public FileFormat getFormat() {
		return (FileFormat)_formatBox.getSelectedItem();
	}

	@Override
	public void setFormat(FileFormat value) {
		boolean disabledEvents = disableEvents();
		try {
			_formatBox.setSelectedItem(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	@Override
	public void enableFormat(boolean value) {
		_formatBox.setEnabled(value);
	}

	@Override
	public String getMonths() {
		return _monthsField.getText();
	}

	@Override
	public void setMonths(String value) {
		_monthsField.setText(value);
	}

	@Override
	public void enableMonths(boolean value) {
		_monthsField.setEnabled(value);
	}
	
	@Override
	public void enableOK(boolean value) {
		_okButton.setEnabled(value);
	}

	private void valuesChanged() {
		getController().valuesChanged();
	}
	
	private void cancel() {
		return;
	}
	
	private void ok() {
		getController().display();
	}

}

