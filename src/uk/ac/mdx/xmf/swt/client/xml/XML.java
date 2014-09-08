package uk.ac.mdx.xmf.swt.client.xml;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class XML.
 */
public abstract class XML {
	
	/**
	 * Adds the child.
	 *
	 * @param element the element
	 */
	public abstract void addChild(XML element);
	
	/**
	 * Adds the children.
	 *
	 * @param elements the elements
	 */
	public abstract void addChildren(Vector elements);
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public abstract Vector getChildren();
	
	/**
	 * Prints the string.
	 */
	public void printString() {
		printString(0);
	}
	
	/**
	 * Prints the string.
	 *
	 * @param indent the indent
	 */
	public abstract void printString(int indent);
}
