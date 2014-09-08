package com.ceteva.mosaic.perspectives;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;

// TODO: Auto-generated Javadoc
/**
 * The Class PerspectiveTemplate.
 */
public class PerspectiveTemplate {

	/** The id. */
	private String id;
	
	/** The label. */
	private String label;
	
	/** The image. */
	private String image;
	
	/** The holders. */
	private Hashtable holders = new Hashtable();
	
	/** The reg. */
	private PerspectiveRegistry reg;
	
	/** The desc. */
	private org.eclipse.ui.internal.registry.PerspectiveDescriptor desc;
	
	/** The pf. */
	private PerspectiveFactory pf;

	/**
	 * Instantiates a new perspective template.
	 *
	 * @param id the id
	 * @param label the label
	 * @param image the image
	 */
	public PerspectiveTemplate(String id, String label, String image) {
		this.id = id;
		this.label = label;
		this.image = image;
		// this.reg = (PerspectiveRegistry) MosaicPlugin.getDefault()
		// .getWorkbench().getPerspectiveRegistry();

		System.err.println("--->>createPerspective");
		desc = new PerspectiveDescriptor(id, label, image, holders);
		// // org.eclipse.ui.internal.registry.PerspectiveDescriptor
		// // createPerspectived = reg.createPerspective(id, desc);
		// System.err.println("--->>" + id + ":" + label);
		// // System.err.println(reg.getPerspectives().length);
		//
		// org.eclipse.ui.internal.registry.PerspectiveDescriptor
		// createdPerspective = reg
		// .createPerspective(label, desc);
		//
		// System.err.println(reg.getPerspectives().length);

		refreshPerspective();
		PerspectiveTemplate.templates.put(desc, this);
	}

	/**
	 * Gets the holders.
	 *
	 * @return the holders
	 */
	public Hashtable getHolders() {
		return holders;
	}

	/** The templates. */
	private static Map<IPerspectiveDescriptor, PerspectiveTemplate> templates = new HashMap<IPerspectiveDescriptor, PerspectiveTemplate>();

	/**
	 * Gets the template.
	 *
	 * @param by the by
	 * @return the template
	 */
	public static PerspectiveTemplate getTemplate(IPerspectiveDescriptor by) {
		return PerspectiveTemplate.templates.get(by);
	}

	/**
	 * Gets the by id.
	 *
	 * @param id the id
	 * @return the by id
	 */
	private IPerspectiveDescriptor getById(String id) {
		return desc;
		// return this.reg.findPerspectiveWithId(id);
	}

	/**
	 * Adds the holder.
	 *
	 * @param id the id
	 * @param position the position
	 * @param refid the refid
	 * @param ratio the ratio
	 */
	public void addHolder(String id, String position, String refid, int ratio) {
		holders.put(id, new Holder(id, position, refid, ratio));
		refreshPerspective();

	}

	/**
	 * Adds the view type to holder.
	 *
	 * @param holderid the holderid
	 * @param viewid the viewid
	 */
	public void addViewTypeToHolder(String holderid, String viewid) {
		Holder holder = (Holder) holders.get(holderid);
		holder.addViewType(viewid);
		refreshPerspective();
	}

	/**
	 * Creates the perspective.
	 */
	@Deprecated
	@SuppressWarnings("restriction")
	private void createPerspective() {
		// Creation of perspectives is currently restricted to plugin.xml
		System.err.println("--->>createPerspective");
		desc = new PerspectiveDescriptor(id, label, image, holders);
		// org.eclipse.ui.internal.registry.PerspectiveDescriptor
		// createPerspectived = reg.createPerspective(id, desc);
		System.err.println("--->>" + id + ":" + label);
		System.err.println(reg.getPerspectives().length);

		org.eclipse.ui.internal.registry.PerspectiveDescriptor createdPerspective = reg
				.createPerspective(label, desc);

		System.err.println(reg.getPerspectives().length);
	}

	/**
	 * Delete perspective.
	 */
	private void deletePerspective() {
		if (desc != null)
			reg.deletePerspective(desc);
	}

	/**
	 * Checks for holder.
	 *
	 * @param holderId the holder id
	 * @return true, if successful
	 */
	public boolean hasHolder(String holderId) {
		return holders.containsKey(holderId);
	}

	/**
	 * Refresh perspective.
	 */
	private void refreshPerspective() {
		System.err.println("Refreshing layout_1");
		if (PerspectiveFactory.layouts != null) {
			System.err.println("Refreshing layout_2");
			IPerspectiveDescriptor perspectiveDesc = getById(id);
			if (perspectiveDesc != null) {
				System.err.println("Refreshing layout_3");
				IPageLayout pageLayout = PerspectiveFactory.layouts
						.get(perspectiveDesc);
				if (pageLayout != null) {
					System.err.println("Refreshing layout_4");
					PerspectiveFactory.createInitialLayout(pageLayout, holders);
				}
			}
		}

	}

	/**
	 * Removes the holder.
	 *
	 * @param id the id
	 */
	public void removeHolder(String id) {
		holders.remove(id);
		refreshPerspective();
	}

	/**
	 * Removes the view type from holder.
	 *
	 * @param holderid the holderid
	 * @param viewid the viewid
	 */
	public void removeViewTypeFromHolder(String holderid, String viewid) {
		Holder holder = (Holder) holders.get(holderid);
		holder.removeViewType(viewid);
		refreshPerspective();
	}
}
