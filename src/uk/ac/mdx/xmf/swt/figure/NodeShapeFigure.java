package uk.ac.mdx.xmf.swt.figure;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeShapeFigure.
 */
public class NodeShapeFigure extends Shape {

	/** The points. */
	Vector points;
	
	/** The outline. */
	boolean outline;

	/**
	 * Instantiates a new node shape figure.
	 *
	 * @param points the points
	 * @param outline the outline
	 */
	public NodeShapeFigure(Vector points, boolean outline) {
		this.points = points;
		this.outline = outline;
	}

	/**
	 * Refresh.
	 *
	 * @param points the points
	 * @param outline the outline
	 */
	public void refresh(Vector points, boolean outline) {
		this.points = points;
		this.outline = outline;
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics) {
		PointList list = new PointList();
		Iterator it = points.iterator();
		while (it.hasNext()) {
			list.addPoint((Point) it.next());
		}
		graphics.fillPolygon(list);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics graphics) {
		if (outline) {
			Point firstPoint = (Point) points.elementAt(0);
			Point lastPoint = firstPoint;
			Iterator it = points.subList(1, points.size()).iterator();
			while (it.hasNext()) {
				Point nextPoint = (Point) it.next();
				if (lastPoint.x != 0 && nextPoint.x != 0)
					graphics.drawLine(lastPoint, nextPoint);
				lastPoint = nextPoint;
			}
			// graphics.drawLine(lastPoint, firstPoint);
		}
	}
}