package com.ceteva.forms.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class FormCheckBox.
 */
class FormCheckBox extends FormElement {

	/** The check. */
	Button check = null;

	/**
	 * Instantiates a new form check box.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 */
	public FormCheckBox(Composite parent, String identity, EventHandler handler) {
		super(identity);
		check = new Button(parent, SWT.CHECK);
		check.setSize(20, 20);
		check.setAlignment(SWT.CENTER);
		this.handler = handler;
		addListener();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return check;
	}

	/**
	 * Adds the listener.
	 */
	public void addListener() {
		check.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Message m = handler.newMessage("setBoolean", 2);
				Value v1 = new Value(getIdentity());
				Value v2 = new Value(check.getSelection());
				m.args[0] = v1;
				m.args[1] = v2;
				check.setSelection(!check.getSelection());
				raiseEvent(m);
			}
		});
	}

	/**
	 * Sets the location.
	 *
	 * @param position the new location
	 */
	public void setLocation(Point position) {
		check.setLocation(position);
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	public void setSelected(boolean selected) {
		check.setSelection(selected);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Commandable#processCall(xos.Message)
	 */
	public Value processCall(Message message) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (message.arity >= 1) {
			if (message.args[0].hasStrValue(getIdentity())) {
				if (message.hasName("check") && message.arity == 1) {
					check.setSelection(true);
					return true;
				}
				if (message.hasName("uncheck") && message.arity == 1) {
					check.setSelection(false);
					return true;
				} else {
					if (ComponentCommandHandler.processMessage(check, message))
						return true;
				}
			}
		}
		return super.processMessage(message);
	}
}