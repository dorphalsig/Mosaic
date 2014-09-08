package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class LineFigure.
 */
public class LineFigure extends Polyline {

	/**
	 * Instantiates a new line figure.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public LineFigure(Point start,Point end) { 
	  this.setStart(start);
	  this.setEnd(end);
	}
}