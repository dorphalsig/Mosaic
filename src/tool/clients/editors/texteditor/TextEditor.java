package tool.clients.editors.texteditor;

import java.io.PrintStream;
import java.util.Vector;

import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.ToolTip;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.editors.EditorClient;
import tool.clients.editors.FindUtil;
import tool.clients.editors.ITextEditor;
import tool.clients.editors.UndoRedoImpl;
import tool.clients.menus.MenuClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class TextEditor implements VerifyListener, VerifyKeyListener, MouseMoveListener, MouseListener, MouseWheelListener, LineBackgroundListener, ExtendedModifyListener, PaintObjectListener, SelectionListener, LineStyleListener, PaintListener, MouseTrackListener, ITextEditor {

  private static final int   ZOOM          = 2;
  private static final int   MAX_FONT_SIZE = 40;
  private static final int   MIN_FONT_SIZE = 2;
  private static final int   LEFT_BUTTON   = 1;
  private static final int   MIDDLE_BUTTON = 2;
  private static final int   RIGHT_BUTTON  = 3;
  private static final int   TRAY_PAD      = 5;
  private static final int   SYNTAX_DELAY  = 2000;
  private static final int   SYNTAX_INC    = 200;
  private static final Color RED           = Display.getDefault().getSystemColor(SWT.COLOR_RED);
  private static final Color BLACK         = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
  private static final Color VAR_DEC       = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);

  public static void drawArrow(GC gc, int x1, int y1, int x2, int y2, double arrowLength, double arrowAngle, Color arrowColor) {
    double theta = Math.atan2(y2 - y1, x2 - x1);
    double offset = (arrowLength - 2) * Math.cos(arrowAngle);
    Color cf = gc.getForeground();
    Color cb = gc.getBackground();
    gc.setForeground(arrowColor);
    gc.setBackground(arrowColor);
    gc.drawLine(x1, y1, (int) (x2 - offset * Math.cos(theta)), (int) (y2 - offset * Math.sin(theta)));
    Path path = new Path(gc.getDevice());
    path.moveTo((float) (x2 - arrowLength * Math.cos(theta - arrowAngle)), (float) (y2 - arrowLength * Math.sin(theta - arrowAngle)));
    path.lineTo((float) x2, (float) y2);
    path.lineTo((float) (x2 - arrowLength * Math.cos(theta + arrowAngle)), (float) (y2 - arrowLength * Math.sin(theta + arrowAngle)));
    path.close();
    gc.fillPath(path);
    path.dispose();
    gc.setForeground(cf);
    gc.setForeground(cf);
  }

  String                id;
  String                label;
  StyledText            text;
  FontData              fontData;
  ToolTip               toolTip;
  LineStyler            lineStyler     = new LineStyler(this);
  Vector<Integer>       highlights     = new Vector<Integer>();
  Vector<ErrorListener> errorListeners = new Vector<ErrorListener>();
  Vector<FileError>     errors         = new Vector<FileError>();
  Vector<VarInfo>       varInfo        = new Vector<VarInfo>();
  VarInfo               mouseOverVar   = null;
  Tray                  tray           = new Tray();
  Timer                 syntaxTimer    = new Timer(SYNTAX_DELAY, SYNTAX_INC, () -> sendTextChanged(), () -> timerIncrement());
  CheckSyntax           checkSyntax    = new CheckSyntax(this);
  int[]                 offsets        = new int[] {};
  boolean               dirty          = false;
  boolean               checkingSyntax = true;
  boolean               rendering      = true;
  char                  lastChar       = '\0';
  int                   errorPosition  = -1;

  String                errorMessage   = "";

  public TextEditor(String id, String label, CTabFolder parent, boolean editable, boolean lineNumbers, String s) {
    this.id = id;
    lineStyler.setLineNumbers(lineNumbers);
    this.label = label;
    createTray();
    createText(parent, editable, s);
    new UndoRedoImpl(text);
  }

  private void addErrorListener(ErrorListener listener) {
    errorListeners.add(listener);
  }

  public void addLineHighlight(int line) {
    highlights.add(text.getOffsetAtLine(line - 1));
    redraw();
  }

  public void addMultilineRule(String id, String start, String end, int red, int green, int blue) {
    if (getId().equals(id)) {
      lineStyler.addMultilineRule(id, start, end, red, green, blue);
    }
  }

  public void addWordRule(String id, String text, int red, int green, int blue) {
    if (getId().equals(id)) lineStyler.addWordRule(id, text, red, green, blue);
  }

  private void cancelToolTip() {
    if (toolTip != null && !toolTip.isDisposed()) {
      toolTip.dispose();
      toolTip = null;
    }
  }

  public void clearErrors() {
    errorPosition = -1;
    errorMessage = "";
    for (ErrorListener l : errorListeners) {
      l.clear();
    }
    errors.clear();
    redraw();
  }

  public void clearHighlights() {
    highlights.clear();
  }

  private void click(MouseEvent event) {
    int x = event.x;
    int y = event.y;
    Tool tool = selectTool(x, y);
    if (tool != null) {
      tool.click(this);
      redraw();
    }
  }

  private void createText(CTabFolder parent, boolean editable, String s) {
    text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    FontData[] fontData = Display.getDefault().getSystemFont().getFontData();
    this.fontData = fontData[0];
    XModeler.getXModeler().getDisplay().loadFont("dejavu/DejaVuSansMono.ttf");
    this.fontData.setName("DejaVu Sans Mono");
    text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
    GC gc = new GC(text);
    gc.setTextAntialias(SWT.ON);
    text.setText(s);
    text.setBackground(bg);
    text.addLineStyleListener(this);
    text.setEditable(editable);
    text.addVerifyListener(this);
    text.addMouseMoveListener(this);
    text.addMouseTrackListener(this);
    text.addPaintListener(this);
    text.addExtendedModifyListener(this);
    text.addVerifyKeyListener(this);
    text.addMouseListener(this);
    text.addMouseWheelListener(this);
    text.addLineBackgroundListener(this);
    text.addVerifyListener(this);
    text.addPaintObjectListener(this);
    text.addSelectionListener(this);
  }

  private void createTray() {
    ErrorTool errors = new ErrorTool();
    addErrorListener(errors);
    tray.addTool(checkSyntax);
    tray.addTool(errors);
    tray.addTool(syntaxTimer);
  }

  private boolean errorToolTip(int x, int y) {
    if (mouseNearParseError(x, y)) {
      org.eclipse.swt.graphics.Point p = text.getLocationAtOffset(errorPosition);
      setToolTip(p.x, p.y, errorMessage);
      return true;
    } else {
      for (FileError e : errors) {
        org.eclipse.swt.graphics.Point p = text.getLocationAtOffset(e.getStart());
        int x2 = p.x;
        int y2 = p.y;
        int dx = x - x2;
        int dy = y - y2;
        double distance = Math.sqrt((dx * dx) + (dy * dy));
        if (distance < 20) {
          p = text.getLocationAtOffset(e.getStart());
          setToolTip(p.x, p.y, e.getMessage());
          return true;
        }
      }
      return false;
    }
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

  public int getLineCount() {
    return text.getLineCount();
  }

  public String getString() {
    return text.getText();
  }

  public StyledText getText() {
    return text;
  }

  public void inflate(Node textEditor) {
    NodeList children = textEditor.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
      inflateElement(children.item(i));
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
    // WHAT TO DO ABOUT THIS?
  }

  private void inflateWordRule(Node item) {
    String word = XModeler.attributeValue(item, "word");
    int red = Integer.parseInt(XModeler.attributeValue(item, "red"));
    int green = Integer.parseInt(XModeler.attributeValue(item, "green"));
    int blue = Integer.parseInt(XModeler.attributeValue(item, "blue"));
    // WHAT TO DO ABOUT THIS?
  }

  private boolean isAlpha(char c) {
    return 'a' <= c && c <= 'z';
  }

  public boolean isCheckingSyntax() {
    return checkingSyntax;
  }

  private boolean isCntrl(MouseEvent e) {
    return (e.stateMask & SWT.CTRL) == SWT.CTRL || (e.stateMask & SWT.COMMAND) == SWT.COMMAND;
  }

  private boolean isCntrl(VerifyEvent e) {
    return (e.stateMask & SWT.CTRL) == SWT.CTRL || (e.stateMask & SWT.COMMAND) == SWT.COMMAND;
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

  public boolean isRendering() {
    return rendering;
  }

  private boolean isRightClick(MouseEvent event) {
    return event.button == RIGHT_BUTTON || isCntrl(event);
  }

  public void lineGetBackground(LineBackgroundEvent event) {
    if (highlights.contains(event.lineOffset)) event.lineBackground = EditorClient.LINE_HIGHLIGHT;
  }

  public void lineGetStyle(LineStyleEvent event) {
    lineStyler.lineGetStyle(event);
  }

  public void modifyText(ExtendedModifyEvent event) {
    //lineStyler.clearCache(text.getLineAtOffset(event.start), isNewline(event));
    lineStyler.clearCache();
    varInfo.clear();
    clearErrors();
    if (!dirty) {
      Message message = EditorClient.theClient().getHandler().newMessage("textDirty", 2);
      message.args[0] = new Value(getId());
      message.args[1] = new Value(true);
      EditorClient.theClient().getHandler().raiseEvent(message);
      dirty = true;
    }
    if (checkingSyntax) syntaxTimer.ping();
  }

  private boolean isNewline(ExtendedModifyEvent event) {
    int index = event.start;
    int length = event.length;
    for (int i = 0; i < length; i++) {
      char c = text.getText().charAt(i+index);
      if (c == '\n') return true;
    }
    for(int i = 0; i < event.replacedText.length();i++) {
      char c = event.replacedText.charAt(i);
      if (c == '\n') return true;
    }
    return false;
  }

  public void mouseDoubleClick(MouseEvent event) {

  }

  public void mouseDown(MouseEvent event) {
    cancelToolTip();
    if (isRightClick(event) || isCommand(event)) {
      rightClick(event);
    } else click(event);
  }

  public void mouseEnter(MouseEvent arg0) {
  }

  public void mouseExit(MouseEvent arg0) {

  }

  public void mouseHover(MouseEvent event) {

  }

  public void mouseMove(MouseEvent event) {
    int x = event.x;
    int y = event.y;
    toolTip(x, y);
    setMouseOverVar(x, y);
    mouseOverVar = selectVarInfo(x, y);
    if (mouseOverVar != null) redraw();
  }

  private boolean mouseNearParseError(int x1, int y1) {
    if (errorPosition >= 0) {
      org.eclipse.swt.graphics.Point p = text.getLocationAtOffset(errorPosition);
      int x2 = p.x;
      int y2 = p.y;
      int dx = x1 - x2;
      int dy = y1 - y2;
      double distance = Math.sqrt((dx * dx) + (dy * dy));
      return distance < 20;
    } else return false;
  }

  public void mouseScrolled(MouseEvent e) {
    if (isCntrl(e) && (e.count > 0)) {
      fontData.setHeight(Math.min(fontData.getHeight() + ZOOM, MAX_FONT_SIZE));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
    }
    if (isCntrl(e) && (e.count < 0)) {
      fontData.setHeight(Math.max(MIN_FONT_SIZE, fontData.getHeight() - ZOOM));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
    }
  }

  public void mouseUp(MouseEvent event) {

  }

  public void paintControl(PaintEvent event) {
    paintSyntaxError(event.gc);
    paintErrors(event.gc);
    paintVar(event.gc);
    paintTray(event.gc);
  }

  private void paintErrors(GC gc) {
    int bottomIndex = JFaceTextUtil.getPartialBottomIndex((StyledText) text);
    int topIndex = JFaceTextUtil.getPartialTopIndex((StyledText) text);
    int height = text.getFont().getFontData()[0].getHeight();
    Color c = gc.getForeground();
    gc.setForeground(RED);
    for (FileError e : errors) {
      int start = e.getStart();
      int end = e.getEnd();
      if (validOffset(start) && validOffset(end)) {
        int line = text.getLineAtOffset(start);
        if (line >= topIndex && line <= bottomIndex) {
          org.eclipse.swt.graphics.Point pStart = text.getLocationAtOffset(start);
          org.eclipse.swt.graphics.Point pEnd = text.getLocationAtOffset(end);
          gc.drawLine(pStart.x, pStart.y + height, pEnd.x, pEnd.y + height);
        }
      }
    }
    gc.setForeground(c);
  }

  public void paintObject(PaintObjectEvent event) {
  }

  private void paintSyntaxError(GC gc) {
    if (errorPosition >= 0 && errorPosition < text.getText().length()) {
      org.eclipse.swt.graphics.Point p = text.getLocationAtOffset(errorPosition);
      int height = text.getFont().getFontData()[0].getHeight();
      Color c = gc.getForeground();
      gc.setForeground(RED);
      gc.drawLine(p.x - 10, p.y + height, p.x + 100, p.y + height);
      gc.setForeground(c);
    }
  }

  private void paintTray(GC gc) {
    ScrollBar bar = text.getVerticalBar();
    Rectangle r = text.getClientArea();
    int width = r.width;
    int height = r.height;
    if (bar != null) width -= bar.getSize().x;
    height -= text.getBorderWidth();
    height -= text.getParent().getBorderWidth();
    height -= TRAY_PAD;
    tray.paint(gc, width, height);
  }

  private void paintVar(GC gc) {
    if (mouseOverVar != null) {
      int varOffset = mouseOverVar.getVarStart();
      int decOffset = mouseOverVar.getDecStart();
      if (validOffset(varOffset) && validOffset(decOffset)) {
        Point pStart = text.getLocationAtOffset(varOffset);
        Point pEnd = text.getLocationAtOffset(decOffset);
        int height = text.getFont().getFontData()[0].getHeight() / 2;
        Color c = gc.getForeground();
        drawArrow(gc, pStart.x, pStart.y + height, pEnd.x, pEnd.y + height, 7, 125, VAR_DEC);
        gc.setForeground(c);
      }
    }
  }

  private void redraw() {
    if (rendering) text.redraw();
  }

  public void rightClick(MouseEvent event) {
    int x = event.x;
    int y = event.y;
    Tool tool = selectTool(x, y);
    if (tool == null)
      MenuClient.popup(id, x, y);
    else tool.rightClick(x, y);
  }

  public void save() {
    Message message = EditorClient.theClient().getHandler().newMessage("saveText", 2);
    message.args[0] = new Value(getId());
    message.args[1] = new Value(getString());
    EditorClient.theClient().getHandler().raiseEvent(message);
  }

  public void scrollToError() {
    if (errorPosition >= 0 && errorPosition <= text.getText().length()) {
      int selection = Math.max(0, errorPosition - 100);
      text.setSelection(selection);
      selection = Math.min(text.getText().length(), errorPosition + 100);
      text.setSelection(selection);
      text.setCaretOffset(errorPosition);
    } else {
      if (errors.size() > 0) {
        int selection = Math.max(0, errors.get(0).getStart() - 100);
        text.setSelection(selection);
        selection = Math.min(text.getText().length(), errors.get(0).getStart() + 100);
        text.setSelection(selection);
        text.setCaretOffset(errors.get(0).getStart());
      }
    }
  }

  private Tool selectTool(int x, int y) {
    ScrollBar bar = text.getVerticalBar();
    Rectangle r = text.getClientArea();
    int width = r.width;
    int height = r.height;
    if (bar != null) width -= bar.getSize().x;
    height -= text.getBorderWidth();
    height -= text.getParent().getBorderWidth();
    height -= TRAY_PAD;
    return tray.selectTool(x, y, width, height);
  }

  private VarInfo selectVarInfo(int x, int y) {
    try {
      int location = text.getOffsetAtLocation(new Point(x, y));
      for (VarInfo info : varInfo) {
        int distance = Math.abs(info.getVarStart() - location);
        if (distance < 3) return info;
      }
      return null;
    } catch (IllegalArgumentException e) {
      return null;
    }

  }

  private void sendTextChanged() {
    varInfo.clear();
    XModeler.getXModeler().getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          Message message = EditorClient.theClient().getHandler().newMessage("textChanged", 2);
          message.args[0] = new Value(getId());
          message.args[1] = new Value(text.getText());
          EditorClient.theClient().getHandler().raiseEvent(message);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  public void setCheckingSyntax(boolean checkingSyntax) {
    this.checkingSyntax = checkingSyntax;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  private void setMouseOverVar(int x, int y) {
    boolean isOverVar = mouseOverVar != null;
    mouseOverVar = selectVarInfo(x, y);
    if (mouseOverVar != null || isOverVar) redraw();
  }

  public void setRendering(boolean rendering) {
    this.rendering = rendering;
    redraw();
  }

  public void setString(String text) {
    setText(text);
  }

  public void setText(String s) {
    // We do not want to fire a dirty event at this point because
    // this should only be called to initialize the content or to
    // refresh the content...
    dirty = true;
    text.setText(s);
    dirty = false;
    lineStyler.clearCache();
    varInfo.clear();
    redraw();
  }

  private void setToolTip(int x, int y, String message) {
    if (toolTip == null || toolTip.isDisposed()) {
      toolTip = new ToolTip(text.getShell(), SWT.ICON_ERROR);
    }
    if (toolTip != null && !toolTip.isDisposed()) {
      toolTip.setMessage(message);
      toolTip.setLocation(x, y);
      toolTip.setVisible(true);
    }
  }

  public void showLine(int line) {
    text.setCaretOffset(text.getOffsetAtLine(line));
    redraw();
  }

  public void syntaxError(int pos, String error) {
    this.errorPosition = Math.min(pos - 10, text.getText().length() - 1);
    this.errorMessage = error;
    for (ErrorListener listener : errorListeners) {
      listener.error(this);
    }
    errors.clear();
    redraw();
  }

  private void timerIncrement() {
    XModeler.getXModeler().getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          redraw();
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  private void toolTip(int x, int y) {
    boolean toolTipDone = false;
    toolTipDone = errorToolTip(x, y);
    toolTipDone = toolTipDone || trayToolTip(x, y);
    if (!toolTipDone) cancelToolTip();
  }

  private boolean trayToolTip(int x, int y) {
    Tool tool = selectTool(x, y);
    if (tool != null) {
      setToolTip(x, y, tool.toolTip());
      return true;
    } else return false;
  }

  public void unboundVar(String name, int charStart, int charEnd) {
    errors.add(new FileError(charStart, charEnd, name + " is unbound"));
    for (ErrorListener listener : errorListeners) {
      listener.error(this);
    }
  }

  private boolean validOffset(int c) {
    return c >= 0 && c <= text.getText().length();
  }

  public void varDec(int charStart, int charEnd, int decStart, int decEnd) {
    varInfo.add(new VarInfo(charStart, charEnd, decStart, decEnd));
  }

  public void verifyKey(VerifyEvent e) {
    if (isCntrl(e) && (e.keyCode == '=')) {
      fontData.setHeight(Math.min(fontData.getHeight() + ZOOM, MAX_FONT_SIZE));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
      e.doit = false;
    }
    if (isCntrl(e) && (e.keyCode == '-')) {
      fontData.setHeight(Math.max(MIN_FONT_SIZE, fontData.getHeight() - ZOOM));
      text.setFont(new Font(XModeler.getXModeler().getDisplay(), fontData));
      e.doit = false;
    }
    if (isCntrl(e) && (e.keyCode == 'f')) {
      FindUtil.show(XModeler.getXModeler(), text);
      e.doit = false;
    }
    if (isCntrl(e) && (e.keyCode == 's')) {
      save();
      e.doit = false;
    }
    if (isCntrl(e) && (e.keyCode == 'l')) {
      lineStyler.toggleLineNumbers();
      e.doit = false;
    }
    if (isCntrl(e) && (e.keyCode == 'v')) {
      Display display = XModeler.getXModeler().getDisplay();
      Clipboard clipboard = new Clipboard(display);
      ImageData imageData = (ImageData) clipboard.getContents(ImageTransfer.getInstance());
    }
    if (e.doit) lastChar = e.character;
  }

  public void verifyText(VerifyEvent e) {
    int start = e.start;
    int replaceCharCount = e.end - e.start;
    int newCharCount = e.text.length();
    for (int i = 0; i < offsets.length; i++) {
      int offset = offsets[i];
      if (start <= offset && offset < start + replaceCharCount) {
        offset = -1;
      }
      if (offset != -1 && offset >= start) offset += newCharCount - replaceCharCount;
      offsets[i] = offset;
    }
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent arg0) {

  }

  public void writeXML(PrintStream out, boolean isSelected, String label, String toolTip) {
    out.print("<TextEditor id='" + getId() + "' selected='" + isSelected + "'");
    out.print(" text='" + XModeler.encodeXmlAttribute(text.getText()) + "'");
    out.print(" lineNumbers='" + lineStyler.getLineNumbers() + "'");
    out.print(" label='" + label + "'");
    out.print(" toolTip='" + toolTip + "'");
    out.print(" editable='" + text.getEditable() + "'>");
    out.print("</TextEditor>");
  }
}