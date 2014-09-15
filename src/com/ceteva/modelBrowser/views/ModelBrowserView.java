package com.ceteva.modelBrowser.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;
import xos.Value;

import com.ceteva.menus.MenuBuilder;
import com.ceteva.modelBrowser.ModelBrowserClient;
import com.ceteva.modelBrowser.actions.CollapseNodes;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelBrowserView.
 */
public class ModelBrowserView {

	/** The Constant ID. */
	public static final String ID = "com.ceteva.modelBrowser.view";

	/** The type. */
	String type = "";
	
	/** The identity. */
	String identity = "";
	
	/** The tree holder. */
	Composite treeHolder;
	
	/** The tree. */
	com.ceteva.modelBrowser.views.ModelBrowserTree tree;
	
	/** The client. */
	ModelBrowserClient client = null;
	
	/** The handler. */
	EventHandler handler = null;
	
	/** The closable. */
	boolean closable = true;

	/**
	 * Instantiates a new model browser view.
	 */
	public ModelBrowserView() {
		// registerAsListener();
	}

	// public void registerAsListener() {
	// IWorkbenchPage page = PlatformUI.getWorkbench()
	// .getActiveWorkbenchWindow().getActivePage();
	// if (page != null)
	// page.addPartListener(this);
	// }

	// public void unregisterAsListener() {
	// IWorkbenchPage page = PlatformUI.getWorkbench()
	// .getActiveWorkbenchWindow().getActivePage();
	// if (page != null)
	// page.removePartListener(this);
	// }

	/**
	 * Dispose.
	 */
	public void dispose() {
		// super.dispose();
//		 unregisterAsListener();
		 tree.getControl().dispose();
//		 MenuBuilder.dispose(getSite());
	}

	/**
	 * Sets the focus.
	 */
	public void setFocus() {
		// tree.setFocus();
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		// this.setPartName(name);
		// this.setTitleToolTip(name);
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
		if (tree != null)
			tree.setIdentity(identity);
	}

	/**
	 * Collapse all nodes.
	 */
	public void collapseAllNodes() {
		// TreeItem[] items = ((Tree) tree.getControl()).getItems();
		// for (int i = 0; i < items.length; i++) {
		// TreeItem item = items[i];
		// collapseNodes(item);
		// }
	}

	/**
	 * Collapse nodes.
	 *
	 * @param item the item
	 */
	public void collapseNodes(TreeItem item) {
		item.setExpanded(false);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++) {
			TreeItem child = items[i];
			collapseNodes(child);
		}
	}

	/**
	 * Focus gained.
	 */
	public void focusGained() {
		if (handler != null) {
			Message m = handler.newMessage("focusGained", 1);
			Value v = new Value(getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Focus lost.
	 */
	public void focusLost() {
		if (handler != null) {
			Message m = handler.newMessage("focusLost", 1);
			Value v = new Value(getIdentity());
			m.args[0] = v;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Tree closed.
	 */
	public void treeClosed() {
		client.browserClosed(getIdentity(), this);
		Message m = handler.newMessage("modelBrowserClosed", 1);
		Value v = new Value(getIdentity());
		m.args[0] = v;
		handler.raiseEvent(m);
	}

	/**
	 * Close model browser.
	 */
	public void closeModelBrowser() {
		// IWorkbenchPage page = ModelBrowserPlugin.getDefault().getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.hideView(this);
		treeClosed();
	}

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/*
	 * public String getToolTipText() { return this.getPartName(); }
	 */

	/**
	 * Fill local tool bar.
	 *
	 * @param manager the manager
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		CollapseNodes collapse = new CollapseNodes(this);
		manager.add(collapse);
	}

	/**
	 * Creates the part control.
	 *
	 * @param parent the parent
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		if (tree == null)
			tree = new ModelBrowserTree(parent, identity, handler,

			true);
		// GUIDemo.getInstance().outlineView.layout(true);
		// IActionBars bars = getViewSite().getActionBars();
		// fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * Register event handler.
	 *
	 * @param handler the handler
	 */
	public void registerEventHandler(EventHandler handler) {
		this.handler = handler;
		if (tree != null)
			tree.setEventHandler(handler);
	}

	/**
	 * Process call.
	 *
	 * @param message the message
	 * @return the value
	 */
	public Value processCall(Message message) {
		return new Value(false);
	}

	/**
	 * Process message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean processMessage(Message message) {
		if (message.args[0].hasStrValue(identity)) {
			if (message.hasName("setName") && message.arity == 2) {
				String name = message.args[1].strValue();
				setName(name);
				handler.setCommandMode(false);
				return true;
			} else if (message.hasName("closeModelBrowser")
					&& message.arity == 1) {
				closeModelBrowser();
				return true;
			} else
				return broadcastCommand(message);
		} else
			return broadcastCommand(message);
	}

	/**
	 * Broadcast command.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean broadcastCommand(Message message) {
		return tree.processMessage(message);

		// GUIDemo.outlineView.addContents("icons\\XCore\\package.gif");
		// GUIDemo.outlineView.addContents("icons\\XCore\\operation.gif");
		// GUIDemo.outlineView.createPartControl();
		// GUIDemo.outlineView.layout();
		// return true;
	}

	// public void propertyChange(PropertyChangeEvent e) {
	// String preference = e.getProperty();
	// if (preference.equals(IPreferenceConstants.INVOKE_PROPERTY_EDITOR)
	// || preference
	// .equals(IPreferenceConstants.INVOKE_DIAGRAM_EDITOR)) {
	// Message m = handler.newMessage(preference + "ActionChange", 1);
	// Value v1 = new Value((String) e.getNewValue());
	// m.args[0] = v1;
	// handler.raiseEvent(m);
	// }
	// }

	/*
	 * public void init() { ModelBrowserPlugin .getDefault()
	 * .getPreferenceStore() .addPropertyChangeListener(this); }
	 */

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(ModelBrowserClient client) {
		this.client = client;
	}

	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public ModelBrowserClient getClient() {
		return client;
	}

	// public void partActivated(IWorkbenchPartReference partRef) {
	// if (partRef.getPart(false).equals(this) && handler != null)
	// focusGained();
	// }
	//
	// public void partBroughtToTop(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partClosed(IWorkbenchPartReference partRef) {
	// if (partRef.getPart(false).equals(this) && handler != null)
	// treeClosed();
	// }
	//
	// public void partDeactivated(IWorkbenchPartReference partRef) {
	// if (partRef.getPart(false) != null) {
	// if (partRef.getPart(false).equals(this) && handler != null)
	// focusLost();
	// }
	// }
	//
	// public void partOpened(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partHidden(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partVisible(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partInputChanged(IWorkbenchPartReference partRef) {
	// }

	/**
	 * Synchronise.
	 *
	 * @param xml the xml
	 */
	public void synchronise(Element xml) {
		tree.synchronise(xml);
	}

}