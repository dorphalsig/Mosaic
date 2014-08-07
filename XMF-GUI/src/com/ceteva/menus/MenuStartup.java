package com.ceteva.menus;

import org.eclipse.ui.IStartup;

public class MenuStartup implements IStartup {
	public void earlyStartup() {
		MenusClient client = new MenusClient();
		// XmfPlugin.XOS.newMessageClient("com.ceteva.menus",client);
	}
}
