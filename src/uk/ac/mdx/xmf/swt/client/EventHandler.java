package uk.ac.mdx.xmf.swt.client;

import xos.Message;

public class EventHandler {

	String client;
	xos.EventHandler eventsOut; // send message to xmf
								// server
	boolean commandMode = false;
	boolean debug = false;

	public EventHandler(String client, xos.EventHandler eventsOut) {
		this.client = client;
		this.eventsOut = eventsOut;
	}

	// Returns a new message

	public Message newMessage(String name, int arity) {
		return new Message(name, arity);
	}

	public void raiseEvent(Message message) {
		if (!commandMode) {
			if (debug)
				System.out.println("EVENT: " + message.toString());
			eventsOut.raiseEvent(client, message);
		}
	}

	public void setCommandMode(boolean mode) {
		commandMode = mode;
	}
}