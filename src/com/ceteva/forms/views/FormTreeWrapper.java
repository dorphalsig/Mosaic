package com.ceteva.forms.views;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import uk.ac.mdx.xmf.swt.client.IconManager;

import com.ceteva.menus.MenuBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class FormTreeWrapper.
 */
public class FormTreeWrapper {

	/** The identity. */
	private String identity;
	
	/** The handler. */
	private FormTreeHandler handler;
	
	/** The tree. */
	private Tree tree = null;
	
	/** The all nodes. */
	private Vector allNodes = new Vector();
	
	/** The all menus. */
	private Vector allMenus = new Vector();
	
	/** The non editable nodes. */
	private Vector nonEditableNodes;
	
	/** The tool tip table. */
	private Hashtable toolTipTable = new Hashtable();
	
	/** The edit text. */
	private Text editText;
	
	/** The current selection. */
	private TreeItem currentSelection = null;

	/**
	 * Instantiates a new form tree wrapper.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param handler the handler
	 * @param editable the editable
	 * @param multiSelect the multi select
	 */
	public FormTreeWrapper(Composite parent, String identity,
			FormTreeHandler handler, boolean editable, boolean multiSelect) {
		int multiMode = multiSelect ? SWT.MULTI : SWT.SINGLE;
		tree = new Tree(parent, SWT.BORDER | multiMode);
		this.nonEditableNodes = new Vector();
		if (editable)
			FormTreeEditListener.addListener(tree, nonEditableNodes, this);
		this.identity = identity;
		this.handler = handler;
		addTreeListener();

		// tree.setSize((int) (parent.getBounds().width),
		// parent.getBounds().height);
	}

	/**
	 * Adds the expansion listener.
	 */
	private void addExpansionListener() {
		tree.addTreeListener(new TreeListener() {
			public void treeExpanded(TreeEvent e) {
				String identity = (String) e.item.getData();
				handler.treeExpanded(identity);
			}

			public void treeCollapsed(TreeEvent e) {
			}
		});
	}

	/**
	 * Adds the focus listener.
	 */
	private void addFocusListener() {
		tree.addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				selectionChanged();
			}
		});
		tree.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event e) {
				selectionChanged();
			}
		});
	}

	/**
	 * Adds the mouse listener.
	 */
	private void addMouseListener() {
		tree.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				String identity = getSelectedNodeIdentity();
				if (identity != null) {
					handler.doubleSelected(identity);
				}
			}
		});
		tree.addListener(SWT.MouseHover, new Listener() {
			public void handleEvent(Event e) {
				TreeItem item = tree.getItem(new Point(e.x, e.y));
				String text = "";
				if (item != null) {
					String parentID = (String) item.getData();
					if (toolTipTable.containsKey(parentID))
						text = (String) toolTipTable.get(parentID);
					else
						text = item.getText();
				}
				if (!text.equals(tree.getToolTipText()))
					tree.setToolTipText(text);
			}
		});
	}

	/**
	 * Adds the node.
	 *
	 * @param parentId the parent id
	 * @param index the index
	 * @param nodeId the node id
	 * @param icon the icon
	 * @param text the text
	 * @param toolTipText the tool tip text
	 * @param editable the editable
	 * @param expanded the expanded
	 * @param selected the selected
	 * @return true, if successful
	 */
	public boolean addNode(String parentId, int index, String nodeId,
			String icon, String text, String toolTipText, boolean editable,
			boolean expanded, boolean selected) {
		Widget parent = getParent(parentId);
		if (icon.equals(""))
			return addNode(parent, nodeId, text, toolTipText, false, index) != null;
		else
			return addNodeWithIcon(parent, nodeId, text, toolTipText, editable,
					icon, index) != null;
	}

	/**
	 * Adds the node.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param text the text
	 * @param toolTipText the tool tip text
	 * @param editableString the editable string
	 * @param index the index
	 * @return the tree item
	 */
	private TreeItem addNode(Widget parent, String identity, String text,
			String toolTipText, boolean editableString, int index) {
		TreeItem item;
		if (parent instanceof Tree) {
			Tree parentTree = (Tree) parent;
			int pos = index >= 0 ? index : parentTree.getItemCount();
			item = new TreeItem(parentTree, SWT.NONE, pos);
		} else if (parent instanceof TreeItem) {
			TreeItem parentItem = (TreeItem) parent;
			int pos = index >= 0 ? index : parentItem.getItemCount();
			item = new TreeItem(parentItem, SWT.NONE, pos);
		} else
			return null;
		item.setData(identity);
		item.setText(text);
		allNodes.add(item);
		boolean editable = (new Boolean(editableString)).booleanValue();
		if (editable)
			nonEditableNodes.remove(identity);
		else if (!nonEditableNodes.contains(identity))
			nonEditableNodes.add(identity);
		if (toolTipText != "")
			toolTipTable.put(identity, toolTipText);
		return item;
	}

	/**
	 * Adds the node with icon.
	 *
	 * @param parent the parent
	 * @param identity the identity
	 * @param text the text
	 * @param toolTipText the tool tip text
	 * @param editable the editable
	 * @param icon the icon
	 * @param indexString the index string
	 * @return the tree item
	 */
	private TreeItem addNodeWithIcon(Widget parent, String identity,
			String text, String toolTipText, boolean editable, String icon,
			int indexString) {
		TreeItem item = addNode(parent, identity, text, toolTipText, editable,
				indexString);
		if (item != null) {
			// The icons directory should be set via a command line argument...
			if (!icon.endsWith(".gif"))
				icon = icon + ".gif";
			if(!icon.startsWith("icons"))
				icon = "icons/" + icon;
			item.setImage(new Image(parent.getDisplay(), icon));
		}
		return item;
	}

	/**
	 * Adds the selection listener.
	 */
	private void addSelectionListener() {
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				TreeItem item = getSelectedNode();
				if (item != currentSelection) {
					if (currentSelection != null) {
						String identity = currentSelection.getData().toString();
						handler.deselected(identity);
					}
					if (item != null) {
						String identity = item.getData().toString();
						handler.selected(identity);
					}
					currentSelection = item;
				}
				selectionChanged();
			}
		});
	}

	/**
	 * Adds the tree listener.
	 */
	private void addTreeListener() {
		addFocusListener();
		addSelectionListener();
		addMouseListener();
		addExpansionListener();
	}

	/**
	 * Deselect node.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean deselectNode(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			if (node == currentSelection) {
				node.setExpanded(false);
				selectionChanged();
			}
			return true;
		}
		return false;
	}

	/**
	 * Enable drag.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean enableDrag(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			handler.enableDrag();
			return true;
		}
		return false;
	}

	/**
	 * Enable drop.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean enableDrop(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			handler.enableDrop();
			return true;
		}
		return false;
	}

	/**
	 * Expand node.
	 *
	 * @param identity the identity
	 * @param expand the expand
	 * @return true, if successful
	 */
	public boolean expandNode(String identity, boolean expand) {
		TreeItem node = getNode(identity);
		if (node != null) {
			node.setExpanded(expand);
			return true;
		}
		return false;
	}

	/**
	 * Gets the children.
	 *
	 * @param identity the identity
	 * @return the children
	 */
	public Vector getChildren(String identity) {
		Vector children = new Vector();
		TreeItem[] items = null;
		if (identity.equals(this.identity))
			items = tree.getItems();
		else
			items = getNode(identity).getItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				TreeItem item = (TreeItem) items[i];
				children.add((String) item.getData());
			}
		}
		return children;
	}

	/**
	 * Gets the editable text.
	 *
	 * @param identity the identity
	 * @return the editable text
	 */
	public void getEditableText(String identity) {
		handler.getEditableText(identity);
	}

	/**
	 * Gets the node.
	 *
	 * @param identity the identity
	 * @return the node
	 */
	private TreeItem getNode(String identity) {
		for (int i = 0; i < allNodes.size(); i++) {
			TreeItem item = (TreeItem) allNodes.elementAt(i);
			String nodeID = (String) item.getData();
			if (nodeID.equals(identity))
				return item;
		}
		return null;
	}

	/**
	 * Gets the node identity.
	 *
	 * @param item the item
	 * @return the node identity
	 */
	public String getNodeIdentity(TreeItem item) {
		if (item != null)
			return (String) item.getData();
		return null;
	}

	/**
	 * Gets the node identity at.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the node identity at
	 */
	public String getNodeIdentityAt(int x, int y) {
		TreeItem item = tree.getItem(new Point(x, y));
		if (item != null)
			return (String) item.getData();
		return null;
	}

	/**
	 * Gets the parent.
	 *
	 * @param identity the identity
	 * @return the parent
	 */
	private Widget getParent(String identity) {
		if (identity.equals(this.identity))
			return tree;
		else
			return getNode(identity);
	}

	/**
	 * Gets the parent id.
	 *
	 * @param identity the identity
	 * @return the parent id
	 */
	public String getParentId(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			TreeItem treeItem = node.getParentItem();
			if (treeItem != null)
				return (String) treeItem.getData();
			Tree tree = node.getParent();
			if (tree != null)
				return (String) tree.getData();
		}
		return null;
	}

	/**
	 * Gets the selected identity.
	 *
	 * @param selection the selection
	 * @return the selected identity
	 */
	private Vector getSelectedIdentity(List selection) {
		Vector selected = new Vector();

		// if zero parts selected then no identity is returned
		if (selection.size() == 0)
			return selected;

		// we know there is at least one part selected
		Iterator selections = selection.iterator();
		while (selections.hasNext()) {
			TreeItem item = (TreeItem) selections.next();
			String id = (String) item.getData();
			if (MenuBuilder.hasMenu(id))
				selected.addElement(id);
		}
		return selected;
	}

	/**
	 * Gets the selected node.
	 *
	 * @return the selected node
	 */
	private TreeItem getSelectedNode() {
		TreeItem[] selected = tree.getSelection();
		if (selected.length > 0)
			return selected[0];
		return null;
	}

	/**
	 * Gets the selected node identities string.
	 *
	 * @return the selected node identities string
	 */
	public String getSelectedNodeIdentitiesString() {
		List items = Arrays.asList(getSelectedNodes());
		Vector ids = getSelectedIdentity(items);
		return ids.toString();
	}

	/**
	 * Gets the selected node identity.
	 *
	 * @return the selected node identity
	 */
	public String getSelectedNodeIdentity() {
		TreeItem item = getSelectedNode();
		if (item != null) {
			String identity = (String) item.getData();
			return identity;
		}
		return null;
	}

	/**
	 * Gets the selected nodes.
	 *
	 * @return the selected nodes
	 */
	private TreeItem[] getSelectedNodes() {
		return tree.getSelection();
	}

	/**
	 * Gets the tree.
	 *
	 * @return the tree
	 */
	public Control getTree() {
		return tree;
	}

	/**
	 * Index of node.
	 *
	 * @param identity the identity
	 * @return the int
	 */
	public int indexOfNode(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			TreeItem treeItem = node.getParentItem();
			if (treeItem != null)
				return treeItem.indexOf(node);
			Tree tree = node.getParent();
			if (tree != null)
				return tree.indexOf(node);
		}
		return -1;
	}

	/**
	 * Move node.
	 *
	 * @param node the node
	 * @param index the index
	 * @param identity the identity
	 * @param icon the icon
	 * @param text the text
	 * @param toolTipText the tool tip text
	 * @param editable the editable
	 * @param expanded the expanded
	 * @param selected the selected
	 */
	public void moveNode(TreeItem node, int index, String identity,
			String icon, String text, String toolTipText, boolean editable,
			boolean expanded, boolean selected) {
		String parentId = getParentId(identity);
		if (parentId != null) {
			removeNode(node);
			addNode(parentId, index, identity, icon, text, toolTipText,
					editable, expanded, selected);
		}
	}

	/**
	 * Parent has child.
	 *
	 * @param parentId the parent id
	 * @param childId the child id
	 * @return true, if successful
	 */
	public boolean parentHasChild(String parentId, String childId) {
		TreeItem[] children;
		if (parentId.equals(identity))
			children = tree.getItems();
		else
			children = getNode(parentId).getItems();
		for (int i = 0; i < children.length; i++) {
			String id = (String) children[i].getData();
			if (id.equals(childId))
				return true;
		}
		return false;
	}

	/**
	 * Removes the node.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean removeNode(String identity) {
		TreeItem node = getNode(identity);
		if (node != null) {
			removeNode(node);
			return true;
		}
		return false;
	}

	/**
	 * Removes the node.
	 *
	 * @param item the item
	 */
	private void removeNode(TreeItem item) {
		item.dispose();
		Iterator ns = allNodes.iterator();
		Vector disposed = new Vector();
		while (ns.hasNext()) {
			TreeItem node = (TreeItem) ns.next();
			if (node.isDisposed())
				disposed.add(node);
		}
		ns = disposed.iterator();
		while (ns.hasNext()) {
			TreeItem node = (TreeItem) ns.next();
			allNodes.remove(node);
		}
		if (currentSelection != null && currentSelection.isDisposed())
			currentSelection = null;
		tree.deselectAll();
		FormTreeEditListener.setLastItem(null);
	}

	/**
	 * Selection changed.
	 */
	private void selectionChanged() {
		MenuBuilder.resetKeyBindings(null);
		List items = Arrays.asList(getSelectedNodes());
		Menu m = tree.getMenu();
		if (m != null)
			m.dispose();
		MenuManager menu = new org.eclipse.jface.action.MenuManager();

		MenuBuilder.calculateMenu(getSelectedIdentity(items), menu, null);
		tree.setMenu(menu.createContextMenu(tree));
	}

	/**
	 * Select node.
	 *
	 * @param identity the identity
	 * @param expand the expand
	 * @return true, if successful
	 */
	public boolean selectNode(String identity, boolean expand) {
		TreeItem node = getNode(identity);
		if (node != null) {
			tree.setSelection(new TreeItem[] { node });
			if (expand)
				node.setExpanded(true);
			selectionChanged();
			return true;
		}
		return false;
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
		tree.setBounds(x, y, width, height);
	}

	/**
	 * Sets the edits the text.
	 *
	 * @param text the new edits the text
	 */
	public void setEditText(Text text) {
		editText = text;
	}

	/**
	 * Sets the editable.
	 *
	 * @param identity the identity
	 * @param editable the editable
	 * @return true, if successful
	 */
	public boolean setEditable(String identity, boolean editable) {
		TreeItem node = getNode(identity);
		if (node != null) {
			if (editable)
				nonEditableNodes.remove(node);
			else
				nonEditableNodes.add(node);
			return true;
		}
		return false;
	}

	/**
	 * Sets the editable text.
	 *
	 * @param identity the identity
	 * @param text the text
	 * @return true, if successful
	 */
	public boolean setEditableText(String identity, String text) {
		TreeItem node = getNode(identity);
		if (node != null) {
			if (editText != null && !editText.isDisposed()) {
				editText.setText(text);
				editText.selectAll();
			}
			return true;
		}
		return false;
	}

	/**
	 * Sets the focus.
	 */
	public void setFocus() {
		tree.setFocus();
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * Sets the icon.
	 *
	 * @param identity the identity
	 * @param icon the icon
	 * @return true, if successful
	 */
	public boolean setIcon(String identity, String icon) {
		TreeItem node = getNode(identity);
		if (node != null) {
			if (!icon.endsWith(".gif"))
				icon = icon + ".gif";
			node.setImage(IconManager.getImageFromFileAbsolute(icon));
			return true;
		}
		return false;
	}

	/**
	 * Sets the text.
	 *
	 * @param identity the identity
	 * @param text the text
	 * @return true, if successful
	 */
	public boolean setText(String identity, String text) {
		TreeItem node = getNode(identity);
		if (node != null) {
			node.setText(text);
			return true;
		}
		return false;
	}

	/**
	 * Sets the tool tip text.
	 *
	 * @param identity the identity
	 * @param text the text
	 * @return true, if successful
	 */
	public boolean setToolTipText(String identity, String text) {
		TreeItem node = getNode(identity);
		if (node != null) {
			toolTipTable.put(identity, text);
			return true;
		}
		return false;
	}

	/**
	 * Text changed.
	 *
	 * @param identity the identity
	 * @param text the text
	 */
	public void textChanged(String identity, String text) {
		handler.textChanged(identity, text);
	}

	/**
	 * Update node.
	 *
	 * @param identity the identity
	 * @param index the index
	 * @param icon the icon
	 * @param text the text
	 * @param toolTipText the tool tip text
	 * @param editable the editable
	 * @param expanded the expanded
	 * @param selected the selected
	 */
	public void updateNode(String identity, int index, String icon,
			String text, String toolTipText, boolean editable,
			boolean expanded, boolean selected) {
		TreeItem node = getNode(identity);
		int currentIndex = indexOfNode(identity);
		if (currentIndex != index)
			moveNode(node, index, identity, icon, text, toolTipText, editable,
					expanded, selected);
		toolTipTable.put(identity, toolTipText);
		if (editable)
			nonEditableNodes.addElement(identity);
		else
			nonEditableNodes.remove(identity);

		// Expanded and selected are currently ignored
	}
}
