package tool.clients.editors.texteditor;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.widgets.Display;

import tool.clients.editors.EditorClient;
import tool.clients.editors.MultiLineRule;
import tool.clients.editors.WordRule;
import tool.xmodeler.XModeler;

public class LineStyler {

  TextEditor                       editor;
  boolean                          lineNumbers     = true;
  Vector<WordRule>                 wordRules       = new Vector<WordRule>();
  Vector<MultiLineRule>            multiLineRules  = new Vector<MultiLineRule>();
  Hashtable<Integer, StyleRange[]> cache           = new Hashtable<Integer, StyleRange[]>();
  Vector<MultiLineStyle>           multiLineStyles = new Vector<MultiLineStyle>();

  public LineStyler(TextEditor editor) {
    super();
    this.editor = editor;
  }

  public void addMultilineRule(String id, String start, String end, int red, int green, int blue) {
    multiLineRules.add(new MultiLineRule(start, end, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  public void addWordRule(String id, String text, int red, int green, int blue) {
    wordRules.add(new WordRule(text, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  public void clearCache(int line, boolean isNewline) {
    if (cache.containsKey(line)) cache.remove(line);
    if (isNewline && editor.getText().getLineCount() > line) {
      clearCache(line + 1, isNewline);
    }
  }

  private Bullet getBullet(int line) {
    if (lineNumbers) {
      int maxLine = editor.getLineCount();
      int lineCountWidth = Math.max(String.valueOf(maxLine).length(), 3);
      StyleRange style = new StyleRange(0, editor.getText().getTextLimit(), EditorClient.GREY, EditorClient.WHITE);
      style.metrics = new GlyphMetrics(0, 0, lineCountWidth * 8 + 5);
      return new Bullet(ST.BULLET_NUMBER, style);
    } else return null;
  }

  private java.util.List<StyleRange> lineCreateStyle(LineStyleEvent event, int start, int end) {
    java.util.List<StyleRange> styles = new java.util.ArrayList<StyleRange>();
    String s = editor.getText().getText();
    for (int i = start; i < end; i++) {
      int prevChar = (event.lineOffset + i == 0) ? '\n' : s.charAt(event.lineOffset + i - 1);
      for (WordRule rule : wordRules) {
        StyleRange style = rule.match(s, event.lineOffset + i, prevChar);
        if (style != null) {
          styles.add(style);
          i += style.length;
          break;
        }
      }
    }
    return styles;
  }

  public void lineGetStyle(LineStyleEvent event) {
    // MultiLineStyle mls = getMultiLineStyle(event.lineOffset);
    int line = editor.getText().getLineAtOffset(event.lineOffset);
    event.bullet = getBullet(line);
    event.bulletIndex = line;
    if (cache.containsKey(line)) {
      event.styles = cache.get(line);
    } else {
      java.util.List<StyleRange> list = lineCreateStyle(event, 0, event.lineText.length());
      StyleRange[] ranges = list.toArray(new StyleRange[0]);
      cache.put(line, ranges);
      event.styles = ranges;
    }
  }

  private MultiLineStyle getMultiLineStyle(int offset) {
    for (MultiLineStyle style : multiLineStyles) {
      if (style.getStart() <= offset && offset <= style.getEnd()) return style;
    }
    return null;
  }

  public void clearCache() {
    cache.clear();
  }

  public void setLineNumbers(boolean lineNumbers) {
    this.lineNumbers = lineNumbers;
  }

  public void toggleLineNumbers() {
    lineNumbers = !lineNumbers;
  }

  public boolean getLineNumbers() {
    return lineNumbers;
  }

  public void refreshMultiLineStyles(String text) {
    multiLineStyles.clear();
    for (MultiLineRule rule : multiLineRules) {
      int start = -1;
      while (start < text.length()) {
        start = text.indexOf(rule.getWord(), start + 1);
        if (start == -1)
          start = text.length();
        else {
          int end = text.indexOf(rule.getEnd(), start);
          if (end == -1)
            start = text.length();
          else {
            multiLineStyles.add(new MultiLineStyle(start, end, rule.getColor()));
            start = end;
          }
        }
      }
    }
  }

}
