package com.ceteva.menus;


// TODO: Auto-generated Javadoc
/**
 * The Class MenusPlugin.
 */
public class MenusPlugin
// extends AbstractUIPlugin implements org.eclipse.ui.IStartup
{

	/** The plugin. */
	private static MenusPlugin plugin;

	/**
	 * Instantiates a new menus plugin.
	 */
	public MenusPlugin() {
		plugin = this;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static MenusPlugin getDefault() {
		return plugin;
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		// MenusClient client = new MenusClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.menus",client);
	}
}
