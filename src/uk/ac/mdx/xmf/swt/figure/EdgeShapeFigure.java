package uk.ac.mdx.xmf.swt.figure;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeShapeFigure.
 */
public class EdgeShapeFigure extends org.eclipse.draw2d.PolylineShape {

	/** The points. */
	Vector points;

	/** The drag points. */
	Vector dragPoints;

	/** The outline. */
	boolean outline;

	/**
	 * Instantiates a new edge shape figure.
	 * 
	 * @param points
	 *            the points
	 * @param dragPoints
	 *            the drag points
	 * @param outline
	 *            the outline
	 */
	public EdgeShapeFigure(Vector points, Vector dragPoints, boolean outline) {
		this.points = points;
		this.dragPoints = dragPoints;
		this.outline = outline;
	}

	/**
	 * Refresh.
	 * 
	 * @param points
	 *            the points
	 * @param outline
	 *            the outline
	 */
	public void refresh(Vector points, boolean outline) {
		this.points = points;
		this.outline = outline;
		// this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.PolylineShape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics) {
		// // PointList list = new PointList();
		// // Iterator it = points.iterator();
		// // while (it.hasNext()) {
		// // list.addPoint((Point) it.next());
		// // }
		// // graphics.fillPolygon(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.PolylineShape#outlineShape(org.eclipse.draw2d.Graphics
	 * )
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

			Iterator itr = dragPoints.iterator();
			while (itr.hasNext()) {
				Point point = (Point) itr.next();

				graphics.drawPoint(point.x, point.y);
			}

		}
	}

	public boolean isClicked(org.eclipse.draw2d.geometry.Point point) {
		boolean isClicked = false;

		Point firstPoint = (Point) points.elementAt(0);
		Point lastPoint = firstPoint;
		Iterator it = points.subList(1, points.size()).iterator();
		while (it.hasNext()) {
			Point nextPoint = (Point) it.next();
			if (lastPoint.x != 0 && nextPoint.x != 0) {
				isClicked = isOnLine(lastPoint, nextPoint, point);
				if (isClicked)
					return isClicked;
			}
			lastPoint = nextPoint;
		}
		return isClicked;
	}

	private boolean isOnLine(Point A, Point B, Point P) {
		double normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y)
				* (B.y - A.y));
		double distance = Math.abs((P.x - A.x) * (B.y - A.y) - (P.y - A.y)
				* (B.x - A.x))
				/ normalLength;
		// System.out.println("distance:" + distance);
		return (distance <= 6);
	}
}
