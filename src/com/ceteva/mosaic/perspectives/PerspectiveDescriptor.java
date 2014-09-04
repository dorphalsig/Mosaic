package com.ceteva.mosaic.perspectives;

import java.util.Hashtable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveFactory;

import uk.ac.mdx.xmf.swt.client.IconManager;

public class PerspectiveDescriptor extends
		org.eclipse.ui.internal.registry.PerspectiveDescriptor {

	private String id;
	private String label;
	private String image;
	private Hashtable holders;

	public PerspectiveDescriptor(String id, String label, String image,
			Hashtable holders) {
		super(null, null, null);
		this.id = id;
		this.label = label;
		this.image = image;
		this.holders = holders;
	}

	public IPerspectiveFactory createFactory() {
		return new PerspectiveFactory();
	}

	public String getDescription() {
		return label;
	}

	public String getId() {
		return id;
	}

	public ImageDescriptor getImageDescriptor() {
		return IconManager.getImageDescriptorAbsolute(image);
	}

	public String getLabel() {
		return label;
	}

}
