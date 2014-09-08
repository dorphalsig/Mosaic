package uk.ac.mdx.xmf.swt.diagram.tracker;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

import uk.ac.mdx.xmf.swt.editPart.EdgeEditPart;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeTextDragTracker.
 */
public class EdgeTextDragTracker extends DragEditPartsTracker {

	/** The edge edit part. */
	private EdgeEditPart edgeEditPart;

	/**
	 * Instantiates a new edge text drag tracker.
	 *
	 * @param sourceEditPart the source edit part
	 * @param edgeEditPart the edge edit part
	 */
	public EdgeTextDragTracker(EditPart sourceEditPart,
			EdgeEditPart edgeEditPart) {
		super(sourceEditPart);
		this.edgeEditPart = edgeEditPart;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.TargetingTool#getTargetEditPart()
	 */
	protected EditPart getTargetEditPart() {
		return edgeEditPart;
	}
}