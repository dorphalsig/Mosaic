package com.ceteva.forms.views;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentWithHandler.
 */
class ComponentWithHandler {

	/** The handler. */
	public EventHandler handler = null;
	
	/** The events enabled. */
	boolean eventsEnabled = true;

	/**
	 * Disable events.
	 */
	public void disableEvents() {
		eventsEnabled = false;
	}

	/**
	 * Raise event.
	 *
	 * @param m the m
	 */
	void raiseEvent(Message m) {
		if (eventsEnabled)
			handler.raiseEvent(m);
	}

}