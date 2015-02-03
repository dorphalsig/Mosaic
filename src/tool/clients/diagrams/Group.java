package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

import tool.xmodeler.XModeler;
import xos.Value;

public class Group implements SelectionListener {

  static FontData            defaultFont = new FontData("Courier New", 12, SWT.NO);

  Composite                  buttonContainer;
  ExpandItem                 item;
  Palette                    palette;
  Hashtable<Button, String>  buttons     = new Hashtable<Button, String>();
  Hashtable<Button, Boolean> edges       = new Hashtable<Button, Boolean>();
  Hashtable<Button, String>  icons       = new Hashtable<Button, String>();

  public Group(Palette palette, ExpandBar parent, String name) {
    this.palette = palette;
    item = new ExpandItem(parent, SWT.NONE);
    buttonContainer = new Composite(parent, SWT.BORDER);
    item.setControl(buttonContainer);
    item.setText(name);
    GridLayout layout = new GridLayout(1, true);
    layout.marginHeight = 0;
    layout.horizontalSpacing = 0;
    layout.verticalSpacing = 0;
    layout.marginWidth = 0;
    buttonContainer.setLayout(layout);
    GridData buttonData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_FILL, true, true);
    buttonData.horizontalSpan = 2;
    buttonContainer.setLayoutData(buttonData);
  }

  public void newTool(String label, String toolId, boolean isEdge, String icon) {
    Image image = new Image(XModeler.getXModeler().getDisplay(), new ImageData("icons/" + icon));
    Button button = new Button(buttonContainer, SWT.TOGGLE);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    button.setText(label);
    button.setImage(image);
    button.setLayoutData(data);
    button.setFont(new Font(XModeler.getXModeler().getDisplay(), defaultFont));
    button.addSelectionListener(this);
    buttons.put(button, toolId);
    edges.put(button, isEdge);
    icons.put(button, icon);
    item.setHeight(buttonContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
    item.setExpanded(true);
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent event) {
    palette.reset();
    for (Button button : buttons.keySet()) {
      if (button == event.widget) {
        button.setSelection(true);
        palette.setCurrentTool(buttons.get(button), edges.get(button));
      }
    }
  }

  public void resetButtons() {
    for (Button button : buttons.keySet()) {
      button.setSelection(false);
    }
  }

  public void writeXML(String name, PrintStream out) {
    out.print("<Group name='" + name + "'>");
    for (Button button : buttons.keySet())
      out.print("<Button name='" + button.getText() + "' tool='" + buttons.get(button) + "' isEdge='" + edges.get(button) + "' icon='" + icons.get(button) + "'/>");
    out.print("</Group>");
  }

  public Value asValue(String name) {
    Value[] bs = new Value[buttons.size() + 1];
    bs[0] = new Value(name);
    int i = 1;
    for (Button button : buttons.keySet()) {
      if (edges.get(button)) {
        bs[i++] = new Value(new Value[] { new Value(buttons.get(button)), new Value("EDGE") });
      } else {
        bs[i++] = new Value(new Value[] { new Value(buttons.get(button)), new Value("NODE") });
      }
    }
    return new Value(bs);
  }

  public void delete() {
    item.dispose();
  }
}
