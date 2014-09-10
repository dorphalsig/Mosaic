package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.misc.VisualElementEvents;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node extends Container {

	/** The is draggable. */
	private boolean isDraggable = false;
	
	/** The ports. */
	private final Vector ports = new Vector(); // ports associated with this
												// node
	/** The source edges. */
												private final Vector sourceEdges = new Vector(); // all edges for which this
														// node
	// is the source
	/** The target edges. */
														private final Vector targetEdges = new Vector(); // all edges for which this
														// node
	// is the target

	/** The is selectable. */
														private boolean isSelectable = true;
	
	/** The is clicked. */
	private boolean isClicked = false;
	
	/** The points. */
	private Vector<Point> points;

	/** The distance. */
	private int distance = 20;
	
	/** The gap. */
	private int gap = 3;

	/**
	 * Instantiates a new node.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 * @param isSelectable the is selectable
	 */
	public Node(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size, boolean isSelectable) {
		super(parent, handler, identity, location, size, null, null);

		points = new Vector();

		reSetPoints(location, size);

		this.isSelectable = isSelectable;
	}

	/**
	 * Instantiates a new node.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param isSelectable the is selectable
	 */
	public Node(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean isSelectable) {

		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), isSelectable);
	}

	/**
	 * Re set points.
	 *
	 * @param location the location
	 * @param size the size
	 */
	public void reSetPoints(Point location, Dimension size) {
		points.clear();
		
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
		points.addElement(new Point(location.x + size.width / 2 + gap + gap,
				location.y));
		points.addElement(new Point(location.x + size.width - gap - gap,
				location.y));

		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x + size.width - gap, location.y));
		points.addElement(new Point(location.x + size.width, location.y));

		// second line

		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x + size.width, location.y + gap));
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
		points.addElement(new Point(location.x + size.width / 2 + gap + gap,
				location.y + size.height));

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
		points.addElement(new Point(location.x + gap, location.y + size.height));
		points.addElement(new Point(location.x, location.y + size.height));

		// fouth line
		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x, location.y + size.height - gap));
		points.addElement(new Point(location.x, location.y + size.height));

		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x, location.y + size.height / 2
				+ gap + gap));
		points.addElement(new Point(location.x, location.y + size.height - gap
				- gap));

		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x, location.y + size.height / 2));
		points.addElement(new Point(location.x, location.y + size.height / 2
				+ gap));

		points.addElement(new Point(0, 0));
		points.addElement(new Point(location.x, location.y + gap + gap));
		points.addElement(new Point(location.x, location.y + size.height / 2
				- gap));
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public Vector getPoints() {
		return points;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		if (parent instanceof Diagram)
			((Diagram) parent).removeNode(this);
		else
			((Group) parent).removeNode(this);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (int i = 0; i < ports.size(); i++) {
			Port p = (Port) ports.elementAt(i);
			p.dispose();
		}
	}

	/**
	 * Gets the ports.
	 *
	 * @return the ports
	 */
	public Vector getPorts() {
		return ports;
	}

	/**
	 * Gets the source edges.
	 *
	 * @return the source edges
	 */
	public Vector getSourceEdges() {
		return sourceEdges();
	}

	/**
	 * Gets the target edges.
	 *
	 * @return the target edges
	 */
	public Vector getTargetEdges() {
		return targetEdges();
	}

	/**
	 * Checks if is draggable.
	 *
	 * @return true, if is draggable
	 */
	public boolean isDraggable() {
		return isDraggable;
	}

	/**
	 * Checks if is selectable.
	 *
	 * @return true, if is selectable
	 */
	public boolean isSelectable() {
		return isSelectable;
	}

	/**
	 * Source edges.
	 *
	 * @return the vector
	 */
	public Vector sourceEdges() {
		Vector validEdges = new Vector();
		for (int i = 0; i < sourceEdges.size(); i++) {
			Edge e = (Edge) sourceEdges.elementAt(i);
			if (getConnectionManager().isRenderingParent(e.parent.identity))
				validEdges.addElement(e);
		}
		return validEdges;
	}

	/**
	 * Target edges.
	 *
	 * @return the vector
	 */
	public Vector targetEdges() {
		Vector validEdges = new Vector();
		for (int i = 0; i < targetEdges.size(); i++) {
			Edge e = (Edge) targetEdges.elementAt(i);
			if (getConnectionManager().isRenderingParent(e.parent.identity))
				validEdges.addElement(e);
		}
		return validEdges;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("enableDrag")
				&& message.args[0].hasStrValue(identity) && message.arity == 1) {
			this.isDraggable = true;
			return true;
		}
		if (message.hasName("newPort") && message.args[0].hasStrValue(identity)
				&& message.arity == 6) {
			String identity = message.args[1].strValue();
			int x = message.args[2].intValue;
			int y = message.args[3].intValue;
			int width = message.args[4].intValue;
			int height = message.args[5].intValue;
			newPort(identity, x, y, width, height);
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * New port.
	 *
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @return the port
	 */
	public Port newPort(String identity, int x, int y, int width, int height) {
		Port port = new Port(this, handler, identity, new Point(x, y),
				new Dimension(width, height));
		ports.addElement(port);
		PortRegistry.addPort(identity, this);
		if (isRendering())
			firePropertyChange("refreshPorts", null, null);
		return port;
	}

	// invoked by changes to the diagram
	// events are raised as a result

	/**
	 * Move resize.
	 *
	 * @param location the location
	 * @param size the size
	 */
	public void moveResize(Point location, Dimension size) {
		if (!location.equals(this.location)) {
			Message m = handler.newMessage("move", 3);
			Value v1 = new Value(identity);
			Value v2 = new Value(location.x);
			Value v3 = new Value(location.y);
			m.args[0] = v1;
			m.args[1] = v2;
			m.args[2] = v3;
			handler.raiseEvent(m);
			this.move(location);
		}
		if (!size.equals(this.size)) {
			Message m = handler.newMessage("resizeNode", 3);
			Value v1 = new Value(identity);
			Value v2 = new Value(size.width);
			Value v3 = new Value(size.height);
			m.args[0] = v1;
			m.args[1] = v2;
			m.args[2] = v3;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Move resize.
	 *
	 * @param location the location
	 */
	public void moveResize(Point location) {
		if (!location.equals(this.location)) {
			Message m = handler.newMessage("move", 3);
			Value v1 = new Value(identity);
			Value v2 = new Value(location.x);
			Value v3 = new Value(location.y);
			m.args[0] = v1;
			m.args[1] = v2;
			m.args[2] = v3;
			handler.raiseEvent(m);
			this.move(location);
		}

	}

	/**
	 * Move resize.
	 *
	 * @param size the size
	 */
	public void moveResize(Dimension size) {
		if (!size.equals(this.size)) {
			Message m = handler.newMessage("resizeNode", 3);
			Value v1 = new Value(identity);
			Value v2 = new Value(size.width);
			Value v3 = new Value(size.height);
			m.args[0] = v1;
			m.args[1] = v2;
			m.args[2] = v3;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Checks if is clicked.
	 *
	 * @param point the point
	 */
	public void isClicked(Point point) {
		isClicked = checkRectangleBoundary(point.x, point.y, location.x,
				location.y, size.width, size.height);
		// if (isClicked) {
		// System.out.println("node-" + identity + "-clicked");
		// }

	}

	/**
	 * Checks if is drag point clicked.
	 *
	 * @param point the point
	 * @return the string
	 */
	public String isDragPointClicked(Point point) {
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
		return "";

	}

	/**
	 * Gets the distance of points.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the distance of points
	 */
	public int getDistanceOfPoints(org.eclipse.draw2d.geometry.Point p1,
			org.eclipse.draw2d.geometry.Point p2) {
		int distance = (int) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
				+ (p1.y - p2.y) * (p1.y - p2.y));

		return distance;

	}

	/**
	 * Checks if is clicked.
	 *
	 * @return true, if is clicked
	 */
	public boolean isClicked() {
		return isClicked;
	}

	/**
	 * Check rectangle boundary.
	 *
	 * @param pointX the point x
	 * @param pointY the point y
	 * @param rectangleX the rectangle x
	 * @param rectangleY the rectangle y
	 * @param width the width
	 * @param height the height
	 * @return true, if successful
	 */
	private boolean checkRectangleBoundary(float pointX, float pointY,
			float rectangleX, float rectangleY, float width, float height) {
		boolean isInside = ((pointX > rectangleX)
				&& (pointX < rectangleX + width) && (pointY > rectangleY) && (pointY < rectangleY
				+ height));

		return isInside;
	}

	/**
	 * Deselect node.
	 */
	public void deselectNode() {
		Message m = handler.newMessage("nodeDeselected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/**
	 * Select node.
	 */
	public void selectNode() {
		Message m = handler.newMessage("nodeSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Display#selected(int)
	 */
	@Override
	public void selected(int clicks) {
		Message m = handler.newMessage("selected", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(clicks);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element node) {
		synchronisePorts(node);
		synchroniseDisplays(node);
		super.synchronise(node);
	}

	/**
	 * Synchronise displays.
	 *
	 * @param node the node
	 */
	public void synchroniseDisplays(Element node) {

	}

	/**
	 * Synchronise ports.
	 *
	 * @param node the node
	 */
	public void synchronisePorts(Element node) {

		// Check there is a port for each of the ports in the document

		for (int i = 0; i < node.childrenSize(); i++) {
			Element child = node.getChild(i);
			if (child.hasName(XMLBindings.port)) {
				boolean found = false;
				String id = child.getString("identity");
				for (int z = 0; z < ports.size(); z++) {
					Port port = (Port) ports.elementAt(z);
					if (port.getIdentity().equals(id)) {
						found = true;
						port.synchronise(child);
					}
				}
				;
				if (!found) {
					int x = child.getInteger("x");
					int y = child.getInteger("y");
					int width = child.getInteger("width");
					int height = child.getInteger("height");
					Port port = newPort(id, x, y, width, height);
					ports.addElement(port);
					port.synchronise(child);
				}
			}
		}

		// Check that each of the nodes ports has a port in the document

		Vector toRemove = new Vector();
		for (int i = 0; i < ports.size(); i++) {
			boolean found = false;
			Port port = (Port) ports.elementAt(i);
			for (int z = 0; z < node.childrenSize(); z++) {
				Element child = node.getChild(z);
				if (child.hasName(XMLBindings.port)) {
					String id = child.getString("identity");
					found = port.getIdentity().equals(id);
				}
			}
			;
			if (!found)
				toRemove.addElement(port);
		}

		// Delete ports

		for (int i = 0; i < toRemove.size(); i++) {
			Port port = (Port) toRemove.elementAt(i);
			port.delete();
		}
	}

	/**
	 * Adds the source edge.
	 *
	 * @param edge the edge
	 */
	public void addSourceEdge(Edge edge) {
		sourceEdges.addElement(edge);
		// if (isRendering())
		firePropertyChange("sourceEdges", null, null);
	}

	/**
	 * Removes the source edge.
	 *
	 * @param edge the edge
	 */
	public void removeSourceEdge(Edge edge) {
		if (!sourceEdges.contains(edge))
			System.out.println("Node is not source of edge: " + edge.identity);
		sourceEdges.removeElement(edge);
		if (isRendering())
			firePropertyChange("sourceEdges", null, null);
	}

	/**
	 * Adds the target edge.
	 *
	 * @param edge the edge
	 */
	public void addTargetEdge(Edge edge) {
		targetEdges.addElement(edge);
		// if (isRendering())
		firePropertyChange("targetEdges", null, null);
	}

	/**
	 * Removes the target edge.
	 *
	 * @param edge the edge
	 */
	public void removeTargetEdge(Edge edge) {
		if (!targetEdges.contains(edge))
			System.out.println("Node is not target of edge: " + edge.identity);
		targetEdges.removeElement(edge);
		if (isRendering())
			firePropertyChange("targetEdges", null, null);
	}
}