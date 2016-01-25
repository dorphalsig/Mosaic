package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import xos.Message;
import xos.Value;

public class Edge {
  // An empty vector to be used where a list intersections is required but there is no need in that case
  private static final Vector<Point> NO_INTERSECTIONS_PLACEHOLDER = new Vector<Point>();

  public static int                  ARROW_ANGLE     = 35;
  public static int                  ARROW_HEAD      = 10;
  public static int                  LINE_WIDTH      = 1;
  public static int                  WAYPOINT_ALIGN  = 20;
  
  enum HeadStyle {NO_ARROW(0), ARROW(1), BLACK_DIAMOND(2), WHITE_DIAMOND(3), BLACK_ARROW(4), WHITE_ARROW(5); 
	  int id; 
	  private HeadStyle(int id) {this.id = id;}
	  private int getID() {return id;}
	  private static HeadStyle getHeadStyle(int id) {
		  for(HeadStyle headStyle : HeadStyle.values()) if(headStyle.id == id) return headStyle;
		  throw new IllegalArgumentException("HeadStyle id " + id + " not in use!");
	  }
  }

  Vector<Waypoint>                   waypoints       = new Vector<Waypoint>();
  Vector<Label>                      labels          = new Vector<Label>();
  boolean                            hidden          = false;
  String                             id;
  Node                               sourceNode;
  Port                               sourcePort;
  Node                               targetNode;
  Port                               targetPort;
  int                                refx;
  int                                refy;
  HeadStyle                          sourceHead;
  HeadStyle                          targetHead;
  int                                lineStyle;
  int                                red;
  int                                green;
  int                                blue;
	  
  /*PACKAGE ACCESS*/ static Point circleIntersect(int centerx, int centery, double radius, int x, int y) {
    double dx = x - centerx;
    double dy = y - centery;
    double lineLength = Math.sqrt((dx * dx) + (dy * dy));
    dx = dx / lineLength;
    dy = dy / lineLength;
    Point p = new Point((int) (centerx + (dx * radius)), (int) (centery + (dy * radius)));
    return p;
  }
  
  /*NEW PRIVATE*/private static boolean isOnLine(int x, int y, int x1, int y1, int x2, int y2) {
    boolean inX = x1 < x2 ? (x >= x1 && x <= x2) : (x >= x2 && x <= x1);
    boolean inY = y1 < y2 ? (y >= y1 && y <= y2) : (y >= y2 && y <= y1);
    return inX && inY;
  }

  /*NEW PRIVATE*/private static double pointToLineDistance(Waypoint A, Waypoint B, int x, int y) {
    double normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));
    return Math.abs((x - A.x) * (B.y - A.y) - (y - A.y) * (B.x - A.x)) / normalLength;
  }

  /*PACKAGE ACCESS*/ Edge(String id, Node sourceNode, Port sourcePort, int sourceX, int sourceY, Node targetNode, Port targetPort, int targetX, int targetY, int refx, int refy, int sourceHead, int targetHead, int lineStyle, int red, int green, int blue) {
    super();
    this.id = id;
    this.sourceNode = sourceNode;
    this.sourcePort = sourcePort;
    this.targetNode = targetNode;
    this.targetPort = targetPort;
    this.refx = refx;
    this.refy = refy;
    this.sourceHead = HeadStyle.getHeadStyle(sourceHead);
    this.targetHead = HeadStyle.getHeadStyle(targetHead);
    this.lineStyle = lineStyle;
    this.red = red;
    this.green = green;
    this.blue = blue;
    sourcePort.addSource(this);
    targetPort.addTarget(this);
    waypoints.add(new Waypoint("start", this, sourceX, sourceY));
    waypoints.add(new Waypoint("end", this, targetX, targetY));
  }

  /*PACKAGE ACCESS*/ void addLabel(String id, String text, String pos, int x, int y, boolean editable, boolean underline, boolean condense, int red, int green, int blue, boolean border,  int borderRed, int borderGreen, int borderBlue, String font, int arrow) {
    labels.add(new Label(this, id, text, pos, x, y, editable, underline, condense, red, green, blue, border, borderRed, borderGreen, borderBlue, font, arrow));
  }

  /** aligns the start and end waypoint to 90° lines if possible 
  /*PACKAGE ACCESS*/ void align() {
    // Called when there is some jiggling to be done.
    if (waypoints.size() == 2) {
      alignStart(end());
      alignEnd(start());
    } else {
      alignStart(waypoints.elementAt(1));
      alignEnd(waypoints.elementAt(waypoints.size() - 2));
    }
  }

  /**
   * When a (visible) waypoint is moved, the start and end waypoints
   * will be moved as possible to have straight (90°) lines.
   */
  /*PACKAGE ACCESS*/ void movedBy(Waypoint justMoved) {
    if (justMoved != start() && justMoved != end()) {
      int index = waypoints.indexOf(justMoved);
      Waypoint w1 = waypoints.elementAt(index - 1);
      Waypoint w2 = waypoints.elementAt(index + 1);
      if (w1 == start()) alignStart(justMoved);
      if (w2 == end()) alignEnd(justMoved);
    }
  }
  
  /**
   * Aligns the last waypoint (hidden under the source node) 
   * so that it connects to the next waypoint in a horizontal 
   * or vertical way if that is possible. The other coordinate 
   * is not changed. No changes at all are made otherwise 
   * (i.e. the next waypoint's x/y cordinates are both outside the box's). 
   */
  private void alignEnd(Waypoint w) {
    int portx = targetNode.getX() + targetPort.getX();
    int porty = targetNode.getY() + targetPort.getY();
    int width = targetPort.getWidth();
    int height = targetPort.getHeight();
    if (w.getX() >= portx && w.getX() <= portx + width) end().setX(w.getX());
    if (w.getY() >= porty && w.getY() <= porty + height) end().setY(w.getY());
  }

  /** Same as <code>alignEnd(Waypoint w)</code>, 
   * but for the last waypoint (hidden under the target node)*/
  private void alignStart(Waypoint w) {
    int portx = sourceNode.getX() + sourcePort.getX();
    int porty = sourceNode.getY() + sourcePort.getY();
    int width = sourcePort.getWidth();
    int height = sourcePort.getHeight();
    if (w.getX() >= portx && w.getX() <= portx + width) start().setX(w.getX());
    if (w.getY() >= porty && w.getY() <= porty + height) start().setY(w.getY());
  }

  /**
   * Removes the justMoved waypoint if it is too close to another one.
   */
  /*PACKAGE ACCESS*/ void checkWaypointsForRedundancy(Waypoint justMoved) {
    if (getWaypoints().size() > 2 && justMoved != start() && justMoved != end()) {
      int index = waypoints.indexOf(justMoved);
      Waypoint w1 = waypoints.elementAt(index - 1);
      Waypoint w2 = waypoints.elementAt(index + 1);
      if (pointToLineDistance(w1, w2, justMoved.x, justMoved.y) < 5) {
        Message message = DiagramClient.theClient().getHandler().newMessage("deleteWaypoint", 1);
        message.args[0] = new Value(justMoved.getId());
        DiagramClient.theClient().getHandler().raiseEvent(message);
      }
    }
  }

  private Vector<Point> getIntersection(Waypoint wp0, Waypoint wp1, Vector<Point> intersections) {
    Vector<Point> i = new Vector<Point>();
    for (Point p : intersections) {
      double d = pointToLineDistance(wp0, wp1, p.x, p.y);
      if (d < 1) {
        i.add(p);
      }
    }
    return i;
  }

  /*PACKAGE ACCESS*/ Label getLabel(String id) {
    for (Label label : labels)
      if (label.getId().equals(id)) return label;
    return null;
  }

  /*PACKAGE ACCESS*/ Vector<Label> getLabels() {
    return labels;
  }

  /*PACKAGE ACCESS*/ Waypoint getWaypoint(String id) {
    for (Waypoint waypoint : waypoints)
      if (waypoint.getId().equals(id)) return waypoint;
    return null;
  }

  /*PACKAGE ACCESS*/ Vector<Waypoint> getWaypoints() {
    return waypoints;
  }

//  /*PACKAGE ACCESS*/ boolean isHidden() {
//    return hidden;
//  }

  /*NEW PRIVATE*/private boolean isSelfEdge() {
    return sourceNode == targetNode;
  }

  /*PACKAGE ACCESS*/ int maxX() {
    int maxX = 0;
    for (Waypoint w : waypoints)
      maxX = Math.max(maxX, w.getX());
    for (Label label : labels)
      maxX = Math.max(maxX, label.maxX());
    return maxX;
  }

  /*PACKAGE ACCESS*/ int maxY() {
    int maxY = 0;
    for (Waypoint w : waypoints)
      maxY = Math.max(maxY, w.getY());
    for (Label label : labels)
      maxY = Math.max(maxY, label.maxY());
    return maxY;
  }

  /*PACKAGE ACCESS*/ void move(String id, int x, int y) {
    for (Label label : labels) {
      label.move(id, x, y);
    }
    for (Waypoint waypoint : waypoints) {
      waypoint.move(id, x, y);
    }
  }

  /*PACKAGE ACCESS*/ void moveSourceBy(int dx, int dy) {
    waypoints.elementAt(0).moveBy(dx, dy);
  }

  /*PACKAGE ACCESS*/ void moveTargetBy(int dx, int dy) {
    waypoints.elementAt(waypoints.size() - 1).moveBy(dx, dy);
  }

  /*PACKAGE ACCESS*/ boolean newWaypoint(int x, int y) {
    for (int i = 0; i < waypoints.size() - 1; i++) {
      Waypoint w1 = waypoints.elementAt(i);
      Waypoint w2 = waypoints.elementAt(i + 1);
      boolean betweenX = x >= Math.min(w1.x, w2.x) && x <= Math.max(w1.x, w2.x);
      boolean betweenY = y >= Math.min(w1.y, w2.y) && y <= Math.max(w1.y, w2.y);
      if (betweenX && betweenY && pointToLineDistance(w1, w2, x, y) < 5) {
        newWaypointEvent(i + 1, x, y);
        return true;
      }
    }
    return false;
  }

  /*PACKAGE ACCESS*/ Waypoint newWaypoint(String parentId, String id, int index, int x, int y) {
    if (parentId.equals(getId())) {
      Waypoint w = new Waypoint(id, this, x, y);
      waypoints.insertElementAt(w, index);
      return w;
    }
    return null;
  }

  private void newWaypointEvent(int index, int x, int y) {
    Message message = DiagramClient.theClient().getHandler().newMessage("newWaypoint", 4);
    message.args[0] = new Value(id);
    message.args[1] = new Value(index);
    message.args[2] = new Value(x);
    message.args[3] = new Value(y);
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }
  
  /*PACKAGE ACCESS*/ void reconnectSource(Node node, Port port) {
    if (this.sourcePort != null) sourcePort.getSources().remove(this);
    this.sourceNode = node; //setSourceNode(node);
    this.sourcePort = port; //setSourcePort(port);
    port.getSources().add(this);
    start().setX(node.getX() + port.getX() + (port.getWidth() / 2));
    start().setY(node.getY() + port.getY() + (port.getHeight() / 2));
  }

  /*PACKAGE ACCESS*/ void reconnectTarget(Node node, Port port) {
    if (this.targetPort != null) targetPort.getTargets().remove(this);
    this.targetNode = node;//setTargetNode(node);
    this.targetPort = port;//setTargetPort(port);
    port.getTargets().add(this);
    end().setX(node.getX() + port.getX() + (port.getWidth() / 2));
    end().setY(node.getY() + port.getY() + (port.getHeight() / 2));
  }

  /*PACKAGE ACCESS*/ Waypoint start() {
    return waypoints.get(0);
  }
  
  private Waypoint second() {
    return waypoints.elementAt(1);
  }
  
  private Waypoint penultimate() {
    return waypoints.elementAt(waypoints.size() - 2);
  }
  
  /*PACKAGE ACCESS*/ Waypoint end() {
    return waypoints.get(waypoints.size() - 1);
  }

  /*PACKAGE ACCESS*/ void setBlue(int blue) {
    this.blue = blue;
  }

  /*PACKAGE ACCESS*/ void setGreen(int green) {
    this.green = green;
  }

  /*PACKAGE ACCESS*/ void setLineStyle(int lineStyle) {
    this.lineStyle = lineStyle;
  }

  /*PACKAGE ACCESS*/ void setRed(int red) {
    this.red = red;
  }

  /*PACKAGE ACCESS*/ void setRefx(int refx) {
    this.refx = refx;
  }

  /*PACKAGE ACCESS*/ void setRefy(int refy) {
    this.refy = refy;
  }

  /*PACKAGE ACCESS*/ void setText(String id, String text) {
    for (Label label : labels)
      label.setText(id, text);
  }

  /*PACKAGE ACCESS*/ boolean sharesSegment(Edge edge) {
    for (int i = 0; i < getWaypoints().size() - 1; i++) {
      Waypoint w11 = getWaypoints().elementAt(i);
      Waypoint w12 = getWaypoints().elementAt(i + 1);
      for (int j = 0; j < edge.getWaypoints().size() - 1; j++) {
        Waypoint w21 = edge.getWaypoints().elementAt(j);
        Waypoint w22 = edge.getWaypoints().elementAt(j + 1);
        if (w11.colocated(w21) && w12.colocated(w22)) return true;
      }
    }
    return false;
  }

  public void straighten() {
    // To straighten edges:
    // (1) Remove all way-points that are not needed.
    // (2) Straighten segments by moving way-points;
    for (Waypoint w : waypoints) {
      if (w != start() && w != end()) {
        checkWaypointsForRedundancy(w);
      }
    }
    straightenFrom(start(), 1, 1);
    straightenFrom(end(), waypoints.size() - 2, -1);
  }

  /*NEW PRIVATE*/private void straightenFrom(Waypoint w1, int start, int inc) {
    for (int i = start; i >= 0 && i < waypoints.size(); i = i + inc) {
      Waypoint w2 = waypoints.elementAt(i);
      int dx = Math.abs(w1.getX() - w2.getX());
      int dy = Math.abs(w1.getY() - w2.getY());
      if (dx < WAYPOINT_ALIGN) {
        w2.move(w1.getX(), w2.getY());
      }
      if (dy < WAYPOINT_ALIGN) {
        w2.move(w2.getX(), w1.getY());
      }
      w1 = w2;
    }
  }

  public String toString() {
    return "Edge(" + waypoints + "," + labels + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Edge ");
    out.print("id='" + getId() + "' ");
    out.print("refx='" + refx + "' ");
    out.print("refy='" + refy+ "' ");
    out.print("source='" + sourceNode.getId() + "' ");
    out.print("target='" + targetNode.getId() + "' ");
    out.print("sourcePort='" + sourcePort.getId() + "' ");
    out.print("targetPort='" + targetPort.getId() + "' ");
    out.print("sourceHead='" + sourceHead.getID() + "' ");
    out.print("targetHead='" + targetHead.getID() + "' ");
    out.print("lineStyle='" + lineStyle + "' ");
    out.print("hidden='" + hidden + "' ");
    out.print("red='" + red + "' ");
    out.print("green='" + green + "' ");
    out.print("blue='" + blue + "'>");
    for (Waypoint waypoint : waypoints)
      if (waypoint != start() && waypoint != end()) waypoint.writeXML(out);
    for (Label label : labels)
      label.writeXML(out);
    out.print("</Edge>");
  }

  public void hide(String id) {
    if (getId().equals(id)) hidden = true;
  }

  public void show(String id) {
    if (getId().equals(id)) hidden = false;
  }

  public void doubleClick(GC gc, Diagram diagram, int x, int y) {
	for(Label label : labels) {
		label.doubleClick(gc, diagram, x, y);
	}
//	if(lineIsHit(x,y,2)) {
//		
//	}
	}
  
  /////// INTERCEPT //////
 
  /*PACKAGE ACCESS*/ Point targetIntercept() { // used by Label
    return intercept(targetNode);
  }
  
  /*PACKAGE ACCESS*/ Point sourceIntercept() { // used by Label
    return intercept(sourceNode);
  }

  /*PACKAGE ACCESS*/ Point intercept(Node node) {
    Point p = intercept(node, Position.TOP);
    p = p == null ? intercept(node, Position.LEFT) : p;
    p = p == null ? intercept(node, Position.RIGHT) : p;
    p = p == null ? intercept(node, Position.BOTTOM) : p;
    return p;
  }
  
  enum Position{TOP, BOTTOM, LEFT, RIGHT}
  
  private Point intercept(Node node, Position position) {
	  Waypoint firstOrLast = node.contains(start()) ? start() : end();
	  Waypoint secondOrPenultimate = node.contains(start()) ? second() : penultimate();
	  return intercept(node, firstOrLast, secondOrPenultimate, position);
  }

  private Point intercept(Node node, Waypoint w1, Waypoint w2, Position position) {
	  	int x2 = node.getX() + (position == Position.RIGHT  ? node.getWidth()  : 0);
	  	int y2 = node.getY() + (position == Position.BOTTOM ? node.getHeight() : 0);
	  	int x3 = node.getX() + (position != Position.LEFT   ? node.getWidth()  : 0);
	  	int y3 = node.getY() + (position != Position.TOP    ? node.getHeight() : 0);
	  	Point p = intercept(w1.x, w1.y, w2.x, w2.y, x2, y2, x3, y3);
	  	if(position == Position.TOP    && (w2.y >= y2 || p.x <= x2 || p.x >= x3)) return null;
	  	if(position == Position.BOTTOM && (w2.y <= y2 || p.x <= x2 || p.x >= x3)) return null;
	  	if(position == Position.LEFT   && (w2.x > x2  || p.y <= y2 || p.y >= y3)) return null;
	  	if(position == Position.RIGHT  && (w2.x < x2  || p.y <= y2 || p.y >= y3)) return null;
	  	return p;
  }
  
  /*
   * intercept checks where the two lines (infinitely extended) would meet. return -1/-1 if the lines are parallel
   * 
   * intersect checks where the two lines do actually meet. returns -1/-1 if the lines don't meet
   */
  
  /*NEW PRIVATE*/private static Point intercept(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
	  // 1 is first (or last) Waypoint
	  // 2 is next Waypoint
	  // 3 and 4 are the relevant corners of the node
	    float x12 = x1 - x2;
	    float x34 = x3 - x4; // is negative width of the line between the corners of the node, zero for TOP or BOTTOM
	    float y12 = y1 - y2;
	    float y34 = y3 - y4; // is negative height of the line between the corners of the node, zero for LEFT or RIGHT
	    float c = x12 * y34 - y12 * x34;
	    if (Math.abs(c) < 0.01)
	      // No intersection
	      return new Point(-1, -1);
	    else {
	      // Intersection
	      float a = x1 * y2 - y1 * x2;
	      float b = x3 * y4 - y3 * x4;
	      float x = (a * x34 - b * x12) / c;
	      float y = (a * y34 - b * y12) / c;
	      return new Point((int) x, (int) y);
	    }
	  }

	  /*PACKAGE ACCESS*/ static Point intersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
	    float x12 = x1 - x2;
	    float x34 = x3 - x4;
	    float y12 = y1 - y2;
	    float y34 = y3 - y4;
	    float c = x12 * y34 - y12 * x34;
	    if (Math.abs(c) < 0.01)
	      // No intersection
	      return new Point(-1, -1);
	    else {
	      // Intersection
	      float a = x1 * y2 - y1 * x2;
	      float b = x3 * y4 - y3 * x4;
	      float x = (a * x34 - b * x12) / c;
	      float y = (a * y34 - b * y12) / c;
	      if (isOnLine((int) x, (int) y, x1, y1, x2, y2) && isOnLine((int) x, (int) y, x3, y3, x4, y4))
	        return new Point((int) x, (int) y);
	      else return new Point(-1, -1);
	    }
	  }
  
  ///////// NEAR /////////
  
  public boolean near(Node node, int x, int y) {
    Point p = intercept(node, Position.TOP);
    p = p == null ? intercept(node, Position.BOTTOM) : p;
    p = p == null ? intercept(node, Position.LEFT) : p;
    p = p == null ? intercept(node, Position.RIGHT) : p;
    if (p != null)
      return distance(p.x, p.y, x, y) < 10;
    else return false;
  }
  
  private double distance(int x1, int y1, int x2, int y2) {
    int dx = x1 - x2;
    int dy = y1 - y2;
    return Math.sqrt((dx * dx) + (dy * dy));
  }

  public boolean nearEnd(int x, int y) {
    return near(targetNode, x, y);
  }

  public boolean nearStart(int x, int y) {
    return near(sourceNode, x, y);
  }

  ///////// DRAW ////////

  private void drawArrow(GC gc, int tipx, int tipy, int tailx, int taily, boolean filled, Color fill, Color line) {
    double phi = Math.toRadians(ARROW_ANGLE);
    double dy = tipy - taily;
    double dx = tipx - tailx;
    double theta = Math.atan2(dy, dx);
    double x, y, rho = theta + phi;
    Color c = gc.getForeground();
    gc.setForeground(line);
    if (!filled) {
      for (int j = 0; j < 2; j++) {
        x = tipx - ARROW_HEAD * Math.cos(rho);
        y = tipy - ARROW_HEAD * Math.sin(rho);
        gc.drawLine(tipx, tipy, (int) x, (int) y);
        rho = theta - phi;
      }
    } else {
      gc.setLineCap(SWT.CAP_ROUND);
      int width = gc.getLineWidth();
      gc.setLineWidth(LINE_WIDTH + 2);
      int[] points = new int[6];
      points[0] = tipx;
      points[1] = tipy;
      for (int j = 0; j < 2; j++) {
        points[(j * 2) + 2] = (int) (tipx - ARROW_HEAD * Math.cos(rho));
        points[(j * 2) + 3] = (int) (tipy - ARROW_HEAD * Math.sin(rho));
        gc.drawLine(tipx, tipy, points[(j * 2) + 2], points[(j * 2) + 3]);
        rho = theta - phi;
      }
      gc.drawLine(points[2], points[3], points[4], points[5]);
      gc.setForeground(c);
      c = gc.getBackground();
      gc.setBackground(fill);
      gc.fillPolygon(points);
      gc.setBackground(c);
      gc.setLineWidth(width);
    }
  }

  private void drawSourceDecoration(GC gc, Color color, int x, int y, int x2, int y2) {
    switch (sourceHead) {
    case NO_ARROW:
      break;
    case ARROW:
      drawArrow(gc, x, y, x2, y2, false, null, color);
      break;
    case WHITE_ARROW:
      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE, color);
      break;
    default:
      System.err.println("unknown type of source decoration: " + sourceHead);
    }
  }

  private void drawTargetDecoration(GC gc, Color color, int x, int y, int x2, int y2) {
    switch (targetHead) {
    case NO_ARROW:
      break;
    case ARROW:
      drawArrow(gc, x, y, x2, y2, false, null, color);
      break;
    case WHITE_ARROW:
      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE, color);
      break;
    default:
      System.err.println("unknown type of target decoration: " + targetHead);
    }
  }  
  
  public void paint(GC gc, Color color, boolean showWaypoints, Vector<Point> intersections) {
    if (!hidden) {
      int x = waypoints.elementAt(0).getX();
      int y = waypoints.elementAt(0).getY();
      int width = gc.getLineWidth();
      gc.setLineWidth(LINE_WIDTH);
      Color c = gc.getBackground();
      gc.setBackground(color);
      for (int i = 1; i < waypoints.size(); i++) {
        final Waypoint wp0 = waypoints.elementAt(i - 1);
        final Waypoint wp1 = waypoints.elementAt(i);
        Vector<Point> p = getIntersection(wp0, wp1, intersections);
        Collections.sort(p, new Comparator<Point>() {
          public int compare(Point o1, Point o2) {
            Point p1 = (Point) o1;
            Point p2 = (Point) o2;
            boolean cmpx = wp0.x <= wp1.x ? p1.x <= p2.x : p2.x <= p1.x;
            boolean cmpy = wp0.y <= wp1.y ? p1.y <= p2.y : p2.y <= p1.y;
            return cmpx && cmpy ? -1 : 1;
          }
        });
        paintLine(gc, x, y, wp1.getX(), wp1.getY(), color, p);
        if (showWaypoints && i < waypoints.size() - 1) gc.fillOval(wp1.getX() - 3, wp1.getY() - 3, 6, 6);
        x = wp1.getX();
        y = wp1.getY();
      }
      gc.setBackground(c);
      for (Label label : labels)
        label.paint(gc);
      paintDecorations(gc, color);
      gc.setLineWidth(width);
    }
  }

  public void paintAligned(GC gc) {
    if (!hidden) {
      int x = waypoints.elementAt(0).getX();
      int y = waypoints.elementAt(0).getY();
      int width = gc.getLineWidth();
      gc.setLineWidth(LINE_WIDTH + 1);
      for (int i = 1; i < waypoints.size(); i++) {
        Waypoint wp = waypoints.elementAt(i);
        paintLine(gc, x, y, wp.getX(), wp.getY(), Diagram.RED, NO_INTERSECTIONS_PLACEHOLDER);
        if (i < waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
        x = wp.getX();
        y = wp.getY();
      }
      gc.setLineWidth(width);
    }
  }

  private void paintDecorations(GC gc, Color color) {
    if (isSelfEdge())
      paintHomogeneousEdgeDecorations(gc, color);
    else paintHeterogeneousEdgeDecorations(gc, color);
  }

  /*NEW PRIVATE*/private void paintHeterogeneousEdgeDecorations(GC gc, Color color) {
    Point topIntercept = intercept(targetNode, Position.TOP);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawTargetDecoration(gc, color, topIntercept.x, topIntercept.y, penultimate().x, penultimate().y);
    Point bottomIntercept = intercept(sourceNode, Position.BOTTOM);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawSourceDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, second().x, second().y);
    topIntercept = intercept(sourceNode, Position.TOP);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawSourceDecoration(gc, color, topIntercept.x, topIntercept.y, second().x, second().y);
    bottomIntercept = intercept(targetNode, Position.BOTTOM);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawTargetDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, penultimate().x, penultimate().y);
    Point leftIntercept = intercept(sourceNode, Position.LEFT);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawSourceDecoration(gc, color, leftIntercept.x, leftIntercept.y, second().x, second().y);
    Point rightIntercept = intercept(targetNode, Position.RIGHT);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawTargetDecoration(gc, color, rightIntercept.x, rightIntercept.y, penultimate().x, penultimate().y);
    leftIntercept = intercept(targetNode, Position.LEFT);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawTargetDecoration(gc, color, leftIntercept.x, leftIntercept.y, penultimate().x, penultimate().y);
    rightIntercept = intercept(sourceNode, Position.RIGHT);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawSourceDecoration(gc, color, rightIntercept.x, rightIntercept.y, second().x, second().y);
  }

  /*NEW PRIVATE*/private void paintHomogeneousEdgeDecorations(GC gc, Color color) {
    // Ensure that the correct waypoint is used when calculating the intercepts...
    Point topIntercept = targetHead == HeadStyle.NO_ARROW ? null : intercept(targetNode, end(), penultimate(), Position.TOP);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawTargetDecoration(gc, color, topIntercept.x, topIntercept.y, penultimate().x, penultimate().y);
    Point bottomIntercept = sourceHead == HeadStyle.NO_ARROW ? null : intercept(sourceNode, start(), second(), Position.BOTTOM);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawSourceDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, second().x, second().y);
    topIntercept = sourceHead == HeadStyle.NO_ARROW ? null : intercept(sourceNode, start(), second(), Position.TOP);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawSourceDecoration(gc, color, topIntercept.x, topIntercept.y, second().x, second().y);
    bottomIntercept = targetHead == HeadStyle.NO_ARROW ? null : intercept(targetNode, end(), penultimate(), Position.BOTTOM);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawTargetDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, penultimate().x, penultimate().y);
    Point leftIntercept = sourceHead == HeadStyle.NO_ARROW ? null : intercept(sourceNode, start(), second(), Position.LEFT);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawSourceDecoration(gc, color, leftIntercept.x, leftIntercept.y, second().x, second().y);
    Point rightIntercept = targetHead == HeadStyle.NO_ARROW ? null : intercept(targetNode, end(), penultimate(), Position.RIGHT);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawTargetDecoration(gc, color, rightIntercept.x, rightIntercept.y, penultimate().x, penultimate().y);
    leftIntercept = targetHead == HeadStyle.NO_ARROW ? null : intercept(targetNode, end(), penultimate(), Position.LEFT);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawTargetDecoration(gc, color, leftIntercept.x, leftIntercept.y, penultimate().x, penultimate().y);
    rightIntercept = sourceHead == HeadStyle.NO_ARROW ? null : intercept(sourceNode, start(), second(), Position.RIGHT);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawSourceDecoration(gc, color, rightIntercept.x, rightIntercept.y, second().x, second().y);
  }

  public void paintHover(GC gc, int x, int y) {
    for (Label label : labels)
      label.paintHover(gc, x, y);
  }

  public void paintLine(GC gc, int x1, int y1, int x2, int y2, Color lineColor, Vector<Point> intersect) {
    // Paint the line in the line style.
    int style = gc.getLineStyle();
    Color c = gc.getForeground();
    gc.setForeground(lineColor);
    switch (lineStyle) {
    case Line.DASH_LINE:
      gc.setLineStyle(SWT.LINE_DASH);
      break;
    case Line.DOTTED_LINE:
      gc.setLineStyle(SWT.LINE_DOT);
      break;
    case Line.DASH_DOTTED_LINE:
      gc.setLineStyle(SWT.LINE_DASHDOT);
      break;
    case Line.DASH_DOT_DOT_LINE:
      gc.setLineStyle(SWT.LINE_DASHDOTDOT);
      break;
    case Line.SOLID_LINE:
    default:
      gc.setLineStyle(SWT.LINE_SOLID);
    }
    for (Point p : intersect) {
      Point p1 = circleIntersect(p.x, p.y, 3.0, x1, y1);
      Point p2 = circleIntersect(p.x, p.y, 3.0, x2, y2);
      gc.drawLine(x1, y1, p1.x, p1.y);
      x1 = p2.x;
      y1 = p2.y;
    }
    gc.drawLine(x1, y1, x2, y2);
    gc.setLineStyle(style);
    gc.setForeground(c);
  }

  public void paintMovingSourceOrTarget(GC gc, int startX, int startY, int endX, int endY) {
    int x = startX;
    int y = startY;
    int width = gc.getLineWidth();
    gc.setLineWidth(LINE_WIDTH);
    Color c = gc.getBackground();
    gc.setBackground(Diagram.BLACK);
    for (int i = 1; i < waypoints.size() - 1; i++) {
      Waypoint wp = waypoints.elementAt(i);
      paintLine(gc, x, y, wp.getX(), wp.getY(), Diagram.BLACK, NO_INTERSECTIONS_PLACEHOLDER);
      if (i < waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
      x = wp.getX();
      y = wp.getY();
    }
    paintLine(gc, x, y, endX, endY, Diagram.BLACK, NO_INTERSECTIONS_PLACEHOLDER);
    gc.setBackground(c);
    for (Label label : labels)
      label.paint(gc);
    drawSourceDecoration(gc, Diagram.BLACK, startX, startY, waypoints.elementAt(1).getX(), waypoints.elementAt(1).getY());
    drawTargetDecoration(gc, Diagram.BLACK, endX, endY, waypoints.elementAt(waypoints.size() - 2).getX(), waypoints.elementAt(waypoints.size() - 2).getY());
    gc.setLineWidth(width);
  }

  public void paintOrthogonal(GC gc, Waypoint waypoint) { // Zielscheibe
    if (waypoint != start() && waypoint != end()) {
      int index = waypoints.indexOf(waypoint);
      int length = 30;
      Waypoint pre = waypoints.elementAt(index - 1);
      Waypoint post = waypoints.elementAt(index + 1);
      Color c = gc.getForeground();
      gc.setForeground(Diagram.RED);
      if (pre.getX() == waypoint.getX() || post.getX() == waypoint.getX()) {
        gc.drawOval(waypoint.getX() - length / 2, waypoint.getY() - length / 2, length, length);
        gc.drawLine(waypoint.getX(), waypoint.getY() - length, waypoint.getX(), waypoint.getY() + length);
      }
      if (pre.getY() == waypoint.getY() || post.getY() == waypoint.getY()) {
        gc.drawOval(waypoint.getX() - length / 2, waypoint.getY() - length / 2, length, length);
        gc.drawLine(waypoint.getX() - length, waypoint.getY(), waypoint.getX() + length, waypoint.getY());
      }
      gc.setForeground(c);
    }
  }

  public void paintSourceMoving(GC gc, int x, int y) {
    paintMovingSourceOrTarget(gc, x, y, end().getX(), end().getY());
  }

  public void paintTargetMoving(GC gc, int x, int y) {
    paintMovingSourceOrTarget(gc, start().getX(), start().getY(), x, y);
  }
  
  //////// GETTER ///////
  
//  /*NEW PRIVATE*/private int getRed()   { return red; }
//  /*NEW PRIVATE*/private int getBlue()  { return blue; }
//  /*NEW PRIVATE*/private int getGreen() { return green; }
  public String getId() { return id; }
  
//  /*NEW PRIVATE*/private int getRefx() { return refx; }
//  /*NEW PRIVATE*/private int getRefy() {  return refy; }
  
//  /*NEW PRIVATE*/private int getSourceHead() { return sourceHead; }
  public Node getSourceNode() { return sourceNode; }
  public Port getSourcePort() { return sourcePort; }
//  /*NEW PRIVATE*/private int getTargetHead() { return targetHead; }
  public Node getTargetNode() { return targetNode; }
  public Port getTargetPort() { return targetPort; }

///*PACKAGE ACCESS*/ int getLineStyle() {
//  return lineStyle;
//}
}
