package uk.ac.mdx.xmf.swt.figure;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
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
	/** The distance. */
	private int distance = 20;

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

			location.x = location.x - 2;
			location.y = location.y - 2;
			size.width = size.width + 4;
			size.height = size.height + 4;

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

			points.addElement(new Point(0, 0));
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
			points.addElement(new Point(0, 0));
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
			points.addElement(new Point(0, 0));
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