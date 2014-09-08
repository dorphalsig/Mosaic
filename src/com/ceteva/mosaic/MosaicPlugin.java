package com.ceteva.mosaic;


// TODO: Auto-generated Javadoc
/**
 * The Class MosaicPlugin.
 */
public class MosaicPlugin {

	/** The plugin. */
	private static MosaicPlugin plugin;

	/**
	 * Instantiates a new mosaic plugin.
	 */
	public MosaicPlugin() {
		plugin = this;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public static MosaicPlugin getDefault() {
		return plugin;
	}

	/**
	 * Early startup.
	 */
	public void earlyStartup() {
		// WorkbenchClient client = new WorkbenchClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.mosaic",client);
	}
}