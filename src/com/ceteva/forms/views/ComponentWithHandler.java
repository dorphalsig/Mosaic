package com.ceteva.forms.views;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

class ComponentWithHandler {

	public EventHandler handler = null;
	boolean eventsEnabled = true;

	public void disableEvents() {
		eventsEnabled = false;
	}

	void raiseEvent(Message m) {
		if (eventsEnabled)
			handler.raiseEvent(m);
	}

}