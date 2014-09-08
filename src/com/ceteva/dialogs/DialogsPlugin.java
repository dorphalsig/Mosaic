package com.ceteva.dialogs;


// TODO: Auto-generated Javadoc
/**
 * The Class DialogsPlugin.
 */
public class DialogsPlugin
// extends AbstractUIPlugin implements org.eclipse.ui.IStartup
{
	
	/** The plugin. */
	private static DialogsPlugin plugin;

	/**
	 * Instantiates a new dialogs plugin.
	 */
	public DialogsPlugin() {
		plugin = this;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static DialogsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		// DialogsClient client = new DialogsClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.dialogs",client);
	}
}
