package uk.ac.mdx.xmf.swt.model;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Display.
 */
public abstract class Display extends CommandEvent {

	/** The hidden. */
	private boolean hidden = false;

	/**
	 * Instantiates a new display.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 */
	public Display(ClientElement parent, EventHandler handler, String identity) {
		super(parent, handler, identity);
	}

	/**
	 * Close.
	 */
	public void close() {
	}

	/**
	 * Hidden.
	 *
	 * @return true, if successful
	 */
	public boolean hidden() {
		return hidden;
	}

	/**
	 * Hide.
	 */
	public void hide() {
		hidden = true;
		if (isRendering())
			firePropertyChange("visibilityChange", null, null);
	}

	/**
	 * Process delete.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean processDelete(String identity) {
		return false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("hide") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			hide();
			return true;
		}
		if (message.hasName("show") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			show();
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Refresh zoom.
	 */
	public void refreshZoom() {
	}

	/**
	 * Selected.
	 *
	 * @param clicks the clicks
	 */
	public void selected(int clicks) {
		Message m = handler.newMessage("selected", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(clicks);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/**
	 * Show.
	 */
	public void show() {
		hidden = false;
		if (isRendering())
			firePropertyChange("visibilityChange", null, null);
	}
}