package uk.ac.mdx.xmf.swt.diagram.tracker;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;

import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.editPart.TextEditPart;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplaySelectionTracker.
 */
public class DisplaySelectionTracker extends NodeSelectionTracker {

	// a tracker that selects its parent node as well as the display

	/** The owner. */
	EditPart owner;
	
	/** The debug. */
	boolean debug = false;

	/**
	 * Instantiates a new display selection tracker.
	 *
	 * @param owner the owner
	 */
	public DisplaySelectionTracker(EditPart owner) {
		super(owner);
		findOwner(owner);
	}

	/**
	 * Find owner.
	 *
	 * @param owner the owner
	 */
	public void findOwner(EditPart owner) {
		// if(owner!=null) {
		if (owner instanceof NodeEditPart)
			this.owner = owner;
		else
			findOwner(owner.getParent());
		// }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
	 */
	protected void performSelection() {
		if (hasSelectionOccurred())
			return;
		setFlag(FLAG_SELECTION_PERFORMED, true);
		EditPartViewer viewer = getCurrentViewer();
		List selectedObjects = viewer.getSelectedEditParts();

		if (getCurrentInput().isControlKeyDown()) {
			if (selectedObjects.contains(getSourceEditPart()))
				viewer.deselect(getSourceEditPart());
			else {
				viewer.appendSelection(getSourceEditPart());
				viewer.appendSelection(owner);
			}
		} else if (getCurrentInput().isShiftKeyDown()) {
			viewer.appendSelection(getSourceEditPart());
			viewer.appendSelection(owner);
		} else {
			viewer.select(getSourceEditPart());
			viewer.appendSelection(owner);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#handleDragStarted()
	 */
	public boolean handleDragStarted() {
		if (owner != null)
			setSourceEditPart(owner);
		return super.handleDragStarted();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performDirectEdit()
	 */
	protected void performDirectEdit() {
		EditPartViewer viewer = getCurrentViewer();
		List selectedObjects = viewer.getSelectedEditParts();
		boolean hasTextEditPart = false;
		for (int i = 0; i < selectedObjects.size(); i++) {
			EditPart ep = (EditPart) selectedObjects.get(i);
			if (ep instanceof TextEditPart) {
				TextEditPart tep = (TextEditPart) ep;
				if (tep.isSelectable())
					hasTextEditPart = true;
			}
		}
		if (!hasTextEditPart) {
			for (int i = 0; i < selectedObjects.size(); i++) {
				EditPart ep = (EditPart) selectedObjects.get(i);
				if (ep instanceof NodeEditPart)
					setSourceEditPart(ep);
			}
		}
		super.performDirectEdit();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performOpen()
	 */
	protected void performOpen() {
		SelectionRequest request = new SelectionRequest();
		request.setLocation(getLocation());
		request.setType(RequestConstants.REQ_OPEN);
		EditPartViewer viewer = getCurrentViewer();
		List selectedObjects = viewer.getSelectedEditParts();
		for (int i = 0; i < selectedObjects.size(); i++) {
			EditPart ep = (EditPart) selectedObjects.get(i);
			ep.performRequest(request);
		}
	}
}