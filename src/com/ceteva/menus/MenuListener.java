package com.ceteva.menus;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving menu events.
 * The class that is interested in processing a menu
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addMenuListener<code> method. When
 * the menu event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MenuEvent
 */
public interface MenuListener {
	
	/**
	 * New menu added.
	 */
	public void newMenuAdded();

}
