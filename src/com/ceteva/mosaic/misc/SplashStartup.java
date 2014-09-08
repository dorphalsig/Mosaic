package com.ceteva.mosaic.misc;

import java.io.File;
import java.io.FileFilter;
import java.util.Hashtable;

import com.ceteva.mosaic.splash.Splash;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashStartup.
 */
public class SplashStartup {
	
	/**
	 * Ini splash.
	 */
	public static void iniSplash() {
		Hashtable<?, ?> imagechoices = getImages();
		Splash splash = new Splash("icons" + "/splash/splash.bmp", imagechoices);
		splash.show();
		String choosenimage = splash.choosenImage();
		// if (choosenimage != "")
		// xmfplugin.setImage(choosenimage);
		// else
		// xmfplugin.setImage(imagedefault);
	}

	/**
	 * Gets the images.
	 *
	 * @return the images
	 */
	private static Hashtable getImages() {
		String workingDir = System.getProperty("user.dir");
		String imagesDir = "F:\\xmf\\XMF-xmf-integration\\com.ceteva.xmf.system\\xmf-img";
		final Hashtable fileTable = new Hashtable();
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".img");
			}
		};
		if (imagesDir != null && !imagesDir.equals("")) {
			File dir = new File(imagesDir);
			if (dir.exists()) {
				File[] files = dir.listFiles(filter);
				if (files.length > 0) {
					final String[] imageFiles = new String[files.length];
					for (int i = 0; i < files.length; i++) {
						String fileName = files[i].getName();
						String filePath = files[i].toString();
						imageFiles[i] = fileName;
						fileTable.put(fileName, filePath);
					}
				}
			}
		}
		return fileTable;
	}
}
