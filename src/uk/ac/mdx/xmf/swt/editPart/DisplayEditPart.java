package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import uk.ac.mdx.xmf.swt.model.Display;

public abstract class DisplayEditPart extends CommandEventEditPart {

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