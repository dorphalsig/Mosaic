package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import tool.clients.dialogs.notifier.NotificationType;
import tool.clients.dialogs.notifier.NotifierDialog;
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
  int arrow;

  public Label(Edge edge, String id, String text, String pos, int x, int y, boolean editable, boolean underline, boolean condense, int red, int green, int blue, boolean border,  int borderRed, int borderGreen, int borderBlue, String font, int arrow) {
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
    this.arrow = arrow;
  }

  public boolean contains(int x, int y) {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return x >= getAbsoluteX() && y >= getAbsoluteY() && x <= getAbsoluteX() + extent.x && y <= getAbsoluteY() + extent.y;
  }

  Node getParentNode() {
	  if (pos.equals("start"))
	      return edge.getSourceNode();
	  return edge.getTargetNode();
  }
  
  int getAbsoluteX() {
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

  int getAbsoluteY() {
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
	  if(text.length() == 0) return;
      Color c = gc.getForeground();
      gc.setForeground(new Color (org.eclipse.swt.widgets.Display.getCurrent(), borderRed, borderGreen, borderBlue));
      gc.drawRectangle(getAbsoluteX() - 2, getAbsoluteY() - 2, getWidth() + 4, getHeight() + 4);
      gc.setForeground(c);
  }
  
  public void paintArrow(GC gc) {
	  // if no arrow is about to be painted or anything is wrong, simply skip this.
	  if(arrow == 0) return;
	  Point sourcePoint = arrow==1?edge.sourceIntercept():edge.targetIntercept();
	  if(sourcePoint==null) return;
	  Point targetPoint = arrow==-1?edge.sourceIntercept():edge.targetIntercept();
	  if(targetPoint==null) return;
	  // Now we need to find the quadrant where the arrow is pointing to.
	  // There are 4 cases separated by the diagonals: North, West, ... 
	  // The angle will give the clue. 
	  // We need the position of the source and
	  // the target and use atan2 to get it.
	  double angle = Math.atan2(targetPoint.y - sourcePoint.y, targetPoint.x - sourcePoint.x);
	  double angleBaseFour = angle*2/Math.PI; // Now a full circle is 4
	  angleBaseFour += 4.5; angleBaseFour %= 4;// The centre of EAST was 0 before, now 0 to 1 is EAST
	  int quadrant = (int) angleBaseFour; // rounding down and we get one of 4 quadrants 0=EAST,1=SOUTH,...
	  // the Position of the arrow is different for each quadrant:
	  int xArrow;
	  int yArrow;
	  switch(quadrant) {
	  case 0 : // EAST
		  xArrow = getAbsoluteX() + getWidth() + 10;
		  yArrow = getAbsoluteY() + getHeight()/2;
	      break;
	  case 3 : // NORTH
		  xArrow = getAbsoluteX() + getWidth()/2;
		  yArrow = getAbsoluteY() - 10;
	      break;
	  case 2 : // WEST
		  xArrow = getAbsoluteX() - 10;
		  yArrow = getAbsoluteY() + getHeight()/2;
	      break;
	  case 1 : // SOUTH
		  xArrow = getAbsoluteX() + getWidth()/2;
		  yArrow = getAbsoluteY() + getHeight() + 10;
	      break;
	  default : 
		  System.err.println("Arrow quadrant = " + quadrant);
		  return; // Something's gone wrong. No arrow this time...
	  }
	  int SIZE = 7;
	  Point p1 = new Point(
			  xArrow+(int)(1.6 * SIZE * Math.cos(angle)), 
			  yArrow+(int)(1.6 * SIZE * Math.sin(angle)));
	  Point p2 = new Point(
			  xArrow+(int)(SIZE * Math.cos(angle+Math.PI*2/3)), 
			  yArrow+(int)(SIZE * Math.sin(angle+Math.PI*2/3)));
	  Point p3 = new Point(
			  xArrow+(int)(SIZE * Math.cos(angle-Math.PI*2/3)), 
			  yArrow+(int)(SIZE * Math.sin(angle-Math.PI*2/3)));
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
    out.print("arrow='" + arrow + "' ");
    out.print("font='" + font + "'/>");
  }

  public void deselect() {
  }

  public void select() {
  }

//public void doubleClick(GC gc, Diagram diagram, int x2, int y2) {
	  public void doubleClick(GC gc, final Diagram diagram, int mouseX, int mouseY) {
		    if (editable && contains(mouseX, mouseY)) {
		      final org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(diagram.getCanvas(), SWT.BORDER);
		      text.setFont(DiagramClient.diagramFont);
		      text.setText(this.text);
		      Point p = diagram.scaleinv(getAbsoluteX(), getAbsoluteY());
		      text.setLocation(p.x, p.y);
		      text.setSize(getWidth() + 10, getHeight() + 10);
		      text.setVisible(true);
		      text.selectAll();
		      //text.setFocus(); - done delayed to not loose focus on Linux, see below
		      NotifierDialog.notify("Edit Text", "Type text then RET to update.\nType ESC to cancel.", NotificationType.values()[3]);
		      Listener listener = new Listener() {
		        public void handleEvent(Event event) {
		          org.eclipse.swt.widgets.Text t;
		          switch (event.type) {
		          case SWT.FocusOut:
					t = (org.eclipse.swt.widgets.Text) event.widget;
					t.setVisible(false);
					t.dispose();
					diagram.redraw();
		            break;
		          case SWT.Verify:
		            t = (org.eclipse.swt.widgets.Text) event.widget;
		            GC gc = new GC(t);
		            Point size = gc.textExtent(t.getText() + event.text);
		            t.setSize(size.x + 10, getHeight() + 10);
		            break;
		          case SWT.Traverse:
		            switch (event.detail) {
		            case SWT.TRAVERSE_RETURN:
		              t = (org.eclipse.swt.widgets.Text) event.widget;
		              textChangedEvent(t.getText());
		              t.setVisible(false);
		              t.dispose();
		              diagram.redraw();
		              event.doit = false;
		              break;
		            case SWT.TRAVERSE_ESCAPE:
		              t = (org.eclipse.swt.widgets.Text) event.widget;
		              t.setVisible(false);
		              t.dispose();
		              diagram.redraw();
		              event.doit = false;
		              break;
		            }
		            break;
		          }
		        }
		      };
		      text.addListener(SWT.FocusOut, listener);
		      text.addListener(SWT.Verify, listener);
		      text.addListener(SWT.Traverse, listener);

		      XModeler.getXModeler().getDisplay().timerExec(100, new Runnable() {
		          public void run() {
		          	  text.setFocus();
		          }
		      });
		    }
		  }
	
	  public void textChangedEvent(String text) {
		    Message message = DiagramClient.theClient().getHandler().newMessage("textChanged", 2);
		    message.args[0] = new Value(id);
		    message.args[1] = new Value(text);
		    DiagramClient.theClient().getHandler().raiseEvent(message);
		  }
}
