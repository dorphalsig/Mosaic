package com.ceteva.text.oleviewer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

// TODO: Auto-generated Javadoc
/**
 * The Class OLEViewerInput.
 */
public class OLEViewerInput implements IEditorInput {
	
	/** The identity. */
	private String identity;
	
	/** The type. */
	private String type;
	
	/** The file. */
	private String file;
	
	/**
	 * Instantiates a new OLE viewer input.
	 *
	 * @param identity the identity
	 * @param type the type
	 * @param file the file
	 */
	public OLEViewerInput(String identity,String type,String file) {
		this.identity = identity;
		this.type = type;
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return true;
	}
	
	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class arg0) {
		return null;
	}
	
}