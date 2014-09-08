package uk.ac.mdx.xmf.swt.client;

import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
// Used to communicate messages to the GUI thread when we
// are concerned about the result, this is placed in a known place and can be retrieved by a getter

/**
 * The Class CallDisplayRunnable.
 */
public class CallDisplayRunnable extends MessageDisplayRunnable {

	/** The value. */
	Value value = null;

	/**
	 * Instantiates a new call display runnable.
	 */
	public CallDisplayRunnable() {
		super();
	}

	/**
	 * Instantiates a new call display runnable.
	 *
	 * @param message the message
	 * @param client the client
	 */
	public CallDisplayRunnable(Message message, Client client) {
		super(message, client);
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.MessageDisplayRunnable#run()
	 */
	@Override
	public void run() {
		if (client.debug)
			System.out.println("MESSAGE: " + message);
		value = client.processCall(message);
	}
}