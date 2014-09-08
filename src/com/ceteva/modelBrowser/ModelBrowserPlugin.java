package com.ceteva.modelBrowser;

import org.eclipse.jface.preference.IPreferenceStore;

import com.ceteva.modelBrowser.preferences.IPreferenceConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelBrowserPlugin.
 */
public class ModelBrowserPlugin
// extends AbstractUIPlugin implements org.eclipse.ui.IStartup
{

	/** The plugin. */
	private static ModelBrowserPlugin plugin;

	/**
	 * Instantiates a new model browser plugin.
	 */
	public ModelBrowserPlugin() {
		plugin = this;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static ModelBrowserPlugin getDefault() {
		return plugin;
	}

	/**
	 * Initialize default preferences.
	 *
	 * @param store the store
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(IPreferenceConstants.INVOKE_PROPERTY_EDITOR,
				"doubleClick");
		store.setDefault(IPreferenceConstants.INVOKE_DIAGRAM_EDITOR, "none");
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		// ModelBrowserClient client = new ModelBrowserClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.modelBrowser",client);
	}
}
