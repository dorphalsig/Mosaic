package tool.clients.editors;

import java.net.URL;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;

import tool.clients.Client;
import xos.Message;
import xos.Value;

public class EditorClient extends Client {

  static CTabFolder                    tabFolder;
  static Hashtable<String, CTabItem>   tabs     = new Hashtable<String, CTabItem>();
  static Hashtable<String, Browser>    browsers = new Hashtable<String, Browser>();
  static Hashtable<String, TextEditor> editors  = new Hashtable<String, TextEditor>();

  public EditorClient() {
    super("com.ceteva.text");
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
    else super.sendMessage(message);
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

  private void newTextEditor(Message message) {
    final Value id = message.args[0];
    final Value label = message.args[1];
    final Value toolTip = message.args[2];
    final Value editable = message.args[3];
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label.strValue());
        tabItem.setToolTipText(toolTip.strValue());
        tabs.put(id.strValue(), tabItem);
        TextEditor editor = new TextEditor(tabFolder, editable.boolValue);
        tabItem.setControl(editor.getText());
        editors.put(id.strValue(), editor);
      }
    });
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

  public boolean processMessage(Message message) {
    System.out.println(this + " <- " + message);
    return false;
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
    } else return super.processCall(message);
  }

  public static void start(CTabFolder tabFolder, int style) {
    EditorClient.tabFolder = tabFolder;
  }

}