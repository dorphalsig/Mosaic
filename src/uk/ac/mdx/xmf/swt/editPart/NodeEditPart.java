package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.diagram.tracker.NodeSelectionTracker;
import uk.ac.mdx.xmf.swt.figure.NodeFigure;
import uk.ac.mdx.xmf.swt.figure.NodeShapeFigure;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeEditPart.
 */
public class NodeEditPart extends CommandEventEditPart implements
		org.eclipse.gef.NodeEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		// this.getViewer().addDragSourceListener(
		// new DragRequest(getViewer(), TextTransfer.getInstance(), this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	public IFigure createFigure() {
		Node node = (Node) getModel();
		NodeFigure figure = new NodeFigure(node.getLocation(), node.getSize(),
				node.getPorts());
		figure.setLayoutManager(new XYLayout());

		// Rectangle rect = new Rectangle(10, 10, 200,
		// 200);
		// float[] dash = { 5F, 5F };
		// Stroke dashedStroke = new BasicStroke(2F, BasicStroke.CAP_SQUARE,
		// BasicStroke.JOIN_MITER, 3F, dash, 0F);
		//
		// dashedStroke.createStrokedShape(rect);

		setFigure(figure);

		return figure;
	}

	/**
	 * Creates the figure.
	 * 
	 * @param flag
	 *            the flag
	 * @param rec
	 *            the rec
	 * @return the i figure
	 */
	public IFigure createFigure(boolean flag, Rectangle rec) {
		Node node = (Node) getModel();
		NodeShapeFigure shape = new NodeShapeFigure(true);

		Point p = new Point();
		p.x = node.getLocation().x - 2;
		p.y = node.getLocation().y - 2;

		Rectangle shapeRec = new Rectangle();
		shapeRec.setLocation(p);
		Dimension d = new Dimension();
		d.width = node.getSize().width + 4;
		d.height = node.getSize().height + 4;
		shapeRec.setSize(d);
		shape.reSetPoints(p, d);
		shape.setBounds(shapeRec);
		shape.setLineWidth(5);
		// shape.setVisible(false);

		// float[] dash = new float[] { 8, 3, 2, 3 };
		// shape.setLineDash(dash);

		return shape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
		// new NodeConnectionPolicy());
		// installEditPolicy(EditPolicy.COMPONENT_ROLE, new NodeEditPolicy());

		// The following role is added to avoid the palette from popping up when
		// the cursor
		// is hovering over a node. It can be used in future to add context
		// specific hover
		// bars on nodes if required.

		// installEditPolicy("Popup", new PopupBarEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return ((Node) getModel()).isSelectable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {
		return ((Node) getModel()).getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new NodeSelectionTracker(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#refresh()
	 */
	@Override
	public void refresh() {
		resetFixedPorts();
		super.refresh();
	}

	/**
	 * Start render refresh.
	 */
	public void startRenderRefresh() {
		// this.refresh();
		// List sconnections = getSourceConnections();
		// List tconnections = getTargetConnections();
		// for (int i = 0; i < sconnections.size(); i++) {
		// ConnectionEditPart cep = (ConnectionEditPart) sconnections.get(i);
		// cep.refresh();
		// }
		// for (int i = 0; i < tconnections.size(); i++) {
		// ConnectionEditPart cep = (ConnectionEditPart) tconnections.get(i);
		// cep.refresh();
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			startRenderRefresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		else if (prop.equals("color"))
			refreshColor();
		else if (prop.equals("displayChange"))
			refreshChildren();
		else if (prop.equals("refreshPorts"))
			resetFixedPorts();
		else if (prop.equals("targetEdges"))
			refreshTargetConnections();
		else if (prop.equals("sourceEdges"))
			refreshSourceConnections();
		else if (prop.equals("visibilityChange")) {
			this.getFigure().setVisible(!getNodeModel().hidden());
			this.getViewer().deselectAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshSourceConnections
	 * ()
	 */
	@Override
	public void refreshSourceConnections() {
		Node source = (Node) getModel();
		_diagramView = Main.getInstance().getView();
		String identity = source.getIdentity();
		// int id = Integer.valueOf(identity) - 1;
		// identity = String.valueOf(id);

		Figure sourceFigure = _diagramView.getFigureNodes().get(identity);
		ChopboxAnchor sourceAnchor = new ChopboxAnchor(sourceFigure);

		if (sourceAnchor != null)
			_diagramView.setSourceAnchor(sourceAnchor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshTargetConnections
	 * ()
	 */
	@Override
	public void refreshTargetConnections() {
		Node source = (Node) getModel();
		_diagramView = Main.getInstance().getView();
		String identity = source.getIdentity();
		// int id = Integer.valueOf(identity) - 1;
		// identity = String.valueOf(id);

		Figure targetFigure = _diagramView.getFigureNodes().get(identity);
		// EdgeFigure edgeFigure = _diagramView.getEdgeFigure().get(
		// source.getIdentity());
		ChopboxAnchor targetAnchor = new ChopboxAnchor(targetFigure);
		if (targetAnchor != null)
			_diagramView.setTargetAnchor(targetAnchor); // change to target
														// method
	}

	/**
	 * Gets the fill color.
	 * 
	 * @return the fill color
	 */
	public RGB getFillColor() {
		return null;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FILL_COLOR);
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public Point getLocation() {
		return ((Node) getModel()).getLocation();
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public Dimension getSize() {
		return ((Node) getModel()).getSize();
	}

	/**
	 * Refresh color.
	 */
	public void refreshColor() {
		this.getFigure().setBackgroundColor(ColorConstants.blue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		Point loc = getLocation();
		Dimension size = getSize();
		Rectangle r = new Rectangle(loc, size);
		// ((GraphicalEditPart) getParent()).setLayoutConstraint(this,
		// getFigure(), r);
		getFigure().setLocation(loc);
		getFigure().setSize(size);
		// getFigure().setVisible(!getNodeModel().hidden());
		refreshColor();
	}

	/**
	 * Reset fixed ports.
	 */
	public void resetFixedPorts() {
		getNodeFigure().resetFixedPorts(getNodeModel().getPorts());
	}

	/**
	 * Gets the node figure.
	 * 
	 * @return the node figure
	 */
	public NodeFigure getNodeFigure() {
		return (NodeFigure) getFigure();
	}

	/**
	 * Gets the node model.
	 * 
	 * @return the node model
	 */
	public Node getNodeModel() {
		return (Node) getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	@Override
	protected List getModelSourceConnections() {
		return getNodeModel().getSourceEdges();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	@Override
	protected List getModelTargetConnections() {
		return getNodeModel().getTargetEdges();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		Edge edge = (Edge) connEditPart.getModel();
		return getNodeFigure().getConnectionAnchor(edge.getSourcePort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return ((NodeFigure) getFigure()).getAnchor(pt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		Edge edge = (Edge) connEditPart.getModel();
		return getNodeFigure().getConnectionAnchor(edge.getTargetPort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return ((NodeFigure) getFigure()).getAnchor(pt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse
	 * .gef.Request)
	 */
	@Override
	public void performRequest(Request req) {
		Node node = (Node) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT)
			node.selected(1);
		else if (request == RequestConstants.REQ_OPEN)
			node.selected(2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
	@Override
	public void preferenceUpdate() {
		refreshColor();
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
		List sourceEdges = this.getSourceConnections();
		for (int i = 0; i < sourceEdges.size(); i++) {
			EdgeEditPart edge = (EdgeEditPart) sourceEdges.get(i);
			edge.preferenceUpdate();
		}
	}
}