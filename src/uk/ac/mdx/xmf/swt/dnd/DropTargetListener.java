package uk.ac.mdx.xmf.swt.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving dropTarget events.
 * The class that is interested in processing a dropTarget
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDropTargetListener<code> method. When
 * the dropTarget event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DropTargetEvent
 */
public class DropTargetListener extends AbstractTransferDropTargetListener {
	
	/** The enabled. */
	private boolean enabled = false;
	
	/**
	 * Instantiates a new drop target listener.
	 *
	 * @param viewer the viewer
	 * @param transfer the transfer
	 */
	public DropTargetListener(EditPartViewer viewer,Transfer transfer) {
		super(viewer,transfer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		if(getCurrentEvent().detail == DND.DROP_COPY)
		  return new DropRequest((String)getCurrentEvent().data,DropRequest.COPY);
		else
		  return new DropRequest((String)getCurrentEvent().data,DropRequest.MOVE);
	}
	
	/**
	 * Gets the enabled.
	 *
	 * @return the enabled
	 */
	public boolean getEnabled() {
		return enabled;
	}
	
	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled; 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		((DropRequest)getTargetRequest()).setLocation(getDropLocation());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#isEnabled(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public boolean isEnabled(DropTargetEvent event) {
		return enabled;
	}
}
