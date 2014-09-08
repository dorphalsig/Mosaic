package com.ceteva.menus;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuStartup.
 */
public class MenuStartup implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		MenusClient client = new MenusClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.menus",client);
	}
}
