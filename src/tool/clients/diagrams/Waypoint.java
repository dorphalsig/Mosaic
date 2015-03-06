package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import tool.clients.EventHandler;
import tool.clients.menus.MenuClient;
import xos.Message;
import xos.Value;

public class Waypoint implements Selectable {

  static int       SELECTED_SIZE = 6;

  // Limits on the movement of a way-point...

  static final int VERTICAL      = 0;
  static final int HORIZONTAL    = 1;
  static final int ANY           = 2;

  String           id;
  Edge             edge;
  int              x;
  int              y;
  int              limitMovement = ANY;

  public Waypoint(String id, Edge edge, int x, int y) {
    super();
    this.id = id;
    this.edge = edge;
    this.x = x;
    this.y = y;
  }

  public boolean above(Waypoint w) {
    return getY() < w.getY();
  }

  public boolean below(Waypoint w) {
    return getY() > w.getY();
  }

  private boolean canMoveHorizontally() {
    return limitMovement == ANY || limitMovement == HORIZONTAL;
  }

  private boolean canMoveVertically() {
    return limitMovement == ANY || limitMovement == VERTICAL;
  }

  public boolean colocated(Waypoint w) {
    return getX() == w.getX() && getY() == w.getY();
  }

  public void deselect() {
    moveAny();
    EventHandler eventHandler = DiagramClient.theClient().getHandler();
    Message message = eventHandler.newMessage("edgeDeselected", 1);
    message.args[0] = new Value(edge.getId());
    eventHandler.raiseEvent(message);
  }

  public int distance(Waypoint w) {
    int dx = x - w.x;
    int dy = y - w.y;
    return (int) Math.sqrt((dx * dx) + (dy * dy));
  }

  public Edge getEdge() {
    return edge;
  }

  public String getId() {
    return id;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isEnd() {
    return getId().equals("end");
  }

  public boolean isStart() {
    return getId().equals("start");
  }

  public boolean left(Waypoint waypoint) {
    return getX() < waypoint.getX();
  }

  public void move(int x, int y) {
    if (canMoveHorizontally()) this.x = x;
    if (canMoveVertically()) this.y = y;
    if (edge.start() != this && edge.end() != this) moveEvent();
  }

  public void move(String id, int x, int y) {
    if (id.equals(getId())) {
      if (canMoveHorizontally()) this.x = x;
      if (canMoveVertically()) this.y = y;
      edge.checkWaypoints(this);
    }
  }

  public void moveAny() {
    limitMovement = ANY;
  }

  public void moveBy(int dx, int dy) {
    if (canMoveHorizontally()) x = x + dx;
    if (canMoveVertically()) y = y + dy;
    edge.movedBy(this);
  }

  public void moveEvent() {
    Message message = DiagramClient.theClient().getHandler().newMessage("move", 3);
    message.args[0] = new Value(id);
    message.args[1] = new Value(getX());
    message.args[2] = new Value(getY());
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }

  public void moveHorizontally() {
    limitMovement = HORIZONTAL;
  }

  public void moveVertically() {
    limitMovement = VERTICAL;
  }

  public boolean nearTo(int x, int y) {
    int dx = getX() - x;
    int dy = getY() - y;
    return Math.sqrt((dx * dx) + (dy * dy)) < 5;
  }

  public void paintSelected(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.RED);
    gc.drawOval(x - SELECTED_SIZE, y - SELECTED_SIZE, SELECTED_SIZE * 2, SELECTED_SIZE * 2);
    gc.setForeground(c);
    edge.paintOrthogonal(gc, this);
  }

  public boolean right(Waypoint waypoint) {
    return getX() > waypoint.getX();
  }

  public void rightClick(int x, int y) {
    MenuClient.popup(edge.getId(), x, y);
  }

  public void select() {
    EventHandler eventHandler = DiagramClient.theClient().getHandler();
    Message message = eventHandler.newMessage("edgeSelected", 1);
    message.args[0] = new Value(edge.getId());
    eventHandler.raiseEvent(message);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return "W(" + x + "," + y + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Waypoint id='" + getId() + "' index='" + edge.getWaypoints().indexOf(this) + "' x='" + getX() + "' y='" + getY() + "'/>");
  }
}
