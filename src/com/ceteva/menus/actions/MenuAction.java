package com.ceteva.menus.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuAction.
 */
public class MenuAction extends Action {

	/** The handler. */
	EventHandler handler = null;
	
	/** The identity. */
	String identity = null;

	/**
	 * Instantiates a new menu action.
	 *
	 * @param text the text
	 * @param identity the identity
	 * @param handler the handler
	 */
	public MenuAction(String text, String identity, EventHandler handler) {
		super(text);
		this.identity = identity;
		this.handler = handler;
		this.setId(identity);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		Message m = handler.newMessage("menuSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}
}