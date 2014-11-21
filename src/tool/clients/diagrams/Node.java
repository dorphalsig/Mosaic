package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Node implements Selectable {

  private static final int SELECTION_GAP = 4;
  private static final int EAR_GAP       = SELECTION_GAP + 2;
  private static final int EAR_LENGTH    = 6;
  String                   id;
  int                      x;
  int                      y;
  int                      width;
  int                      height;
  boolean                  selectable;
  Hashtable<String, Port>  ports         = new Hashtable<String, Port>();
  Vector<Display>          displays      = new Vector<Display>();

  public Node(String id, int x, int y, int width, int height, boolean selectable) {
    super();
    this.id = id;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.selectable = selectable;
  }

  public boolean atOrigin() {
    return getX() == 0 && getY() == 0;
  }

  public boolean contains(Waypoint w) {
    return contains(w.x, w.y);
  }

  public boolean contains(int x, int y) {
    return getX() <= x && getY() <= y && x <= (getX() + getWidth()) && y <= (getY() + getHeight());
  }

  public void editText(String id) {
    for (Display display : displays)
      display.editText(id);
  }

  public int getHeight() {
    return height;
  }

  public String getId() {
    return id;
  }

  public Hashtable<String, Port> getPorts() {
    return ports;
  }

  public int getWidth() {
    return width;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isSelectable() {
    return selectable;
  }

  public void move(String id, int x, int y) {
    if (getId().equals(id))
      move(x, y);
    else {
      for (Display display : displays)
        display.move(id, x, y);
    }
  }

  public void move(int x, int y) {
    int dx = x - getX();
    int dy = y - getY();
    this.x = x;
    this.y = y;
    for (Port port : ports.values()) {
      port.ownerMovedBy(dx, dy);
    }
  }

  public void moveBy(int dx, int dy) {
    x = x + dx;
    y = y + dy;
    for (Port port : ports.values()) {
      port.ownerMovedBy(dx, dy);
    }
  }

  public void moveEvent() {
    Message message = DiagramClient.theClient().getHandler().newMessage("move", 3);
    message.args[0] = new Value(id);
    message.args[1] = new Value(getX());
    message.args[2] = new Value(getY());
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }

  public void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    if (getId().equals(parentId)) {
      Box box = new Box(id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      displays.add(box);
    } else {
      for (Display display : displays) {
        display.newBox(parentId, id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      }
    }
  }

  public void newPort(String id, int x, int y, int width, int height) {
    Port port = new Port(id, x, y, Math.min(width, getWidth()), Math.min(height, getHeight()));
    ports.put(id, port);
  }

  private void newText(String id, String s, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    Text text = new Text(id, s, x, y, editable, underline, italicise, red, green, blue);
    displays.add(text);
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    if (parentId.equals(getId()))
      newText(id, text, x, y, editable, underline, italicise, red, green, blue);
    else for (Display display : displays)
      display.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
  }

  public void paint(GC gc) {
    for (Display display : displays) {
      display.paint(gc, x, y);
    }
  }

  public void paintHover(GC gc, int x, int y, boolean selected) {
    if (contains(x, y)) {
      paintSelectableOutline(gc);
      for (Display display : displays)
        display.paintHover(gc, x, y, getX(), getY());
    }
    if (!selected && !contains(x, y) && atCorner(x, y)) paintResizeHover(gc, x, y);
  }

  public boolean atCorner(int x, int y) {
    return atBottomLeftCorner(x, y) || atBottomRightCorner(x, y) || atTopLeftCorner(x, y) || atTopRightCorner(x, y);
  }

  public boolean atTopLeftCorner(int x, int y) {
    return distance(new Point(getX(), getY()), new Point(x, y)) < 5;
  }

  public boolean atTopRightCorner(int x, int y) {
    return distance(new Point(getX() + getWidth(), getY()), new Point(x, y)) < 5;
  }

  public boolean atBottomLeftCorner(int x, int y) {
    return distance(new Point(getX(), getY() + getHeight()), new Point(x, y)) < 5;
  }

  public boolean atBottomRightCorner(int x, int y) {
    return distance(new Point(getX() + getWidth(), getY() + getHeight()), new Point(x, y)) < 5;
  }

  private double distance(Point p1, Point p2) {
    int dx = p1.x - p2.x;
    int dy = p1.y - p2.y;
    return Math.sqrt((dx * dx) + (dy * dy));
  }

  private void paintResizeHover(GC gc, int x, int y) {
    if (atTopLeftCorner(x, y)) paintResizeTopLeft(gc);
    if (atTopRightCorner(x, y)) paintResizeTopRight(gc);
    if (atBottomLeftCorner(x, y)) paintResizeBottomLeft(gc);
    if (atBottomRightCorner(x, y)) paintResizeBottomRight(gc);
  }

  private void paintResizeTopLeft(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREEN);
    gc.drawLine(getX() - EAR_GAP, getY() - EAR_GAP, getX() + EAR_GAP + EAR_LENGTH, getY() - EAR_GAP);
    gc.drawLine(getX() - EAR_GAP, getY() - EAR_GAP, getX() - EAR_GAP, getY() + EAR_GAP + EAR_LENGTH);
    gc.setForeground(c);
  }

  private void paintResizeBottomLeft(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREEN);
    gc.drawLine(getX() - EAR_GAP, getY() + (getHeight() + EAR_GAP), getX() - EAR_GAP, getY() + (getHeight() - EAR_LENGTH));
    gc.drawLine(getX() - EAR_GAP, getY() + (getHeight() + EAR_GAP), getX() + EAR_GAP + EAR_LENGTH, getY() + (getHeight() + EAR_GAP));
    gc.setForeground(c);
  }

  private void paintResizeBottomRight(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREEN);
    gc.drawLine(getX() + (getWidth() + EAR_GAP), getY() + (getHeight() + EAR_GAP), getX() + (getWidth() + EAR_GAP), getY() + (getHeight() - EAR_LENGTH));
    gc.drawLine(getX() + (getWidth() + EAR_GAP), getY() + (getHeight() + EAR_GAP), getX() + (getWidth() - EAR_LENGTH), getY() + (getHeight() + EAR_GAP));
    gc.setForeground(c);
  }

  private void paintResizeTopRight(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREEN);
    gc.drawLine(getX() + getWidth() + EAR_GAP, getY() - EAR_GAP, getX() + (getWidth() - EAR_LENGTH), getY() - EAR_GAP);
    gc.drawLine(getX() + getWidth() + EAR_GAP, getY() - EAR_GAP, getX() + getWidth() + EAR_GAP, getY() + EAR_GAP + EAR_LENGTH);
    gc.setForeground(c);
  }

  private void paintSelectableOutline(GC gc) {
    Color c = gc.getForeground();
    int width = gc.getLineWidth();
    gc.setLineWidth(1);
    gc.setForeground(XModeler.getXModeler().getDisplay().getSystemColor(SWT.COLOR_RED));
    gc.drawRectangle(getX() - SELECTION_GAP, getY() - SELECTION_GAP, getWidth() + (SELECTION_GAP * 2), getHeight() + (SELECTION_GAP * 2));
    gc.setForeground(c);
    gc.setLineWidth(width);
  }

  public void paintSelected(GC gc) {
    Color c = gc.getForeground();
    int width = gc.getLineWidth();
    gc.setLineWidth(2);
    gc.setForeground(XModeler.getXModeler().getDisplay().getSystemColor(SWT.COLOR_RED));
    gc.drawRectangle(getX() - SELECTION_GAP, getY() - SELECTION_GAP, getWidth() + (SELECTION_GAP * 2), getHeight() + (SELECTION_GAP * 2));
    gc.setForeground(c);
    gc.setLineWidth(width);
  }

  public void resize(String id, int width, int height) {
    if (id.equals(getId())) {
      this.width = width;
      this.height = height;
    } else {
      for (Display display : displays)
        display.resize(id, width, height);
      for (Port port : ports.values())
        port.resize(id, width, height);
    }
  }

  public boolean sameLocation(Node other) {
    return getX() == other.getX() && getY() == other.getY();
  }

  public void setText(String id, String text) {
    for (Display display : displays)
      display.setText(id, text);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return "Node(" + id + "," + x + "," + y + "," + width + "," + height + "," + displays + ")";
  }

  public Display getDisplay(String id) {
    for (Display display : displays)
      if (display.getId().equals(id)) return display;
    return null;
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

  public void doubleClick(GC gc, Diagram diagram, int x, int y) {
    for (Display display : displays) {
      display.doubleClick(gc, diagram, getX(), getY(), x, y);
    }
  }

  public void writeXML(PrintStream out) {
    out.print("<Node id='" + getId() + "' x = '" + getX() + "' y='" + getY() + "' width='" + getWidth() + "' height='" + getHeight() + "' selectable='" + isSelectable() + "'>");
    for (Port port : ports.values())
      port.writeXML(out);
    for (Display display : displays)
      display.writeXML(out);
    out.print("</Node>");

  }

  public int maxY() {
    return getY() + getHeight();
  }

  public int maxX() {
    return getX() + getWidth();
  }

  public void paintPortHover(GC gc, int x, int y) {
    for (Port port : ports.values()) {
      if (port.contains(x - getX(), y - getY())) port.paintHover(gc, getX(), getY());
    }
  }

  public void rightClick(int x, int y) {
    MenuClient.popup(id, x, y);
  }

  public void newMultilineText(String parentId, String id, String text, int x, int y, int width, int height, boolean editable, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, String font) {
    if (getId().equals(parentId)) {
      MultilineText t = new MultilineText(id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
      displays.add(displays.size(), t);
    } else {
      for (Display d : displays)
        d.newMultilineText(parentId, id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
    }
  }
}
