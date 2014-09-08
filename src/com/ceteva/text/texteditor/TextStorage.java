package com.ceteva.text.texteditor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;

// TODO: Auto-generated Javadoc
/**
 * The Class TextStorage.
 */
public class TextStorage implements IStorage {
	
	/** The is. */
	private InputStream is;

	/**
	 * Instantiates a new text storage.
	 *
	 * @param buffer the buffer
	 */
	public TextStorage(String buffer) {
		this.is = new ByteArrayInputStream(buffer.getBytes());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IStorage#getContents()
	 */
	public InputStream getContents() throws CoreException {
		try {
			return this.is;
		} catch (Exception e) {
			throw new CoreException(new Status(Status.ERROR,
					"com.ceteva.text.TextEditor", Status.OK, "", e));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IStorage#getFullPath()
	 */
	public IPath getFullPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IStorage#getName()
	 */
	public String getName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IStorage#isReadOnly()
	 */
	public boolean isReadOnly() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}
}