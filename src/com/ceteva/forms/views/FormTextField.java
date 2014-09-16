package com.ceteva.forms.views;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPartSite;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import xos.Message;
import xos.Value;

import com.ceteva.forms.FormsPlugin;
import com.ceteva.menus.MenuBuilder;
import com.ceteva.menus.MenuListener;
import com.ceteva.menus.MenuManager;

// TODO: Auto-generated Javadoc
/**
 * The Class FormTextField.
 */
class FormTextField extends FormElement implements MenuListener,
		DisposeListener {

	/** The site. */
	IWorkbenchPartSite site;
	
	/** The menu items. */
	Hashtable menuItems = new Hashtable();
	
	/** The text. */
	Text text = null;
	
	/** The changes made. */
	boolean changesMade = false;
	
	/** The menu. */
	Menu menu = null;
	
	/** The owning form. */
	Composite owningForm;

	/**
	 * Instantiates a new form text field.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 * @param site the site
	 */
	public FormTextField(Composite parent, String identity,
			EventHandler handler, IWorkbenchPartSite site) {
		super(identity);
		this.owningForm = parent;
		this.handler = handler;
		initText(parent);
		// setMenuListener();
		calculateMenu();
		MenuManager.addMenuListener(this);
		text.addDisposeListener(this);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return text;
	}

	/**
	 * Calculate menu.
	 */
	public void calculateMenu() {
		Menu m = text.getMenu();
		if (m != null)
			m.dispose();
		MenuBuilder.resetKeyBindings(site);
		org.eclipse.jface.action.MenuManager menu = new org.eclipse.jface.action.MenuManager();
		MenuBuilder.calculateMenu(getIdentity(), menu, site);
		text.setMenu(menu.createContextMenu(text));
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
		text.setBounds(x, y, width, height);
	}

	/*
	 * public void setMenuListener() { menu = new Menu(text);
	 * text.setMenu(menu); menu.addMenuListener(new MenuAdapter() { public void
	 * menuShown(MenuEvent e) { MenuBuilder.calculateMenu(identity,menu); } });
	 * }
	 */

	/**
	 * Sets the text.
	 *
	 * @param textString the new text
	 */
	public void setText(String textString) {
		text.setText(textString);
		text.setToolTipText(textString);
		changesMade(false);
	}

	/**
	 * Changes made.
	 *
	 * @param b the b
	 */
	public void changesMade(boolean b) {
		changesMade = b;
	}

	/**
	 * Inits the text.
	 *
	 * @param parent the parent
	 */
	public void initText(Composite parent) {
		text = new Text(parent, SWT.SINGLE);
		Menu menu = new Menu(text);
		text.setMenu(menu);
		menuItems.put(this.getIdentity(), menu);
		addClickListener();
		changesMade(false);
	}

	/**
	 * Sets the editable.
	 *
	 * @param editable the new editable
	 */
	public void setEditable(boolean editable) {
		text.setEditable(editable);
		if (editable) {
			text.setBackground(FormsPlugin.normalBackgroundColor);
			addChangeListener();
		} else {
			text.setBackground(FormsPlugin.disabledBackgroundColor);
		}
	}

	/**
	 * Sets the background.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setBackground(int red, int green, int blue) {
		text.setBackground(ColorManager.getColor(new RGB(red, green, blue)));
	}

	/**
	 * Adds the change listener.
	 */
	public void addChangeListener() {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				changesMade(true);
			}
		});

		text.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				getEditableText();
			}

			public void focusLost(FocusEvent e) {
				if (changesMade) {
					Message m = handler.newMessage("textChanged", 2);
					Value v1 = new Value(getIdentity());
					Value v2 = new Value(text.getText());
					m.args[0] = v1;
					m.args[1] = v2;
					raiseEvent(m);
				} else {
					Message m = handler.newMessage("resetText", 1);
					Value v1 = new Value(getIdentity());
					m.args[0] = v1;
					raiseEvent(m);
				}
				changesMade(false);
			}
		});

		text.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR && changesMade) {
					Message m = handler.newMessage("textChanged", 2);
					Value v1 = new Value(getIdentity());
					Value v2 = new Value(text.getText());
					m.args[0] = v1;
					m.args[1] = v2;
					raiseEvent(m);
					changesMade(false);
				} else if (e.keyCode == SWT.CR || e.keyCode == SWT.ESC) {
					Message m = handler.newMessage("resetText", 1);
					Value v1 = new Value(getIdentity());
					m.args[0] = v1;
					raiseEvent(m);
					changesMade = false;
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});

	}

	/**
	 * Adds the click listener.
	 */
	public void addClickListener() {
		text.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				Message m = handler.newMessage("doubleSelected", 1);
				Value v1 = new Value(getIdentity());
				m.args[0] = v1;
				raiseEvent(m);
			}
		});
		text.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				// should the following event not be selected?

				Message m = handler.newMessage("doubleSelected", 1);
				Value v1 = new Value(getIdentity());
				m.args[0] = v1;
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
			/*
			 * if (message.hasName("addMenuItem") && message.arity == 3) { if
			 * (menuItems.containsKey(message.args[0].strValue())) { //
			 * addMenuItem(message); return true; } } else
			 */

			if (message.args[0].hasStrValue(getIdentity())) {
				if (message.hasName("getText") && message.arity == 1) {
					getText();
					return true;
				} else if (message.hasName("setText") && message.arity == 2) {
					setText(message);
					// owningForm.forceFocus();
					return true;
				} else if (message.hasName("setBackground")
						&& message.arity == 4) {
					setBackground(message);
					return true;
				} else if (message.hasName("setEditableText")
						&& message.arity == 2) {
					int caretpos = text.getCaretPosition();
					setText(message);
					text.setSelection(caretpos, caretpos);
					return true;
				} else if (ComponentCommandHandler
						.processMessage(text, message))
					return true;
			}
		}
		return super.processMessage(message);
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public void getText() {
		Message m = handler.newMessage("text", 2);
		Value v1 = new Value(getIdentity());
		Value v2 = new Value(text.getText());
		m.args[0] = v1;
		m.args[1] = v2;
		raiseEvent(m);
	}

	/**
	 * Gets the editable text.
	 *
	 * @return the editable text
	 */
	public void getEditableText() {
		Message m = handler.newMessage("getEditableText", 1);
		Value v1 = new Value(getIdentity());
		m.args[0] = v1;
		raiseEvent(m);
	}

	/**
	 * Sets the text.
	 *
	 * @param message the new text
	 */
	public void setText(Message message) {
		String textString = message.args[1].strValue();
		setText(textString);
	}

	/**
	 * Sets the background.
	 *
	 * @param message the new background
	 */
	public void setBackground(Message message) {
		int red = message.args[1].intValue;
		int green = message.args[2].intValue;
		int blue = message.args[3].intValue;
		setBackground(red, green, blue);
	}

	/* (non-Javadoc)
	 * @see com.ceteva.menus.MenuListener#newMenuAdded()
	 */
	public void newMenuAdded() {
		calculateMenu();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		MenuManager.removeMenuListener(this);
		MenuBuilder.dispose(site);
	}

}