package com.ceteva.menus;

import java.util.Vector;

import org.eclipse.swt.graphics.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class Menu.
 */
public class Menu {
	
	/** The identities. */
	private Vector identities;
	
	/** The name. */
	private String name;
	
	/** The handler point id. */
	private String handlerPointId;
	
	/** The enabled. */
	private boolean enabled = true;
	
	/** The supports multi. */
	private boolean supportsMulti = true;
	
	/** The icon. */
	private Image icon = null;
	
	/** The menus. */
	private Vector menus = new Vector();
	
	/**
	 * Instantiates a new menu.
	 *
	 * @param identity the identity
	 * @param name the name
	 */
	public Menu(String identity,String name) {
		Vector v = new Vector();
		v.addElement(identity);
		this.identities = v;
		this.name = name;
	}	
	
	/**
	 * Instantiates a new menu.
	 *
	 * @param identity the identity
	 * @param name the name
	 * @param supportsMulti the supports multi
	 */
	public Menu(String identity,String name,boolean supportsMulti) {
		Vector v = new Vector();
		v.addElement(identity);
		this.identities = v;
		this.name = name;
		this.supportsMulti = supportsMulti;
	}
	
	/**
	 * Instantiates a new menu.
	 *
	 * @param identities the identities
	 * @param name the name
	 */
	public Menu(Vector identities,String name) {
		this.identities = identities;
		this.name = name;
	}
	
	/**
	 * Adds the menu.
	 *
	 * @param menu the menu
	 */
	public void addMenu(Menu menu) {
		menus.addElement(menu);
	}
	
	/**
	 * Find menu.
	 *
	 * @param identity the identity
	 * @return the menu
	 */
	public Menu findMenu(String identity) {
		if(getIdentity().equals(identity))
		  return this;
		else {
		  for(int i=0;i<menus.size();i++) {
		  	Menu m = (Menu)menus.elementAt(i);
		  	Menu m2 = m.findMenu(identity);
		  	if(m2 != null)
		  	  return m2;
		  }
		}
		return null;
	}
	
	/**
	 * Gets the enabled.
	 *
	 * @return the enabled
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Gets the handler point id.
	 *
	 * @return the handler point id
	 */
	public String getHandlerPointId() {
		return handlerPointId;
	}
	
	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return (String)identities.elementAt(0);
	}
	
	/**
	 * Gets the identities.
	 *
	 * @return the identities
	 */
	public Vector getIdentities() {
		return identities;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * Gets the menu.
	 *
	 * @param name the name
	 * @return the menu
	 */
	public Menu getMenu(String name) {
		for(int i=0;i<menus.size();i++) {
		  com.ceteva.menus.Menu m = (com.ceteva.menus.Menu)menus.elementAt(i);
		  if(name.equals(m.getName()))
		    return m;
		}
		return null;
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
	 * Gets the sub menus.
	 *
	 * @return the sub menus
	 */
	public Vector getSubMenus() {
		return menus;
	}
	
	/**
	 * Checks for icon.
	 *
	 * @return true, if successful
	 */
	public boolean hasIcon() {
		return icon != null;
	}
	
	/**
	 * Checks for sub menu.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	public boolean hasSubMenu(String name) {
		for(int i=0;i<menus.size();i++) {
			com.ceteva.menus.Menu menu = (com.ceteva.menus.Menu)menus.elementAt(i);
			if(name.equals(menu.getName()))
			  return true;
		}
		return false;
	}
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Checks if is parent.
	 *
	 * @return true, if is parent
	 */
	public boolean isParent() {
		return !menus.isEmpty();
	}

    /**
     * Removes the menu.
     *
     * @param menu the menu
     */
    public void removeMenu(Menu menu) {
        menus.removeElement(menu);
    }
    
	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Sets the handler point id.
	 *
	 * @param handlerPointId the new handler point id
	 */
	public void setHandlerPointId(String handlerPointId) {
		this.handlerPointId = handlerPointId;
	}	
	
	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	/**
	 * Supports multi.
	 *
	 * @return true, if successful
	 */
	public boolean supportsMulti() {
		return supportsMulti;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
	   String s = "Menu(" + name + ", " + identities + ")";
	   return s;
	}

}