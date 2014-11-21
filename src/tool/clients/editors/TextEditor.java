package tool.clients.editors;

import java.io.PrintStream;
import java.util.Hashtable;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.dialogs.notifier.NotifierDialog;
import tool.clients.editors.pprint.Indent;
import tool.clients.editors.pprint.Literal;
import tool.clients.editors.pprint.NewLine;
import tool.clients.editors.pprint.PPrint;
import tool.clients.editors.pprint.Seq;
import tool.clients.editors.pprint.Space;
import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class TextEditor implements ModifyListener, VerifyKeyListener, MouseListener, LineBackgroundListener {

  private static final int           ZOOM          = 2;
  private static final int           MAX_FONT_SIZE = 40;
  private static final int           MIN_FONT_SIZE = 2;
  private static final int           RIGHT_BUTTON  = 3;

  String                             id;
  String                             label;
  StyledText                         text;
  FontData                           fontData      = new FontData("Courier New", 16, SWT.NO);
  Hashtable<String, PPrint>          atTable       = new Hashtable<String, PPrint>();
  Hashtable<String, Vector<Keyword>> keyTable      = new Hashtable<String, Vector<Keyword>>();
  Vector<WordRule>                   wordRules     = new Vector<WordRule>();
  Vector<Integer>                    highlights    = new Vector<Integer>();
  boolean                            lineNumbers   = true;
  boolean                            dirty         = false;
  boolean                            autoComplete  = true;
  char                               lastChar      = '\0';

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
    populateAt();
    populateKeywords();
    new UndoRedoImpl(text);
    addCommentWordRule();
  }

  private void populateKeywords() {
    addKeyword("context", "context (class)", contextClass());
    addKeyword("context", "context (operation)", contextOperation());
    addKeyword("if", "if (then)", ifThen());
    addKeyword("if", "if (then else)", ifThenElse());
    addKeyword("format", "format", format());
    addKeyword("let", "let (multiple sequential bindings)", letMultipleSequentialBindings());
    addKeyword("let", "let (single binding)", letSingleBinding());
    addKeyword("try", "try", _try());
    addKeyword("context", "context (package)", contextPackage());
  }

  private PPrint _try() {
    return new Seq(new Indent(new Seq(new NewLine(), new Literal("body"))), new NewLine(), new Indent(new Seq(new Literal("catch(exception)"), new NewLine(), new Literal("handler"))), new NewLine(), new Literal("end"));
  }

  private PPrint format() {
    return new Literal("(stdout, \"\",Seq{})");
  }

  private PPrint ifThen() {
    return new Seq(new Space(), new Literal("exp"), new Space(), new NewLine(), new Literal("then"), new Space(), new Literal("exp"), new NewLine(), new Literal("end"));
  }

  private PPrint ifThenElse() {
    return new Seq(new Space(), new Literal("exp"), new Space(), new NewLine(), new Literal("then"), new Space(), new Literal("exp"), new NewLine(), new Literal("then"), new Space(), new Literal("exp"), new NewLine(), new Literal("end"));
  }

  private PPrint contextClass() {
    return new Indent(new Seq(new Space(), new Literal("Root"), new NewLine(), _class()));
  }

  private PPrint contextPackage() {
    return new Indent(new Seq(new Space(), new Literal("Root"), new NewLine(), _package()));
  }

  private PPrint _package() {
    return new Seq(new Indent(new Seq(new Literal("@Package"), new Space(), new Literal("name"), new NewLine(), _class())), new NewLine(), new Literal("end"));
  }

  private PPrint _class() {
    return new Seq(new Indent(new Seq(new Literal("@Class"), new Space(), new Literal("name"), new Space(), new Literal("extends"), new Space(), new Literal("Object"), new NewLine(), attribute(), new NewLine(), operation())), new NewLine(), new Literal("end"));
  }

  private PPrint attribute() {
    return new Seq(new Literal("@Attribute"), new Space(), new Literal("name"), new Space(), new Literal(":"), new Space(), new Literal("Type"), new Space(), new Literal("end"));
  }

  private PPrint contextOperation() {
    return new Indent(new Seq(new Space(), new Literal("Root"), new NewLine(), operation()));
  }

  private void populateAt() {
    atTable.put("Attribute", attribute());
    atTable.put("Class", _class());
    atTable.put("Constructor", constructor());
    atTable.put("For", _for());
    atTable.put("Operation", operation());
    atTable.put("TypeCase", typeCase());
    atTable.put("WithOpenFile (in)", withOpenFileIn());
    atTable.put("WithOpenFile (out)", withOpenFileOut());
  }

  private PPrint withOpenFileIn() {
    return new Seq(new Indent(new Seq(new Literal("@WithOpenFile(fin <- filename)"), new NewLine(), new Literal("body"))), new NewLine(), new Literal("end"));
  }

  private PPrint withOpenFileOut() {
    return new Seq(new Indent(new Seq(new Literal("@WithOpenFile(fout -> filename)"), new NewLine(), new Literal("body"))), new NewLine(), new Literal("end"));
  }

  private PPrint _for() {
    return new Seq(new Indent(new Seq(new Literal("@For"), new Space(), new Literal("name"), new Space(), new Literal("in"), new Space(), new Literal("collection"), new Space(), new Literal("do"), new NewLine(), new Literal("body"))), new NewLine(), new Literal("end"));
  }

  private PPrint constructor() {
    return new Literal("@Constructor(slots) ! end");
  }

  private PPrint typeCase() {
    return new Seq(new Indent(new Seq(new Literal("@TypeCase(exp)"), new NewLine(), new Indent(new Seq(new Literal("exp"), new Space(), new Literal("do"), new NewLine(), new Literal("exp"))), new NewLine(), new Literal("end"), new NewLine(), new Literal("else"), new Space(), new Literal("exp"))), new NewLine(), new Literal("end"));
  }

  private PPrint operation() {
    return new Seq(new Literal("@Operation name(args)"), new Indent(new Seq(new NewLine(), new Literal("body"))), new NewLine(), new Literal("end"));
  }

  private void addCommentWordRule() {
    // This should be done by XMF really. Add comment as the first multiline rule...
    addMultilineRule(getId(), "//", "\n", 204, 0, 0);
  }

  private void addKeyword(String keyword, String description, PPrint pprint) {
    if (!keyTable.containsKey(keyword)) keyTable.put(keyword, new Vector<Keyword>());
    Vector<Keyword> keys = keyTable.get(keyword);
    keys.add(new Keyword(description, pprint));
  }

  public void addLineHighlight(int line) {
    highlights.add(text.getOffsetAtLine(line - 1));
    text.redraw();
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
      text.setLineIndent(0, text.getLineCount(), lineCountWidth + 10);
    } else text.setLineBullet(0, text.getLineCount(), null);
  }

  public void addMultilineRule(String id, String start, String end, int red, int green, int blue) {
    if (getId().equals(id)) wordRules.add(new MultiLineRule(start, end, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  private void addStyles() {
    if (text.getCharCount() > 0) text.replaceStyleRanges(0, text.getCharCount() - 1, styleRanges());
  }

  public void addWordRule(String id, String text, int red, int green, int blue) {
    if (getId().equals(id)) wordRules.add(new WordRule(text, new Color(XModeler.getXModeler().getDisplay(), red, green, blue)));
  }

  private boolean at() {
    // The user typed an '@'. Offer up the options in the atTable and
    // return false if nothing is selected. Insert the choice and return
    // true if selected...
    if (!atTable.isEmpty()) {
      final boolean[] result = new boolean[] { false };
      Menu menu = getAtPopup(result);
      Point p = text.getCaret().getLocation();
      Point displayPoint = text.toDisplay(p);
      menu.setLocation(displayPoint);
      menu.setVisible(true);
      while (!menu.isDisposed() && menu.isVisible()) {
        if (!Display.getCurrent().readAndDispatch()) Display.getCurrent().sleep();
      }
      menu.dispose();
      return result[0];
    } else return false;
  }

  public void clearHighlights() {
    highlights.clear();
  }

  protected Menu getAtPopup(final boolean[] result) {
    // Offer the @ options. If selected then insert the text
    // including the '@' and set the result...
    Menu menu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
    for (final String name : atTable.keySet()) {
      MenuItem item = new MenuItem(menu, SWT.CASCADE);
      item.setText(name);
      item.addSelectionListener(new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent event) {
        }

        public void widgetSelected(SelectionEvent event) {
          PPrint pprint = atTable.get(name);
          int indent = getCurrentIndent();
          String s = pprint.toString(indent);
          text.insert(s);
          text.setCaretOffset(text.getCaretOffset() + s.length());
          result[0] = true;
        }
      });
    }
    return menu;
  }

  private int getCurrentIndent() {
    String s = text.getText();
    int start = text.getOffsetAtLine(text.getLineAtOffset(text.getCaretOffset()));
    int indent = 0;
    for (int i = start; i < s.length() && s.charAt(i) == ' '; i++)
      indent++;
    return indent;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public StyledText getText() {
    return text;
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

  private void inflateMultiLineRule(Node item) {
    String word = XModeler.attributeValue(item, "word");
    String end = XModeler.attributeValue(item, "end");
    int red = Integer.parseInt(XModeler.attributeValue(item, "red"));
    int green = Integer.parseInt(XModeler.attributeValue(item, "green"));
    int blue = Integer.parseInt(XModeler.attributeValue(item, "blue"));
    addMultilineRule(getId(), word, end, red, green, blue);
  }

  private void inflateWordRule(Node item) {
    String word = XModeler.attributeValue(item, "word");
    int red = Integer.parseInt(XModeler.attributeValue(item, "red"));
    int green = Integer.parseInt(XModeler.attributeValue(item, "green"));
    int blue = Integer.parseInt(XModeler.attributeValue(item, "blue"));
    addWordRule(getId(), word, red, green, blue);
  }

  private boolean isCommand(MouseEvent event) {
    return (event.stateMask & SWT.COMMAND) != 0;
  }

  public boolean isDirty() {
    return dirty;
  }

  public boolean isLeft(MouseEvent event) {
    return event.button == 1;
  }

  private boolean isRightClick(MouseEvent event) {
    return event.button == RIGHT_BUTTON;
  }

  private PPrint letSingleBinding() {
    return new Seq(new Space(), new Literal("name = exp"), new NewLine(), new Literal("in"), new Space(), new Literal("body"), new NewLine(), new Literal("end"));
  }

  private PPrint letMultipleSequentialBindings() {
    return new Seq(new Space(), new Indent(new Indent(new Seq(new Literal("name = exp then"), new NewLine(), new Literal("name = exp")))), new NewLine(), new Literal("in"), new Space(), new Literal("body"), new NewLine(), new Literal("end"));
  }

  public void lineGetBackground(LineBackgroundEvent event) {
    if (highlights.contains(event.lineOffset)) event.lineBackground = EditorClient.LINE_HIGHLIGHT;
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
    if (autoComplete) checkKeywords();
  }

  private void checkKeywords() {
    if (isAlpha(lastChar)) {
      Vector<Keyword> keys = getKeysAtCurrentPosition();
      if (keys != null) key(keys);
    }
  }

  private boolean isAlpha(char c) {
    return 'a' <= c && c <= 'z';
  }

  public void mouseDoubleClick(MouseEvent event) {

  }

  public void mouseDown(MouseEvent event) {
    if (isRightClick(event) || isCommand(event)) {
      rightClick(event);
    }
  }

  public void mouseUp(MouseEvent event) {

  }

  private void newline(int indent) {
    text.insert("\n");
    text.setCaretOffset(text.getCaretOffset() + 1);
    for (int i = 0; i < indent; i++)
      text.insert(" ");
    text.setCaretOffset(text.getCaretOffset() + indent);
  }

  public void rightClick(MouseEvent event) {
    MenuClient.popup(id, event.x, event.y);
  }

  public void save() {
    Message message = EditorClient.theClient().getHandler().newMessage("saveText", 2);
    message.args[0] = new Value(getId());
    message.args[1] = new Value(text.getText());
    EditorClient.theClient().getHandler().raiseEvent(message);
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public void setText(String s) {
    // We do not want to fire a dirty event at this point because
    // this should only be called to initialize the content or to
    // refresh the content...
    dirty = true;
    text.setText(s);
    dirty = false;
  }

  public void showLine(int line) {
    text.setCaretOffset(text.getOffsetAtLine(line));
    text.redraw();
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
    if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'l')) {
      lineNumbers = !lineNumbers;
      addLines();
      e.doit = false;
    }
    if (((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && (e.keyCode == '2') && autoComplete) {
      e.doit = !at();
    }
    if (e.character == '\r') {
      newline(getCurrentIndent());
      e.doit = false;
    }
    if (e.doit) lastChar = e.character;
  }

  private void key(Vector<Keyword> keys) {
    if (keys.size() == 1) {
      String s = keys.elementAt(0).toString(getCurrentIndent());
      autoComplete = false;
      text.insert(s);
      autoComplete = true;
      text.setCaretOffset(text.getCaretOffset() + s.length());
    }
    if (keys.size() > 1) {
      Menu menu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
      for (final Keyword keyword : keys) {
        MenuItem item = new MenuItem(menu, SWT.NONE);
        item.setText(keyword.description);
        item.addSelectionListener(new SelectionListener() {
          public void widgetDefaultSelected(SelectionEvent event) {
          }

          public void widgetSelected(SelectionEvent event) {
            String s = keyword.toString(getCurrentIndent());
            autoComplete = false;
            text.insert(s);
            autoComplete = true;
            text.setCaretOffset(text.getCaretOffset() + s.length());
          }
        });
      }
      menu.setVisible(true);
      while (!menu.isDisposed() && menu.isVisible()) {
        if (!Display.getCurrent().readAndDispatch()) Display.getCurrent().sleep();
      }
      menu.dispose();
    }
  }

  private Vector<Keyword> getKeysAtCurrentPosition() {
    int index = text.getCaretOffset();
    String s = text.getText();
    for (String key : keyTable.keySet()) {
      if (s.indexOf(key, index - key.length()) == index - key.length()) return keyTable.get(key);
    }
    return null;
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
}