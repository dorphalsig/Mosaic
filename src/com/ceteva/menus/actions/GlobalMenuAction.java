package com.ceteva.menus.actions;

import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

import com.ceteva.menus.MenusClient;
import com.ceteva.text.texteditor.TextEditor;

// TODO: Auto-generated Javadoc
/**
 * The Class GlobalMenuAction.
 */
public class GlobalMenuAction extends Action {

	/** The menu identities. */
	private Vector menuIdentities;
	
	/** The identities. */
	private Vector identities;

	/**
	 * Instantiates a new global menu action.
	 *
	 * @param text the text
	 * @param menuIdentities the menu identities
	 * @param identities the identities
	 * @param enabled the enabled
	 */
	public GlobalMenuAction(String text, Vector menuIdentities,
			Vector identities, boolean enabled) {
		super(text);
		this.menuIdentities = menuIdentities;
		this.identities = identities;
		this.setEnabled(enabled);

		// Need to set this for key bindings

		// if (identities.size()>1)
		// System.out.println(menuIdentities + " -> " + identities);

		if (!identities.isEmpty()) {

			// We assume that the keybinding for the first element
			// is the same as for all the elements bound within this
			// action

			String identity = (String) menuIdentities.elementAt(0);
			this.setId(identity);
			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EventHandler handler = MenusClient.handler;
		int size = identities.size();
		Message m = handler.newMessage("rightClickMenuSelected", 1);
		Value[] ids = new Value[size * 2];
		for (int i = 0; i < size; i++) {
			String menuIdentity = (String) menuIdentities.elementAt(i);
			// hijack the code here, change it later, 56,57,58, magic number for
			// save,save as and save as and compile,
//			if (menuIdentity.equalsIgnoreCase("56")
//					|| menuIdentity.equalsIgnoreCase("57")
//					|| menuIdentity.equalsIgnoreCase("58")) {
//				if (TextEditor.tabItem!=null)
//				TextEditor.tabItem.setImage(null);
//			}
			
			ids[i * 2] = new Value(menuIdentity);
			String identity = (String) identities.elementAt(i);
			ids[i * 2 + 1] = new Value(identity);
		}
		m.args[0] = new Value(ids);
		handler.raiseEvent(m);
	}

	/*
	 * public void run() { EventHandler handler = MenusClient.handler; int
	 * numberOfIdentities = identities.size(); Message m =
	 * handler.newMessage("rightClickMenuSelected",numberOfIdentities*2);
	 * for(int i=0;i<numberOfIdentities;i++) { String menuIdentity =
	 * (String)menuIdentities.elementAt(i); m.args[i*2] = new
	 * Value(menuIdentity); String identity = (String)identities.elementAt(i);
	 * m.args[i*2+1] = new Value(identity); } handler.raiseEvent(m); }
	 */

}