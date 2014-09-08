package uk.ac.mdx.xmf.swt.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class IOThread.
 */
public class IOThread extends Thread {

	/** The in. */
	private InputStream in;

	/** The out. */
	private OutputStream out;

	/** The Constant debug. */
	private final static boolean debug = false;

	/**
	 * Instantiates a new IO thread.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public IOThread(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	/**
	 * Debug.
	 *
	 * @param s the s
	 */
	public static void debug(String s) {
		if (debug) {
			System.err.println(s);
			System.err.flush();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			try {
				IOThread.debug("About to read on " + in);
				int c = in.read();
				IOThread.debug("read '" + (char) c + "'");
				IOThread.debug("About to write on " + out);
				out.write(c);
				IOThread.debug("written. About to flush");
				out.flush();
				IOThread.debug("flushed.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}