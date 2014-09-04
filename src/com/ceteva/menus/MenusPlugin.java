package com.ceteva.menus;


public class MenusPlugin
// extends AbstractUIPlugin implements org.eclipse.ui.IStartup
{

	private static MenusPlugin plugin;

	public MenusPlugin() {
		plugin = this;
	}

	public static MenusPlugin getDefault() {
		return plugin;
	}

	public void earlyStartup() {
		// MenusClient client = new MenusClient();
		// XmfPlugin.XOS.newMessageClient("com.ceteva.menus",client);
	}
}
