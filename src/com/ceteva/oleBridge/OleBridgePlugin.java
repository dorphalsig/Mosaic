package com.ceteva.oleBridge;


public class OleBridgePlugin {

	private static OleBridgePlugin plugin;

	public OleBridgePlugin() {
		plugin = this;
	}

	// public void start(BundleContext context) throws Exception {
	// super.start(context);
	// }
	//
	// public void stop(BundleContext context) throws Exception {
	// super.stop(context);
	// plugin = null;
	// }

	public static OleBridgePlugin getDefault() {
		return plugin;
	}
	/*
	 * public void earlyStartup() { OleBridgeClient client = new
	 * OleBridgeClient();
	 * XmfPlugin.XOS.newMessageClient("com.ceteva.oleBridge",client); }
	 */

}
