package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import uk.ac.mdx.xmf.swt.figure.EdgeFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineEdgeTextConstraint.
 */
public class MultilineEdgeTextConstraint implements Locator {

	/** The text. */
	String text;
	
	/** The parent. */
	IFigure parent;
	
	/** The edge edit part. */
	EdgeEditPart edgeEditPart;
	
	/** The edge text edit part. */
	MultilineEdgeTextEditPart edgeTextEditPart;
	
	/** The edge figure. */
	EdgeFigure edgeFigure;
	
	/** The position. */
	String position;
	
	/** The offset. */
	Point offset;

	/**
	 * Instantiates a new multiline edge text constraint.
	 *
	 * @param edgeTextEditPart the edge text edit part
	 * @param text the text
	 * @param parent the parent
	 * @param edgeEditPart the edge edit part
	 * @param position the position
	 * @param offset the offset
	 */
	public MultilineEdgeTextConstraint(
			MultilineEdgeTextEditPart edgeTextEditPart, String text,
			IFigure parent, EdgeEditPart edgeEditPart, String position,
			Point offset) {
		this.edgeTextEditPart = edgeTextEditPart;
		this.text = text;
		this.parent = parent;
		this.edgeEditPart = edgeEditPart;
		this.position = position;
		this.offset = offset;
		edgeFigure = edgeEditPart.getEdgeFigure();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(IFigure figure) {
		// figure.setSize(edgeTextEditPart.getSize());
		Dimension minimum = FigureUtilities.getTextExtents(text,
				figure.getFont());
		figure.setSize(minimum);
		Point endLocation;
		if (position.equals("start"))
			endLocation = edgeFigure.getStart();
		else if (position.equals("end"))
			endLocation = edgeFigure.getEnd();
		else
			endLocation = getMiddle();
		Point offsetCopy = offset.getCopy();
		offsetCopy.translate(endLocation);
		figure.setLocation(offsetCopy);
	}

	/**
	 * Gets the middle.
	 *
	 * @return the middle
	 */
	public Point getMiddle() {
		PointList points = edgeFigure.getPoints();
		return points.getMidpoint();
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Sets the offset.
	 *
	 * @param offset the new offset
	 */
	public void setOffset(Point offset) {
		this.offset = offset;
	}
}