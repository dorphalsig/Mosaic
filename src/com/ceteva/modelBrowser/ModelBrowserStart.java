package com.ceteva.modelBrowser;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelBrowserStart.
 */
public class ModelBrowserStart implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		ModelBrowserClient client = new ModelBrowserClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.modelBrowser",client);
	}
}
