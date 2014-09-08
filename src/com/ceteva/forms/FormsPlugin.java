package com.ceteva.forms;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class FormsPlugin.
 */
public class FormsPlugin {

	/** The plugin. */
	private static FormsPlugin plugin;
	// font
	/** The default font. */
	public static FontData defaultFont;
	
	/** The font. */
	public static Font font;
	// font colours
	/** The normal background color. */
	public static Color normalBackgroundColor;
	
	/** The disabled background color. */
	public static Color disabledBackgroundColor;
	
	/** The modified background color. */
	public static Color modifiedBackgroundColor;
	// syntax highlighting colours
	/** The red. */
	public static Color RED;
	
	/** The green. */
	public static Color GREEN;
	
	/** The blue. */
	public static Color BLUE;
	
	/** The black. */
	public static Color BLACK;
	
	/** The instance. */
	private static FormsPlugin instance = null;

	/**
	 * Instantiates a new forms plugin.
	 */
	public FormsPlugin() {
		defaultFont = new FontData(
				"1|Courier New|9|0|WINDOWS|1|-13|0|0|0|400|0|0|0|0|3|2|1|49|Courier New");
		// font = new Font(Display.getCurrent(),defaultFont);
		font = FontManager.getFont(defaultFont);
		normalBackgroundColor = ColorManager.getColor(new RGB(255, 255, 255));
		disabledBackgroundColor = ColorManager.getColor(new RGB(222, 221, 220));
		modifiedBackgroundColor = ColorManager.getColor(new RGB(221, 171, 160));
		RED = ColorManager.getColor(new RGB(255, 0, 0));
		GREEN = ColorManager.getColor(new RGB(0, 255, 0));
		BLUE = ColorManager.getColor(new RGB(0, 0, 255));
		BLACK = ColorManager.getColor(new RGB(0, 0, 0));
		plugin = this;
	}

	/**
	 * Gets the single instance of FormsPlugin.
	 *
	 * @return single instance of FormsPlugin
	 */
	public static FormsPlugin getInstance() {
		if (instance == null) {
			instance = new FormsPlugin();
		}
		return instance;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static FormsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Stop.
	 *
	 * @throws Exception the exception
	 */
	public void stop() throws Exception {
		modifiedBackgroundColor.dispose();
		font.dispose();
		RED.dispose();
		GREEN.dispose();
		BLUE.dispose();
		BLACK.dispose();
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		FormsClient client = new FormsClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.forms", client);
	}
}
