package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Text implements Display {
  String  id;
  String  text;
  int     x;
  int     y;
  boolean editable;
  boolean underline;
  boolean italicise;
  int     red;
  int     green;
  int     blue;

  public Text(String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    super();
    this.id = id;
    this.text = text;
    this.x = x;
    this.y = y;
    this.editable = editable;
    this.underline = underline;
    this.italicise = italicise;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public void writeXML(PrintStream out) {
    out.print("<Text ");
    out.print("id='" + getId() + "' ");
    out.print("text='" + XModeler.encodeXmlAttribute(getText()) + "' ");
    out.print("x='" + getX() + "' ");
    out.print("y='" + getY() + "' ");
    out.print("editable='" + isEditable() + "' ");
    out.print("underline='" + isUnderline() + "' ");
    out.print("italicise='" + isItalicise() + "' ");
    out.print("red='" + getRed() + "' ");
    out.print("green='" + getGreen() + "' ");
    out.print("blue='" + getBlue() + "'/>");
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isEditable() {
    return editable;
  }

  public boolean isUnderline() {
    return underline;
  }

  public boolean isItalicise() {
    return italicise;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }

  public void paint(GC gc, int x, int y) {
    gc.setFont(DiagramClient.diagramFont);
    gc.drawText(text, x + getX(), y + getY());
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
  }

  public void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
  }

  public String toString() {
    return "Text(" + id + "," + x + "," + y + "," + text + ")";
  }

  public void resize(String id, int width, int height) {
  }

  public void editText(String id) {
    if (id.equals(getId())) editable = true;
  }

  public void setText(String id, String text) {
    if (id.equals(getId())) this.text = text;
  }

  public void move(String id, int x, int y) {
    if (getId().equals(id)) {
      this.x = x;
      this.y = y;
    }
  }

  public int getWidth() {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return extent.x;
  }

  public int getHeight() {
    Point extent = DiagramClient.theClient().textDimension(text, DiagramClient.diagramFont);
    return extent.y;
  }

  public boolean contains(int x, int y) {
    return x >= getX() && y >= getY() && x <= getX() + getWidth() && y <= getY() + getHeight();
  }

  public void paintHover(GC gc, int x, int y, int dx, int dy) {
    if (editable && contains(x - dx, y - dy)) paintSelectableOutline(gc, dx, dy);
  }

  private void paintSelectableOutline(GC gc, int dx, int dy) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREY);
    gc.drawRectangle(dx + getX(), dy + getY(), getWidth(), getHeight());
    gc.setForeground(c);
  }

  public void remove(String id) {
  }

  public void doubleClick(GC gc, final Diagram diagram, int dx, int dy, int mouseX, int mouseY) {
    if (editable && contains(mouseX - dx, mouseY - dy)) {
      final org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(diagram.getCanvas(), SWT.NONE);
      text.setFont(DiagramClient.diagramFont);
      text.setText(this.text);
      text.setLocation(dx + getX(), dy + getY());
      text.setSize(getWidth(), getHeight());
      text.setVisible(true);
      text.setFocus();
      Listener listener = new Listener() {
        public void handleEvent(Event event) {
          org.eclipse.swt.widgets.Text t;
          switch (event.type) {
          case SWT.FocusOut:
            t = (org.eclipse.swt.widgets.Text) event.widget;
            textChangedEvent(t.getText());
            t.setVisible(false);
            t.dispose();
            diagram.redraw();
            break;
          case SWT.Verify:
            t = (org.eclipse.swt.widgets.Text) event.widget;
            GC gc = new GC(t);
            Point size = gc.textExtent(t.getText() + event.text);
            t.setSize(size.x, size.y);
            break;
          case SWT.Traverse:
            switch (event.detail) {
            case SWT.TRAVERSE_RETURN:
            case SWT.TRAVERSE_ESCAPE:
              t = (org.eclipse.swt.widgets.Text) event.widget;
              textChangedEvent(t.getText());
              t.setVisible(false);
              t.dispose();
              diagram.redraw();
              event.doit = false;
            }
            break;
          }
        }
      };
      text.addListener(SWT.FocusOut, listener);
      text.addListener(SWT.Verify, listener);
      text.addListener(SWT.Traverse, listener);
    }
  }

  public void textChangedEvent(String text) {
    Message message = DiagramClient.theClient().getHandler().newMessage("textChanged", 2);
    message.args[0] = new Value(id);
    message.args[1] = new Value(text);
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }
}
