package com.ceteva.dialogs.progress.ui;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import com.ceteva.dialogs.progress.model.Job;


// TODO: Auto-generated Javadoc
/**
 * The Class BackgroundJobs.
 */
public class BackgroundJobs {
	
	/** The monitor. */
	private IProgressMonitor monitor;
	
	/** The current job. */
	private Job currentJob = null;
		
	/**
	 * Done.
	 */
	public void done() {
	    if(currentJob != null) {
		  currentJob = null;
		  monitor.done();
	    }
	}
	
	/**
	 * Gets the latest background job.
	 *
	 * @param jobs the jobs
	 * @return the latest background job
	 */
	public Job getLatestBackgroundJob(Vector jobs) {
		Job latest = null;
		for(int i=0;i<jobs.size();i++) {
		  Job job = (Job)jobs.elementAt(i);
		  if(job.isBackground())
		    latest = job;
		}
		return latest;
	}
	
	/**
	 * Gets the progress monitor.
	 *
	 * @return the progress monitor
	 */
	public IProgressMonitor getProgressMonitor() {
		if(monitor == null) {
		  WorkbenchWindow window = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		  IStatusLineManager manager = window.getActionBars().getStatusLineManager();
		  monitor = manager.getProgressMonitor();
		}
		return monitor;
	}
	
	/**
	 * Refresh.
	 *
	 * @param jobs the jobs
	 */
	public void refresh(Vector jobs) {
		Job latest = getLatestBackgroundJob(jobs);
		if(latest != null) {
		  if(currentJob != latest) {
		    done();
		    show(latest);
		  }
		}
		else {
		  done();
		}
	}
	
	/**
	 * Show.
	 *
	 * @param job the job
	 */
	public void show(Job job) {
		currentJob = job;
		getProgressMonitor().beginTask(job.getName(),IProgressMonitor.UNKNOWN);
	}

}
