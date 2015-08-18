package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;

import tool.clients.EventHandler;
import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Diagram implements Display, MouseListener, PaintListener, MouseMoveListener, KeyListener {

  enum MouseMode {
    NONE, SELECTED, NEW_EDGE, DOUBLE_CLICK, MOVE_TARGET, MOVE_SOURCE, RESIZE_TOP_LEFT, RESIZE_TOP_RIGHT, RESIZE_BOTTOM_LEFT, RESIZE_BOTTOM_RIGHT, RUBBER_BAND
  };

  class Polar {

    double magnitude;
    double angle;

    public Polar(double magnitude, double angle) {
      super();
      this.magnitude = magnitude;
      this.angle = angle;
    }

    public Polar add(Polar p) {
      return toRectangular().add(p.toRectangular()).toPolar();
    }

    public Polar negate() {
      return toRectangular().negate().toPolar();
    }

    public Rectangular toRectangular() {
      return new Rectangular(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public String toString() {
      return toRectangular().toString();
    }
  }

  class Rectangular {

    double x;
    double y;

    public Rectangular(double x, double y) {
      super();
      this.x = x;
      this.y = y;
    }

    public Rectangular add(Rectangular r) {
      return new Rectangular(x + r.x, y + r.y);
    }

    public Rectangular negate() {
      return new Rectangular(-x, -y);
    }

    public Polar toPolar() {
      return new Polar(Math.sqrt(x * x + y * y), Math.atan2(y, x));
    }

    public String toString() {
      return "Rect(" + x + "," + y + ")";
    }
  }

  public static Color color(int code) {
    return XModeler.getXModeler().getDisplay().getSystemColor(code);
  }

  static final Color[] COLOURS                = new Color[] { color(SWT.COLOR_RED), color(SWT.COLOR_BLUE), color(SWT.COLOR_DARK_GREEN), color(SWT.COLOR_YELLOW), color(SWT.COLOR_GRAY), color(SWT.COLOR_DARK_RED), color(SWT.COLOR_CYAN), color(SWT.COLOR_DARK_YELLOW), color(SWT.COLOR_MAGENTA) };
  static final Color   RED                    = new Color(XModeler.getXModeler().getDisplay(), 255, 0, 0);
  static final Color   GREY                   = new Color(XModeler.getXModeler().getDisplay(), 192, 192, 192);
  static final Color   WHITE                  = new Color(XModeler.getXModeler().getDisplay(), 255, 255, 255);
  static final Color   GREEN                  = new Color(XModeler.getXModeler().getDisplay(), 0, 170, 0);

  static final Color   BLACK                  = new Color(XModeler.getXModeler().getDisplay(), 0, 0, 0);
  static double        ATTRACTION_CONSTANT    = 0.1;
  static int           REPULSION_CONSTANT     = 700;
  static double        DEFAULT_DAMPING        = 0.5;
  static int           DEFAULT_SPRING_LENGTH  = 200;

  static int           DEFAULT_MAX_ITERATIONS = 200;
  static int           RIGHT_BUTTON           = 3;
  static int           MAX_ZOOM               = 200;
  static int           MIN_ZOOM               = 20;

  static int           ZOOM_INC               = 10;

  static int           MIN_EDGE_DISTANCE      = 5;
  Color                diagramBackgroundColor = WHITE;
  Vector<Node>         nodes                  = new Vector<Node>();
  Vector<Edge>         edges                  = new Vector<Edge>();
  Vector<Display>      displays               = new Vector<Display>();
  Vector<Selectable>   selection              = new Vector<Selectable>();
  Transform            transform              = new Transform(org.eclipse.swt.widgets.Display.getCurrent());
  String               id;
  SashForm             container;
  Palette              palette;
  Canvas               canvas;
  ScrolledComposite    scroller;
  Edge                 selectedEdge           = null;
  Node                 selectedNode           = null;
  int                  x                      = 0;
  int                  y                      = 0;
  int                  render                 = 0;
  int                  firstX                 = 0;
  int                  firstY                 = 0;
  int                  bandX                  = 0;
  int                  bandY                  = 0;
  int                  lastX                  = 0;
  int                  lastY                  = 0;
  Port                 sourcePort;
  MouseMode            mode                   = MouseMode.NONE;
  int                  zoom                   = 100;
  boolean              disambiguationColors   = true;

  boolean              showWaypoints          = true;
  boolean              magneticWaypoints      = true;
  boolean              dogLegs                = true;

  String               edgeCreationType       = null;
  String               nodeCreationType       = null;

  public Diagram(String id, CTabFolder parent) {
    container = new SashForm(parent, SWT.HORIZONTAL | SWT.BORDER);
    palette = new Palette(container, this);
    scroller = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
    scroller.setExpandHorizontal(true);
    scroller.setExpandVertical(true);
    canvas = new Canvas(scroller, SWT.BORDER | SWT.DOUBLE_BUFFERED);
    canvas.setBackground(diagramBackgroundColor);
    canvas.addMouseListener(this);
    canvas.addPaintListener(this);
    canvas.addMouseMoveListener(this);
    canvas.addKeyListener(this);
    container.setWeights(new int[] { 1, 5 });
    scroller.setContent(canvas);
    this.id = id;
  }

  public void align() {
    for (Edge edge : edges)
      edge.align();
  }

  private boolean at(int x1, int y1, int x2, int y2) {
    return x1 == x2 && y1 == y2;
  }

  private boolean between(int v, int v1, int v2) {
    if (v1 < v2)
      return v >= v1 && v <= v2;
    else return v >= v2 && v <= v1;
  }

  private void checkEdgeCreation(int x, int y) {
    Port port = selectPort(x, y);
    if (port != null) {
      String sourceId = sourcePort.getId();
      String targetId = port.getId();
      Message m = DiagramClient.theClient().getHandler().newMessage("newEdge", 3);
      m.args[0] = new Value(edgeCreationType);
      m.args[1] = new Value(sourceId);
      m.args[2] = new Value(targetId);
      DiagramClient.theClient().getHandler().raiseEvent(m);
      resetPalette();
    }
  }

  private void checkMovedEdge() {
    // Have we arrived over a port?
    if (mode == MouseMode.MOVE_SOURCE)
      checkMovedSourceEdge();
    else checkMovedTargetEdge();
  }

  private void checkMovedSourceEdge() {
    boolean reconnected = false;
    for (Node n : nodes) {
      for (Port p : n.getPorts().values()) {
        if (!reconnected && p.contains(lastX - n.getX(), lastY - n.getY())) {
          reconnectEdgeSource(selectedEdge, n, p);
          reconnected = true;
        }
      }
    }
  }

  private void checkMovedTargetEdge() {
    boolean reconnected = false;
    for (Node n : nodes) {
      for (Port p : n.getPorts().values()) {
        if (!reconnected && p.contains(lastX - n.getX(), lastY - n.getY())) {
          reconnectEdgeTarget(selectedEdge, n, p);
          reconnected = true;
        }
      }
    }
  }

  private void checkSize() {
    Point p = canvas.getSize();
    int width = Math.max(p.x, maxWidth());
    int height = Math.max(p.y, maxHeight());
    float[] points = new float[] { (float) width, (float) height };
    transform.transform(points);
    width = (int) points[0];
    height = (int) points[1];
    canvas.setSize(width, height);
    scroller.setMinSize(width, height);
  }

  private void clear(GC gc) {
    gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
  }

  private void copyToClipboard() {
    Clipboard clipboard = new Clipboard(XModeler.getXModeler().getDisplay());
    org.eclipse.swt.widgets.Display d = XModeler.getXModeler().getDisplay().getCurrent();
    double zoomRatio = ((double) zoom) / 100.0;
    int width = (int) (zoomRatio * (maxWidth() + 50));
    int height = (int) (zoomRatio * (maxHeight() + 50));
    Image image = new Image(d, width, height);
    GC gc = new GC(image);
    paintOn(gc);
    ImageTransfer imageTransfer = ImageTransfer.getInstance();
    clipboard.setContents(new Object[] { image.getImageData() }, new Transfer[] { imageTransfer }, DND.CLIPBOARD | DND.SELECTION_CLIPBOARD);
    clipboard.dispose();
    image.dispose();
    gc.dispose();
  }

  public void copyToClipboard(String id) {
    if (getId().equals(id)) copyToClipboard();
  }

  public void delete(String id) {
    for (Display display : displays)
      display.remove(id);
    Display display = getDisplay(id);
    if (display != null) displays.remove(display);
    for (Node node : nodes) {
      node.remove(id);
    }
    Node node = getNode(id);
    if (node != null) {
      nodes.remove(node);
      deselect(node);
    }
    for (Edge edge : edges) {
      Label label = edge.getLabel(id);
      if (label != null) {
        edge.getLabels().remove(label);
        deselect(label);
      }
      Waypoint waypoint = edge.getWaypoint(id);
      if (waypoint != null) {
        edge.getWaypoints().remove(waypoint);
        deselect(waypoint);
      }
    }
    Edge edge = getEdge(id);
    if (edge != null) edges.remove(edge);
    redraw();
  }

  private void deselect(Selectable s) {
    selection.remove(s);
    s.deselect();
  }

  public void deleteGroup(String name) {
    palette.deleteGroup(name);
  }

  public void deselect() {
    for (Selectable selected : selection)
      selected.deselect();
    selection.clear();
    selectedEdge = null;
    selectedNode = null;
  }

  public void deselectPalette() {
    edgeCreationType = null;
    nodeCreationType = null;
    palette.deselect();
  }

  private int distance(Point p1, Point p2) {
    int dx = p1.x - p2.x;
    int dy = p1.y - p2.y;
    return (int) Math.sqrt((dx * dx) + (dy * dy));
  }

  private void dogLegs(Waypoint waypoint) {
    // Edges that have 90 degree angles incident on a node
    // should be maintained. This is implemented by selecting
    // the appropriate way-point and limiting its movement...
    if (dogLegs) {
      Edge edge = waypoint.getEdge();
      Vector<Waypoint> waypoints = edge.getWaypoints();
      int length = waypoints.size();
      int i = waypoints.indexOf(waypoint);
      if (i == length - 2 && length > 3) {
        Waypoint w = edge.getWaypoints().elementAt(i - 1);
        if (edge.end().above(waypoint) && edge.end().above(w)) {
          select(w);
          w.moveVertically();
          w.setY(waypoint.getY());
        } else if (edge.end().left(waypoint) && edge.end().left(w)) {
          select(w);
          w.moveHorizontally();
          w.setX(waypoint.getX());
        } else if (edge.end().right(waypoint) && edge.end().right(w)) {
          select(w);
          w.moveHorizontally();
          w.setX(waypoint.getX());
        } else if (edge.end().below(waypoint) && edge.end().below(w)) {
          select(w);
          w.moveVertically();
          w.setY(waypoint.getY());
        }
      }
      if (i == 1 && length > 3) {
        Waypoint w = edge.getWaypoints().elementAt(i + 1);
        if (edge.end().above(waypoint) && edge.end().above(w)) {
          select(w);
          w.moveVertically();
          w.setY(waypoint.getY());
        } else if (edge.end().left(waypoint) && edge.end().left(w)) {
          select(w);
          w.moveHorizontally();
          w.setX(waypoint.getX());
        } else if (edge.end().right(waypoint) && edge.end().right(w)) {
          select(w);
          w.moveHorizontally();
          w.setX(waypoint.getX());
        } else if (edge.end().below(waypoint) && edge.end().below(w)) {
          select(w);
          w.moveVertically();
          w.setY(waypoint.getY());
        }
      }
    }
  }

  public void doubleClick(GC gc, Diagram diagram, int dx, int dy, int mouseX, int mouseY) {
    // Called when a diagram is a display element.
    // Currently does nothing.
  }

  private Vector<Point> edgeIntersections(Edge edge) {
    Vector<Point> intersections = new Vector<Point>();
    boolean found = false;
    for (Edge e : edges) {
      if (e == edge)
        found = true;
      else {
        if (found) {
          for (int i = 1; i < edge.getWaypoints().size(); i++) {
            for (int j = 1; j < e.getWaypoints().size(); j++) {
              int x1 = edge.getWaypoints().elementAt(i - 1).getX();
              int y1 = edge.getWaypoints().elementAt(i - 1).getY();
              int x2 = edge.getWaypoints().elementAt(i).getX();
              int y2 = edge.getWaypoints().elementAt(i).getY();
              int x3 = e.getWaypoints().elementAt(j - 1).getX();
              int y3 = e.getWaypoints().elementAt(j - 1).getY();
              int x4 = e.getWaypoints().elementAt(j).getX();
              int y4 = e.getWaypoints().elementAt(j).getY();
              Point p = Edge.intersect(x1, y1, x2, y2, x3, y3, x4, y4);
              if (p.x > 0 && p.y > 0 && p.x != x1 && p.x != x2 && p.x != x3 && p.x != x4 && p.y != y1 && p.y != y2 && p.y != y3 && p.y != y4) {
                intersections.add(p);
              }
            }
          }
        }
      }
    }
    return intersections;
  }

  public void editText(String id) {
    for (Node node : nodes)
      node.editText(id);
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public SashForm getContainer() {
    return container;
  }

  public Color getDiagramBackgroundColor() {
    return diagramBackgroundColor;
  }

  public Display getDisplay(String id) {
    for (Display display : displays)
      if (display.getId().equals(id)) return display;
    return null;
  }

  public Edge getEdge(Label label) {
    for (Edge edge : edges)
      if (edge.getLabels().contains(label)) return edge;
    return null;
  }

  private Edge getEdge(String id) {
    for (Edge edge : edges)
      if (edge.getId().equals(id)) return edge;
    return null;
  }

  private Color getEdgeColor(Edge edge) {
    if (disambiguationColors) {
      if (isCloseToOtherEdge(edge)) {
        // If the number of colors is sufficiently large then
        // hopefully adjacent edges will not wind up the same
        // color. OTOH, just painting them colors will show
        // that they are close.
        return COLOURS[edges.indexOf(edge) % COLOURS.length];
      } else return Diagram.BLACK;
    } else return Diagram.BLACK;
  }

  public String getEdgeCreationType() {
    return edgeCreationType;
  }

  public Vector<Edge> getEdges() {
    return edges;
  }

  public String getId() {
    return id;
  }

  Node getNode(Port port) {
    for (Node node : nodes)
      if (node.getPorts().values().contains(port)) return node;
    return null;
  }

  public Node getNode(String id) {
    for (Node node : nodes)
      if (node.getId().equals(id)) return node;
    return null;
  }

  public String getNodeCreationType() {
    return nodeCreationType;
  }

  public Vector<Node> getNodes() {
    return nodes;
  }

  public Palette getPalette() {
    return palette;
  }

  private Port getPort(String id) {
    for (Node node : nodes)
      for (Port port : node.getPorts().values())
        if (port.getId().equals(id)) return port;
    return null;
  }

  public int getZoom() {
    return zoom;
  }

  private void handleDoubleClick(GC gc) {
    if (mode == MouseMode.DOUBLE_CLICK) {
      mode = MouseMode.NONE;
      for (Display display : displays) {
        display.doubleClick(gc, this, 0, 0, lastX, lastY);
      }
      for (Node node : nodes) {
        node.doubleClick(gc, this, lastX, lastY);
      }
    }
  }

  private void help() {
    // Show the current state of the diagram...
    String s = "Diagram controls: ";
    s = s + "select all (ctrl-a), ";
    s = s + "copy to clipboard (ctrl-c), ";
    s = s + "straighten edges (ctrl-s), ";
    s = s + "show waypoints (ctrl-w), ";
    s = s + "magnetism (ctrl-m) = " + (magneticWaypoints ? "on" : "off") + ", ";
    s = s + "doglegs (ctrl-o) = " + (dogLegs ? "on" : "off") + ", ";
    s = s + "zoom (ctrl+ ctrl-) = " + zoom + "%, ";
    s = s + "coloring (ctrl-d) = " + (disambiguationColors ? "on" : "off");
    XModeler.showMessage("Diagram Status", s);
  }

  private Vector<Point> intersectionPoints(Edge edge, Hashtable<Edge, Hashtable<Edge, Vector<Point>>> iTable) {
    Hashtable<Edge, Vector<Point>> eTable = iTable.get(edge);
    Vector<Point> points = new Vector<Point>();
    for (Edge e : eTable.keySet()) {
      for (Point p : eTable.get(e)) {
        points.add(p);
        iTable.get(e).remove(edge);
      }
    }
    return points;
  }

  private Hashtable<Edge, Hashtable<Edge, Vector<Point>>> intersectionTable() {
    Hashtable<Edge, Hashtable<Edge, Vector<Point>>> iTable = new Hashtable<Edge, Hashtable<Edge, Vector<Point>>>();
    for (Edge e1 : edges) {
      iTable.put(e1, new Hashtable<Edge, Vector<Point>>());
      for (Edge e2 : edges) {
        if (e1 != e2) {
          iTable.get(e1).put(e2, new Vector<Point>());
          for (int i = 1; i < e1.getWaypoints().size(); i++) {
            for (int j = 1; j < e2.getWaypoints().size(); j++) {
              int x1 = e1.getWaypoints().elementAt(i - 1).getX();
              int y1 = e1.getWaypoints().elementAt(i - 1).getY();
              int x2 = e1.getWaypoints().elementAt(i).getX();
              int y2 = e1.getWaypoints().elementAt(i).getY();
              int x3 = e2.getWaypoints().elementAt(j - 1).getX();
              int y3 = e2.getWaypoints().elementAt(j - 1).getY();
              int x4 = e2.getWaypoints().elementAt(j).getX();
              int y4 = e2.getWaypoints().elementAt(j).getY();
              Point p = Edge.intersect(x1, y1, x2, y2, x3, y3, x4, y4);
              int x = p.x;
              int y = p.y;
              if (!at(x, y, x1, y1) && !at(x, y, x2, y2) && !at(x, y, x3, y3) && !at(x, y, x4, y4) && onLine(x, y, x1, y1, x2, y2) && onLine(x, y, x3, y3, x4, y4)) {
                iTable.get(e1).get(e2).add(p);
              }
            }
          }
        }
      }
    }
    return iTable;
  }

  private boolean isCloseToOtherEdge(Edge edge) {
    for (Edge e : edges) {
      int distance = minDistance(edge, e);
      if (e != edge && 0 < distance && distance < MIN_EDGE_DISTANCE) return true;
    }
    return false;
  }

  private boolean isCommand(MouseEvent event) {
    return (event.stateMask & SWT.COMMAND) != 0;
  }

  public boolean isLeft(MouseEvent event) {
    return event.button == 1;
  }

  private boolean isRightClick(MouseEvent event) {
    return event.button == RIGHT_BUTTON;
  }

  public void italicise(String id, boolean italics) {
    for (Display display : displays)
      display.italicise(id, italics);
    for (Node node : nodes)
      node.italicise(id, italics);
    redraw();
  }

  public void keyPressed(KeyEvent e) {
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && ((SWT.SHIFT & e.stateMask) != SWT.SHIFT) && (e.keyCode == 'f')) {
      layout();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'a')) {
      selectAll();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c')) {
      copyToClipboard();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'd')) {
      disambiguationColors = !disambiguationColors;
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 's')) {
      straightenEdges();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'm')) {
      magneticWaypoints = !magneticWaypoints;
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'o')) {
      dogLegs = !dogLegs;
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'w')) {
      showWaypoints = !showWaypoints;
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '=')) {
      zoomIn();
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '-')) {
      zoomOut();
      help();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '/')) {
      help();
      redraw();
    }
  }

  public void keyReleased(KeyEvent event) {
  }

  private void layout() {
    Hashtable<Node, Polar> positions = new Hashtable<Node, Polar>();
    for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
      for (Node current : nodes) {
        if (!current.atOrigin() && !selection.contains(current)) {
          Polar force = positions.containsKey(current) ? positions.get(current) : new Rectangular(current.getX(), current.getY()).toPolar();
          for (Node other : nodes) {
            if (!current.sameLocation(other)) {
              force = force.add(nodeRepulsion(current, other));
            }
            for (Edge edge : edges) {
              if (edge.getSourceNode() == other && edge.getTargetNode() == current) {
                force = force.add(nodeAttraction(current, other, DEFAULT_SPRING_LENGTH));
              }
              if (edge.getTargetNode() == other && edge.getSourceNode() == current) {
                force = force.add(nodeAttraction(current, other, DEFAULT_SPRING_LENGTH));
              }
            }
          }
          positions.put(current, force);
        }
      }
      for (Node current : nodes) {
        if (positions.containsKey(current)) {
          Polar polar = positions.get(current);
          Rectangular rectangular = polar.toRectangular();
          current.move((int) Math.max(0, rectangular.x), (int) Math.max(0, rectangular.y));
        }
      }
    }
    for (Node node : positions.keySet())
      node.moveEvent();
  }

  private void leftClick(MouseEvent event) {
    if (nodeCreationType == null && edgeCreationType == null) {
      select(event.stateMask, event.x, event.y);
      lastX = event.x;
      lastY = event.y;
      redraw();
    } else if (edgeCreationType != null) {
      deselect();
      int x = event.x;
      int y = event.y;
      Port port = selectPort(x, y);
      if (port != null) {
        sourcePort = port;
        firstX = x;
        firstY = y;
        lastX = x;
        lastY = y;
        mode = MouseMode.NEW_EDGE;
      }
      redraw();
    } else if (nodeCreationType != null) {
      DiagramClient.theClient().newNode(nodeCreationType, id, event.x, event.y);
      resetPalette();
    }
  }

  private void magnetize(Waypoint waypoint) {
    // If we are in magnetic mode then select near way-points
    // and move them to be co-located...
    if (magneticWaypoints) {
      for (Edge edge : edges) {
        for (Waypoint w : edge.getWaypoints()) {
          if (w != waypoint && w.distance(waypoint) < 10) {
            // Important to select the way-point so that it
            // gets moved and XMF gets informed when the
            // mouse is released...
            select(w);
            // Co-locate the two way-points...
            w.setX(waypoint.getX());
            w.setY(waypoint.getY());
            dogLegs(w);
          }
        }
      }
    }

  }

  private int maxHeight() {
    int maxHeight = 0;
    for (Node node : nodes)
      maxHeight = Math.max(maxHeight, node.maxY());
    for (Edge edge : edges)
      maxHeight = Math.max(maxHeight, edge.maxY());
    return maxHeight;
  }

  private int maxWidth() {
    int maxWidth = 0;
    for (Node node : nodes)
      maxWidth = Math.max(maxWidth, node.maxX());
    for (Edge edge : edges)
      maxWidth = Math.max(maxWidth, edge.maxX());
    return maxWidth;
  }

  private int minDistance(Edge e1, Edge e2) {
    // The minimum distance between two edges is calculated as the
    // the minimum distance between the way-points and the intercepts
    // on the corresponding source and target nodes.
    int minDistance = Integer.MAX_VALUE;
    for (Waypoint w1 : e1.getWaypoints()) {
      if (w1 != e1.start() && w1 != e1.end()) {
        for (Waypoint w2 : e2.getWaypoints()) {
          if (w2 != e2.start() && w2 != e2.end()) {
            minDistance = Math.min(minDistance, w1.distance(w2));
          }
        }
      }
    }
    Node e1Source = e1.getSourceNode();
    Node e1Target = e1.getTargetNode();
    Node e2Source = e2.getSourceNode();
    Node e2Target = e2.getTargetNode();
    Point source1 = e1.intercept(e1Source);
    Point target1 = e1.intercept(e1Target);
    Point source2 = e2.intercept(e2Source);
    Point target2 = e2.intercept(e2Target);
    // Intercepts are degenerately null...
    minDistance = source1 == null || source2 == null ? minDistance : Math.min(minDistance, distance(source1, source2));
    minDistance = target1 == null || target2 == null ? minDistance : Math.min(minDistance, distance(target1, target2));
    return minDistance;
  }

  public void mouseDoubleClick(MouseEvent event) {
    scale(event);
  }

  public void mouseDown(MouseEvent event) {
    scale(event);
    if (isRightClick(event) || isCommand(event)) {
      rightClick(event);
    } else if (event.count == 1 && isLeft(event)) {
      leftClick(event);
    } else if (event.count == 2 && isLeft(event)) {
      mode = MouseMode.DOUBLE_CLICK;
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
  }

  public void mouseMove(final MouseEvent event) {
    scale(event);
    if (mode == MouseMode.SELECTED) {
      DiagramClient.theClient().runOnDisplay(new Runnable() {
        public void run() {
          if (event.x >= 0 && event.y >= 0) {
            int dx = event.x - lastX;
            int dy = event.y - lastY;
            lastX = event.x;
            lastY = event.y;
            for (Selectable selectable : selection)
              selectable.moveBy(dx, dy);
            redraw();
          }
        }
      });
    }
    if (mode == MouseMode.NEW_EDGE) {
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
    if (movingEdgeEnd()) {
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
    if (mode == MouseMode.NONE) {
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) {
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
    if (mode == MouseMode.RUBBER_BAND) {
      lastX = event.x;
      lastY = event.y;
      redraw();
    }
  }

  public void mouseUp(MouseEvent event) {
    scale(event);
    if (mode == MouseMode.NEW_EDGE) checkEdgeCreation(event.x, event.y);
    if (mode == MouseMode.SELECTED) sendMoveSelectedEvents();
    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) resizeBottomRight();
    if (mode == MouseMode.RUBBER_BAND) selectRubberBand();
    if (movingEdgeEnd()) checkMovedEdge();
    mode = MouseMode.NONE;
    redraw();
  }

  public void move(String id, int x, int y) {
    for (Display display : displays)
      display.move(id, x, y);
    for (Node node : nodes)
      node.move(id, x, y);
    for (Edge edge : edges)
      edge.move(id, x, y);
    redraw();
  }

  private boolean movingEdgeEnd() {
    return mode == MouseMode.MOVE_SOURCE || mode == MouseMode.MOVE_TARGET;
  }

  public void newBox(String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    Box box = new Box(id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
    displays.add(box);
  }

  public void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    if (parentId.equals(getId()))
      newBox(id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
    else {
      for (Display display : displays)
        display.newBox(parentId, id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      for (Node node : nodes)
        node.newBox(parentId, id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      redraw();
    }
  }

  public void newEdge(String id, String sourceId, String targetId, int refx, int refy, int sourceHead, int targetHead, int lineStyle, int red, int green, int blue) {
    Port sourcePort = getPort(sourceId);
    Node sourceNode = getNode(sourcePort);
    Port targetPort = getPort(targetId);
    Node targetNode = getNode(targetPort);
    int sourceX = sourceNode.getX() + sourcePort.getX() + (sourcePort.getWidth() / 2);
    int sourceY = sourceNode.getY() + sourcePort.getY() + (sourcePort.getHeight() / 2);
    int targetX = targetNode.getX() + targetPort.getX() + (targetPort.getWidth() / 2);
    int targetY = targetNode.getY() + targetPort.getY() + (targetPort.getHeight() / 2);
    if (sourcePort != null) {
      if (targetPort != null) {
        Edge edge = new Edge(id, sourceNode, sourcePort, sourceX, sourceY, targetNode, targetPort, targetX, targetY, refx, refy, sourceHead, targetHead, lineStyle, red, green, blue);
        edges.add(edge);
        redraw();
      } else System.err.println("cannot find target port " + targetId);
    } else System.err.println("cannot find source port " + sourceId);
  }

  private void newEllipse(String id, int x, int y, int width, int height, boolean showOutline, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    displays.add(new Ellipse(id, x, y, width, height, showOutline, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue));
  }

  public void newEllipse(String parentId, String id, int x, int y, int width, int height, boolean showOutline, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    if (parentId.equals(getId()))
      newEllipse(id, x, y, width, height, showOutline, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
    else {
      for (Display display : displays)
        display.newEllipse(parentId, id, x, y, width, height, showOutline, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      for (Node n : nodes)
        n.newEllipse(parentId, id, x, y, width, height, showOutline, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
    }
    redraw();
  }

  public void newGroup(String name) {
    if (!palette.hasGroup(name)) {
      palette.newGroup(name);
      container.layout();
    }
  }

  public void newImage(String id, String fileName, int x, int y, int width, int height) {
    displays.add(new tool.clients.diagrams.Image(id, fileName, x, y, width, height));
  }

  public void newImage(String parentId, String id, String fileName, int x, int y, int width, int height) {
    if (parentId.equals(getId()))
      newImage(id, fileName, x, y, width, height);
    else {
      for (Display display : displays)
        display.newImage(parentId, id, fileName, x, y, width, height);
      for (Node node : nodes)
        node.newImage(parentId, id, fileName, x, y, width, height);
    }
    redraw();
  }

  public void newMultilineText(String id, String text, int x, int y, int width, int height, boolean editable, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, String font) {
    MultilineText t = new MultilineText(id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
    displays.add(displays.size(), t);
  }

  public void newMultilineText(String parentId, String id, String text, int x, int y, int width, int height, boolean editable, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, String font) {
    if (parentId.equals(getId()))
      newMultilineText(id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
    else {
      for (Display display : displays)
        display.newMultilineText(parentId, id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
      for (Node node : nodes)
        node.newMultilineText(parentId, id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
    }
    redraw();
  }

  public void newNode(String id, int x, int y, int width, int height, boolean selectable) {
    Node node = new Node(id, x, y, width, height, selectable);
    nodes.add(node);
    deselect();
    select(node);
  }

  public void newPort(String nodeId, String id, int x, int y, int width, int height) {
    for (Node node : nodes)
      if (node.getId().equals(nodeId)) node.newPort(id, x, y, width, height);
  }

  private void newText(String id, String s, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    Text text = new Text(id, s, x, y, editable, underline, italicise, red, green, blue);
    displays.add(text);
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    if (parentId.equals(getId()))
      newText(id, text, x, y, editable, underline, italicise, red, green, blue);
    else {
      for (Display display : displays)
        display.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
      for (Node node : nodes)
        node.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
    }
    redraw();
  }

  public void newToggle(String groupId, String label, String toolId, boolean state, String icon) {
    palette.newToggle(this, groupId, label, toolId, state, icon);
    container.layout();
  }

  public void newAction(String groupId, String label, String toolId, String icon) {
    palette.newAction(this, groupId, label, toolId, icon);
    container.layout();
  }

  public void newTool(String group, String label, String toolId, boolean isEdge, String icon) {
    palette.newTool(this, group, label, toolId, isEdge, icon);
    container.layout();
  }

  public void newWaypoint(String parentId, String id, int index, int x, int y) {
    for (Edge edge : edges) {
      Waypoint w = edge.newWaypoint(parentId, id, index, x, y);
      if (w != null) {
        deselect();
        select(w);
        mode = MouseMode.SELECTED;
      }
    }
    redraw();
  }

  private Polar nodeAttraction(Node target, Node source, int springLength) {
    int x1 = target.getX();
    int y1 = target.getY();
    int x2 = source.getX();
    int y2 = source.getY();
    int dx = x1 - x2;
    int dy = y1 - y2;
    double magnitude = Math.sqrt((dx * dx) + (dy * dy));
    double force = ATTRACTION_CONSTANT * (Math.max(0, magnitude - springLength));
    if (magnitude <= 0.0001)
      return new Polar(0.0, 0.0);
    else {
      double angle = Math.atan2(dy, dx);
      return new Polar(force, angle).negate();
    }
  }

  private Polar nodeRepulsion(Node target, Node source) {
    int x1 = target.getX();
    int y1 = target.getY();
    int x2 = source.getX();
    int y2 = source.getY();
    int dx = x1 - x2;
    int dy = y1 - y2;
    double magnitude = Math.sqrt((dx * dx) + (dy * dy));
    double force = REPULSION_CONSTANT / (magnitude * magnitude);
    if (magnitude < 0.0001)
      return new Polar(0.0, 0.0);
    else {
      double angle = Math.atan2(dy, dx);
      return new Polar(force, angle);
    }
  }

  private boolean onLine(int x, int y, int x1, int y1, int x2, int y2) {
    return between(x, x1, x2) && between(y, y1, y2);
  }

  public void paint(GC gc, int x, int y) {
    // This is called when a diagram is a display element. The offsets need to be
    // included so that global painting is relative to (0,0).
    paintOn(gc);
  }

  private void paintAlignment(GC gc) {
    if (mode == MouseMode.SELECTED) {
      paintNodeAlignment(gc);
      paintEdgeAlignment(gc);
    }
  }

  public void paintControl(PaintEvent event) {
    if (render == 0) {
      GC gc = event.gc;
      paintOn(gc);
    }
  }

  private void paintDisplays(GC gc) {
    for (Display display : displays) {
      display.paint(gc, x, y);
    }
  }

  private void paintEdgeAlignment(GC gc) {
    for (Edge edge1 : edges) {
      for (Edge edge2 : edges) {
        if (edge1 != edge2) {
          for (Waypoint w : edge1.getWaypoints()) {
            if (selection.contains(w)) {
              if (edge1.sharesSegment(edge2)) {
                edge1.paintAligned(gc);
                edge2.paintAligned(gc);
              }
            }
          }
        }
      }
    }
  }

  private void paintEdges(GC gc) {
    Hashtable<Edge, Hashtable<Edge, Vector<Point>>> iTable = intersectionTable();
    for (Edge edge : edges) {
      if ((mode != MouseMode.MOVE_SOURCE && mode != MouseMode.MOVE_TARGET) || (selectedEdge != edge)) {
        Color color = getEdgeColor(edge);
        edge.paint(gc, color, showWaypoints, intersectionPoints(edge, iTable));
      } else {
        if (mode == MouseMode.MOVE_SOURCE) {
          edge.paintSourceMoving(gc, lastX, lastY);
        } else edge.paintTargetMoving(gc, lastX, lastY);
      }
    }
  }

  private void paintHover(GC gc) {
    if (!movingEdgeEnd()) {
      for (Node node : nodes)
        node.paintHover(gc, lastX, lastY, selection.contains(node));
      for (Edge edge : edges)
        edge.paintHover(gc, lastX, lastY);
    }
    if (movingEdgeEnd()) {
      for (Node node : nodes)
        node.paintPortHover(gc, lastX, lastY);
    }
  }

  public void paintHover(GC gc, int x, int y, int dx, int dy) {
    // Called when a diagram is a display element.
    // Currently does nothing.
  }

  private void paintNewEdge(GC gc) {
    if (mode == MouseMode.NEW_EDGE) {
      gc.drawLine(firstX, firstY, lastX, lastY);
    }
  }

  private void paintNodeAlignment(GC gc) {
    for (Node node1 : nodes)
      for (Node node2 : nodes)
        if (node1 != node2 && (selection.contains(node1) || selection.contains(node2))) {
          int[] style = gc.getLineDash();
          Color c = gc.getForeground();
          gc.setForeground(RED);
          gc.setLineDash(new int[] { 1, 2 });
          int x1 = node1.getX();
          int y1 = node1.getY();
          int w1 = node1.getWidth();
          int h1 = node1.getHeight();
          int x2 = node2.getX();
          int y2 = node2.getY();
          int w2 = node2.getWidth();
          int h2 = node2.getHeight();
          if (x1 == x2) gc.drawLine(x1, y1, x2, y2);
          if (x1 + w1 == x2) gc.drawLine(x1 + w1, y1, x2, y2);
          if (x1 == x2 + w2) gc.drawLine(x1, y1, x2 + w2, y2);
          if (x1 + w1 == x2 + w2) gc.drawLine(x1 + w1, y1, x2 + w2, y2);
          if (y1 == y2) gc.drawLine(x1, y1, x2, y2);
          if (y1 + h1 == y2) gc.drawLine(x1, y1 + h1, x2, y2);
          if (y1 == y2 + h2) gc.drawLine(x1, y1, x2, y2 + h2);
          if (y1 + h1 == y2 + h2) gc.drawLine(x1, y1 + h1, x2, y2 + h2);
          if (x1 + (w1 / 2) == x2 + (w2 / 2)) gc.drawLine(x1 + (w1 / 2), y1, x2 + (w2 / 2), y2);
          if (y1 + (h1 / 2) == y2 + (h2 / 2)) gc.drawLine(x1, y1 + (h1 / 2), x2, y2 + (h2 / 2));
          gc.setForeground(c);
          gc.setLineDash(style);
        }
  }

  private void paintNodes(GC gc) {
    for (Node node : nodes)
      node.paint(gc, this);
  }

  private void paintOn(GC gc) {
	    gc.setAntialias(SWT.ON);
	    gc.setTextAntialias(SWT.ON);
	    gc.setInterpolation(SWT.HIGH);
	    gc.setAdvanced(true);
	    gc.setTransform(transform);
	    clear(gc);
	    paintDisplays(gc);
	    paintNewEdge(gc);
	    paintResizing(gc);
	    paintEdges(gc);
	    paintAlignment(gc);
	    paintNodes(gc);
	    paintHover(gc);
	    paintSelected(gc);
	    paintRubberBand(gc);
	    paintNewNode(gc);
	    handleDoubleClick(gc);
	  }
	  
  private void paintNewNode(GC gc) {	  
		if(nodeCreationType == null) return;

		int X = lastX + 21;
		int Y = lastY + 16;
		int A = 2;
		int B = 7;
		int[] polygon = new int[] {
				X-A,Y-A,
				X-A,Y-B,
				X+A,Y-B,
				X+A,Y-A,
				X+B,Y-A,
				X+B,Y+A,
				X+A,Y+A,
				X+A,Y+B,
				X-A,Y+B,
				X-A,Y+A,
				X-B,Y+A,
				X-B,Y-A,
				X-A,Y-A};

		Color oldFGColor = gc.getForeground();
		Color oldBGColor = gc.getBackground();
		
		gc.setBackground(new Color(XModeler.getXModeler().getDisplay(), 0, 200, 100));
		gc.fillPolygon(polygon);
		gc.setForeground(BLACK);
		gc.drawPolygon(polygon);
		
		gc.setBackground(oldBGColor);
		gc.drawText("new " + nodeCreationType, X+8, Y+2);
		gc.setForeground(oldFGColor);
		
	}
	  
  private void paintResizing(GC gc) {
    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) {
      int width = lastX - selectedNode.getX();
      int height = lastY - selectedNode.getY();
      if (width >= 10 && height >= 10) gc.drawRectangle(selectedNode.getX(), selectedNode.getY(), width, height);
    }
  }

  private void paintRubberBand(GC gc) {
    if (mode == MouseMode.RUBBER_BAND) {
      gc.drawRectangle(bandX, bandY, lastX - bandX, lastY - bandY);
    }
  }

  private void paintSelected(GC gc) {
    if (!movingEdgeEnd()) {
      for (Selectable selected : selection)
        selected.paintSelected(gc);
    }
  }

  private void reconnectEdgeSource(Edge edge, Node node, Port port) {
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("edgeSourceReconnected", 2);
    message.args[0] = new Value(edge.getId());
    message.args[1] = new Value(port.getId());
    handler.raiseEvent(message);
  }

  private void reconnectEdgeTarget(Edge edge, Node node, Port port) {
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("edgeTargetReconnected", 2);
    message.args[0] = new Value(edge.getId());
    message.args[1] = new Value(port.getId());
    handler.raiseEvent(message);
  }

  public void redraw() {
    if (render == 0) {
      DiagramClient.theClient().runOnDisplay(new Runnable() {
        public void run() {
          checkSize();
          container.redraw();
          canvas.redraw(); // ensure repainting on all platforms
          canvas.update();
        }
      });
    }
  }

  public void remove(String id) {
    Display d = getDisplay(id);
    if (d != null) {
      displays.remove(d);
    } else {
      for (Display display : displays) {
        display.remove(id);
      }
    }
  }

  public void renderOff() {
    render++;
  }

  public void renderOn() {
    render = Math.max(render - 1, 0);
    redraw();
  }

  public void resetPalette() {
    edgeCreationType = null;
    nodeCreationType = null;
    DiagramClient.theClient().runOnDisplay(new Runnable() {
      public void run() {
        palette.reset();
      }
    });
  }

  public void resize(String id, int width, int height) {
    for (Display display : displays)
      display.resize(id, width, height);
    for (Node node : nodes)
      node.resize(id, width, height);
    redraw();
  }

  private void resizeBottomRight() {
    int width = lastX - selectedNode.getX();
    int height = lastY - selectedNode.getY();
    if (width >= 10 && height >= 10) resizeNode(selectedNode, width, height);
  }

  private void resizeNode(Node node, int width, int height) {
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("resizeNode", 3);
    message.args[0] = new Value(node.getId());
    message.args[1] = new Value(width);
    message.args[2] = new Value(height);
    handler.raiseEvent(message);
  }

  public void rightClick(MouseEvent event) {
    if (selection.isEmpty())
      MenuClient.popup(id, event.x, event.y);
    else {
      if (selection.size() == 1) {
        selection.elementAt(0).rightClick(event.x, event.y);
      }
    }
  }

  public Point scale(int x, int y) {
    float[] points = new float[] { (float) x, (float) y };
    transform.invert();
    transform.transform(points);
    transform.invert();
    return new Point((int) points[0], (int) points[1]);
  }

  public void scale(MouseEvent event) {
    float[] points = new float[] { (float) event.x, (float) event.y };
    transform.invert();
    transform.transform(points);
    transform.invert();
    event.x = (int) points[0];
    event.y = (int) points[1];
  }

  public Point scaleinv(int x, int y) {
    float[] points = new float[] { (float) x, (float) y };
    transform.transform(points);
    return new Point((int) points[0], (int) points[1]);
  }

  private void select(int stateMask, int x, int y) {
    boolean selected = false;
    boolean isShift = (stateMask & SWT.SHIFT) == SWT.SHIFT;
    if (!selected) {
      for (Edge edge : edges) {
        for (Waypoint waypoint : edge.getWaypoints()) {
          // Try the existing waypoints. Be careful to exclude the
          // dummy start and end waypoints...
          if (!selected && waypoint != edge.start() && waypoint != edge.end() && waypoint.nearTo(x, y)) {
            mode = MouseMode.SELECTED;
            if (!isShift && !selected(waypoint)) deselect();
            select(waypoint);
            magnetize(waypoint);
            dogLegs(waypoint);
            selected = true;
          }
        }
        if (!selected) {
          // See if we are near an end. If so then we go into
          // end reconnection mode...
          if (edge.nearStart(x, y)) {
            deselect();
            mode = MouseMode.MOVE_SOURCE;
            selectedEdge = edge;
            selected = true;
          } else if (edge.nearEnd(x, y)) {
            deselect();
            mode = MouseMode.MOVE_TARGET;
            selectedEdge = edge;
            selected = true;
          }
        }
        if (!selected) {
          // See if we are sufficiently near an edge to add a new way-point...
          selected = edge.newWaypoint(x, y);
          for (Label label : edge.getLabels()) {
            // See if we are selecting an edge...
            if (!selected && label.contains(x, y)) {
              mode = MouseMode.SELECTED;
              if (!isShift && !selected(label)) deselect();
              select(label);
              selected = true;
            }
          }
        }
      }
    }
    for (Node node : nodes) {
      if (!selected && node.contains(x, y)) {
        // If all else fails we might be selecting a node.
        // Trying nodes last allows the other elements behind
        // nodes to be selected...
        mode = MouseMode.SELECTED;
        if (!isShift && !selected(node)) deselect();
        select(node);
        selectSelfEdges(node);
        sendSelectedEvent(node);
        selected = true;
      }
      if (!selected && node.atTopLeftCorner(x, y)) {
        deselect();
        mode = MouseMode.RESIZE_TOP_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atTopRightCorner(x, y)) {
        deselect();
        mode = MouseMode.RESIZE_TOP_RIGHT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomLeftCorner(x, y)) {
        deselect();
        mode = MouseMode.RESIZE_BOTTOM_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomRightCorner(x, y)) {
        deselect();
        mode = MouseMode.RESIZE_BOTTOM_RIGHT;
        selectedNode = node;
        selected = true;
      }
    }
    if (!selected && !isShift) {
      deselect();
      mode = MouseMode.RUBBER_BAND;
      bandX = x;
      bandY = y;
    }
  }

  private void select(Selectable selectable) {
    if (!selection.contains(selectable)) {
      selection.add(selectable);
      selectable.select();
    }
  }

  private void selectAll() {
    deselect();
    for (Node node : nodes)
      select(node);
    for (Edge edge : edges) {
      for (Waypoint waypoint : edge.getWaypoints())
        if (waypoint != edge.start() && waypoint != edge.end()) select(waypoint);
      mode = MouseMode.SELECTED;
    }
  }

  private boolean selected(Selectable selectable) {
    return selection.contains(selectable);
  }

  private Port selectPort(int x, int y) {
    for (Node node : nodes) {
      for (Port port : node.getPorts().values()) {
        if (port.contains(x - node.getX(), y - node.getY())) { return port; }
      }
    }
    return null;
  }

  private void selectRubberBand() {
    int x = Math.min(bandX, lastX);
    int y = Math.min(bandY, lastY);
    int width = Math.abs(bandX - lastX);
    int height = Math.abs(bandY - lastY);
    Rectangle r = new Rectangle(x, y, width, height);
    deselect();
    for (Node node : nodes) {
      if (r.contains(node.getX(), node.getY())) select(node);
    }
    for (Edge edge : edges) {
      for (Waypoint w : edge.getWaypoints()) {
        if (!w.isStart() && !w.isEnd() && r.contains(w.getX(), w.getY())) select(w);
      }
      for (Label l : edge.getLabels()) {
        if (r.contains(l.getX(), l.getY())) select(l);
      }
    }
  }

  private void selectSelfEdges(Node node) {
    for (Edge edge : edges) {
      if (edge.getSourceNode() == node && edge.getTargetNode() == node) {
        for (Waypoint w : edge.getWaypoints()) {
          if (w != edge.start() && w != edge.end()) select(w);
        }
      }
    }
  }

  private void sendMoveSelectedEvents() {
    for (Selectable selectable : selection)
      selectable.moveEvent();
  }

  private void sendSelectedEvent(Node node) {
    String id = node.getId();
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("nodeSelected", 1);
    message.args[0] = new Value(id);
    handler.raiseEvent(message);
  }

  public void setEdgeCreationType(String edgeCreationType) {
    this.edgeCreationType = edgeCreationType;
  }

  public void setEdgeSource(String edgeId, String portId) {
    for (Edge edge : edges) {
      if (edge.getId().equals(edgeId)) {
        for (Node node : nodes) {
          for (Port port : node.getPorts().values()) {
            if (port.getId().equals(portId)) {
              edge.reconnectSource(node, port);
              redraw();
            }
          }
        }
      }
    }
  }

  public void setEdgeTarget(String edgeId, String portId) {
    for (Edge edge : edges) {
      if (edge.getId().equals(edgeId)) {
        for (Node node : nodes) {
          for (Port port : node.getPorts().values()) {
            if (port.getId().equals(portId)) {
              edge.reconnectTarget(node, port);
              redraw();
            }
          }
        }
      }
    }
  }

  public void setFillColor(String id, int red, int green, int blue) {
    for (Node node : nodes)
      node.setFillColor(id, red, green, blue);
    redraw();
  }

  public void setFont(String id, String fontData) {
    for (Node node : nodes) {
      node.setFont(id, fontData);
    }
    redraw();
  }

  public void setMagneticWaypoints(boolean magneticWaypoints) {
    this.magneticWaypoints = magneticWaypoints;
  }

  public void setNodeCreationType(String nodeCreationType) {
    this.nodeCreationType = nodeCreationType;
  }

  public void setText(String id, String text) {
    for (Display d : displays)
      d.setText(id, text);
    for (Node node : nodes)
      node.setText(id, text);
    for (Edge edge : edges)
      edge.setText(id, text);
    redraw();
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
    transform = new Transform(org.eclipse.swt.widgets.Display.getCurrent());
    transform.scale((float) (zoom / 100.0), (float) (zoom / 100.0));
  }

  private void straightenEdges() {
    for (Edge edge : edges)
      edge.straighten();
  }

  public void toggle(String toolId, boolean state) {
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("toggle", 3);
    message.args[0] = new Value(id);
    message.args[1] = new Value(toolId);
    message.args[2] = new Value(state);
    handler.raiseEvent(message);
  }

  public String toString() {
    return "Diagram(" + nodes + "," + edges + ")";
  }

  public void writeXML(PrintStream out) {
    writeXML("", out);
  }

  public void writeXML(String label, PrintStream out) {
    out.print("<Diagram id='" + getId() + "' label='" + label + "' zoom='" + getZoom() + "' magnetic='" + magneticWaypoints + "'>");
    palette.writeXML(out);
    for (Display display : displays)
      display.writeXML(out);
    for (Node node : nodes)
      node.writeXML(out);
    for (Edge edge : edges)
      edge.writeXML(out);
    out.print("</Diagram>");
  }

  public void zoomIn() {
    if (getZoom() < MAX_ZOOM) {
      setZoom(getZoom() + ZOOM_INC);
      zoomTo();
    }
  }

  public void zoomOut() {
    if (getZoom() > MIN_ZOOM) {
      setZoom(getZoom() - ZOOM_INC);
      zoomTo();
    }
  }

  private void zoomTo() {
    EventHandler eventHandler = DiagramClient.theClient().getHandler();
    Message message = eventHandler.newMessage("zoomChanged", 2);
    message.args[0] = new Value(getId());
    message.args[1] = new Value(zoom);
    eventHandler.raiseEvent(message);
  }

  public void action(String id) {
    EventHandler eventHandler = DiagramClient.theClient().getHandler();
    Message message = eventHandler.newMessage("action", 2);
    message.args[0] = new Value(getId());
    message.args[1] = new Value(id);
    eventHandler.raiseEvent(message);
  }

  public void hide(String id) {
    for (Node node : nodes)
      node.hide(id);
    for (Edge edge : edges)
      edge.hide(id);
  }

  public void show(String id) {
    for (Node node : nodes)
      node.show(id);
    for (Edge edge : edges)
      edge.show(id);
  }
}
