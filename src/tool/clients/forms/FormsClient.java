package tool.clients.forms;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tool.clients.Client;
import tool.clients.EventHandler;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class FormsClient extends Client implements CTabFolder2Listener {

  public static Font getFormLabelFont() {
    return formLabelFont;
  }

  public static Font getFormTextFieldFont() {
    return formLabelFont;
  }

  public static void select() {
    CTabItem selectedItem = tabFolder.getSelection();
    for (String id : tabs.keySet()) {
      if (tabs.get(id) == selectedItem) {
        for (ToolItem item : toolBar.getItems())
          item.dispose();
        FormTools formTools = FormsClient.theClient().getFormTools(id);
        formTools.populateToolBar(toolBar);
      }
    }
  }

  public static void start(CTabFolder tabFolder, ToolBar toolBar, int style) {
    FormsClient.tabFolder = tabFolder;
    FormsClient.toolBar = toolBar;
    tabFolder.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent event) {
      }

      public void widgetSelected(SelectionEvent event) {
        select();
      }
    });
  }

  public static FormsClient theClient() {
    return theClient;
  }

  static final Color                  WHITE             = new Color(null, 255, 255, 255);
  static FormsClient                  theClient;
  static CTabFolder                   tabFolder;
  static ToolBar                      toolBar;

  static Hashtable<String, CTabItem>  tabs              = new Hashtable<String, CTabItem>();
  static Vector<Form>                 forms             = new Vector<Form>();
  static Hashtable<String, FormTools> toolDefs          = new Hashtable<String, FormTools>();
  static Font                         formLabelFont     = Display.getDefault().getSystemFont();//new Font(Display.getDefault(), new FontData("Monaco", 12, SWT.NO));
  static Font                         formTextFieldFont = Display.getDefault().getSystemFont();//new Font(Display.getDefault(), new FontData("Monaco", 12, SWT.NO));

  public FormsClient() {
    super("com.ceteva.forms");
    theClient = this;
    tabFolder.addCTabFolder2Listener(this);
  }

  private void addComboItem(Message message) {
    String parentId = message.args[0].strValue();
    String value = message.args[1].strValue();
    addComboItem(parentId, value);
  }

  private void addComboItem(final String parentId, final String value) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.addComboItem(parentId, value);
      }
    });
  }

  private void addItem(Message message) {
    if (message.arity == 2)
      addComboItem(message);
    else addListItem(message);
  }

  private void addListItem(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    String value = message.args[2].strValue();
    addListItem(parentId, id, value);
  }

  private void addListItem(final String parentId, final String id, final String value) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.addListItem(parentId, id, value);
      }
    });
  }

  private void addNodeWithIcon(Message message) {
    String parentId = message.args[0].strValue();
    String nodeId = message.args[1].strValue();
    String text = message.args[2].strValue();
    boolean editable = message.args[3].boolValue;
    String icon = message.args[4].strValue();
    int index = -1;
    if (message.arity == 6) index = message.args[5].intValue;
    addNodeWithIcon(parentId, nodeId, text, editable, icon, index);
  }

  private void addNodeWithIcon(final String parentId, final String nodeId, final String text, final boolean editable, final String icon, final int index) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newNodeWithIcon(parentId, nodeId, text, editable, icon, index);
      }
    });
  }

  public Value callMessage(Message message) {
    if (message.hasName("getText"))
      return getText(message);
    else return super.callMessage(message);
  }

  private Value getText(Message message) {
    final String id = message.args[0].strValue();
    final String[] text = new String[] { "" };
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms) {
          String textIn = form.getText(id);
          if (textIn != null) text[0] = textIn;
        }
      }
    });
    return new Value(text[0]);
  }

  private void clearForm(Message message) {
    String id = message.args[0].strValue();
    final Form form = getForm(id);
    if (form != null) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          form.clear();
        }
      });
    } else System.err.println("cannot find form to clear " + id);
  }

  private Form getForm(String id) {
    for (Form form : forms)
      if (form.getId().equals(id)) return form;
    return null;
  }

  private FormTools getFormTools(String id) {
    if (toolDefs.containsKey(id))
      return toolDefs.get(id);
    else {
      FormTools formTools = new FormTools(id);
      toolDefs.put(id, formTools);
      return formTools;
    }
  }

  private void inflateButton(String parentId, Node button) {
    String id = XModeler.attributeValue(button, "id");
    String text = XModeler.attributeValue(button, "text");
    int x = Integer.parseInt(XModeler.attributeValue(button, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(button, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(button, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(button, "height"));
    newButton(parentId, id, text, x, y, width, height);
  }

  private void inflateCheck(String parentId, Node check) {
    String id = XModeler.attributeValue(check, "id");
    String text = XModeler.attributeValue(check, "text");
    int x = Integer.parseInt(XModeler.attributeValue(check, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(check, "y"));
    boolean checked = XModeler.attributeValue(check, "checked").equals("true");
    newCheckBox(parentId, id, x, y, checked);
  }

  private void inflateCombo(String parentId, Node combo) {
    String id = XModeler.attributeValue(combo, "id");
    String string = XModeler.attributeValue(combo, "string");
    int x = Integer.parseInt(XModeler.attributeValue(combo, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(combo, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(combo, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(combo, "height"));
    boolean editable = XModeler.attributeValue(combo, "editable").equals("true");
    newComboBox(parentId, id, x, y, width, height);
    NodeList items = combo.getChildNodes();
    for (int i = 0; i < items.getLength(); i++) {
      Node item = items.item(i);
      addComboItem(id, XModeler.attributeValue(item, "item"));
    }
  }

  private void inflateForm(Node form) {
    String id = XModeler.attributeValue(form, "id");
    String label = XModeler.attributeValue(form, "label");
    boolean selected = XModeler.attributeValue(form, "selected").equals("true");
    newForm(id, label, selected);
    NodeList elements = form.getChildNodes();
    for (int i = 0; i < elements.getLength(); i++)
      inflateFormElement(id, elements.item(i));
  }

  private void inflateFormClientElement(Node element) {
    if (element.getNodeName().equals("Form")) inflateForm(element);
    if (element.getNodeName().equals("FormTools")) inflateFormTools(element);
  }

  private void inflateFormElement(String parentId, Node element) {
    if (element.getNodeName().equals("TextField"))
      inflateTextField(parentId, element);
    else if (element.getNodeName().equals("Label"))
      inflateLabel(parentId, element);
    else if (element.getNodeName().equals("TextBox"))
      inflateTextBox(parentId, element);
    else if (element.getNodeName().equals("Combo"))
      inflateCombo(parentId, element);
    else if (element.getNodeName().equals("Check"))
      inflateCheck(parentId, element);
    else if (element.getNodeName().equals("Button"))
      inflateButton(parentId, element);
    else if (element.getNodeName().equals("Tree"))
      inflateTree(parentId, element);
    else if (element.getNodeName().equals("List"))
      inflateList(parentId, element);
    else System.err.println("Unknown type of form element: " + element.getNodeName());
  }

  private void inflateFormTools(Node formTools) {
    String id = XModeler.attributeValue(formTools, "id");
    FormTools tools = getFormTools(id);
    NodeList children = formTools.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      String toolId = XModeler.attributeValue(node, "id");
      String event = XModeler.attributeValue(node, "event");
      String icon = XModeler.attributeValue(node, "icon");
      tools.addTool(event, toolId);
    }
  }

  private void inflateLabel(String parentId, Node label) {
    String id = XModeler.attributeValue(label, "id");
    String string = XModeler.attributeValue(label, "string");
    int x = Integer.parseInt(XModeler.attributeValue(label, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(label, "y"));
    newText(parentId, id, string, x, y);
    getForm(parentId).getLabels().get(id).setText(string);
  }

  private void inflateList(String parentId, Node list) {
    String id = XModeler.attributeValue(list, "id");
    int x = Integer.parseInt(XModeler.attributeValue(list, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(list, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(list, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(list, "height"));
    newList(parentId, id, x, y, width, height);
    NodeList items = list.getChildNodes();
    for (int i = 0; i < items.getLength(); i++) {
      String itemId = XModeler.attributeValue(items.item(i), "id");
      String value = XModeler.attributeValue(items.item(i), "value");
      addListItem(parentId, itemId, value);
    }
  }

  private void inflateTextBox(String parentId, Node textBox) {
    String id = XModeler.attributeValue(textBox, "id");
    String string = XModeler.attributeValue(textBox, "string");
    int x = Integer.parseInt(XModeler.attributeValue(textBox, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(textBox, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(textBox, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(textBox, "height"));
    boolean editable = XModeler.attributeValue(textBox, "editable").equals("true");
    newTextBox(parentId, id, x, y, width, height, editable);
    getForm(parentId).getBoxes().get(id).setText(string);
  }

  private void inflateTextField(String parentId, Node textField) {
    String id = XModeler.attributeValue(textField, "id");
    String string = XModeler.attributeValue(textField, "string");
    int x = Integer.parseInt(XModeler.attributeValue(textField, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(textField, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(textField, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(textField, "height"));
    boolean editable = XModeler.attributeValue(textField, "editable").equals("true");
    newTextField(parentId, id, x, y, width, height, editable);
    getForm(parentId).getTextFields().get(id).setText(string);
  }

  private void inflateTree(String parentId, Node tree) {
    String id = XModeler.attributeValue(tree, "id");
    int x = Integer.parseInt(XModeler.attributeValue(tree, "x"));
    int y = Integer.parseInt(XModeler.attributeValue(tree, "y"));
    int width = Integer.parseInt(XModeler.attributeValue(tree, "width"));
    int height = Integer.parseInt(XModeler.attributeValue(tree, "height"));
    newTree(parentId, id, x, y, width, height, true);
    inflateTreeItems(tree);
  }

  private void inflateTreeItems(Node node) {
    String id = XModeler.attributeValue(node, "id");
    NodeList children = node.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      String childId = XModeler.attributeValue(child, "id");
      String text = XModeler.attributeValue(child, "text");
      String image = XModeler.attributeValue(child, "image");
      boolean expanded = XModeler.attributeValue(child, "expanded").equals("true");
      addNodeWithIcon(id, childId, text, expanded, image, i);
      inflateTreeItems(child);
    }
  }

  public void inflateXML(final Document doc) {
    runOnDisplay(new Runnable() {
      public void run() {
        try {
          NodeList formClients = doc.getElementsByTagName("Forms");
          if (formClients.getLength() == 1) {
            Node formClient = formClients.item(0);
            NodeList forms = formClient.getChildNodes();
            for (int i = 0; i < forms.getLength(); i++) {
              Node element = forms.item(i);
              inflateFormClientElement(element);
            }
          } else System.err.println("expecting exactly 1 editor client got: " + formClients.getLength());
        } catch (Throwable t) {
          t.printStackTrace(System.err);
        }
      }
    });
  }

  private void newButton(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    String label = message.args[2].strValue();
    int x = message.args[3].intValue;
    int y = message.args[4].intValue;
    int width = message.args[5].intValue;
    int height = message.args[6].intValue;
    newButton(parentId, id, label, x, y, width, height);
  }

  private void newButton(final String parentId, final String id, final String label, final int x, final int y, final int width, final int height) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newButton(parentId, id, label, x, y, width, height);
      }
    });
  }

  private void newCheckBox(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    boolean checked = message.args[4].boolValue;
    newCheckBox(parentId, id, x, y, checked);
  }

  private void newCheckBox(final String parentId, final String id, final int x, final int y, final boolean checked) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newCheckBox(parentId, id, x, y, checked);
      }
    });
  }

  private void newComboBox(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    int width = message.args[4].intValue;
    int height = message.args[5].intValue;
    newComboBox(parentId, id, x, y, width, height);
  }

  private void newComboBox(final String parentId, final String id, final int x, final int y, final int width, final int height) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newComboBox(parentId, id, x, y, width, height);
      }
    });
  }

  private void newForm(Message message) {
    String id = message.args[0].strValue();
    String type = message.args[1].strValue(); // needed?
    String label = message.args[2].strValue();
    newForm(id, label, true);
  }

  private void newForm(final String id, final String label, final boolean selected) {
    runOnDisplay(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label);
        tabs.put(id, tabItem);
        Form form = new Form(tabFolder, id);
        tabItem.setControl(form.getForm());
        tabItem.setShowClose(true);
        forms.add(form);
        if (selected) tabFolder.setSelection(tabItem);
      }
    });
  }

  private void newList(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    int width = message.args[4].intValue;
    int height = message.args[5].intValue;
    newList(parentId, id, x, y, width, height);
  }

  private void newList(String parentId, String id, int x, int y, int width, int height) {
    for (Form form : forms)
      form.newList(parentId, id, x, y, width, height);
  }

  private void newText(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    String string = message.args[2].strValue();
    int x = message.args[3].intValue;
    int y = message.args[4].intValue;
    newText(parentId, id, string, x, y);
  }

  private void newText(final String parentId, final String id, final String string, final int x, final int y) {
    final Form form = getForm(parentId);
    if (form != null) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          form.newText(id, string, x, y);
        }
      });
    } else System.err.println("cannot find text parent " + parentId);
  }

  private void newTextBox(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    int width = message.args[4].intValue;
    int height = message.args[5].intValue;
    boolean editable = message.args[6].boolValue;
    newTextBox(parentId, id, x, y, width, height, editable);
  }

  private void newTextBox(final String parentId, final String id, final int x, final int y, final int width, final int height, final boolean editable) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newTextBox(parentId, id, x, y, width, height, editable);
      }
    });
  }

  private void newTextField(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    int width = message.args[4].intValue;
    int height = message.args[5].intValue;
    boolean editable = message.args[6].boolValue;
    newTextField(parentId, id, x, y, width, height, editable);
  }

  private void newTextField(final String parentId, final String id, final int x, final int y, final int width, final int height, final boolean editable) {
	final Form form = getForm(parentId);
    if (form != null) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          form.newTextField(id, x, y, width, height, editable);
        }
      });
    } else System.err.println("cannot find text field parent " + parentId);
  }

  private void newTree(Message message) {
    String parentId = message.args[0].strValue();
    String id = message.args[1].strValue();
    int x = message.args[2].intValue;
    int y = message.args[3].intValue;
    int width = message.args[4].intValue;
    int height = message.args[5].intValue;
    boolean editable = message.args[6].boolValue;
    newTree(parentId, id, x, y, width, height, editable);
  }

  private void newTree(final String parentId, final String id, final int x, final int y, final int width, final int height, final boolean editable) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.newTree(parentId, id, x, y, width, height, editable);
      }
    });
  }

  public boolean processMessage(Message message) {
    return false;
  }

  private void selectForm(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        if (tabs.containsKey(id))
          tabFolder.setSelection(tabs.get(id));
        else System.err.println("cannot find form: " + id);
      }
    });
  }

  private void maximiseToCanvas(Message message) {
	    final String id = message.args[0].strValue();
	    runOnDisplay(new Runnable() {
	        public void run() {
	          for (Form form : forms)
	            form.maximiseToCanvas(id);
	        }
	      });
  }

  public void sendMessage(final Message message) {
System.err.println("####### MESSAGE TO FORM CLIENT: "+message.output());
    if (message.hasName("newForm"))
      newForm(message);
    else if (message.hasName("setTool"))
      setTool(message);
    else if (message.hasName("newText"))
      newText(message);
    else if (message.hasName("setText"))
      setText(message);
    else if (message.hasName("newTextField"))
      newTextField(message);
    else if (message.hasName("clearForm"))
      clearForm(message);
    else if (message.hasName("newList"))
      newList(message);
    else if (message.hasName("addItem"))
      addItem(message);
    else if (message.hasName("newTextBox"))
      newTextBox(message);
    else if (message.hasName("newComboBox"))
      newComboBox(message);
    else if (message.hasName("setSelection"))
      setSelection(message);
    else if (message.hasName("newCheckBox"))
      newCheckBox(message);
    else if (message.hasName("newButton"))
      newButton(message);
    else if (message.hasName("newTree"))
      newTree(message);
    else if (message.hasName("addNodeWithIcon"))
      addNodeWithIcon(message);
    else if (message.hasName("setVisible"))
      setVisible(message);
    else if (message.hasName("clear"))
      clear(message);
    else if (message.hasName("check"))
      check(message);
    else if (message.hasName("uncheck"))
      uncheck(message);
    else if (message.hasName("maximiseToCanvas"))
      maximiseToCanvas(message);
    else {
//System.out.println("------- UNKNOWN");    	
    	super.sendMessage(message);
    }
  }

  private void check(Message message) {
    String id = message.args[0].strValue();
    check(id);
  }

  private void check(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.check(id);
      }
    });
  }

  private void uncheck(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.uncheck(id);
      }
    });
  }

  private void uncheck(Message message) {
    String id = message.args[0].strValue();
    uncheck(id);
  }

  private void clear(Message message) {
    String id = message.args[0].strValue();
    clear(id);
  }

  private void clear(final String id) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms) {
          form.clear(id);
        }
      }
    });
  }

  private void setSelection(Message message) {
    String comboId = message.args[0].strValue();
    int index = message.args[1].intValue;
    setSelection(comboId, index);
  }

  private void setSelection(final String comboId, final int index) {
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms)
          form.setSelection(comboId, index);
      }
    });
  }

  private void setText(Message message) {
    final Value id = message.args[0];
    final Value text = message.args[1];
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms) {
          form.setText(id.strValue(), text.strValue());
        }
      }
    });
  }

  private void setTool(Message message) {
    Value id = message.args[0];
    Value name = message.args[1];
    Value enabled = message.args[2];
    if (enabled.boolValue) {
      if (tabs.containsKey(id.strValue())) {
        FormTools formTools = getFormTools(id.strValue());
        formTools.addTool(name.strValue(), id.strValue());
      } else System.err.println("cannot find form " + id);
    }
  }

  private void setVisible(Message message) {
    String id = message.args[0].strValue();
    selectForm(id);
  }

  public void toolItemEvent(String event, String id) {
    Message m = getHandler().newMessage(event, 1);
    Value v = new Value(id);
    m.args[0] = v;
    getHandler().raiseEvent(m);
  }

  public void writeXML(PrintStream out) {
    out.print("<Forms>");
    for (Form form : forms)
      form.writeXML(out, tabFolder.getSelection() == tabs.get(form.getId()), tabs.get(form.getId()).getText());
    for (FormTools tools : toolDefs.values())
      tools.writeXML(out);
    out.print("</Forms>");
  }

  public void close(CTabFolderEvent event) {
    CTabItem item = (CTabItem) event.item;
    String id = getId(item);
    if (id != null && getForm(id) != null) {
      EventHandler handler = getHandler();
      Message message = handler.newMessage("formClosed", 1);
      message.args[0] = new Value(id);
      handler.raiseEvent(message);
      forms.remove(getForm(id));
      tabs.remove(id);
    }
  }

  private String getId(CTabItem item) {
    for (String id : tabs.keySet())
      if (tabs.get(id).equals(item)) return id;
    return null;
  }

  public void maximize(CTabFolderEvent event) {

  }

  public void minimize(CTabFolderEvent event) {

  }

  public void restore(CTabFolderEvent event) {

  }

  public void showList(CTabFolderEvent event) {

  }

  public void doubleClick(String id) {
    EventHandler handler = getHandler();
    Message message = handler.newMessage("doubleSelected", 1);
    message.args[0] = new Value(id);
    handler.raiseEvent(message);
  }
}