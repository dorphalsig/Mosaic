package uk.ac.mdx.xmf.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import uk.ac.mdx.xmf.swt.demo.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class Palette.
 */
public class Palette {

	/** The parent. */
	Composite parent;

	/** The image name. */
	private String imageName = "";

	/** The tools. */
	ArrayList<String> tools = new ArrayList<String>();
	ArrayList<String> icons = new ArrayList<String>();
	HashMap<String, Boolean> connections = new HashMap<String, Boolean>();

	/** The point. */
	Point point = null;

	/** The tool size. */
	Point toolSize = new Point(30, 5);

	/** The display. */
	Display display;

	/** The models. */
	Vector<String> models = new Vector<String>();

	/** The groups. */
	Vector<String> groups = new Vector<String>();

	/** The transfer class. */
	private String transferClass = "";

	/** The canvas. */
	// Canvas canvas;

	/** The is focus. */
	private boolean isFocus = false;

	/** The is initial. */
	private boolean isInitial = false;

	/** The color select. */
	private Color colorSelect;

	/** The color. */
	private Color color;

	/** The color section. */
	private Color colorSection;

	/** The _palettes. */
	Vector<Palette> _palettes = new Vector<Palette>();

	// uk.ac.mdx.xmf.swt.model.AbstractDiagram diagram;
	// private volatile static Palette instance = null;

	// public static Palette getInstance(Composite parent, int style,
	// Display display) {
	// if (instance == null) {
	//
	// instance = new Palette(parent, style, display);
	//
	// }
	// return instance;
	// }

	/**
	 * Instantiates a new palette.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 * @param display
	 *            the display
	 */
	private boolean initialOnce = true;

	public Palette(Composite parent, int style, Display display) {
		this.parent = parent;
		this.display = display;

		addDrawer("Palette");
		addEntry("Palette", "Select", null, false, "Select.gif");
		addEntry("Palette", "Marquee", null, false, "Marquee.gif"); // default

		colorSelect = display.getSystemColor(SWT.COLOR_LIST_SELECTION);
		color = display.getSystemColor(SWT.COLOR_WHITE);
		colorSection = display.getSystemColor(SWT.COLOR_GRAY);

	}

	/**
	 * Adds the drawer.
	 * 
	 * @param group
	 *            the group
	 */
	public void addDrawer(String group) {
		boolean exist = false;
		for (String g : groups) {
			if (g.equalsIgnoreCase(group))
				exist = true;
		}
		if (!exist) {
			groups.add(group);
			initialOnce = true;
		}

		// if ((!initialOnce))
		// createPartControl();
	}

	/**
	 * Adds the entry.
	 * 
	 * @param parent
	 *            the parent
	 * @param label
	 *            the label
	 * @param identity
	 *            the identity
	 * @param connection
	 *            the connection
	 * @param icon
	 *            the icon
	 */
	public void addEntry(String parent, String label, String identity,
			boolean connection, String icon) {
		if (!connections.containsKey(label)) {
			connections.put(label, connection);
			tools.add(label);
			tools.add(parent);
			icons.add(icon);

			initialOnce = true;
		}

		// if (!initialOnce)
		// createPartControl();
	}

	public HashMap<String, Boolean> getConnections() {
		return connections;
	}

	/**
	 * Gets the select class.
	 * 
	 * @return the select class
	 */
	public String getSelectClass() {
		return transferClass;
	}

	/**
	 * Sets the initial.
	 * 
	 * @param initial
	 *            the new initial
	 */
	public void setInitial(boolean initial) {
		isFocus = initial;
	}

	/**
	 * Gets the initial.
	 * 
	 * @return the initial
	 */
	public boolean getInitial() {
		return isInitial;
	}

	/**
	 * Creates the part control.
	 */
	public boolean getInitialOnce() {
		return initialOnce;
	}

	private boolean iniCanvas = true;
	private int size;
	Canvas canvas = null;
	Label labelText;

	public void createPartControl() {
		// enable scroll bar
		// canvas = new Canvas(parent,SWT.H_SCROLL | SWT.V_SCROLL);
		initialOnce = false;

		if (iniCanvas) {
			canvas = new Canvas(parent, SWT.BORDER);
			canvas.setBounds((int) (parent.getBounds().width * 0.8), 0,
					(int) (parent.getBounds().width * 0.2),
					parent.getBounds().height);
			canvas.setBackground(ColorConstants.white);
		}

		iniCanvas = false;
		size = tools.size() / 2;

		int gap = 10;
		int w1 = canvas.getBounds().width / 5;
		int w2 = canvas.getBounds().width - w1 - gap;
		int height = canvas.getBounds().height / 20;

		Image image;

		Label label;

		/** The images. */
		Image[] images;

		/** The label images. */
		final Label[] labelImages;

		/** The label texts. */
		final Label[] labelTexts;

		images = new Image[size];
		labelImages = new Label[size];
		labelTexts = new Label[size];

		int count = 0;
		for (int m = 0; m < groups.size(); m++) {
			final int selectItemGroup = m;
			String group = groups.get(m);
			if (!group.equalsIgnoreCase("Palette"))
				group = "XCore";
			image = new Image(display, "images/" + group + ".gif");

			label = new Label(canvas, SWT.NO);
			label.setImage(image);
			label.setBounds(0, count * height, w1, height);
			label.setBackground(colorSection);

			labelText = new Label(canvas, SWT.NO);
			labelText.setText(groups.get(m));
			labelText.setBounds(w1, count * height, w2, height);
			labelText.setBackground(colorSection);

			count++;

			// set popup menu
			Menu popupMenu = new Menu(labelText);
			MenuItem deleteItem = new MenuItem(popupMenu, SWT.CASCADE);
			deleteItem.setText("Delete Group");
			deleteItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println(labelText.getText() + " selected");
					deletTool(labelText.getText());
				}
			});
			labelText.setMenu(popupMenu);

			for (int i = 0; i < size; i++) {
				final int selectItem = i;
				if (groups.get(m).equalsIgnoreCase(tools.get(i * 2 + 1))) {
					if (icons.get(i).length() < 1) {
						images[i] = null;
					} else if (icons.get(i).contains(":")) {
						images[i] = new Image(display, icons.get(i));
					} else {
						images[i] = new Image(display, "icons/" + icons.get(i));
					}

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
							// labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);
							imageName = tools.get(j);
							Main.getInstance().getView().clearPorts();

							for (int a = 0; a < size; a++) {
								if (a != k && labelImages[a] != null) {
									// labelImages[a].setBackground(color);
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
							// labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);

							for (int a = 0; a < size; a++) {
								if (a != k && labelImages[a] != null) {
									// labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}
					});

					labelTexts[i] = new Label(canvas, SWT.NO);
					labelTexts[i].setText(tools.get(i * 2));
					labelTexts[i].setBounds(w1 + gap, count * height + 7, w2,
							height);
					labelTexts[i].setBackground(color);
					labelTexts[i].setToolTipText(tools.get(i * 2));

					labelTexts[i].addMouseListener(new MouseListener() {

						@Override
						public void mouseDoubleClick(MouseEvent arg0) {

						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							// labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);
							imageName = tools.get(j);
							Main.getInstance().getView().clearPorts();

							for (int a = 0; a < size; a++) {
								if (a != k && labelImages[a] != null) {
									// labelImages[a].setBackground(color);
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
							// labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);

							for (int a = 0; a < size; a++) {

								if (a != k && labelImages[a] != null) {
									// labelImages[a].setBackground(color);
									labelTexts[a].setBackground(color);
								}
							}
						}
					});

					// set popup menu
					popupMenu = new Menu(labelTexts[i]);
					deleteItem = new MenuItem(popupMenu, SWT.CASCADE);
					deleteItem.setText("Delete Node");
					deleteItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							// Main.getInstance()
							// .getView()
							// .deleteToolGroup(
							// labelTexts[selectItem].getText());
							deletTool(labelTexts[selectItem].getText());

						}
					});
					labelTexts[i].setMenu(popupMenu);

					labelTexts[i].pack(true);

					count++;

				}
			}
		}

		canvas.layout(true);
		canvas.pack();
		canvas.pack(true);
		canvas.redraw();

		Main.tabFolderDiagram.pack();
		Main.tabFolderDiagram.layout();
		Main.tabFolderDiagram.layout(true);
		Main.tabFolderDiagram.redraw();

		// Main.sectionTopMiddle.pack();
		Main.sectionTopMiddle.layout(true);

		isInitial = true; // make use this function only run once
	}

	public boolean isContains(int x, int y, int x2, int y2, int width,
			int height) {
		boolean isInside = false;
		if (x >= x2 && y >= y2 && x <= x2 + width && y <= y2 + height)
			isInside = true;
		return isInside;
	}

	/**
	 * Translate to relative location.
	 * 
	 * @param location
	 *            the location
	 * @return the org.eclipse.draw2d.geometry. point
	 */

	public org.eclipse.draw2d.geometry.Point translateToRelativeLocation(
			Point location) {
		Point point = null;
		point = Main.display.map(null, canvas, location);
		org.eclipse.draw2d.geometry.Point point2 = new org.eclipse.draw2d.geometry.Point(
				0, 0);
		point2.x = point.x;
		point2.y = point.y;

		return point2;
	}

	/**
	 * Gets the select image.
	 * 
	 * @return the select image
	 */
	public void deletTool(String toolName) {
		for (int i = 0; i < groups.size(); i++) {
			String s = groups.get(i);
			if (s.equalsIgnoreCase(toolName)) {

				for (int j = 0; j < tools.size() / 2; j++) {
					String parent = tools.get(j * 2 + 1);
					String label = tools.get(j * 2);
					if (s.equalsIgnoreCase(parent)) {
						deletTool(label);
					}

				}
				groups.remove(i);
			}
		}

		for (int i = 0; i < tools.size() / 2; i++) {
			String s = tools.get(i * 2);
			if (s.equalsIgnoreCase(toolName)) {
				// tools.remove(s);
				tools.remove(i * 2);
				tools.remove(i * 2);
				icons.remove(i);
			}
		}
		iniCanvas = true;
		canvas.dispose();
		createPartControl();
	}

	public String getSelectImage() {

		return imageName;
	}

	/**
	 * Checks if is focus.
	 * 
	 * @return true, if is focus
	 */
	public boolean isFocus() {
		return isFocus;
	}

	/**
	 * Sets the focus.
	 * 
	 * @param focus
	 *            the focus
	 * @param palettes
	 *            the palettes
	 */
	public void setFocus(boolean focus, Vector<Palette> palettes) {
		_palettes = palettes;
		setAllFocus();
		isFocus = focus;
	}

	/**
	 * Sets the all focus.
	 */
	public void setAllFocus() {
		for (Palette palettes : _palettes) {
			palettes.setFocus(false);
		}
	}

	/**
	 * Sets the focus.
	 * 
	 * @param focus
	 *            the new focus
	 */
	public void setFocus(boolean focus) {
		isFocus = focus;
	}

	/**
	 * Sets the select image.
	 */
	public void setSelectImage() {
		imageName = "";

		// for (int a = 0; a < labelImages.length; a++) {
		// labelImages[a].setBackground(color);
		// labelTexts[a].setBackground(color);
		// }
	}

}
