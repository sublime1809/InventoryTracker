package common;

//This is exactly the same as the class in gui.common but I thought it probably 

import java.io.Serializable;

// shouldn't only be for the gui but for all. I haven't taken it out of gui.common 
// yet because I wanted to leave it up for debate. 
/**
 * Enumeration defining all the units of measurement (including weight, volume, and count) supported
 * by the program.
 */
public enum SizeUnit implements Serializable
{

	// Weight units
	Pounds("pounds", 1, Type.WEIGHT),
	Ounces("ounces", 16, Type.WEIGHT),
	Grams("grams", 453.592, Type.WEIGHT),
	Kilograms("kilograms", 0.453592, Type.WEIGHT),
	// Volume units
	Gallons("gallons", 1, Type.VOLUME),
	Quarts("quarts", 4, Type.VOLUME),
	Pints("pints", 8, Type.VOLUME),
	FluidOunces("fluid ounces", 128, Type.VOLUME),
	Liters("liters", 3.78541, Type.VOLUME),
	// Count units
	Count("count", 1, Type.COUNT);
        
        enum Type {VOLUME, WEIGHT, COUNT};
        
	/**
	 * String value to be returned by toString.
	 */
	private String _string;
        private Type type;
        private double conversion;

	/**
	 * Constructor.
	 *
	 * @param s String value to be returned by toString.
	 *
	 * {
	 *
	 * @pre s != null}
	 *
	 * {
	 * @post toString() == s}
	 */
	private SizeUnit(String s, double conversion, Type type)
	{
		_string = s;
                this.conversion = conversion;
                this.type = type;
	}

	@Override
	public String toString()
	{
		return _string;
	}
	
	public boolean isCompatible(SizeUnit unit) {
		return this.type == unit.type;
	}

	/**
	 * This will give you the conversion factor for converting one unit to another
	 * @param unit - unit to convert to
	 * @return 
	 */
	public double convertTo(SizeUnit unit) {
		assert(isCompatible(unit));

		return this.conversion/unit.conversion;
	}
        
	/**
	 * Finds the type of SizeUnit you should use based on the string given
	 * @param s		the sizeunit you want in string form
	 * @return		the sizeuint you want in the from of a sizeunit.
	 */
	public static SizeUnit parse(String s)
	{
            assert(s != null);
            if(s.equals(FluidOunces.toString()))
                return FluidOunces;
            else if(s.equals(Gallons.toString()))
                return Gallons;
            else if(s.equals(SizeUnit.Grams.toString()))
                return Grams;
            else if(s.equals(SizeUnit.Kilograms.toString()))
                return Kilograms;
            else if(s.equals(SizeUnit.Liters.toString()))
                return Liters;
            else if(s.equals(SizeUnit.Ounces.toString()))
                return Ounces;
            else if(s.equals(SizeUnit.Pints.toString()))
                return Pints;
            else if(s.equals(SizeUnit.Pounds.toString()))
                return Pounds;
            else if(s.equals(SizeUnit.Quarts.toString()))
                return Quarts;
            return Count;
	}
	
	public static boolean isWeight(SizeUnit s){
		switch(s){
			case Pounds:
				return true;
			case Kilograms:
				return true;
			case Ounces:
				return true;
			case Grams:
				return true;
			default:
				return false;
		}
	}
	
	public static boolean isVolume(SizeUnit s){
		switch(s){
			case FluidOunces:
				return true;
			case Gallons:
				return true;
			case Quarts:
				return true;
			case Liters:
				return true;
			case Pints:
				return true;
			default:
				return false;
		}
	}
        
}
