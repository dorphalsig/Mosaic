package uk.ac.mdx.xmf.swt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IdManager;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.misc.VisualElementEvents;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Edge.
 */
public class Edge extends CommandEvent {

	/** The source node. */
	private Node sourceNode;

	/** The target node. */
	private Node targetNode;

	/** The source port. */
	private String sourcePort;

	/** The target port. */
	private String targetPort;

	/** The source head. */
	private int sourceHead;

	/** The target head. */
	private int targetHead;

	/** The style. */
	private int style;

	/** The type. */
	private String type = "normal";

	/** The width. */
	private int width = 1;

	/** The ref point. */
	private Point refPoint = new Point();

	/** The waypoints. */
	private List waypoints = new ArrayList();

	/** The labels. */
	private final Vector labels = new Vector();

	/** The hidden. */
	private boolean hidden = false;

	/** The color. */
	private RGB color;

	/** The is selectable. */
	private boolean isSelectable = true;

	/** The is clicked. */
	private boolean isClicked = false;

	/** The points. */
	private Vector<Point> points = new Vector<Point>();

	/** The drag points. */
	private Vector<Point> dragPoints = new Vector<Point>();

	/** The gap. */
	private int gap = 3;

	/** The set drag point once. */
	private boolean setDragPointOnce = false;

	/** The get point index. */
	private int getPointIndex = 0;

	/**
	 * Instantiates a new edge.
	 * 
	 * @param parent
	 *            the parent
	 * @param handler
	 *            the handler
	 * @param identity
	 *            the identity
	 * @param sourceNode
	 *            the source node
	 * @param targetNode
	 *            the target node
	 * @param sourcePort
	 *            the source port
	 * @param targetPort
	 *            the target port
	 * @param xRef
	 *            the x ref
	 * @param yRef
	 *            the y ref
	 * @param sourceHead
	 *            the source head
	 * @param targetHead
	 *            the target head
	 * @param dotStyle
	 *            the dot style
	 * @param color
	 *            the color
	 */
	public Edge(ClientElement parent, EventHandler handler, String identity,
			Node sourceNode, Node targetNode, String sourcePort,
			String targetPort, int xRef, int yRef, int sourceHead,
			int targetHead, int dotStyle, RGB color) {
		super(parent, handler, identity);
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		this.sourcePort = sourcePort;
		this.targetPort = targetPort;
		this.targetHead = targetHead;
		this.sourceHead = sourceHead;
		this.color = color;
		this.refPoint = new Point(xRef, yRef);
		if (dotStyle != 0)
			this.style = dotStyle;
		else
			this.style = 1;
		sourceNode.addSourceEdge(this);
		targetNode.addTargetEdge(this);

		// set intial points of shape
		// int gap = 3;
		//
		// Point location1 = sourceNode.getLocation();
		// Point location2 = targetNode.getLocation();
		//
		// setPoints(location1, location2, gap);
	}

	/**
	 * Sets the points.
	 * 
	 * @param location1
	 *            the location1
	 * @param location2
	 *            the location2
	 */
	public void setPoints(Point location1, Point location2) {
		if ((location1 != null) && (location2 != null)) {
			if (location1.y < location2.y)
				calculatePoints(location1, location2);
			else
				calculatePoints(location2, location1);
		}
	}

	/**
	 * Calculate points.
	 * 
	 * @param location1
	 *            the location1
	 * @param location2
	 *            the location2
	 */
	public void calculatePoints(Point location1, Point location2) {
		int x1 = location1.x;
		int y1 = location1.y;
		int x2 = location2.x;
		int y2 = location2.y;
		int h1 = sourceNode.getSize().height;
		int w1 = sourceNode.getSize().width;
		int h2 = targetNode.getSize().height;
		int w2 = targetNode.getSize().width;

		Point topPoint = new Point();
		Point middlePoint = new Point();
		Point bottomPoint = new Point();

		Point startLine1 = new Point();
		Point endLine1 = new Point();
		Point startLine2 = new Point();
		Point endLine2 = new Point();

		// avoid y2-y1=0 divided by zero;
		if (y2 == y1) {
			y2 = y1 + 1;
			x2 = x1;
		}

		if (x1 < x2) {
			topPoint.x = x1 + (h1 / 2) * (x2 - x1) / (y2 - y1);
			topPoint.y = y1 + h1 / 2;

			bottomPoint.x = x2 - (h2 / 2) * (x2 - x1) / (y2 - y1);
			bottomPoint.y = y2 - h2 / 2;

			middlePoint.x = topPoint.x + Math.abs((topPoint.x - bottomPoint.x))
					/ 2;
			middlePoint.y = topPoint.y + Math.abs((topPoint.y - bottomPoint.y))
					/ 2;

			startLine1.x = topPoint.x + gap * (x2 - x1) / (y2 - y1);
			startLine1.y = topPoint.y + gap;

			endLine1.x = startLine1.x
					+ ((bottomPoint.y - topPoint.y) / 2 - gap - gap)
					* (x2 - x1) / (y2 - y1);
			endLine1.y = middlePoint.y - gap;

			startLine2.x = middlePoint.x + gap * (x2 - x1) / (y2 - y1);
			startLine2.y = middlePoint.y + gap;

			endLine2.x = startLine2.x
					+ ((bottomPoint.y - topPoint.y) / 2 - gap - gap)
					* (x2 - x1) / (y2 - y1);
			endLine2.y = bottomPoint.y - gap;

		} else {
			topPoint.x = x1 + (h1 / 2) * (x2 - x1) / (y2 - y1);
			topPoint.y = y1 + h1 / 2;

			bottomPoint.x = x2 - (h2 / 2) * (x2 - x1) / (y2 - y1);
			bottomPoint.y = y2 - h2 / 2;

			middlePoint.x = topPoint.x - Math.abs((topPoint.x - bottomPoint.x))
					/ 2;
			middlePoint.y = topPoint.y + Math.abs((topPoint.y - bottomPoint.y))
					/ 2;

			startLine1.x = topPoint.x + gap * (x2 - x1) / (y2 - y1);
			startLine1.y = topPoint.y + gap;

			endLine1.x = startLine1.x
					+ ((bottomPoint.y - topPoint.y) / 2 - gap - gap)
					* (x2 - x1) / (y2 - y1);
			endLine1.y = middlePoint.y - gap;

			startLine2.x = middlePoint.x + gap * (x2 - x1) / (y2 - y1);
			startLine2.y = middlePoint.y + gap;

			endLine2.x = startLine2.x
					+ ((bottomPoint.y - topPoint.y) / 2 - gap - gap)
					* (x2 - x1) / (y2 - y1);
			endLine2.y = bottomPoint.y - gap;
		}

		dragPoints.add(new Point(topPoint.x, topPoint.y));
		dragPoints.add(new Point(middlePoint.x, middlePoint.y));
		dragPoints.add(new Point(bottomPoint.x, bottomPoint.y));

		points.add(startLine1);
		points.add(endLine1);

		points.addElement(new Point(0, 0));
		points.add(startLine2);
		points.add(endLine2);

	}

	/**
	 * Sets the drag points.
	 */
	public void setDragPoints() {
		setDragPointOnce = true;
	}

	/**
	 * Sets the drag points.
	 * 
	 * @param newPoint
	 *            the new point
	 * @param index
	 *            the index
	 */
	private int index = 1;

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setDragPoints(org.eclipse.draw2d.geometry.Point newPoint) {
		// index = 1;
		if (setDragPointOnce) {
			org.eclipse.draw2d.geometry.Point before = dragPoints
					.get(index - 1);
			org.eclipse.draw2d.geometry.Point next = dragPoints.get(index + 1);

			dragPoints.remove(index); // remove old one
			dragPoints.add(index, newPoint); // middle one

			org.eclipse.draw2d.geometry.Point m1 = new org.eclipse.draw2d.geometry.Point();
			org.eclipse.draw2d.geometry.Point m2 = new org.eclipse.draw2d.geometry.Point();

			m1.x = (before.x + newPoint.x) / 2;
			m1.y = (before.y + newPoint.y) / 2;
			m2.x = (next.x + newPoint.x) / 2;
			m2.y = (next.y + newPoint.y) / 2;

			dragPoints.add(index, m1);
			dragPoints.add(index + 2, m2);
		} else {
			org.eclipse.draw2d.geometry.Point before = dragPoints
					.get(index - 1);
			org.eclipse.draw2d.geometry.Point next = null;
			if (index + 3 < dragPoints.size()) {
				next = dragPoints.get(index + 3);

				dragPoints.remove(index); // remove old one
				dragPoints.remove(index); //
				dragPoints.remove(index); //

				org.eclipse.draw2d.geometry.Point m1 = new org.eclipse.draw2d.geometry.Point();
				org.eclipse.draw2d.geometry.Point m2 = new org.eclipse.draw2d.geometry.Point();

				m1.x = (before.x + newPoint.x) / 2;
				m1.y = (before.y + newPoint.y) / 2;
				m2.x = (next.x + newPoint.x) / 2;
				m2.y = (next.y + newPoint.y) / 2;

				dragPoints.add(index, m2);
				dragPoints.add(index, newPoint);
				dragPoints.add(index, m1);

				System.out.println("size of dragpoint:" + dragPoints.size());
			}
		}

		points.clear();

		for (int i = 0; i < dragPoints.size() - 1; i++) {
			org.eclipse.draw2d.geometry.Point p1 = dragPoints.get(i);
			org.eclipse.draw2d.geometry.Point p2 = dragPoints.get(i + 1);
			calculateLinePoints(p1, p2);
		}
		System.out.println("size of dragpoint:" + dragPoints.size());
		setDragPointOnce = false;
	}

	/**
	 * Calculate line points.
	 * 
	 * @param topPoint
	 *            the top point
	 * @param bottomPoint
	 *            the bottom point
	 */
	public void calculateLinePoints(org.eclipse.draw2d.geometry.Point topPoint,
			org.eclipse.draw2d.geometry.Point bottomPoint) {

		Point startLine1 = new Point();
		Point endLine1 = new Point();
		int x1 = topPoint.x;
		int y1 = topPoint.y;
		int x2 = bottomPoint.x;
		int y2 = bottomPoint.y;

		// avoid y2-y1=0 divided by zero;
		if (y2 == y1) {
			y2 = y1 + 1;
			x2 = x1;
		}

		startLine1.x = topPoint.x + gap * (x2 - x1) / (y2 - y1);
		startLine1.y = topPoint.y + gap;

		endLine1.x = bottomPoint.x - gap * (x2 - x1) / (y2 - y1);
		endLine1.y = bottomPoint.y - gap;

		points.addElement(new Point(0, 0));
		points.add(startLine1);
		points.add(endLine1);

	}

	/**
	 * Gets the point element.
	 * 
	 * @param location
	 *            the location
	 * @return the point element
	 */
	public String getPointElement(org.eclipse.draw2d.geometry.Point location) {
		String element = "";
		if (dragPoints.size() > 0) {
			for (int i = 1; i < dragPoints.size() - 1; i++) {
				int distance = (getDistanceOfPoints(dragPoints.get(i), location));
				if (distance < ((gap + 1) * (gap + 1))) {
					if (i % 2 == 1) {
						element = VisualElementEvents.wayPointEdgePoint;
						index = i;
					} else {
						element = VisualElementEvents.moveEdgePoint;
						index = i;
					}
				}
			}
		}
		// System.out.println("getPointIndex:" + getPointIndex);
		return element;
	}

	/**
	 * Gets the point index.
	 * 
	 * @return the point index
	 */
	public int getPointIndex() {
		return getPointIndex;
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
	 * Gets the points.
	 * 
	 * @return the points
	 */
	public Vector getPoints() {
		return points;
	}

	/**
	 * Gets the drag points.
	 * 
	 * @return the drag points
	 */
	public Vector getDragPoints() {
		return dragPoints;
	}

	/**
	 * Checks if is clicked.
	 * 
	 * @param point
	 *            the point
	 */
	public void isClicked(Point point) {
		// List<?> list = new ArrayList(points);
		// Point location=(Point) list.get(0);
		// isClicked = checkRectangleBoundary(point.x, point.y, location.x,
		// location.y, size.width, size.height);
		// if (isClicked) {
		// System.out.println("node-" + identity + "-clicked");
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (int i = 0; i < waypoints.size(); i++) {
			Waypoint w = (Waypoint) waypoints.get(i);
			w.dispose();
		}
		for (int i = 0; i < labels.size(); i++) {
			if (labels.elementAt(i) instanceof EdgeText) {
				EdgeText l = (EdgeText) labels.get(i);
				l.dispose();
			} else {
				MultilineEdgeText l = (MultilineEdgeText) labels.get(i);
				l.dispose();
			}
		}
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public RGB getColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#stopRender()
	 */
	@Override
	public void stopRender() {
		setRender(false);
		render(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.model.CommandEvent#identityExists(java.lang.String)
	 */
	@Override
	public boolean identityExists(String identity) {
		if (this.identity.equals(identity))
			return true;
		for (int i = 0; i < labels.size(); i++) {
			CommandEvent et = (CommandEvent) labels.elementAt(i);
			if (et.identityExists(identity))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#startRender()
	 */
	@Override
	public void startRender() {
		setRender(true);
		render(true);
	}

	/**
	 * Render.
	 * 
	 * @param render
	 *            the render
	 */
	public void render(boolean render) {
		for (int i = 0; i < labels.size(); i++) {
			Object o = labels.elementAt(i);
			if (o instanceof EdgeText) {
				EdgeText label = (EdgeText) o;
				if (!render)
					label.stopRender();
				else
					label.startRender();
			}
			if (o instanceof MultilineEdgeText) {
				MultilineEdgeText label = (MultilineEdgeText) o;
				if (!render)
					label.stopRender();
				else
					label.startRender();
			}
		}
		for (int i = 0; i < waypoints.size(); i++) {
			Waypoint waypoint = (Waypoint) waypoints.get(i);
			if (!render)
				waypoint.stopRender();
			else {
				waypoint.startRender();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		sourceNode.removeSourceEdge(this);
		targetNode.removeTargetEdge(this);
		sourceNode = null;
		targetNode = null;
		AbstractDiagram diagram = (AbstractDiagram) parent;
		diagram.removeEdge(this);

		/*
		 * if(parent instanceof Diagram) ((Diagram)parent).removeEdge(this);
		 * else ((Group)parent).removeEdge(this); if (isRendering())
		 * firePropertyChange("newSourceTarget", null, null);
		 */
	}

	/**
	 * Refresh.
	 */
	public void refresh() {
		if (isRendering())
			firePropertyChange("newSourceTarget", null, null);
	}

	/**
	 * Hide.
	 */
	public void hide() {
		hidden = true;
		// if(render)
		firePropertyChange("visibilityChange", null, null);
	}

	/**
	 * Sets the new source.
	 * 
	 * @param newSource
	 *            the new source
	 * @param sourcePort
	 *            the source port
	 */
	public void setNewSource(Node newSource, String sourcePort) {
		sourceNode.removeSourceEdge(this);
		this.sourceNode = newSource;
		sourceNode.addSourceEdge(this);
		this.sourcePort = sourcePort;
		if (isRendering())
			firePropertyChange("newSourceTarget", null, null);
	}

	/**
	 * Sets the new target.
	 * 
	 * @param newTarget
	 *            the new target
	 * @param targetPort
	 *            the target port
	 */
	public void setNewTarget(Node newTarget, String targetPort) {
		targetNode.removeTargetEdge(this);
		this.targetNode = newTarget;
		targetNode.addTargetEdge(this);
		this.targetPort = targetPort;
		if (isRendering())
			firePropertyChange("newSourceTarget", null, null);
	}

	/**
	 * Hidden.
	 * 
	 * @return true, if successful
	 */
	public boolean hidden() {
		return hidden;
	}

	/**
	 * Show.
	 */
	public void show() {
		hidden = false;
		// if(render)
		firePropertyChange("visibilityChange", null, null);
	}

	/**
	 * Gets the source port.
	 * 
	 * @return the source port
	 */
	public String getSourcePort() {
		return sourcePort;
	}

	/**
	 * Gets the target port.
	 * 
	 * @return the target port
	 */
	public String getTargetPort() {
		return targetPort;
	}

	/**
	 * Gets the waypoints.
	 * 
	 * @return the waypoints
	 */
	public List getWaypoints() {
		return waypoints;
	}

	/**
	 * Gets the source head.
	 * 
	 * @return the source head
	 */
	public int getSourceHead() {
		return sourceHead;
	}

	/**
	 * Gets the target head.
	 * 
	 * @return the target head
	 */
	public int getTargetHead() {
		return targetHead;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the style.
	 * 
	 * @return the style
	 */
	public int getStyle() {
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("setEdgeSource")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			String sourcePort = message.args[1].strValue();
			Node node = PortRegistry.getNode(sourcePort);
			if (node != null)
				setNewSource(node, sourcePort);
			return true;
		}
		if (message.hasName("setEdgeTarget")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			String targetPort = message.args[1].strValue();
			Node node = PortRegistry.getNode(targetPort);
			if (node != null)
				setNewTarget(node, targetPort);
			return true;
		}
		if (message.hasName("setEdgeWidth")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			width = message.args[1].intValue;
			if (isRendering())
				firePropertyChange("newEdgeWidth", null, null);
			return true;
		}
		if (message.hasName("changeHeads")
				&& message.args[0].hasStrValue(identity) && message.arity == 3) {
			int sourceHead = message.args[1].intValue;
			int targetHead = message.args[2].intValue;
			changeHeads(sourceHead, targetHead);
			return true;
		}
		if (message.hasName("newWaypoint")
				&& message.args[0].hasStrValue(identity) && message.arity == 5) {
			String waypointID = message.args[1].strValue();
			int index = message.args[2].intValue;
			int x = message.args[3].intValue;
			int y = message.args[4].intValue;
			addNewWaypoint(waypointID, index, x, y);
			return true;
		}
		if (message.hasName("newEdgeText")
				&& message.args[0].hasStrValue(identity)) {
			labels.addElement(ModelFactory.newEdgeText(this, handler, message));
			Main.getInstance().getView().refresh(labels);
			// if (isRendering())
			firePropertyChange("newEdgeText", null, null);
			return true;
		}
		if (message.hasName("newMultilineEdgeText")
				&& message.args[0].hasStrValue(identity)) {
			labels.addElement(ModelFactory.newMultilineEdgeText(this, handler,
					message));
			if (isRendering())
				firePropertyChange("newEdgeText", null, null);
			return true;
		}
		if (message.hasName("setRefPoint")
				&& message.args[0].hasStrValue(identity) && message.arity == 3) {
			int x = message.args[1].intValue;
			int y = message.args[2].intValue;
			this.changeRefPoint(new Point(x, y));
			return true;
		}
		if (message.hasName("setEdgeStyle")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			int style = message.args[1].intValue;
			this.setStyle(style);
			return true;
		}
		if (message.hasName("setEdgeType")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			String type = message.args[1].strValue();
			this.setType(type);
			return true;
		}
		if (message.hasName("show") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			show();
			return true;
		}
		if (message.hasName("hide") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			hide();
			return true;
		}
		if (message.hasName("setColor")
				&& message.args[0].hasStrValue(identity) && message.arity == 4) {
			int red = message.args[1].intValue;
			int green = message.args[2].intValue;
			int blue = message.args[3].intValue;
			setColor(red, green, blue);
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Adds the dummy waypoint.
	 * 
	 * @param index
	 *            the index
	 * @param location
	 *            the location
	 */
	public void addDummyWaypoint(int index, Point location) {
		Waypoint waypoint = new Waypoint(this, handler, "dummy", location);
		waypoints.add(index, waypoint);
		// if (isRendering())
		firePropertyChange("waypoints", null, null);
	}

	/**
	 * Adds the new waypoint.
	 * 
	 * @param waypointID
	 *            the waypoint id
	 * @param index
	 *            the index
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void addNewWaypoint(String waypointID, int index, int x, int y) {
		if (!IdManager.changeId("dummy", waypointID)) {
			Waypoint waypoint = new Waypoint(this, handler, waypointID,
					new Point(x, y));
			waypoints.add(index, waypoint);
			// if (isRendering())
			firePropertyChange("waypoints", null, null);
		}
	}

	/**
	 * Removes the edge text.
	 * 
	 * @param edgetext
	 *            the edgetext
	 */
	public void removeEdgeText(EdgeText edgetext) {
		labels.remove(edgetext);
		if (isRendering())
			firePropertyChange("waypoints", null, null);
	}

	/**
	 * Removes the waypoint.
	 * 
	 * @param waypoint
	 *            the waypoint
	 */
	public void removeWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
		if (isRendering())
			firePropertyChange("waypoints", null, null);
	}

	/**
	 * Gets the waypoint identity.
	 * 
	 * @param index
	 *            the index
	 * @return the waypoint identity
	 */
	public String getWaypointIdentity(int index) {
		Waypoint waypoint = (Waypoint) waypoints.get(index);
		return waypoint.getIdentity();
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Empty waypoints.
	 * 
	 * @return true, if successful
	 */
	public boolean emptyWaypoints() {
		return waypoints.size() == 0;
	}

	/**
	 * Change heads.
	 * 
	 * @param sourceHead
	 *            the source head
	 * @param targetHead
	 *            the target head
	 */
	public void changeHeads(int sourceHead, int targetHead) {
		this.sourceHead = sourceHead;
		this.targetHead = targetHead;
		if (isRendering())
			firePropertyChange("headsChange", null, null);
	}

	/**
	 * Gets the contents.
	 * 
	 * @return the contents
	 */
	public Vector getContents() {
		return labels;
	}

	/**
	 * Edge deselected.
	 */
	public void edgeDeselected() {
		Message m = handler.newMessage("edgeDeselected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/**
	 * Edge selected.
	 */
	public void edgeSelected() {
		Message m = handler.newMessage("edgeSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
	}

	/**
	 * Sets the ref point.
	 * 
	 * @param refPoint
	 *            the new ref point
	 */
	public void setRefPoint(Point refPoint) {
		if (!this.refPoint.equals(refPoint)) {
			raiseRefPointEvent(refPoint);
			this.refPoint = refPoint;
			// if (isRendering())
			firePropertyChange("refPoint", null, null);
		}
	}

	/**
	 * Change ref point.
	 * 
	 * @param refPoint
	 *            the ref point
	 */
	public void changeRefPoint(Point refPoint) {
		this.refPoint = refPoint;
		if (isRendering())
			firePropertyChange("refPoint", null, null);
	}

	/**
	 * Raise ref point event.
	 * 
	 * @param position
	 *            the position
	 */
	public void raiseRefPointEvent(Point position) {
		Message m = handler.newMessage("setReferencePoint", 3);
		Value v1 = new Value(getIdentity());
		Value v2 = new Value(position.x);
		Value v3 = new Value(position.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		handler.raiseEvent(m);
	}

	/**
	 * Gets the ref point.
	 * 
	 * @return the ref point
	 */
	public Point getRefPoint() {
		return refPoint;
	}

	/**
	 * Selected.
	 * 
	 * @param clicks
	 *            the clicks
	 */
	public void selected(int clicks) {
		Message m = handler.newMessage("selected", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(clicks);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/**
	 * Sets the color.
	 * 
	 * @param red
	 *            the red
	 * @param green
	 *            the green
	 * @param blue
	 *            the blue
	 */
	public void setColor(int red, int green, int blue) {
		color = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}

	/**
	 * Sets the style.
	 * 
	 * @param style
	 *            the new style
	 */
	public void setStyle(int style) {
		this.style = style;
		if (isRendering())
			firePropertyChange("styleChange", null, null);
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
		if (isRendering())
			firePropertyChange("typeChange", null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.client.ClientElement#synchronise(uk.ac.mdx.xmf.swt.
	 * client.xml.Element)
	 */
	@Override
	public void synchronise(Element edge) {
		synchroniseWaypoints(edge);
		synchroniseLabels(edge);
		synchroniseMultilineLabels(edge);
		String sourcePort = edge.getString("source");
		if (!sourcePort.equals(this.sourcePort)) {
			Node node = PortRegistry.getNode(sourcePort);
			if (node != null)
				setNewSource(node, sourcePort);
		}
		String targetPort = edge.getString("target");
		if (!targetPort.equals(this.targetPort)) {
			Node node = PortRegistry.getNode(targetPort);
			if (node != null)
				setNewTarget(node, targetPort);
		}
		sourceHead = edge.getInteger("sourceHead");
		targetHead = edge.getInteger("targetHead");
		style = edge.getInteger("lineStyle");
		width = edge.getInteger("width");
		refPoint.x = edge.getInteger("refx");
		refPoint.y = edge.getInteger("refy");
	}

	/**
	 * Synchronise labels.
	 * 
	 * @param edge
	 *            the edge
	 */
	public void synchroniseLabels(Element edge) {

		// Check that there is a label for each of the model's edge labels

		for (int i = 0; i < edge.childrenSize(); i++) {
			Element child = edge.getChild(i);
			if (child.hasName(XMLBindings.label)) {
				String id = child.getString("identity");
				boolean found = false;
				for (int z = 0; z < labels.size(); z++) {
					if (labels.elementAt(z) instanceof EdgeText) {
						EdgeText edgeText = (EdgeText) labels.elementAt(z);
						if (edgeText.getIdentity().equals(id)) {
							found = true;
							edgeText.synchronise(child);
						}
					}
				}
				if (!found) {
					String identity = child.getString("identity");
					String text = child.getString("text");
					String attachedTo = child.getString("attachedTo");
					int relx = child.getInteger("x");
					int rely = child.getInteger("y");
					boolean editable = child.getBoolean("editable");
					boolean underline = child.getBoolean("underline");
					int truncate = child.getInteger("truncate");
					String font = child.getString("font");
					EdgeText edgeText = new EdgeText(this, handler, identity,
							attachedTo, relx, rely, text, editable, underline,
							truncate, font);
					labels.add(edgeText);
					edgeText.synchronise(child);
				}
			}
		}

		// Check that each of the edge labels is represented in the document

		Vector toRemove = new Vector();
		for (int i = 0; i < labels.size(); i++) {
			if (labels.elementAt(i) instanceof EdgeText) {
				EdgeText text = (EdgeText) labels.elementAt(i);
				String id = text.getIdentity();
				boolean found = false;
				for (int z = 0; z < edge.childrenSize(); z++) {
					Element child = edge.getChild(z);
					if (child.hasName(XMLBindings.label)
							&& child.getString("identity").equals(id))
						found = true;
				}
				if (!found)
					toRemove.add(text);
			}
		}

		// Delete edge text

		for (int i = 0; i < toRemove.size(); i++) {
			EdgeText text = (EdgeText) toRemove.elementAt(i);
			text.delete();
		}
	}

	/**
	 * Synchronise multiline labels.
	 * 
	 * @param edge
	 *            the edge
	 */
	public void synchroniseMultilineLabels(Element edge) {

		// Check that there is a multiline label for each of the model's edge
		// multiline labels

		for (int i = 0; i < edge.childrenSize(); i++) {
			Element child = edge.getChild(i);
			if (child.hasName(XMLBindings.multilinetext)) {
				String id = child.getString("identity");
				boolean found = false;
				for (int z = 0; z < labels.size(); z++) {
					if (labels.elementAt(z) instanceof MultilineEdgeText) {
						MultilineEdgeText edgeText = (MultilineEdgeText) labels
								.elementAt(z);
						if (edgeText.getIdentity().equals(id)) {
							found = true;
							edgeText.synchronise(child);
						}
					}
				}
				if (!found) {
					String identity = child.getString("id");
					String text = child.getString("text");
					String attachedTo = child.getString("attachedTo");
					int relx = child.getInteger("x");
					int rely = child.getInteger("y");
					boolean editable = child.getBoolean("editable");
					boolean underline = child.getBoolean("underline");
					int truncate = child.getInteger("truncate");
					String font = child.getString("font");
					MultilineEdgeText edgeText = new MultilineEdgeText(this,
							handler, identity, attachedTo, relx, rely, text,
							editable, underline, truncate, font);
					labels.add(edgeText);
					edgeText.synchronise(child);
				}
			}
		}

		// Check that each of the edge labels is represented in the document

		Vector toRemove = new Vector();
		for (int i = 0; i < labels.size(); i++) {
			if (labels.elementAt(i) instanceof MultilineEdgeText) {
				MultilineEdgeText text = (MultilineEdgeText) labels
						.elementAt(i);
				String id = text.getIdentity();
				boolean found = false;
				for (int z = 0; z < edge.childrenSize(); z++) {
					Element child = edge.getChild(z);
					if (child.hasName(XMLBindings.label)
							&& child.getString("identity").equals(id))
						found = true;
				}
				if (!found)
					toRemove.add(text);
			}
		}

		// Delete multiline edge text

		for (int i = 0; i < toRemove.size(); i++) {
			MultilineEdgeText text = (MultilineEdgeText) toRemove.elementAt(i);
			text.delete();
		}
	}

	/**
	 * Synchronise waypoints.
	 * 
	 * @param edge
	 *            the edge
	 */
	public void synchroniseWaypoints(Element edge) {

		// The most efficent way of synchronising waypoints is
		// to remove them all and reconstruct them from the document

		waypoints = new ArrayList();
		for (int i = 0; i < edge.childrenSize(); i++) {
			Element child = edge.getChild(i);
			if (child.getName().equals(XMLBindings.waypoint)) {

				// add in a dummy

				waypoints.add("dummy");
			}
		}
		for (int i = 0; i < edge.childrenSize(); i++) {
			Element child = edge.getChild(i);
			if (child.getName().equals(XMLBindings.waypoint)) {
				String id = child.getString("identity");
				int index = child.getInteger("index");
				int x = child.getInteger("x");
				int y = child.getInteger("y");
				Waypoint waypoint = new Waypoint(this, handler, id, new Point(
						x, y));
				waypoints.set(index, waypoint);
			}
		}
	}

}