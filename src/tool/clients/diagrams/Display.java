package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.graphics.GC;

public interface Display {

  void paint(GC gc, int x, int y);

  void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue);

  void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue);

  void resize(String id, int width, int height);

  void editText(String id);

  void setText(String id, String text);

  void move(String id, int x, int y);

  void paintHover(GC gc, int x, int y, int dx, int dy);

  void remove(String id);

  void doubleClick(GC gc, Diagram diagram, int dx, int dy, int mouseX, int mouseY);

  void writeXML(PrintStream out);

}
