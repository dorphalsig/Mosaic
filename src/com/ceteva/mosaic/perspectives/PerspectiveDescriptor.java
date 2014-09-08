package com.ceteva.mosaic.perspectives;

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveFactory;

import uk.ac.mdx.xmf.swt.client.IconManager;

// TODO: Auto-generated Javadoc
/**
 * The Class PerspectiveDescriptor.
 */
public class PerspectiveDescriptor extends
		org.eclipse.ui.internal.registry.PerspectiveDescriptor {

	/** The id. */
	private String id;
	
	/** The label. */
	private String label;
	
	/** The image. */
	private String image;
	
	/** The holders. */
	private Hashtable holders;

	/**
	 * Instantiates a new perspective descriptor.
	 *
	 * @param id the id
	 * @param label the label
	 * @param image the image
	 * @param holders the holders
	 */
	public PerspectiveDescriptor(String id, String label, String image,
			Hashtable holders) {
		super(null, null, null);
		this.id = id;
		this.label = label;
		this.image = image;
		this.holders = holders;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.PerspectiveDescriptor#createFactory()
	 */
	public IPerspectiveFactory createFactory() {
		return new PerspectiveFactory();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.PerspectiveDescriptor#getDescription()
	 */
	public String getDescription() {
		return label;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.PerspectiveDescriptor#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.PerspectiveDescriptor#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return IconManager.getImageDescriptorAbsolute(image);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.registry.PerspectiveDescriptor#getLabel()
	 */
	public String getLabel() {
		return label;
	}

}
