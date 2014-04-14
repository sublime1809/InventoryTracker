package gui.common;

import java.awt.*;
import javax.swing.*;

/**
 * TableOperations provides useful utility functions for operating on
 * {@link javax.swing.JTable}s.
 */
public final class TableOperations {

	/**
	 * Private Constructor.
	 */
	private TableOperations() {
		assert false;
	}

	/**
	 * Selects the table row that occupies the specified (x,y) point.
	 * This is used to select the table row clicked on by the user.
	 * 
	 * @param table table object in which the mouse click occurred
	 * @param point coordinates of the mouse click event
	 * 
	 * {@pre table != null}
	 * {@pre point != null}
	 * 
	 * {@post The table row corresponding to the specified point is selected.
	 * If the specified point is not within the bounds of a table row,
	 * nothing is done.}
	 */
	public static void selectTableRow(JTable table, Point point) {
		int rowIndex = table.rowAtPoint(point);
		if (rowIndex >= 0) {
			selectTableRow(table, rowIndex);
		}
	}
	
	/**
	 * Selects a table row.
	 * 
	 * @param table table object in which selection is to be made
	 * @param rowIndex index of row to be selected
	 * 
	 * {@pre table != null}
	 * {@pre rowIndex >= 0 AND rowIndex < table.getRowCount()}
	 * 
	 * {@post The specified row in the table is selected.}
	 */
	public static void selectTableRow(final JTable table, final int rowIndex) {
		table.setRowSelectionInterval(rowIndex, rowIndex);
		table.setColumnSelectionInterval(0, table.getColumnCount() - 1);
	}

}


