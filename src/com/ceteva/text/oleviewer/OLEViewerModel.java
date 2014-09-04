package com.ceteva.text.oleviewer;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

public class OLEViewerModel extends ClientElement {

	OLEViewer viewer;

	public OLEViewerModel(String identity, EventHandler handler,
			OLEViewer viewer) {
		super(null, handler, identity);
		this.viewer = viewer;
	}

	public boolean processMessage(Message message) {
		if (message.args[0].hasStrValue(identity)) {
			if (message.hasName("saveAs")) {
				String filename = message.args[1].strValue();
				viewer.saveAs(filename);
				return true;
			}
		}
		return false;
	}

}
