package uk.ac.mdx.xmf.swt.io;

import java.io.File;
import java.io.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * The Class DirFilterWatcher.
 */
public class DirFilterWatcher implements FileFilter {
	
	/** The filter. */
	private String filter;

	/**
	 * Instantiates a new dir filter watcher.
	 */
	public DirFilterWatcher() {
		this.filter = "";
	}

	/**
	 * Instantiates a new dir filter watcher.
	 *
	 * @param filter the filter
	 */
	public DirFilterWatcher(String filter) {
		this.filter = filter;
	}

	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file) {
		if ("".equals(filter)) {
			return true;
		}
		return (file.getName().endsWith(filter));
	}
}
