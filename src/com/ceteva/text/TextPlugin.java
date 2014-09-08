package com.ceteva.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.misc.ColorManager;

import com.ceteva.text.preferences.IPreferenceConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class TextPlugin.
 */
public class TextPlugin {

	/** The plugin. */
	private static TextPlugin plugin;

	/** The partitioner. */
	public static String PARTITIONER = "default_partitioner";

	/** The red. */
	public static Color RED = ColorManager.getColor(new RGB(255, 0, 0));
	//public static Color GREEN = ColorManager.getColor(new RGB(63, 127, 95));
	/** The green. */
	public static Color GREEN = ColorManager.getColor(new RGB(0, 147, 0));
	
	/** The blue. */
	public static Color BLUE = ColorManager.getColor(new RGB(0, 0, 255));
	
	/** The black. */
	public static Color BLACK = ColorManager.getColor(new RGB(0, 0, 0));

	/**
	 * Instantiates a new text plugin.
	 */
	public TextPlugin() {
		plugin = this;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static TextPlugin getDefault() {
		return plugin;
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		EditorClient client = new EditorClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.text", client);
	}

	/**
	 * Initialize default preferences.
	 *
	 * @param store the store
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		// font
		PreferenceConverter.setDefault(store, JFaceResources.TEXT_FONT,
				new FontData("Courier New", 10, SWT.NORMAL));

		// colors
		PreferenceConverter
				.setDefault(store, IPreferenceConstants.CURRENT_LINE_COLOR,
						new RGB(232, 242, 254));
		PreferenceConverter.setDefault(store,
				IPreferenceConstants.HIGHLIGHT_LINE_COLOR, new RGB(221, 171,
						160));

		// line numbers
		store.setDefault(IPreferenceConstants.LINE_NUMBERS, false);
	}

	// public void stop(BundleContext context) throws Exception {
	// RED.dispose();;
	// GREEN.dispose();
	// BLUE.dispose();
	// BLACK.dispose();
	// super.stop(context);
	// }

	/*
	 * public String getHelpDirectory() { return
	 * HelpPlugin.getDefault().getHelpPath(); }
	 */
}
