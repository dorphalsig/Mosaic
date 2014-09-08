package com.ceteva.mosaic.perspectives;

import java.util.Enumeration;
import java.util.Hashtable;

// TODO: Auto-generated Javadoc
/**
 * The Class PerspectiveManager.
 */
public class PerspectiveManager {
	
	/** The standard perspective. */
	public static String standardPerspective = "com.ceteva.mosaic.XmodelerPerspective";

	/** The default pm. */
	private static PerspectiveManager defaultPM = new PerspectiveManager();

	/**
	 * Instantiates a new perspective manager.
	 */
	private PerspectiveManager() {

	}

	/**
	 * Gets the default manager.
	 *
	 * @return the default manager
	 */
	public static PerspectiveManager getDefaultManager() {
		return PerspectiveManager.defaultPM;
	}

	/** The templates. */
	Hashtable<String, PerspectiveTemplate> templates = new Hashtable<String, PerspectiveTemplate>();

	/**
	 * Adds the new perspective.
	 *
	 * @param id the id
	 * @param title the title
	 * @param image the image
	 */
	public void addNewPerspective(String id, String title, String image) {
		PerspectiveTemplate template = new PerspectiveTemplate(id, title, image);
		templates.put(id, template);
	}

	/**
	 * Adds the new place holder.
	 *
	 * @param perspectiveId the perspective id
	 * @param placeHolderId the place holder id
	 * @param position the position
	 * @param refid the refid
	 * @param ratio the ratio
	 * @return true, if successful
	 */
	public boolean addNewPlaceHolder(String perspectiveId,
			String placeHolderId, String position, String refid, int ratio) {
		// PerspectiveTemplate template =
		// (PerspectiveTemplate)templates.get(perspectiveId);
		PerspectiveTemplate template = (PerspectiveTemplate) templates
				.get(perspectiveId);
		if (template != null) {
			template.addHolder(placeHolderId, position, refid, ratio);
			return true;
		}
		return false;
	}

	/**
	 * Adds the new place holder type.
	 *
	 * @param placeHolderId the place holder id
	 * @param type the type
	 * @return true, if successful
	 */
	public boolean addNewPlaceHolderType(String placeHolderId, String type) {
		Enumeration e = templates.elements();
		while (e.hasMoreElements()) {
			PerspectiveTemplate template = (PerspectiveTemplate) e
					.nextElement();
			if (template.hasHolder(placeHolderId)) {
				template.addViewTypeToHolder(placeHolderId, type);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the perspective.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean removePerspective(String id) {
		return false;
	}

	/**
	 * Removes the place holder.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean removePlaceHolder(String id) {
		return false;
	}

	/**
	 * Removes the place holder type.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean removePlaceHolderType(String id) {
		return false;
	}

	/**
	 * Show perspective.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean showPerspective(String id) {
		// Uncomment this again if perspectives can be edited through xmf
		/*
		 * IWorkbench workbench = MosaicPlugin.getDefault().getWorkbench(); try
		 * { workbench.showPerspective(id,workbench.getActiveWorkbenchWindow());
		 * } catch(WorkbenchException e) { e.printStackTrace(); }
		 */
		return true;
	}
}
