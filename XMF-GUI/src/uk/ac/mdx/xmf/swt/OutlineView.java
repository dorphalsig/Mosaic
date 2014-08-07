package uk.ac.mdx.xmf.swt;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class OutlineView extends View {
	Composite parent;
	Tree tree;
	Set<String> contents = new HashSet<String>();
	TreeItem item;
	Boolean isSelectProject = true;
	int i = 0;

	// LightweightSystem lws;
	// FigureCanvas canvas;

	public OutlineView(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;

		// canvas = new FigureCanvas(this);
		// lws = new LightweightSystem(canvas);
		// canvas.setViewport(new Viewport(true));
		// tree = new Tree(parent, SWT.BORDER | SWT.MULTI);
		// tree.addSelectionListener(new SelectionAdapter() {
		// public void widgetSelected(SelectionEvent e) {
		// TreeItem ti = (TreeItem) e.item;
		//
		// if (ti.getText().equalsIgnoreCase("New Project")) {
		// isSelectProject = true;
		// } else {
		// isSelectProject = false;
		// }
		// }
		//
		// });

	}

	public void display() {

	}

	public void addContents(String icon) {
		contents.add(icon);
	}

	public void createPartControl() {

		// tree.setSize(300, parent.getSize().y);
		//
		// for (String s : contents) {
		// if (i < 2) {
		// item = new TreeItem(tree, SWT.NONE);
		// item.setText("Project");
		// item.setImage((new Image(parent.getDisplay(), s)));
		// }
		// i++;
		// }
		//
		// Menu popupMenu = new Menu(tree);
		// if (isSelectProject) {
		// MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
		// newItem.setText("New Project");
		// MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
		// refreshItem.setText("Refresh");
		// MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
		// deleteItem.setText("Delete");
		// } else {
		// MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
		// newItem.setText("create diagram");
		// MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
		// refreshItem.setText("Refresh");
		// MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
		// deleteItem.setText("Delete");
		//
		// Menu newMenu = new Menu(popupMenu);
		// newItem.setMenu(newMenu);
		//
		// MenuItem shortcutItem = new MenuItem(newMenu, SWT.NONE);
		// shortcutItem.setText("Create diagram");
		// MenuItem iconItem = new MenuItem(newMenu, SWT.NONE);
		// iconItem.setText("Icon");
		// }
		// tree.setMenu(popupMenu);
		// org.eclipse.jface.action.MenuManager menu = new
		// org.eclipse.jface.action.MenuManager();
		// MenuBuilder.calculateMenu(identity, menu, getSite());
		// tree.setMenu(menu.createContextMenu(formContentsHolder));

	}

}
