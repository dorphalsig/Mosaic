package uk.ac.mdx.xmf.swt.client;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class IconManager.
 */
public class IconManager {

	/** The graphics dir. */
	private static String graphicsDir = "icons";

	/** The icon cache. */
	private static Hashtable iconCache = new Hashtable();

	/**
	 * Gets the image from file absolute.
	 *
	 * @param filename the filename
	 * @return the image from file absolute
	 */
	public static Image getImageFromFileAbsolute(String filename) {
		if (iconCache.containsKey(filename))
			return (Image) iconCache.get(filename);
		else {
			Image image = ImageDescriptor.createFromFile(null,
					"/icons/" + filename).createImage();
			return image;
		}
	}

	/**
	 * Gets the image descriptor absolute.
	 *
	 * @param filename the filename
	 * @return the image descriptor absolute
	 */
	public static ImageDescriptor getImageDescriptorAbsolute(String filename) {

		return ImageDescriptor.createFromFile(null, filename);
	}

	/**
	 * Dispose.
	 */
	public static void dispose() {
		Enumeration e = iconCache.elements();
		while (e.hasMoreElements()) {
			Image i = (Image) e.nextElement();
			i.dispose();
		}
	}
}