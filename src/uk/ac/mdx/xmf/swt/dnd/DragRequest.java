package uk.ac.mdx.xmf.swt.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.model.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class DragRequest.
 */
public class DragRequest extends AbstractTransferDragSourceListener {

	/** The node edit part. */
	private final NodeEditPart nodeEditPart;
	
	/** The ctrlpressed. */
	static private boolean ctrlpressed = false;

	/**
	 * Instantiates a new drag request.
	 *
	 * @param viewer the viewer
	 * @param transfer the transfer
	 * @param nodeEditPart the node edit part
	 */
	public DragRequest(EditPartViewer viewer, Transfer transfer,
			NodeEditPart nodeEditPart) {
		super(viewer, transfer);
		this.nodeEditPart = nodeEditPart;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public Node getModel() {
		return (Node) nodeEditPart.getModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = ctrlpressed && getModel().isDraggable();
		ctrlpressed = false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = getModel().getIdentity();
	}

	/**
	 * Sets the ctrl.
	 *
	 * @param value the new ctrl
	 */
	public static void setCtrl(boolean value) {
		ctrlpressed = value;
	}
}
