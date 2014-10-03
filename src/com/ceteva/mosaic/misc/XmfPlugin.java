package com.ceteva.mosaic.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import com.ceteva.mosaic.util.PropertiesCaseInsensitive;

import xos.OperatingSystem;

// TODO: Auto-generated Javadoc
/**
 * The Class XmfPlugin.
 */
public class XmfPlugin {
	
	/**
	 * Possible file names for reading the configuration file from. Lowest priority first.
	 * @see #getEnvVar(String)
	 */
	public static final String[] CONFIGURATION_FILES = new String[] { "/etc/xmodeler.conf", 
																	  "C:\\ProgramData\\XModeler\\xmodeler.conf", 
																	  System.getProperty("user.home") + File.separator + ".xmodeler.conf",
																	  System.getProperty("user.home") + File.separator + "xmodeler.conf" };

	/** The Constant COMMAND_LINE_ARGS. */
	public static final String COMMAND_LINE_ARGS = "startup.txt";

	/** The plugin. */
	public static XmfPlugin plugin = null;
	
	/**
	 * Configuration loaded from configuration file, if available.
	 */
	public static Properties configuration;

	/** The xos. */
	public OperatingSystem xos = new OperatingSystem();

	/** The version. */
	public static String version = "undefined";

	/** The image file. */
	public static String imageFile = null;

	/**
	 * Instantiates a new xmf plugin.
	 */
	public XmfPlugin() {
		plugin = this;
		MosaicRun.runningMosaic();
	}

	// public
	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static XmfPlugin getDefault() {
		return plugin;
	}

	/**
	 * Gets the install directory.
	 *
	 * @return the install directory
	 */
	public String getInstallDirectory() {
		return null;
		// URL installUrl = Platform.getInstallLocation().getURL();
		// return installUrl.getFile().toString();
	}

	/**
	 * Gets the value of a user-configurable parameter from a configuration
	 * file, or from an environment variable on Windows systems.
	 * 
	 * A global configuration file can either be stored in /etc/xmodeler.conf
	 * or C:\ProgramData\XModeler\xmodeler.conf (on Windows), to set 
	 * parameters for all users running XModeler on that system.
	 * 
	 * Per-user configuration files can be stored in either 
	 * <home-dir>/.xmodeler.conf or <home-dir>/xmodeler.conf. If a per-user
	 * configuration file exists, any global configuration file is ignored.
	 * 
	 * On Windows operating systems, it is also possible to use
	 * environment variables for setting the desired configuration parameters.
	 * However, since environment variables are deprecated and considered 
	 * anachronistic on any OS, using a configuration file is recommended.
	 * 
	 * The format of the configuration file is straight-forward:
	 * 
	 * <parameter1>=<value1>
	 * <parameter2>=<value2>
	 * ...
	 * 
	 * Parameter names are treated case-insensitive to reduce possible 
	 * configuration mistakes. Blanks in value strings are allowed.
	 * 
	 * TODO This mechanism may be consolidated with the configuration of
	 * startup arguments read from <home>/Mosaic/startup.txt.
	 *
	 * @param name parameter name to get the configured string value for 
	 * @return configured value, or null if the parameter has not been set
	 */
	public static String getEnvVar(String name) {
		String value = "";

		if (configuration == null) { // lazy init
			// find conf file
			File configurationFile = null;
			for (String filename : CONFIGURATION_FILES) {
				File file = new File(filename);
				if (file.exists()) {
					configurationFile = file; // higher priority would overwrite
				}
			}
			if (configurationFile != null) { // found a conf file
				System.out.println("Loading configuration from "+configurationFile.getAbsolutePath());
				Properties conf = new PropertiesCaseInsensitive();
				try {
					FileReader reader = new FileReader(configurationFile);
					conf.load(reader);
					reader.close();
					configuration = conf;
				} catch (IOException ioe) {
					ioe.printStackTrace(System.err);
				}
			}
		}

		if (configuration != null) {
			value = configuration.getProperty(name);
		}
		
		// else fall back to deprecated query of Windows environment variable
		
		// Only works on the windows platform

		else if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
			// Accesses the Windows environment variable with the given
			// name. Returns the value of the variable or "" if it is not
			// defined.

			try {
				String var = "%" + name + "%";
				Process p = Runtime.getRuntime().exec("cmd.exe /c echo " + var);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String myvar = br.readLine();
				p.destroy();
				br.close();
				value = myvar.equals(var) ? "" : myvar;
			} catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		}
		return value;
	}

	/**
	 * Gets the runtime directory.
	 *
	 * @return the runtime directory
	 */
	public String getRuntimeDirectory() {
		URL installUrl = null;
		// try {
		// installUrl = FileLocator.resolve(this.getBundle().getEntry("/"));
		// } catch (IOException iox) {
		// System.out.println(iox);
		// }

		// Platform gets the path with a leading space i.e
		// \C:\eclipse\workspace\...
		// since the path is often used as the basis of comparison to test when
		// a
		// file is being edited, the leading "\" must be removed. Note this may
		// be an issue when Mosaic is used on other platforms than windows

		// return installUrl.getFile().substring(1);
		return installUrl.getFile();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		XmfPlugin xmf = new XmfPlugin();
		System.out.println("start");
		xmf.earlyStartup();
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		System.out.println("[ Early Startup XmfPlugin ]");
		Thread t = new Thread() {
			@Override
			public void run() {
				String projDir = getEnvVar("XMFPROJECTS");
				// String demoDir = getEnvVar("MOSAICDEMOS");
				// String initFile = getEnvVar("MOSAICINIT");
				// String imagesDir = getEnvVar("XMFIMAGES");
				// String dirSlash = getRuntimeDirectory();
				// String dir = dirSlash.substring(0, dirSlash.length() - 1);
				// String user = System.getProperties().getProperty("user.name")
				// .replaceAll(" ", "\\\\ ");
				// String[] args = readStartupArgs(dir, user, projDir, demoDir,
				// imagesDir, initFile, XmfPlugin.imageFile);
				String[] args = null;
				// try {
				// args = xos.readTxt("F:\\xmf\\code\\args.txt");
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				try {
					// registerEscapeHandler();
					xos.init(args);

				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		t.start();
	}

	/**
	 * Read startup args.
	 *
	 * @param home the home
	 * @param user the user
	 * @param projDir the proj dir
	 * @param demoDir the demo dir
	 * @param imagesDir the images dir
	 * @param initFile the init file
	 * @param image the image
	 * @return the string[]
	 */
	public String[] readStartupArgs(String home, String user, String projDir,
			String demoDir, String imagesDir, String initFile, String image) {
		String[] args = null;
		try {
			String argFile = home + "/Mosaic/" + COMMAND_LINE_ARGS;
			FileInputStream fin = new FileInputStream(argFile);
			BufferedReader bin = new BufferedReader(new InputStreamReader(fin));
			String argLines = "-arg user:" + user;
			argLines = argLines + " -arg home:" + home.replaceAll(" ", "\\\\ ");
			argLines = argLines + " -image " + image.replaceAll(" ", "\\\\ ");
			argLines = initFile.equals("") ? argLines : argLines
					+ " -arg initFile:" + initFile.replaceAll(" ", "\\\\ ");
			if (projDir != null)
				argLines = argLines + " -arg projects:"
						+ projDir.replaceAll(" ", "\\\\ ");
			if (demoDir != null)
				argLines = argLines + " -arg demos:"
						+ demoDir.replaceAll(" ", "\\\\ ");
			if (imagesDir != null)
				argLines = argLines + " -arg images:"
						+ imagesDir.replaceAll(" ", "\\\\ ");
			String line = "";
			String prefString = "-arg prefs:";
			while ((line = bin.readLine()) != null) {
				line = line.trim();
				if (!line.startsWith("//"))
					if (line.startsWith("-pref"))
						prefString = prefString
								+ line.substring(5).replace(':', '=').trim()
								+ ",";
					else
						argLines = argLines + " " + line;
			}
			if (!prefString.equals("-arg prefs:"))
				argLines = argLines + " " + prefString;
			argLines = argLines.replaceAll("\\\\ ", "SPACE");
			args = argLines.split(" ");
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].replaceAll("SPACE", " ");
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
			System.exit(0);
		}

		// store the version number to be referenced from other places

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("version:"))
				version = arg.replaceAll("version:", "");
		}
		return args;
	}

	/**
	 * Register escape handler.
	 */
	public void registerEscapeHandler() {
		try {

			// This is done using reflection since the console plugin might not
			// exist
			// in a given install of an XMF-Mosaic based tool

			// Class c = XmfPlugin.getDefault().getBundle()
			// .loadClass("com.ceteva.console.views.ConsoleView");
			Class c = Class.forName("com.ceteva.console.views.ConsoleView");
			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = EscapeHandler.class;
			Method setEscapeHandler = c.getMethod("setEscapeHandler",
					parameterTypes);

			Object[] parameters = new Object[1];
			InterruptHandler handler = new InterruptHandler(xos);
			parameters[0] = handler;
			setEscapeHandler.invoke(null, parameters);

		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		} catch (NoSuchMethodException nsm) {
			nsm.printStackTrace();
		} catch (IllegalAccessException iax) {
			iax.printStackTrace();
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		}
	}

	/**
	 * Gets the default image.
	 *
	 * @return the default image
	 */
	public String getDefaultImage() {
		String dirSlash = getRuntimeDirectory();
		String dir = dirSlash.substring(0, dirSlash.length() - 1);
		return dir + "/Images/mosaic.img";
	}

	/**
	 * Gets the images.
	 *
	 * @return the images
	 */
	public Hashtable getImages() {
		String imagesDir = getEnvVar("XMFIMAGES");
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

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(String image) {
		imageFile = image;
	}

	/**
	 * Stop.
	 *
	 * @throws Exception the exception
	 */
	public void stop() throws Exception {
		if (xos != null)
			xos.closeAll();
		// super.stop(context);
	}

}