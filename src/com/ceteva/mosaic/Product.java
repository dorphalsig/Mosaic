package com.ceteva.mosaic;

import java.net.MalformedURLException;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * The Class Product.
 */
public class Product {

	/**
	 * Gets the application.
	 *
	 * @return the application
	 */
	public String getApplication() {
		return "com.ceteva.mosaic";
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return "XMF-Mosaic";
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return "";
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return "com.ceteva.mosaic.Product";
	}

	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public String getProperty(String key) {
		System.out.println("Requesting: " + key);
		/*
		 * if(key.equals(IProductConstants.APP_NAME)) return "XMF-Mosaic";
		 * if(key.equals(IProductConstants.ABOUT_IMAGE)) return iconPath() +
		 * "mosaicLogo.gif"; else if(key.equals(IProductConstants.ABOUT_TEXT))
		 * return aboutText(); else
		 * if(key.equals(IProductConstants.WINDOW_IMAGES)) return iconPath() +
		 * "mosaic16.gif";
		 */
		return null;
	}

	/**
	 * Icon path.
	 *
	 * @return the string
	 */
	public String iconPath() {
		String path = "";
		// URL installURL = MosaicPlugin.getDefault().getBundle().getEntry("/");
		URL installURL = null;
		try {
			URL url = new URL(installURL, "icons/");
			path = url.toString();
		} catch (MalformedURLException ex) {
			System.out.println(ex);
		}
		return path;
	}

	/**
	 * About text.
	 *
	 * @return the string
	 */
	public String aboutText() {
		/*
		 * String line1 = "XMF-Mosaic\n"; String line2 = "Version " +
		 * XmfPlugin.version + "\n"; String line3 = "\n"; String line4 =
		 * "(c) Ceteva Ltd. 2003 - 2005.  All rights reserved\n"; String line5 =
		 * "http://www.ceteva.com\n"; return line1+line2+line3+line4+line5;
		 */
		return "";
	}

	// public Bundle getDefiningBundle() {
	// return MosaicPlugin.getDefault().getBundle();
	// }
}
