package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.views.FormView;

public class NextInHistory extends Action {

	FormView form = null;
	public boolean enabled = false;

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

	public void run() {
		EventHandler handler = form.getHandler();
		if (enabled && handler != null) {
			Message m = handler.newMessage("nextInHistory", 1);
			Value v = new Value(form.getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	public void update() {
		String icon = enabled ? "icons/Forward.gif"
				: "icons/ForwardDisabled.gif";
		setImageDescriptor(IconManager.getImageDescriptorAbsolute(icon));
	}

}