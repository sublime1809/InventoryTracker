package gui.common;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import gui.main.GUI;

/**
 * View is a base class that provides common functionality required
 * by all views in the program (i.e., view classes extend View 
 * instead of {@link javax.swing.JPanel JPanel}).
 * <P>
 * The functionality provided by View to its subclasses includes:
 * <UL>
 * <LI>A reference to the owning {@link gui.main.GUI GUI} object
 * <LI>A reference to the view's controller
 * <LI>A facility for turning off event notifications when
 * user interface components are being programmatically updated
 * <LI>Methods for displaying various types of messages to users
 * <LI>Utility methods for creating fonts, borders, and other UI objects.
 * <LI>An abstract method interface that must be supported by all subclasses
 * <LI>A template method that defines a standard algorithm for constructing 
 * subclass instances
 * </UL>
 * <P>
 * Subclasses should override the following methods:
 * <UL>
 * <LI>{@link #createComponents() createComponents}
 * <LI>{@link #layoutComponents() layoutComponents}
  * </UL>
 * <P>
 * Subclasses should also call {@link #construct() construct} from their
 * constructors.
 * Subclass constructors should also initialize the view's controller; 
 */
public abstract class View extends JPanel implements IView {
	
	public static final int MenuFontSize = 14;
	public static final int BorderFontSize = 14;
	public static final int ContentFontSize = 12;
	public static final int ContextInfoFontSize = 14;
	public static final Color TableColumnHeaderColor = new Color(153, 204, 255);

	/**
	 * Owning {@link gui.main.GUI GUI} object.
	 */
	protected GUI _parent;
	
	/**
	 * Controller for this view.
	 */
	protected IController _controller;

	/**
	 * Flag indicating whether event notifications are currently disabled
	 */
	private boolean _eventsDisabled;
	
	/**
	 * Constructor.
	 * 
	 * @param parent Owning {@link gui.main.GUI GUI} object.
	 * 
	 * {@pre parent != null}
	 * 
	 * {@post The View has been initialized with the specified parent.}
	 */
	protected View(GUI parent) {
		this._parent = parent;
		this._controller = null;
	}
	
	/**
	 * Returns the controller for this view.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the controller for this view}
	 */
	public IController getController() {
		return _controller;
	}
	
	/**
	 * Creates a font.
	 * 
	 * @param prototype Prototype font on which the new font should be based.
	 * @param size Point size of the new font.
	 * 
	 * {@pre prototype != null, size > 0}
	 * 
	 * {@post Returns a font based on the specified prototype and size.}
	 */
	public static Font createFont(Font prototype, int size) {
		return new Font(prototype.getName(), prototype.getStyle(), size);
	}
	
	/**
	 * Creates a titled border.
	 * 
	 * @param borderTitle Title text for the border.
	 * @param size Font size for the title text.
	 * 
	 * {@pre borderTitle != null, int size > 0}
	 * 
	 * {@post Returns a new titled border with the specified title text
	 * and font size.}
	 */
	public static TitledBorder createTitledBorder(String borderTitle, int size) {
		TitledBorder border = BorderFactory.createTitledBorder(borderTitle);
		border.setTitleFont(createFont(UIManager.getFont("TitledBorder.font") , size));
		return border;
	}
	
	/**
	 * Creates a table column.
	 * 
	 * @param columnIndex Index for the new table column.
	 * @param columnTitle Title text for the new table column. 
	 * @param size font size for the column title. 
	 * 
	 * {@pre columnIndex >= 0, columnTitle != null, size > 0}
	 * 
	 * {@post Returns a new table column with the specified column index,
	 * title, and title font size.}
	 */
	public static TableColumn createTableColumn(int columnIndex, String columnTitle, int size) {
		Font DefaultFont = new JTable().getFont();
		TableColumn column = new TableColumn(columnIndex);
		column.setHeaderRenderer(new TableHeaderRenderer(createFont(DefaultFont, size)));
		column.setCellRenderer(new StringCellRenderer(createFont(DefaultFont, size)));
		column.setHeaderValue(columnTitle);
		return column;
	}


	/**
	 * Table cell renderer used to draw table cells.
	 */
	@SuppressWarnings("serial") 
	private static class StringCellRenderer extends DefaultTableCellRenderer {
		
		/**
		 * Font to be used to draw table cells.
		 */
		private Font _customFont;

		/**
		 * Constructor.
		 * 
		 * @param customFont Font to be used to draw table cells.
		 * 
		 * {@pre customFont != null}
		 * 
		 * {@post StringCellRenderer has been initialized with the
		 * specified custom font.}
		 */
		public StringCellRenderer(Font customFont) {
			this._customFont = customFont;
			setFont(customFont);
		}

		/**
		 * Returns the custom font used to draw table cells.
		 */
		@Override
		public Font getFont() {
			return _customFont;
		}
	}
	
	
	/**
	 * Table cell renderer used to draw table headers.
	 */	
	@SuppressWarnings("serial") 
	private static class TableHeaderRenderer extends StringCellRenderer {

		/**
		 * Constructor.
		 * 
		 * @param customFont Custom font to be used to draw table headers.
		 * 
		 * {@pre customFont != null}
		 * 
		 * {@post TableHeaderRenderer has been initialized with the
		 * specified custom font.}
		 */
		public TableHeaderRenderer(Font customFont) {
			super(customFont);
		}
		
		/**
		 * Returns the background color used to draw table headers.
		 */
		@Override
		public Color getBackground() {
			return TableColumnHeaderColor;
		}
	}


//	@SuppressWarnings("serial") 
//	private static class NumberCellRenderer extends StringCellRenderer {
//		
//		public NumberCellRenderer(Font customFont) {
//			super(customFont);
//			setHorizontalAlignment(SwingConstants.RIGHT);
//		}
//	} 
	
	//
	// Abstract subclass interface
	//
	
	/**
	 * This is a template method that defines the construction process 
	 * for all subclasses.  Subclasses should call this method from their 
	 * constructors in order to properly initialize themselves.
	 * 
	 * {@pre None}
	 * 
	 * {@post The components for this view have been created and lain out
	 * on the panel.}
	 */
	protected void construct() {
		createComponents();
		layoutComponents();
	}
	
	/**
	 * Creates and initializes the components that populate this view.
	 * This method should only create/initialize the component objects, but not actually
	 * add them to the panel.  Laying out the components on the panel is done by 
	 * the {@link #layoutComponents() layoutComponents} method.
	 * 
	 * {@pre None}
	 * 
	 * {@post The components for this view have been created}
	 */
	protected void createComponents() {
		return;
	}
	
	/**
	 * Lays out components on the view.
	 * 
	 * {@pre None}
	 * 
	 * {@post The components for this view have been lain out on the panel}
	 */
	protected void layoutComponents() {
		return;
	}
 	
	//
	// Event enable/disable routines
	//
	
	/**
	 * Disables events in this View if they are not already disabled.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if events were disabled.  Returns false if
	 * events were already disabled.}
	 */
	protected boolean disableEvents() {
		if (_eventsDisabled) {
			return false;
		}
		else {
			_eventsDisabled = true;
			return true;
		}
	}
	
	/**
	 * Enables events in this View.
	 * 
	 * {@pre eventsAreDisabled() == true}
	 * 
	 * {@post Returns true if events were enabled.  Returns false if
	 * events were already enabled.}
	 */
	protected boolean enableEvents() {
		if (_eventsDisabled) {
			_eventsDisabled = false;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Indicates whether or not events are DISABLED in this View.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if events are disabled.  Returns false
	 * if events are enabled.}
	 */
	protected boolean eventsAreDisabled() {
		return _eventsDisabled;
	}

	/**
	 * {@inheritDoc}
	 */
	public void displayErrorMessage(String message) {
		if (_parent != null)
			_parent.displayErrorMessage(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void displayInformationMessage(String message) {
		if (_parent != null)
			_parent.displayInformationMessage(message);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void displayWarningMessage(String message) {
		if (_parent != null)
			_parent.displayWarningMessage(message);
	}

}

