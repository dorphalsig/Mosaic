package uk.ac.mdx.xmf.swt.client;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class IconManager {

	private static String graphicsDir = "icons";

	private static Hashtable iconCache = new Hashtable();

	public static Image getImageFromFileAbsolute(String filename) {
		if (iconCache.containsKey(filename))
			return (Image) iconCache.get(filename);
		else {
			Image image = ImageDescriptor.createFromFile(null,
					"/icons/" + filename).createImage();
			return image;
		}
	}

	public static ImageDescriptor getImageDescriptorAbsolute(String filename) {

		return ImageDescriptor.createFromFile(null, filename);
	}

	public static void dispose() {
		Enumeration e = iconCache.elements();
		while (e.hasMoreElements()) {
			Image i = (Image) e.nextElement();
			i.dispose();
		}
	}
}