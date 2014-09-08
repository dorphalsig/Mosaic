package uk.ac.mdx.xmf.swt.client;

import org.eclipse.swt.widgets.Display;

import xos.Message;
import xos.MessageHandler;
import xos.MessagePacket;
import xos.Value;

// TODO: Auto-generated Javadoc
// This is a comment

/**
 * The Class Client.
 */
public abstract class Client implements MessageHandler {

	/** The message runnable. */
	private static MessageDisplayRunnable messageRunnable = new MessageDisplayRunnable();
	
	/** The packet runnable. */
	private static PacketDisplayRunnable packetRunnable = new PacketDisplayRunnable();
	
	/** The call runnable. */
	private static CallDisplayRunnable callRunnable = new CallDisplayRunnable();

	// All events for this client are raised on a handler

	/** The debug. */
	protected boolean debug = false;
	
	/** The name. */
	protected String name;
	
	/** The handler. */
	protected EventHandler handler;

	/**
	 * Instantiates a new client.
	 *
	 * @param name the name
	 */
	public Client(String name) {
		this.name = name;
	}

	// When a message is received it is placed on the Mosaic
	// GUI thread synchronously. If the command is not consumed
	// by the client then an error is reported on the GUI thread
	/* (non-Javadoc)
	 * @see xos.MessageHandler#sendMessage(xos.Message)
	 */
	@Override
	public void sendMessage(final Message message) {
		messageRunnable.setClient(this);
		messageRunnable.setMessage(message);
		Display.getDefault().syncExec(messageRunnable);
	}

	/* (non-Javadoc)
	 * @see xos.MessageHandler#sendPacket(xos.MessagePacket)
	 */
	@Override
	public void sendPacket(final MessagePacket packet) {
		packetRunnable.setClient(this);
		packetRunnable.setPacket(packet);
		Display.getDefault().syncExec(packetRunnable);
	}

	// When a call is received it is placed on the Mosaic GUI
	// thread synchronously since the result must be returned
	// to the caller.
	/* (non-Javadoc)
	 * @see xos.MessageHandler#callMessage(xos.Message)
	 */
	@Override
	public Value callMessage(final Message message) {
		callRunnable.setClient(this);
		callRunnable.setMessage(message);
		Display.getDefault().syncExec(callRunnable);
		return callRunnable.getValue();
	}

	// xos supplies each client with an event handler

	/* (non-Javadoc)
	 * @see xos.MessageHandler#registerEventHandler(xos.EventHandler)
	 */
	@Override
	public void registerEventHandler(xos.EventHandler handler) {
		this.handler = new EventHandler(name, handler);
		setEventHandler(this.handler);
	}

	// The following two methods should be overriden in concrete
	// implementations of Clients

	/**
	 * Process message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public abstract boolean processMessage(Message message);

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public abstract void setEventHandler(EventHandler handler);

	/**
	 * Process call.
	 *
	 * @param message the message
	 * @return the value
	 */
	public Value processCall(Message message) {
		return null;
	}
}