package com.ceteva.undo;

import org.eclipse.ui.IStartup;

public class UndoStartup implements IStartup {
	public void earlyStartup() {
		UndoClient undoClient = new UndoClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.undo",undoClient);
	}
}
