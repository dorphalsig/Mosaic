package com.ceteva.text;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.PartInitException;

import uk.ac.mdx.xmf.swt.client.Client;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import uk.ac.mdx.xmf.swt.client.IdManager;
import uk.ac.mdx.xmf.swt.demo.Main;
import xos.Message;
import xos.Value;

import com.ceteva.text.htmlviewer.HTMLViewer;
import com.ceteva.text.texteditor.TextEditor;
import com.ceteva.text.texteditor.TextEditorInput;
import com.ceteva.text.texteditor.TextStorage;

// TODO: Auto-generated Javadoc
/**
 * The Class EditorClient.
 */
public class EditorClient extends Client {

  /** The handler. */
  // public EventHandler handler = null;

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Client#setEventHandler(uk.ac.mdx.xmf.swt.client.EventHandler)
   */
  @Override
  public void setEventHandler(EventHandler eventsOut) {
    // handler = eventsOut;
  }

  /**
   * Instantiates a new editor client.
   */
  public EditorClient() {
    super("com.ceteva.text");
  }

  /**
   * New browser.
   *
   * @param message
   *          the message
   * @return true, if successful
   */
  public boolean newBrowser(Message message) {
    String identity = message.args[0].strValue();
    String name = message.args[1].strValue();
    String tooltip = message.args[2].strValue();
    String url = message.args[3].strValue();
    showBrowser(identity, name, tooltip, tooltip);
    return true;
  }

  /**
   * New ole editor.
   *
   * @param message
   *          the message
   * @return true, if successful
   */
  public boolean newOleEditor(Message message) {
    // TextPlugin textManager = TextPlugin.getDefault();
    // IWorkbenchPage page = textManager.getWorkbench()
    // .getActiveWorkbenchWindow().getActivePage();
    // String identity = message.args[0].strValue();
    // String type = message.args[1].strValue();
    // String file = message.args[2].strValue();
    // OLEViewerInput input = new OLEViewerInput(identity, type, file);
    // try {
    // OLEViewer viewer = (OLEViewer) page.openEditor(input,
    // "com.ceteva.text.OLEViewer");
    // viewer.setEventHandler(handler);
    // } catch (PartInitException pie) {
    // System.out.println(pie);
    // }
    return true;
  }

  /**
   * Show browser.
   *
   * @param identity
   *          the identity
   * @param title
   *          the title
   * @param tooltip
   *          the tooltip
   * @param urls
   *          the urls
   */
  public void showBrowser(String identity, String title, String tooltip, String urls) {
    // // Create a web browser
    // if (urls.length() > 1)
    {
      CTabItem tabItem = new CTabItem(Main.tabFolderDiagram, SWT.BORDER);
      tabItem.setText(title);
      Canvas c = new Canvas(Main.tabFolderDiagram, SWT.BORDER);
      tabItem.setControl(c);

      HTMLViewer viewer = new HTMLViewer();
      viewer.init(identity);
      viewer.setEventHandler(handler);
      viewer.createPartControl(c);
      viewer.setName(title);
      viewer.setToolTip(tooltip);
      if (!urls.equals("")) viewer.setURL(urls);

      // Browser browser = new Browser(c, SWT.BORDER);
      // viewer.setBrowser(browser);
      // if (urls.length() > 1)
      // browser.setUrl(urls);
      // // browser.setText(urls);
      // browser.setBounds(Main.tabFolderDiagram.getBounds());
      // browser.setLocation(0, 0);
      // browser.layout(true, true);

      tabItem.addDisposeListener(new DisposeListener() {

        @Override
        public void widgetDisposed(DisposeEvent arg0) {
          // viewer.dispose();
          Main.numberOfAddingItem--;
        }

      });

      Main.tabFolderDiagram.setSelection(tabItem);
      Main.numberOfAddingItem++;
    }
  }

  /*
   * public void showBrowser(String identity, String title, String tooltip, String urls) { // // Create a web browser // if (urls.length() > 1) { CTabItem tabItem = new
   * CTabItem(Main.tabFolderDiagram, SWT.BORDER); tabItem.setText(title); Canvas c = new Canvas(Main.tabFolderDiagram, SWT.BORDER); tabItem.setControl(c);
   * 
   * final HTMLViewer viewer = new HTMLViewer(); viewer.init(identity); viewer.setEventHandler(handler);
   * 
   * Browser browser = new Browser(c, SWT.BORDER); viewer.setBrowser(browser); if (urls.length() > 1) browser.setUrl(urls); // browser.setText(urls);
   * browser.setBounds(Main.tabFolderDiagram.getBounds()); browser.setLocation(0, 0); browser.layout(true, true);
   * 
   * tabItem.addDisposeListener(new DisposeListener() {
   * 
   * @Override public void widgetDisposed(DisposeEvent arg0) { // viewer.dispose(); Main.numberOfAddingItem--; }
   * 
   * });
   * 
   * Main.tabFolderDiagram.setSelection(tabItem); Main.numberOfAddingItem++; } }
   */
  /**
   * New text editor.
   *
   * @param message
   *          the message
   * @return true, if successful
   */
  public boolean newTextEditor(Message message) {
    if (message.arity == 4 || message.arity == 5) {
      String identity = message.args[0].strValue();
      String name = message.args[1].strValue();
      String tooltip = message.args[2].strValue();
      boolean editable = message.args[3].boolValue;
      // TextPlugin textManager = TextPlugin.getDefault();
      // IWorkbenchPage page = textManager.getWorkbench()
      // .getActiveWorkbenchWindow().getActivePage();
      // //
      TextStorage storage = new TextStorage(identity);
      TextEditorInput input = new TextEditorInput(storage);
      if (handler != null) {
        // TextEditor newEditor = (TextEditor) page.openEditor(input,
        // "com.ceteva.text.TextEditor");
        TextEditor newEditor = new TextEditor();
        try {
          newEditor.init(Main.sectionTopMiddle, input, name);
        } catch (PartInitException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        newEditor.setName(name);

        newEditor.setToolTip(tooltip);
        newEditor.setEditable(editable);
        newEditor.setEventHandler(handler);
        if (message.arity == 5) {
          String icon = message.args[4].strValue();
          if (!icon.endsWith(".gif")) icon = icon + ".gif";
          newEditor.setImage(IconManager.getImageFromFileAbsolute(icon));
        }
      }
      return true;
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Client#processCall(xos.Message)
   */
  public Value processCall(Message message) {
    if (message.hasName("getWelcomePage")) {

      // help page if it is available.
      URL location = EditorClient.class.getProtectionDomain().getCodeSource().getLocation();
      String path = location.toString();
      path = path.substring(0, path.length() - 4); // delete "/bin" from string
      path += "file/Welcome/welcome.html";
      return new Value(path);
    }
    return IdManager.processCall(message);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Client#processMessage(xos.Message)
   */
  public boolean processMessage(Message message) {
    if (message.hasName("newTextEditor")) return newTextEditor(message);
    if (message.hasName("newBrowser")) return newBrowser(message);
    if (message.hasName("newOleEditor")) return this.newOleEditor(message);
    return IdManager.processMessage(message);
  }

}