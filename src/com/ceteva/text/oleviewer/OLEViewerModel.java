package com.ceteva.text.oleviewer;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class OLEViewerModel.
 */
public class OLEViewerModel extends ClientElement {

	/** The viewer. */
	OLEViewer viewer;

	/**
	 * Instantiates a new OLE viewer model.
	 *
	 * @param identity the identity
	 * @param handler the handler
	 * @param viewer the viewer
	 */
	public OLEViewerModel(String identity, EventHandler handler,
			OLEViewer viewer) {
		super(null, handler, identity);
		this.viewer = viewer;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#processMessage(xos.Message)
	 */
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
