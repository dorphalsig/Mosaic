package tool.clients.editors.texteditor;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class AST {

  private static final Color HIGHLIGHT = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);

  public static void paintDelimiters(GC gc, StyledText text, int charStart, int charEnd, Color colour, boolean isLine) {
    Color c = gc.getForeground();
    gc.setForeground(colour);
    int height = gc.getFontMetrics().getHeight();
    int gap = 2;
    int width = 5;
    int lStart = text.getLineAtOffset(charStart);
    int lEnd = text.getLineAtOffset(charEnd);
    Point pStart = text.getLocationAtOffset(charStart);
    Point pEnd = text.getLocationAtOffset(charEnd);
    if (lStart != lEnd) {
      gc.drawLine(pStart.x, pStart.y + height / 2, pStart.x - gap, pStart.y + height / 2);
      gc.drawLine(pStart.x - gap, pStart.y + height / 2, pStart.x - gap, pEnd.y + height / 2);
      gc.drawLine(pStart.x - gap, pEnd.y + height / 2, pStart.x, pEnd.y + height / 2);
    } else {
      if (isLine) {
        gc.drawLine(pStart.x, pStart.y + height, pEnd.x, pEnd.y + height);
      } else {
        gc.drawLine(pStart.x - gap, pStart.y + height, pStart.x - gap, pStart.y);
        gc.drawLine(pStart.x - gap, pStart.y + height, pStart.x + width, pStart.y + height);
        gc.drawLine(pStart.x - gap, pStart.y, pStart.x + width, pStart.y);
        gc.drawLine(pEnd.x + gap, pEnd.y + height, pEnd.x + gap, pEnd.y);
        gc.drawLine(pEnd.x + gap, pEnd.y + height, pEnd.x - width, pEnd.y + height);
        gc.drawLine(pEnd.x + gap, pEnd.y, pEnd.x - width, pEnd.y);
      }
    }
    gc.setForeground(c);
  }

  StyledText  text;
  String      tooltip;
  int         charStart;
  int         charEnd;
  Vector<AST> children = new Vector<AST>();

  AST         parent   = null;

  public AST(StyledText text, String tooltip, int charStart, int charEnd) {
    super();
    this.text = text;
    this.tooltip = tooltip;
    this.charStart = charStart;
    this.charEnd = charEnd;
  }

  public void add(AST a) {
    for (AST child : children) {
      if (child.contains(a)) {
        child.add(a);
        return;
      }
    }
    children.add(a);
    a.parent = this;
  }

  public boolean contains(AST a) {
    return a.getCharStart() >= charStart && a.getCharEnd() <= charEnd;
  }

  public AST find(int index) {
    if (index < charStart || index > charEnd) return null;
    AST found = null;
    for (AST child : children) {
      found = child.find(index);
      if (found != null) return found;
    }
    return this;
  }

  public int getCharEnd() {
    return charEnd;
  }

  public int getCharStart() {
    return charStart;
  }

  public String getTooltip() {
    return tooltip;
  }

  public boolean isRoot() {
    return parent == null;
  }

  public void paint(GC gc) {
    try {
      paintDelimiters(gc);
    } catch (Exception e) {
    }
  }

  public void paintDelimiters(GC gc) {
    if (!isRoot()) paintDelimiters(gc, text, charStart, charEnd, HIGHLIGHT, false);

    // Paint delimiters for the AST element...

    if (!isRoot()) {

    }
  }

}
