package com.ceteva.console;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;

import uk.ac.mdx.xmf.swt.demo.Main;

import com.ceteva.console.views.ConsoleView;

// TODO: Auto-generated Javadoc
/**
 * The Class ConsoleClient.
 */
public class ConsoleClient extends Thread {

	/** The view. */
	ConsoleView view = null;
	
	/** The in. */
	BufferedReader in;
	
	/** The out. */
	PrintStream out;
	
	/** The queued input. */
	StringBuffer queuedInput = new StringBuffer();
	
	/** The display. */
	Display display;

	/**
	 * Instantiates a new console client.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public ConsoleClient(InputStream in, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = new PrintStream(new BufferedOutputStream(out));
	}

	/**
	 * Sets the display.
	 *
	 * @param display the new display
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(ConsoleView view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		char[] buffer = new char[1000];
		while (true) {
			try {
				int size = in.read(buffer);
				if (size > 0)
					sendInput(new String(buffer).substring(0, size));
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Debug.
	 *
	 * @param message the message
	 */
	public void debug(String message) {

		System.err.println(java.lang.Thread.currentThread() + ": " + message);
		System.err.flush();

	}

	/**
	 * Try connecting.
	 *
	 * @return true, if successful
	 */
	public boolean tryConnecting() {
		// ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
		// // IWorkbenchPage page = consolePlugin.getWorkbench()
		// // .getActiveWorkbenchWindow().getActivePage();
		// if (page == null) {
		// debug("Active workbench was not found");
		// return false;
		// }
		// view = (ConsoleView) page.findView("com.ceteva.console.view");
		// if (view == null) {
		// debug("Console View was not found with id = com.ceteva.console.view");
		// return false;
		// }

		view = Main.consoleView;
		// view.setSize(GUIDemo.getInstance().sectionBottomRight.getSize());
		view.setOutput(out);
		view.processInput(queuedInput.toString());

		return false;
	}

	/**
	 * Send input.
	 *
	 * @param input the input
	 */
	public void sendInput(final String input) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (view != null)
					view.processInput(input);
				else if (tryConnecting())
					view.processInput(input);
				else
					queueInput(input);
			}
		});
	}

	/**
	 * Queue input.
	 *
	 * @param input the input
	 */
	public void queueInput(String input) {
		queuedInput.append(input);
	}
}