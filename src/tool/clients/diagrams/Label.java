package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Label implements Selectable {
  Edge    edge;
  String  id;
  String  text;
  String  pos;
  int     x;
  int     y;
  boolean editable;
  boolean underline;
  boolean condense;
  int     red;
  int     green;
  int     blue;
  boolean border;
  int borderRed; 
  int borderGreen; 
  int borderBlue; 
  String  font;

  public Label(Edge edge, String id, String text, String pos, int x, int y, boolean editable, boolean underline, boolean condense, int red, int green, int blue, boolean border,  int borderRed, int borderGreen, int borderBlue, String font) {
    super();
    this.edge = edge;
    this.id = id;
    this.text = text;
    this.pos = pos == null ? "end" : pos;
    this.x = x;
    this.y = y;
    this.editable = editable;
    this.underline = underline;
    this.condense = condense;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.font = font;
    this.border = border;
    this.borderRed = borderRed;
    this.borderGreen = borderGreen;
    this.borderBlue = borderBlue;
  }

  public boolean contains(int x, int y) {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return x >= getAbsoluteX() && y >= getAbsoluteY() && x <= getAbsoluteX() + extent.x && y <= getAbsoluteY() + extent.y;
  }

  private int getAbsoluteX() {
    Port source = edge.getSourcePort();
    Port target = edge.getTargetPort();
    Node sourceNode = edge.getSourceNode();
    Node targetNode = edge.getTargetNode();
    if (pos.equals("start"))
      return sourceNode.getX() + source.getMidX() + x;
    else if (pos.equals("end"))
      return targetNode.getX() + target.getMidX() + x;
    else {
      int x1 = sourceNode.getX() + source.getMidX() + x;
      int x2 = targetNode.getX() + target.getMidX() + x;
      return x1 + ((x2 - x1) / 2);
    }
  }

  private int getAbsoluteY() {
    Port source = edge.getSourcePort();
    Port target = edge.getTargetPort();
    Node sourceNode = edge.getSourceNode();
    Node targetNode = edge.getTargetNode();
    if (pos.equals("start"))
      return sourceNode.getY() + source.getMidY() + y;
    else if (pos.equals("end"))
      return targetNode.getY() + target.getMidY() + y;
    else {
      int y1 = sourceNode.getY() + source.getMidY() + y;
      int y2 = targetNode.getY() + target.getMidY() + y;
      return y1 + ((y2 - y1) / 2);
    }
  }

  public int getHeight() {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return extent.y;
  }

  public String getId() {
    return id;
  }

  public int getWidth() {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return extent.x;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int maxX() {
    if (pos.equals("start"))
      return edge.start().getX() + x;
    else if (pos.equals("end"))
      return edge.end().getX() + x;
    else return 0;
  }

  public int maxY() {
    if (pos.equals("start"))
      return edge.start().getY() + y;
    else if (pos.equals("end"))
      return edge.end().getY() + y;
    else return 0;
  }

  public void move(String id, int x, int y) {
    if (id.equals(getId())) {
      this.x = x;
      this.y = y;
    }
  }

  public void moveBy(int dx, int dy) {
    x = x + dx;
    y = y + dy;
  }

  public void moveEvent() {
    Message message = DiagramClient.theClient().getHandler().newMessage("move", 3);
    message.args[0] = new Value(id);
    message.args[1] = new Value(getX());
    message.args[2] = new Value(getY());
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }

  public void paint(GC gc) {
    gc.setFont(DiagramClient.diagramFont);
    gc.drawText(text, getAbsoluteX(), getAbsoluteY());
    paintBorder(gc);
    paintArrow(gc);
  }

  public void paintBorder(GC gc) {
	  if(!border) return;
      Color c = gc.getForeground();
      gc.setForeground(new Color (org.eclipse.swt.widgets.Display.getCurrent(), borderRed, borderGreen, borderBlue));
      gc.drawRectangle(getAbsoluteX() - 2, getAbsoluteY() - 2, getWidth() + 4, getHeight() + 4);
      gc.setForeground(c);
  }
  
  public void paintArrow(GC gc) {
	  int dir = "source".equals(text)?-1:"target".equals(text)?1:0;
//	  if("A".equals(text)) dir = 1;
	  if(dir == 0) return;
	  Point p = dir==1?edge.sourceIntercept():edge.targetIntercept();
	  if(p==null) return;
	  int x0 = getAbsoluteX() - 10;
	  int y0 = getAbsoluteY() + 8;
	  double angle = Math.atan2(p.y - y0, p.x - x0);
	  int SIZE = 6;
	  Point p1 = new Point(
			  x0+(int)(1.7 * SIZE * Math.cos(angle)), 
			  y0+(int)(1.7 * SIZE * Math.sin(angle)));
	  Point p2 = new Point(
			  x0+(int)(SIZE * Math.cos(angle+Math.PI*2/3)), 
			  y0+(int)(SIZE * Math.sin(angle+Math.PI*2/3)));
	  Point p3 = new Point(
			  x0+(int)(SIZE * Math.cos(angle-Math.PI*2/3)), 
			  y0+(int)(SIZE * Math.sin(angle-Math.PI*2/3)));
//	  System.err.println((angle * 180 / Math.PI) + "°");
	  Color c = gc.getBackground();
	  gc.setBackground(new Color(c.getDevice(), 0, 0, 0));
	  gc.fillPolygon(new int[]{p1.x, p1.y, p2.x, p2.y, p3.x, p3.y});
	  gc.setBackground(c);
  }
  
  public void paintHover(GC gc, int x, int y) {
	    if (contains(x, y)) {
	      Color c = gc.getForeground();
	      gc.setForeground(Diagram.GREY);
	      gc.drawRectangle(getAbsoluteX() - 2, getAbsoluteY() - 2, getWidth() + 4, getHeight() + 4);
	      Point source = edge.sourceIntercept();
	      Point target = edge.targetIntercept();
	      if (source != null && target != null) {
	        int startX = pos.equals("start") ? source.x : pos.equals("end") ? target.x : (target.x + source.x) / 2;
	        int startY = pos.equals("start") ? source.y : pos.equals("end") ? target.y : (target.y + source.y) / 2;
	        gc.drawLine(startX, startY, getAbsoluteX() - 2, getAbsoluteY() - 2);
	      }
	      gc.setForeground(c);
	    }
	  }

  public void paintSelected(GC gc) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.RED);
    gc.drawRectangle(getAbsoluteX() - 2, getAbsoluteY() - 2, getWidth() + 4, getHeight() + 4);
    Point source = edge.sourceIntercept();
    Point target = edge.targetIntercept();
    if (source != null && target != null) {
      int startX = pos.equals("start") ? source.x : pos.equals("end") ? target.x : (target.x + source.x) / 2;
      int startY = pos.equals("start") ? source.y : pos.equals("end") ? target.y : (target.y + source.y) / 2;
      gc.drawLine(startX, startY, getAbsoluteX() - 2, getAbsoluteY() - 2);
      gc.setForeground(c);
    }
  }

  public void rightClick(int x, int y) {
    MenuClient.popup(id, x, y);
  }

  public void setText(String id, String text) {
    if (getId().equals(id)) this.text = text;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String toString() {
    return "L(" + text + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Label id='" + getId() + "' ");
    out.print("text='" + XModeler.encodeXmlAttribute(text) + "' ");
    out.print("pos='" + pos + "' ");
    out.print("x='" + x + "' ");
    out.print("y='" + y + "' ");
    out.print("editable='" + editable + "' ");
    out.print("underline='" + underline + "' ");
    out.print("condense='" + condense + "' ");
    out.print("red='" + red + "' ");
    out.print("green='" + green + "' ");
    out.print("blue='" + blue + "' ");
    out.print("border='" + border + "' ");
    out.print("borderRed='" + borderRed + "' ");
    out.print("borderGreen='" + borderGreen + "' ");
    out.print("borderBlue='" + borderBlue + "' ");
    out.print("font='" + font + "'/>");
  }

  public void deselect() {
  }

  public void select() {
  }
}
