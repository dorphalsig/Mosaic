package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import uk.ac.mdx.xmf.swt.model.Display;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayEditPart.
 */
public abstract class DisplayEditPart extends CommandEventEditPart {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	@Override
	public void performRequest(Request req) {
		Display display = (Display) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT)
			display.selected(1);
		else if (request == RequestConstants.REQ_OPEN)
			display.selected(2);
	}
}