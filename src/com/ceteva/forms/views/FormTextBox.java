package com.ceteva.forms.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

import com.ceteva.forms.FormsPlugin;
import com.ceteva.menus.MenuBuilder;

class FormTextBox extends FormElement {

	final int OK = 0;
	final int CANCEL = 1;

	// private CustomSourceViewer textBox = null;
	private Text textBox = null;
	Document document = null;
	FormView owningView = null;
	Scanner scanner = null;
	boolean changesMade = false;
	MenuManager menumanager;
	IMenuListener fMenuListener;
	IAction undo;
	IAction redo;
	String oldText = "";

	public FormTextBox(Composite parent, String identity, EventHandler handler,
			FormView owningView) {
		super(identity);
		this.handler = handler;
		this.owningView = owningView;
		init(parent);
	}

	public Control getControl() {
		// return textBox.getControl();
		return null;
	}

	public void init(Composite parent) {
		textBox = new Text(parent, SWT.SINGLE);

		// FormTextBoxConfiguration ftbc = new FormTextBoxConfiguration();
		// textBox.configure(ftbc);
		// document = new Document();
		// textBox.setDocument(document);
		// undo/redo actions

		// undo = new Undo(textBox.);
		// redo = new Redo(textBox.getUndoManager());
		// IActionBars actionBars = ((Object)
		// owningView).getViewSite().getActionBars();
		// actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
		// actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
		// actionBars.updateActionBars();
		// owningView.getViewSite().getKeyBindingService().registerAction(undo);
		// owningView.getViewSite().getKeyBindingService().registerAction(redo);

		// scanner = ftbc.getTagScanner();
		setMenuListener();
		changesMade(false);
	}

	public Value processCall(Message message) {
		if (message.args[0].hasStrValue(getIdentity())
				&& message.hasName("getText")) {
			return new Value(getText());
		}
		return null;
	}

	public boolean processMessage(Message message) {
		if (message.arity >= 1) {
			if (message.args[0].hasStrValue(getIdentity())) {
				if (message.hasName("changesMade") && message.arity == 2) {
					boolean value = message.args[1].boolValue;
					changesMade(value);
					return true;
				}
				if (message.hasName("getText") && message.arity == 1) {
					getText();
					return true;
				} else if (message.hasName("setText") && message.arity == 2) {
					String text = message.args[1].strValue();
					setText(text);
					return true;
				} else if (message.hasName("addRule") && message.arity == 3) {
					addRule(message);
					return true;
				} else if (message.hasName("clearRules") && message.arity == 1) {
					clearRules();
					return true;
				} else if (message.hasName("forceFocus") && message.arity == 1) {
					forceFocus();
					return true;
				} else if (ComponentCommandHandler
						.processMessage(null, message))
					return true;
			}
		}
		return super.processMessage(message);
	}

	public void setBounds(int x, int y, int width, int height) {
		// Control c = textBox.getControl();
		textBox.setBounds(x, y, width, height);
	}

	public void setEditable(boolean editable) {
		textBox.setEditable(editable);
		if (editable)
			addChangeListener();
	}

	public void forceFocus() {
		// textBox.getTextWidget().forceFocus();
		textBox.forceFocus();
		changesMade(true);
	}

	public void addChangeListener() {
		textBox.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				String newText = textBox.getText();
				if (!newText.equals(oldText))
					changesMade(true);
				oldText = newText;

			}
		});
	}

	public int textChangedDialog() {
		MessageDialog dialog = new MessageDialog(Display.getCurrent()
				.getActiveShell(), "Text has changed", null,
				"Do you want to keep the changes you've made?",
				MessageDialog.QUESTION, new String[] { "OK", "Cancel" }, 1);
		return dialog.open();
	}

	public void changesMade(boolean b) {
		changesMade = b;
		owningView.setChangesPending(b, this);
		if (changesMade)
			textBox.setBackground(FormsPlugin.getInstance().modifiedBackgroundColor);
		else
			textBox.setBackground(FormsPlugin.getInstance().normalBackgroundColor);
	}

	public String getText() {
		return textBox.getText();
	}

	public void setText(String text) {
		textBox.setText(text);
		oldText = text;
		changesMade(false);
	}

	public void addRule(Message message) {
		// String word = message.args[1].strValue();
		// String color = message.args[2].strValue();
		// scanner.addRule(word, color);
		// document.set(document.get());
	}

	public void clearRules() {
		scanner.clearRules();
	}

	protected final IMenuListener getContextMenuListener() {
		if (fMenuListener == null) {
			fMenuListener = new IMenuListener() {
				public void menuAboutToShow(IMenuManager menu) {
					// IWorkbenchPartSite site = owningView.getSite();
					MenuBuilder.calculateMenu(getIdentity(), menumanager, null);
					// menu.add(undo);
					// menu.add(redo);
				}
			};
		}
		return fMenuListener;
	}

	public void setMenuListener() {
		menumanager = new MenuManager();
		menumanager.setRemoveAllWhenShown(true);
		menumanager.addMenuListener(getContextMenuListener());
		Menu menu = menumanager.createContextMenu(textBox);
		textBox.setMenu(menu);
	}

	public void setEnabled(boolean b) {
		textBox.setEnabled(b);
		Color c = b ? FormsPlugin.getInstance().normalBackgroundColor
				: FormsPlugin.disabledBackgroundColor;
		textBox.setBackground(c);
	}

	public void maximiseToCanvas(final Composite scrollable,
			final Composite formContentsHolder) {
		final Composite canvas = owningView.canvas;
		final FormTextBox ftb = this;
		Point p = canvas.getSize();
		scrollable.setBounds(0, 0, p.x + 50, p.y + 50);
		formContentsHolder.setBounds(0, 0, p.x, p.y);
		ftb.setBounds(0, 0, p.x, p.y);
		ControlListener listener = new ControlListener() {
			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				Point p = canvas.getSize();
				scrollable.setBounds(0, 0, p.x + 50, p.y + 50);
				formContentsHolder.setBounds(0, 0, p.x, p.y);
				ftb.setBounds(0, 0, p.x, p.y);
			}
		};
		canvas.addControlListener(listener);
		owningView.addCanvasEventListener(listener);
	}

}