package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.misc.VisualElementEvents;
import XOS.Message;
import XOS.Value;

public class Node extends Container {

	private boolean isDraggable = false;
	private final Vector ports = new Vector(); // ports associated with this
												// node
	private final Vector sourceEdges = new Vector(); // all edges for which this
														// node
	// is the source
	private final Vector targetEdges = new Vector(); // all edges for which this
														// node
	// is the target

	private boolean isSelectable = true;
	private boolean isClicked = false;
	private Vector<Point> points;

	private int distance = 20;
	private int gap = 3;

	public Node(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size, boolean isSelectable) {
		super(parent, handler, identity, location, size, null, null);

		points = new Vector();

		reSetPoints(location, size);

		this.isSelectable = isSelectable;
	}

	public Node(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean isSelectable) {

		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), isSelectable);
	}

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

	public Vector getPoints() {
		return points;
	}

	@Override
	public void delete() {
		super.delete();
		if (parent instanceof Diagram)
			((Diagram) parent).removeNode(this);
		else
			((Group) parent).removeNode(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		for (int i = 0; i < ports.size(); i++) {
			Port p = (Port) ports.elementAt(i);
			p.dispose();
		}
	}

	public Vector getPorts() {
		return ports;
	}

	public Vector getSourceEdges() {
		return sourceEdges();
	}

	public Vector getTargetEdges() {
		return targetEdges();
	}

	public boolean isDraggable() {
		return isDraggable;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	public Vector sourceEdges() {
		Vector validEdges = new Vector();
		for (int i = 0; i < sourceEdges.size(); i++) {
			Edge e = (Edge) sourceEdges.elementAt(i);
			if (getConnectionManager().isRenderingParent(e.parent.identity))
				validEdges.addElement(e);
		}
		return validEdges;
	}

	public Vector targetEdges() {
		Vector validEdges = new Vector();
		for (int i = 0; i < targetEdges.size(); i++) {
			Edge e = (Edge) targetEdges.elementAt(i);
			if (getConnectionManager().isRenderingParent(e.parent.identity))
				validEdges.addElement(e);
		}
		return validEdges;
	}

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

	public void isClicked(Point point) {
		isClicked = checkRectangleBoundary(point.x, point.y, location.x,
				location.y, size.width, size.height);
		// if (isClicked) {
		// System.out.println("node-" + identity + "-clicked");
		// }

	}

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

	public int getDistanceOfPoints(org.eclipse.draw2d.geometry.Point p1,
			org.eclipse.draw2d.geometry.Point p2) {
		int distance = (int) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x)
				+ (p1.y - p2.y) * (p1.y - p2.y));

		return distance;

	}

	public boolean isClicked() {
		return isClicked;
	}

	private boolean checkRectangleBoundary(float pointX, float pointY,
			float rectangleX, float rectangleY, float width, float height) {
		boolean isInside = ((pointX > rectangleX)
				&& (pointX < rectangleX + width) && (pointY > rectangleY) && (pointY < rectangleY
				+ height));

		return isInside;
	}

	public void deselectNode() {
		Message m = handler.newMessage("nodeDeselected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	public void selectNode() {
		Message m = handler.newMessage("nodeSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	@Override
	public void selected(int clicks) {
		Message m = handler.newMessage("selected", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(clicks);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	@Override
	public void synchronise(Element node) {
		synchronisePorts(node);
		synchroniseDisplays(node);
		super.synchronise(node);
	}

	public void synchroniseDisplays(Element node) {

	}

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

	public void addSourceEdge(Edge edge) {
		sourceEdges.addElement(edge);
		// if (isRendering())
		firePropertyChange("sourceEdges", null, null);
	}

	public void removeSourceEdge(Edge edge) {
		if (!sourceEdges.contains(edge))
			System.out.println("Node is not source of edge: " + edge.identity);
		sourceEdges.removeElement(edge);
		if (isRendering())
			firePropertyChange("sourceEdges", null, null);
	}

	public void addTargetEdge(Edge edge) {
		targetEdges.addElement(edge);
		// if (isRendering())
		firePropertyChange("targetEdges", null, null);
	}

	public void removeTargetEdge(Edge edge) {
		if (!targetEdges.contains(edge))
			System.out.println("Node is not target of edge: " + edge.identity);
		targetEdges.removeElement(edge);
		if (isRendering())
			firePropertyChange("targetEdges", null, null);
	}
}