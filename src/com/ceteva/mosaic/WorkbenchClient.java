package com.ceteva.mosaic;

import org.eclipse.swt.widgets.Display;

import uk.ac.mdx.xmf.swt.client.Client;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

import com.ceteva.mosaic.perspectives.PerspectiveManager;

// TODO: Auto-generated Javadoc
/**
 * The Class WorkbenchClient.
 */
public class WorkbenchClient extends Client {

	/** The perspective manager. */
	PerspectiveManager perspectiveManager = PerspectiveManager
			.getDefaultManager();
	
	/** The handler. */
	public EventHandler handler = null;

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#setEventHandler(uk.ac.mdx.xmf.swt.client.EventHandler)
	 */
	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	/**
	 * Instantiates a new workbench client.
	 */
	public WorkbenchClient() {
		super("com.ceteva.mosaic");
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#processMessage(xos.Message)
	 */
	public boolean processMessage(Message message) {
		if (message.hasName("shutdown")) {
			Display.getDefault().close();
			return true;
		}
		return processPerspectiveMessage(message);
	}

	/**
	 * Process perspective message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean processPerspectiveMessage(Message message) {
		if (message.hasName("newPerspective")) {
			String id = message.args[0].strValue();
			String title = message.args[1].strValue();
			String image = message.args[2].strValue();
			System.out.println("newPerspective:" + id + ":" + title + ":"
					+ image);
			perspectiveManager.addNewPerspective(id, title, image);
			return true;
		}
		if (message.hasName("newPlaceHolder")) {
			String perpId = message.args[0].strValue();
			String phId = message.args[1].strValue();
			String position = message.args[2].strValue();
			String ref = message.args[3].strValue();
			int ratio = message.args[4].intValue;
			// System.out.println("newPlaceHolder:");
			// return true;
			return perspectiveManager.addNewPlaceHolder(perpId, phId, position,
					ref, ratio);
		}
		if (message.hasName("newPlaceHolderType")) {
			String phId = message.args[0].strValue();
			String type = message.args[1].strValue();
			// System.out.println("newPlaceHolderType:");
			// return true;
			return perspectiveManager.addNewPlaceHolderType(phId, type);
		}
		if (message.hasName("showPerspective")) {
			String id = message.args[0].strValue();
			// System.out.println("showPerspective:");
			return perspectiveManager.showPerspective(id);
		}
		if (message.hasName("setFilenameRedirect")) {
			String source = message.args[0].strValue();
			String target = message.args[1].strValue();
			// FileRedirector.addRedirect(source, target);
			System.out.println("setFilenameRedirect:" + source + ":" + target);
			return true;
		}
		return true;
	}

	// public void setEventHandler(EventHandler handler) {
	// this.handler = handler;// change later, yong
	// if (ActionAdvisor.exit != null)
	// ActionAdvisor.exit.setEventHandler(handler);
	// }
}