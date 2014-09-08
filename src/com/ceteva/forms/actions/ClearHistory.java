package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.views.FormView;

// TODO: Auto-generated Javadoc
/**
 * The Class ClearHistory.
 */
public class ClearHistory extends Action {

	/** The form. */
	FormView form = null;

	/**
	 * Instantiates a new clear history.
	 *
	 * @param form the form
	 */
	public ClearHistory(FormView form) {
		setId("com.ceteva.forms.actions.ClearHistory");
		setText("Clear History");
		setToolTipText("Clear the history");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("icons/Clear.gif"));
		this.form = form;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EventHandler handler = form.getHandler();
		if (handler != null) {
			Message m = handler.newMessage("clearHistory", 1);
			Value v = new Value(form.getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Update.
	 */
	public void update() {
	}
}