package com.ceteva.forms.views;

import java.util.Vector;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;
import xos.Value;

import com.ceteva.forms.XMLBindings;

// TODO: Auto-generated Javadoc
/**
 * The Class FormTree.
 */
public class FormTree extends FormElement implements FormTreeHandler {

	/** The expanded. */
	Vector expanded = new Vector();
	
	/** The tree. */
	FormTreeWrapper tree;

	// IWorkbenchPartSite site;

	/**
	 * Instantiates a new form tree.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 * @param editable the editable
	 * @param multiSelect the multi select
	 */
	public FormTree(Composite parent, String identity, EventHandler handler,
			boolean editable, boolean multiSelect) {
		super(identity);
		// this.site = site;
		tree = new FormTreeWrapper(parent, identity, this, editable,
				multiSelect);
		this.setEventHandler(handler);
	}

	/**
	 * Adds the node.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean addNode(Message message) {
		String parentID = message.args[0].strValue();
		String nodeID = message.args[1].strValue();
		String text = message.args[2].strValue();
		int index = -1;
		if (message.arity == 3)
			index = message.args[3].intValue;
		return tree.addNode(parentID, index, nodeID, "", text, "", true, false,
				false);
	}

	/**
	 * Adds the node with icon.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean addNodeWithIcon(Message message) {
		String parentID = message.args[0].strValue();
		String nodeID = message.args[1].strValue();
		String text = message.args[2].strValue();
		boolean editable = message.args[3].boolValue;
		String icon = message.args[4].strValue();
		int index = -1;
		if (message.arity == 6)
			index = message.args[5].intValue;
		return tree.addNode(parentID, index, nodeID, icon, text, "", editable,
				false, false);
	}

	/**
	 * Broadcast to nodes.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean broadcastToNodes(Message message) {
		if (message.hasName("addNode")
				&& (message.arity == 3 || message.arity == 4))
			return addNode(message);
		if (message.hasName("addNodeWithIcon")
				&& (message.arity == 5 || message.arity == 6))
			return addNodeWithIcon(message);
		if (message.hasName("removeNode") && message.arity == 1)
			return removeNode(message);
		if (message.hasName("expandNode") && message.arity == 1)
			return expandNode(message);
		if (message.hasName("selectNode") && message.arity == 2)
			return selectNode(message);
		if (message.hasName("deselectNode") && message.arity == 1)
			return deselectNode(message);
		if (message.hasName("setNodeIcon") && message.arity == 2)
			return setIcon(message);
		if (message.hasName("setText") && message.arity == 2)
			return setText(message);
		if (message.hasName("setEditableText") && message.arity == 2)
			return setEditableText(message);
		if (message.hasName("setEditable") && message.arity == 2)
			return setEditable(message);
		if (message.hasName("setToolTipText") && message.arity == 2)
			return setToolTipText(message);
		if (message.hasName("enableDrag") && message.arity == 1)
			return enableDrag(message);
		if (message.hasName("enableDrop") && message.arity == 1)
			return enableDrop(message);
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#getEditableText(java.lang.String)
	 */
	public void getEditableText(String identity) {
		Message m = handler.newMessage("getEditableText", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	// public AbstractUIPlugin getPlugin() {
	// return FormsPlugin.getDefault();
	// }

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ComponentWithControl#getControl()
	 */
	public Control getControl() {
		return tree.getTree();
	}

	/*
	 * public Tree getTree() { return tree; }
	 */

	/*
	 * ublic Tree getTreeViewer() { return tree; }
	 */

	/*
	 * public IWorkbenchPartSite getSite() { return site; }
	 */

	/*
	 * public TreeItem getSelectedNode() { TreeItem[] selected =
	 * tree.getSelection(); if (selected.length>0) return selected[0]; return
	 * null; }
	 * 
	 * public TreeItem[] getSelectedNodes() { return tree.getSelection(); }
	 * 
	 * public String getSelectedNodeIdentity() { TreeItem item =
	 * getSelectedNode(); if (item!=null) { String identity =
	 * (String)item.getData(); return identity; } return null; }
	 */

	/**
	 * Sets the bounds.
	 *
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public void setBounds(int x, int y, int width, int height) {
		tree.setBounds(x, y, width, height);
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
		if (message.args[0].hasStrValue(getIdentity())) {
			if (message.hasName("addNode") && message.arity == 3) {
				String nodeId = message.args[1].strValue();
				String text = message.args[2].strValue();
				tree.addNode(getIdentity(), -1, nodeId, "", text, "", true,
						false, false);
				return true;
			}
			if (message.hasName("addNode") && message.arity == 4) {
				String nodeId = message.args[1].strValue();
				String text = message.args[2].strValue();
				int index = message.args[3].intValue;
				tree.addNode(getIdentity(), index, nodeId, "", text, "", true,
						false, false);
				return true;
			}
			if (message.hasName("addNodeWithIcon") && message.arity == 5) {
				String nodeId = message.args[1].strValue();
				String text = message.args[2].strValue();
				boolean editable = message.args[3].boolValue;
				String icon = message.args[4].strValue();
				tree.addNode(getIdentity(), -1, nodeId, icon, text, "", true,
						false, false);
				return true;
			}
			if (message.hasName("addNodeWithIcon") && message.arity == 6) {
				String nodeId = message.args[1].strValue();
				;
				String text = message.args[2].strValue();
				boolean editable = message.args[3].boolValue;
				String icon = message.args[4].strValue();
				int index = message.args[5].intValue;
				tree.addNode(getIdentity(), index, nodeId, icon, text, "",
						true, false, false);
				return true;
			}
			if (ComponentCommandHandler.processMessage(getControl(), message))
				return true;
		} else {
			if (broadcastToNodes(message))
				return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Removes the node.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean removeNode(Message message) {
		String nodeId = message.args[0].strValue();
		return tree.removeNode(nodeId);
	}

	/**
	 * Expand node.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean expandNode(Message message) {
		String target = message.args[0].strValue();
		return tree.expandNode(target, true);
	}

	/**
	 * Select node.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean selectNode(Message message) {
		String identity = message.args[0].strValue();
		boolean expand = message.args[1].boolValue;
		return tree.selectNode(identity, expand);
	}

	/**
	 * Deselect node.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean deselectNode(Message message) {
		String target = message.args[0].strValue();
		return tree.deselectNode(target);
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#setIdentity(java.lang.String)
	 */
	public void setIdentity(String identity) {
		super.setIdentity(identity);
		if (tree != null)
			tree.setIdentity(identity);
	}

	/*
	 * public void dragEnter(DropTargetEvent event) { dragDelayTimer = 0;
	 * currentDropTarget = null; }
	 * 
	 * public void dragOver(DropTargetEvent event) { if
	 * (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
	 * //if (checkDragDelay()) { String dropId =
	 * tree.getNodeIdentityAt(event.x,event.y); System.out.println("dragOver: ("
	 * + event.x + "," + event.y + ") = " + dropId); if (dropId != null) {
	 * String dragIdsString = (String)event.data; String operation =
	 * getDropOperation(event.detail); System.out.println("dragOver: '" +
	 * dragIdsString + "->" + dropId + "'");
	 * //raiseDragAndDropEvent(dropId,operation,dragIdsString);
	 * //currentDropTarget = item; } //} } }
	 */
	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			String dragIdsString = tree.getSelectedNodeIdentitiesString();
			event.data = dragIdsString;
		}
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormElement#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			String dropId = tree.getNodeIdentity((TreeItem) event.item);
			if (dropId != null) {
				String dragIdsString = (String) event.data;
				String operation = getDropOperation(event.detail);
				// System.out.println("drop: '" + dragIdsString + "->" + dropId
				// + "'");
				raiseDragAndDropEvent(dropId, operation, dragIdsString);
			}
		}
	}

	/**
	 * Sets the icon.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setIcon(Message message) {
		String identity = message.args[0].strValue();
		String icon = message.args[1].strValue();
		return tree.setIcon(identity, icon);
	}

	/**
	 * Sets the text.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setText(Message message) {
		String identity = message.args[0].strValue();
		String text = message.args[1].strValue();
		return tree.setText(identity, text);
	}

	/**
	 * Sets the editable text.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setEditableText(Message message) {
		String identity = message.args[0].strValue();
		String text = message.args[1].strValue();
		return tree.setEditableText(identity, text);
	}

	/**
	 * Sets the tool tip text.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setToolTipText(Message message) {
		String identity = message.args[0].strValue();
		String text = message.args[1].strValue();
		return tree.setToolTipText(identity, text);
	}

	/**
	 * Sets the focus.
	 */
	public void setFocus() {
		tree.setFocus();
	}

	/**
	 * Sets the editable.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setEditable(Message message) {
		String identity = message.args[0].strValue();
		String editable = message.args[1].strValue().trim().toLowerCase();
		return tree.setEditableText(identity, editable);
	}

	/**
	 * Enable drag.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean enableDrag(Message message) {
		String identity = message.args[0].strValue();
		return tree.enableDrag(identity);
	}

	/**
	 * Enable drop.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean enableDrop(Message message) {
		String identity = message.args[0].strValue();
		return tree.enableDrop(identity);
	}

	/**
	 * Gets the handler.
	 *
	 * @return the handler
	 */
	public EventHandler getHandler() {
		return handler;
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#deselected(java.lang.String)
	 */
	public void deselected(String identity) {
		Message m = handler.newMessage("deselected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#doubleSelected(java.lang.String)
	 */
	public void doubleSelected(String identity) {
		Message m = handler.newMessage("doubleSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	// public IWorkbenchPartSite getSite() {
	// return site;
	// }

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#selected(java.lang.String)
	 */
	public void selected(String identity) {
		Message m = handler.newMessage("selected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#treeExpanded(java.lang.String)
	 */
	public void treeExpanded(String identity) {
		Message m = handler.newMessage("expanded", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/* (non-Javadoc)
	 * @see com.ceteva.forms.views.FormTreeHandler#textChanged(java.lang.String, java.lang.String)
	 */
	public void textChanged(String identity, String text) {
		Message m = handler.newMessage("textChanged", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(text);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/*
	 * private boolean checkDragDelay() { long currentTime =
	 * System.currentTimeMillis(); if (currentTime - dragDelayTimer > 1000) {
	 * dragDelayTimer = currentTime; return true; } return false; }
	 */

	/**
	 * Synchronise.
	 *
	 * @param browser the browser
	 */
	public void synchronise(Element browser) {
		expanded = new Vector();
		synchronise(browser, getIdentity());
		for (int i = 0; i < expanded.size(); i++) {
			String expandId = (String) expanded.elementAt(i);
			tree.expandNode(expandId, true);
		}
	}

	/**
	 * Synchronise.
	 *
	 * @param doctree the doctree
	 * @param parentId the parent id
	 */
	public void synchronise(Element doctree, String parentId) {

		// Check that there is a node for each of the document nodes

		for (int i = 0; i < doctree.childrenSize(); i++) {
			Element child = (Element) doctree.getChild(i);
			String id = child.getString("identity");
			if (child.hasName(XMLBindings.node)) {
				int index = child.getInteger("index");
				String icon = child.getString("icon");
				String text = child.getString("text");
				String toolTipText = child.getString("toolTipText");
				boolean editable = child.getBoolean("editable");
				boolean expanded = child.getBoolean("expanded");
				boolean selected = child.getBoolean("selected");

				if (expanded)
					this.expanded.add(id);

				if (!tree.parentHasChild(parentId, id)) {

					// Add a new node for the identity

					tree.addNode(parentId, index, id, icon, text, toolTipText,
							editable, expanded, selected);
				}

				// Update the node

				tree.updateNode(id, index, icon, text, toolTipText, editable,
						expanded, selected);
			}
		}

		// Check that each of the browser nodes has a node in the document

		Vector children = tree.getChildren(parentId);
		Vector delete = new Vector();
		for (int i = 0; i < children.size(); i++) {
			boolean found = false;
			String identity = (String) children.elementAt(i);
			for (int z = 0; z < doctree.childrenSize(); z++) {
				Element child = (Element) doctree.getChild(z);
				if (child.hasName(XMLBindings.node)) {
					String id = child.getString("identity");
					if (id.equals(identity))
						found = true;
				}
			}
			if (!found)
				delete.add(identity);
		}
		for (int i = 0; i < delete.size(); i++) {
			tree.removeNode((String) delete.elementAt(i));
		}

		for (int i = 0; i < doctree.childrenSize(); i++) {
			Element child = (Element) doctree.getChild(i);
			String id = child.getString("identity");
			if (child.hasName(XMLBindings.node)) {
				synchronise(child, id);
			}
		}
	}

}