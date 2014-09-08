package com.ceteva.dialogs.progress.ui;

import java.util.Vector;

import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.ceteva.dialogs.progress.model.Job;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgressDialog.
 */
public class ProgressDialog extends IconAndMessageDialog {
	
	/**
	 * The Class Timer.
	 */
	class Timer extends Thread {
		
		/** The maximum. */
		private int maximum = 5;
		
		/** The done. */
		private boolean done = false;
		
		/**
		 * Done.
		 *
		 * @return true, if successful
		 */
		public synchronized boolean done() {
			return done;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			for(int i=0;i<maximum;i++) {
			  try {
			    Thread.sleep(100);
			  }
			  catch(InterruptedException iox) {
			    done = true;	  
			  }
			}
			done = true;
		}
	}

	/** The viewer. */
	private DetailedProgressViewer viewer;
	
	/** The cancel selected. */
	private Button cancelSelected;
	
	/** The arrow cursor. */
	private Cursor arrowCursor;
	
	/** The wait cursor. */
	private Cursor waitCursor;
	
	/** The timer. */
	private Timer timer;

	/**
	 * Instantiates a new progress dialog.
	 *
	 * @param parent the parent
	 */
	public ProgressDialog(Shell parent) {
		super(parent);
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
		// no close button
		setBlockOnOpen(false);
		setMessage("Progress");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		setMessage(message);
		createMessageArea(parent);
		showJobDetails(parent);
		startTimer();
		return parent;
	}
	
	/**
	 * Show job details.
	 *
	 * @param parent the parent
	 */
	void showJobDetails(Composite parent) {
		viewer = new DetailedProgressViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData data = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		int heightHint = convertHeightInCharsToPixels(10);
		data.heightHint = heightHint;
		viewer.getControl().setLayoutData(data);
	}


	/**
	 * Clear cursors.
	 */
	private void clearCursors() {
		clearCursor(cancelSelected);
		clearCursor(getShell());
		if (arrowCursor != null) {
			arrowCursor.dispose();
		}
		if (waitCursor != null) {
			waitCursor.dispose();
		}
		arrowCursor = null;
		waitCursor = null;
	}

	/**
	 * Clear cursor.
	 *
	 * @param control the control
	 */
	private void clearCursor(Control control) {
		if (control != null && !control.isDisposed()) {
			control.setCursor(null);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (waitCursor == null) {
			waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
		}
		shell.setCursor(waitCursor);
		shell.setText("Progress Information");
	}

	/**
	 * Sets the message.
	 *
	 * @param messageString the new message
	 */
	private void setMessage(String messageString) {
		message = messageString == null ? "" : messageString;
		if (messageLabel == null || messageLabel.isDisposed()) {
			return;
		}
		messageLabel.setText(message);
	}
	
	/**
	 * Start timer.
	 */
	private void startTimer() {
		timer = new Timer();
		timer.start();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
	 */
	protected Image getImage() {
		return getInfoImage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	public boolean close() {
		while(!timer.done());
		clearCursors();
		return super.close();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IconAndMessageDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createButtonBar(Composite parent) {
		return parent;
	}
	
	/**
	 * Refresh.
	 *
	 * @param jobs the jobs
	 */
	public void refresh(Vector jobs) {
		if(jobs.size() > 0) {
		  this.open();
		  Job latest = (Job)jobs.lastElement();
		  setMessage(latest.getName());
		}
		else
		  this.close();
		viewer.refresh(jobs);
	}
}
