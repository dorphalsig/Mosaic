package com.ceteva.console;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import com.ceteva.console.preferences.IPreferenceConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class ConsolePlugin.
 */
public class ConsolePlugin {

	/** The plugin. */
	private static ConsolePlugin plugin;
	
	/** The default font. */
	private FontData defaultFont = new FontData(
			"1|Courier New|10|0|WINDOWS|1|-13|0|0|0|400|0|0|0|0|3|2|1|49|Courier New");
	
	/** The out. */
	private static BufferedWriter out;
	
	/** The write to file. */
	private static boolean writeToFile = false;

	/**
	 * Instantiates a new console plugin.
	 */
	public ConsolePlugin() {
		plugin = this;
		if (writeToFile) {
			try {
				out = new BufferedWriter(new FileWriter("c:\\temp\\xmflog.txt"));
				Date currentDate = new java.util.Date();
				out.write("###" + currentDate.toString() + "\n");
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static ConsolePlugin getDefault() {
		return plugin;
	}

	/**
	 * Initialize default preferences.
	 *
	 * @param store the store
	 */
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		PreferenceConverter.setDefault(store,
				IPreferenceConstants.CONSOLE_BACKGROUND, new RGB(0, 0, 128));
		PreferenceConverter.setDefault(store,
				IPreferenceConstants.CONSOLE_FONT_COLOUR,
				new RGB(255, 255, 255));
		PreferenceConverter.setDefault(store,
				IPreferenceConstants.CONSOLE_FONT, defaultFont);
		store.setDefault(IPreferenceConstants.LINE_LIMIT, 5000);
		store.setDefault(IPreferenceConstants.COMMAND_HISTORY_LIMIT, 10);
	}

	/**
	 * Stop.
	 *
	 * @throws Exception the exception
	 */
	public void stop() throws Exception {
		// super.stop(context);
		if (writeToFile)
			out.close();
	}

	/**
	 * Write to file.
	 *
	 * @param string the string
	 */
	public static void writeToFile(String string) {
		if (writeToFile) {
			try {
				out.write(string + "\n");
				out.flush();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
