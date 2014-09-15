package com.ceteva.modelBrowser;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.XMLClient;
import uk.ac.mdx.xmf.swt.client.xml.Document;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.demo.Main;
import xos.Message;
import xos.Value;

import com.ceteva.modelBrowser.views.ModelBrowserView;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelBrowserClient.
 */
public class ModelBrowserClient extends XMLClient {

	/** The view. */
	ModelBrowserView view = null;
	
	/** The open browsers. */
	Hashtable openBrowsers = new Hashtable();
	
	/** The handler. */
	public EventHandler handler = null;

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#setEventHandler(uk.ac.mdx.xmf.swt.client.EventHandler)
	 */
	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	/**
	 * Instantiates a new model browser client.
	 */
	public ModelBrowserClient() {
		super("com.ceteva.modelBrowser");
	}

	/**
	 * Browser added.
	 *
	 * @param id the id
	 * @param browser the browser
	 */
	public void browserAdded(String id, ModelBrowserView browser) {
		openBrowsers.put(id, browser);
	}

	/**
	 * Browser closed.
	 *
	 * @param id the id
	 * @param browser the browser
	 */
	public void browserClosed(String id, ModelBrowserView browser) {
		openBrowsers.remove(id);
	}

	/**
	 * Browser exists.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean browserExists(String id) {
		return openBrowsers.containsKey(id);
	}

	// public IViewReference[] getAllForms() {
	// return getPage().getViewReferences();
	// }

	/**
	 * New browser.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean newBrowser(Message message) {
		String identity = message.args[0].strValue();
		String type = message.args[1].strValue();
		String name = message.args[2].strValue();
		if (newBrowser(identity, type, name, true, true) != null)
			return true;
		else
			return false;
	}

	/** The Constant ID_DIV. */
	private static final String ID_DIV = ".";

	/**
	 * New browser.
	 *
	 * @param identity the identity
	 * @param type the type
	 * @param name the name
	 * @param closable the closable
	 * @param hasFocus the has focus
	 * @return the model browser view
	 */
	public ModelBrowserView newBrowser(String identity, String type,
			String name, boolean closable, boolean hasFocus) {
		String id = type + ID_DIV + identity;

		CTabItem tabItem = new CTabItem(Main.tabFolderOutline, SWT.BORDER);
		tabItem.setText(name);
		Canvas c = new Canvas(Main.tabFolderOutline, SWT.BORDER);
		tabItem.setControl(c);

		final ModelBrowserView browser = new ModelBrowserView();
		browser.createPartControl(c);
		System.err.println("Modelbrowser added with id: "
				+ browser.getIdentity() + " id:" + id);
		browser.setName(name);
		browser.setType(type);
		browser.setIdentity(identity);
		browser.registerEventHandler(handler);
		browser.setClient(this);
		browserAdded(identity, browser);
		// if(closable) // Not sure what needs to be done here
		if (hasFocus)
			browser.focusGained();
		
		Main.tabFolderOutline.setSelection(tabItem);
		Main.sectionTopLeft.setFocus();
		return browser;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#processCall(xos.Message)
	 */
	public Value processCall(Message message) {
		Enumeration e = openBrowsers.elements();
		while (e.hasMoreElements()) {
			ModelBrowserView browser = (ModelBrowserView) e.nextElement();
			Value value = browser.processCall(message);
			if (value != null)
				return value;
		}
		return new Value(false);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.XMLClient#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (super.processMessage(message))
			return true;
		if (message.hasName("newModelBrowser"))
			return newBrowser(message);
		else if (message.hasName("setVisible") && message.arity == 1)
			return setVisible(message);
		else if (message.hasName("setFocus") && message.arity == 1)
			return setFocus(message);
		else {
			Enumeration e = openBrowsers.elements();
			while (e.hasMoreElements()) {
				ModelBrowserView browser = (ModelBrowserView) e.nextElement();
				if (browser.processMessage(message))
					return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.XMLClient#processXML(uk.ac.mdx.xmf.swt.client.xml.Document)
	 */
	public void processXML(Document xml) {
		// xml.printString();
		synchroniseBrowsers(xml);
	}

	/**
	 * Sets the focus.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setFocus(Message message) {
		// String identity = message.args[0].strValue();
		// if (openBrowsers.containsKey(identity)) {
		// ModelBrowserView browser = (ModelBrowserView) openBrowsers
		// .get(identity);
		// try {
		// String id = browser.getType() + ID_DIV + identity;
		// IViewPart part = getPage().showView(ModelBrowserView.ID, id,
		// IWorkbenchPage.VIEW_ACTIVATE);
		// handler.setCommandMode(false);
		// return part != null;
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }
		// }
		return false;
	}

	/**
	 * Sets the visible.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setVisible(Message message) {
		// String identity = message.args[0].strValue();
		// if (browserExists(identity)) {
		// ModelBrowserView browser = (ModelBrowserView) openBrowsers
		// .get(identity);
		// try {
		// String id = browser.getType() + ID_DIV + identity;
		// IViewPart part = getPage().showView(ModelBrowserView.ID, id,
		// IWorkbenchPage.VIEW_VISIBLE);
		// page.bringToTop(part);
		// handler.setCommandMode(false);
		// return part != null;
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }
		// }
		return false;
	}

	/**
	 * Synchronise browsers.
	 *
	 * @param xml the xml
	 */
	public void synchroniseBrowsers(Element xml) {

		// check that there is a browser for each of the document's browsers

		for (int i = 0; i < xml.childrenSize(); i++) {
			Element child = xml.getChild(i);
			if (child.hasName(XMLBindings.browser)) {
				boolean isOpen = child.getBoolean("isOpen");
				if (isOpen) {
					String identity = child.getString("identity");
					String type = child.getString("type");
					if (openBrowsers.containsKey(identity)) {

						// Update the existing one

						ModelBrowserView browser = (ModelBrowserView) openBrowsers
								.get(identity);
						browser.synchronise(child);
					} else {

						// Create a new one

						String name = child.getString("name");
						boolean closable = child.getBoolean("closable");
						boolean hasFocus = child.getBoolean("hasFocus");
						ModelBrowserView browser = newBrowser(identity, type,
								name, closable, hasFocus);
						browser.synchronise(child);
					}
				}
			}
		}

		// check that there is an open browser in the document for each of the
		// client's browsers

		// needs to be implemented
	}

	// private IWorkbenchPage getPage() {
	// if (page == null)
	// page = ModelBrowserPlugin.getDefault().getWorkbench()
	// .getActiveWorkbenchWindow().getActivePage();
	// return page;
	// }

}