package uk.ac.mdx.xmf.swt.client;

// TODO: Auto-generated Javadoc
/**
 * The Interface Draggable.
 */
public interface Draggable extends ComponentWithIdentity {

	/**
	 * Drag enabled.
	 *
	 * @return true, if successful
	 */
	public boolean dragEnabled();

	// Check if drag has already been enabled (otherwise SWTError is thrown). If
	// not,
	// call setDraggable
	/**
	 * Enable drag.
	 */
	public void enableDrag();

	/**
	 * Sets the draggable.
	 */
	public void setDraggable();

}
