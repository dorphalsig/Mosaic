package uk.ac.mdx.xmf.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eclipse.draw2d.ColorConstants;
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
	HashMap<String,Boolean> connections=new HashMap<String,Boolean>();
	
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
	Canvas canvas;
	
	/** The is focus. */
	private boolean isFocus = false;
	
	/** The is initial. */
	private boolean isInitial = false;

	/** The images. */
	private Image[] images;
	
	/** The label images. */
	private Label[] labelImages;
	
	/** The label texts. */
	private Label[] labelTexts;
	
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
	 * @param parent the parent
	 * @param style the style
	 * @param display the display
	 */
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
	 * @param group the group
	 */
	public void addDrawer(String group) {
		boolean exist = false;
		for (String g : groups) {
			if (g.equalsIgnoreCase(group))
				exist = true;
		}
		if (!exist)
			groups.add(group);
	}

	/**
	 * Adds the entry.
	 *
	 * @param parent the parent
	 * @param label the label
	 * @param identity the identity
	 * @param connection the connection
	 * @param icon the icon
	 */
	public void addEntry(String parent, String label, String identity,
			boolean connection, String icon) {
		tools.add(label);
		tools.add(parent);
		icons.add(icon);
		connections.put(label,connection);
	}
    public HashMap<String,Boolean> getConnections(){
    	return  connections;
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
	 * @param initial the new initial
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
	public void createPartControl() {

		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setBounds((int) (parent.getBounds().width * 0.8), 0,
				(int) (parent.getBounds().width * 0.2),
				parent.getBounds().height);
		canvas.setBackground(ColorConstants.white);

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
			String group=groups.get(m);
			if (!group.equalsIgnoreCase("Palette"))
				group="XCore";
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

			for (int i = 0; i < size; i++) {
				if (groups.get(m).equalsIgnoreCase(tools.get(i * 2 + 1))) {
					if (icons.get(i ).contains(":"))
						images[i] = new Image(display,icons.get(i )
								);
					else
					images[i] = new Image(display, "icons/" + icons.get(i )
							);

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
							Main.getInstance().getView().clearPorts();

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

						}

						@Override
						public void mouseDown(MouseEvent arg0) {
							labelImages[k].setBackground(colorSelect);
							labelTexts[k].setBackground(colorSelect);
							imageName = tools.get(j);
							Main.getInstance().getView().clearPorts();

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

		Main.tabFolderDiagram.pack();
		Main.tabFolderDiagram.layout();
		Main.tabFolderDiagram.layout(true);
		Main.tabFolderDiagram.redraw();

		// Main.sectionTopMiddle.pack();
		Main.sectionTopMiddle.layout(true);

		isInitial = true; // make use this function only run once
	}

	/**
	 * Gets the select image.
	 *
	 * @return the select image
	 */
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
	 * @param focus the focus
	 * @param palettes the palettes
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
	 * @param focus the new focus
	 */
	public void setFocus(boolean focus) {
		isFocus = focus;
	}

	/**
	 * Sets the select image.
	 */
	public void setSelectImage() {
		imageName = "";

		for (int a = 0; a < labelImages.length; a++) {
			labelImages[a].setBackground(color);
			labelTexts[a].setBackground(color);
		}
	}

}
