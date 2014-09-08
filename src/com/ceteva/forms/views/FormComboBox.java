package com.ceteva.forms.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class FormComboBox.
 */
class FormComboBox extends FormElement {

	/** The combo. */
	private Combo combo = null;

	/**
	 * Instantiates a new form combo box.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 */
	public FormComboBox(Composite parent, String identity, EventHandler handler) {
		super(identity);
		combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		this.handler = handler;
		addListener();
	}

	/**
	 * Adds the item.
	 *
	 * @param item the item
	 * @param select the select
	 */
	public void addItem(String item, boolean select) {
		combo.add(item);
		if (select)
			combo.select(combo.getItemCount() - 1);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return combo;
	}

	/**
	 * Adds the listener.
	 */
	public void addListener() {
		combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Message m = handler.newMessage("comboBoxSelection", 2);
				Value v1 = new Value(getIdentity());
				Value v2 = new Value(combo.getItem(combo.getSelectionIndex()));
				m.args[0] = v1;
				m.args[1] = v2;
				raiseEvent(m);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
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
		combo.setBounds(x, y, width, height);
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
				if (message.hasName("addItem") && message.arity == 2) {
					String value = message.args[1].strValue();
					addItem(value, false);
					return true;
				}
				if (message.hasName("setSelection") && message.arity == 2) {
					int index = message.args[1].intValue;
					selectItem(index);
					return true;
				} else if (message.hasName("clear") && message.arity == 1) {
					removeAllItems();
					return true;
				} else if (ComponentCommandHandler.processMessage(combo,
						message))
					return true;
			}
		}
		return super.processMessage(message);
	}

	/**
	 * Removes the all items.
	 */
	public void removeAllItems() {
		combo.removeAll();
	}

	/**
	 * Select item.
	 *
	 * @param index the index
	 */
	public void selectItem(int index) {
		combo.select(index);
	}

}