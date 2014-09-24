package uk.ac.mdx.xmf.swt.figure;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

import uk.ac.mdx.xmf.swt.misc.VisualElementEvents;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeShapeFigure.
 */
public class NodeShapeFigure extends Shape {

	/** The points. */
	private Vector<Point> points;

	/** The outline. */
	boolean outline;
	/** The distance. */
	private int distance = 8;

	/** The gap. */
	private int gap = 3;

	/**
	 * Instantiates a new node shape figure.
	 * 
	 * @param points
	 *            the points
	 * @param outline
	 *            the outline
	 */
	public NodeShapeFigure(boolean outline) {
		points = new Vector();
		this.outline = outline;
	}

	public void reSetPoints(Point location, Dimension size) {
		points.clear();
		// Point location = new Point(0, 0);
		// Dimension size = new Dimension();

		// if (resetPoint)
		{

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x, location.y));
			points.addElement(new Point(location.x + gap, location.y));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + gap + gap, location.y));
			points.addElement(new Point(location.x + size.width / 2 - gap,
					location.y));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width / 2, location.y));
			points.addElement(new Point(location.x + size.width / 2 + gap,
					location.y));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(
					location.x + size.width / 2 + gap + gap, location.y));
			points.addElement(new Point(location.x + size.width - gap - gap,
					location.y));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width - gap,
					location.y));
			points.addElement(new Point(location.x + size.width, location.y));

			// second line

			points.addElement(new Point(0, 0)); // point(15)
			points.addElement(new Point(location.x + size.width, location.y
					+ gap));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height / 2 - gap));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height / 2));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height / 2 + gap));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height / 2 + gap + gap));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height - gap - gap));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height - gap));
			points.addElement(new Point(location.x + size.width, location.y
					+ size.height));

			// third line
			points.addElement(new Point(0, 0)); // point(27)
			points.addElement(new Point(location.x + size.width - gap - gap,
					location.y + size.height));
			points.addElement(new Point(
					location.x + size.width / 2 + gap + gap, location.y
							+ size.height));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width / 2 + gap,
					location.y + size.height));
			points.addElement(new Point(location.x + size.width / 2, location.y
					+ size.height));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + size.width / 2 - gap,
					location.y + size.height));
			points.addElement(new Point(location.x + gap + gap, location.y
					+ size.height));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x + gap, location.y
					+ size.height));
			points.addElement(new Point(location.x, location.y + size.height));

			// fouth line
			points.addElement(new Point(0, 0)); // point(39)
			points.addElement(new Point(location.x, location.y + size.height
					- gap));
			points.addElement(new Point(location.x, location.y + size.height));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x, location.y + size.height
					/ 2 + gap + gap));
			points.addElement(new Point(location.x, location.y + size.height
					- gap - gap));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x, location.y + size.height
					/ 2));
			points.addElement(new Point(location.x, location.y + size.height
					/ 2 + gap));

			points.addElement(new Point(0, 0));
			points.addElement(new Point(location.x, location.y + gap + gap));
			points.addElement(new Point(location.x, location.y + size.height
					/ 2 - gap));
		}

	}

	/**
	 * Checks if is drag point clicked.
	 * 
	 * @param point
	 *            the point
	 * @return the string
	 */
	public String isDragPointClicked(Point point) {
		if (points != null && points.size() > 42) {

			Point leftTopCornerPoint = points.get(1);
			Point rightTopCornerPoint = points.get(16);
			Point rightBottomCornerPoint = points.get(28);
			Point leftBottomCornerPoint = points.get(40);

			if (getDistanceOfPoints(point, leftTopCornerPoint) < distance)
				return VisualElementEvents.leftTopCornerPoint;
			if (getDistanceOfPoints(point, rightTopCornerPoint) < distance)
				return VisualElementEvents.rightTopCornerPoint;
			if (getDistanceOfPoints(point, rightBottomCornerPoint) < distance)
				return VisualElementEvents.rightBottomCornerPoint;
			if (getDistanceOfPoints(point, leftBottomCornerPoint) < distance)
				return VisualElementEvents.leftBottomCornerPoint;

			Point topMiddlePoint = points.get(8);
			Point rightMiddlePoint = points.get(20);
			Point bottomMiddlePoint = points.get(32);
			Point leftMiddlePoint = points.get(43);

			if (getDistanceOfPoints(point, topMiddlePoint) < distance)
				return VisualElementEvents.topMiddlePoint;
			if (getDistanceOfPoints(point, rightMiddlePoint) < distance)
				return VisualElementEvents.rightMiddlePoint;
			if (getDistanceOfPoints(point, bottomMiddlePoint) < distance)
				return VisualElementEvents.bottomMiddlePoint;
			if (getDistanceOfPoints(point, leftMiddlePoint) < distance)
				return VisualElementEvents.leftMiddlePoint;

		}
		return "";

	}

	/**
	 * Gets the distance of points.
	 * 
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @return the distance of points
	 */
	public int getDistanceOfPoints(org.eclipse.draw2d.geometry.Point p1,
			org.eclipse.draw2d.geometry.Point p2) {
		int distance = (int) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
				+ (p1.y - p2.y) * (p1.y - p2.y));

		return distance;

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
		this.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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