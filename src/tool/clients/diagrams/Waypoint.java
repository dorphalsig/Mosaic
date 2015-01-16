package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import tool.clients.menus.MenuClient;
import xos.Message;
import xos.Value;

public class Waypoint implements Selectable {

  static int SELECTED_SIZE = 6;

  String     id;
  Edge       edge;
  int        x;
  int        y;

  public Waypoint(String id, Edge edge, int x, int y) {
    super();
    this.id = id;
    this.edge = edge;
    this.x = x;
    this.y = y;
  }

  public boolean colocated(Waypoint w) {
    return getX() == w.getX() && getY() == w.getY();
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

  public void move(String id, int x, int y) {
    if (id.equals(getId())) {
      this.x = x;
      this.y = y;
      edge.checkWaypoints(this);
    }
  }

  public void moveBy(int dx, int dy) {
    x = x + dx;
    y = y + dy;
    edge.movedBy(this);
  }

  public void moveEvent() {
    Message message = DiagramClient.theClient().getHandler().newMessage("move", 3);
    message.args[0] = new Value(id);
    message.args[1] = new Value(getX());
    message.args[2] = new Value(getY());
    DiagramClient.theClient().getHandler().raiseEvent(message);
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

  public void rightClick(int x, int y) {
    MenuClient.popup(edge.getId(), x, y);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return "Waypoint(" + x + "," + y + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Waypoint id='" + getId() + "' index='" + edge.getWaypoints().indexOf(this) + "' x='" + getX() + "' y='" + getY() + "'/>");
  }
}