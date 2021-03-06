package uk.ac.mdx.xmf.swt.editPart;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.FixedAnchor;
import uk.ac.mdx.xmf.swt.model.Edge;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeRouter.
 */
public class EdgeRouter extends AbstractRouter {

	/** The model. */
	private final Edge model;
	
	/** The constraints. */
	private final Map constraints = new HashMap(11);

	/**
	 * Instantiates a new edge router.
	 *
	 * @param model the model
	 */
	public EdgeRouter(Edge model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.ConnectionRouter#route(org.eclipse.draw2d.Connection)
	 */
	@Override
	public void route(Connection conn) {
		Rectangle start = findStartRectangle(conn);
		Rectangle end = findEndRectangle(conn);
		List bendpoints = (List) getConstraint(conn);
		if (bendpoints == null)
			bendpoints = Collections.EMPTY_LIST;
		plotRoute((EdgeFigure) conn, start, end, bendpoints);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractRouter#getConstraint(org.eclipse.draw2d.Connection)
	 */
	@Override
	public Object getConstraint(Connection connection) {
		return constraints.get(connection);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractRouter#setConstraint(org.eclipse.draw2d.Connection, java.lang.Object)
	 */
	@Override
	public void setConstraint(Connection connection, Object constraint) {
		constraints.put(connection, constraint);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractRouter#remove(org.eclipse.draw2d.Connection)
	 */
	@Override
	public void remove(Connection connection) {
		constraints.remove(connection);
	}

	/**
	 * Plot route.
	 *
	 * @param conn the conn
	 * @param start the start
	 * @param end the end
	 * @param bendpoints the bendpoints
	 */
	public void plotRoute(EdgeFigure conn, Rectangle start, Rectangle end,
			List bendpoints) {
		Point ref = new Point();
		Point ref1;
		Point ref2;
		if (bendpoints.size() == 0) {
			ref = conn.getRefPoint();
			Point centre = findCentrePoint(start, end);
			conn.translateToRelative(centre);
			if (validHorizontalReference(ref, conn, start.getCopy(),
					end.getCopy()))
				model.setRefPoint(new Point(centre.x, ref.y));
			else if (validVerticalReference(ref, conn, start.getCopy(),
					end.getCopy()))
				model.setRefPoint(new Point(ref.x, centre.y));
			else {
				ref = findCentrePoint(start, end);
				conn.translateToRelative(ref);
				model.setRefPoint(ref);
			}
			ref1 = ref.getCopy();
			ref2 = ref.getCopy();
		} else {
			ref1 = new Point(((Bendpoint) bendpoints.get(0)).getLocation());
			ref2 = new Point(
					((Bendpoint) bendpoints.get(bendpoints.size() - 1))
							.getLocation());
		}
		conn.translateToAbsolute(ref1);
		conn.translateToAbsolute(ref2);

		Point source;
		Point target;

		if (start.equals(end)) {
			source = findReferencePoint(start, ref1);
			target = findReferencePoint(end, ref2);
		} else if (start.contains(end)) {
			target = findReferencePoint(end, ref2);
			source = findReferencePoint2(start, ref1, target);
		} else if (end.contains(start)) {
			source = findReferencePoint(start, ref1);
			target = findReferencePoint2(end, ref2, source);
		} else {
			source = findReferencePoint(start, ref1);
			target = findReferencePoint(end, ref2);
		}
		conn.translateToRelative(source);
		PointList points = conn.getPoints();
		points.removeAllPoints();
		points.addPoint(source);

		for (int i = 0; i < bendpoints.size(); i++) {
			Bendpoint bp = (Bendpoint) bendpoints.get(i);
			points.addPoint(bp.getLocation());
		}
		conn.translateToRelative(target);
		points.addPoint(target);
		conn.setPoints(points);
	}

	/**
	 * Valid horizontal reference.
	 *
	 * @param ref the ref
	 * @param conn the conn
	 * @param start the start
	 * @param end the end
	 * @return true, if successful
	 */
	public boolean validHorizontalReference(Point ref, EdgeFigure conn,
			Rectangle start, Rectangle end) {
		conn.translateToRelative(start);
		conn.translateToRelative(end);
		return start.contains(start.x, ref.y) && end.contains(end.x, ref.y);
	}

	/**
	 * Valid vertical reference.
	 *
	 * @param ref the ref
	 * @param conn the conn
	 * @param start the start
	 * @param end the end
	 * @return true, if successful
	 */
	public boolean validVerticalReference(Point ref, EdgeFigure conn,
			Rectangle start, Rectangle end) {
		conn.translateToRelative(start);
		conn.translateToRelative(end);
		return start.contains(ref.x, start.y) && end.contains(ref.x, end.y);
	}

	/**
	 * Find reference point.
	 *
	 * @param r the r
	 * @param point the point
	 * @return the point
	 */
	public Point findReferencePoint(Rectangle r, Point point) {
		Point p = point.getCopy();
		if (p.x < r.x)
			p.x = r.x;
		if (p.x > r.x + r.width)
			p.x = r.x + r.width;
		if (p.y < r.y)
			p.y = r.y;
		if (p.y > r.y + r.height)
			p.y = r.y + r.height;
		return p;
	}

	/**
	 * Find reference point2.
	 *
	 * @param r the r
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the point
	 */
	public Point findReferencePoint2(Rectangle r, Point p1, Point p2) {
		Point p = p1.getCopy();

		if (p1.y == p2.y) {
			// the line is horizontal
			if (p1.x > p2.x) {
				// the direction is W to E
				p.x = r.x + r.width;
			} else {
				// the direction is E to W
				p.x = r.x;
			}
		}
		if (p1.x == p2.x) {
			// the line is vertical
			if (p1.y > p2.y) {
				// the direction is S to N
				p.y = r.y + r.height;
			} else {
				// the direction is N to S
				p.y = r.y;
			}
		}
		return p;
	}

	/**
	 * Find start rectangle.
	 *
	 * @param conn the conn
	 * @return the rectangle
	 */
	public Rectangle findStartRectangle(Connection conn) {
		ConnectionAnchor sourceAnchor = conn.getSourceAnchor();
		ConnectionAnchor targetAnchor = conn.getTargetAnchor();
		if (sourceAnchor instanceof FixedAnchor) {
			Rectangle r = ((FixedAnchor) sourceAnchor).getReferenceRectangle()
					.getCopy();
			return r;
		} else {
			Rectangle rec = new Rectangle();
			rec.setLocation(sourceAnchor.getLocation(targetAnchor
					.getReferencePoint()));
			rec.setSize(1, 1);
			return rec;
		}
	}

	/**
	 * Find end rectangle.
	 *
	 * @param conn the conn
	 * @return the rectangle
	 */
	public Rectangle findEndRectangle(Connection conn) {
		ConnectionAnchor sourceAnchor = conn.getSourceAnchor();
		ConnectionAnchor targetAnchor = conn.getTargetAnchor();
		if (targetAnchor instanceof FixedAnchor) {
			Rectangle r = ((FixedAnchor) targetAnchor).getReferenceRectangle()
					.getCopy();
			return r;
		} else {
			Rectangle rec = new Rectangle();
			rec.setLocation(targetAnchor.getLocation(sourceAnchor
					.getReferencePoint()));
			rec.setSize(1, 1);
			return rec;
		}
	}

	/**
	 * Find centre point.
	 *
	 * @param r1 the r1
	 * @param r2 the r2
	 * @return the point
	 */
	public Point findCentrePoint(Rectangle r1, Rectangle r2) {
		if (r1.contains(r2)) {
			int x1 = r2.x;
			int y1 = r1.y;
			int x2 = r2.x + r2.width;
			int y2 = r2.y;
			Rectangle r = new Rectangle(new Point(x1, y1), new Point(x2, y2));
			return r.getCenter();
		} else if (r2.contains(r1)) {
			int x1 = r1.x;
			int y1 = r2.y;
			int x2 = r1.x + r1.width;
			int y2 = r1.y;
			Rectangle r = new Rectangle(new Point(x1, y1), new Point(x2, y2));
			return r.getCenter();
		} else {
			int x1 = Math.max(r1.x, r2.x);
			int y1 = Math.max(r1.y, r2.y);
			int x2 = Math.min(r1.x + r1.width, r2.x + r2.width);
			int y2 = Math.min(r1.y + r1.height, r2.y + r2.height);
			Rectangle r = new Rectangle(new Point(x1, y1), new Point(x2, y2));
			return r.getCenter();
		}
	}
}
