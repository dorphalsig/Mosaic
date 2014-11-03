package tool.clients.editors;

import java.io.PrintStream;
import java.net.URL;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.Client;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class EditorClient extends Client {

  public static void start(CTabFolder tabFolder, int style) {
    EditorClient.tabFolder = tabFolder;
  }

  public static EditorClient theClient() {
    return theClient;
  }

  static EditorClient                  theClient;
  static CTabFolder                    tabFolder;
  static Hashtable<String, CTabItem>   tabs     = new Hashtable<String, CTabItem>();
  static Hashtable<String, Browser>    browsers = new Hashtable<String, Browser>();
  static Hashtable<String, TextEditor> editors  = new Hashtable<String, TextEditor>();

  public EditorClient() {
    super("com.ceteva.text");
    theClient = this;
  }

  private void addMultilineRule(Message message) {
    String id = message.args[0].strValue();
    String start = message.args[1].strValue();
    String end = message.args[2].strValue();
    String color = message.args[3].strValue();
    if (color.equals("red"))
      addMultilineRule(id, start, end, 255, 0, 0);
    else if (color.equals("green"))
      addMultilineRule(id, start, end, 0, 153, 0);
    else if (color.equals("blue"))
      addMultilineRule(id, start, end, 50, 50, 255);
    else System.out.println("unknown color: " + color);
  }

  private void addMultilineRule(String id, String start, String end, int red, int green, int blue) {
    for (TextEditor editor : editors.values())
      editor.addMultilineRule(id, start, end, red, green, blue);
  }

  private void addWordRule(Message message) {
    if (message.arity == 3)
      addWordRuleNamedColor(message);
    else addWordRuleColor(message);
  }

  private void addWordRuleColor(Message message) {
    String id = message.args[0].strValue();
    String text = message.args[1].strValue();
    int red = message.args[2].intValue;
    int green = message.args[3].intValue;
    int blue = message.args[4].intValue;
    addWordRuleColor(id, text, red, green, blue);
  }

  private void addWordRuleColor(String id, String text, int red, int green, int blue) {
    for (TextEditor editor : editors.values())
      editor.addWordRule(id, text, red, green, blue);
  }

  private void addWordRuleNamedColor(Message message) {
    String id = message.args[0].strValue();
    String text = message.args[1].strValue();
    String color = message.args[2].strValue();
    if (color.equals("red"))
      addWordRuleColor(id, text, 255, 0, 0);
    else if (color.equals("green"))
      addWordRuleColor(id, text, 0, 153, 0);
    else if (color.equals("blue"))
      addWordRuleColor(id, text, 50, 50, 255);
    else System.out.println("unknown color: " + color);
  }

  public Value callMessage(Message message) {
    if (message.hasName("getWelcomePage")) {
      // help page if it is available.
      URL location = EditorClient.class.getProtectionDomain().getCodeSource().getLocation();
      String path = location.toString();
      path = path.substring(0, path.length() - 4); // delete "/bin" from
      // string
      path += "file/Welcome/welcome.html";
      // showBrowser(path);
      return new Value(path);
    }
    if (message.hasName("getText")) return getText(message);
    return super.callMessage(message);
  }

  private Value getText(final Message message) {
    final String id = message.args[0].strValue();
    final Value[] result = new Value[] { null };
    runOnDisplay(new Runnable() {
      public void run() {
        for (String editorId : editors.keySet()) {
          if (id.equals(editorId)) result[0] = new Value(editors.get(editorId).getText().getText());
        }
      }
    });
    if (result[0] == null)
      throw new Error("Cannot find editor with id " + id);
    else return result[0];
  }

  private void inflateEditorElement(Node editor) {
    if (editor.getNodeName().equals("TextEditor")) inflateTextEditor(editor);
  }

  private void inflateTextEditor(Node textEditor) {
    final String id = XModeler.attributeValue(textEditor, "id");
    String text = XModeler.attributeValue(textEditor, "text");
    String label = XModeler.attributeValue(textEditor, "label");
    String toolTip = XModeler.attributeValue(textEditor, "toolTip");
    final boolean selected = XModeler.attributeValue(textEditor, "selected").equals("true");
    boolean editable = XModeler.attributeValue(textEditor, "editable").equals("true");
    boolean lineNumbers = XModeler.attributeValue(textEditor, "lineNumbers").equals("true");
    int fontHeight = Integer.parseInt(XModeler.attributeValue(textEditor, "fontHeight"));
    newTextEditor(id, label, toolTip, editable, lineNumbers, text);
    final TextEditor editor = editors.get(id);
    editor.inflate(textEditor);
    runOnDisplay(new Runnable() {
      public void run() {
        editor.getText().redraw();
        if (selected) tabFolder.setSelection(tabs.get(id));
      }
    });
  }

  public void inflateXML(Document doc) {
    NodeList editorClients = doc.getElementsByTagName("Editors");
    if (editorClients.getLength() == 1) {
      Node editorClient = editorClients.item(0);
      NodeList editors = editorClient.getChildNodes();
      for (int i = 0; i < editors.getLength(); i++) {
        Node editor = editors.item(i);
        inflateEditorElement(editor);
      }
    } else System.out.println("expecting exactly 1 editor client got: " + editorClients.getLength());
  }

  private void newBrowser(Message message) {
    final Value id = message.args[0];
    final Value label = message.args[1];
    Value tootip = message.args[2];
    Value url = message.args[3];
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label.strValue());
        tabs.put(id.strValue(), tabItem);
        Browser browser = new Browser(tabFolder, SWT.BORDER);
        tabItem.setControl(browser);
        browser.setText("<b> Nothing </b>");
        browsers.put(id.strValue(), browser);
        browser.setVisible(true);
      }
    });
  }

  private void newTextEditor(Message message) {
    String id = message.args[0].strValue();
    String label = message.args[1].strValue();
    String toolTip = message.args[2].strValue();
    boolean editable = message.args[3].boolValue;
    newTextEditor(id, label, toolTip, editable, true, "");
  }

  private void newTextEditor(final String id, final String label, final String toolTip, final boolean editable, final boolean lineNumbers, final String text) {
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label);
        tabItem.setToolTipText(toolTip);
        tabs.put(id, tabItem);
        TextEditor editor = new TextEditor(id, label, tabFolder, editable, lineNumbers, text);
        tabItem.setControl(editor.getText());
        editors.put(id, editor);
      }
    });
  }

  public boolean processMessage(Message message) {
    System.out.println(this + " <- " + message);
    return false;
  }

  public void sendMessage(final Message message) {
    if (message.hasName("newBrowser"))
      newBrowser(message);
    else if (message.hasName("setUrl"))
      setUrl(message);
    else if (message.hasName("newTextEditor"))
      newTextEditor(message);
    else if (message.hasName("setText"))
      setText(message);
    else if (message.hasName("addWordRule"))
      addWordRule(message);
    else if (message.hasName("addMultilineRule"))
      addMultilineRule(message);
    else if (message.hasName("setDirty"))
      setDirty(message);
    else if (message.hasName("setClean"))
      setClean(message);
    else super.sendMessage(message);
  }

  private void setDirty(Message message) {
    String id = message.args[0].strValue();
    setDirty(id);
  }

  private void setClean(Message message) {
    String id = message.args[0].strValue();
    setClean(id);
  }

  private void setText(Message message) {
    final Value id = message.args[0];
    final Value text = message.args[1];
    if (editors.containsKey(id.strValue())) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          TextEditor editor = editors.get(id.strValue());
          editor.setText(text.strValue());
          tabFolder.setFocus();
          tabFolder.setSelection(tabs.get(id.strValue()));
        }
      });
    } else System.out.println("cannot find text editor " + id);
  }

  private void setUrl(Message message) {
    final Value id = message.args[0];
    final Value url = message.args[1];
    if (browsers.containsKey(id.strValue())) {
      final Browser browser = browsers.get(id.strValue());
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          browser.setUrl(url.strValue());
          tabFolder.setFocus();
          tabFolder.setSelection(tabs.get(id.strValue()));
        }
      });
    } else System.out.println("cannot find browser " + id);
  }

  public void writeXML(PrintStream out) {
    out.print("<Editors>");
    for (String id : editors.keySet()) {
      CTabItem item = tabs.get(id);
      TextEditor editor = editors.get(id);
      editor.writeXML(out, tabFolder.getSelection() == item, item.getText(), item.getToolTipText());
    }
    out.print("</Editors>");
  }

  public void setClean(String id) {
    final CTabItem item = tabs.get(id);
    final TextEditor editor = editors.get(id);
    runOnDisplay(new Runnable() {
      public void run() {
        editor.setDirty(false);
        item.setText(editor.getLabel());
      }
    });
  }

  public void setDirty(String id) {
    final CTabItem item = tabs.get(id);
    final TextEditor editor = editors.get(id);
    runOnDisplay(new Runnable() {
      public void run() {
        editor.setDirty(true);
        item.setText("*" + editor.getLabel());
      }
    });
  }

}