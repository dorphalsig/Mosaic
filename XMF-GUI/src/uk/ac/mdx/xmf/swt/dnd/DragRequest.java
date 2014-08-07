package uk.ac.mdx.xmf.swt.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.model.Node;

public class DragRequest extends AbstractTransferDragSourceListener {

	private final NodeEditPart nodeEditPart;
	static private boolean ctrlpressed = false;

	public DragRequest(EditPartViewer viewer, Transfer transfer,
			NodeEditPart nodeEditPart) {
		super(viewer, transfer);
		this.nodeEditPart = nodeEditPart;
	}

	public Node getModel() {
		return (Node) nodeEditPart.getModel();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = ctrlpressed && getModel().isDraggable();
		ctrlpressed = false;
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = getModel().getIdentity();
	}

	public static void setCtrl(boolean value) {
		ctrlpressed = value;
	}
}
