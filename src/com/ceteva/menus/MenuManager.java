package com.ceteva.menus;

import java.util.Hashtable;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuManager.
 */
public class MenuManager {
	
	/** The global menus. */
	private static Vector globalMenus = new Vector();
	
	/** The menu bindings. */
	private static Hashtable menuBindings = new Hashtable();
	
	/** The menu listeners. */
	private static Vector menuListeners = new Vector();
	
	/**
	 * Adds the global menu.
	 *
	 * @param identity the identity
	 */
	public static void addGlobalMenu(String identity) {
		GlobalMenu gm = new GlobalMenu(identity);
		globalMenus.addElement(gm);
	}
	
	/**
	 * Adds the menu.
	 *
	 * @param ownerId the owner id
	 * @param menuId the menu id
	 * @param name the name
	 * @param keybinding the keybinding
	 * @param supportsMulti the supports multi
	 * @param handlerPointId the handler point id
	 * @return true, if successful
	 */
	public static boolean addMenu(String ownerId,String menuId,String name,String keybinding,boolean supportsMulti,String handlerPointId) {
		Menu m = new Menu(menuId,name,supportsMulti);
		Menu parent = findMenu(ownerId);
		m.setHandlerPointId(handlerPointId);
		if(parent != null) {
		  parent.addMenu(m);
		  if(!keybinding.equals(""))
		    KeyBindingManager.addBinding(menuId,keybinding);
		  return true;
		}
		return false;
	}
	
	/**
	 * Adds the menu listener.
	 *
	 * @param listener the listener
	 */
	public static void addMenuListener(MenuListener listener) {
		if(!menuListeners.contains(listener))
		  menuListeners.add(listener);
	}
	
	/**
	 * Bind global menu.
	 *
	 * @param globalMenuId the global menu id
	 * @param objectId the object id
	 * @return true, if successful
	 */
	public static boolean bindGlobalMenu(String globalMenuId,String objectId) {
		if(findGlobalMenu(globalMenuId) != null) {
		  menuBindings.put(objectId,findMenu(globalMenuId));
		  menuAdded();
		  return true;
		}
		return false;
	}
	
	/**
	 * Menu added.
	 */
	public static void menuAdded() {
		for(int i=0;i<menuListeners.size();i++) {
		  MenuListener listener = (MenuListener)menuListeners.elementAt(i);
		  listener.newMenuAdded();
		}
		
	}
	
	/**
	 * Find menu.
	 *
	 * @param identity the identity
	 * @return the menu
	 */
	private static Menu findMenu(String identity) {
		for(int i=0;i<globalMenus.size();i++) {
		  Menu m = (Menu)globalMenus.elementAt(i);
		  Menu m2 = m.findMenu(identity);
		  if(m2 != null)
			return m2;
		}
		return null;
	}
	
	/**
	 * Find global menu.
	 *
	 * @param identity the identity
	 * @return the global menu
	 */
	private static GlobalMenu findGlobalMenu(String identity) {
		for(int i=0;i<globalMenus.size();i++) {
		  GlobalMenu gm = (GlobalMenu)globalMenus.elementAt(i);
		  if(gm.getIdentity().equals(identity))
		  	 return gm;
		}
		return null;
	}
	
	/**
	 * Gets the global menu.
	 *
	 * @param identity the identity
	 * @return the global menu
	 */
	public static GlobalMenu getGlobalMenu(String identity) {
		if(identity != null && menuBindings.containsKey(identity))
		  return (GlobalMenu)menuBindings.get(identity);
		//System.out.println("No binding for: " + identity);
		return null;
	}
	
	/**
	 * Removes the menu listener.
	 *
	 * @param listener the listener
	 */
	public static void removeMenuListener(MenuListener listener) {
		menuListeners.remove(listener);
	}

    /**
     * Delete global menu.
     *
     * @param identity the identity
     * @return true, if successful
     */
    public static boolean deleteGlobalMenu(String identity) {
		for(int i=0;i<globalMenus.size();i++) {
		  GlobalMenu gm = (GlobalMenu)globalMenus.elementAt(i);
		  if(gm.getIdentity().equals(identity)) {
		    globalMenus.remove(gm);
		    return true;
		  }
		}
	    return false;
    }

    /**
     * Delete menu.
     *
     * @param ownerId the owner id
     * @param menuId the menu id
     * @return true, if successful
     */
    public static boolean deleteMenu(String ownerId,String menuId) {
        Menu parent = findMenu(ownerId);
        Menu menu = findMenu(menuId);
        if(parent != null && menu != null) {
          parent.removeMenu(menu);
          return true;
        }
        return false;
    }
}