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
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;

import tool.clients.EventHandler;
import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Diagram implements MouseListener, PaintListener, MouseMoveListener, KeyListener {

  enum MouseMode {
    NONE, SELECTED, NEW_EDGE, DOUBLE_CLICK, MOVE_TARGET, MOVE_SOURCE, RESIZE_TOP_LEFT, RESIZE_TOP_RIGHT, RESIZE_BOTTOM_LEFT, RESIZE_BOTTOM_RIGHT
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

  static final Color      RED                    = new Color(XModeler.getXModeler().getDisplay(), 255, 0, 0);
  static final Color      GREY                   = new Color(XModeler.getXModeler().getDisplay(), 192, 192, 192);
  static final Color      WHITE                  = new Color(XModeler.getXModeler().getDisplay(), 255, 255, 255);
  static final Color      GREEN                  = new Color(XModeler.getXModeler().getDisplay(), 0, 170, 0);
  static final Color      BLACK                  = new Color(XModeler.getXModeler().getDisplay(), 0, 0, 0);

  static double           ATTRACTION_CONSTANT    = 0.1;
  static int              REPULSION_CONSTANT     = 700;
  static double           DEFAULT_DAMPING        = 0.5;
  static int              DEFAULT_SPRING_LENGTH  = 200;
  static int              DEFAULT_MAX_ITERATIONS = 200;

  static int              RIGHT_BUTTON           = 3;
  static int              MAX_ZOOM               = 200;
  static int              MIN_ZOOM               = 20;
  static int              ZOOM_INC               = 10;

  Color                   diagramBackgroundColor = WHITE;
  Hashtable<String, Node> nodes                  = new Hashtable<String, Node>();
  Hashtable<String, Edge> edges                  = new Hashtable<String, Edge>();
  Vector<Selectable>      selection              = new Vector<Selectable>();
  Transform               transform              = new Transform(org.eclipse.swt.widgets.Display.getCurrent());
  String                  id;
  SashForm                container;
  Palette                 palette;
  Canvas                  canvas;
  ScrolledComposite       scroller;
  Edge                    selectedEdge           = null;
  Node                    selectedNode           = null;
  int                     render                 = 0;
  int                     firstX;
  int                     firstY;
  int                     lastX;
  int                     lastY;
  Port                    sourcePort;
  MouseMode               mode                   = MouseMode.NONE;
  int                     zoom                   = 100;

  public Diagram(String id, CTabFolder parent) {
    container = new SashForm(parent, SWT.HORIZONTAL | SWT.BORDER);
    palette = new Palette(container);
    scroller = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
    scroller.setExpandHorizontal(true);
    scroller.setExpandVertical(true);
    canvas = new Canvas(scroller, SWT.BORDER);
    canvas.setBackground(diagramBackgroundColor);
    canvas.addMouseListener(this);
    canvas.addPaintListener(this);
    canvas.addMouseMoveListener(this);
    canvas.addKeyListener(this);
    container.setWeights(new int[] { 1, 5 });
    scroller.setContent(canvas);
    this.id = id;
  }

  private void checkEdgeCreation(int x, int y) {
    Port port = selectPort(x, y);
    if (port != null) {
      String sourceId = sourcePort.getId();
      String targetId = port.getId();
      String type = palette.getCurrentTool();
      palette.reset();
      Message m = DiagramClient.theClient().getHandler().newMessage("newEdge", 3);
      m.args[0] = new Value(type);
      m.args[1] = new Value(sourceId);
      m.args[2] = new Value(targetId);
      DiagramClient.theClient().getHandler().raiseEvent(m);
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
    for (Node n : nodes.values()) {
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
    for (Node n : nodes.values()) {
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
    Image image = new Image(XModeler.getXModeler().getDisplay().getCurrent(), maxWidth() + 50, maxHeight() + 50);
    GC gc = new GC(image);
    paintOn(gc);
    ImageTransfer imageTransfer = ImageTransfer.getInstance();
    clipboard.setContents(new Object[] { image.getImageData() }, new Transfer[] { imageTransfer }, DND.CLIPBOARD | DND.SELECTION_CLIPBOARD);
    clipboard.dispose();
    image.dispose();
    gc.dispose();
  }

  public void delete(String id) {
    for (Node node : nodes.values()) {
      node.remove(id);
    }
    if (nodes.containsKey(id)) {
      Node node = nodes.get(id);
      nodes.remove(id);
      selection.remove(node);
    }
    for (Edge edge : edges.values()) {
      Label label = edge.getLabel(id);
      if (label != null) {
        edge.getLabels().remove(label);
        selection.remove(label);
      }
      Waypoint waypoint = edge.getWaypoint(id);
      if (waypoint != null) {
        edge.getWaypoints().remove(waypoint);
        selection.remove(waypoint);
      }
    }
    if (edges.containsKey(id)) {
      Edge edge = edges.get(id);
      if (edge != null) edges.remove(id);
    }
    redraw();
  }

  private void deselect() {
    selection.clear();
    selectedEdge = null;
    selectedNode = null;
  }

  public void editText(String id) {
    for (Node node : nodes.values())
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

  public Edge getEdge(Label label) {
    for (Edge edge : edges.values())
      if (edge.getLabels().contains(label)) return edge;
    return null;
  }

  public Hashtable<String, Edge> getEdges() {
    return edges;
  }

  public String getId() {
    return id;
  }

  Node getNode(Port port) {
    for (Node node : nodes.values())
      if (node.getPorts().values().contains(port)) return node;
    return null;
  }

  public Hashtable<String, Node> getNodes() {
    return nodes;
  }

  public Palette getPalette() {
    return palette;
  }

  private Port getPort(String id) {
    for (Node node : nodes.values())
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
      for (Node node : nodes.values()) {
        node.doubleClick(gc, this, lastX, lastY);
      }
    }
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
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '=')) {
      zoomIn();
      redraw();
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '-')) {
      zoomOut();
      redraw();
    }
  }

  public void keyReleased(KeyEvent event) {
  }

  private void layout() {
    Hashtable<Node, Polar> positions = new Hashtable<Node, Polar>();
    for (int i = 0; i < DEFAULT_MAX_ITERATIONS; i++) {
      for (Node current : nodes.values()) {
        if (!current.atOrigin() && !selection.contains(current)) {
          Polar force = positions.containsKey(current) ? positions.get(current) : new Rectangular(current.getX(), current.getY()).toPolar();
          for (Node other : nodes.values()) {
            if (!current.sameLocation(other)) {
              force = force.add(nodeRepulsion(current, other));
            }
            for (Edge edge : edges.values()) {
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
      for (Node current : nodes.values()) {
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
    if (palette.getCurrentTool().equals("Select")) {
      deselect();
      select(event.x, event.y);
      lastX = event.x;
      lastY = event.y;
      redraw();
    } else if (palette.currentToolIsEdge()) {
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
    } else {
      String type = palette.getCurrentTool();
      palette.reset();
      DiagramClient.theClient().newNode(type, id, event.x, event.y);
    }
  }

  private int maxHeight() {
    int maxHeight = 0;
    for (Node node : nodes.values())
      maxHeight = Math.max(maxHeight, node.maxY());
    return maxHeight;
  }

  private int maxWidth() {
    int maxWidth = 0;
    for (Node node : nodes.values())
      maxWidth = Math.max(maxWidth, node.maxX());
    return maxWidth;
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
  }

  private void resizeNode(Node node, int width, int height) {
    EventHandler handler = DiagramClient.theClient().getHandler();
    Message message = handler.newMessage("resizeNode", 3);
    message.args[0] = new Value(node.getId());
    message.args[1] = new Value(width);
    message.args[2] = new Value(height);
    handler.raiseEvent(message);
  }

  public void mouseUp(MouseEvent event) {
    scale(event);
    if (mode == MouseMode.NEW_EDGE) checkEdgeCreation(event.x, event.y);
    if (mode == MouseMode.SELECTED) sendMoveSelectedEvents();
    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) resizeBottomRight();
    if (movingEdgeEnd()) checkMovedEdge();
    mode = MouseMode.NONE;
    redraw();
  }

  private void resizeBottomRight() {
    int width = lastX - selectedNode.getX();
    int height = lastY - selectedNode.getY();
    if (width >= 10 && height >= 10) resizeNode(selectedNode, width, height);
  }

  public void move(String id, int x, int y) {
    for (Node node : nodes.values())
      node.move(id, x, y);
    for (Edge edge : edges.values())
      edge.move(id, x, y);
    redraw();
  }

  private boolean movingEdgeEnd() {
    return mode == MouseMode.MOVE_SOURCE || mode == MouseMode.MOVE_TARGET;
  }

  public void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    for (Node node : nodes.values())
      node.newBox(parentId, id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
    redraw();
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
        edges.put(id, edge);
        redraw();
      } else System.out.println("cannot find target port " + targetId);
    } else System.out.println("cannot find source port " + sourceId);
  }

  public void newGroup(String name) {
    palette.newGroup(name);
    container.layout();
  }

  public void newNode(String id, int x, int y, int width, int height, boolean selectable) {
    Node node = new Node(id, x, y, width, height, selectable);
    nodes.put(id, node);
    deselect();
    selection.add(node);
  }

  public void newPort(String nodeId, String id, int x, int y, int width, int height) {
    for (Node node : nodes.values())
      if (node.getId().equals(nodeId)) node.newPort(id, x, y, width, height);
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    for (Node node : nodes.values())
      node.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
    redraw();
  }

  public void newTool(String group, String label, String toolId, boolean isEdge, String icon) {
    palette.newTool(group, label, toolId, isEdge, icon);
    container.layout();
  }

  public void newWaypoint(String parentId, String id, int index, int x, int y) {
    for (Edge edge : edges.values()) {
      Waypoint w = edge.newWaypoint(parentId, id, index, x, y);
      if (w != null) {
        deselect();
        selection.add(w);
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

  private void paintEdgeAlignment(GC gc) {
    for (Edge edge1 : edges.values()) {
      for (Edge edge2 : edges.values()) {
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
    for (Edge edge : edges.values()) {
      if ((mode != MouseMode.MOVE_SOURCE && mode != MouseMode.MOVE_TARGET) || (selectedEdge != edge)) {
        edge.paint(gc);
      } else {
        if (mode == MouseMode.MOVE_SOURCE) {
          edge.paintSourceMoving(gc, lastX, lastY);
        } else edge.paintTargetMoving(gc, lastX, lastY);
      }
    }
  }

  private void paintHover(GC gc) {
    if (!movingEdgeEnd()) {
      for (Node node : nodes.values())
        node.paintHover(gc, lastX, lastY, selection.contains(node));
      for (Edge edge : edges.values())
        edge.paintHover(gc, lastX, lastY);
    }
    if (movingEdgeEnd()) {
      for (Node node : nodes.values())
        node.paintPortHover(gc, lastX, lastY);
    }
  }

  private void paintNewEdge(GC gc) {
    if (mode == MouseMode.NEW_EDGE) {
      gc.drawLine(firstX, firstY, lastX, lastY);
    }
  }

  private void paintNodeAlignment(GC gc) {
    for (Node node1 : nodes.values())
      for (Node node2 : nodes.values())
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
    for (Node node : nodes.values())
      node.paint(gc);
  }

  private void paintOn(GC gc) {
    gc.setAntialias(SWT.ON);
    gc.setTextAntialias(SWT.ON);
    gc.setInterpolation(SWT.HIGH);
    gc.setAdvanced(true);
    gc.setTransform(transform);
    clear(gc);
    paintNewEdge(gc);
    paintResizing(gc);
    paintEdges(gc);
    paintAlignment(gc);
    paintNodes(gc);
    paintHover(gc);
    paintSelected(gc);
    handleDoubleClick(gc);
  }

  private void paintResizing(GC gc) {
    if (mode == MouseMode.RESIZE_BOTTOM_RIGHT) {
      int width = lastX - selectedNode.getX();
      int height = lastY - selectedNode.getY();
      if (width >= 10 && height >= 10) gc.drawRectangle(selectedNode.getX(), selectedNode.getY(), width, height);
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
        }
      });
    }
  }

  public void renderOff() {
    render++;
  }

  public void renderOn() {
    render = Math.max(render - 1, 0);
    redraw();
  }

  public void resize(String id, int width, int height) {
    for (Node node : nodes.values())
      node.resize(id, width, height);
    redraw();
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

  private void scale(MouseEvent event) {
    float[] points = new float[] { (float) event.x, (float) event.y };
    transform.invert();
    transform.transform(points);
    transform.invert();
    event.x = (int) points[0];
    event.y = (int) points[1];
  }

  private void select(int x, int y) {
    boolean selected = false;
    if (!selected) {
      for (Edge edge : edges.values()) {
        for (Waypoint waypoint : edge.getWaypoints()) {
          // Try the existing waypoints. Be careful to exclude the
          // dummy start and end waypoints...
          if (waypoint != edge.start() && waypoint != edge.end() && waypoint.nearTo(x, y)) {
            mode = MouseMode.SELECTED;
            selection.add(waypoint);
            selected = true;
          }
        }
        if (!selected) {
          // See if we are near an end. If so then we go into
          // end reconnection mode...
          if (edge.nearStart(x, y)) {
            mode = MouseMode.MOVE_SOURCE;
            selectedEdge = edge;
            selected = true;
          } else if (edge.nearEnd(x, y)) {
            mode = MouseMode.MOVE_TARGET;
            selectedEdge = edge;
            selected = true;
          }
        }
        if (!selected) {
          // See if we are sufficiently near an edge to add a new waypoint...
          selected = edge.newWaypoint(x, y);
          for (Label label : edge.getLabels()) {
            // See if we are selecting an edge...
            if (!selected && label.contains(x, y)) {
              mode = MouseMode.SELECTED;
              selection.add(label);
              selected = true;
            }
          }
        }
      }
    }
    for (Node node : nodes.values()) {
      if (!selected && node.contains(x, y)) {
        // If all else fails we might be selecting a node.
        // Trying nodes last allows the other elements behind
        // nodes to be selected...
        mode = MouseMode.SELECTED;
        selection.add(node);
        selected = true;
      }
      if (!selected && node.atTopLeftCorner(x, y)) {
        mode = MouseMode.RESIZE_TOP_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atTopRightCorner(x, y)) {
        mode = MouseMode.RESIZE_TOP_RIGHT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomLeftCorner(x, y)) {
        mode = MouseMode.RESIZE_BOTTOM_LEFT;
        selectedNode = node;
        selected = true;
      }
      if (!selected && node.atBottomRightCorner(x, y)) {
        mode = MouseMode.RESIZE_BOTTOM_RIGHT;
        selectedNode = node;
        selected = true;
      }
    }
  }

  private void selectAll() {
    selection.clear();
    for (Node node : nodes.values())
      selection.add(node);
    for (Edge edge : edges.values()) {
      for (Waypoint waypoint : edge.getWaypoints())
        if (waypoint != edge.start() && waypoint != edge.end()) selection.add(waypoint);
      mode = MouseMode.SELECTED;
    }
  }

  private Port selectPort(int x, int y) {
    for (Node node : nodes.values()) {
      for (Port port : node.getPorts().values()) {
        if (port.contains(x - node.getX(), y - node.getY())) { return port; }
      }
    }
    return null;
  }

  private void sendMoveSelectedEvents() {
    for (Selectable selectable : selection)
      selectable.moveEvent();
  }

  public void setEdgeSource(String edgeId, String portId) {
    for (Edge edge : edges.values()) {
      if (edge.getId().equals(edgeId)) {
        for (Node node : nodes.values()) {
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
    for (Edge edge : edges.values()) {
      if (edge.getId().equals(edgeId)) {
        for (Node node : nodes.values()) {
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

  public void setText(String id, String text) {
    for (Node node : nodes.values())
      node.setText(id, text);
    for (Edge edge : edges.values())
      edge.setText(id, text);
    redraw();
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
    transform = new Transform(org.eclipse.swt.widgets.Display.getCurrent());
    transform.scale((float) (zoom / 100.0), (float) (zoom / 100.0));
  }

  public String toString() {
    return "Diagram(" + nodes.values() + "," + edges.values() + ")";
  }

  public void writeXML(String label, PrintStream out) {
    out.print("<Diagram id='" + getId() + "' label='" + label + "' zoom='" + getZoom() + "'>");
    palette.writeXML(out);
    for (Node node : nodes.values())
      node.writeXML(out);
    for (Edge edge : edges.values())
      edge.writeXML(out);
    out.print("</Diagram>");
  }

  private void zoomIn() {
    if (getZoom() < MAX_ZOOM) {
      setZoom(getZoom() + ZOOM_INC);
      zoomTo();
    }
  }

  private void zoomOut() {
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
}
