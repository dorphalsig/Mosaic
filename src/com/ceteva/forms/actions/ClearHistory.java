package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.views.FormView;

public class ClearHistory extends Action {

	FormView form = null;

	public ClearHistory(FormView form) {
		setId("com.ceteva.forms.actions.ClearHistory");
		setText("Clear History");
		setToolTipText("Clear the history");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("icons/Clear.gif"));
		this.form = form;
	}

	public void run() {
		EventHandler handler = form.getHandler();
		if (handler != null) {
			Message m = handler.newMessage("clearHistory", 1);
			Value v = new Value(form.getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	public void update() {
	}
}