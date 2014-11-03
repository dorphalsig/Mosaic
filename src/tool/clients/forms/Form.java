package tool.clients.forms;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class Form {

  String                        id;
  ScrolledComposite             form;
  Composite                     content;
  Hashtable<String, Text>       textFields = new Hashtable<String, Text>();
  Hashtable<String, List>       lists      = new Hashtable<String, List>();
  Hashtable<String, StyledText> boxes      = new Hashtable<String, StyledText>();
  Hashtable<String, CCombo>     combos     = new Hashtable<String, CCombo>();
  Hashtable<String, Button>     checks     = new Hashtable<String, Button>();
  Hashtable<String, Button>     buttons    = new Hashtable<String, Button>();

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

  public String getId() {
    return id;
  }

  public ScrolledComposite getForm() {
    return form;
  }

  public void newText(String id, String string, int x, int y) {
    Text text = new Text(content, SWT.NONE);
    text.setText(string);
    text.setEditable(false);
    text.setBackground(content.getBackground());
    text.setFont(FormsClient.getFormLabelFont());
    text.setLocation(x, y);
    text.pack();
    form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  public void clear() {
    textFields.clear();
    lists.clear();
    for (Control child : content.getChildren())
      child.dispose();
  }

  public void newTextField(String id, int x, int y, int width, int height, boolean editable) {
    Text text = new Text(content, SWT.BORDER);
    text.setEditable(editable);
    text.setBackground(FormsClient.theClient().WHITE);
    text.setFont(FormsClient.getFormTextFieldFont());
    text.setBounds(x, y, width, height);
    form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    textFields.put(id, text);
  }

  public void setText(String id, String string) {
    if (textFields.containsKey(id)) {
      Text text = textFields.get(id);
      text.setText(string);
      text.pack();
      form.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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

  public void addListItem(String parentId, String id, String value) {
    for (String listId : lists.keySet()) {
      if (listId.equals(parentId)) lists.get(listId).add(id, value);
    }
  }

  public void newTextBox(String parentId, String id, int x, int y, int width, int height, boolean editable) {
    if (getId().equals(parentId)) {
      StyledText text = new StyledText(content, SWT.BORDER);
      text.setLocation(x, y);
      text.setSize(width, height);
      boxes.put(id, text);
    }
  }

  public void newComboBox(String parentId, String id, int x, int y, int width, int height) {
    if (getId().equals(parentId)) {
      CCombo combo = new CCombo(content, SWT.READ_ONLY | SWT.DROP_DOWN);
      combo.setLocation(x, y);
      // combo.setSize(width, height);
      combo.setFont(FormsClient.formLabelFont);
      combos.put(id, combo);
    }
  }

  public void addComboItem(String comboId, String value) {
    if (combos.containsKey(comboId)) {
      combos.get(comboId).add(value);
      combos.get(comboId).pack();
    }
  }

  public void setSelection(String comboId, int index) {
    for (String id : combos.keySet()) {
      if (id.equals(comboId)) {
        combos.get(id).select(index);
      }
    }
  }

  public void newCheckBox(String parentId, String id, int x, int y, boolean checked) {
    if (getId().equals(parentId)) {
      Button button = new Button(content, SWT.CHECK);
      button.setLocation(x, y);
      button.setSelection(checked);
      button.setText("");
      button.pack();
      checks.put(id, button);
    }
  }

  public void newButton(String parentId, String id, String label, int x, int y, int width, int height) {
    if (getId().equals(parentId)) {
      Button button = new Button(content, SWT.PUSH);
      button.setLocation(x, y);
      button.setSize(width, height);
      button.setText(label);
      button.setFont(FormsClient.formLabelFont);
      buttons.put(id, button);
    }
  }
}
