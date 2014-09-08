package com.ceteva.forms.views;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class FormText.
 */
class FormText extends FormElement {

	/** The text. */
	Label text = null;

	/**
	 * Instantiates a new form text.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 */
	public FormText(Composite parent, String identity, EventHandler handler) {
		super(identity);
		text = new Label(parent, SWT.NONE);
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param textString the new text
	 */
	public void setText(String textString) {
		text.setText(textString);
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Point location) {
		text.setLocation(location);
	}

	/**
	 * Calculate size.
	 */
	public void calculateSize() {
		Dimension minimum = FigureUtilities.getTextExtents(text.getText(),
				text.getFont());
		text.setSize(minimum.width, minimum.height);
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
		if (message.args[0].strValue().equals(getIdentity())) {
			if (message.hasName("setText")) {
				text.setText(message.args[1].strValue());
				return true;
			} else {
				if (ComponentCommandHandler.processMessage(text, message))
					return true;
			}
		}
		return super.processMessage(message);
	}
}