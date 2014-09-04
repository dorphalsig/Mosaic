package com.ceteva.modelBrowser;

import org.eclipse.ui.IStartup;

public class ModelBrowserStart implements IStartup {
	public void earlyStartup() {
		ModelBrowserClient client = new ModelBrowserClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.modelBrowser",client);
	}
}
