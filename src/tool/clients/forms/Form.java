package tool.clients.forms;

import java.io.PrintStream;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import tool.clients.EventHandler;
import tool.clients.diagrams.DiagramClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class Form implements MouseListener, SelectionListener {

  static Font                   labelFont  = new Font(XModeler.getXModeler().getDisplay(), new FontData("Courier New", 12, SWT.NONE));

  String                        id;
  ScrolledComposite             form;
  Composite                     content;
  Hashtable<String, Text>       textFields = new Hashtable<String, Text>();
  Hashtable<String, Text>       labels     = new Hashtable<String, Text>();
  Hashtable<String, List>       lists      = new Hashtable<String, List>();
  Hashtable<String, StyledText> boxes      = new Hashtable<String, StyledText>();
  Hashtable<String, CCombo>     combos     = new Hashtable<String, CCombo>();
  Hashtable<String, Button>     checks     = new Hashtable<String, Button>();
  Hashtable<String, Button>     buttons    = new Hashtable<String, Button>();
  Hashtable<String, Tree>       trees      = new Hashtable<String, Tree>();
  Hashtable<String, TreeItem>   items      = new Hashtable<String, TreeItem>();
  Hashtable<String, String>     images     = new Hashtable<String, String>();

  public Form(CTabFolder parent, String id) {
    this.id = id;
    form = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
    form.setVisible(true);
    form.setExpandHorizontal(true);
    form.setExpandVertical(true);
    content = new Composite(form, SWT.BORDER);
    content.setVisible(true);
    form.setContent(content);
  }

  public void addComboItem(String comboId, String value) {
    if (combos.containsKey(comboId)) {
      combos.get(comboId).add(value);
      combos.get(comboId).pack();
    }
  }

  public void addListItem(String parentId, String id, String value) {
    for (String listId : lists.keySet()) {
      if (listId.equals(parentId)) lists.get(listId).add(id, value);
    }
  }

  private void addNodeWithIcon(final String parentId, final String nodeId, final String text, boolean editable, final boolean expanded, final String icon, final int index) {
    if (items.containsKey(parentId)) {
      TreeItem parent = items.get(parentId);
      String iconFile = "icons/" + icon;
      ImageData data = new ImageData(iconFile);
      Image image = new Image(XModeler.getXModeler().getDisplay(), data);
      TreeItem item = new TreeItem(parent, SWT.NONE, (index == -1) ? parent.getItemCount() : index);
      images.put(nodeId, icon);
      items.put(nodeId, item);
      item.setText(text);
      item.setImage(image);
      item.setExpanded(expanded);
      item.setFont(labelFont);
    } else System.out.println("Cannot find node " + parentId);
  }

  private void addRootNodeWithIcon(final String parentId, final String nodeId, final String text, boolean editable, final boolean expanded, final String icon, final int index) {
    String iconFile = "icons/" + icon;
    ImageData data = new ImageData(iconFile);
    Image image = new Image(XModeler.getXModeler().getDisplay(), data);
    Tree tree = trees.get(parentId);
    TreeItem item = new TreeItem(tree, SWT.NONE, (index == -1) ? tree.getItemCount() : index);
    images.put(nodeId, icon);
    items.put(nodeId, item);
    item.setText(text);
    item.setImage(image);
    item.setExpanded(expanded);
    item.setFont(labelFont);
  }

  public void clear() {
    labels.clear();
    textFields.clear();
    lists.clear();
    boxes.clear();
    combos.clear();
    checks.clear();
    buttons.clear();
    trees.clear();
    items.clear();
    images.clear();
    for (Control child : content.getChildren())
      child.dispose();
  }

  public void clear(String id) {
    if (getId().equals(id))
      clear();
    else {
      if (lists.containsKey(id)) {
        List l = lists.get(id);
        l.clear();
      }
    }
  }

  private void doubleClick(TreeItem item) {
    String id = getId(item);
    Message m = FormsClient.theClient().getHandler().newMessage("doubleSelected", 1);
    m.args[0] = new Value(id);
    FormsClient.theClient().getHandler().raiseEvent(m);
  }

  public Hashtable<String, StyledText> getBoxes() {
    return boxes;
  }

  public Hashtable<String, Button> getButtons() {
    return buttons;
  }

  public Hashtable<String, Button> getChecks() {
    return checks;
  }

  public Hashtable<String, CCombo> getCombos() {
    return combos;
  }

  public ScrolledComposite getForm() {
    return form;
  }

  public String getId() {
    return id;
  }

  private String getId(Button b) {
    for (String id : buttons.keySet())
      if (buttons.get(id) == b) return id;
    return null;
  }

  private String getId(CCombo c) {
    for (String id : combos.keySet())
      if (combos.get(id) == c) return id;
    return null;
  }

  private String getId(TreeItem item) {
    for (String id : items.keySet())
      if (items.get(id) == item) return id;
    return null;
  }

  public Hashtable<String, TreeItem> getItems() {
    return items;
  }

  public Hashtable<String, Text> getLabels() {
    return labels;
  }

  public Hashtable<String, List> getLists() {
    return lists;
  }

  public Hashtable<String, Text> getTextFields() {
    return textFields;
  }

  public Hashtable<String, Tree> getTrees() {
    return trees;
  }

  public void mouseDoubleClick(MouseEvent event) {
    Widget widget = event.widget;
    if (widget instanceof Tree) {
      Tree tree = (Tree) widget;
      if (tree.getSelectionCount() == 1) {
        TreeItem item = tree.getSelection()[0];
        doubleClick(item);
      }
    }
  }

  public void mouseDown(MouseEvent event) {
  }

  public void mouseUp(MouseEvent arg0) {

  }

  public void newButton(String parentId, String id, String label, int x, int y, int width, int height) {
    if (getId().equals(parentId)) {
      Button button = new Button(content, SWT.PUSH);
      button.addSelectionListener(this);
      button.setLocation(x, y);
      button.setSize(width, height);
      button.setText(label);
      button.setFont(FormsClient.formLabelFont);
      buttons.put(id, button);
    }
  }

  public void newCheckBox(String parentId, final String id, int x, int y, boolean checked) {
    if (getId().equals(parentId)) {
      final Button button = new Button(content, SWT.CHECK);
      button.addSelectionListener(new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent event) {
        }

        public void widgetSelected(SelectionEvent event) {
          setSelection(id, button.getSelection());
        }
      });
      button.setLocation(x, y);
      button.setSelection(checked);
      button.setText("");
      button.pack();
      checks.put(id, button);
    }
  }

  private void setSelection(String id, boolean state) {
    EventHandler handler = FormsClient.theClient().getHandler();
    Message message = handler.newMessage("setBoolean", 2);
    message.args[0] = new Value(id);
    message.args[1] = new Value(state);
    handler.raiseEvent(message);
  }

  public void newComboBox(String parentId, String id, int x, int y, int width, int height) {
    if (getId().equals(parentId)) {
      CCombo combo = new CCombo(content, SWT.READ_ONLY | SWT.DROP_DOWN);
      combo.addSelectionListener(this);
      combo.setLocation(x, y);
      // combo.setSize(width, height);
      combo.setFont(FormsClient.formLabelFont);
      combos.put(id, combo);
    }
  }

  public void newList(String parentId, final String id, final int x, final int y, final int width, final int height) {
    if (getId().equals(parentId)) {
      FormsClient.theClient().runOnDisplay(new Runnable() {
        public void run() {
          List list = new List(id, content, x, y, width, height);
          lists.put(id, list);
        }
      });
    }
  }

  public void newNodeWithIcon(String parentId, String nodeId, String text, boolean editable, String icon, int index) {
    if (trees.containsKey(parentId))
      addRootNodeWithIcon(parentId, nodeId, text, editable, false, icon, index);
    else addNodeWithIcon(parentId, nodeId, text, editable, false, icon, index);
  }

  public void newText(String id, String string, int x, int y) {
    Text text = new Text(content, SWT.NONE);
    text.setText(string);
    text.setEditable(false);
    text.setBackground(content.getBackground());
    text.setFont(FormsClient.getFormLabelFont());
    text.setLocation(x, y);
    text.pack();
    labels.put(id, text);
    form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  public void newTextBox(String parentId, String id, int x, int y, int width, int height, boolean editable) {
    if (getId().equals(parentId)) {
      StyledText text = new StyledText(content, SWT.BORDER);
      text.setFont(FormsClient.formLabelFont);
      text.setLocation(x, y);
      text.setSize(width, height);
      text.setEditable(editable);
      boxes.put(id, text);
    }
  }

  public void newTextField(final String id, int x, int y, int width, int height, boolean editable) {
    final Text text = new Text(content, SWT.BORDER);
    text.setEditable(editable);
    text.setBackground(FormsClient.theClient().WHITE);
    text.setFont(FormsClient.getFormTextFieldFont());
    text.setBounds(x, y, width, height);
    // form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    textFields.put(id, text);
    Listener listener = new Listener() {
      public void handleEvent(Event event) {
        switch (event.type) {
        case SWT.FocusOut:
          textChangedEvent(id, text.getText());
          break;
        case SWT.Traverse:
          switch (event.detail) {
          case SWT.TRAVERSE_RETURN:
          case SWT.TRAVERSE_ESCAPE:
            textChangedEvent(id, text.getText());
            break;
          }
        }
      }
    };
    text.addListener(SWT.FocusOut, listener);
    text.addListener(SWT.Verify, listener);
    text.addListener(SWT.Traverse, listener);
  }

  public void newTree(String parentId, String id, int x, int y, int width, int height, boolean editable) {
    if (getId().equals(parentId)) {
      Tree tree = new Tree(content, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
      tree.setLocation(x, y);
      tree.setSize(width, height);
      tree.addMouseListener(this);
      trees.put(id, tree);
    }
  }

  private void selected(Button b) {
    String id = getId(b);
    EventHandler handler = FormsClient.theClient().getHandler();
    Message message = handler.newMessage("buttonPressed", 1);
    message.args[0] = new Value(id);
    handler.raiseEvent(message);
  }

  private void selected(CCombo c) {
    String id = getId(c);
    EventHandler handler = FormsClient.theClient().getHandler();
    Message message = handler.newMessage("comboBoxSelection", 2);
    message.args[0] = new Value(id);
    message.args[1] = new Value(c.getItem(c.getSelectionIndex()));
    handler.raiseEvent(message);
  }

  public void setSelection(String comboId, int index) {
    for (String id : combos.keySet()) {
      if (id.equals(comboId)) {
        combos.get(id).select(index);
      }
    }
  }

  public void setText(String id, String string) {
    if (textFields.containsKey(id)) {
      Text text = textFields.get(id);
      text.setText(string);
      text.pack();
      form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
    if (boxes.containsKey(id)) {
      StyledText text = boxes.get(id);
      text.setText(string);
      text.pack();
    }
  }

  public void textChangedEvent(String id, String text) {
    Message message = FormsClient.theClient().getHandler().newMessage("textChanged", 2);
    message.args[0] = new Value(id);
    message.args[1] = new Value(text);
    FormsClient.theClient().getHandler().raiseEvent(message);
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent event) {
    Widget w = event.widget;
    if (w instanceof Button) {
      Button b = (Button) w;
      selected(b);
    }
    if (w instanceof CCombo) {
      CCombo c = (CCombo) w;
      selected(c);
    }
  }

  public void writeXML(PrintStream out, boolean selected, String formLabel) {
    out.print("<Form id='" + getId() + "' selected='" + selected + "' label='" + formLabel + "'>");
    for (String id : textFields.keySet()) {
      Text field = textFields.get(id);
      out.print("<TextField id='" + id + "'");
      out.print(" string='" + XModeler.encodeXmlAttribute(field.getText()) + "'");
      out.print(" x='" + field.getLocation().x + "'");
      out.print(" y='" + field.getLocation().y + "'");
      out.print(" width='" + field.getSize().x + "'");
      out.print(" height='" + field.getSize().y + "'");
      out.print(" editable='" + field.getEditable() + "'/>");
    }
    for (String id : labels.keySet()) {
      Text label = labels.get(id);
      out.print("<Label id='" + id + "'");
      out.print(" string='" + XModeler.encodeXmlAttribute(label.getText()) + "'");
      out.print(" x='" + label.getLocation().x + "'");
      out.print(" y='" + label.getLocation().y + "'/>");
    }
    for (List list : lists.values())
      list.writeXML(out);
    out.print("</Form>");
    for (String id : boxes.keySet()) {
      StyledText box = boxes.get(id);
      out.print("<TextBox id='" + id + "'");
      out.print(" string='" + XModeler.encodeXmlAttribute(box.getText()) + "'");
      out.print(" x='" + box.getLocation().x + "'");
      out.print(" y='" + box.getLocation().y + "'");
      out.print(" width='" + box.getSize().x + "'");
      out.print(" height='" + box.getSize().y + "'");
      out.print(" editable='" + box.getEditable() + "'/>");
    }
    for (String id : combos.keySet()) {
      CCombo combo = combos.get(id);
      out.print("<Combo id='" + id + "'");
      out.print(" string='" + XModeler.encodeXmlAttribute(combo.getText()) + "'");
      out.print(" x='" + combo.getLocation().x + "'");
      out.print(" y='" + combo.getLocation().y + "'");
      out.print(" width='" + combo.getSize().x + "'");
      out.print(" height='" + combo.getSize().y + "'");
      out.print(" editable='" + combo.getEditable() + "'>");
      for (String value : combo.getItems())
        out.print("<Item item='" + XModeler.encodeXmlAttribute(value) + "'/>");
      out.print("</Combo>");
    }
    for (String id : checks.keySet()) {
      out.print("<Check id='" + id + "'");
      out.print(" checked='" + checks.get(id).getEnabled() + "'");
      out.print(" x='" + checks.get(id).getLocation().x + "'");
      out.print(" y='" + checks.get(id).getLocation().y + "'");
      out.print(" text='" + XModeler.encodeXmlAttribute(checks.get(id).getText()) + "'/>");
    }
    for (String id : buttons.keySet()) {
      out.print("<Button id='" + id + "'");
      out.print(" x='" + buttons.get(id).getLocation().x + "'");
      out.print(" y='" + buttons.get(id).getLocation().y + "'");
      out.print(" width='" + buttons.get(id).getSize().x + "'");
      out.print(" height='" + buttons.get(id).getSize().y + "'");
      out.print(" text='" + XModeler.encodeXmlAttribute(buttons.get(id).getText()) + "'/>");
    }
    for (String id : trees.keySet()) {
      Tree tree = trees.get(id);
      out.print("<Tree id='" + id + "'");
      out.print(" x='" + trees.get(id).getLocation().x + "'");
      out.print(" y='" + trees.get(id).getLocation().y + "'");
      out.print(" width='" + trees.get(id).getSize().x + "'");
      out.print(" height='" + trees.get(id).getSize().y + "'");
      out.print(" editable='true'>");
      writeXMLTreeItems(tree.getItems(), out);
      out.print("</Tree>");
    }
  }

  private void writeXMLTreeItems(TreeItem[] children, PrintStream out) {
    for (TreeItem item : children) {
      String id = null;
      for (String itemId : items.keySet()) {
        if (items.get(itemId) == item) id = itemId;
      }
      if (id == null) System.out.println("error: cannot find tree item " + item);
      String icon = images.get(id);
      out.print("<Item id='" + id + "' text='" + XModeler.encodeXmlAttribute(item.getText()) + "' image='" + icon + "' expanded='" + item.getExpanded() + "'>");
      writeXMLTreeItems(item.getItems(), out);
      out.print("</Item>");
    }
  }

  public void check(String id) {
    for (String bid : checks.keySet())
      if (bid.equals(id) && !checks.get(id).getSelection()) checks.get(id).setSelection(true);
  }

  public void uncheck(String id) {
    for (String bid : checks.keySet())
      if (bid.equals(id) && checks.get(id).getSelection()) checks.get(id).setSelection(false);
  }
}
