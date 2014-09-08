package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class Waypoint.
 */
public class Waypoint extends DisplayWithPosition {

	/**
	 * Instantiates a new waypoint.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param position the position
	 */
	public Waypoint(ClientElement parent, EventHandler handler,
			String identity, Point position) {
		super(parent, handler, identity, position);
	}

	/**
	 * Instantiates a new waypoint.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 */
	public Waypoint(ClientElement parent, EventHandler handler,
			String identity, int x, int y) {
		super(parent, handler, identity, new Point(x, y));
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	public void delete() {
		super.delete();
		((Edge) parent).removeWaypoint(this);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#move(org.eclipse.draw2d.geometry.Point)
	 */
	public void move(Point location) {
		this.location = location;
		Edge edge = (Edge) parent;
		if (edge.isRendering())
			edge.firePropertyChange("waypoints", null, null);
	}
}