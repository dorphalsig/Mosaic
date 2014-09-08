package uk.ac.mdx.xmf.swt.client.xml;

// TODO: Auto-generated Javadoc
/**
 * The Class Attribute.
 */
public class Attribute {
	
	/** The name. */
	private String name = "";
	
	/** The value. */
	private String value = "";
	
	/**
	 * Instantiates a new attribute.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public Attribute(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
