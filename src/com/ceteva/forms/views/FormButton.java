package com.ceteva.forms.views;

import org.eclipse.swt.SWT;
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
 * The Class FormButton.
 */
class FormButton extends FormElement {

	/** The button. */
	Button button = null;

	/**
	 * Instantiates a new form button.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 */
	public FormButton(Composite parent, String identity, EventHandler handler) {
		super(identity);
		button = new Button(parent, SWT.PUSH);
		addEventHandler();
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return button;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		button.setText(text);
	}

	/**
	 * Sets the bounds.
	 *
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public void setBounds(int x, int y, int width, int height) {
		button.setBounds(x, y, width, height);
	}

	/**
	 * Adds the event handler.
	 */
	public void addEventHandler() {
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				Message m = handler.newMessage("buttonPressed", 1);
				Value v = new Value(getIdentity());
				m.args[0] = v;
				raiseEvent(m);
			}
		});
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
			if (message.args[0].hasStrValue(getIdentity()))
				return ComponentCommandHandler.processMessage(button, message);
		}
		return super.processMessage(message);
	}
}