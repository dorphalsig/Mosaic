package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.views.FormView;

// TODO: Auto-generated Javadoc
/**
 * The Class NextInHistory.
 */
public class NextInHistory extends Action {

	/** The form. */
	FormView form = null;
	
	/** The enabled. */
	public boolean enabled = false;

	/**
	 * Instantiates a new next in history.
	 *
	 * @param form the form
	 * @param enabled the enabled
	 */
	public NextInHistory(FormView form, boolean enabled) {
		setId("com.ceteva.forms.actions.NextInHistory");
		setText("Next in History");
		setToolTipText("Selects the next form in the history");
		setImageDescriptor(uk.ac.mdx.xmf.swt.client.IconManager
				.getImageDescriptorAbsolute("icons/Forward.gif"));
		this.form = form;
		this.enabled = enabled;
		update();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		EventHandler handler = form.getHandler();
		if (enabled && handler != null) {
			Message m = handler.newMessage("nextInHistory", 1);
			Value v = new Value(form.getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Update.
	 */
	public void update() {
		String icon = enabled ? "icons/Forward.gif"
				: "icons/ForwardDisabled.gif";
		setImageDescriptor(IconManager.getImageDescriptorAbsolute(icon));
	}

}