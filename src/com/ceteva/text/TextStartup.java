package com.ceteva.text;

import org.eclipse.ui.IStartup;

public class TextStartup implements IStartup {
	public void earlyStartup() {
		System.out.println("[ register com.ceteva.text ]");
		EditorClient client = new EditorClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.text",client);
	}
}
