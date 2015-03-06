package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import tool.xmodeler.XModeler;

public class ToggleTool extends Tool {

  boolean state;

  public ToggleTool(Composite parent, Diagram diagram, String label, String id, boolean state, String icon) {
    super(parent, diagram, label, id, icon);
    this.state = state;
    if (state)
      select();
    else unselect();
  }

  public Button createButton(Composite parent) {
    Image image = new Image(XModeler.getXModeler().getDisplay(), new ImageData("icons/" + icon));
    Button button = new Button(parent, SWT.CHECK);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    button.setText(label);
    button.setImage(image);
    button.setLayoutData(data);
    button.setFont(new Font(XModeler.getXModeler().getDisplay(), Group.defaultFont));
    button.addSelectionListener(this);
    button.pack();
    return button;
  }

  public void writeXML(PrintStream out) {
    out.print("<ToggleTool label='" + label + "'");
    out.print(" id='" + id + "'");
    out.print(" state='" + state + "'");
    out.print(" icon='" + icon + "'/>");
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent event) {
    toggle();
    event.doit = false;
    diagram.toggle(getId(), button.getSelection());
  }

  private void toggle() {
    state = !state;
    button.setSelection(state);
  }

  public String getType() {
    return "TOGGLE";
  }

  public void reset() {
    // Called by the palette to globally affect the state of the palette.
    // Ignore such requests since the toggle should reflect the state
    // of a boolean flag...
  }

  private void unselect() {
    button.setSelection(false);
    button.setGrayed(false);
  }

  public void select() {
    button.setSelection(true);
    button.setGrayed(true);
  }

}
