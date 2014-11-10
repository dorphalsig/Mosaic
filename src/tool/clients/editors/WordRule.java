package tool.clients.editors;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

import tool.xmodeler.XModeler;

public class WordRule {

  String word;
  Color  color;

  public WordRule(String word, Color color) {
    super();
    this.word = word;
    this.color = color;
  }

  public void writeXML(PrintStream out) {
    out.print("<WordRule word='" + XModeler.encodeXmlAttribute(word) + "' red='" + color.getRed() + "' green='" + color.getGreen() + "' blue='" + color.getBlue() + "'/>");
  }

  public String getWord() {
    return word;
  }

  public Color getColor() {
    return color;
  }

  public StyleRange match(String s, int i, int prevChar) {
    if (canStartKeyword(prevChar, word.charAt(0)) && s.startsWith(word, i)) {
      StyleRange style = new StyleRange();
      style.start = i;
      style.length = word.length();
      style.fontStyle = SWT.UNDERLINE_SINGLE;
      style.foreground = color;
      return style;
    } else return null;
  }

  public boolean canStartKeyword(int prevChar, int keyChar) {
    return !(Character.isAlphabetic(prevChar) && Character.isAlphabetic(keyChar));
  }

  public String toString() {
    return "WordRule(" + word + "," + color + ")";
  }

}
