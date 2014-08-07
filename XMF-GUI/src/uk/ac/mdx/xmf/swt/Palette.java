package uk.ac.mdx.xmf.swt;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import uk.ac.mdx.xmf.swt.demo.GUIDemo;

public class Palette extends View {
	Composite parent;
	private String imageName = "";
	static ArrayList<String> tools = new ArrayList<String>();
	Point point = null;
	Point toolSize = new Point(30, 5);
	Display display;

	Vector<String> models = new Vector<String>();
	Vector<String> groups = new Vector<String>();

	private String transferClass = "";
	Canvas canvas;
	private boolean isInitial = false;

	private Image[] images;
	private Label[] labelImages;
	private Label[] labelTexts;
	private Color colorSelect;
	private Color color;
	private Color colorSection;

	// uk.ac.mdx.xmf.swt.model.AbstractDiagram diagram;
	private volatile static Palette instance = null;

	public static Palette getInstance(Composite parent, int style,
			Display display) {
		if (instance == null) {

			instance = new Palette(parent, style, display);

		}
		return instance;
	}

	public Palette(Composite parent, int style, Display display) {
		super(parent, style);
		this.parent = parent;
		this.display = display;
		this.setVisible(false);

		addDrawer("Palette");
		addEntry("Palette", "Select", null, false, null);
		addEntry("Palette", "Marquee", null, false, null); // default

		colorSelect = display.getSystemColor(SWT.COLOR_LIST_SELECTION);
		color = display.getSystemColor(SWT.COLOR_WHITE);
		colorSection = display.getSystemColor(SWT.COLOR_GRAY);
	}

	public void addDrawer(String group) {
		boolean exist = false;
		for (String g : groups) {
			if (g.equalsIgnoreCase(group))
				exist = true;
		}
		if (!exist)
			groups.add(group);
	}

	public void addEntry(String parent, String label, String identity,
			boolean connection, String icon) {
		tools.add(label);
		tools.add(parent);
	}

	public String getSelectClass() {
		return transferClass;
	}

	public void setInitial(boolean initial) {
		isInitial = initial;
	}

	public boolean getInitial() {
		return isInitial;
	}

	public void createPartControl() {

		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setSize((int) (parent.getBounds().width),
				parent.getBounds().height);

		final int size = tools.size() / 2;
		int gap = 10;
		int w1 = canvas.getBounds().width / 5;
		int w2 = canvas.getBounds().width - w1 - gap;
		int height = canvas.getBounds().height / 20;

		Image image;
		Label labelText;
		Label label;

		images = new Image[size];
		labelImages = new Label[size];
		labelTexts = new Label[size];

		int count = 0;

		for (int m = 0; m < groups.size(); m++) {
			image = new Image(display, "images\\" + groups.get(m) + ".gif");

			label = new Label(canvas, SWT.NO);
			label.setImage(image);
			label.setBounds(0, count * height, w1, height);
			label.setBackground(colorSection);

			labelText = new Label(canvas, SWT.NO);
			labelText.setText(groups.get(m));
			labelText.setBounds(w1, count * height, w2, height);
			labelText.setBackground(colorSection);

			count++;

			for (int i = 0; i < size; i++) {
				if (groups.get(m).equalsIgnoreCase(tools.get(i * 2 + 1))) {
					images[i] = new Image(display, "images\\"
							+ tools.get(i * 2) + ".gif");

					labelImages[i] = new Label(canvas, SWT.NO);
					labelImages[i].setImage(images[i]);
					labelImages[i].setBounds(gap, count * height, w1, height);
					labelImages[i].setBackground(color);
					labelImages[i].setToolTipText(tools.get(i * 2));

					final int j = i * 2;
					final int k = i;
					labelImages[i].addMouseListener(new MouseListener() {

						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);
							imageName = tools.get(j);
							GUIDemo.getInstance().getView().clearPorts();

							for (int a = 0; a < size; a++) {
								if (a != k) {
									labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}

						@Override
						public void mouseUp(MouseEvent arg0) {

						}

					});
					// check mouse hover
					labelImages[i].addListener(SWT.MouseHover, new Listener() {

						@Override
						public void handleEvent(Event arg0) {
							labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);

							for (int a = 0; a < size; a++) {
								if (a != k) {
									labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}
					});

					labelTexts[i] = new Label(canvas, SWT.NO);
					labelTexts[i].setText(tools.get(i * 2));
					labelTexts[i].setBounds(w1 + gap, count * height, w2,
							height);
					labelTexts[i].setBackground(color);
					labelTexts[i].setToolTipText(tools.get(i * 2));

					labelTexts[i].addMouseListener(new MouseListener() {

						@Override
						public void mouseDoubleClick(MouseEvent arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);
							imageName = tools.get(j);
							GUIDemo.getInstance().getView().clearPorts();

							for (int a = 0; a < size; a++) {
								if (a != k) {
									labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}

						@Override
						public void mouseUp(MouseEvent arg0) {

						}

					});

					// check mouse hover
					labelTexts[i].addListener(SWT.MouseHover, new Listener() {

						@Override
						public void handleEvent(Event arg0) {
							labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);

							for (int a = 0; a < size; a++) {
								if (a != k) {
									labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}
					});

					count++;
				}
			}
		}

		isInitial = true; // make use this function only run once
	}

	public String getSelectImage() {

		return imageName;
	}

	public void setSelectImage() {
		imageName = "";

		for (int a = 0; a < labelImages.length; a++) {
			labelImages[a].setBackground(color);
			labelTexts[a].setBackground(color);
		}
	}

}
