package tool.clients.editors;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

import tool.xmodeler.XModeler;

public class MultiLineRule extends WordRule {

  String end;

  public MultiLineRule(String word, String end, Color color) {
    super(word, color);
    this.end = end;
  }

  public void writeXML(PrintStream out) {
    out.print("<MultiLineRule word='" + XModeler.encodeXmlAttribute(word) + "' end='" + XModeler.encodeXmlAttribute(end) + "' red='" + color.getRed() + "' green='" + color.getGreen() + "' blue='" + color.getBlue() + "'/>");
  }

  public StyleRange match(String s, int i, int prevChar) {
    if (canStartKeyword(prevChar, word.charAt(0)) && s.startsWith(word, i) && s.indexOf(end, i + 1) >= 0) {
      StyleRange style = new StyleRange();
      style.start = i;
      style.length = (s.indexOf(end, i + 1) - i) + 1;
      style.fontStyle = SWT.UNDERLINE_SINGLE;
      style.foreground = color;
      return style;
    } else return null;
  }

  public String toString() {
    return "Multiline(" + word + "," + end + "," + color + ")";
  }

}