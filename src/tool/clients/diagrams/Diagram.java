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
import tool.clients.diagrams.Diagram.MouseMode;
import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Diagram implements Display {

  enum MouseMode {
    NONE, SELECTED, NEW_EDGE, DOUBLE_CLICK, MOVE_TARGET, MOVE_SOURCE, RESIZE_TOP_LEFT, RESIZE_TOP_RIGHT, RESIZE_BOTTOM_LEFT, RESIZE_BOTTOM_RIGHT, RUBBER_BAND
  };
  
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
  static float           MAX_ZOOM               = 2.00f;
  static float           MIN_ZOOM               = .20f;

  static float           ZOOM_INC               = .10f;

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
  int                  render                 = 0;
  
  /*
   * These coordinates are required for Mouse interaction (draw lines, rectangles, move ...)
   */
  
  int                  firstX                 = 0;
  int                  firstY                 = 0;
  int                  bandX                  = 0;
  int                  bandY                  = 0;
  int                  lastX                  = 0;
  int                  lastY                  = 0;
    
  Port                 sourcePort;
  MouseMode            mode                   = MouseMode.NONE;
  float                  zoom                   = 1.00f;
  boolean              disambiguationColors   = true;

  boolean              showWaypoints          = true;
  boolean              magneticWaypoints      = true;
  boolean              dogLegs                = true;

  String               edgeCreationType       = null;
  String               nodeCreationType       = null;
  public String updateID = null;

  /**
   * Creates a new Diagram
   * @param id
   * @param parent
   */
  public Diagram(String id, CTabFolder parent) {
    container = new SashForm(parent, SWT.HORIZONTAL | SWT.BORDER);
    palette = new Palette(container, this);
    scroller = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
    scroller.setExpandHorizontal(true);
    scroller.setExpandVertical(true);
    canvas = new Canvas(scroller, SWT.BORDER | SWT.DOUBLE_BUFFERED);
    canvas.setBackground(diagramBackgroundColor);
    canvas.addMouseListener(new MyMouseListener());
    canvas.addPaintListener(new MyPaintListener());
    canvas.addMouseMoveListener(new MyMouseMoveListener());
    canvas.addKeyListener(new MyKeyListener());
    container.setWeights(new int[] { 1, 5 });
    scroller.setContent(canvas);
    this.id = id;
  }

  
	private void sendMessageToDeleteSelection() {
		System.out.println("sendMessageToDeleteSelection");
		for (Node node : nodes)
			if (selection.contains(node)) 
				new OutboundMessages().deleteComand(node.id);
	}
  
  /**
   * Deletes a Display
   * @param id
   */
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



  public void deleteGroup(String name) {
    palette.deleteGroup(name);
  }

  public void deselectPalette() {
    edgeCreationType = null;
    nodeCreationType = null;
    palette.deselect();
  }

  private void dogLegs(Waypoint waypoint) {
    // Edges that have 90 degree angles incident on a node
    // should be maintained. This is implemented by selecting
    // the appropriate way-point and limiting its movement...
    Edge edge = waypoint.getEdge();
    boolean squaredEdge = edge.isSquared();
    if (dogLegs) {
      Vector<Waypoint> waypoints = edge.getWaypoints();
      int length = waypoints.size();
      int i = waypoints.indexOf(waypoint);     
      deselectAll(); select(waypoint);
      
      if (i <= length - 2 && i >= 2 && length > 3) { // move previous Waypoint
	    Waypoint next     = edge.getWaypoints().elementAt(i - 1);
	    Waypoint nextNext = edge.getWaypoints().elementAt(i - 2);
    	if((waypoint.isApproximatelyLeftOrRightOf(next) || squaredEdge) && next.isExactlyAboveOrBelow(nextNext)) {  
  		  select(next);
  		  next.limitMovementToVertical(); 
  		  next.setY(waypoint.getY()); }
  	    if((waypoint.isApproximatelyAboveOrBelow(next) || squaredEdge) && next.isExactlyLeftOrRightOf(nextNext)) {  
  		  select(next);
  		  next.limitMovementToHorizontal();
  		  next.setX(waypoint.getX()); }
      }
      
      if (i >= 1 && i <= length - 3 && length > 3) { // move next Waypoint
    	Waypoint next     = edge.getWaypoints().elementAt(i + 1);
    	Waypoint nextNext = edge.getWaypoints().elementAt(i + 2);
	  	if((waypoint.isApproximatelyLeftOrRightOf(next) || squaredEdge) && next.isExactlyAboveOrBelow(nextNext)) {  
  		  select(next);
  		  next.limitMovementToVertical(); 
  		  next.setY(waypoint.getY()); }
	  	if((waypoint.isApproximatelyAboveOrBelow(next) || squaredEdge) && next.isExactlyLeftOrRightOf(nextNext)) {  
  		  select(next);
  		  next.limitMovementToHorizontal();
  		  next.setX(waypoint.getX()); }
      }
      
    }
  }

  public void editText(String id) {
    for (Node node : nodes)
      node.editText(id);
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
      } else if(edge.getRed()>=0 && edge.getGreen() >=0 && edge.getBlue()>=0 )
    	  		return new Color(XModeler.getXModeler().getDisplay(), edge.getRed(),edge.getGreen(),edge.getBlue());
      	else return Diagram.BLACK;
    } else return Diagram.BLACK;
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

  private void handleDoubleClick(GC gc) {
    if (mode == MouseMode.DOUBLE_CLICK) {
      mode = MouseMode.NONE;
      for (Display display : displays) {
        display.doubleClick(gc, this, 0, 0, lastX, lastY);
      }
      for (Node node : nodes) {
          node.doubleClick(gc, this, lastX, lastY);
      }
      for(Edge edge : edges) {
    	  edge.doubleClick(gc, this, lastX, lastY);
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


  private boolean isCloseToOtherEdge(Edge edge) {
    for (Edge e : edges) {
      double distance = minDistance(edge, e);
      if (e != edge && 0 < distance && distance < MIN_EDGE_DISTANCE) return true;
    }
    return false;
  }

  private boolean isCommand(MouseEvent event) {
    return (event.stateMask & SWT.COMMAND) != 0;
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

  @Override
  public void doubleClick(GC gc, Diagram diagram, int dx, int dy, int mouseX, int mouseY) {
    // Called when a diagram is a display element.
    // Currently does nothing.
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

  private double minDistance(Edge e1, Edge e2) {
    // The minimum distance between two edges is calculated as the
    // the minimum distance between the way-points and the intercepts
    // on the corresponding source and target nodes.
	double minDistance = Double.POSITIVE_INFINITY;
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
  
  private int distance(Point p1, Point p2) {
    int dx = p1.x - p2.x;
    int dy = p1.y - p2.y;
    return (int) Math.sqrt((dx * dx) + (dy * dy));
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

  public void newEdge(String id, String sourceId, String targetId, int refx, int refy, int sourceHead, int targetHead, int lineStyle, int red, int green, int blue, Integer sourceX, Integer sourceY, Integer targetX, Integer targetY) {
    Port sourcePort = getPort(sourceId);
    Node sourceNode = getNode(sourcePort);
    Port targetPort = getPort(targetId);
    Node targetNode = getNode(targetPort);
    if (sourcePort != null) {
      if (targetPort != null) {
    	 if(sourceX == null) sourceX = sourceNode.getX() + sourcePort.getX() + (sourcePort.getWidth() / 2);
    	 if(sourceY == null) sourceY = sourceNode.getY() + sourcePort.getY() + (sourcePort.getHeight() / 2);
    	 if(targetX == null) targetX = targetNode.getX() + targetPort.getX() + (targetPort.getWidth() / 2);
    	 if(targetY == null) targetY = targetNode.getY() + targetPort.getY() + (targetPort.getHeight() / 2);
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
    deselectAll();
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

  public void newToggle(String groupId, String label, String toolId, boolean state, String iconTrue, String iconFalse) {
    palette.newToggle(this, groupId, label, toolId, state, iconTrue, iconFalse);
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
  

  public void removeAny(String toolId) {
	  palette.removeAny(this, toolId);
	  container.layout();
  }

  transient static boolean dontSelectNextWaypoint = false;
  public void newWaypoint(String parentId, String id, int index, int x, int y, boolean skipSelection) {
    for (Edge edge : edges) {
      Waypoint w = edge.newWaypoint(parentId, id, index, x, y);
      if (w != null) {
        deselectAll();
        if(!dontSelectNextWaypoint && !skipSelection) {
        	mode = MouseMode.SELECTED;
        	select(w);
        } else {
        	dontSelectNextWaypoint = false;
        }
      }
    }
    redraw();
  }

  private Point2D nodeAttraction(Node target, Node source, int springLength) {
    int x1 = target.getX();
    int y1 = target.getY();
    int x2 = source.getX();
    int y2 = source.getY();
    int dx = x1 - x2;
    int dy = y1 - y2;
    double magnitude = Math.sqrt((dx * dx) + (dy * dy));
    double force = ATTRACTION_CONSTANT * (Math.max(0, magnitude - springLength));
    if (magnitude <= 0.0001)
      return Point2D.ZERO;
    else {
      double angle = Math.atan2(dy, dx);
      return Point2D.createPolar(force, angle).negate();
    }
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

  private void paintDisplays(GC gc) {
    for (Display display : displays) {
      display.paint(gc, 0, 0);
    }
  }

  private void paintEdgeAlignment(GC gc) {
    for (Edge edge1 : edges) {
      for (Edge edge2 : edges) {
        if (edge1 != edge2) {
          for (Waypoint w : edge1.getWaypoints()) {
            if (selection.contains(w)) {
              if (edge1.sharesSegment(edge2)) {
                edge1.getPainter().paintAligned(gc);
                edge2.getPainter().paintAligned(gc);
              }
            }
          }
        }
      }
    }
  }

  private void paintHover(GC gc) {
    if (!movingEdgeEnd()) {
      for (Node node : nodes)
        node.paintHover(gc, lastX, lastY, selection.contains(node));
      for (Edge edge : edges)
        edge.getPainter().paintHover(gc, lastX, lastY);
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
//    gc.drawString("Zoom: " + zoom, 5, 5);
//    gc.drawString("Width M: " + maxWidth(), 5, 17);
//    gc.drawString("Width C: " + canvas.getSize().x, 5, 29);
  }

  private void clear(GC gc) {
    gc.fillRectangle(0, 0, canvas.getSize().x, canvas.getSize().y);
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
  
  /**
   * This function checks if the canvas has to be resized because 
   * it is too small. If it is too small it's resized.
   */
  private void checkSize() {
//    System.err.println("\ncheckSize");
    /* p is the size of the canvas. 
     * The size does not change with the zoom. */
	Point p = canvas.getSize();
//	System.err.println("canvas Screen Size : " + p);
	/* Now it is compared with the needed size.
     * This is calculated as raw positions. 
	 */
	Point maxRawSize = new Point(maxWidth(), maxHeight());
//	System.err.println("needed Raw Size : " + maxRawSize);
	/* Now  the canvas' screen size has to be 
	 * converted to raw size.
	 */
	float[] canvasPoints = new float[] { (float) p.x, (float) p.y };
	transform.invert();
	transform.transform(canvasPoints);
	transform.invert();
	Point canvasRawSize = new Point((int) canvasPoints[0], (int) canvasPoints[1]);
//	System.err.println("canvas Raw Size : " + canvasRawSize);
	/* The new size can now be calculated */
    int width = Math.max(canvasRawSize.x, maxRawSize.x);
    int height = Math.max(canvasRawSize.y, maxRawSize.y);
    /* But the new size is still in Raw size, 
     * so it's transformed to screen size */
    float[] newSize = new float[] { (float) width, (float) height };
    transform.transform(newSize);
    width = (int) newSize[0];
    height = (int) newSize[1];    
	/* As some rounding errors may have accumulated now,
	 * any change which is less then 5 is ignored.
	 */
    if(Math.abs(width - canvas.getSize().x) < 4.5) width = canvas.getSize().x;
    if(Math.abs(height - canvas.getSize().y) < 4.5) height = canvas.getSize().y;    
    /* The new sizes are now set */
	canvas.setSize(width, height);
	scroller.setMinSize(width, height);
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

  public Point scale(int x, int y) {
    float[] points = new float[] { (float) x, (float) y };
//    System.err.println("before Transform (scale): " + points[0]);
    transform.invert();
    transform.transform(points);
    transform.invert();
//    System.err.println("after Transform (scale): " + points[0]);
    return new Point((int) points[0], (int) points[1]);
  }

  public void scale(MouseEvent event) {
	  Point p = scale(event.x, event.y);
	  event.x = p.x;
      event.y = p.y;
  }

  public Point scaleinv(int x, int y) {
    float[] points = new float[] { (float) x, (float) y };
    transform.transform(points);
    return new Point((int) points[0], (int) points[1]);
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
  
  public void setEditable(String id, boolean editable) { //Björn
	  for (Node node : nodes)
   		node.setEditable(id, editable);
      redraw();  
  }
  
  public void showEdges(String id, boolean top,boolean bottom,boolean left, boolean right) { //Björn
	  for (Node node : nodes)
	    node.showEdges(id, top, bottom, left,right);
	  redraw();  
  }
  
  public void setTextColor(String id, int red, int green, int blue) {
    for (Display display : displays)
      display.setFillColor(id, red, green, blue);
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

  public void setBorder(String id, boolean border) {
	  for (Edge edge : edges)
	      edge.setBorder(id, border);
	    redraw();
  }
  
  public void setFill(String id, boolean fill) {
	  for (Edge edge : edges)
	      edge.setFill(id, fill);
	    redraw();
  }
  
  public void setZoom(float zoom) {
    this.zoom = zoom;
    transform = new Transform(org.eclipse.swt.widgets.Display.getCurrent());
    transform.scale(zoom, zoom);//(float) (zoom / 100.0), (float) (zoom / 100.0));
  }

  private void straightenEdges() {
    for (Edge edge : edges)
      edge.straighten();
  }
  
  public String toString() {
    return "Diagram(" + nodes + "," + edges + ")";
  }

  
  /*
   * The following functions are used to paint edges
   */

  private boolean isOnLine(int x, int y, int x1, int y1, int x2, int y2) {
    return isBetween(x, x1, x2) && isBetween(y, y1, y2);
  }
	  
  private boolean isAt(int x1, int y1, int x2, int y2) {
    return x1 == x2 && y1 == y2;
  }

  private boolean isBetween(int v, int v1, int v2) {
    if (v1 < v2)
      return v >= v1 && v <= v2;
    else return v >= v2 && v <= v1;
  }
  
  private Hashtable<Edge, Hashtable<Edge, Vector<Point>>> getIntersectionTable() {
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
              if (!isAt(x, y, x1, y1) && !isAt(x, y, x2, y2) && !isAt(x, y, x3, y3) && !isAt(x, y, x4, y4) && isOnLine(x, y, x1, y1, x2, y2) && isOnLine(x, y, x3, y3, x4, y4)) {
                iTable.get(e1).get(e2).add(p);
              }
            }
          }
        }
      }
    }
    return iTable;
  }
  
  private void paintEdges(GC gc) {
    Hashtable<Edge, Hashtable<Edge, Vector<Point>>> iTable = getIntersectionTable();
    for (Edge edge : edges) {
      if ((mode != MouseMode.MOVE_SOURCE && mode != MouseMode.MOVE_TARGET) || (selectedEdge != edge)) {
        Color color = getEdgeColor(edge);
        edge.getPainter().paint(gc, color, showWaypoints, intersectionPoints(edge, iTable));
      } else {
        if (mode == MouseMode.MOVE_SOURCE) {
          edge.getPainter().paintSourceMoving(gc, lastX, lastY);
        } else edge.getPainter().paintTargetMoving(gc, lastX, lastY);
      }
    }
  }

  /*
   * The previous functions are used to paint edges
   *
   * The following functions are used for Zoom
   * 
   * Zoom In/Out can be called by a KeyEvent or by DiagramClient
   * Zoom 1 is still unused
   * Why is zoom int? is it 100-based?
   */

  public void zoomOne() {
      setZoom(1);
      new OutboundMessages().zoomTo();
  }
  
  public void zoomTo(float f) {
      setZoom(f);
      new OutboundMessages().zoomTo();
  }
  
  public void zoomIn() {
    if (getZoom() < MAX_ZOOM) {
      setZoom(getZoom() + ZOOM_INC);
      new OutboundMessages().zoomTo();
    }
  }

  public void zoomOut() {
    if (getZoom() > MIN_ZOOM) {
      setZoom(getZoom() - ZOOM_INC);
      new OutboundMessages().zoomTo();
    }
  }
  
  public float getZoom() {
    return zoom;
  }

  /*
   * The previous functions are used for Zoom
   *
   * The following functions are simply forwarding
   */
  
  public void align() {
    for (Edge edge : edges)
      edge.align();
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
  
  /*
   *  XML Save Load
   */

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
  
  /*
   * Simple Getters
   */
  
  public Canvas getCanvas() { return canvas; }
  public SashForm getContainer() { return container; }
  public Color getDiagramBackgroundColor() { return diagramBackgroundColor;}
  public String getEdgeCreationType() { return edgeCreationType; }
  public Vector<Edge> getEdges() { return edges; }
  public String getId() { return id; }

  /*
   * Listeners 
   */

  private class MyMouseListener implements MouseListener {
	  
	  @Override
	  public void mouseDoubleClick(MouseEvent event) {
	    scale(event);
	  }

	  @Override
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

	  private void leftClick(MouseEvent event) {
	    if (nodeCreationType == null && edgeCreationType == null) {
	      select(event.stateMask, event.x, event.y);
	      lastX = event.x;
	      lastY = event.y;
	      redraw();
	    } else if (edgeCreationType != null) {
	      deselectAll();
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
	    
	    if(updateID != null) action(updateID);
	  }
	  
	  private void rightClick(MouseEvent event) {
	    if (selection.isEmpty())
	      MenuClient.popup(id, event.x, event.y);
	    else {
	      if (selection.size() == 1) {
	        selection.elementAt(0).rightClick(event.x, event.y);
	      }
	    }
	  }
	  
	  private boolean isLeft(MouseEvent event) {
	    return event.button == 1;
	  }

	  @Override
	  public void mouseUp(MouseEvent event) {
	    scale(event);
	    if (mode == MouseMode.NEW_EDGE) new OutboundMessages().checkEdgeCreation(event.x, event.y);
	    if (mode == MouseMode.SELECTED) sendMoveSelectedEvents();
	    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) resizeBottomRight();
	    if (mode == MouseMode.RUBBER_BAND) selectRubberBand();
	    if (movingEdgeEnd()) checkMovedEdge();
	    mode = MouseMode.NONE;
	    redraw();
	  }
	  
	  private void sendMoveSelectedEvents() {
	    for (Selectable selectable : selection)
	      selectable.moveEvent();
	  }
	  
	  private void resizeBottomRight() {
	    int width = lastX - selectedNode.getX();
	    int height = lastY - selectedNode.getY();
	    if (width >= 10 && height >= 10) new OutboundMessages().resizeNode(selectedNode, width, height);
	  }

	  private void selectRubberBand() {
	    int x = Math.min(bandX, lastX);
	    int y = Math.min(bandY, lastY);
	    int width = Math.abs(bandX - lastX);
	    int height = Math.abs(bandY - lastY);
	    Rectangle r = new Rectangle(x, y, width, height);
	    deselectAll();
	    for (Node node : nodes) {
	      if (r.contains(node.getX(), node.getY())) select(node);
	    }
	    for (Edge edge : edges) {
	      for (Waypoint w : edge.getWaypoints()) {
	        if (!w.isStart() && !w.isEnd() && r.contains(w.getX(), w.getY())) select(w);
	      }
	      for (Label l : edge.getLabels()) {
	        if (!selection.contains(l.getParentNode()) &&
	            r.contains(l.getAbsoluteX(), l.getAbsoluteY()) && 
		        r.contains(l.getAbsoluteX() + l.getWidth(), l.getAbsoluteY() + l.getHeight())) select(l);
	      }
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
	          new OutboundMessages().reconnectEdgeSource(selectedEdge, n, p);
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
	          new OutboundMessages().reconnectEdgeTarget(selectedEdge, n, p);
	          reconnected = true;
	        }
	      }
	    }
	  }
	}
  private class MyKeyListener implements KeyListener {

	  @Override
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
	    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '+')) {
	      zoomIn();
	      help();
	      redraw();
		}
	    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '1')) {
	      zoomOne();
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
	    if (e.keyCode == SWT.DEL) {
			sendMessageToDeleteSelection();
		    redraw();
		}
	  }

	  @Override public void keyReleased(KeyEvent event) {}
	  
	  private void layout() {
	    Hashtable<Node, Point2D> positions = new Hashtable<Node, Point2D>();
	    for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
	      for (Node current : nodes) {
	        if (!current.atOrigin() && !selection.contains(current)) {
	          Point2D force = positions.containsKey(current) 
	        		  ? positions.get(current) 
	        		  : Point2D.createRectangular(current.getX(), current.getY());
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
	          Point2D point = positions.get(current);
	          current.move((int) Math.max(0, point.getX()), (int) Math.max(0, point.getY()));
	        }
	      }
	    }
	    for (Node node : positions.keySet())
	      node.moveEvent();
	  }
	  
	  private Point2D nodeRepulsion(Node target, Node source) {
	    int x1 = target.getX();
	    int y1 = target.getY();
	    int x2 = source.getX();
	    int y2 = source.getY();
	    int dx = x1 - x2;
	    int dy = y1 - y2;
	    double magnitude = Math.sqrt((dx * dx) + (dy * dy));
	    double force = REPULSION_CONSTANT / (magnitude * magnitude);
	    if (magnitude < 0.0001)
	      return Point2D.ZERO;
	    else {
	      double angle = Math.atan2(dy, dx);
	      return Point2D.createPolar(force, angle);
	    }
	  }
	  
  }
  private class MyMouseMoveListener implements MouseMoveListener {

	@Override
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
  }
  private class MyPaintListener implements PaintListener {

		@Override
		public void paintControl(PaintEvent event) {
			if (render == 0) {
				GC gc = event.gc;
				paintOn(gc);
			}
		}
	}
	
	/*
	 * Clipboard
	 */
	
  private void copyToClipboard() {
    Clipboard clipboard = new Clipboard(XModeler.getXModeler().getDisplay());
    org.eclipse.swt.widgets.Display d = org.eclipse.swt.widgets.Display.getCurrent();
    double zoomRatio = zoom;//((double) zoom) / 100.0;
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

  /*
   * Selections
   */
  
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
            if (!isShift && !selected(waypoint)) deselectAll();
            select(waypoint);
            magnetize(waypoint); // this will call "doglegs" on the other (magnetic) waypoint 
            dogLegs(waypoint);
            selected = true;
          }
        }
        if (!selected) {
          // See if we are near an end. If so then we go into
          // end reconnection mode...
          if (edge.nearStart(x, y)) {
            deselectAll();
            mode = MouseMode.MOVE_SOURCE;
            selectedEdge = edge;
            selected = true;
          } else if (edge.nearEnd(x, y)) {
            deselectAll();
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
              if (!isShift && !selected(label)) deselectAll();
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
        if (!isShift && !selected(node)) deselectAll();
        select(node);
        selectSelfEdges(node);
        new OutboundMessages().sendSelectedEvent(node);
        selected = true;
      }
      if (!selected && node.atTopLeftCorner(x, y)) {
        deselectAll();
        mode = MouseMode.RESIZE_TOP_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atTopRightCorner(x, y)) {
        deselectAll();
        mode = MouseMode.RESIZE_TOP_RIGHT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomLeftCorner(x, y)) {
        deselectAll();
        mode = MouseMode.RESIZE_BOTTOM_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomRightCorner(x, y)) {
        deselectAll();
        mode = MouseMode.RESIZE_BOTTOM_RIGHT;
        selectedNode = node;
        selected = true;
      }
    }
    if (!selected && !isShift) {
      deselectAll();
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
    deselectAll();
    for (Node node : nodes)
      select(node);
    for (Edge edge : edges) {
      for (Waypoint waypoint : edge.getWaypoints())
        if (waypoint != edge.start() && waypoint != edge.end()) select(waypoint);
      mode = MouseMode.SELECTED;
    }
  }
  
  private void deselect(Selectable s) {
    selection.remove(s);
    s.deselect();
  }
  
  public void deselectAll() {
    for (Selectable selected : selection)
      selected.deselect();
    selection.clear();
    selectedEdge = null;
    selectedNode = null;
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

  private void selectSelfEdges(Node node) {
    for (Edge edge : edges) {
      if (edge.getSourceNode() == node && edge.getTargetNode() == node) {
        for (Waypoint w : edge.getWaypoints()) {
          if (w != edge.start() && w != edge.end()) select(w);
        }
      }
    }
  }
  
  /*
   * Outbound Messages
   */
	public void toggle(String toolId, boolean state) {new OutboundMessages().toggle(toolId, state);}
	public void action(String id) {new OutboundMessages().action(id);}
	
	private class OutboundMessages {

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

		private void sendSelectedEvent(Node node) {
			String id = node.getId();
			EventHandler handler = DiagramClient.theClient().getHandler();
			Message message = handler.newMessage("nodeSelected", 1);
			message.args[0] = new Value(id);
			handler.raiseEvent(message);
		}

		public void toggle(String toolId, boolean state) {
			EventHandler handler = DiagramClient.theClient().getHandler();
			Message message = handler.newMessage("toggle", 3);
			message.args[0] = new Value(id);
			message.args[1] = new Value(toolId);
			message.args[2] = new Value(state);
			handler.raiseEvent(message);
		}

		public void action(String id) {
			EventHandler eventHandler = DiagramClient.theClient().getHandler();
			Message message = eventHandler.newMessage("action", 2);
			message.args[0] = new Value(getId());
			message.args[1] = new Value(id);
			eventHandler.raiseEvent(message);
		}

		private void resizeNode(Node node, int width, int height) {
			EventHandler handler = DiagramClient.theClient().getHandler();
			Message message = handler.newMessage("resizeNode", 3);
			message.args[0] = new Value(node.getId());
			message.args[1] = new Value(width);
			message.args[2] = new Value(height);
			handler.raiseEvent(message);
		}

		private void zoomTo() {
			EventHandler eventHandler = DiagramClient.theClient().getHandler();
			Message message = eventHandler.newMessage("zoomChanged", 2);
			message.args[0] = new Value(getId());
			message.args[1] = new Value((float) zoom);
			eventHandler.raiseEvent(message);
		}
		
		private void deleteComand(String id) {
			EventHandler eventHandler = DiagramClient.theClient().getHandler();
			Message message = eventHandler.newMessage("delete", 1); // deleteIfOfContained
			message.args[0] = new Value(id);
			eventHandler.raiseEvent(message);
		}

		private void checkEdgeCreation(int x, int y) {
			Port port = selectPort(x, y);
			if (port != null) {
				String sourceId = sourcePort.getId();
				String targetId = port.getId();
				Message m = DiagramClient.theClient().getHandler().newMessage("newEdge", 7);
				m.args[0] = new Value(edgeCreationType);
				m.args[1] = new Value(sourceId);
				m.args[2] = new Value(targetId);
				m.args[3] = new Value(firstX);
				m.args[4] = new Value(firstY);
				m.args[5] = new Value(x);
				m.args[6] = new Value(y);
				DiagramClient.theClient().getHandler().raiseEvent(m);
				resetPalette();
			}
		}
	}

}
