package com.ceteva.menus;

import java.util.Vector;

import org.eclipse.draw2d.InputEvent;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPartSite;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

import com.ceteva.menus.actions.GlobalMenuAction;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuBuilder.
 */
public class MenuBuilder {

	/**
	 * Adds the listener.
	 *
	 * @param item the item
	 * @param elementIdentity the element identity
	 * @param menuIdentity the menu identity
	 */
	private static void addListener(MenuItem item,
			final String elementIdentity, final String menuIdentity) {
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				EventHandler handler = MenusClient.handler;
				Message m = handler.newMessage("rightClickMenuSelected", 2);
				Value v1 = new Value(menuIdentity);
				Value v2 = new Value(elementIdentity);
				m.args[0] = v1;
				m.args[1] = v2;
				handler.raiseEvent(m);
			}
		});
	}

	/* *** START *** */

	// New machinery for dealing with calculating menus based on multiple
	// selection

	/**
	 * Calculate key bindings.
	 *
	 * @param manager the manager
	 * @param site the site
	 */
	public static void calculateKeyBindings(IMenuManager manager,
			IWorkbenchPartSite site) {
		// if (site != null)
		{
			IContributionItem[] ici = manager.getItems();
			for (int i = 0; i < ici.length; i++) {
				IContributionItem item = ici[i];
				if (item instanceof ActionContributionItem) {
					ActionContributionItem actionItem = (ActionContributionItem) item;
					// KeyBindingBuilder.addKeyBinding(site,
					// actionItem.getAction());
				}
			}
		}
	}

	/**
	 * Calculate menu.
	 *
	 * @param identities the identities
	 * @param menu the menu
	 * @param manager the manager
	 */
	public static void calculateMenu(Vector identities, GlobalMenu menu,
			IMenuManager manager) {
		Vector menus = menu.getSubMenus();
		for (int i = 0; i < menus.size(); i++) {
			com.ceteva.menus.Menu m = (com.ceteva.menus.Menu) menus
					.elementAt(i);
			calculateMenu(identities, manager, m);
		}
	}

	/**
	 * Calculate menu.
	 *
	 * @param identities the identities
	 * @param manager the manager
	 * @param site the site
	 */
	public static void calculateMenu(Vector identities, IMenuManager manager,
			IWorkbenchPartSite site) {
		Vector globalMenus = new Vector();
		for (int i = 0; i < identities.size(); i++) {

			// Get the global menu for each of the selected elements
			// At this point we know that the element has a menu

			String identity = (String) identities.elementAt(i);
			globalMenus.add(MenuManager.getGlobalMenu(identity));
		}

		if (globalMenus.size() == 1) {

			// There is only a single global menu, use that

			GlobalMenu globalMenu = (GlobalMenu) globalMenus.elementAt(0);
			calculateMenu(identities, globalMenu, manager);
			calculateKeyBindings(manager, site);
		} else if (globalMenus.size() > 1) {

			// At this point we know we have more than one global menu
			// Create a single new GlobalMenu based on the intersection of the
			// given menus.

			GlobalMenu newgm = new GlobalMenu("");
			constructMultiMenu(newgm, globalMenus);
			calculateMenu(identities, newgm, manager);
			calculateKeyBindings(manager, site);
		}
	}

	/**
	 * Construct multi menu.
	 *
	 * @param menu the menu
	 * @param menus the menus
	 * @return true, if successful
	 */
	public static boolean constructMultiMenu(com.ceteva.menus.Menu menu,
			Vector menus) {

		if (!menus.isEmpty()) {

			// Get the first menu and use as comparison point

			com.ceteva.menus.Menu m = (com.ceteva.menus.Menu) menus
					.elementAt(0);

			// Compare each sub menu

			for (int i = 0; i < m.getSubMenus().size(); i++) {

				Vector menuIdentities = new Vector();
				Vector subMenus = new Vector();
				com.ceteva.menus.Menu sm = (com.ceteva.menus.Menu) m
						.getSubMenus().elementAt(i);
				String mname = sm.getName();
				String mid = sm.getIdentity();
				String hid = sm.getHandlerPointId();
				boolean supportsMulti = sm.supportsMulti();
				menuIdentities.add(mid);
				subMenus.addElement(sm);

				// Compare each menu to the first

				boolean keep = true;
				// boolean conflict = false;

				for (int z = 1; z < menus.size(); z++) {
					com.ceteva.menus.Menu m2 = (com.ceteva.menus.Menu) menus
							.elementAt(z);
					if (!m2.hasSubMenu(mname) || !supportsMulti) {
						keep = false;
					} else {
						com.ceteva.menus.Menu m3 = m2.getMenu(mname);
						String mid3 = m3.getIdentity();
						String hid3 = m3.getHandlerPointId();
						if (!hid3.equals(hid) || !supportsMulti
								|| !m3.supportsMulti())
							// conflict = true;
							keep = false;
						menuIdentities.add(mid3);
						subMenus.addElement(m3);
					}
				}
				if (keep) {
					com.ceteva.menus.Menu newMenu = new com.ceteva.menus.Menu(
							menuIdentities, mname);
					// newMenu.setEnabled(!conflict);
					menu.addMenu(newMenu);
					if (!constructMultiMenu(newMenu, subMenus))
						menu.removeMenu(newMenu);
				}
			}
			if (menu.getSubMenus().isEmpty() && !m.getSubMenus().isEmpty())
				return false;
			else
				return true;
		}
		return true;
	}

	// End of new machinery

	/* *** END *** */

	/**
	 * Calculate menu.
	 *
	 * @param elementIdentity the element identity
	 * @param manager the manager
	 * @param site the site
	 */
	public static void calculateMenu(String elementIdentity,
			IMenuManager manager, IWorkbenchPartSite site) {
		GlobalMenu globalMenu = MenuManager.getGlobalMenu(elementIdentity);
		if (globalMenu != null) {
			Vector menus = globalMenu.getSubMenus();
			for (int i = 0; i < menus.size(); i++) {
				com.ceteva.menus.Menu m = (com.ceteva.menus.Menu) menus
						.elementAt(i);
				Vector identities = new Vector();
				identities.addElement(elementIdentity);
				calculateMenu(identities, manager, m);
			}

			// deal with key bindings

			// if (site != null)
			{
				IContributionItem[] ici = manager.getItems();
				for (int i = 0; i < ici.length; i++) {
					IContributionItem item = ici[i];
					if (item instanceof ActionContributionItem) {
						ActionContributionItem actionItem = (ActionContributionItem) item;
						// KeyBindingBuilder.addKeyBinding(site,
						// actionItem.getAction());
					}
				}
			}
		}
	}

	/**
	 * Calculate menu.
	 *
	 * @param elementIdentity the element identity
	 * @param parent the parent
	 */
	public static void calculateMenu(String elementIdentity, Menu parent) {
		clearMenu(parent);
		GlobalMenu globalMenu = MenuManager.getGlobalMenu(elementIdentity);
		if (globalMenu != null) {
			Vector menus = globalMenu.getSubMenus();
			for (int i = 0; i < menus.size(); i++) {
				com.ceteva.menus.Menu m = (com.ceteva.menus.Menu) menus
						.elementAt(i);
				calculateMenu(elementIdentity, parent, m);
			}
		}
	}

	/**
	 * Calculate menu.
	 *
	 * @param elementIdentity the element identity
	 * @param parent the parent
	 * @param m the m
	 */
	private static void calculateMenu(String elementIdentity, Menu parent,
			com.ceteva.menus.Menu m) {
		if (m.isParent()) {
			MenuItem item = new MenuItem(parent, SWT.CASCADE);
			Menu newMenu = new Menu(item);
			item.setText(m.getName());
			item.setEnabled(m.isEnabled());
			item.setMenu(newMenu);
			Vector menus = m.getSubMenus();
			for (int i = 0; i < menus.size(); i++) {
				com.ceteva.menus.Menu sm = (com.ceteva.menus.Menu) menus
						.elementAt(i);
				calculateMenu(elementIdentity, newMenu, sm);
			}
		} else {
			MenuItem item = new MenuItem(parent, SWT.NONE);
			item.setText(m.getName());
			item.setEnabled(m.isEnabled());
			addListener(item, elementIdentity, m.getIdentity());
		}
	}

	/**
	 * Calculate menu.
	 *
	 * @param identities the identities
	 * @param parent the parent
	 * @param m the m
	 */
	private static void calculateMenu(Vector identities, IMenuManager parent,
			com.ceteva.menus.Menu m) {
		if (m.isParent()) {
			org.eclipse.jface.action.MenuManager newManager = new org.eclipse.jface.action.MenuManager(
					m.getName());
			parent.add(newManager);
			Vector menus = m.getSubMenus();
			for (int i = 0; i < menus.size(); i++) {
				com.ceteva.menus.Menu sm = (com.ceteva.menus.Menu) menus
						.elementAt(i);
				calculateMenu(identities, newManager, sm);
			}
		} else {
			GlobalMenuAction action = new GlobalMenuAction(m.getName(),
					m.getIdentities(), identities, m.getEnabled());
			if (!identities.isEmpty()) {
				String identity = (String) m.getIdentities().elementAt(0);
				
//				if (identity.equalsIgnoreCase("56")) {
//					action.setAccelerator(SWT.CTRL+'S');
//					action.setEnabled(true);
//				}
			}
			parent.add(action);
		}
	}

	/**
	 * Clear menu.
	 *
	 * @param menu the menu
	 */
	private static void clearMenu(Menu menu) {
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++)
			items[i].dispose();
	}

	/**
	 * Dispose.
	 *
	 * @param site the site
	 */
	public static void dispose(IWorkbenchPartSite site) {
		KeyBindingBuilder.resetKeyBindings(site);
	}

	/**
	 * Checks for menu.
	 *
	 * @param elementIdentity the element identity
	 * @return true, if successful
	 */
	public static boolean hasMenu(String elementIdentity) {
		GlobalMenu globalMenu = MenuManager.getGlobalMenu(elementIdentity);
		return globalMenu != null;
	}

	/**
	 * Reset key bindings.
	 *
	 * @param site the site
	 */
	public static void resetKeyBindings(IWorkbenchPartSite site) {
		if (site != null)
			KeyBindingBuilder.resetKeyBindings(site);
	}
}