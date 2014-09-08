package com.ceteva.mosaic;

import org.eclipse.ui.IStartup;

// TODO: Auto-generated Javadoc
/**
 * The Class MosaicStart.
 */
public class MosaicStart implements IStartup {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup() {
		WorkbenchClient client = new WorkbenchClient();
		// XmfPlugin.xos.newMessageClient("com.ceteva.mosaic",client);
	}
}
