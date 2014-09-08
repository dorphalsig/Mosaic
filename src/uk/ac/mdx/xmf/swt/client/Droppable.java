package uk.ac.mdx.xmf.swt.client;

// TODO: Auto-generated Javadoc
/**
 * The Interface Droppable.
 */
public interface Droppable extends ComponentWithIdentity {

	/**
	 * Drop enabled.
	 *
	 * @return true, if successful
	 */
	public boolean dropEnabled();

	// Check if drop has already been enabled (otherwise SWTError is thrown). If
	// not,
	// call setDroppable
	/**
	 * Enable drop.
	 */
	public void enableDrop();

	/**
	 * Sets the droppable.
	 */
	public void setDroppable();

}
