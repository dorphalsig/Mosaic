package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.views.FormView;

// TODO: Auto-generated Javadoc
/**
 * The Class BrowseHistory.
 */
public class BrowseHistory extends Action {

	/** The form. */
	FormView form = null;

	/**
	 * Instantiates a new browse history.
	 *
	 * @param form the form
	 */
	public BrowseHistory(FormView form) {
		setId("com.ceteva.forms.actions.BrowseHistory");
		setText("Browse History");
		setToolTipText("Select a form from the history");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("icons/History.gif"));
		this.form = form;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EventHandler handler = form.getHandler();
		if (handler != null) {
			Message m = handler.newMessage("browseHistory", 1);
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