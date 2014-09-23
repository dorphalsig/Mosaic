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

	private boolean isOnLine(Point startPoint, Point endPoint, Point pt) {
		int width = endPoint.x - startPoint.x;
		int height = endPoint.y - startPoint.y;
		int dx = pt.x - startPoint.x;
		int dy = pt.y = startPoint.y;
		double distance;

		if (dx != 0 && width != 0) // if both lines are not vertical
		{
			double angleToCorner = Math.atan(height / width);
			double angleToPoint = Math.atan(dy / dx);
			distance = Math.sqrt(dx * dx + dy * dy)
					* Math.sin(Math.abs(angleToCorner - angleToPoint));
		} else // one or both slopes have zero divisor (are vertical)
		{
			distance = Math.abs(dx);
		}
		System.out.println("distance:" + distance);
		return (distance <= 6);
	}
}