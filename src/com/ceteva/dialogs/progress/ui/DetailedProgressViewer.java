package com.ceteva.dialogs.progress.ui;

import java.util.Vector;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.ceteva.dialogs.progress.model.Job;

// TODO: Auto-generated Javadoc
/**
 * The Class DetailedProgressViewer.
 */
public class DetailedProgressViewer {

	/** The control. */
	Composite control;
	
	/** The scrolled. */
	private ScrolledComposite scrolled;
	
	/** The no entry area. */
	private Composite noEntryArea;
	
	/** The progressbars. */
	private Vector progressbars = new Vector();

	/**
	 * Instantiates a new detailed progress viewer.
	 *
	 * @param parent the parent
	 * @param style the style
	 */
	public DetailedProgressViewer(Composite parent, int style) {
		scrolled = new ScrolledComposite(parent, SWT.V_SCROLL | style);
		int height = JFaceResources.getDefaultFont().getFontData()[0]
				.getHeight();
		scrolled.getVerticalBar().setIncrement(height * 2);
		scrolled.setExpandHorizontal(true);
		scrolled.setExpandVertical(true);

		control = new Composite(scrolled, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		control.setLayout(layout);
		control.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		scrolled.setContent(control);
		noEntryArea = new Composite(scrolled, SWT.NONE);
		noEntryArea.setLayout(new GridLayout());
		Text noEntryLabel = new Text(noEntryArea, SWT.SINGLE);
		noEntryLabel.setBackground(noEntryArea.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		GridData textData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		noEntryLabel.setLayoutData(textData);
		noEntryLabel.setEditable(false);
	}
	
	/**
	 * Gets the control.
	 *
	 * @return the control
	 */
	public Control getControl() {
		return scrolled;
	}
	
	/**
	 * Exists progress for job.
	 *
	 * @param job the job
	 * @return true, if successful
	 */
	public boolean existsProgressForJob(Job job) {
		return getProgressForJob(job) != null;
	}
	
	/**
	 * Gets the progress for job.
	 *
	 * @param job the job
	 * @return the progress for job
	 */
	public ProgressInfoItem getProgressForJob(Job job) {
		for(int i=0;i<progressbars.size();i++) {
			ProgressInfoItem progress = (ProgressInfoItem)progressbars.elementAt(i);
			if(progress.getJob() == job)
			  return progress;
		}
		return null;
	}
	
	/**
	 * Adds the progress.
	 *
	 * @param job the job
	 */
	public void addProgress(Job job) {
		ProgressInfoItem progress = new ProgressInfoItem(job,control, SWT.NONE);
		progressbars.add(progress);
	}
	
	/**
	 * Refresh.
	 *
	 * @param jobs the jobs
	 */
	public void refresh(Vector jobs) {
		for(int i=0;i<progressbars.size();i++) {
			ProgressInfoItem progress = (ProgressInfoItem)progressbars.elementAt(i);
			if(!jobs.contains(progress.getJob())) {
				progress.delay();
				removeProgress(progress);
			}
		}
		for(int i=0;i<jobs.size();i++) {
			Job job = (Job)jobs.elementAt(i);
			if(!existsProgressForJob(job))
				addProgress(job);
		}
		if(!control.isDisposed())
		  control.layout(true);
	}
	
	/**
	 * Removes the progress.
	 *
	 * @param progress the progress
	 */
	public void removeProgress(ProgressInfoItem progress) {
		progressbars.remove(progress);
		progress.dispose();
	}


}