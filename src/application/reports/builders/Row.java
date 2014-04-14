/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package application.reports.builders;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author owner
 */
public class Row {
    
    private List<String> information;
    
    public Row(List<String> info) {
        this.information = info;
    }
    
    public Row() {
        this.information = new ArrayList<String>();
    }
    
    public void addColumn(String info) {
        this.information.add(info);
    }
    
    public String getColumn(int i) {
        return this.information.get(i);
    }
    
    public Iterator<String> getColumns() {
        return this.information.iterator();
    }
    
    public int getSize() {
        return this.information.size();
    }
    
	public boolean equals(Row other)
	{
		for(int i = 0; i < this.information.size(); i++)
		{
			if(!information.get(i).equals(other.getColumn(i)))
			{
				return false;
			}
		}
		
		return true;
	}
}
