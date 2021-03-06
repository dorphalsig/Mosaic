package uk.ac.mdx.xmf.swt.misc;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import uk.ac.mdx.xmf.swt.demo.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class FontManager.
 */
public class FontManager {

	/** The fontbinding. */
	private static Hashtable fontbinding = new Hashtable();

	/**
	 * Gets the font.
	 *
	 * @param data the data
	 * @return the font
	 */
	public static Font getFont(FontData data) {
		// String name = data.getName() + data.getStyle() +
		// data.data.lfUnderline;
		// String datastring = data.toString();
		// if (fontbinding.containsKey(datastring)) {
		// return (Font) fontbinding.get(datastring);
		// } else {
		// Font f = new Font(Display.getCurrent(), data);
		// fontbinding.put(datastring, f);
		// return f;
		// }

		Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
		return classFont;
	}

	/**
	 * Gets the font.
	 *
	 * @param data the data
	 * @param flag the flag
	 * @return the font
	 */
	public static Font getFont(FontData data, boolean flag) {

		return new Font(Main.getInstance().display, data);

	}

	/**
	 * Dispose.
	 */
	public static void dispose() {
		Enumeration e = fontbinding.elements();
		while (e.hasMoreElements()) {
			Font f = (Font) e.nextElement();
			f.dispose();
		}
	}
}