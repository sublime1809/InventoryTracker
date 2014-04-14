package gui.common;

/**
 * Enumeration defining all the units of measurement (including weight,
 * volume, and count) supported by the program.
 */
public enum SizeUnits {
	
	// Weight units
	Pounds("pounds"),
	Ounces("ounces"),
	Grams("grams"),
	Kilograms("kilograms"),
	
	// Volume units
	Gallons("gallons"),
	Quarts("quarts"),
	Pints("pints"),
	FluidOunces("fluid ounces"),
	Liters("liters"),
	
	// Count units
	Count("count");
	
	/**
	 * String value to be returned by toString.
	 */
	private String _string;
	
	/**
	 * Constructor.
	 * 
	 * @param s String value to be returned by toString.
	 * 
	 * {@pre s != null}
	 * 
	 * {@post toString() == s}
	 */
	private SizeUnits(String s) {
		_string = s;
	}
	
	@Override
	public String toString() {
		return _string;
	}
	
}

