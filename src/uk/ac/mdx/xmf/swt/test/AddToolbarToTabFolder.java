package uk.ac.mdx.xmf.swt.test;

/*
 * Created on 2008/05/12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author qingkangxu
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AddToolbarToTabFolder {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("TabFolder");
		shell.setLayout(new FillLayout());

		TabFolder folder = new TabFolder(shell, SWT.NONE);
		TabItem item1 = new TabItem(folder, SWT.NONE);
		item1.setText("ToolBar");

		// create the composite for ToolBar
		final Composite toolComp = new Composite(shell, SWT.NONE);
		GridData suGridData = new GridData(GridData.FILL_BOTH);
		toolComp.setLayoutData(suGridData);
		toolComp.setLayout(new GridLayout(1, true));

		final ToolBar treeToolBar = new ToolBar(folder, SWT.NONE);
		treeToolBar
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Text filterText = new Text(treeToolBar, SWT.BORDER);

		// final ToolItem item1 = new ToolItem(treeToolBar, SWT.PUSH |
		// SWT.CENTER);
		// item1.setImage(new Image(display, "icons/Tools/box.gif"));

		// final ToolItem item2 = new ToolItem(treeToolBar, SWT.PUSH |
		// SWT.CENTER);
		// item2.setImage(new Image(display, "icons/user/Arrow2Left.gif"));

		final ToolItem item3 = new ToolItem(treeToolBar, SWT.PUSH | SWT.CENTER);
		item3.setImage(new Image(display, "icons/user/Arrow2Left.gif"));

		treeToolBar.pack();
		treeToolBar.setLocation(100, 0);

		// add toolbar on the toolComp
		// ToolBar toolBar = new ToolBar(toolComp, SWT.NONE);
		// ToolItem saveItem = new ToolItem(toolBar, SWT.PUSH);
		// saveItem.setText("Save");
		// toolBar.pack();

		// set the "toolComp" is being controlled,this is necessary!!
		// shell.setControl(toolComp);

		TabItem item2 = new TabItem(folder, SWT.NONE);
		item2.setText("radio");
		Group group2 = new Group(folder, SWT.NONE);
		group2.setText("Radio Group");
		group2.setLayout(new GridLayout());
		Button radio1 = new Button(group2, SWT.RADIO);
		radio1.setText("radio1");
		Button radio2 = new Button(group2, SWT.RADIO);
		radio2.setText("radio2");
		item2.setControl(group2);

		shell.setVisible(true);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
