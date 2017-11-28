package tool.clients.diagrams;

import java.awt.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

public class DiagramError {

  private static final int LINE_WIDTH = 1;

  public static String getLongestString(String[] array) {
    int maxLength = 0;
    String longestString = null;
    for (String s : array) {
      if (s.length() > maxLength) {
        maxLength = s.length();
        longestString = s;
      }
    }
    return longestString;
  }

  String id;
  String error;

  public DiagramError(String id, String error) {
    this.id = id;
    this.error = error;
  }

  public static int getLineWidth() {
    return LINE_WIDTH;
  }

  public String getId() {
    return id;
  }

  public String getError() {
    return error;
  }

  public void paint(GC gc, Diagram diagram) {
    org.eclipse.swt.graphics.Rectangle r = diagram.scroller.getClientArea();
    drawErrorBox(gc, 0, r.height);
  }

  protected int getWidth(GC gc) {
    return gc.stringExtent(getLongestString(getLines())).x + (2 * LINE_WIDTH);
  }

  private String[] getLines() {
    return ("Diagram Error:-------------::" + error).split(":");
  }

  protected void drawErrorBox(GC gc, int baseX, int baseY) {
    String[] lines = getLines();
    Color bg = gc.getBackground();
    Color fg = gc.getForeground();
    int alpha = gc.getAlpha();
    int lineWidth = gc.getLineWidth();
    int lineJoin = gc.getLineJoin();
    gc.setAlpha(200);
    gc.setForeground(Diagram.RED);
    gc.setBackground(Diagram.WHITE);
    int height = gc.getFontMetrics().getHeight();
    int boxWidth = getWidth(gc);
    int boxHeight = height * lines.length;
    int x = baseX;
    int y = baseY - boxHeight;
    gc.fillRectangle(x, y, boxWidth, boxHeight);
    for (String line : lines) {
      gc.drawString(line, x + LINE_WIDTH, y);
      x = baseX;
      y += height;
    }
    gc.setForeground(Diagram.RED);
    gc.setLineWidth(LINE_WIDTH);
    gc.setLineJoin(SWT.JOIN_ROUND);
    gc.drawRectangle(baseX, baseY - boxHeight, boxWidth, boxHeight);
    gc.setForeground(fg);
    gc.setBackground(bg);
    gc.setLineWidth(lineWidth);
    gc.setLineJoin(lineJoin);
    gc.setAlpha(alpha);
  }

  public void addError(String e) {
    error = error + "::" + e;
  }

  public Node selectableNode() {
    return null;
  }

  public Edge selectableEdge() {
    return null;
  }

}
