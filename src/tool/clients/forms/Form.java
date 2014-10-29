package tool.clients.forms;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class Form {

  String                  id;
  ScrolledComposite       form;
  Composite               content;
  Hashtable<String, Text> textFields = new Hashtable<String, Text>();
  Hashtable<String, List> lists      = new Hashtable<String, List>();

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
    Text text = new Text(content, SWT.NONE);
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

  public void addItem(String parentId, String id, String value) {
    for (String listId : lists.keySet()) {
      if (listId.equals(parentId)) lists.get(listId).add(id, value);
    }
  }

}
