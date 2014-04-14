/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.Serializable;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

/**
 * @author Stephen Kitto
 * @author Trevor Burbidge
 * @author Christina Ramos
 * @author Leckie Gunter
 *
 * Helper class to hold Sizes for the Products, Items and ProductGroups
 */
public class Size implements Serializable
{

	private float size;
	private SizeUnit unit;

	/**
	 * This will create a new size object with the given size and unit
	 *
	 * @param _size
	 * @param _unit
	 *
	 */
	public Size(float _size, SizeUnit _unit)
	{
		this.size = _size;
		this.unit = _unit;
	}

	/**
	 * Getter for Size
	 *
	 * @return size of this object
	 */
	public float getSize()
	{
		return this.size;
	}

	/**
	 * Getter for Unit
	 *
	 * @return unit of this object
	 */
	public SizeUnit getUnit()
	{
		return this.unit;
	}

	/**
	 *
	 * @param _size
	 */
	public void setSize(float _size)
	{
		this.size = _size;
	}

	/**
	 *
	 * @param _unit
	 *
	 */
	public void setUnit(SizeUnit _unit)
	{
		this.unit = _unit;
	}
	
	@Override
	public String toString(){
		
		if((int) size == size) {
			return (int)size + " " + unit;
		}
		
		return size + " " + unit;
	}
	
	/**
	 * gets the value of the size
	 * @return	the value of the size
	 */
	public String getSizeValueString(){
		if((int) size == size) {
			return (int)size + "";
		}
		
		return size + "";
	}

    public String getUnitString() {
        //throw new UnsupportedOperationException("Not yet implemented");
        return ""+unit;
    }
}
