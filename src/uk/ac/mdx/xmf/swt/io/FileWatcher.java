package uk.ac.mdx.xmf.swt.io;

import java.io.File;
import java.util.TimerTask;

// TODO: Auto-generated Javadoc
/**
 * The Class FileWatcher.
 */
public abstract class FileWatcher extends TimerTask {
	
	/** The time stamp. */
	private long timeStamp;
	
	/** The file. */
	private File file;

	/**
	 * Instantiates a new file watcher.
	 *
	 * @param file the file
	 */
	public FileWatcher(File file) {
		this.file = file;
		this.timeStamp = file.lastModified();
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public final void run() {
		long timeStamp = file.lastModified();

		if (this.timeStamp != timeStamp) {
			this.timeStamp = timeStamp;
			onChange(file);
		}
	}

	/**
	 * On change.
	 *
	 * @param file the file
	 */
	protected abstract void onChange(File file);
}
