package tool.clients.diagrams;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import tool.clients.diagrams.Edge.HeadStyle;
import tool.clients.diagrams.Edge.Position;

public class EdgePainter {
	
	// An empty vector to be used where a list of intersections is required but there is no need in that case
	private static final Vector<Point> NO_INTERSECTIONS_PLACEHOLDER = new Vector<Point>();

	public static int                  ARROW_ANGLE     = 35;
	public static int                  ARROW_HEAD      = 10;
	public static int                  LINE_WIDTH      = 1;
	private final Edge edge;

	public EdgePainter(Edge edge) {
		this.edge = edge;
	}

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
	    switch (edge.sourceHead) {
	    case NO_ARROW:
	      break;
	    case ARROW:
	      drawArrow(gc, x, y, x2, y2, false, null, color);
	      break;
	    case WHITE_ARROW:
	      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE, color);
	      break;
	    default:
	      System.err.println("unknown type of source decoration: " + edge.sourceHead);
	    }
	  }

	  private void drawTargetDecoration(GC gc, Color color, int x, int y, int x2, int y2) {
	    switch (edge.targetHead) {
	    case NO_ARROW:
	      break;
	    case ARROW:
	      drawArrow(gc, x, y, x2, y2, false, null, color);
	      break;
	    case WHITE_ARROW:
	      drawArrow(gc, x, y, x2, y2, true, Diagram.WHITE, color);
	      break;
	    default:
	      System.err.println("unknown type of target decoration: " + edge.targetHead);
	    }
	  }  
	  
	  public void paint(GC gc, Color color, boolean showWaypoints, Vector<Point> intersections) {
	    if (!edge.hidden) {
	      int x = edge.waypoints.elementAt(0).getX();
	      int y = edge.waypoints.elementAt(0).getY();
	      int width = gc.getLineWidth();
	      gc.setLineWidth(LINE_WIDTH);
	      Color c = gc.getBackground();
	      gc.setBackground(color);
	      for (int i = 1; i < edge.waypoints.size(); i++) {
	        final Waypoint wp0 = edge.waypoints.elementAt(i - 1);
	        final Waypoint wp1 = edge.waypoints.elementAt(i);
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
	        if (showWaypoints && i < edge.waypoints.size() - 1) gc.fillOval(wp1.getX() - 3, wp1.getY() - 3, 6, 6);
	        x = wp1.getX();
	        y = wp1.getY();
	      }
	      gc.setBackground(c);
	      for (Label label : edge.labels)
	        label.paint(gc);
	      paintDecorations(gc, color);
	      gc.setLineWidth(width);
	    }
	  }

	  public void paintAligned(GC gc) {
	    if (!edge.hidden) {
	      int x = edge.waypoints.elementAt(0).getX();
	      int y = edge.waypoints.elementAt(0).getY();
	      int width = gc.getLineWidth();
	      gc.setLineWidth(LINE_WIDTH + 1);
	      for (int i = 1; i < edge.waypoints.size(); i++) {
	        Waypoint wp = edge.waypoints.elementAt(i);
	        paintLine(gc, x, y, wp.getX(), wp.getY(), Diagram.RED, NO_INTERSECTIONS_PLACEHOLDER);
	        if (i < edge.waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
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

	  private boolean isSelfEdge() {
	    return edge.sourceNode == edge.targetNode;
	  }

	  private void paintHeterogeneousEdgeDecorations(GC gc, Color color) {
	    Point topIntercept = edge.intercept(edge.targetNode, Position.TOP);
	    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawTargetDecoration(gc, color, topIntercept.x, topIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    Point bottomIntercept = edge.intercept(edge.sourceNode, Position.BOTTOM);
	    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawSourceDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, edge.second().x, edge.second().y);
	    topIntercept = edge.intercept(edge.sourceNode, Position.TOP);
	    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawSourceDecoration(gc, color, topIntercept.x, topIntercept.y, edge.second().x, edge.second().y);
	    bottomIntercept = edge.intercept(edge.targetNode, Position.BOTTOM);
	    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawTargetDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    Point leftIntercept = edge.intercept(edge.sourceNode, Position.LEFT);
	    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawSourceDecoration(gc, color, leftIntercept.x, leftIntercept.y, edge.second().x, edge.second().y);
	    Point rightIntercept = edge.intercept(edge.targetNode, Position.RIGHT);
	    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawTargetDecoration(gc, color, rightIntercept.x, rightIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    leftIntercept = edge.intercept(edge.targetNode, Position.LEFT);
	    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawTargetDecoration(gc, color, leftIntercept.x, leftIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    rightIntercept = edge.intercept(edge.sourceNode, Position.RIGHT);
	    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawSourceDecoration(gc, color, rightIntercept.x, rightIntercept.y, edge.second().x, edge.second().y);
	  }

	  /*NEW PRIVATE*/private void paintHomogeneousEdgeDecorations(GC gc, Color color) {
	    // Ensure that the correct waypoint is used when calculating the intercepts...
	    Point topIntercept = edge.targetHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.targetNode, edge.end(), edge.penultimate(), Position.TOP);
	    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawTargetDecoration(gc, color, topIntercept.x, topIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    Point bottomIntercept = edge.sourceHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.sourceNode, edge.start(), edge.second(), Position.BOTTOM);
	    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawSourceDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, edge.second().x, edge.second().y);
	    topIntercept = edge.sourceHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.sourceNode, edge.start(), edge.second(), Position.TOP);
	    if (topIntercept != null && topIntercept.x >= 0 && topIntercept.y >= 0) drawSourceDecoration(gc, color, topIntercept.x, topIntercept.y, edge.second().x, edge.second().y);
	    bottomIntercept = edge.targetHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.targetNode, edge.end(), edge.penultimate(), Position.BOTTOM);
	    if (bottomIntercept != null && bottomIntercept.x >= 0 && bottomIntercept.y >= 0) drawTargetDecoration(gc, color, bottomIntercept.x, bottomIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    Point leftIntercept = edge.sourceHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.sourceNode, edge.start(), edge.second(), Position.LEFT);
	    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawSourceDecoration(gc, color, leftIntercept.x, leftIntercept.y, edge.second().x, edge.second().y);
	    Point rightIntercept = edge.targetHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.targetNode, edge.end(), edge.penultimate(), Position.RIGHT);
	    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawTargetDecoration(gc, color, rightIntercept.x, rightIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    leftIntercept = edge.targetHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.targetNode, edge.end(), edge.penultimate(), Position.LEFT);
	    if (leftIntercept != null && leftIntercept.x >= 0 && leftIntercept.y >= 0) drawTargetDecoration(gc, color, leftIntercept.x, leftIntercept.y, edge.penultimate().x, edge.penultimate().y);
	    rightIntercept = edge.sourceHead == HeadStyle.NO_ARROW ? null : edge.intercept(edge.sourceNode, edge.start(), edge.second(), Position.RIGHT);
	    if (rightIntercept != null && rightIntercept.x >= 0 && rightIntercept.y >= 0) drawSourceDecoration(gc, color, rightIntercept.x, rightIntercept.y, edge.second().x, edge.second().y);
	  }

	  public void paintHover(GC gc, int x, int y) {
	    for (Label label : edge.labels)
	      label.paintHover(gc, x, y);
	  }

	  public void paintLine(GC gc, int x1, int y1, int x2, int y2, Color lineColor, Vector<Point> intersect) {
	    // Paint the line in the line style.
	    int style = gc.getLineStyle();
	    Color c = gc.getForeground();
	    gc.setForeground(lineColor);
	    switch (edge.lineStyle) {
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
	      Point p1 = Edge.circleIntersect(p.x, p.y, 3.0, x1, y1);
	      Point p2 = Edge.circleIntersect(p.x, p.y, 3.0, x2, y2);
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
	    for (int i = 1; i < edge.waypoints.size() - 1; i++) {
	      Waypoint wp = edge.waypoints.elementAt(i);
	      paintLine(gc, x, y, wp.getX(), wp.getY(), Diagram.BLACK, NO_INTERSECTIONS_PLACEHOLDER);
	      if (i < edge.waypoints.size() - 1) gc.fillOval(wp.getX() - 3, wp.getY() - 3, 6, 6);
	      x = wp.getX();
	      y = wp.getY();
	    }
	    paintLine(gc, x, y, endX, endY, Diagram.BLACK, NO_INTERSECTIONS_PLACEHOLDER);
	    gc.setBackground(c);
	    for (Label label : edge.labels)
	      label.paint(gc);
	    drawSourceDecoration(gc, Diagram.BLACK, startX, startY, edge.waypoints.elementAt(1).getX(), edge.waypoints.elementAt(1).getY());
	    drawTargetDecoration(gc, Diagram.BLACK, endX, endY, edge.waypoints.elementAt(edge.waypoints.size() - 2).getX(), edge.waypoints.elementAt(edge.waypoints.size() - 2).getY());
	    gc.setLineWidth(width);
	  }

	  public void paintOrthogonal(GC gc, Waypoint waypoint) { // Zielscheibe
	    if (waypoint != edge.start() && waypoint != edge.end()) {
	      int index = edge.waypoints.indexOf(waypoint);
	      int length = 30;
	      Waypoint pre = edge.waypoints.elementAt(index - 1);
	      Waypoint post = edge.waypoints.elementAt(index + 1);
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
	    paintMovingSourceOrTarget(gc, x, y, edge.end().getX(), edge.end().getY());
	  }

	  public void paintTargetMoving(GC gc, int x, int y) {
	    paintMovingSourceOrTarget(gc, edge.start().getX(), edge.start().getY(), x, y);
	  }
	  

	  private Vector<Point> getIntersection(Waypoint wp0, Waypoint wp1, Vector<Point> intersections) {
	    Vector<Point> i = new Vector<Point>();
	    for (Point p : intersections) {
	      double d = Edge.pointToLineDistance(wp0, wp1, p.x, p.y);
	      if (d < 1) {
	        i.add(p);
	      }
	    }
	    return i;
	  }
}