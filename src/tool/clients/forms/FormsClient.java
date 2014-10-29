package tool.clients.forms;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import tool.clients.Client;
import xos.Message;
import xos.Value;

public class FormsClient extends Client {

  static final Color                  WHITE             = new Color(null, 255, 255, 255);
  static FormsClient                  theClient;
  static CTabFolder                   tabFolder;
  static ToolBar                      toolBar;
  static Hashtable<String, CTabItem>  tabs              = new Hashtable<String, CTabItem>();
  static Hashtable<String, Form>      forms             = new Hashtable<String, Form>();
  static Hashtable<String, FormTools> toolDefs          = new Hashtable<String, FormTools>();
  static Font                         formLabelFont     = new Font(Display.getDefault(), new FontData("Courier New", 12, SWT.NO));
  static Font                         formTextFieldFont = new Font(Display.getDefault(), new FontData("Courier New", 12, SWT.NO));

  public FormsClient() {
    super("com.ceteva.forms");
    theClient = this;
  }

  public static FormsClient theClient() {
    return theClient;
  }

  public void sendMessage(final Message message) {
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
    else super.sendMessage(message);
  }

  private void addItem(Message message) {
    final String parentId = message.args[0].strValue();
    final String id = message.args[1].strValue();
    final String value = message.args[2].strValue();
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms.values())
          form.addItem(parentId, id, value);
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
    for (Form form : forms.values())
      form.newList(parentId, id, x, y, width, height);
  }

  private void setText(Message message) {
    final Value id = message.args[0];
    final Value text = message.args[1];
    runOnDisplay(new Runnable() {
      public void run() {
        for (Form form : forms.values()) {
          form.setText(id.strValue(), text.strValue());
        }
      }
    });
  }

  private void clearForm(Message message) {
    final Value id = message.args[0];
    if (forms.containsKey(id.strValue())) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          forms.get(id.strValue()).clear();
        }
      });
    } else System.out.println("cannot find form to clear " + id);
  }

  private void newText(Message message) {
    final Value parentId = message.args[0];
    final Value id = message.args[1];
    final Value string = message.args[2];
    final Value x = message.args[3];
    final Value y = message.args[4];
    if (forms.containsKey(parentId.strValue())) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          Form form = forms.get(parentId.strValue());
          form.newText(id.strValue(), string.strValue(), x.intValue, y.intValue);
        }
      });
    } else System.out.println("cannot find text parent " + parentId);
  }

  private void newTextField(Message message) {
    final Value parentId = message.args[0];
    final Value id = message.args[1];
    final Value x = message.args[2];
    final Value y = message.args[3];
    final Value width = message.args[4];
    final Value height = message.args[5];
    final Value editable = message.args[6];
    if (forms.containsKey(parentId.strValue())) {
      Display.getDefault().syncExec(new Runnable() {
        public void run() {
          Form form = forms.get(parentId.strValue());
          form.newTextField(id.strValue(), x.intValue, y.intValue, width.intValue, height.intValue, editable.boolValue);
        }
      });
    } else System.out.println("cannot find text field parent " + parentId);
  }

  private void setTool(Message message) {
    Value id = message.args[0];
    Value name = message.args[1];
    Value enabled = message.args[2];
    if (enabled.boolValue) {
      if (tabs.containsKey(id.strValue())) {
        FormTools formTools = getFormTools(id.strValue());
        formTools.addTool(name.strValue(), id.strValue());
      } else System.out.println("cannot find form " + id);
    }
  }

  private FormTools getFormTools(String id) {
    if (toolDefs.containsKey(id))
      return toolDefs.get(id);
    else {
      FormTools formTools = new FormTools();
      toolDefs.put(id, formTools);
      return formTools;
    }
  }

  private void newForm(Message message) {
    final Value id = message.args[0];
    Value type = message.args[1]; // needed?
    final Value label = message.args[2];
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        CTabItem tabItem = new CTabItem(tabFolder, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tabItem.setText(label.strValue());
        tabs.put(id.strValue(), tabItem);
        Form form = new Form(tabFolder, id.strValue());
        tabItem.setControl(form.getForm());
        forms.put(id.strValue(), form);
      }
    });
  }

  public boolean processMessage(Message message) {
    System.out.println(this + " <- " + message);
    return false;
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

  public void toolItemEvent(String event, String id) {
    Message m = getHandler().newMessage(event, 1);
    Value v = new Value(id);
    m.args[0] = v;
    getHandler().raiseEvent(m);
  }

  public static Font getFormLabelFont() {
    return formLabelFont;
  }

  public static Font getFormTextFieldFont() {
    return formLabelFont;
  }
}