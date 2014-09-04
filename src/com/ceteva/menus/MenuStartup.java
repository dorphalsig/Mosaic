package com.ceteva.menus;

import org.eclipse.ui.IStartup;

public class MenuStartup implements IStartup {
	public void earlyStartup() {
		MenusClient client = new MenusClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.menus",client);
	}
}
