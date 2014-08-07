package com.ceteva.modelBrowser.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.IconManager;

import com.ceteva.modelBrowser.views.ModelBrowserView;

public class CollapseNodes extends Action {

	ModelBrowserView browser = null;

	public CollapseNodes(ModelBrowserView browser) {
		this.setId("com.ceteva.modelBrowser.actions.CollapseNodes");
		this.browser = browser;
		setText("Collapse nodes");
		setToolTipText("Collapse all nodes");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("Collapse.gif"));
	}

	public void run() {
		browser.collapseAllNodes();
	}

	public void update() {
		this.setEnabled(browser != null);
	}
}