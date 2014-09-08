package uk.ac.mdx.xmf.swt.io;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

// TODO: Auto-generated Javadoc
/**
 * The Class DirWatcher.
 */
public abstract class DirWatcher extends TimerTask {
	
	/** The path. */
	private String path;
	
	/** The files array. */
	private File filesArray[];
	
	/** The dir. */
	private HashMap dir = new HashMap();
	
	/** The dfw. */
	private DirFilterWatcher dfw;

	/**
	 * Instantiates a new dir watcher.
	 *
	 * @param path the path
	 */
	public DirWatcher(String path) {
		this(path, "");
	}

	/**
	 * Instantiates a new dir watcher.
	 *
	 * @param path the path
	 * @param filter the filter
	 */
	public DirWatcher(String path, String filter) {
		this.path = path;
		dfw = new DirFilterWatcher(filter);
		filesArray = new File(path).listFiles(dfw);

		// transfer to the hashmap be used a reference and keep the
		// lastModfied value
		for (int i = 0; i < filesArray.length; i++) {
			dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
		}
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public final void run() {
		HashSet checkedFiles = new HashSet();
		filesArray = new File(path).listFiles(dfw);

		// scan the files and check for modification/addition
		for (int i = 0; i < filesArray.length; i++) {
			Long current = (Long) dir.get(filesArray[i]);
			checkedFiles.add(filesArray[i]);
			if (current == null) {
				// new file
				dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
				onChange(filesArray[i], "add");
			} else if (current.longValue() != filesArray[i].lastModified()) {
				// modified file
				dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
				onChange(filesArray[i], "modify");
			}
		}

		// now check for deleted files
		Set ref = ((HashMap) dir.clone()).keySet();
		ref.removeAll((Set) checkedFiles);
		Iterator it = ref.iterator();
		while (it.hasNext()) {
			File deletedFile = (File) it.next();
			dir.remove(deletedFile);
			onChange(deletedFile, "delete");
		}
	}

	/**
	 * On change.
	 *
	 * @param file the file
	 * @param action the action
	 */
	protected abstract void onChange(File file, String action);
}
