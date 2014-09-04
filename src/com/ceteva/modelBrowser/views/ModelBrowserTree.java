package com.ceteva.modelBrowser.views;

import org.eclipse.swt.widgets.Composite;

import uk.ac.mdx.xmf.swt.client.EventHandler;

import com.ceteva.forms.views.FormTree;

public class ModelBrowserTree extends FormTree {

	public ModelBrowserTree(Composite parent, String identity,
			EventHandler handler, boolean editable) {
		super(parent, identity, handler, editable, true);
	}

	// public AbstractUIPlugin getPlugin() {
	// return ModelBrowserPlugin.getDefault();
	// }

	/*
	 * protected void deselectNode(TreeItem item) { item.setExpanded(false);
	 * selectionChanged(); }
	 */

	/*
	 * public boolean setGlobalMenu(Message message) { String targetId =
	 * message.args[0].strValue(); String globalMenuId =
	 * message.args[1].strValue(); if(nodeExists(targetId)) return
	 * MenuManager.bindGlobalMenu(globalMenuId,targetId); return false; }
	 */

	/*
	 * public void selectionChanged() { MenuBuilder.resetKeyBindings(site);
	 * TreeItem item = getSelectedNode(); Menu m = tree.getMenu(); if(m!=null)
	 * m.dispose(); if(item != null) { String identity = (String)
	 * item.getData(); org.eclipse.jface.action.MenuManager menu = new
	 * org.eclipse.jface.action.MenuManager();
	 * MenuBuilder.calculateMenu(identity,menu,site);
	 * tree.setMenu(menu.createContextMenu(tree)); } }
	 */

}
