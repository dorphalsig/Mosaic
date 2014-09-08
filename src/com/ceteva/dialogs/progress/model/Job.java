package com.ceteva.dialogs.progress.model;

import org.eclipse.core.runtime.IProgressMonitor;

// TODO: Auto-generated Javadoc
/**
 * The Class Job.
 */
public class Job {
	
	/** The identity. */
	private String identity;
	
	/** The name. */
	private String name;
	
	/** The tooltip. */
	private String tooltip;
	
	/** The background. */
	private boolean background;
	
	/** The minimum. */
	private int minimum = 0;
	
	/** The maximum. */
	private int maximum = 100;
	
	/** The current. */
	private int current = IProgressMonitor.UNKNOWN;
	
	/**
	 * Instantiates a new job.
	 *
	 * @param identity the identity
	 * @param name the name
	 * @param tooltip the tooltip
	 * @param minimum the minimum
	 * @param maximum the maximum
	 * @param background the background
	 */
	public Job(String identity,String name,String tooltip,int minimum,int maximum,boolean background) {
		this.identity = identity;
		this.name = name;
		this.tooltip = tooltip;
		this.minimum = minimum;
		this.maximum = maximum;
		this.background = background;
	}
	
	/**
	 * Instantiates a new job.
	 *
	 * @param identity the identity
	 * @param name the name
	 * @param tooltip the tooltip
	 * @param background the background
	 */
	public Job(String identity,String name,String tooltip,boolean background) {
		this.identity = identity;
		this.name = name;
		this.tooltip = tooltip;
		this.background = background;
	}
	
	/**
	 * Checks if is background.
	 *
	 * @return true, if is background
	 */
	public boolean isBackground() {
		return background;
	}
	
	/**
	 * Gets the current.
	 *
	 * @return the current
	 */
	public int getCurrent() {
		return current;
	}
	
	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}
	
	/**
	 * Gets the minimum.
	 *
	 * @return the minimum
	 */
	public int getMinimum() {
		return minimum;
	}
	
	/**
	 * Gets the maximum.
	 *
	 * @return the maximum
	 */
	public int getMaximum() {
		return maximum;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the tooltip.
	 *
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}
	

}
