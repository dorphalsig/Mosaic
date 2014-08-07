package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Vector;

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

import uk.ac.mdx.xmf.swt.demo.GUIDemo;
import uk.ac.mdx.xmf.swt.diagram.tracker.NodeSelectionTracker;
import uk.ac.mdx.xmf.swt.figure.NodeFigure;
import uk.ac.mdx.xmf.swt.figure.NodeShapeFigure;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.Node;

public class NodeEditPart extends CommandEventEditPart implements
		org.eclipse.gef.NodeEditPart {

	@Override
	public void activate() {
		super.activate();
		// this.getViewer().addDragSourceListener(
		// new DragRequest(getViewer(), TextTransfer.getInstance(), this));
	}

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

	public IFigure createFigure(boolean flag, Rectangle rec) {
		Node node = (Node) getModel();
		Vector points = node.getPoints();
		NodeShapeFigure shape = new NodeShapeFigure(points, true);
		shape.setBounds(rec);
		shape.setLineWidth(5);
		// shape.setVisible(false);

		// float[] dash = new float[] { 8, 3, 2, 3 };
		// shape.setLineDash(dash);

		return shape;
	}

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

	@Override
	public boolean isSelectable() {
		return ((Node) getModel()).isSelectable();
	}

	@Override
	protected List getModelChildren() {
		return ((Node) getModel()).getContents();
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		return new NodeSelectionTracker(this);
	}

	@Override
	public void refresh() {
		resetFixedPorts();
		super.refresh();
	}

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

	@Override
	public void refreshSourceConnections() {
		Node source = (Node) getModel();
		_diagramView = GUIDemo.getInstance().getView();
		String identity = source.getIdentity();
		// int id = Integer.valueOf(identity) - 1;
		// identity = String.valueOf(id);

		Figure sourceFigure = _diagramView.getFigureNodes().get(identity);
		ChopboxAnchor sourceAnchor = new ChopboxAnchor(sourceFigure);

		if (sourceAnchor != null)
			_diagramView.setSourceAnchor(sourceAnchor);
	}

	@Override
	public void refreshTargetConnections() {
		Node source = (Node) getModel();
		_diagramView = GUIDemo.getInstance().getView();
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

	public RGB getFillColor() {
		return null;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FILL_COLOR);
	}

	public Point getLocation() {
		return ((Node) getModel()).getLocation();
	}

	public Dimension getSize() {
		return ((Node) getModel()).getSize();
	}

	public void refreshColor() {
		this.getFigure().setBackgroundColor(ColorConstants.blue);
	}

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

	public void resetFixedPorts() {
		getNodeFigure().resetFixedPorts(getNodeModel().getPorts());
	}

	public NodeFigure getNodeFigure() {
		return (NodeFigure) getFigure();
	}

	public Node getNodeModel() {
		return (Node) getModel();
	}

	@Override
	protected List getModelSourceConnections() {
		return getNodeModel().getSourceEdges();
	}

	@Override
	protected List getModelTargetConnections() {
		return getNodeModel().getTargetEdges();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		Edge edge = (Edge) connEditPart.getModel();
		return getNodeFigure().getConnectionAnchor(edge.getSourcePort());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return ((NodeFigure) getFigure()).getAnchor(pt);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		Edge edge = (Edge) connEditPart.getModel();
		return getNodeFigure().getConnectionAnchor(edge.getTargetPort());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return ((NodeFigure) getFigure()).getAnchor(pt);
	}

	@Override
	public void performRequest(Request req) {
		Node node = (Node) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT)
			node.selected(1);
		else if (request == RequestConstants.REQ_OPEN)
			node.selected(2);
	}

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