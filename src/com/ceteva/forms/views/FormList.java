package com.ceteva.forms.views;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class FormList.
 */
class FormList extends FormElement {

	/** The id bindings. */
	Hashtable idBindings = new Hashtable();
	
	/** The binding ids. */
	Hashtable bindingIds = new Hashtable();

	/** The list. */
	private List list = null;

	/**
	 * Instantiates a new form list.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 */
	public FormList(Composite parent, String identity, EventHandler handler) {
		super(identity);
		list = new List(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL);
		this.handler = handler;
		addListener();
	}

	/**
	 * Adds the item.
	 *
	 * @param identity the identity
	 * @param text the text
	 */
	public void addItem(String identity, String text) {
		idBindings.put(new Integer(list.getItemCount()), identity);
		bindingIds.put(identity, text);
		list.add(text);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return list;
	}

	/**
	 * Adds the listener.
	 */
	public void addListener() {
		list.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				int count = list.getSelectionIndex();
				if (count != -1) {
					Message m = handler.newMessage("doubleSelected", 2);
					Value v1 = new Value((String) idBindings.get(new Integer(
							count)));
					Value v2 = new Value(getIdentity());
					m.args[0] = v1;
					m.args[1] = v2;
					raiseEvent(m);
				}
			}
		});
		list.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int count = list.getSelectionIndex();
				if (count != -1) {
					Message m = handler.newMessage("selected", 2);
					Value v1 = new Value((String) idBindings.get(new Integer(
							count)));
					Value v2 = new Value(getIdentity());
					m.args[0] = v1;
					m.args[1] = v2;
					raiseEvent(m);
				}
			}
		});
		list.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event e) {
				list.deselectAll();
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
		list.setBounds(x, y, width, height);
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
				if (message.hasName("addItem") && message.arity == 3) {
					String identity = message.args[1].strValue();
					String text = message.args[2].strValue();
					addItem(identity, text);
					return true;
				}
				if (message.hasName("clear") && message.arity == 1) {
					removeAllItems();
					return true;
				} else if (ComponentCommandHandler
						.processMessage(list, message))
					return true;
			} else if (checkListItems(message))
				return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Check list items.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean checkListItems(Message message) {
		if (message.hasName("removeItem") && message.arity == 1) {
			String target = message.args[0].strValue();
			if (idBindings.contains(target)) {
				String item = (String) bindingIds.get(target);
				int count = list.indexOf(item);
				list.remove(item);
				refreshIdBindings(count);
				bindingIds.remove(target);
				return true;
			}
		}
		if (message.hasName("enableDrag") && message.arity == 1) {
			String target = message.args[0].strValue();
			if (idBindings.contains(target)) {
				enableDrag();
				return true;
			}
		}
		if (message.hasName("enableDrop") && message.arity == 1) {
			String target = message.args[0].strValue();
			if (idBindings.contains(target)) {
				enableDrop();
				return true;
			}
		}
		return false;
	}

	/**
	 * Refresh id bindings.
	 *
	 * @param removedIndex the removed index
	 */
	void refreshIdBindings(int removedIndex) {
		idBindings.remove(new Integer(removedIndex));
		Hashtable dummyTable = new Hashtable();
		Enumeration e = idBindings.keys();
		while (e.hasMoreElements()) {
			Integer key = (Integer) e.nextElement();
			String item = (String) idBindings.get(key);
			int index = key.intValue();
			if (index >= removedIndex) {
				key = new Integer(index - 1);
			}
			dummyTable.put(key, item);
		}
		idBindings = dummyTable;
	}

	/**
	 * Removes the all items.
	 */
	public void removeAllItems() {
		list.removeAll();
		idBindings.clear();
		bindingIds.clear();
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			int count = list.getSelectionIndex();
			if (count != -1) {
				String id = (String) idBindings.get(new Integer(count));
				// event.data = id;
				Vector dragIds = new Vector();
				dragIds.add(id);
				String dragIdsString = dragIds.toString();
				event.data = dragIdsString;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			int count = list.getSelectionIndex();
			if (count != -1) {
				String dragId = (String) event.data;
				String dropId = (String) idBindings.get(new Integer(count));
				System.out.println("drop: '" + dragId + "->" + dropId + "'");
			}
		}
	}

}