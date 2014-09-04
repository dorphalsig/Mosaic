package com.ceteva.undo;

import uk.ac.mdx.xmf.swt.client.Client;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

public class UndoClient extends Client {

	// public static UndoAction undo = new UndoAction();
	// public static RedoAction redo = new RedoAction();
	public EventHandler handler = null;

	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	public UndoClient() {
		super("com.ceteva.undo");
	}

	// public void setEventHandler(EventHandler handler) {
	// this.handler = handler;
	// // undo.registerEventHandler(handler);
	// // redo.registerEventHandler(handler);
	// }

	public boolean processMessage(Message message) {
		// if (message.hasName("enableUndo")) {
		// undo.setEnabled(true);
		// return true;
		// }
		// if (message.hasName("disableUndo")) {
		// undo.setEnabled(false);
		// return true;
		// }
		// if (message.hasName("enableRedo")) {
		// redo.setEnabled(true);
		// return true;
		// }
		// if (message.hasName("disableRedo")) {
		// redo.setEnabled(false);
		// return true;
		// }
		return false;
	}
}