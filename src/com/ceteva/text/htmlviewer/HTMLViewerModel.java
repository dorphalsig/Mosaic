package com.ceteva.text.htmlviewer;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class HTMLViewerModel.
 */
public class HTMLViewerModel extends ClientElement {

	/** The viewer. */
	HTMLViewer viewer;

	/**
	 * Instantiates a new HTML viewer model.
	 *
	 * @param identity the identity
	 * @param handler the handler
	 * @param viewer the viewer
	 */
	public HTMLViewerModel(String identity, EventHandler handler,
			HTMLViewer viewer) {
		super(null, handler, identity);
		this.viewer = viewer;
	}

	/**
	 * Instantiates a new HTML viewer model.
	 *
	 * @param identity the identity
	 * @param handler the handler
	 */
	public HTMLViewerModel(String identity, EventHandler handler) {
		super(null, handler, identity);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	public void delete() {
		// viewer.delete();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (message.arity > 0) {
			if (message.args[0].hasStrValue(identity)) {
				if (message.hasName("delete") && message.arity == 1) {
					viewer.delete();
					return true;
				}
				if (message.hasName("setUrl") && message.arity == 2) {
					String url = message.args[1].strValue();
					viewer.setURL(url);
					return true;
				}
				if (message.hasName("setName") && message.arity == 2) {
					String name = message.args[1].strValue();
					viewer.setName(name);
					return true;
				}
				if (message.hasName("setTooltip") && message.arity == 2) {
					String tooltip = message.args[1].strValue();
					viewer.setToolTip(tooltip);
					return true;
				}
				if (message.hasName("setFocus") && message.arity == 1) {
					viewer.setFocusInternal();
					return true;
				}
			}
		}
		return false;
	}

}