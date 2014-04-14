package common;

/**
 * Utility class providing String operations.
 */
public final class StringOps
{

	/**
	 * Private Constructor.
	 */
	private StringOps()
	{
		assert false;
	}

	/**
	 * Returns true if string is null or empty, and false otherwise.
	 *
	 * @param s String to be tested.
	 *
	 * {
	 *
	 * @return 
	 * @pre s != null}
	 *
	 * {
	 * @post Returns true if string is null or empty, and false otherwise.}
	 */
	public static boolean isNullOrEmpty(String s)
	{
		return ((s == null) || (s.length() == 0));
	}
}
