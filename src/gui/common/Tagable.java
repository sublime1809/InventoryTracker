package gui.common;


/**
 * 
 * Tagable is the base class for all classes used to pass data into views.
 * Tagable subclasses contain the data that will be displayed by views.
 * The Tag property is used to link Tagable objects back to the model objects
 * they represent.
 */
public class Tagable {

	/**
	 * Tag property.  Reference to a model object corresponding to this object.
	 */
	private Object _tag;
	
	/**
	 * Constructs an empty Tagable object.
	 * 
	 * {@pre None}
	 * 
	 * {@post getTag() == null}
	 */
	public Tagable() {
		_tag = null;
	}
	
	/**
	 * Sets the Tag property.
	 * 
	 * @param value new value for the Tag property
	 * 
	 * {@pre None}
	 * 
	 * {@post getTag() == value}
	 */
	public void setTag(Object value) {
		_tag = value;
	}
	
	/**
	 * Returns the current value of the Tag property.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the current value of the Tag property.}
	 */
	public Object getTag() {
		return _tag;
	}
	
	/**
	 * Returns true if and only if Tag is not null.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns true if and only if Tag is not null}
	 */
	public boolean hasTag() {
		return (_tag != null);
	}
	
}

