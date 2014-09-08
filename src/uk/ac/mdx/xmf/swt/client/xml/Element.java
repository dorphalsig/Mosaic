package uk.ac.mdx.xmf.swt.client.xml;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class Element.
 */
public class Element extends XML { // implements IElement {
	
	/** The name. */
 private String name;
	
	/** The attributes. */
	private Vector attributes = new Vector();
	
	/** The elements. */
	private Vector elements = new Vector();
	
	/**
	 * Adds the attribute.
	 *
	 * @param att the att
	 */
	public void addAttribute(Attribute att) {
		attributes.add(att);
	}
	
	/**
	 * Adds the child.
	 *
	 * @param index the index
	 * @param element the element
	 */
	public void addChild(int index,XML element) {
		if(element instanceof Element)
			elements.add(index,element);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.xml.XML#addChild(uk.ac.mdx.xmf.swt.client.xml.XML)
	 */
	public void addChild(XML element) {
		if(element instanceof Element)
			elements.add(element);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.xml.XML#addChildren(java.util.Vector)
	 */
	public void addChildren(Vector elements) {
		this.elements = elements;
	}
	
	/**
	 * Children size.
	 *
	 * @return the int
	 */
	public int childrenSize() {
		return elements.size();
	}
	
	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public Vector getAttributes() {
		return attributes;
	}
	
	/**
	 * Gets the boolean.
	 *
	 * @param name the name
	 * @return the boolean
	 */
	public boolean getBoolean(String name) {
		String value = getString(name);
		return value.toLowerCase().equals("true");
	}
	
	/**
	 * Gets the child.
	 *
	 * @param index the index
	 * @return the child
	 */
	public Element getChild(int index) {
		return (Element)elements.get(index);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.xml.XML#getChildren()
	 */
	public Vector getChildren() {
		return elements;
	}
	
	/**
	 * Gets the integer.
	 *
	 * @param name the name
	 * @return the integer
	 */
	public int getInteger(String name) {
		String value = getString(name);
		return Integer.parseInt(value);
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
	 * Gets the string.
	 *
	 * @param name the name
	 * @return the string
	 */
	public String getString(String name) {
		for(int i=0;i<attributes.size();i++) {
		  Attribute att = (Attribute)attributes.elementAt(i);
		  if(att.getName().equals(name))
		    return att.getValue();
		}
		System.err.println("Warning - attribute '" + name + "' cannot be found in element '" + this.name + "'");
		return "";
	}
	
	/**
	 * Checks for children.
	 *
	 * @return true, if successful
	 */
	public boolean hasChildren() {
		return !elements.isEmpty();
	}
	
	/**
	 * Checks for name.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean hasName(String name) {
		return getName().equals(name);
	}
	
	/**
	 * Prints the.
	 *
	 * @param indent the indent
	 * @param string the string
	 */
	public void print(int indent,String string) {
		for(int i=0;i<indent;i++)
		  System.err.print(" ");
		System.err.print(string + "\n");
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.xml.XML#printString(int)
	 */
	public void printString(int indent) {
		String start = "<" + getName();
		for(int i=0;i<attributes.size();i++) {
		  Attribute att = (Attribute)attributes.elementAt(i);
		  start = start + " " + att.getName() + " = " + att.getValue();	
		}
		start = start + ">";
		print(indent,start);
		for(int i=0;i<elements.size();i++) {
		  XML element = (XML)elements.elementAt(i);
		  element.printString(indent + 2);
		}
		print(indent,"</" + getName() + ">");
	}
	
	/**
	 * Removes the child.
	 *
	 * @param index the index
	 */
	public void removeChild(int index) {
		elements.remove(index);
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
