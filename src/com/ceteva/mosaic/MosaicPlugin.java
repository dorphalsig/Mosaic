package com.ceteva.mosaic;


public class MosaicPlugin {

	private static MosaicPlugin plugin;

	public MosaicPlugin() {
		plugin = this;
	}

	public static MosaicPlugin getDefault() {
		return plugin;
	}

	public void earlyStartup() {
		// WorkbenchClient client = new WorkbenchClient();
		// XmfPlugin.XOS.newMessageClient("com.ceteva.mosaic",client);
	}
}