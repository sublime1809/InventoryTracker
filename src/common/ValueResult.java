package common;

/**
 * Represents the result of an operation that returns a value.
 *
 * @param <T> Type of operation return value.
 */
public class ValueResult<T> extends Result
{

	/**
	 * Value attribute. Contains the return value of the operation.
	 */
	private T _value;

	/**
	 * Constructor.
	 *
	 * {
	 *
	 * @pre None}
	 *
	 * {
	 * @post getStatus() == false, getMessage() == "", getValue() == null}
	 */
	public ValueResult()
	{
		super();

		setValue(null);
	}

	/**
	 * Constructor.
	 *
	 * @param status Initial value of Status attribute.
	 *
	 * {
	 *
	 * @pre None}
	 *
	 * {
	 * @post getStatus() == status, getMessage() == "", getValue() == null}
	 */
	public ValueResult(boolean status)
	{
		super(status);

		setValue(null);
	}

	/**
	 * Constructor.
	 *
	 * @param status  Initial value of Status attribute.
	 * @param message Initial value of Message attribute.
	 *
	 * {
	 *
	 * @pre None}
	 *
	 * {
	 * @post getStatus() == status, getMessage() == message, getValue() == null}
	 */
	public ValueResult(boolean status, String message)
	{
		super(status, message);

		setValue(null);
	}

	/**
	 * Constructor.
	 *
	 * @param status  Initial value of Status attribute.
	 * @param message Initial value of Message attribute.
	 * @param value   Initial value of Value attribute.
	 *
	 * {
	 *
	 * @pre None}
	 *
	 * {
	 * @post getStatus() == status, getMessage() == message, getValue() == value}
	 */
	public ValueResult(boolean status, String message, T value)
	{
		super(status, message);

		setValue(value);
	}

	/**
	 * Copy Constructor.
	 *
	 * @param other Object to be copied.
	 *
	 * {
	 *
	 * @pre other != null}
	 *
	 * {
	 * @post this is a copy of other}
	 */
	public ValueResult(ValueResult<T> other)
	{
		super(other);

		setValue(other.getValue());
	}

	/**
	 * Sets value of Value attribute.
	 *
	 * @param value New value of Value attribute
	 *
	 * {
	 *
	 * @pre None}
	 *
	 * {
	 * @post getValue() == value}
	 */
	public void setValue(T value)
	{
		_value = value;
	}

	/**
	 * Returns value of Value attribute.
	 *
	 * {
	 *
	 * @return 
	 * @pre None}
	 *
	 * {
	 * @post Returns value of Value attribute.}
	 */
	public T getValue()
	{
		return _value;
	}
}
