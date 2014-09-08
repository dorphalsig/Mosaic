package com.ceteva.text;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class TextStartup.
 */
public class TextStartup implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		System.out.println("[ register com.ceteva.text ]");
		EditorClient client = new EditorClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.text",client);
	}
}
