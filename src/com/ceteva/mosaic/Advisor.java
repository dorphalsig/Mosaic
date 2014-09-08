package com.ceteva.mosaic;

import java.util.Hashtable;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.ceteva.mosaic.misc.XmfPlugin;
import com.ceteva.mosaic.perspectives.PerspectiveManager;
import com.ceteva.mosaic.splash.Splash;

// TODO: Auto-generated Javadoc
/**
 * The Class Advisor.
 */
public class Advisor extends WorkbenchAdvisor {

	/** The icon path. */
	public static String iconPath = "icons/";
	
	/** The icon name. */
	public static String iconName = "mosiacSmall.gif";
	
	/** The product. */
	public static IProduct product = Platform.getProduct();
	
	/** The splash. */
	public static Splash splash = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new WindowAdvisor(configurer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	public String getInitialWindowPerspectiveId() {
		return PerspectiveManager.standardPerspective;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
	 */
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		System.out.println("[ Application Initialize ]");
		configurer.setSaveAndRestore(false);
		setCurvedLook();
		XmfPlugin xmfplugin = XmfPlugin.getDefault();
		String imagedefault = xmfplugin.getDefaultImage();
		Hashtable imagechoices = xmfplugin.getImages();
		splash = new Splash(splashPath() + "/splash/splash.bmp", imagechoices);
		splash.show();
		String choosenimage = splash.choosenImage();
		if (choosenimage != "")
			xmfplugin.setImage(choosenimage);
		else
			xmfplugin.setImage(imagedefault);
	}

	/**
	 * Sets the curved look.
	 */
	public void setCurvedLook() {
		IPreferenceStore store = PlatformUI.getPreferenceStore();
		store.setValue(
				IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
				false);
		store.setValue(IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
				IWorkbenchPreferenceConstants.TOP_RIGHT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
	 */
	public void postStartup() {
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setMaximized(true);
		/*
		 * LicenseManager manager = LicenseManager.getInstance(); try {
		 * if(!manager.isValid()) reportInvalidLicense(); }
		 * catch(GeneralSecurityException gse) { reportInvalidLicense(); }
		 */
	}

	/**
	 * Report invalid license.
	 */
	public static void reportInvalidLicense() {
		splash.dispose();
		String text = "Mosaic License is invalid";
		Display display = Display.getCurrent();
		Shell shell = new Shell(display);
		MessageDialog.openError(shell, "Mosaic Error", text);
		display.dispose();
		System.exit(0);
	}

	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}

	/**
	 * Splash path.
	 *
	 * @return the string
	 */
	public String splashPath() {
		// URL installURL =
		// MosaicPlugin.getDefault().getBundle().getEntry("/");;
		// try {
		// URL installUrl = Platform.resolve(installURL);
		// return installUrl.getPath();
		// }
		// catch(IOException iox) {
		// System.out.println(iox);
		// }
		return null;
	}
}
