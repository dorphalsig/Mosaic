package com.ceteva.text.texteditor;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

// TODO: Auto-generated Javadoc
/**
 * The Class TextEditorInput.
 */
public class TextEditorInput implements IEditorInput {
	
	/** The storage. */
	private TextStorage storage;

	/**
	 * Instantiates a new text editor input.
	 *
	 * @param _storage the _storage
	 */
	public TextEditorInput(TextStorage _storage) {
		this.storage = _storage;
	}

	/**
	 * Gets the storage.
	 *
	 * @return the storage
	 * @throws CoreException the core exception
	 */
	public IStorage getStorage() throws CoreException {
		return this.storage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return true;
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
	public Object getAdapter(Class adapter) {
		return null;
	}
}