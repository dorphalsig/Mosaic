package com.ceteva.modelBrowser.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.IconManager;

import com.ceteva.modelBrowser.views.ModelBrowserView;

// TODO: Auto-generated Javadoc
/**
 * The Class CollapseNodes.
 */
public class CollapseNodes extends Action {

	/** The browser. */
	ModelBrowserView browser = null;

	/**
	 * Instantiates a new collapse nodes.
	 *
	 * @param browser the browser
	 */
	public CollapseNodes(ModelBrowserView browser) {
		this.setId("com.ceteva.modelBrowser.actions.CollapseNodes");
		this.browser = browser;
		setText("Collapse nodes");
		setToolTipText("Collapse all nodes");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("Collapse.gif"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		browser.collapseAllNodes();
	}

	/**
	 * Update.
	 */
	public void update() {
		this.setEnabled(browser != null);
	}
}