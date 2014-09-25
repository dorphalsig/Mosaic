package uk.ac.mdx.xmf.swt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import uk.ac.mdx.xmf.swt.command.ConnectionCommand;
import uk.ac.mdx.xmf.swt.command.CreateNodeCommand;
import uk.ac.mdx.xmf.swt.command.CreateWaypointCommand;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.editPart.BoxEditPart;
import uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart;
import uk.ac.mdx.xmf.swt.editPart.DiagramEditPart;
import uk.ac.mdx.xmf.swt.editPart.EdgeEditPart;
import uk.ac.mdx.xmf.swt.editPart.EdgeTextEditPart;
import uk.ac.mdx.xmf.swt.editPart.EllipseEditPart;
import uk.ac.mdx.xmf.swt.editPart.ImageEditPart;
import uk.ac.mdx.xmf.swt.editPart.MultilineTextEditPart;
import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.editPart.ShapeEditPart;
import uk.ac.mdx.xmf.swt.editPart.TextEditPart;
import uk.ac.mdx.xmf.swt.figure.BoxFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeLabelFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeShapeFigure;
import uk.ac.mdx.xmf.swt.figure.ImageFigure;
import uk.ac.mdx.xmf.swt.figure.LabelFigure;
import uk.ac.mdx.xmf.swt.figure.MultilineTextFigure;
import uk.ac.mdx.xmf.swt.figure.NodeShapeFigure;
import uk.ac.mdx.xmf.swt.io.Provider;
import uk.ac.mdx.xmf.swt.misc.VisualElementEvents;
import uk.ac.mdx.xmf.swt.model.AbstractDiagram;
import uk.ac.mdx.xmf.swt.model.Box;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.EdgeText;
import uk.ac.mdx.xmf.swt.model.Ellipse;
import uk.ac.mdx.xmf.swt.model.Image;
import uk.ac.mdx.xmf.swt.model.MultilineText;
import uk.ac.mdx.xmf.swt.model.Node;
import uk.ac.mdx.xmf.swt.model.Shape;
import uk.ac.mdx.xmf.swt.model.Text;
import xos.Message;
import xos.Value;

import com.ceteva.menus.MenuBuilder;

// TODO: Auto-generated Javadoc

/**
 * listeners to the views of diagram.
 * 
 * @author yongjun1
 */
public class DiagramView extends View {

	/** The parent. */
	Composite parent;

	/** The canvas. */
	public FigureCanvas canvas;

	/** The root figure. */
	public Figure rootFigure;

	/** The figure. */
	public Figure figure;

	/** The palette. */
	Palette palette;

	/** The display. */
	Display display;

	/** The tool identity. */
	String toolIdentity;

	/** The model identity. */
	String modelIdentity;

	/** The create node command. */
	CreateNodeCommand createNodeCommand;

	/** The select icon name. */
	String selectIconName = "";

	/** The display figure. */
	Figure displayFigure = new Figure();

	/** The provider. */
	Provider provider;

	/** The contents. */
	Vector contents = new Vector();

	/** The nodes. */
	Vector nodes = new Vector();

	/** The edges. */
	Vector edges = new Vector();

	/** The ports. */
	Vector<Node> ports = new Vector<Node>();

	/** The text edit part. */
	TextEditPart textEditPart;

	/** The node edit part. */
	NodeEditPart nodeEditPart;

	/** The box edit part. */
	BoxEditPart boxEditPart;

	/** The multiline text edit part. */
	MultilineTextEditPart multilineTextEditPart;

	/** The edge edge text edit. */
	EdgeTextEditPart edgeEdgeTextEdit;

	/** The shape edit part. */
	ShapeEditPart shapeEditPart;

	EllipseEditPart ellipseEditPart;

	/** The image edit part. */
	ImageEditPart imageEditPart;

	/** The edge edit part. */
	EdgeEditPart edgeEditPart;

	/** The diagram edit part. */
	DiagramEditPart diagramEditPart;

	/** The source anchor. */
	private ChopboxAnchor sourceAnchor;

	/** The target anchor. */
	private ChopboxAnchor targetAnchor;

	/** The _edit part. */
	CommandEventEditPart _editPart;

	/** The identity. */
	String identity;

	/** The is focus. */
	boolean isFocus = false;

	/** The _tab item. */
	CTabItem _tabItem;

	/** The _views. */
	Vector<DiagramView> _views;

	/** The _diagram. */
	AbstractDiagram _diagram;

	/** The rect shape. */
	RectangleFigure rectShape = new RectangleFigure();
	Polyline line = new Polyline();

	/** The mouse down. */
	private boolean mouseDown = false;
	private String cursorName = "";

	/** The node is selected. */
	private boolean nodeIsSelected = false;

	/** The node select. */
	private String nodeSelect = "";

	/** The edge select. */
	private String edgeSelect = "";

	/** The get point. */
	private String getPoint = "";

	/** The get edge point. */
	private String getEdgePoint = "";

	/** The get point index. */
	private int getPointIndex = 0;

	/** The resize shape. */
	private boolean resizeShape = false;

	/** The resize edge shape. */
	private boolean resizeEdgeShape = false;

	/** The drag points. */
	private Vector dragPoints = new Vector();

	/** The set drag point once. */
	private boolean setDragPointOnce = true;

	/** The edge drage shape. */
	Figure edgeDrageShape = new Figure();

	private Object iModel;

	Cursor[] cursor = new Cursor[3];
	ImageData image0 = new ImageData("icons/nodeWait.gif");
	ImageData image1 = new ImageData("icons/connectionWait.gif");
	ImageData image2 = new ImageData("icons/cursor-lifebuoy-icon.gif");

	/**
	 * Instantiates a new diagram view.
	 * 
	 * @param parent
	 *            section of the main shell
	 * @param style
	 *            display style
	 * @param palette
	 *            different palette mapping to differnt views
	 * @param display
	 *            SWT display
	 * @param diagramClient
	 *            client register into xmf
	 * @param diagram
	 *            diagram instance of each view
	 * @param tabItem
	 *            tabItem to be shown into the shell
	 */
	public DiagramView(Composite parent, int style, Palette palette,
			Display display, DiagramClient diagramClient,
			AbstractDiagram diagram, CTabItem tabItem) {
		super(parent, style);
		this.parent = parent;
		this.palette = palette;
		this.display = display;
		_diagram = diagram;

		diagramClient.setView(this);
		this.setVisible(false);
		canvas = new FigureCanvas(parent, SWT.H_SCROLL & SWT.V_SCROLL);
		canvas.setBounds(0, 0, (int) (parent.getBounds().width * 0.8),
				parent.getBounds().height);
		canvas.setBackground(ColorConstants.white);
		canvas.setScrollBarVisibility(FigureCanvas.ALWAYS);
		// canvas.setViewport(new Viewport(true));

		registerListener();
		iniEditpart();
		_tabItem = tabItem;

		figure = new Figure();
		rootFigure = new Figure();
		rootFigure.add(rectShape);
		rootFigure.add(line);
		rootFigure.add(edgeDrageShape);

		cursor[0] = new Cursor(display, image0, 0, 0);
		cursor[1] = new Cursor(display, image1, 0, 0);
		cursor[2] = new Cursor(display, image2, 0, 0);

		Main.tabFolderDiagram.setSelection(tabItem);
	}

	public CTabItem getTabItem() {
		return _tabItem;
	}

	/**
	 * Sets the identity.
	 * 
	 * @param identity
	 *            the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * Sets the focus.
	 * 
	 * @param focus
	 *            the focus
	 * @param views
	 *            the views
	 */
	public void setFocus(boolean focus, Vector<DiagramView> views) {
		raiseFocusGained();
		_views = views;
		setAllFocus();
		isFocus = focus;
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
	 * Sets the all focus.
	 */
	public void setAllFocus() {
		for (DiagramView view : _views) {
			view.setFocus(false);
		}
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
	 * Gets the identtity.
	 * 
	 * @return the identtity
	 */
	public String getIdenttity() {
		return identity;
	}

	/**
	 * Ini editpart.
	 */
	public void iniEditpart() {

		// nodeEditPart.activate();

		diagramEditPart = new DiagramEditPart();
		diagramEditPart.setModel(_diagram);
		diagramEditPart.activate();
		diagramEditPart.setDiagramView(this);

	}

	/**
	 * Register listener.
	 */
	private boolean dragEdge = true;
	int number = 0;
	private String edgeTextId = "";

	public void registerListener() {

		canvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				mouseDown = true;

				// conditions for dragEdge
				dragEdge = true;
				line.setVisible(false);

				canvas.setCursor(Display.getCurrent().getSystemCursor(
						SWT.CURSOR_ARROW));

				Vector<String> identities = new Vector<String>();

				identities.add(identity);

				MenuBuilder.resetKeyBindings(null);
				MenuManager manager = new MenuManager();
				MenuBuilder.calculateMenu(identities, manager, null);
				canvas.setMenu(manager.createContextMenu(canvas));

				String s = Main.getInstance().getPalette().getSelectImage();
				HashMap<String, Boolean> connections = new HashMap<String, Boolean>();
				connections = Main.getInstance().getPalette().getConnections();
				cursorName = s;
				// setAllFocus();
				// isFocus = true;
				Point location = display.getCursorLocation();

				org.eclipse.draw2d.geometry.Point location2 = translateToRelativeLocation(location);

				// check click node
				boolean nodeContains = false;
				Iterator<String> keySetIterator = nodeModels.keySet()
						.iterator();

				while (keySetIterator.hasNext()) {
					String key = keySetIterator.next();
					Node node = nodeModels.get(key);

					if (figureNodes.get(key).containsPoint(location2)) {
						nodeSelect = node.getIdentity();
						// restShape(node.getLocation(), node.getSize());
						nodeShapes.get(node.getIdentity()).setVisible(true);
						nodeShapes.get(node.getIdentity()).setOpaque(false);

						if (connections.get(s) != null && connections.get(s))
							ports.add(node);

						node.selectNode();
						nodeContains = true;

						//
						for (int i = 0; i < figureNodes.get(key).getChildren()
								.size() - 1; i++) {
							if (figureNodes.get(key).getChildren().get(i) instanceof BoxFigure) {
								BoxFigure box1 = (BoxFigure) figureNodes
										.get(key).getChildren().get(i);
								BoxFigure box2 = (BoxFigure) figureNodes
										.get(key).getChildren().get(i + 1);
								BoxFigure box = box1;
								if (box1.getSize().height < box2.getSize().height) {
									box = box2;
								}
							}
						}

						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								0, 0);
						p.x = node.getLocation().x - 2;
						p.y = node.getLocation().y - 2;

						Rectangle shapeRec = new Rectangle();
						shapeRec.setLocation(p);
						Dimension dim = new Dimension();
						dim.width = node.getSize().width + 4;
						dim.height = node.getSize().height + 4;
						shapeRec.setSize(dim);

						NodeShapeFigure shape;
						shape = (NodeShapeFigure) nodeShapes.get(key);
						shape.reSetPoints(p, dim);
						shape.setBounds(shapeRec);
						shape.setLineWidth(5);

						org.eclipse.draw2d.geometry.Point l = figureNodes.get(
								key).getLocation();
						Dimension d = figureNodes.get(key).getSize();

						// add sub menu
						Vector<String> identitiesNode = new Vector<String>();
						NodeEditPart editPart = nodeEditParts.get(nodeSelect);

						identitiesNode.add(nodeSelect);

						MenuBuilder.resetKeyBindings(null);
						MenuManager managerNode = new MenuManager();
						MenuBuilder.calculateMenu(identitiesNode, managerNode,
								null);
						canvas.setMenu(managerNode.createContextMenu(canvas));

					} else {
						node.deselectNode();
						nodeShapes.get(node.getIdentity()).setVisible(false);
					}

				}
				if (nodeContains)
					nodeIsSelected = true;
				else
					nodeIsSelected = false;

				// -----check mouse click

				Iterator<String> nodeShapeItr = nodeShapes.keySet().iterator();

				while (nodeShapeItr.hasNext()) {
					String key = nodeShapeItr.next();
					NodeShapeFigure shape = (NodeShapeFigure) nodeShapes
							.get(key);

					if (shape.isVisible()) {
						getPoint = shape.isDragPointClicked(location2);
						// shape.setOutline(true);
					}
				}

				// check edge label
				Iterator<String> edgeTextItr = edgeTexts.keySet().iterator();

				while (edgeTextItr.hasNext()) {
					String key = edgeTextItr.next();
					EdgeLabelFigure label = edgeLabelFigures.get(key);

					if (label.containsPoint(location2)) {
						getPoint = VisualElementEvents.edgeLabelPoint;
						edgeTextId = key;

						// add sub menu
						Vector<String> identitiesEdge = new Vector<String>();

						identitiesEdge.add(key);

						MenuBuilder.resetKeyBindings(null);
						MenuManager managerEdge = new MenuManager();
						MenuBuilder.calculateMenu(identitiesEdge, managerEdge,
								null);
						canvas.setMenu(managerEdge.createContextMenu(canvas));
					}
				}

				// check click Edge line

				Iterator<String> edgeIterator = edgeFigures.keySet().iterator();

				while (edgeIterator.hasNext()) {
					String key = edgeIterator.next();
					EdgeFigure edgeFigure = edgeFigures.get(key);

					org.eclipse.draw2d.geometry.Point topRef = edgeFigure
							.getSourceAnchor().getReferencePoint();
					org.eclipse.draw2d.geometry.Point bottomRef = edgeFigure
							.getTargetAnchor().getReferencePoint();

					// if (checkEdgeIsClicked(topRef, bottomRef, location2))
					if (((EdgeShapeFigure) edgeShapes.get(key))
							.isClicked(location2)) {
						edgeShapes.get(key).setVisible(true);
						edgeFigures.get(key).setOutline(false);

						edgeSelect = key;

						// add sub menu
						Vector<String> identitiesEdge = new Vector<String>();

						identitiesEdge.add(key);

						MenuBuilder.resetKeyBindings(null);
						MenuManager managerEdge = new MenuManager();
						MenuBuilder.calculateMenu(identitiesEdge, managerEdge,
								null);
						canvas.setMenu(managerEdge.createContextMenu(canvas));

					} else {
						edgeShapes.get(key).setVisible(false);
						edgeFigures.get(key).setOutline(true);

						dragPoints.clear();
					}

				}

				if (connections.get(s) != null && connections.get(s)) // connection
				{
					if (ports.size() > 1) {
						raiseFocusGained();

						String toolIdentity = s;

						ConnectionCommand connectionCommand = new ConnectionCommand();
						connectionCommand.setParent(_diagram);
						connectionCommand.setToolIdentity(toolIdentity);
						String source = (String) ports.get(0).getIdentity();
						String target = (String) ports.get(1).getIdentity();
						connectionCommand.setSource(String.valueOf(Integer
								.valueOf(source) + 1));
						connectionCommand.setTarget(String.valueOf(Integer
								.valueOf(target) + 1));
						connectionCommand.execute();
						ports.clear();

						Main.getInstance().palette.setSelectImage();
					}
				} else { // node
					raiseFocusGained();

					String toolIdentity = s;
					NodeEditPart nodeEditPart = new NodeEditPart();
					nodeEditPart.setModel(_diagram);
					createNodeCommand = new CreateNodeCommand(nodeEditPart,
							toolIdentity, location2);
					createNodeCommand.execute();

					selectIconName = "";
					Main.getInstance().palette.setSelectImage();
				}
			}
		});

		canvas.addListener(SWT.MouseMove, new Listener() {

			@Override
			public void handleEvent(Event arg0) {

				Point location = display.getCursorLocation();
				org.eclipse.draw2d.geometry.Point location2 = translateToRelativeLocation(location);

				String getTool = Main.getInstance().getPalette()
						.getSelectImage();
				HashMap<String, Boolean> connections = new HashMap<String, Boolean>();
				connections = Main.getInstance().getPalette().getConnections();

				if (connections.get(getTool) != null
						&& !connections.get(getTool)) {
					canvas.setCursor(cursor[0]);
				}
				if (getTool.length() < 1)
					getTool = cursorName;

				if (connections.get(getTool) != null) {
					if (connections.get(getTool)) // connection
					{
						Iterator<String> keySetIterator = nodeShapes.keySet()
								.iterator();

						while (keySetIterator.hasNext()) {
							String key = keySetIterator.next();
							NodeShapeFigure shape = (NodeShapeFigure) nodeShapes
									.get(key);
							if (shape.containsPoint(location2)) {
								// canvas.setCursor(Display.getCurrent()
								// .getSystemCursor(SWT.CURSOR_ARROW));
								canvas.setCursor(cursor[2]);
								break;
							} else {
								canvas.setCursor(cursor[1]);
							}
						}

					}
				}

				if (connections.get(getTool) != null
						&& connections.get(getTool)) // connection
				{
					if (ports.size() == 1) {
						line.setStart(ports.get(0).getLocation());
						line.setBackgroundColor(ColorConstants.black);
						line.setEnd(location2);
						line.setVisible(true);
					} else {
						line.setVisible(false);
					}
				}

				// -----check mouse hover

				if (!mouseDown) {

					Iterator<String> keySetIterator = nodeShapes.keySet()
							.iterator();

					while (keySetIterator.hasNext()) {
						String key = keySetIterator.next();
						NodeShapeFigure shape = (NodeShapeFigure) nodeShapes
								.get(key);

						getPoint = shape.isDragPointClicked(location2);

						if (shape.isVisible()) {
							if (getPoint
									.equalsIgnoreCase(VisualElementEvents.topMiddlePoint))

							{
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZEN));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightMiddlePoint))

							{
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZEE));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.bottomMiddlePoint))

							{
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZEN));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftMiddlePoint))

							{
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZEE));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftTopCornerPoint)) {
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZESE));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightTopCornerPoint)) {
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZENE));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightBottomCornerPoint)) {
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZESE));
							} else if (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftBottomCornerPoint)) {
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_SIZENE));
							} else {
								canvas.setCursor(Display.getCurrent()
										.getSystemCursor(SWT.CURSOR_ARROW));
							}
						}
					}

					// check edge label
					Iterator<String> edgeTextItr = edgeTexts.keySet()
							.iterator();

					while (edgeTextItr.hasNext()) {
						String key = edgeTextItr.next();
						EdgeLabelFigure label = edgeLabelFigures.get(key);

						if (label.containsPoint(location2)) {
							canvas.setCursor(Display.getCurrent()
									.getSystemCursor(SWT.CURSOR_SIZEALL));
							// getPoint = VisualElementEvents.edgeLabelPoint;
							// edgeTextId=key;
						}
					}
				}

				// check edge

				Iterator<String> edgeIterator = edgeModels.keySet().iterator();

				while (edgeIterator.hasNext()) {
					String key = edgeIterator.next();
					Edge edge = edgeModels.get(key);

					getEdgePoint = edge.getPointElement(location2);

					if (edgeShapes.get(edgeSelect) != null
							&& edgeShapes.get(edgeSelect).isVisible()) {
						if (getEdgePoint
								.equalsIgnoreCase(VisualElementEvents.wayPointEdgePoint))

						{
							canvas.setCursor(Display.getCurrent()
									.getSystemCursor(SWT.CURSOR_SIZEALL));

						} else if (getEdgePoint
								.equalsIgnoreCase(VisualElementEvents.moveEdgePoint))

						{
							canvas.setCursor(Display.getCurrent()
									.getSystemCursor(SWT.CURSOR_SIZEALL));

						} else {
							canvas.setCursor(Display.getCurrent()
									.getSystemCursor(SWT.CURSOR_ARROW));
						}
					}
				}

				// -----end check mouse hover

				if (mouseDown) {
					// System.out.println("Point:" + getPoint);
					Figure shape = nodeShapes.get(nodeSelect);

					if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightMiddlePoint))) {

						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = Math.abs(location2.x
								- node.getLocation().x);
						size.height = nodeShapes.get(nodeSelect).getSize().height - 4;

						rectShape.setLocation(node.getLocation());
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						resizeShape = true;
						rectShape.setVisible(true);
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftMiddlePoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = Math.abs(location2.x
								- node.getLocation().x);
						size.height = nodeShapes.get(nodeSelect).getSize().height - 4;

						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								location2.x, node.getLocation().y);

						rectShape.setLocation(p);
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						resizeShape = true;
						rectShape.setVisible(true);
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.topMiddlePoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = node.getSize().width;
						size.height = Math.abs(node.getLocation().y
								- location2.y);
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								node.getLocation().x, location2.y);
						rectShape.setLocation(p);
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.bottomMiddlePoint))) {

						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = node.getSize().width;
						size.height = Math.abs(node.getLocation().y
								- location2.y);
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								node.getLocation().x, node.getLocation().y);
						rectShape.setLocation(p);
						rectShape.setSize(size.width, size.height);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftTopCornerPoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = node.getSize().width - location2.x
								+ node.getLocation().x;
						size.height = Math.abs(node.getLocation().y
								- location2.y)
								+ node.getSize().height;
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								node.getLocation().x, location2.y);
						rectShape.setLocation(location2);
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightTopCornerPoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = Math.abs(location2.x
								- node.getLocation().x);
						size.height = Math.abs(node.getLocation().y
								- location2.y)
								+ node.getSize().height;
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								node.getLocation().x, location2.y);
						rectShape.setLocation(p);
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.rightBottomCornerPoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = Math.abs(location2.x
								- node.getLocation().x);
						size.height = Math.abs(node.getLocation().y
								- location2.y);
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								node.getLocation().x, location2.y);
						rectShape.setLocation(node.getLocation());
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else if (shape != null
							&& (getPoint
									.equalsIgnoreCase(VisualElementEvents.leftBottomCornerPoint))) {
						Node node = nodeModels.get(nodeSelect);
						Dimension size = new Dimension();
						size.width = Math.abs(location2.x
								- node.getLocation().x)
								+ node.getSize().width;
						size.height = Math.abs(node.getLocation().y
								- location2.y);
						org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
								location2.x, node.getLocation().y);
						rectShape.setLocation(p);
						rectShape.setSize(size);
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					} else {
						if (nodeIsSelected
								&& canvas.getCursor().hashCode() == 65539
								&& !getPoint
										.equalsIgnoreCase(VisualElementEvents.edgeLabelPoint)) {
							Node node = nodeModels.get(nodeSelect);
							Dimension size = new Dimension();
							size = node.getSize();

							rectShape.setLocation(location2);
							rectShape.setSize(size.width, size.height);
							rectShape
									.setBackgroundColor(ColorConstants.lightGray);
							rectShape.setVisible(true);
							resizeShape = true;

							nodeShapes.get(nodeSelect).setLocation(location2);
						}
					}

					// move edge label
					EdgeLabelFigure label = edgeLabelFigures.get(edgeTextId);

					if (label != null
							&& getPoint
									.equalsIgnoreCase(VisualElementEvents.edgeLabelPoint)) {

						rectShape.setLocation(location2);
						rectShape.setSize(label.getSize());
						rectShape.setBackgroundColor(ColorConstants.lightGray);
						rectShape.setVisible(true);
						resizeShape = true;
					}

				}

				if (dragEdge) {

					Edge edge = edgeModels.get(edgeSelect);
					EdgeEditPart edit = edgeEditPartFigures.get(edgeSelect);

					if (edge != null && edgeShapes.get(edgeSelect).isVisible()) {

						if (setDragPointOnce)
							edge.setDragPoints();

						edge.setDragPoints(location2);

						edgeDrageShape = (Figure) edit.createFigure(true);

						edgeDrageShape.setVisible(true);

						setDragPointOnce = false;

						rootFigure.repaint();
						resizeEdgeShape = true;
					}
				}
			}
		});

		// direct edit
		canvas.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Point location = display.getCursorLocation();

				org.eclipse.draw2d.geometry.Point location2 = translateToRelativeLocation(location);

				Iterator<String> itrLabel = figureLabels.keySet().iterator();

				while (itrLabel.hasNext()) {
					String key = itrLabel.next();

					Text model = texts.get(key);
					Label l = figureLabels.get(key);
					String id = model.parent.identity;
					Point p = null;

					int width = l.getSize().width;
					int height = l.getSize().height;
					Dimension d = new Dimension(width, height);

					String select = nodeSelect;
					select = model.getParent().getParent().getParent()
							.getIdentity();

					Node node = nodeModels.get(select);
					if (node != null)
						p = new Point(node.getLocation().x
								+ model.getLocation().x, node.getLocation().y
								+ model.getLocation().y);
					else
						p = new Point(0, 0);
					if (checkRectangleBoundary(location2.x, location2.y, p.x,
							p.y, width, height)) {
						textEdits.get(key).performDirectEdit(model, p, d);
					}
				}
				// edit multiline label, for example, not
				Iterator<String> itrMultiline = figureMulitLineTextLabels
						.keySet().iterator();

				while (itrMultiline.hasNext()) {
					String key = itrMultiline.next();

					MultilineText model = multilineTexts.get(key);
					MultilineTextFigure l = figureMulitLineTextLabels.get(key);
					String id = model.parent.identity;
					Point p = null;

					int width = l.getSize().width;
					int height = l.getSize().height;
					Dimension d = new Dimension(width, height);

					String select = nodeSelect;
					// select = model.getParent().getParent().getParent()
					// .getIdentity();

					Node node = nodeModels.get(select);
					if (node != null)
						p = new Point(node.getLocation().x
								+ model.getLocation().x, node.getLocation().y
								+ model.getLocation().y);
					else
						p = new Point(0, 0);

					if (checkRectangleBoundary(location2.x, location2.y, p.x,
							p.y, width, height)) {
						multilineTextEdits.get(key).performDirectEdit(model, p,
								d);
					}
				}

				// edit edge label text

				Iterator<String> itrEdgeLabel = edgeLabelFigures.keySet()
						.iterator();

				while (itrEdgeLabel.hasNext()) {
					String key = itrEdgeLabel.next();

					EdgeText model = edgeTexts.get(key);
					String id = model.parent.identity; // get editpart
					EdgeTextEditPart edgeTextEditPart = edgeTextEditParts
							.get(model.getIdentity());
					EdgeLabelFigure l = edgeLabelFigures.get(key);
					Point p = new Point(l.getLocation().x, l.getLocation().y);

					int width = l.getSize().width;
					int height = l.getSize().height;
					Dimension d = new Dimension(width, height);

					if (width != 0) {
						if (checkRectangleBoundary(location2.x, location2.y,
								l.getLocation().x, l.getLocation().y, width,
								height)) {
							edgeTextEditPart.performDirectEdit(model, p, d);
						}
					}
				}

			}
		});
		canvas.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Point location = display.getCursorLocation();
				org.eclipse.draw2d.geometry.Point location2 = translateToRelativeLocation(location);
				if (resizeShape
						&& (getPoint
								.equalsIgnoreCase(VisualElementEvents.rightMiddlePoint))) {
					Node node = nodeModels.get(nodeSelect);
					Dimension size = new Dimension();
					size.width = Math.abs(location2.x - node.getLocation().x);
					size.height = node.getSize().height;
					node.moveResize(size);
				} else if (resizeShape
						&& (getPoint
								.equalsIgnoreCase(VisualElementEvents.leftMiddlePoint))) {
					Node node = nodeModels.get(nodeSelect);
					Dimension size = new Dimension();
					size.width = Math.abs(location2.x - node.getLocation().x)
							+ node.getSize().width;
					size.height = node.getSize().height;
					node.moveResize(size);

					org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
							location2.x, node.getLocation().y);
					node.moveResize(p);
				} else if (resizeShape
						&& (getPoint
								.equalsIgnoreCase(VisualElementEvents.topMiddlePoint))) {
					Node node = nodeModels.get(nodeSelect);
					Dimension size = new Dimension();
					size.width = node.getSize().width;
					size.height = Math.abs(node.getLocation().y - location2.y)
							+ node.getSize().height;
					node.moveResize(size);

					org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
							node.getLocation().x, location2.y);
					node.moveResize(p);
				} else if (resizeShape
						&& (getPoint
								.equalsIgnoreCase(VisualElementEvents.bottomMiddlePoint))) {
					Node node = nodeModels.get(nodeSelect);
					Dimension size = new Dimension();
					size.width = node.getSize().width;
					size.height = Math.abs(node.getLocation().y - location2.y);
					node.moveResize(size);
				} else if (resizeShape
						&& getPoint
								.equalsIgnoreCase(VisualElementEvents.leftTopCornerPoint)
						|| getPoint
								.equalsIgnoreCase(VisualElementEvents.rightTopCornerPoint)
						|| getPoint
								.equalsIgnoreCase(VisualElementEvents.leftBottomCornerPoint)
						|| getPoint
								.equalsIgnoreCase(VisualElementEvents.rightBottomCornerPoint)) {
					Node node = nodeModels.get(nodeSelect);
					node.moveResize(rectShape.getLocation());
					node.moveResize(rectShape.getSize());
				} else if (resizeShape
						&& (getPoint
								.equalsIgnoreCase(VisualElementEvents.edgeLabelPoint))) {
					EdgeText text = edgeTexts.get(edgeTextId);
					org.eclipse.draw2d.geometry.Point p1 = edgeLabelFigures
							.get(edgeTextId).getLocation();
					org.eclipse.draw2d.geometry.Point p = new org.eclipse.draw2d.geometry.Point(
							location2.x - p1.x, location2.y - p1.y);
					text.raiseMoveEvent(p);
				} else if (resizeShape) {
					Node node = nodeModels.get(nodeSelect);
					node.moveResize(location2);
					org.eclipse.draw2d.geometry.Point p = node.getLocation();
				}

				if (resizeShape) {
					Iterator<String> keySetIterator = nodeShapes.keySet()
							.iterator();

					while (keySetIterator.hasNext()) {
						String key = keySetIterator.next();
						NodeShapeFigure shape = (NodeShapeFigure) nodeShapes
								.get(key);
						shape.setVisible(false);
					}
				}

				EdgeShapeFigure edgeShape = edgeShapes.get(edgeSelect);
				Edge edge = edgeModels.get(edgeSelect);
				EdgeEditPart edit = edgeEditPartFigures.get(edgeSelect);

				if (resizeEdgeShape)
				// && getEdgePoint
				// .equalsIgnoreCase(VisualElementEvents.wayPointEdgePoint))
				{
					edge.setDragPoints(location2);

					edgeShape = (EdgeShapeFigure) edit.createFigure(true);

					edgeShape.setVisible(true);

					CreateWaypointCommand createWaypointCommand = new CreateWaypointCommand(
							edge, 0, location2);
					createWaypointCommand.execute();

				}

				getPoint = "";
				getEdgePoint = "";
				rectShape.setVisible(false);
				edgeDrageShape.setVisible(false);

				mouseDown = false;
				resizeShape = false;
				resizeEdgeShape = false;

				// conditions for dragEdge
				dragEdge = false;
				setDragPointOnce = true; // for another edge

				update();
				canvas.setCursor(Display.getCurrent().getSystemCursor(
						SWT.CURSOR_ARROW));
			}
		});
	}

	/**
	 * Clear ports.
	 */
	public void clearPorts() {
		ports.clear();
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
	 * Raise focus gained.
	 */
	public void raiseFocusGained() {
		Message m = _diagram.handler.newMessage("focusGained", 1);
		Value v1 = new Value(_diagram.identity);
		m.args[0] = v1;
		_diagram.handler.raiseEvent(m);
	}

	/**
	 * Adds the edit part.
	 * 
	 * @param editPart
	 *            the edit part
	 */
	public void addEditPart(CommandEventEditPart editPart) {
		_editPart = editPart;
		_editPart.setDiagramView(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.View#display()
	 */
	public void display() {
		canvas.setContents(rootFigure);
		// canvas.redraw();
		//
		// // rootFigure.add(figure);
		//
		// rootFigure.repaint();
		//
		// Main.getInstance().tabFolderDiagram.layout(true);

		// canvas.pack();
		// canvas.layout(true);
		// parent.pack();
		// this.redraw();
	}

	private void updateFigures() {
		// canvas.redraw();
		rootFigure.add(figure);

		// canvas.setRedraw(false);
		// display();
	}

	/**
	 * Gets the figure nodes.
	 * 
	 * @return the figure nodes
	 */
	public Map<String, Figure> getFigureNodes() {
		return figureNodes;
	}

	/**
	 * Gets the figure labels.
	 * 
	 * @return the figure labels
	 */
	public Map<String, Label> getFigureLabels() {
		return figureLabels;
	}

	/**
	 * Gets the figure mulit line text labels.
	 * 
	 * @return the figure mulit line text labels
	 */
	public Map<String, MultilineTextFigure> getfigureMulitLineTextLabels() {
		return figureMulitLineTextLabels;
	}

	/**
	 * Gets the figure boxs.
	 * 
	 * @return the figure boxs
	 */
	public Map<String, BoxFigure> getFigureBoxs() {
		return figureBoxs;
	}

	/**
	 * Gets the edge figure.
	 * 
	 * @return the edge figure
	 */
	public Map<String, EdgeFigure> getEdgeFigure() {
		return edgeFigures;
	}

	/**
	 * Gets the edge label figure.
	 * 
	 * @return the edge label figure
	 */
	public Map<String, EdgeLabelFigure> getEdgeLabelFigure() {
		return edgeLabelFigures;
	}

	/**
	 * Gets the edge parts.
	 * 
	 * @return the edge parts
	 */
	public Map<String, EdgeEditPart> getEdgeParts() {
		return edgeEditParts;
	}

	/**
	 * Check rectangle boundary.
	 * 
	 * @param pointX
	 *            the point x
	 * @param pointY
	 *            the point y
	 * @param rectangleX
	 *            the rectangle x
	 * @param rectangleY
	 *            the rectangle y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @return true, if successful
	 */
	private boolean checkRectangleBoundary(float pointX, float pointY,
			float rectangleX, float rectangleY, float width, float height) {
		boolean isInside = ((pointX > rectangleX)
				&& (pointX < rectangleX + width) && (pointY > rectangleY) && (pointY < rectangleY
				+ height));
		return isInside;
	}

	/**
	 * Refresh.
	 * 
	 * @param displays
	 *            the displays
	 */
	NodeShapeFigure shape;

	public void refresh(Vector displays) {
		this.contents = displays;
		updateFigures();

		for (int i = 0; i < contents.size(); i++) {
			iModel = contents.get(i);
			if (iModel instanceof Node) {
				figure = new Figure();
				nodeEditPart = new NodeEditPart();
				// Node node = (Node) iModel;
				nodeEditPart.setModel((Node) iModel);
				nodeEditPart.setDiagramView(this);
				nodeEditPart.activate();
				figure = (Figure) nodeEditPart.createFigure();

				Rectangle rect = new Rectangle(((Node) iModel).getLocation(),
						((Node) iModel).getSize());
				shape = (NodeShapeFigure) nodeEditPart.createFigure(true, rect);
				shape.setVisible(false);
				rootFigure.add(shape);
				figure.setBorder(null);
				String identity = ((Node) iModel).getIdentity();
				nodeEditParts.put(identity, nodeEditPart);
				nodeShapes.put(identity, shape);
				nodeModels.put(identity, (Node) iModel);
				figureNodes.put(identity, figure);
			} else if (iModel instanceof Box) {
				boxEditPart = new BoxEditPart();
				boxEditPart.setModel((Box) iModel);
				boxEditPart.setDiagramView(this);
				boxEditPart.activate();

				BoxFigure boxFigure = (BoxFigure) boxEditPart.createFigure();
				if (boxFigure != null) {
					String id = ((Box) iModel).identity;
					figure.add(boxFigure);
					figureBoxs.put(id, boxFigure);
				}

			} else if (iModel instanceof Text) {
				textEditPart = new TextEditPart();
				Text text = (Text) iModel;
				textEditPart.setModel(text);
				textEditPart.activate();
				//
				textEditPart.setDiagramView(this);
				LabelFigure label = (LabelFigure) textEditPart.createFigure();
				String id = text.parent.identity;

				BoxFigure boxFigure = figureBoxs.get(id);
				if (boxFigure != null) // check, because if the node is
										// "Mapping", then the parent figure is
										// node
					boxFigure.add(label);

				Figure nodeFigure = figureNodes.get(id);
				if (nodeFigure != null)
					nodeFigure.add(label);

				texts.put(text.getIdentity(), text);
				textEdits.put(text.getIdentity(), textEditPart);
				figureLabels.put(text.getIdentity(), label);

			} else if (iModel instanceof MultilineText) {
				multilineTextEditPart = new MultilineTextEditPart();
				// MultilineText text = (MultilineText) iModel;
				multilineTextEditPart.setModel((MultilineText) iModel);
				multilineTextEditPart.activate();
				//
				multilineTextEditPart.setDiagramView(this);
				MultilineTextFigure label = (MultilineTextFigure) multilineTextEditPart
						.createFigure();
				String s = ((MultilineText) iModel).getIdentity();
				multilineTexts.put(s, (MultilineText) iModel);
				figure.add(label);
				multilineTextEdits.put(s, multilineTextEditPart);
				figureMulitLineTextLabels.put(s, label);

			} else if (iModel instanceof Edge) {
				edgeEditPart = new EdgeEditPart();
				// Edge edge = (Edge) iModel;
				edgeEditPart.setModel((Edge) iModel);
				String id = ((Edge) iModel).getIdentity();
				edgeEditPart.activate();

				EdgeFigure edgeFigure = (EdgeFigure) edgeEditPart
						.createFigure();
				if (sourceAnchor != null && targetAnchor != null) {
					edgeFigure.setSourceAnchor(sourceAnchor);
					edgeFigure.setTargetAnchor(targetAnchor);

					edgeEditPart.setRoute(edgeFigure);

					org.eclipse.draw2d.geometry.Point location1 = sourceAnchor
							.getReferencePoint();
					org.eclipse.draw2d.geometry.Point location2 = targetAnchor
							.getReferencePoint();

					((Edge) iModel).setPoints(location1, location2);

					EdgeShapeFigure shape = (EdgeShapeFigure) edgeEditPart
							.createFigure(false);

					shape.setVisible(false);

					edgeShapes.put(((Edge) iModel).getIdentity(), shape);
					rootFigure.add(shape);

					if (rootFigure != null) {
						rootFigure.add(edgeFigure);
					}

					edgeFigures.put(((Edge) iModel).getIdentity(), edgeFigure);
					edgeEditPartFigures.put(((Edge) iModel).getIdentity(),
							edgeEditPart);
					edgeModels.put(id, (Edge) iModel);
					edgeEditParts.put(id, edgeEditPart);
				}

			} else if (iModel instanceof EdgeText) {
				edgeEdgeTextEdit = new EdgeTextEditPart();
				edgeEdgeTextEdit.setModel((EdgeText) iModel);
				edgeEdgeTextEdit.activate();
				//
				edgeEdgeTextEdit.setDiagramView(this);
				EdgeLabelFigure label = (EdgeLabelFigure) edgeEdgeTextEdit
						.createFigure();
				String id = ((EdgeText) iModel).parent.identity;

				EdgeFigure edgeFigure = edgeFigures.get(id);
				edgeFigure.add(label);
				label.setVisible(true);

				edgeLabelFigures.put(((EdgeText) iModel).getIdentity(), label);
				edgeTexts.put(((EdgeText) iModel).getIdentity(),
						(EdgeText) iModel);
				edgeTextEditParts.put(((EdgeText) iModel).getIdentity(),
						edgeEdgeTextEdit);
			} else if (iModel instanceof Shape) {
				shapeEditPart = new ShapeEditPart();
				shapeEditPart.setModel((Shape) iModel);
				shapeEditPart.setDiagramView(this);
				shapeEditPart.activate();
				IFigure shapeFigure = shapeEditPart.createFigure();
				if (shapeFigure != null) {
					figure.add(shapeFigure);
				}
			} else if (iModel instanceof Ellipse) {
				ellipseEditPart = new EllipseEditPart();
				ellipseEditPart.setModel((Ellipse) iModel);
				ellipseEditPart.setDiagramView(this);
				ellipseEditPart.activate();
				IFigure ellipseFigure = ellipseEditPart.createFigure();
				if (ellipseFigure != null) {
					figure.add(ellipseFigure);
				}
			} else if (iModel instanceof Image) {
				imageEditPart = new ImageEditPart();
				imageEditPart.setModel((Image) iModel);
				imageEditPart.setDiagramView(this);
				imageEditPart.activate();
				ImageFigure imageFigure = (ImageFigure) imageEditPart
						.createFigure();
				if (imageFigure != null) {
					String id = ((Image) iModel).identity;
					figure.add(imageFigure);
				}

			}
		}
		contents.clear();

	}

	/**
	 * Refresh children.
	 * 
	 * @param child
	 *            the child
	 */
	public void refreshChildren(IFigure child) {
		figure.add(child);
	}

	/**
	 * Sets the source anchor.
	 * 
	 * @param sourceAnchor
	 *            the new source anchor
	 */
	public void setSourceAnchor(ChopboxAnchor sourceAnchor) {
		this.sourceAnchor = sourceAnchor;
	}

	/**
	 * Sets the target anchor.
	 * 
	 * @param targetAnchor
	 *            the new target anchor
	 */
	public void setTargetAnchor(ChopboxAnchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	/**
	 * New tool.
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
	public void newTool(String parent, String label, String identity,
			boolean connection, String icon) {
		palette.addEntry(parent, label, identity, connection, icon);
	}

	/**
	 * New tool group.
	 * 
	 * @param name
	 *            the name
	 */
	public void newToolGroup(String name) {
		palette.addDrawer(name);
	}

	/**
	 * Gets the pallete.
	 * 
	 * @return the pallete
	 */
	public Palette getPallete() {
		return palette;
	}

	/**
	 * Clear tool palette.
	 */
	public void clearToolPalette() {
		// Palette.clearPalette(getPaletteRoot());
		// this.createPalettePage();
		// this.createPaletteViewerProvider();
	}

	/**
	 * Gets the canvas.
	 * 
	 * @return the canvas
	 */
	public FigureCanvas getCanvas() {
		return canvas;
	}

	/**
	 * Delete.
	 */
	public void delete() {
		// DiagramPlugin diagramManager = DiagramPlugin.getDefault();
		// IWorkbenchPage page = diagramManager.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.closeEditor(this, false);
	}

	/**
	 * Process call.
	 * 
	 * @param message
	 *            the message
	 * @return the value
	 */
	public Value processCall(Message message) {
		return null;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		// setPartName(name);
	}

	/**
	 * Sets the droppable.
	 */
	public void setDroppable() {
		setDroppable(true);
	}

	/**
	 * Sets the droppable.
	 * 
	 * @param enabled
	 *            the new droppable
	 */
	public void setDroppable(boolean enabled) {
		// dropTargetListener.setEnabled(enabled);
	}

	/**
	 * Sets the viewer model.
	 * 
	 * @param newDiagram
	 *            the new viewer model
	 */
	public void setViewerModel(AbstractDiagram newDiagram) {
	}

}
