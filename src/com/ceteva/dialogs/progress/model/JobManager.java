package com.ceteva.dialogs.progress.model;

import java.util.Vector;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import xos.Message;

import com.ceteva.dialogs.progress.ui.BackgroundJobs;
import com.ceteva.dialogs.progress.ui.ProgressDialog;

// TODO: Auto-generated Javadoc
/**
 * The Class JobManager.
 */
public class JobManager {
	
	/** The viewer. */
	ProgressDialog viewer;
	
	/** The bviewer. */
	BackgroundJobs bviewer;
	
	/** The jobs. */
	Vector jobs = new Vector();
	
	/**
	 * Gets the job for identity.
	 *
	 * @param identity the identity
	 * @return the job for identity
	 */
	private Job getJobForIdentity(String identity) {
		for(int i=0;i<jobs.size();i++) {
			Job job = (Job)jobs.elementAt(i);
			if(job.getIdentity().equals(identity))
			  return job;
		}
		return null;
	}
	
	/**
	 * Gets the background jobs.
	 *
	 * @return the background jobs
	 */
	public Vector getBackgroundJobs() {
		Vector background = new Vector();
		for(int i=0;i<jobs.size();i++) {
		  Job job = (Job)jobs.elementAt(i);
		  if(job.isBackground())
		    background.addElement(job);
		}
		return background;
	}
	
	/**
	 * Gets the foreground jobs.
	 *
	 * @return the foreground jobs
	 */
	public Vector getForegroundJobs() {
		Vector foreground = new Vector();
		for(int i=0;i<jobs.size();i++) {
		  Job job = (Job)jobs.elementAt(i);
		  if(!job.isBackground())
		    foreground.addElement(job);
		}
		return foreground;
	}
	
	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}
	
	/**
	 * New busy job.
	 *
	 * @param identity the identity
	 * @param name the name
	 * @param tooltip the tooltip
	 * @param background the background
	 */
	public void newBusyJob(String identity,String name,String tooltip,boolean background) {
		Job job = new Job(identity,name,tooltip,background);
		jobs.addElement(job);
		refreshViewer();
	}
	
	/**
	 * Process message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean processMessage(Message message) {
		if (message.hasName("newBusyDialog") && message.arity == 3) {
			String identity = message.args[0].strValue();
			String title = message.args[1].strValue();
			boolean background = message.args[2].boolValue;
			newBusyJob(identity,title,title,background);
			return true;
		}
		else if(message.hasName("noLongerBusy") && message.arity==1) {
			String identity = message.args[0].strValue();
			return removeBusyJob(identity);
		}
		return false;
	}
	
	/**
	 * Refresh viewer.
	 */
	public void refreshViewer() {
		if(viewer==null)
		  viewer = new ProgressDialog(getShell());
		if(bviewer==null)
		  bviewer = new BackgroundJobs();
		viewer.refresh(getForegroundJobs());
		bviewer.refresh(getBackgroundJobs());
	}
	
	/**
	 * Removes the busy job.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean removeBusyJob(String identity) {
		Job job = getJobForIdentity(identity);
		if(job!=null && jobs.remove(job)) {
		  refreshViewer();
		  return true;
		}
		return false;
	}

}
