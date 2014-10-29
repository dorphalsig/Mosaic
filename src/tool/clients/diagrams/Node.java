package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Node implements Selectable {

  String                     id;
  int                        x;
  int                        y;
  int                        width;
  int                        height;
  boolean                    selectable;
  Hashtable<String, Port>    ports    = new Hashtable<String, Port>();
  Hashtable<String, Display> displays = new Hashtable<String, Display>();

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
    for (Display display : displays.values())
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
      for (Display display : displays.values())
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
      displays.put(id, box);
    } else {
      for (Display display : displays.values()) {
        display.newBox(parentId, id, x, y, width, height, curve, top, right, bottom, left, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue);
      }
    }
  }

  public void newPort(String id, int x, int y, int width, int height) {
    Port port = new Port(id, x, y, width, height);
    ports.put(id, port);
  }

  private void newText(String id, String s, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    Text text = new Text(id, s, x, y, editable, underline, italicise, red, green, blue);
    displays.put(id, text);
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    if (parentId.equals(getId()))
      newText(id, text, x, y, editable, underline, italicise, red, green, blue);
    else for (Display display : displays.values())
      display.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
  }

  public void paint(GC gc) {
    for (Display display : displays.values()) {
      display.paint(gc, x, y);
    }
  }

  public void paintHover(GC gc, int x, int y) {
    if (contains(x, y)) paintSelectableOutline(gc);
    for (Display display : displays.values())
      display.paintHover(gc, x, y, getX(), getY());
  }

  private void paintSelectableOutline(GC gc) {
    Color c = gc.getForeground();
    int width = gc.getLineWidth();
    gc.setLineWidth(1);
    gc.setForeground(XModeler.getXModeler().getDisplay().getSystemColor(SWT.COLOR_RED));
    gc.drawRectangle(getX() - 4, getY() - 4, getWidth() + 8, getHeight() + 8);
    gc.setForeground(c);
    gc.setLineWidth(width);
  }

  public void paintSelected(GC gc) {
    Color c = gc.getForeground();
    int width = gc.getLineWidth();
    gc.setLineWidth(2);
    gc.setForeground(XModeler.getXModeler().getDisplay().getSystemColor(SWT.COLOR_RED));
    gc.drawRectangle(getX() - 4, getY() - 4, getWidth() + 8, getHeight() + 8);
    gc.setForeground(c);
    gc.setLineWidth(width);
  }

  public void resize(String id, int width, int height) {
    if (id.equals(getId())) {
      this.width = width;
      this.height = height;
    } else {
      for (Display display : displays.values())
        display.resize(id, width, height);
    }
  }

  public boolean sameLocation(Node other) {
    return getX() == other.getX() && getY() == other.getY();
  }

  public void setText(String id, String text) {
    for (Display display : displays.values())
      display.setText(id, text);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return "Node(" + id + "," + x + "," + y + "," + width + "," + height + "," + displays.values() + ")";
  }

  public void remove(String id) {
    if (displays.containsKey(id)) {
      Display display = displays.get(id);
      displays.remove(id);
    } else {
      for (Display display : displays.values()) {
        display.remove(id);
      }
    }
  }

  public void doubleClick(GC gc, Diagram diagram, int x, int y) {
    for (Display display : displays.values()) {
      display.doubleClick(gc, diagram, getX(), getY(), x, y);
    }
  }

  public void writeXML(PrintStream out) {
    out.print("<Node id='" + getId() + "' x = '" + getX() + "' y='" + getY() + "' width='" + getWidth() + "' height='" + getHeight() + "' selectable='" + isSelectable() + "'>");
    for (Port port : ports.values())
      port.writeXML(out);
    for (Display display : displays.values())
      display.writeXML(out);
    out.print("</Node>");

  }

  public int maxY() {
    return getY() + getHeight();
  }

  public int maxX() {
    return getX() + getWidth();
  }
}
