package gui.reports.removed;

import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gui.common.*;
import gui.main.*;

import common.util.*;

@SuppressWarnings("serial")
public class RemovedReportView extends DialogView implements IRemovedReportView {

	private JPanel _valuesPanel;
	private JLabel _formatLabel;
	private JComboBox _formatBox;
	private ButtonGroup _buttonGroup;
	private JRadioButton _sinceLastButton;
	private JRadioButton _sinceDateButton;
	private SpinnerModel _sinceDateSpinnerModel;
	private JSpinner.DateEditor _sinceDateSpinnerEditor;
	private JSpinner _sinceDateSpinner;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public RemovedReportView(GUI parent, DialogBox dialog) {
		super(parent, dialog);

		construct();

		_controller = new RemovedReportController(this);
	}

	@Override
	public IRemovedReportController getController() {
		return (IRemovedReportController) super.getController();
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
		_formatBox.setMaximumSize(_formatBox.getPreferredSize());
		_formatBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
			}
		});

		_sinceLastButton = new JRadioButton(
				"Since the last time I ran this report");
		_sinceLastButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
			}
		});

		_sinceDateButton = new JRadioButton("Since the following date:");
		_sinceDateButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
			}
		});

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils.earliestDate();

		_sinceDateSpinnerModel = new SpinnerDateModel(initDate, earliestDate,
				latestDate, Calendar.YEAR);
		_sinceDateSpinner = new JSpinner(_sinceDateSpinnerModel);
		_sinceDateSpinnerEditor = new JSpinner.DateEditor(_sinceDateSpinner,
				DateUtils.DATE_FORMAT);
		_sinceDateSpinner.setEditor(_sinceDateSpinnerEditor);
		_sinceDateSpinner.setMaximumSize(_sinceDateSpinner.getPreferredSize());
		_sinceDateSpinnerEditor.getTextField().getDocument()
				.addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent e) {
						return;
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						processChange(e);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						processChange(e);
					}

					private void processChange(DocumentEvent e) {
						if (eventsAreDisabled()) {
							return;
						}
						if (_sinceDateSpinnerEditor.getTextField().hasFocus()) {
							valuesChanged();
						}
					}
				});
//		_sinceDateSpinner.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent arg0) {
//				if (eventsAreDisabled()) {
//					return;
//				}
//				valuesChanged();
//			}
//		});

		_buttonGroup = new ButtonGroup();
		_buttonGroup.add(_sinceLastButton);
		_buttonGroup.add(_sinceDateButton);
	}

	private void createButtonsPanel() {
		_buttonsPanel = new ButtonBankPanel(new String[] { "OK", "Cancel" },
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
				});

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

		final int MARGIN_SPACING = 20;
		final int ROW_SPACING = 15;

		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new BoxLayout(formatPanel, BoxLayout.X_AXIS));
		formatPanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		formatPanel.add(_formatLabel);
		formatPanel.add(Box.createHorizontalStrut(5));
		formatPanel.add(_formatBox);
		formatPanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		formatPanel.add(Box.createHorizontalGlue());

		JPanel sinceLastPanel = new JPanel();
		sinceLastPanel
				.setLayout(new BoxLayout(sinceLastPanel, BoxLayout.X_AXIS));
		sinceLastPanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceLastPanel.add(_sinceLastButton);
		sinceLastPanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceLastPanel.add(Box.createHorizontalGlue());

		JPanel sinceDatePanel = new JPanel();
		sinceDatePanel
				.setLayout(new BoxLayout(sinceDatePanel, BoxLayout.X_AXIS));
		sinceDatePanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceDatePanel.add(_sinceDateButton);
		sinceDatePanel.add(Box.createHorizontalStrut(5));
		sinceDatePanel.add(_sinceDateSpinner);
		sinceDatePanel.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceDatePanel.add(Box.createHorizontalGlue());

		_valuesPanel.setLayout(new BoxLayout(_valuesPanel, BoxLayout.Y_AXIS));
		_valuesPanel.add(formatPanel);
		_valuesPanel.add(Box.createVerticalStrut(ROW_SPACING));
		_valuesPanel.add(sinceLastPanel);
		_valuesPanel.add(Box.createVerticalStrut(ROW_SPACING));
		_valuesPanel.add(sinceDatePanel);
	}

	@Override
	public FileFormat getFormat() {
		return (FileFormat) _formatBox.getSelectedItem();
	}

	@Override
	public void setFormat(FileFormat value) {
		boolean disabledEvents = disableEvents();
		try {
			_formatBox.setSelectedItem(value);
		} finally {
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
	public boolean getSinceLast() {
		return _sinceLastButton.isSelected();
	}

	@Override
	public void setSinceLast(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			_sinceLastButton.setSelected(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceLast(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			_sinceLastButton.setEnabled(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setSinceLastValue(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			String text = "Since the last time I ran this report";
			if (value != null) {
				text += "  [" + DateUtils.formatDateTime(value) + "]";
			}
			_sinceLastButton.setText(text);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public boolean getSinceDate() {
		return _sinceDateButton.isSelected();
	}

	@Override
	public void setSinceDate(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			_sinceDateButton.setSelected(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceDate(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			_sinceDateButton.setEnabled(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public Date getSinceDateValue() {

		// return (Date) _sinceDateSpinnerModel.getValue();
	
		String sinceDateText = _sinceDateSpinnerEditor.getTextField().getText();
		if (sinceDateText == null) {
			return null;
		}
		try {
			return DateUtils.parseDate(sinceDateText);
		}
		catch (ParseException e) {
			return null;
		}	
	}

	@Override
	public void setSinceDateValue(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			_sinceDateSpinnerModel.setValue(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceDateValue(boolean value) {
		_sinceDateSpinner.setEnabled(value);
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

