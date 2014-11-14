package tool.clients.editors;

import java.io.PrintStream;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class TextEditor implements ModifyListener, VerifyKeyListener, MouseListener, LineBackgroundListener {

  private static final int ZOOM          = 2;
  private static final int MAX_FONT_SIZE = 40;
  private static final int MIN_FONT_SIZE = 2;
  private static final int RIGHT_BUTTON  = 3;

  String                   id;
  String                   label;
  StyledText               text;
  FontData                 fontData      = new FontData("Courier New", 16, SWT.NO);
  Vector<WordRule>         wordRules     = new Vector<WordRule>();
  Vector<Integer>          highlights    = new Vector<Integer>();
  boolean                  lineNumbers   = true;
  boolean                  dirty         = false;

  public StyledText getText() {
    return text;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public String getLabel() {
    return label;
  }

  public TextEditor(String id, String label, CTabFolder parent, boolean editable, boolean lineNumbers, String s) {
    this.id = id;
    this.lineNumbers = lineNumbers;
    this.label = label;
    text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    text.setEditable(editable);
    text.setText(s);
    text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
    Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    text.setBackground(bg);
    text.addModifyListener(this);
    text.addVerifyKeyListener(this);
    text.addMouseListener(this);
    text.addLineBackgroundListener(this);
    new UndoRedoImpl(text);
    addCommentWordRule();
  }

  private void addCommentWordRule() {
    // This should be done by XMF really. Add comment as the first multiline rule...
    addMultilineRule(getId(), "//", "\n", 204, 0, 0);
  }

  public String getId() {
    return id;
  }

  public void setText(String s) {
    // We do not want to fire a dirty event at this point because
    // this should only be called to initialize the content or to
    // refresh the content...
    dirty = true;
    text.setText(s);
    dirty = false;
  }

  public void addWordRule(String id, String text, int red, int green, int blue) {
    if (getId().equals(id)) wordRules.add(new WordRule(text, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  public void addMultilineRule(String id, String start, String end, int red, int green, int blue) {
    if (getId().equals(id)) wordRules.add(new MultiLineRule(start, end, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  public void modifyText(ModifyEvent event) {
    addLines();
    if (!dirty) {
      Message message = EditorClient.theClient().getHandler().newMessage("textDirty", 2);
      message.args[0] = new Value(getId());
      message.args[1] = new Value(true);
      EditorClient.theClient().getHandler().raiseEvent(message);
      dirty = true;
    }
    addStyles();
  }

  private void addStyles() {
    if (text.getCharCount() > 0) text.replaceStyleRanges(0, text.getCharCount() - 1, styleRanges());
  }

  public void addLines() {
    if (lineNumbers) {
      int maxLine = text.getLineCount();
      int lineCountWidth = Math.max(String.valueOf(maxLine).length(), 3);
      StyleRange style = new StyleRange();
      style.metrics = new GlyphMetrics(0, 0, lineCountWidth * 8 + 5);
      Bullet bullet = new Bullet(ST.BULLET_NUMBER, style);
      text.setLineBullet(0, text.getLineCount(), null);
      text.setLineBullet(0, text.getLineCount(), bullet);
    }
  }

  private StyleRange[] styleRanges() {
    java.util.List<StyleRange> ranges = new java.util.ArrayList<StyleRange>();
    String s = text.getText();
    int prevChar = -1;
    for (int i = 0; i < s.length(); i++) {
      for (WordRule wordRule : wordRules) {
        StyleRange style = wordRule.match(s, i, prevChar);
        if (style != null) {
          ranges.add(style);
          i = i + style.length - 1;
          break;
        }
      }
      prevChar = s.charAt(i);
    }
    return (StyleRange[]) ranges.toArray(new StyleRange[0]);
  }

  public void verifyKey(VerifyEvent e) {
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '=')) {
      fontData.setHeight(Math.min(fontData.getHeight() + ZOOM, MAX_FONT_SIZE));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
      e.doit = false;
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == '-')) {
      fontData.setHeight(Math.max(MIN_FONT_SIZE, fontData.getHeight() - ZOOM));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
      e.doit = false;
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'f')) {
      FindUtil.show(XModeler.getXModeler(), text);
      e.doit = false;
    }
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 's')) {
      save();
      e.doit = false;
    }
  }

  private boolean isCommand(MouseEvent event) {
    return (event.stateMask & SWT.COMMAND) != 0;
  }

  public boolean isLeft(MouseEvent event) {
    return event.button == 1;
  }

  private boolean isRightClick(MouseEvent event) {
    return event.button == RIGHT_BUTTON;
  }

  public void mouseDoubleClick(MouseEvent event) {

  }

  public void mouseDown(MouseEvent event) {
    if (isRightClick(event) || isCommand(event)) {
      rightClick(event);
    }
  }

  public void save() {
    Message message = EditorClient.theClient().getHandler().newMessage("saveText", 2);
    message.args[0] = new Value(getId());
    message.args[1] = new Value(text.getText());
    EditorClient.theClient().getHandler().raiseEvent(message);
  }

  public void rightClick(MouseEvent event) {
    MenuClient.popup(id, event.x, event.y);
  }

  public void mouseUp(MouseEvent event) {

  }

  public void writeXML(PrintStream out, boolean isSelected, String label, String toolTip) {
    out.print("<TextEditor id='" + getId() + "' selected='" + isSelected + "'");
    out.print(" text='" + XModeler.encodeXmlAttribute(text.getText()) + "'");
    out.print(" lineNumbers='" + lineNumbers + "'");
    out.print(" label='" + label + "'");
    out.print(" toolTip='" + toolTip + "'");
    out.print(" editable='" + text.getEditable() + "'");
    out.print(" fontHeight='" + fontData.getHeight() + "'>");
    for (WordRule rule : wordRules)
      rule.writeXML(out);
    out.print("</TextEditor>");
  }

  public void inflate(Node textEditor) {
    NodeList children = textEditor.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
      inflateElement(children.item(i));
    EditorClient.theClient().runOnDisplay(new Runnable() {
      public void run() {
        addLines();
        addStyles();
      }
    });
  }

  private void inflateElement(Node item) {
    if (item.getNodeName().equals("WordRule"))
      inflateWordRule(item);
    else if (item.getNodeName().equals("MultiLineRule")) inflateMultiLineRule(item);
  }

  private void inflateWordRule(Node item) {
    String word = XModeler.attributeValue(item, "word");
    int red = Integer.parseInt(XModeler.attributeValue(item, "red"));
    int green = Integer.parseInt(XModeler.attributeValue(item, "green"));
    int blue = Integer.parseInt(XModeler.attributeValue(item, "blue"));
    addWordRule(getId(), word, red, green, blue);
  }

  private void inflateMultiLineRule(Node item) {
    String word = XModeler.attributeValue(item, "word");
    String end = XModeler.attributeValue(item, "end");
    int red = Integer.parseInt(XModeler.attributeValue(item, "red"));
    int green = Integer.parseInt(XModeler.attributeValue(item, "green"));
    int blue = Integer.parseInt(XModeler.attributeValue(item, "blue"));
    addMultilineRule(getId(), word, end, red, green, blue);
  }

  public void showLine(int line) {
    text.setCaretOffset(text.getOffsetAtLine(line));
    text.redraw();
  }

  public void lineGetBackground(LineBackgroundEvent event) {
    if (highlights.contains(event.lineOffset)) event.lineBackground = EditorClient.LINE_HIGHLIGHT;
  }

  public void addLineHighlight(int line) {
    highlights.add(text.getOffsetAtLine(line - 1));
    text.redraw();
  }

  public void clearHighlights() {
    highlights.clear();
  }
}