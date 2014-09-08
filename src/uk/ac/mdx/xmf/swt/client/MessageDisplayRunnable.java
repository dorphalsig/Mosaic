package uk.ac.mdx.xmf.swt.client;

import xos.Message;

// TODO: Auto-generated Javadoc
// Used to communicate messages to the GUI thread when we
// are not concerned about the result

/**
 * The Class MessageDisplayRunnable.
 */
public class MessageDisplayRunnable implements Runnable {

	/** The message. */
	protected Message message;
	
	/** The client. */
	protected Client client;

	/**
	 * Instantiates a new message display runnable.
	 */
	public MessageDisplayRunnable() {
	}

	/**
	 * Instantiates a new message display runnable.
	 *
	 * @param message the message
	 * @param client the client
	 */
	public MessageDisplayRunnable(Message message, Client client) {
		this.message = message;
		this.client = client;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(Client client) {
		if (client.debug)
			System.out.println("MESSAGE: " + message);
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		client.processMessage(message);
	}
}
