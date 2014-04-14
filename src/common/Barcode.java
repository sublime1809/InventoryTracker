package common;

import common.exceptions.InvalidBarcodeException;
import common.exceptions.InsufficientBarcodesException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Set;

/**
 * Represents a barcode.
 *
 * @author Trevor
 */
public class Barcode implements Comparable<Barcode>, Serializable
{

	private static final int BARCODE_PREFIX = 4;
	private static final int MAX_BARCODE = 10000000;
	private String _barcode;
	// this is the last barcode so that we can continue from there
	private static int _last_base;

	/**
	 * Creates a new barcode with a random value.
	 */
	public Barcode()
	{
		this._barcode = generateValue();
	}
	
	/**
	 * Creates a new barcode with the given barcode
	 */
	public Barcode(Barcode other)
	{
		this._barcode = other._barcode;
	}
        
	public String toString(){
		return _barcode;
	}
        
	/**
	 * Creates a new barcode with the given barcode.
	 *
	 * @param barcode
	 */
	public Barcode(String barcode)
	{
		this._barcode = barcode;
	}

	private String generateValue()
	{
		int base = ++_last_base;
		int prefix = BARCODE_PREFIX;

		DecimalFormat format = new DecimalFormat("0000000000");

		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(format.format(base));

		int suffix = getLast(sb.toString());

		sb.append(suffix);

		return sb.toString();
	}

	/**
	 * Generates a random barcode
	 * @return	returns a barcode object
	 */
	public static Barcode generate()
	{
		int base = ++_last_base;
		int prefix = BARCODE_PREFIX;

		DecimalFormat format = new DecimalFormat("0000000000");

		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(format.format(base));

		int suffix = getLast(sb.toString());

		sb.append(suffix);
		return new Barcode(sb.toString());
	}

	/**
	 * generates a barcode 
	 * @param existing
	 * @return
	 * @throws InsufficientBarcodesException 
	 */
	public static Barcode generate(Set<Barcode> existing) throws InsufficientBarcodesException
	{
		Barcode barcode;
		int i = 0;

		do
		{
			barcode = Barcode.generate();
			i++;
		}
		while (existing.contains(barcode) && i < MAX_BARCODE);
		if (i <= MAX_BARCODE)
		{
			return barcode;
		}
		else
		{
			throw new InsufficientBarcodesException();
		}
	}

	/**
	 * checks to see if the given barcode is a valid item barcode
	 * @param barcode	the barcode in question
	 * @return			true if it is valid, false if not
	 */
	public static boolean isValid(Barcode barcode)
	{
		String value = barcode.getValue();
		String base = value.substring(0, value.length() - 1);

		int result = getLast(base);

		if (Integer.parseInt(Character.toString(value.charAt(value.length() - 1))) == result)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns the value of the barcode
	 * @return	the value of the barcode
	 */
	public String getValue()
	{
		return this._barcode;
	}

	/**
	 * Compares this barcode with the barcode passed in.
	 *
	 * @param other
	 *
	 * @return -1 if this barcode comes before the other barcode. 0 if the barcodes are the same. 1
	 *            if this barcode comes after the other.
	 */
	@Override
	public int compareTo(Barcode other)
	{
		return getValue().compareTo(other.getValue());
	}

	@Override
	public boolean equals(Object other)
	{
		assert (other instanceof Barcode);
		return this._barcode.equals(((Barcode) other).getValue());
	}

	@Override
	public int hashCode()
	{
		return this._barcode.hashCode();
	}

	// Helper methods //
	private static int getSum(String value, boolean odd)
	{
		int sum = 0;
		for (int i = 1; i <= value.length(); i++)
		{
			if (i % 2 == 0 && !odd)
			{
				sum += Integer.parseInt(Character.toString(value.charAt(i - 1)));
			}
			else
			{
				if (i % 2 == 1 && odd)
				{
					sum += Integer.parseInt(Character.toString(value.charAt(i - 1)));
				}
			}
		}
		return sum;
	}

	private static int getLast(String value)
	{
		int oddSums = getSum(value, true);
		int evenSums = getSum(value, false);

		int result = ((oddSums * 3) + evenSums) % 10;
		if (result != 0)
		{
			result = 10 - result;
		}
		return result;
	}
}
