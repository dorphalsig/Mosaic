package uk.ac.mdx.xmf.swt.client;

import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientElement.
 */
public class ClientElement implements Commandable, ComponentWithIdentity {

	/** The identity. */
	public String identity = "";
	
	/** The handler. */
	public EventHandler handler = null;
	
	/** The parent. */
	public ClientElement parent = null;

	/**
	 * Instantiates a new client element.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 */
	public ClientElement(ClientElement parent, EventHandler handler,
			String identity) {
		this.parent = parent;
		this.handler = handler;
		this.identity = identity;
		IdManager.put(identity, this);
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		IdManager.remove(identity);
	}

	/**
	 * Delete.
	 */
	public void delete() {
		dispose();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithIdentity#getIdentity()
	 */
	@Override
	public String getIdentity() {
		return identity;
	}

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ClientElement getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Commandable#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (message.arity > 0) {
			if (message.hasName("delete")
					&& message.args[0].hasStrValue(identity)
					&& message.arity == 1) {
				delete();
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Commandable#processCall(xos.Message)
	 */
	public Value processCall(Message message) {
		return null;
	}

	/**
	 * Synchronise.
	 *
	 * @param xml the xml
	 */
	public void synchronise(Element xml) {
	}

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

}