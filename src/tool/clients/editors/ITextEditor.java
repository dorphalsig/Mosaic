package tool.clients.editors;

import java.io.PrintStream;

import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Node;

import xos.Value;

public interface ITextEditor {

  void action(String name, Value[] args, int charStart, int charEnd);

  void addLineHighlight(int line);

  void addMultilineRule(String id, String start, String end, int red, int green, int blue);

  void addWordRule(String id, String text, int red, int green, int blue);

  void ast(String tooltip, int charStart, int charEnd);

  void clearErrors();

  void clearHighlights();

  void dotError(int charStart, int charEnd, String name);

  String getLabel();

  String getString();

  Control getText();

  void inflate(Node newTextEditor);

  void setDirty(boolean b);

  void setRendering(boolean state);

  void setSignature(Value[] entries);

  void setString(String strValue);

  void setTooltip(String tooltip, int charStart, int charEnd);

  void showLine(int line);

  void syntaxError(int pos, String error);

  void terminates(String end, String start);

  void typeError(int charStart, int charEnd, String expected, String found);

  void unboundVar(String name, int charStart, int charEnd);

  void varDec(int charStart, int charEnd, int decStart, int decEnd);

  void writeXML(PrintStream out, boolean b, String text, String toolTipText);

}
