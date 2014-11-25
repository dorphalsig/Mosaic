package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import xos.Message;
import xos.Value;

public class Edge {

  public static int ARROW_ANGLE   = 35;
  public static int ARROW_HEAD    = 10;
  public static int LINE_WIDTH    = 1;

  static final int  NO_ARROW      = 0;
  static final int  ARROW         = 1;
  static final int  BLACK_DIAMOND = 2;
  static final int  WHITE_DIAMOND = 3;
  static final int  BLACK_ARROW   = 4;
  static final int  WHITE_ARROW   = 5;

  Vector<Waypoint>  waypoints     = new Vector<Waypoint>();
  Vector<Label>     labels        = new Vector<Label>();

  String            id;
  Node              sourceNode;
  Port              sourcePort;
  Node              targetNode;
  Port              targetPort;
  int               refx;
  int               refy;
  int               sourceHead;
  int               targetHead;
  int               lineStyle;
  int               red;
  int               green;
  int               blue;

  public Edge(String id, Node sourceNode, Port sourcePort, int sourceX, int sourceY, Node targetNode, Port targetPort, int targetX, int targetY, int refx, int refy, int sourceHead, int targetHead, int lineStyle, int red, int green, int blue) {
    super();
    this.id = id;
    this.sourceNode = sourceNode;
    this.sourcePort = sourcePort;
    this.targetNode = targetNode;
    this.targetPort = targetPort;
    this.refx = refx;
    this.refy = refy;
    this.sourceHead = sourceHead;
    this.targetHead = targetHead;
    this.lineStyle = lineStyle;
    this.red = red;
    this.green = green;
    this.blue = blue;
    sourcePort.addSource(this);
    targetPort.addTarget(this);
    waypoints.add(new Waypoint("start", this, sourceX, sourceY));
    waypoints.add(new Waypoint("end", this, targetX, targetY));
  }

  public void addLabel(String id, String text, String pos, int x, int y, boolean editable, boolean underline, boolean condense, int red, int green, int blue, String font) {
    labels.add(new Label(this, id, text, pos, x, y, editable, underline, condense, red, green, blue, font));
  }

  public void align() {
    // Called when there is some jiggling to be done.
    if (waypoints.size() == 2) {
      alignStart(end());
      alignEnd(start());
    } else {
      alignStart(waypoints.elementAt(1));
      alignEnd(waypoints.elementAt(waypoints.size() - 2));
    }
  }

  private void alignEnd(Waypoint w) {
    int portx = targetNode.getX() + targetPort.getX();
    int porty = targetNode.getY() + targetPort.getY();
    int width = targetPort.getWidth();
    int height = targetPort.getHeight();
    if (w.getX() >= portx && w.getX() <= portx + width) end().setX(w.getX());
    if (w.getY() >= porty && w.getY() <= porty + height) end().setY(w.getY());
  }

  private void alignStart(Waypoint w) {
    int portx = sourceNode.getX() + sourcePort.getX();
    int porty = sourceNode.getY() + sourcePort.getY();
    int width = sourcePort.getWidth();
    int height = sourcePort.getHeight();
    if (w.getX() >= portx && w.getX() <= portx + width) start().setX(w.getX());
    if (w.getY() >= porty && w.getY() <= porty + height) start().setY(w.getY());
  }

  public Point bottomIntercept(Node node) {
    int x0 = node.contains(start()) ? start().getX() : end().getX();
    int y0 = node.contains(start()) ? start().getY() : end().getY();
    int x1 = node.contains(start()) ? second().getX() : penultimate().getX();
    int y1 = node.contains(start()) ? second().getY() : penultimate().getY();
    int x2 = node.getX();
    int y2 = node.getY() + node.getHeight();
    int x3 = node.getX() + node.getWidth();
    int y3 = node.getY() + node.getHeight();
    Point p = intercept(x0, y0, x1, y1, x2, y2, x3, y3);
    if (y1 <= y2 || p.x <= x2 || p.x >= x3)
      return null;
    else return p;
  }

  public void checkWaypoints(Waypoint justMoved) {
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

  private double distance(int x1, int y1, int x2, int y2) {
    int dx = x1 - x2;
    int dy = y1 - y2;
    return Math.sqrt((dx * dx) + (dy * dy));
  }

  private void drawArrow(GC gc, int tipx, int tipy, int tailx, int taily, boolean filled, Color fill) {
    double phi = Math.toRadians(ARROW_ANGLE);
    double dy = tipy - taily;
    double dx = tipx - tailx;
    double theta = Math.atan2(dy, dx);
    double x, y, rho = theta + phi;
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
      gc.fillPolygon(points);
      gc.setLineWidth(width);
    }
  }

  private void drawSourceDecoration(GC gc, int x, int y, int x2, int y2) {
    switch (sourceHead) {
    case NO_ARROW:
      break;
    case ARROW:
      drawArrow(gc, x, y, x2, y2, false, null);
      break;
    case WHITE_ARROW:
      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE);
      break;
    default:
      System.err.println("unknown type of source decoration: " + sourceHead);
    }
  }

  private void drawTargetDecoration(GC gc, int x, int y, int x2, int y2) {
    switch (targetHead) {
    case NO_ARROW:
      break;
    case ARROW:
      drawArrow(gc, x, y, x2, y2, false, null);
      break;
    case WHITE_ARROW:
      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE);
      break;
    default:
      System.err.println("unknown type of target decoration: " + targetHead);
    }
  }

  public Waypoint end() {
    return waypoints.get(waypoints.size() - 1);
  }

  public int getBlue() {
    return blue;
  }

  public int getGreen() {
    return green;
  }

  public String getId() {
    return id;
  }

  public Label getLabel(String id) {
    for (Label label : labels)
      if (label.getId().equals(id)) return label;
    return null;
  }

  public Vector<Label> getLabels() {
    return labels;
  }

  public int getLineStyle() {
    return lineStyle;
  }

  public int getRed() {
    return red;
  }

  public int getRefx() {
    return refx;
  }

  public int getRefy() {
    return refy;
  }

  public int getSourceHead() {
    return sourceHead;
  }

  public Node getSourceNode() {
    return sourceNode;
  }

  public Port getSourcePort() {
    return sourcePort;
  }

  public int getTargetHead() {
    return targetHead;
  }

  public Node getTargetNode() {
    return targetNode;
  }

  public Port getTargetPort() {
    return targetPort;
  }

  public Waypoint getWaypoint(String id) {
    for (Waypoint waypoint : waypoints)
      if (waypoint.getId().equals(id)) return waypoint;
    return null;
  }

  public Vector<Waypoint> getWaypoints() {
    return waypoints;
  }

  public Point intercept(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
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

      return new Point((int) x, (int) y);
    }
  }

  public Point intercept(Node node) {
    Point p = topIntercept(node);
    p = p == null ? leftIntercept(node) : p;
    p = p == null ? rightIntercept(node) : p;
    p = p == null ? bottomIntercept(node) : p;
    return p;
  }

  public Point leftIntercept(Node node) {
    int x0 = node.contains(start()) ? start().getX() : end().getX();
    int y0 = node.contains(start()) ? start().getY() : end().getY();
    int x1 = node.contains(start()) ? second().getX() : penultimate().getX();
    int y1 = node.contains(start()) ? second().getY() : penultimate().getY();
    int x2 = node.getX();
    int y2 = node.getY();
    int x3 = node.getX();
    int y3 = node.getY() + node.getHeight();
    Point p = intercept(x0, y0, x1, y1, x2, y2, x3, y3);
    if (x1 > x2 || p.y <= y2 || p.y >= y3)
      return null;
    else return p;
  }

  public void move(String id, int x, int y) {
    for (Label label : labels) {
      label.move(id, x, y);
    }
    for (Waypoint waypoint : waypoints) {
      waypoint.move(id, x, y);
    }
  }

  public void movedBy(Waypoint justMoved) {
    if (justMoved != start() && justMoved != end()) {
      int index = waypoints.indexOf(justMoved);
      Waypoint w1 = waypoints.elementAt(index - 1);
      Waypoint w2 = waypoints.elementAt(index + 1);
      if (w1 == start()) alignStart(justMoved);
      if (w2 == end()) alignEnd(justMoved);
    }
  }

  public void moveSourceBy(int dx, int dy) {
    waypoints.elementAt(0).moveBy(dx, dy);
  }

  public void moveTargetBy(int dx, int dy) {
    waypoints.elementAt(waypoints.size() - 1).moveBy(dx, dy);
  }

  public boolean near(Node node, int x, int y) {
    Point p = topIntercept(node);
    p = p == null ? leftIntercept(node) : p;
    p = p == null ? rightIntercept(node) : p;
    p = p == null ? bottomIntercept(node) : p;
    if (p != null)
      return distance(p.x, p.y, x, y) < 10;
    else return false;
  }

  public boolean nearEnd(int x, int y) {
    return near(targetNode, x, y);
  }

  public boolean nearStart(int x, int y) {
    return near(sourceNode, x, y);
  }

  public boolean newWaypoint(int x, int y) {
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

  public Waypoint newWaypoint(String parentId, String id, int index, int x, int y) {
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

  public void paint(GC gc) {
    int x = waypoints.elementAt(0).getX();
    int y = waypoints.elementAt(0).getY();
    int width = gc.getLineWidth();
    gc.setLineWidth(LINE_WIDTH);
    Color c = gc.getBackground();
    gc.setBackground(Diagram.BLACK);
    for (int i = 1; i < waypoints.size(); i++) {
      Waypoint wp = waypoints.elementAt(i);
      gc.drawLine(x, y, wp.getX(), wp.getY());
      if (i < waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
      x = wp.getX();
      y = wp.getY();
    }
    gc.setBackground(c);
    for (Label label : labels)
      label.paint(gc);
    paintDecorations(gc);
    gc.setLineWidth(width);
  }

  public void paintAligned(GC gc) {
    int x = waypoints.elementAt(0).getX();
    int y = waypoints.elementAt(0).getY();
    int width = gc.getLineWidth();
    gc.setLineWidth(LINE_WIDTH + 1);
    Color c = gc.getForeground();
    gc.setForeground(Diagram.RED);
    for (int i = 1; i < waypoints.size(); i++) {
      Waypoint wp = waypoints.elementAt(i);
      gc.drawLine(x, y, wp.getX(), wp.getY());
      if (i < waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
      x = wp.getX();
      y = wp.getY();
    }
    gc.setLineWidth(width);
    gc.setForeground(c);
  }

  private void paintDecorations(GC gc) {
    Point topIntercept = topIntercept(targetNode);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawTargetDecoration(gc, topIntercept.x, topIntercept.y, penultimate().x, penultimate().y);
    Point bottomIntercept = bottomIntercept(sourceNode);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawSourceDecoration(gc, bottomIntercept.x, bottomIntercept.y, second().x, second().y);
    topIntercept = topIntercept(sourceNode);
    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawSourceDecoration(gc, topIntercept.x, topIntercept.y, second().x, second().y);
    bottomIntercept = bottomIntercept(targetNode);
    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawTargetDecoration(gc, bottomIntercept.x, bottomIntercept.y, penultimate().x, penultimate().y);
    Point leftIntercept = leftIntercept(sourceNode);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawSourceDecoration(gc, leftIntercept.x, leftIntercept.y, second().x, second().y);
    Point rightIntercept = rightIntercept(targetNode);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawTargetDecoration(gc, rightIntercept.x, rightIntercept.y, penultimate().x, penultimate().y);
    leftIntercept = leftIntercept(targetNode);
    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawTargetDecoration(gc, leftIntercept.x, leftIntercept.y, penultimate().x, penultimate().y);
    rightIntercept = rightIntercept(sourceNode);
    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawSourceDecoration(gc, rightIntercept.x, rightIntercept.y, second().x, second().y);
  }

  public void paintHover(GC gc, int x, int y) {
    for (Label label : labels)
      label.paintHover(gc, x, y);
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
      gc.drawLine(x, y, wp.getX(), wp.getY());
      if (i < waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
      x = wp.getX();
      y = wp.getY();
    }
    gc.drawLine(x, y, endX, endY);
    gc.setBackground(c);
    for (Label label : labels)
      label.paint(gc);
    drawSourceDecoration(gc, startX, startY, waypoints.elementAt(1).getX(), waypoints.elementAt(1).getY());
    drawTargetDecoration(gc, endX, endY, waypoints.elementAt(waypoints.size() - 2).getX(), waypoints.elementAt(waypoints.size() - 2).getY());
    gc.setLineWidth(width);
  }

  public void paintOrthogonal(GC gc, Waypoint waypoint) {
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

  private Waypoint penultimate() {
    return waypoints.elementAt(waypoints.size() - 2);
  }

  private double pointToLineDistance(Waypoint A, Waypoint B, int x, int y) {
    double normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y) * (B.y - A.y));
    return Math.abs((x - A.x) * (B.y - A.y) - (y - A.y) * (B.x - A.x)) / normalLength;
  }

  public void reconnectSource(Node node, Port port) {
    if (sourcePort != null) sourcePort.getSources().remove(this);
    setSourceNode(node);
    setSourcePort(port);
    port.getSources().add(this);
    start().setX(node.getX() + port.getX() + (port.getWidth() / 2));
    start().setY(node.getY() + port.getY() + (port.getHeight() / 2));

  }

  public void reconnectTarget(Node node, Port port) {
    if (targetPort != null) targetPort.getTargets().remove(this);
    setTargetNode(node);
    setTargetPort(port);
    port.getTargets().add(this);
    end().setX(node.getX() + port.getX() + (port.getWidth() / 2));
    end().setY(node.getY() + port.getY() + (port.getHeight() / 2));
  }

  public Point rightIntercept(Node node) {
    int x0 = node.contains(start()) ? start().getX() : end().getX();
    int y0 = node.contains(start()) ? start().getY() : end().getY();
    int x1 = node.contains(start()) ? second().getX() : penultimate().getX();
    int y1 = node.contains(start()) ? second().getY() : penultimate().getY();
    int x2 = node.getX() + node.getWidth();
    int y2 = node.getY();
    int x3 = node.getX() + node.getWidth();
    int y3 = node.getY() + node.getHeight();
    Point p = intercept(x0, y0, x1, y1, x2, y2, x3, y3);
    if (x1 < x2 || p.y <= y2 || p.y >= y3)
      return null;
    else return p;
  }

  private Waypoint second() {
    return waypoints.elementAt(1);
  }

  public void setBlue(int blue) {
    this.blue = blue;
  }

  public void setGreen(int green) {
    this.green = green;
  }

  public void setLineStyle(int lineStyle) {
    this.lineStyle = lineStyle;
  }

  public void setRed(int red) {
    this.red = red;
  }

  public void setRefx(int refx) {
    this.refx = refx;
  }

  public void setRefy(int refy) {
    this.refy = refy;
  }

  public void setSourceHead(int sourceHead) {
    this.sourceHead = sourceHead;
  }

  public void setSourceNode(Node sourceNode) {
    this.sourceNode = sourceNode;
  }

  public void setSourcePort(Port sourcePort) {
    this.sourcePort = sourcePort;
  }

  public void setTargetHead(int targetHead) {
    this.targetHead = targetHead;
  }

  public void setTargetNode(Node targetNode) {
    this.targetNode = targetNode;
  }

  public void setTargetPort(Port targetPort) {
    this.targetPort = targetPort;
  }

  public void setText(String id, String text) {
    for (Label label : labels)
      label.setText(id, text);
  }

  public boolean sharesSegment(Edge edge) {
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

  public Point sourceIntercept() {
    return intercept(sourceNode);
  }

  public Waypoint start() {
    return waypoints.get(0);
  }

  public Point targetIntercept() {
    return intercept(targetNode);
  }

  public Point topIntercept(Node node) {
    int x0 = node.contains(start()) ? start().getX() : end().getX();
    int y0 = node.contains(start()) ? start().getY() : end().getY();
    int x1 = node.contains(start()) ? second().getX() : penultimate().getX();
    int y1 = node.contains(start()) ? second().getY() : penultimate().getY();
    int x2 = node.getX();
    int y2 = node.getY();
    int x3 = node.getX() + node.getWidth();
    int y3 = node.getY();
    Point p = intercept(x0, y0, x1, y1, x2, y2, x3, y3);
    if (y1 >= y2 || p.x <= x2 || p.x >= x3)
      return null;
    else return p;
  }

  public String toString() {
    return "Edge(" + waypoints + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Edge ");
    out.print("id='" + getId() + "' ");
    out.print("refx='" + getRefx() + "' ");
    out.print("refy='" + getRefy() + "' ");
    out.print("source='" + sourceNode.getId() + "' ");
    out.print("target='" + targetNode.getId() + "' ");
    out.print("sourcePort='" + sourcePort.getId() + "' ");
    out.print("targetPort='" + targetPort.getId() + "' ");
    out.print("sourceHead='" + getSourceHead() + "' ");
    out.print("targetHead='" + getTargetHead() + "' ");
    out.print("lineStyle='" + getLineStyle() + "' ");
    out.print("red='" + getRed() + "' ");
    out.print("green='" + getGreen() + "' ");
    out.print("blue='" + getBlue() + "'>");
    for (Waypoint waypoint : waypoints)
      if (waypoint != start() && waypoint != end()) waypoint.writeXML(out);
    for (Label label : labels)
      label.writeXML(out);
    out.print("</Edge>");
  }
}
