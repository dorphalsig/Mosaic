package uk.ac.mdx.xmf.swt.client;

import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class EventHandler.
 */
public class EventHandler {

	/** The client. */
	String client;
	
	/** The events out. */
	xos.EventHandler eventsOut; // send message to xmf
								// server
	/** The command mode. */
								boolean commandMode = false;
	
	/** The debug. */
	boolean debug = false;

	/**
	 * Instantiates a new event handler.
	 *
	 * @param client the client
	 * @param eventsOut the events out
	 */
	public EventHandler(String client, xos.EventHandler eventsOut) {
		this.client = client;
		this.eventsOut = eventsOut;
	}

	// Returns a new message

	/**
	 * New message.
	 *
	 * @param name the name
	 * @param arity the arity
	 * @return the message
	 */
	public Message newMessage(String name, int arity) {
		return new Message(name, arity);
	}

	/**
	 * Raise event.
	 *
	 * @param message the message
	 */
	public void raiseEvent(Message message) {
		if (!commandMode) {
			if (debug)
				System.out.println("EVENT: " + message.toString());
			eventsOut.raiseEvent(client, message);
		}
	}

	/**
	 * Sets the command mode.
	 *
	 * @param mode the new command mode
	 */
	public void setCommandMode(boolean mode) {
		commandMode = mode;
	}
}