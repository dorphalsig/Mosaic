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
	 * @param points the points
	 * @param dragPoints the drag points
	 * @param outline the outline
	 */
	public EdgeShapeFigure(Vector points, Vector dragPoints, boolean outline) {
		this.points = points;
		this.dragPoints = dragPoints;
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
		// this.repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PolylineShape#fillShape(org.eclipse.draw2d.Graphics)
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

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PolylineShape#outlineShape(org.eclipse.draw2d.Graphics)
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
	public boolean isClicked(org.eclipse.draw2d.geometry.Point point){
		boolean isClicked=false;
		
		Point firstPoint = (Point) points.elementAt(0);
		Point lastPoint = firstPoint;
		Iterator it = points.subList(1, points.size()).iterator();
		while (it.hasNext()) {
			Point nextPoint = (Point) it.next();
			if (lastPoint.x != 0 && nextPoint.x != 0){
				isClicked=isOnLine(lastPoint, nextPoint, point);
				if (isClicked)
				return isClicked;
			}
			lastPoint = nextPoint;
		}
		return isClicked;
	}
	private boolean isOnLine(Point p1,Point p2, Point p3){
		boolean isOnLine=false;
		if ((p1.y==p2.y)&&(p2.y==p3.y))
		return true;
		
		double a=Math.sqrt((p1.x-p2.x)^2+(p1.y-p2.y)^2);
		double b=Math.sqrt((p3.x-p2.x)^2+(p3.y-p2.y)^2);
		double c=Math.sqrt((p3.x-p1.x)^2+(p3.y-p1.y)^2);
		
		double s=a*a+b*b-c*c;
		s=s/a;
		s=s/2;
		s=b*b-s*s;
		
		double distance=Math.sqrt(s);
		System.out.println("distance:"+distance);
		
		if (distance<10)
			return true;
		
		return isOnLine;
	}
}