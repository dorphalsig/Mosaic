package uk.ac.mdx.xmf.swt.figure;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;

public class EdgeShapeFigure extends org.eclipse.draw2d.PolylineShape {

	Vector points;
	Vector dragPoints;
	boolean outline;

	public EdgeShapeFigure(Vector points, Vector dragPoints, boolean outline) {
		this.points = points;
		this.dragPoints = dragPoints;
		this.outline = outline;
	}

	public void refresh(Vector points, boolean outline) {
		this.points = points;
		this.outline = outline;
		// this.repaint();
	}

	@Override
	protected void fillShape(Graphics graphics) {
		// // PointList list = new PointList();
		// // Iterator it = points.iterator();
		// // while (it.hasNext()) {
		// // list.addPoint((Point) it.next());
		// // }
		// // graphics.fillPolygon(list);
	}

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
}