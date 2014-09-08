package com.ceteva.forms;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class FormsStartup.
 */
public class FormsStartup implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		FormsClient client = new FormsClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.forms",client);
	}
}
