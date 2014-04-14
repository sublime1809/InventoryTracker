package gui.batches;

import java.text.ParseException;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import common.util.DateUtils;

import gui.common.*;
import gui.inventory.*;
import gui.main.GUI;
import gui.product.*;

@SuppressWarnings("serial")
public class AddItemBatchView extends ItemBatchView implements
		IAddItemBatchView {

	private JLabel entryDateLabel;
	private SpinnerModel entryDateSpinnerModel;
	private JSpinner.DateEditor entryDateSpinnerEditor;
	private JSpinner entryDateSpinner;
	private JLabel countLabel;
	private JTextField countField;

	public AddItemBatchView(GUI parent, DialogBox dialog,
			ProductContainerData target) {
		super(parent, dialog);

		construct();

		_controller = new AddItemBatchController(this, target);
	}

	@Override
	protected void createComponents() {
		super.createComponents();

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils.earliestDate();

		entryDateLabel = new JLabel("Entry Date:");

		entryDateSpinnerModel = new SpinnerDateModel(initDate, earliestDate,
				latestDate, Calendar.YEAR);
		entryDateSpinner = new JSpinner(entryDateSpinnerModel);
		entryDateSpinnerEditor = new JSpinner.DateEditor(entryDateSpinner,
				DateUtils.DATE_FORMAT);
		entryDateSpinner.setEditor(entryDateSpinnerEditor);
		entryDateSpinnerEditor.getTextField().getDocument()
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
						if (entryDateSpinnerEditor.getTextField().hasFocus()) {
							entryDateChanged();
						}
					}
				});
//		entryDateSpinner.addChangeListener(new ChangeListener() {
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				if (eventsAreDisabled()) {
//					return;
//				}
//				entryDateChanged();
//			}
//		});

		countLabel = new JLabel("Count:");

		countField = new JTextField(5);
		countField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				countChanged();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
		});
	}

	@Override
	protected void layoutComponents() {
		batchPanel = new JPanel();
		batchPanel.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 1, 1, 1);
		batchPanel.add(entryDateLabel, c);

		c.place(1, 1, 2, 1);
		batchPanel.add(entryDateSpinner, c);

		c.place(3, 1, 1, 1);
		batchPanel.add(countLabel, c);

		c.place(4, 1, 1, 1);
		batchPanel.add(countField, c);

		c.place(0, 0, 1, 1);
		batchPanel.add(barcodeLabel, c);

		c.place(1, 0, 2, 1);
		batchPanel.add(barcodeField, c);

		c.place(3, 0, 2, 1);
		batchPanel.add(scannerBox, c);

		c.place(1, 2, 1, 1);
		batchPanel.add(itemActionButton, c);

		c.place(2, 2, 1, 1);
		batchPanel.add(undoButton, c);

		c.place(3, 2, 1, 1);
		batchPanel.add(redoButton, c);

		c.place(4, 2, 1, 1);
		batchPanel.add(doneButton, c);

		setMaximumSize(batchPanel);

		productPanel = new JPanel();
		productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));

		productPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		productPanel.add(batchPanel);
		productPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		productPanel.add(productTableScrollPane);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, productPanel,
				itemTableScrollPane);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(splitPane);
	}

	private void entryDateChanged() {
		getController().entryDateChanged();
	}

	private void countChanged() {
		getController().countChanged();
	}

	// //////////////////////////
	// ItemBatchView Overrides
	// //////////////////////////

	@Override
	protected String getBarcodeLabel() {
		return "Product Barcode:";
	}

	@Override
	public IAddItemBatchController getController() {
		return (IAddItemBatchController) super.getController();
	}

	@Override
	protected void done() {
		getController().done();
	}

	@Override
	protected void itemAction() {
		getController().addItem();
	}

	@Override
	protected String getItemActionName() {
		return "Add Item";
	}

	@Override
	protected void barcodeChanged() {
		getController().barcodeChanged();
	}

	@Override
	protected void useScannerChanged() {
		getController().useScannerChanged();
	}

	@Override
	protected void selectedProductChanged() {
		getController().selectedProductChanged();
	}

	@Override
	protected void selectedItemChanged() {
		return;
	}

	@Override
	protected void redo() {
		getController().redo();
	}

	@Override
	protected void undo() {
		getController().undo();
	}

	// //////////////////////////
	// IAddItemsView overrides
	// //////////////////////////

	@Override
	public void displayAddProductView() {
		DialogBox dialogBox = new DialogBox(_parent, "Add Product");
		AddProductView dialogView = new AddProductView(_parent, dialogBox,
				getBarcode());
		dialogBox.display(dialogView, false);
	}

	@Override
	public String getCount() {
		return countField.getText();
	}

	@Override
	public Date getEntryDate() {
		
		// return DateUtils.removeTimeFromDate((Date) entryDateSpinnerModel
		// 		.getValue());

		String entryDateText = entryDateSpinnerEditor.getTextField().getText();
		if (entryDateText == null) {
			return null;
		}
		try {
			return DateUtils.parseDate(entryDateText);
		}
		catch (ParseException e) {
			return null;
		}
	}

	@Override
	public void setCount(String value) {
		boolean disabledEvents = disableEvents();
		try {
			countField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setEntryDate(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			entryDateSpinnerModel.setValue(DateUtils.removeTimeFromDate(value));
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

}

