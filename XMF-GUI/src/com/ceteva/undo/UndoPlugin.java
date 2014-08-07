package com.ceteva.undo;

public class UndoPlugin {

	private static UndoPlugin plugin;

	public UndoPlugin() {
		plugin = this;
	}

	public static UndoPlugin getDefault() {
		return plugin;
	}

	public void earlyStartup() {
		UndoClient undoClient = new UndoClient();
		// XmfPlugin.XOS.newMessageClient("com.ceteva.undo",undoClient);
	}
}
