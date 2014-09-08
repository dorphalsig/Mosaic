package com.ceteva.text.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

import com.ceteva.text.texteditor.TextEditor;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuAction.
 */
public class MenuAction extends Action {

	/** The text editor. */
	TextEditor textEditor = null;
	
	/** The parent id. */
	String parentId;
	
	/** The identity. */
	String identity;
	
	/** The label. */
	String label;

	/**
	 * Instantiates a new menu action.
	 *
	 * @param textEditor the text editor
	 * @param parentId the parent id
	 * @param identity the identity
	 * @param label the label
	 */
	public MenuAction(TextEditor textEditor, String parentId, String identity,
			String label) {
		super(label);
		this.textEditor = textEditor;
		this.parentId = parentId;
		this.identity = identity;
		this.label = label;
		this.setId(identity);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (textEditor.getEventHandler() != null) {
			EventHandler handler = textEditor.getEventHandler();
			Message m = handler.newMessage("rightClickMenuSelected", 1);
			Value v = new Value(identity);
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Gets the parent id.
	 *
	 * @return the parent id
	 */
	public String getParentId() {
		return parentId;
	}
}
