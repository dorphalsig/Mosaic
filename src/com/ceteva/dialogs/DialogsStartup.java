package com.ceteva.dialogs;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class DialogsStartup.
 */
public class DialogsStartup implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		DialogsClient client = new DialogsClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.dialogs",client);
	}
}
