package com.ceteva.menus;

import java.util.Hashtable;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyBindingManager.
 */
public class KeyBindingManager {
	
	/** The keybindings. */
	private static Hashtable keybindings = new Hashtable();
	
	/**
	 * Adds the binding.
	 *
	 * @param identity the identity
	 * @param keybinding the keybinding
	 */
	public static void addBinding(String identity,String keybinding) {
		keybindings.put(identity,keybinding);
	}
	
	/**
	 * Gets the binding.
	 *
	 * @param identity the identity
	 * @return the binding
	 */
	public static String getBinding(String identity) {
		return (String)keybindings.get(identity);
	}
	
	/**
	 * Checks for binding.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public static boolean hasBinding(String identity) {
		return keybindings.containsKey(identity);
	}

}
