package tool.clients.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import tool.xmodeler.XModeler;

public class FormToolDef implements SelectionListener {

  String event;
  String id;
  String icon;

  public FormToolDef(String event, String id, String icon) {
    super();
    this.event = event;
    this.id = id;
    this.icon = icon;
  }

  public void populateToolBar(ToolBar toolBar) {
    ToolItem item = new ToolItem(toolBar, SWT.PUSH);
    ImageData imageData = new ImageData(icon);
    Image image = new Image(XModeler.getXModeler().getDisplay(), imageData);
    item.setImage(image);
    item.addSelectionListener(this);
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent event) {
    FormsClient.theClient().toolItemEvent(this.event, id);
  }
}
