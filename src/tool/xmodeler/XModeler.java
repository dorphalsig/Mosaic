package tool.xmodeler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import tool.clients.browser.ModelBrowserClient;
import tool.clients.diagrams.DiagramClient;
import tool.clients.dialogs.DialogsClient;
import tool.clients.editors.EditorClient;
import tool.clients.forms.FormsClient;
import tool.clients.menus.MenuClient;
import tool.clients.workbench.WorkbenchClient;
import tool.console.Console;
import xos.OperatingSystem;

import com.ceteva.oleBridge.OleBridgeClient;
import com.ceteva.undo.UndoClient;

public class XModeler {

  // XModeler is a tool that controls and is controlled by the XMF VM that runs
  // on the XMF operating system.

  public static String attributeValue(Node node, String name) {
    NamedNodeMap attrs = node.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Attr attribute = (Attr) attrs.item(i);
      if (attribute.getName().equals(name)) return attribute.getValue();
    }
    return null;
  }

  public static String attributeValue(Node node, String name, String defaultValue) {
    String value = attributeValue(node, name);
    if (value == null)
      return defaultValue;
    else return value;
  }

  private static Listener closeListener() {
    return new Listener() {
      public void handleEvent(Event event) {
        if (loadedImagePath == null) {
          WorkbenchClient.theClient().shutdownEvent();
        } else {
          WorkbenchClient.theClient().shutdownAndSaveEvent(loadedImagePath, inflationPath());
        }
        event.doit = false;
      }
    };
  }

  public static String encodeXmlAttribute(String str) {
    if (str == null) return null;

    int len = str.length();
    if (len == 0) return str;

    StringBuffer encoded = new StringBuffer();
    for (int i = 0; i < len; i++) {
      char c = str.charAt(i);
      if (c == '<')
        encoded.append("&lt;");
      else if (c == '\"')
        encoded.append("&quot;");
      else if (c == '>')
        encoded.append("&gt;");
      else if (c == '\'')
        encoded.append("&apos;");
      else if (c == '&')
        encoded.append("&amp;");
      else encoded.append(c);
    }

    return encoded.toString();
  }

  private static String getImage(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-image")) return args[i + 1];
    }
    return null;
  }

  public static Menu getMenuBar() {
    return menuBar;
  }

  public static Shell getXModeler() {
    return XModeler;
  }

  public static void inflate(String inflationPath) {
    try {
      File fXmlFile = new File(inflationPath);
      if (fXmlFile.exists()) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        String root = doc.getDocumentElement().getNodeName();
        if (root.equals("XModeler")) {
          ModelBrowserClient.theClient().inflateXML(doc);
          DiagramClient.theClient().inflateXML(doc);
          MenuClient.theClient().inflateXML(doc);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }

  private static String inflationPath() {
    if (loadedImagePath != null) {
      String name = loadedImagePath.substring(0, loadedImagePath.indexOf('.'));
      return name + ".xml";
    }
    return null;
  }

  private static String lookupArg(String string, String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-arg") && args[i + 1].startsWith("projects:")) { return args[i + 1].substring(9); }
    }
    return null;
  }

  public static void main(String[] args) {
    startXOS(args[0]);
    startXModeler();
    startClients();
    startDispatching();
  }

  private static boolean overwrite(final String file) {
    final boolean[] result = new boolean[] { false };
    XModeler.getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          result[0] = MessageDialog.openConfirm(XModeler, "Overwite", "Overwrite " + file);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
    return result[0];
  }

  public static void removeBusyInformation() {
    XModeler.setText("XModeler");
  }

  public static void saveInflator(final String inflationPath) {
    XModeler.getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          if (inflationPath != null && overwrite(inflationPath)) {
            File file = new File(inflationPath);
            FileOutputStream fout = new FileOutputStream(file);
            PrintStream out = new PrintStream(fout);
            out.print("<XModeler>");
            ModelBrowserClient.theClient().writeXML(out);
            DiagramClient.theClient().writeXML(out);
            MenuClient.theClient().writeXML(out);
            out.print("</XModeler>");
            out.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private static void setImage(String[] args) {
    String defaultImage = getImage(args);
    if (defaultImage == null) throw new Error("you have not supplied an image in the initialisation args:\n" + Arrays.toString(args));
    if (!new File(defaultImage).exists()) throw new Error("the default image file must exist: " + defaultImage);
    FileDialog dialog = new FileDialog(XModeler, SWT.OPEN);
    dialog.setText("Select the image file");
    dialog.setFilterExtensions(new String[] { "*.img" });
    dialog.setFileName(defaultImage);
    dialog.setFilterPath(projDir);
    String selectedImage = dialog.open();
    if (selectedImage != null && !selectedImage.equals(defaultImage)) {
      loadedImagePath = selectedImage;
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-image")) args[i + 1] = loadedImagePath;
      }
    }
  }

  private static void setProjectDirectory(String[] args) {
    projDir = lookupArg("projects", args);
    if (projDir == null) throw new Error("you have not set the project directory in the initialisation arguments:\n" + Arrays.toString(args));
  }

  public static void showBusyInformation(String info) {
    XModeler.setText("XModeler[" + info + "]");
  }

  public static void shutdown() {
    XModeler.getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          XModeler.dispose();
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  public static void startClients() {
    xos.newMessageClient("com.ceteva.text", new EditorClient());
    xos.newMessageClient("com.ceteva.mosaic", new WorkbenchClient());
    xos.newMessageClient("com.ceteva.menus", new MenuClient());
    xos.newMessageClient("com.ceteva.modelBrowser", new ModelBrowserClient());
    xos.newMessageClient("com.ceteva.diagram", new DiagramClient());
    xos.newMessageClient("com.ceteva.dialogs", new DialogsClient());
    xos.newMessageClient("com.ceteva.forms", new FormsClient());
    xos.newMessageClient("com.ceteva.undo", new UndoClient());
    xos.newMessageClient("com.ceteva.oleBridge", new OleBridgeClient());
  }

  public static void startDispatching() {
    while (!XModeler.isDisposed()) {
      if (!display.readAndDispatch()) display.sleep();
    }
    display.dispose();
    System.exit(0);
  }

  public static void startXModeler() {
    display = Display.getDefault();
    XModeler.setText("XModeler");
    Image windowIcon = new Image(XModeler.getDisplay(), "icons/shell/mosaic32.gif");
    XModeler.setImage(windowIcon);
    XModeler.setLayout(new FillLayout());
    XModeler.setLocation(TOOL_X, TOOL_Y);
    XModeler.setSize(new org.eclipse.swt.graphics.Point(TOOL_WIDTH, TOOL_HEIGHT));
    XModeler.addListener(SWT.Close, closeListener());
    menuBar = new Menu(XModeler, SWT.BAR);
    SashForm outerSash = new SashForm(XModeler, SWT.HORIZONTAL);
    SashForm leftSash = new SashForm(outerSash, SWT.VERTICAL);
    CTabFolder browserTabFolder = new CTabFolder(leftSash, SWT.BORDER);
    CTabFolder propertyTabFolder = new CTabFolder(leftSash, SWT.BORDER);
    ToolBar propertyToolbar = new ToolBar(propertyTabFolder, SWT.HORIZONTAL | SWT.FLAT);
    propertyTabFolder.setTopRight(propertyToolbar);
    leftSash.setWeights(new int[] { 3, 1 });
    ModelBrowserClient.start(browserTabFolder, SWT.LEFT);
    SashForm rightSash = new SashForm(outerSash, SWT.VERTICAL);
    outerSash.setWeights(new int[] { 1, 5 });
    CTabFolder editorTabFolder = new CTabFolder(rightSash, SWT.BORDER);
    EditorClient.start(editorTabFolder, SWT.BORDER);
    DiagramClient.start(editorTabFolder);
    FormsClient.start(propertyTabFolder, propertyToolbar, SWT.BORDER);
    Console.start(rightSash, SWT.BOTTOM);
    XModeler.open();
  }

  static void startXOS(String initFile) {
    final String[] args = xos.getInitArgs(initFile);
    setProjectDirectory(args);
    setImage(args);
    Thread t = new Thread() {
      public void run() {
        try {
          xos.init(args);
        } catch (Throwable t) {
          System.out.println(t);
          t.printStackTrace();
        }
      }
    };
    t.start();
  }

  static int             TOOL_X          = 100;

  static int             TOOL_Y          = 100;

  static int             TOOL_WIDTH      = 1000;

  static int             TOOL_HEIGHT     = 750;

  static OperatingSystem xos             = new OperatingSystem();

  static Shell           XModeler        = new Shell(SWT.BORDER | SWT.SHELL_TRIM);

  static Display         display;

  static Menu            menuBar;

  static String          projDir;

  static String          loadedImagePath = null;
}
