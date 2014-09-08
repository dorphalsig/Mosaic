package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;

// TODO: Auto-generated Javadoc
/**
 * The Interface ZoomableFigure.
 */
public interface ZoomableFigure {
	
	/**
	 * Gets the figure.
	 *
	 * @return the figure
	 */
	public ScalableFigure getFigure();

	/**
	 * Gets the view port.
	 *
	 * @return the view port
	 */
	public IFigure getViewPort();
	
}
