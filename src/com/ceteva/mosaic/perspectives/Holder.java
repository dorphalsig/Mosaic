package com.ceteva.mosaic.perspectives;

import java.util.Vector;

import org.eclipse.ui.IPageLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class Holder.
 */
public class Holder {

	/** The holderid. */
	private String holderid;
	
	/** The position. */
	private String position;
	
	/** The refid. */
	private String refid;
	
	/** The ratio. */
	private int ratio;
	
	/** The view types. */
	private Vector viewTypes = new Vector();
	
	/**
	 * Instantiates a new holder.
	 *
	 * @param holderid the holderid
	 * @param position the position
	 * @param refid the refid
	 * @param ratio the ratio
	 */
	public Holder(String holderid,String position,String refid,int ratio) {
		this.holderid = holderid;
		this.position = position;
		this.refid = refid;
		this.ratio = ratio;
	}
	
	/**
	 * Adds the view type.
	 *
	 * @param id the id
	 */
	public void addViewType(String id) {
		viewTypes.addElement(id);
	}
	
	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return holderid;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		if(position.equals("left"))
		  return IPageLayout.LEFT;
		else if(position.equals("right"))
		  return IPageLayout.RIGHT;
		else if(position.equals("bottom"))
		  return IPageLayout.BOTTOM;
		return IPageLayout.TOP;
	}
	
	/**
	 * Gets the ref id.
	 *
	 * @return the ref id
	 */
	public String getRefId() {
		return refid;
	}
	
	/**
	 * Gets the ratio.
	 *
	 * @return the ratio
	 */
	public float getRatio() {
		float f = ratio;
		f = f/100;
		f = Math.min(f,(float)0.95);
		f = Math.max(f,(float)0.05);
		return f;
	}
	
	/**
	 * Gets the view types.
	 *
	 * @return the view types
	 */
	public Vector getViewTypes() {
		return viewTypes;
	}
	
	/**
	 * Removes the view type.
	 *
	 * @param type the type
	 */
	public void removeViewType(String type) {
		viewTypes.remove(type);
	}
	
}
