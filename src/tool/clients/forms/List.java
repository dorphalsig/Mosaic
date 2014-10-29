package tool.clients.forms;

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class List {

  String                       id;
  org.eclipse.swt.widgets.List list;
  Hashtable<String, String>    items = new Hashtable<String, String>();

  public List(String id, Composite parent, int x, int y, int width, int height) {
    this.id = id;
    list = new org.eclipse.swt.widgets.List(parent, SWT.BORDER);
    list.setLocation(x, y);
    list.setSize(width, height);
    list.setFont(FormsClient.formTextFieldFont);
  }

  public void add(String id, String value) {
    list.add(value);
    items.put(id, value);
  }

  public String getId() {
    return id;
  }

}
