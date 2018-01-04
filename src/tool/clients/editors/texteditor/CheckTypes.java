package tool.clients.editors.texteditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class CheckTypes extends Tool {

  private static final Color CHECKING     = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
  private static final Color NOT_CHECKING = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
  private static final Color BLACK        = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

  TextEditor                 editor;

  CheckTypes(TextEditor editor) {
    this.editor = editor;
  }

  public void paint(GC gc, int x, int y, int width, int height) {
    Color fg = gc.getForeground();
    Color bg = gc.getBackground();
    gc.setBackground(getColor());
    gc.setForeground(BLACK);
    gc.fillOval(x, y, width, height);
    gc.drawOval(x, y, width, height);
    gc.setForeground(fg);
    gc.setBackground(bg);
  }

  public Color getColor() {
    switch (editor.getCheckingTypes()) {
      case OFF:
        return NOT_CHECKING;
      case ON:
        return CHECKING;
      case IGNORE_ELEMENT:
        return BLACK;
      default:
        return NOT_CHECKING;
    }
  }

  public String toolTip() {
    switch (editor.getCheckingTypes()) {
      case OFF:
        return "no type checking";
      case ON:
        return "type checking";
      case IGNORE_ELEMENT:
        return "ignoring element";
      default:
        return "huh?";
    }
  }

  public void click(TextEditor editor) {
    editor.advanceTypeCheck();
  }

  public void rightClick(int x, int y) {
  }

}
