/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author owner
 */
public class StubBuilder implements ReportBuilder {
	
	private List<Row> _rows;
    
    @Override
    public void buildTable(int columns, List<String> headers, List<Row> rows) {
        this._rows = rows;
    }

    @Override
    public void buildList(List<String> items) {
    }
    
    @Override
    public void buildList(List<String> items, BulletType type) {
    }

    @Override
    public void addTitle(FontSize size, String title) {
    }

    @Override
    public void addTitle(FontSize size, String title, Alignment align) {
    }

    @Override
    public void addParagraph(String paragraph) {
    }

    @Override
    public String printReport() {
		return "";
    }

    @Override
    public String getContent() {
		return "";
    }
    
    public List<Row> getRows() {
        return _rows;
    }
	
	
    
}
