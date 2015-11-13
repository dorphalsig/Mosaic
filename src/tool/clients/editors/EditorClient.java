package tool.clients.editors;

import java.io.PrintStream;
import java.net.URL;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.Client;
import tool.clients.EventHandler;
import tool.clients.diagrams.DiagramClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class EditorClient extends Client implements LocationListener, CTabFolder2Listener {

  public static void start(CTabFolder tabFolder, int style) {
    EditorClient.tabFolder = tabFolder;
  }

  public static EditorClient theClient() {
    return theClient;
  }

  static final Color                   LINE_HIGHLIGHT = new Color(XModeler.getXModeler().getDisplay(), 192, 192, 192);
  static final Color                   RED            = new Color(XModeler.getXModeler().getDisplay(), 255, 0, 0);
  static final Color                   GREY           = new Color(XModeler.getXModeler().getDisplay(), 192, 192, 192);
  static final Color                   WHITE          = new Color(XModeler.getXModeler().getDisplay(), 255, 255, 255);
  static final Color                   GREEN          = new Color(XModeler.getXModeler().getDisplay(), 0, 170, 0);
  static final Color                   BLACK          = new Color(XModeler.getXModeler().getDisplay(), 0, 0, 0);

  static EditorClient                  theClient;
  static CTabFolder                    tabFolder;
  boolean                       browserLocked  = true;
  static Hashtable<String, CTabItem>   tabs           = new Hashtable<String, CTabItem>();
  static Hashtable<String, Browser>    browsers       = new Hashtable<String, Browser>();
  static Hashtable<String, TextEditor> editors        = new Hashtable<String, TextEditor>();

  public EditorClient() {
    super("com.ceteva.text");
    theClient = this;
    tabFolder.addCTabFolder2Listener(this);
  }

  private void addLineHighlight(Message message) {
    String id = message.args[0].strValue();
    int line = message.args[1].intValue;
    addLineHighlight(id, line);
  }

  private void addLineHighlight(final String id, final int line) {
    runOnDisplay(new Runnable() {
      public void run() {
        if (editors.containsKey(id))
          editors.get(id).addLineHighlight(line);
        else System.err.println("cannot find editor: " + id);
      }
    });
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
    else System.err.println("unknown color: " + color);
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
    else System.err.println("unknown color: " + color);
  }

  public Value callMessage(Message message) {
    if (message.hasName("getWelcomePage")) {
      // help page if it is available.
      URL location = EditorClient.class.getProtectionDomain().getCodeSource().getLocation();
      String path = location.toString();
      path = path.substring(0, path.length() - 4); // delete "/bin" from string
      path += "web/index.html";
//      System.err.println("getWelcomePage: >" + path + "<");
      return new Value(path);
    }
    if (message.hasName("getText")) { return getText(message); }
    return super.callMessage(message);
  }

  @Override
  public void changed(LocationEvent event) {
  }

  @Override
  public void changing(LocationEvent event) {
//	  System.err.println("changing: " + event);
//	  System.err.println("browserLocked: " + browserLocked);
    if (browserLocked) {
      event.doit = false;
      Browser browser = (Browser) event.widget;
      EventHandler handler = getHandler();
      Message message = handler.newMessage("urlRequest", 2);
      message.args[0] = new Value(getId(browser));
      message.args[1] = new Value(event.location);
//      System.err.println("message: " + message);
      handler.raiseEvent(message);
    } else {
    	browserLocked = true;
    }
  }

  private void clearHighlights(Message message) {
    String id = message.args[0].strValue();
    clearHighlights(id);
  }

  private void clearHighlights(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        if (editors.containsKey(id))
          editors.get(id).clearHighlights();
        else System.err.println("cannot find editor: " + id);
      }
    });
  }

  public void close(CTabFolderEvent event) {
    // Careful because the diagrams and files share the same tab folder...
    CTabItem item = (CTabItem) event.item;
    String id = getId(item);
    if (id != null && (editors.containsKey(id) || browsers.containsKey(id))) {
      EventHandler handler = getHandler();
      Message message = handler.newMessage("textClosed", 1);
      message.args[0] = new Value(id);
      handler.raiseEvent(message);
      editors.remove(id);
      browsers.remove(id);
      tabs.remove(id);
    } else {
      DiagramClient.theClient().close(event);
    }
  }

  private String getId(Browser browser) {
    for (String id : browsers.keySet())
      if (browsers.get(id) == browser) return id;
    return null;
  }

  private String getId(CTabItem item) {
    for (String id : tabs.keySet())
      if (tabs.get(id) == item) return id;
    return null;
  }

  private Value getText(final Message message) {
    final String id = message.args[0].strValue();
    final Value[] result = new Value[] { null };
    runOnDisplay(new Runnable() {
      public void run() {
        for (String editorId : editors.keySet()) {
          if (id.equals(editorId)) {
            result[0] = new Value(editors.get(editorId).getString());
          }
        }
      }
    });
    if (result[0] == null)
      throw new Error("Cannot find editor with id " + id);
    else return result[0];
  }

  private void inflateBrowser(Node browser) {
    String id = XModeler.attributeValue(browser, "id");
    String label = XModeler.attributeValue(browser, "label");
    String tooltip = XModeler.attributeValue(browser, "toolTip");
    String url = XModeler.attributeValue(browser, "url");
    String text = XModeler.attributeValue(browser, "text");
    newBrowser(id, label, tooltip, url, text);
  }

  private void inflateEditorElement(Node editor) {
    if (editor.getNodeName().equals("TextEditor")) inflateTextEditor(editor);
    if (editor.getNodeName().equals("Browser")) inflateBrowser(editor);
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
    } else System.err.println("expecting exactly 1 editor client got: " + editorClients.getLength());
  }

  public void maximize(CTabFolderEvent event) {
  }

  public void minimize(CTabFolderEvent event) {
  }

  private void newBrowser(Message message) {
    String id = message.args[0].strValue();
    String label = message.args[1].strValue();
    String tooltip = message.args[2].strValue();
    String url = message.args[3].strValue();
    newBrowser(id, label, tooltip, url, "");
  }

  private void newBrowser(final String id, final String label, String tooltip, final String url, final String text) {
    runOnDisplay(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label);
        tabItem.setShowClose(true);
        tabs.put(id, tabItem);
        Browser browser = new Browser(tabFolder, SWT.BORDER);
        tabItem.setControl(browser);
        browser.setText(text);
        browser.setJavascriptEnabled(true);
        browserLocked = false;
        if (isURL(url)) {
        	browser.setUrl(url);
        }
        browsers.put(id, browser);
        browser.setVisible(true);
        browser.addLocationListener(EditorClient.this);
        tabFolder.setSelection(tabItem);
      }
    });
  }

  private boolean isURL(String url) {
    return url.startsWith("http://") || url.startsWith("file:/");
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
        tabItem.setShowClose(true);
        tabs.put(id, tabItem);
        TextEditor editor = new TextEditor(id, label, tabFolder, editable, lineNumbers, text);
        tabItem.setControl(editor.getText());
        editors.put(id, editor);
        tabFolder.setSelection(tabItem);
      }
    });
  }

  public boolean processMessage(Message message) {
    return false;
  }

  public void restore(CTabFolderEvent event) {
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
    else if (message.hasName("setName"))
      setName(message);
    else if (message.hasName("showLine"))
      showLine(message);
    else if (message.hasName("addLineHighlight"))
      addLineHighlight(message);
    else if (message.hasName("clearHighlights"))
      clearHighlights(message);
    else if (message.hasName("setFocus"))
      setFocus(message);
    else super.sendMessage(message);
  }

  private void setName(Message message) {
    String id = message.args[0].strValue();
    String name = message.args[1].strValue();
    setName(id, name);
  }

  private void setName(final String id, final String name) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (String tid : tabs.keySet()) {
          if (tid.equals(id)) tabs.get(id).setText(name);
        }
      }
    });
  }

  private void setClean(Message message) {
    String id = message.args[0].strValue();
    setClean(id);
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

  private void setDirty(Message message) {
    String id = message.args[0].strValue();
    setDirty(id);
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

  private void setFocus(Message message) {
    String id = message.args[0].strValue();
    setFocus(id);
  }

  private void setFocus(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        if (tabs.containsKey(id))
          tabFolder.setSelection(tabs.get(id));
        else System.err.println("cannot set focus to editor: " + id);
      }
    });
  }

  private void setText(Message message) {
    final Value id = message.args[0];
    final Value text = message.args[1];
    if (editors.containsKey(id.strValue())) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          TextEditor editor = editors.get(id.strValue());
          editor.setString(text.strValue());
          tabFolder.setFocus();
          tabFolder.setSelection(tabs.get(id.strValue()));
        }
      });
    } else System.err.println("cannot find text editor " + id);
  }

  private void setUrl(Message message) {
    final Value id = message.args[0];
    final Value url = message.args[1];
    if (browsers.containsKey(id.strValue())) {
      final Browser browser = browsers.get(id.strValue());
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          browserLocked = false;
          String s = url.strValue();
//          System.err.println("message.url: >" + url.strValue() +"<");
          if (isLikelyToBeHTML(s)) //s.startsWith("<html>"))
            browser.setText(s);
          else browser.setUrl(url.strValue()); // +" "?
          
          tabFolder.setFocus();
          tabFolder.setSelection(tabs.get(id.strValue()));
        }

		private boolean isLikelyToBeHTML(String s) {
//			System.err.println("isLikelyToBeHTML?" + url.strValue());
			s = s.trim();
			s = s.toLowerCase();
			if(s.startsWith("<html>")) return true;
			if(s.startsWith("<!doctype html")) return true;
			return false;
		}
		
      });
    } else System.err.println("cannot find browser " + id);
  }

  private void showLine(Message message) {
    String id = message.args[0].strValue();
    int line = message.args[1].intValue;
    showLine(id, line);
  }

  private void showLine(final String id, final int line) {
    runOnDisplay(new Runnable() {
      public void run() {
        if (editors.containsKey(id))
          editors.get(id).showLine(line);
        else System.err.println("cannot find editor: " + id);
      }
    });
  }

  public void showList(CTabFolderEvent event) {
  }

  public void writeXML(PrintStream out) {
    out.print("<Editors>");
    for (String id : editors.keySet()) {
      CTabItem item = tabs.get(id);
      TextEditor editor = editors.get(id);
      editor.writeXML(out, tabFolder.getSelection() == item, item.getText(), item.getToolTipText());
    }
    for (String id : browsers.keySet()) {
      CTabItem tab = tabs.get(id);
      String label = tab.getText();
      String tooltip = tab.getToolTipText();
      Browser browser = browsers.get(id);
      String url = browser.getUrl();
      String text = browser.getText();
      out.print("<Browser id='" + id + "' label='" + label + "' tooltip='" + tooltip + "' url='" + url + "' text='" + XModeler.encodeXmlAttribute(text) + "'/>");
    }
    out.print("</Editors>");
  }

}