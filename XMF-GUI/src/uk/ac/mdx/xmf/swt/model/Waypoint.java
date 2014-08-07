package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;

public class Waypoint extends DisplayWithPosition {

	public Waypoint(ClientElement parent, EventHandler handler,
			String identity, Point position) {
		super(parent, handler, identity, position);
	}

	public Waypoint(ClientElement parent, EventHandler handler,
			String identity, int x, int y) {
		super(parent, handler, identity, new Point(x, y));
	}

	public void delete() {
		super.delete();
		((Edge) parent).removeWaypoint(this);
	}

	public void move(Point location) {
		this.location = location;
		Edge edge = (Edge) parent;
		if (edge.isRendering())
			edge.firePropertyChange("waypoints", null, null);
	}
}