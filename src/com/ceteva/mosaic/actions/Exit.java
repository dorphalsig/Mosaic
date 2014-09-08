package com.ceteva.mosaic.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Exit.
 */
public class Exit extends Action {

	/** The handler. */
	private EventHandler handler;

	/**
	 * Instantiates a new exit.
	 */
	public Exit() {
		super("E&xit");
		this.setId("exit");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Message m = handler.newMessage("shutdownRequest", 0);
		handler.raiseEvent(m);
	}

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
	}
}
