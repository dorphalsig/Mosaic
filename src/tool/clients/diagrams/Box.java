package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

import tool.xmodeler.XModeler;

public class Box implements Display {
  String                     id;
  int                        x;
  int                        y;
  int                        width;
  int                        height;
  int                        curve;
  boolean                    top;
  boolean                    right;
  boolean                    bottom;
  boolean                    left;
  int                        lineRed;
  int                        lineGreen;
  int                        lineBlue;
  int                        fillRed;
  int                        fillGreen;
  int                        fillBlue;
  Hashtable<String, Display> displays = new Hashtable<String, Display>();

  public Box(String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
    super();
    this.id = id;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.curve = curve;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.left = left;
    this.lineRed = lineRed == -1 ? 0 : lineRed;
    this.lineGreen = lineGreen == -1 ? 0 : lineGreen;
    this.lineBlue = lineBlue == -1 ? 0 : lineBlue;
    this.fillRed = fillRed == -1 ? 255 : fillRed;
    this.fillGreen = fillGreen == -1 ? 255 : fillGreen;
    this.fillBlue = fillBlue == -1 ? 255 : fillBlue;
  }

  public void doubleClick(GC gc, Diagram diagram, int dx, int dy, int mouseX, int mouseY) {
    for (Display display : displays.values()) {
      display.doubleClick(gc, diagram, dx + getX(), dy + getY(), mouseX, mouseY);
    }
  }

  public void editText(String id) {
    for (Display display : displays.values())
      display.editText(id);
  }

  public int getCurve() {
    return curve;
  }

  public int getFillBlue() {
    return fillBlue;
  }

  public int getFillGreen() {
    return fillGreen;
  }

  public int getFillRed() {
    return fillRed;
  }

  public int getHeight() {
    return height;
  }

  public String getId() {
    return id;
  }

  public int getLineBlue() {
    return lineBlue;
  }

  public int getLineGreen() {
    return lineGreen;
  }

  public int getLineRed() {
    return lineRed;
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

  public boolean isBottom() {
    return bottom;
  }

  public boolean isLeft() {
    return left;
  }

  public boolean isRight() {
    return right;
  }

  public boolean isTop() {
    return top;
  }

  public void move(String id, int x, int y) {
    if (getId().equals(id)) {
      this.x = x;
      this.y = y;
    } else {
      for (Display display : displays.values())
        display.move(id, x, y);
    }
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

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    if (parentId.equals(getId())) {
      Text t = new Text(id, text, x, y, editable, underline, italicise, red, green, blue);
      displays.put(id, t);
    } else for (Display display : displays.values())
      display.newText(parentId, id, text, x, y, editable, underline, italicise, red, green, blue);
  }

  public void paint(GC gc, int x, int y) {
    if (width > 0 && height > 0) {
      Color fillColor = gc.getBackground();
      gc.setBackground(new Color(XModeler.getXModeler().getDisplay(), getFillRed(), getFillGreen(), getFillBlue()));
      gc.fillRectangle(x + getX(), y + getY(), width, height);
      gc.setBackground(fillColor);
      for (Display display : displays.values())
        display.paint(gc, x + getX(), y + getY());
      Color lineColor = gc.getForeground();
      gc.setForeground(new Color(XModeler.getXModeler().getDisplay(), getLineRed(), getLineGreen(), getLineBlue()));
      gc.drawRectangle(x + getX(), y + getY(), width, height);
      gc.setForeground(lineColor);
    }
  }

  public void paintHover(GC gc, int x, int y, int dx, int dy) {
    for (Display display : displays.values())
      display.paintHover(gc, x, y, dx + getX(), dy + getY());
  }

  public void remove(String id) {
    if (displays.containsKey(id)) {
      displays.remove(id);
    } else {
      for (Display display : displays.values()) {
        display.remove(id);
      }
    }
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

  public void setText(String id, String text) {
    for (Display display : displays.values())
      display.setText(id, text);
  }

  public String toString() {
    return "Box(" + id + "," + x + "," + y + "," + width + "," + height + "," + displays.values() + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Box ");
    out.print("id='" + getId() + "' ");
    out.print("x='" + getX() + "' ");
    out.print("y='" + getY() + "' ");
    out.print("width='" + getWidth() + "' ");
    out.print("height='" + getHeight() + "' ");
    out.print("curve='" + getCurve() + "' ");
    out.print("top='" + isTop() + "' ");
    out.print("right='" + isRight() + "' ");
    out.print("bottom='" + isBottom() + "' ");
    out.print("left='" + isLeft() + "' ");
    out.print("lineRed='" + getLineRed() + "' ");
    out.print("lineGreen='" + getLineGreen() + "' ");
    out.print("lineBlue='" + getLineBlue() + "' ");
    out.print("fillRed='" + getFillRed() + "' ");
    out.print("fillGreen='" + getFillGreen() + "' ");
    out.print("fillBlue='" + getFillBlue() + "'>");
    for (Display display : displays.values())
      display.writeXML(out);
    out.print("</Box>");
  }

  public void newMultilineText(String parentId, String id, String text, int x, int y, int width, int height, boolean editable, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, String font) {
    if (getId().equals(parentId)) {
      MultilineText t = new MultilineText(id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
      displays.put(id, t);
    } else {
      for (Display d : displays.values())
        d.newMultilineText(parentId, id, text, x, y, width, height, editable, lineRed, lineGreen, lineBlue, fillRed, fillGreen, fillBlue, font);
    }
  }
}
