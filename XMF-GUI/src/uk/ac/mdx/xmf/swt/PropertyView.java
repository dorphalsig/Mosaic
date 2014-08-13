package uk.ac.mdx.xmf.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class PropertyView extends View {
	Composite parent;
	Image image = null;
	// Enable a label as a Drag Source
	Label dragLabel = null;
	Tree tree;

	public PropertyView(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void display() {
		tree = new Tree(parent, SWT.BORDER);

		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		TreeItem item = new TreeItem(tree, SWT.NULL);
		item.setText("Project");

		TreeItem item2 = new TreeItem(item, SWT.NULL);
		item2.setText("Diagram");

		TreeItem item3 = new TreeItem(item2, SWT.NULL);
		item3.setText("Node");

	}

	public void createPartControl() {
		dragLabel = new Label(this, SWT.BORDER);
		dragLabel.setText("class");

		this.setLayout(new FillLayout());
		dragLabel.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event e) {
				image = new Image(e.display, "images/class_obj.gif");
				GC gc = e.gc;
				gc.drawImage(image, 10, 20);
				gc.dispose();
			}
		});
		// Allow data to be copied or moved from the drag source
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		DragSource source = new DragSource(dragLabel, operations);

		// Provide data in Text format
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				if (dragLabel.getText().length() == 0) {
					event.doit = false;
				}
				System.out.println("drag start");
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = dragLabel.getText();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// If a move operation has been performed, remove the data
				// from the source
				if (event.detail == DND.DROP_MOVE) {
					dragLabel.setText("");

				}
				System.out.println(dragLabel.getText());
			}
		});

	}

}
