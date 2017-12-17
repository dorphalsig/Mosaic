package tool.clients.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ListDialog extends Dialog {

  List     list;
  String   message;
  String   title;
  String[] values;
  String[] selection;

  public ListDialog(Shell parent, String title, String message, String[] values) {
    super(parent);
    this.message = message;
    this.title = title;
    this.values = values;
  }

  public String[] open(int x, int y) {
    // Create the dialog window
    Shell shell = new Shell(getParent(), getStyle());
    shell.setText(message);
    createContents(shell);
    // shell.pack();
    shell.setLocation(x, y);
    shell.setSize(300, 500);
    shell.open();
    Display display = getParent().getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    return selection;
  }

  private void createContents(final Shell shell) {

    GridLayout gridLayout = new GridLayout(1, true);
    shell.setLayout(gridLayout);
    Label title = new Label(shell,SWT.NONE);
    title.setText(this.title);
    Label message = new Label(shell,SWT.NONE);
    message.setText(this.message);
    Button ok = new Button(shell, SWT.PUSH);
    ok.setText("OK");
    Button cancel = new Button(shell, SWT.PUSH);
    cancel.setText("Cancel");
    final List list = new List(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
    list.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
    list.setItems(values);
    ok.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent arg0) {
        selection = list.getItems();
        shell.dispose();
      }
      public void widgetSelected(SelectionEvent arg0) {
        selection = list.getItems();
        shell.dispose();
      }
    });
    cancel.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent arg0) {
        selection = null;
        shell.dispose();
      }
      public void widgetSelected(SelectionEvent arg0) {
        selection = null;
        shell.dispose();
      }
    });
  }

}
