package uk.ac.mdx.xmf.swt.diagram.tracker;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;

import uk.ac.mdx.xmf.swt.editPart.EdgeEditPart;

public class EdgeTextDragTracker extends DragEditPartsTracker {

	private EdgeEditPart edgeEditPart;

	public EdgeTextDragTracker(EditPart sourceEditPart,
			EdgeEditPart edgeEditPart) {
		super(sourceEditPart);
		this.edgeEditPart = edgeEditPart;
	}

	protected EditPart getTargetEditPart() {
		return edgeEditPart;
	}
}