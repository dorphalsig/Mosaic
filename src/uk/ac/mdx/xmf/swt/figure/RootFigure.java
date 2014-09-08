package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.MarginBorder;

// TODO: Auto-generated Javadoc
/**
 * The Class RootFigure.
 */
class RootFigure extends FreeformLayeredPane {
	
	/**
	 * Instantiates a new root figure.
	 */
	public RootFigure() {
		setLayoutManager(new FreeformLayout());
		setBorder(new MarginBorder(5));
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}
}
