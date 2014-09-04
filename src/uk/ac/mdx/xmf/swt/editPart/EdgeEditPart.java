package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeShapeFigure;
import uk.ac.mdx.xmf.swt.model.CommandEvent;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.Group;

public class EdgeEditPart extends AbstractConnectionEditPart implements
		PropertyChangeListener {

	EdgeRouter router;
	Point preferredSourceOffset;
	Point preferredTargetOffset;

	CommandEvent parent;
	DiagramView _diagramView;

	IFigure figure;
	EdgeFigure connection;

	@Override
	protected void activateFigure() {

		if (isActive() == false) {
			super.activate();
			parent.addPropertyChangeListener(this);
		}
		// ClientElement element = edge.getParent();
		// if (element instanceof Group) {
		// Group group = (Group) element;
		// if (!group.isTopLevel()) {
		// IFigure layer = group.getConnectionManager().getLayer(
		// element.getIdentity());
		// layer.add(getFigure());
		// } else
		// super.activateFigure();
		// } else
		// super.activateFigure();
	}

	public IFigure createFigure(boolean flag) {
		Edge edge = (Edge) getModel();
		Vector points = edge.getPoints();
		Vector dragPoint = edge.getDragPoints();
		EdgeShapeFigure shape = new EdgeShapeFigure(points, dragPoint, true);

		Rectangle rec = new Rectangle(Main.getInstance().getView()
				.getCanvas().getBounds());
		shape.setBounds(rec);
		shape.setLineWidth(2);
		// shape.setBackgroundColor(ColorConstants.lightGray);
		// shape.setVisible(false);

		// float[] dash = new float[] { 8, 3, 2, 3 };
		// shape.setLineDash(dash);

		return shape;
	}

	@Override
	protected void deactivateFigure() {
		Edge edge = (Edge) getModel();
		ClientElement element = edge.getParent();
		if (element instanceof Group) {
			Group group = (Group) element;

			if (!group.isTopLevel()) {
				group.getConnectionManager().getLayer(element.getIdentity())
						.remove(getFigure());
			} else
				super.deactivateFigure();
		} else
			super.deactivateFigure();
	}

	@Override
	public IFigure createFigure() {
		Edge edge = (Edge) getModel();
		connection = new EdgeFigure();

		// router = new EdgeRouter(getEdgeModel());
		// router.route(connection);
		// connection.setConnectionRouter(router);

		connection.setEdgeType(getEdgeModel().getType());
		connection.setSourceHead(getEdgeModel().getSourceHead());
		connection.setTargetHead(getEdgeModel().getTargetHead());
		connection.setLineStyle(getEdgeModel().getStyle());
		setFigure(connection);
		return connection;
	}

	public IFigure getFigure() {
		return connection;
	}

	public void setRoute(EdgeFigure connection) {
		// refreshRefPoint();
		// router = new EdgeRouter(getEdgeModel());
		// router.route(connection);
		BendpointConnectionRouter router = new BendpointConnectionRouter();
		connection.setConnectionRouter(router);
		connection.setConnectionRouter(router);

	}

	@Override
	public void activate() {
		if (isActive() == false) {
			super.activate();
			getEdgeModel().addPropertyChangeListener(this);
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			getEdgeModel().removePropertyChangeListener(this);
		}
	}

	public void setModel(CommandEvent parent) {
		this.parent = parent;
	}

	@Override
	public CommandEvent getModel() {
		return parent;
	}

	public void setDiagramView(DiagramView view) {
		_diagramView = view;
	}

	@Override
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
		// new EdgeEndPolicy());
		// installEditPolicy(EditPolicy.CONNECTION_ROLE, new EdgePolicy());
		// installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
		// new WaypointEditPolicy());

		// The following role is added to avoid the palette from popping up when
		// the cursor
		// is hovering over an edge. It can be used in future to add context
		// specific hover
		// bars on edges if required.

		// installEditPolicy("Popup", new PopupBarEditPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			refresh();
		if (prop.equals("newSourceTarget"))
			refresh();
		if (prop.equals("waypoints"))
			refreshWaypoints();
		if (prop.equals("refPoint"))
			refreshRefPoint();
		if (prop.equals("newEdgeText"))
			refresh();
		if (prop.equals("newEdgeWidth"))
			refresh();
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("headsChange") || prop.equals("styleChange"))
			refresh();
		if (prop.equals("visibilityChange")) {
			this.getFigure().setVisible(!getEdgeModel().hidden());
			this.getViewer().deselectAll();
		}
		if (prop.equals("typeChange"))
			refresh();
	}

	public RGB getColor() {
		RGB color = ((Edge) getModel()).getColor();
		return color;
		// if (color != null)
		// return color;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.EDGE_COLOR);
	}

	public ConnectionLayerManager getConnectionManager() {
		return getEdgeModel().getConnectionManager();
	}

	public Edge getEdgeModel() {
		return (Edge) getModel();
	}

	public EdgeFigure getEdgeFigure() {
		return (EdgeFigure) this.getFigure();
	}

	@Override
	public void refresh() {
		this.getFigure().setVisible(!getEdgeModel().hidden());
		refreshType();
		refreshHeads();
		refreshWidth();
		refreshStyle();
		refreshWaypoints();
		refreshRefPoint();
		refreshColor();
		// super.refresh();
	}

	public void refreshColor() {
		// getFigure().setForegroundColor(ColorManager.getColor(getColor()));
	}

	public void refreshHeads() {
		getEdgeFigure().setSourceHead(getEdgeModel().getSourceHead());
		getEdgeFigure().setTargetHead(getEdgeModel().getTargetHead());
	}

	public void refreshStyle() {
		getEdgeFigure().setLineStyle(getEdgeModel().getStyle());
	}

	public void refreshType() {
		getEdgeFigure().setEdgeType(getEdgeModel().getType());
	}

	public void refreshWidth() {
		getEdgeFigure().setLineWidth(getEdgeModel().getWidth());
	}

	public EdgeRouter getEdgeRouter() {
		EdgeFigure f = (EdgeFigure) getConnectionFigure();
		return (EdgeRouter) f.getConnectionRouter();
	}

	public int getWaypointCount() {
		EdgeFigure f = (EdgeFigure) getConnectionFigure();
		List l = (List) f.getRoutingConstraint();
		return l.size();
	}

	public Point getWaypointPosition(int index) {
		EdgeFigure f = (EdgeFigure) getConnectionFigure();
		List l = (List) f.getRoutingConstraint();
		AbsoluteBendpoint abp = (AbsoluteBendpoint) l.get(index);
		return abp.getLocation().getCopy();
	}

	protected void refreshWaypoints() {
		List modelConstraint = getEdgeModel().getWaypoints();
		List figureConstraint = new ArrayList();
		// for (int i = 0; i < modelConstraint.size(); i++) {
		// Waypoint wp = (Waypoint) modelConstraint.get(i);
		// AbsoluteBendpoint abp = new AbsoluteBendpoint(wp.getLocation());
		// figureConstraint.add(abp);
		// }

		Edge edge = (Edge) getModel();
		Vector<Point> points = edge.getDragPoints();
		for (int i = 0; i < points.size(); i++) {
			// Waypoint wp = (Waypoint) modelConstraint.get(i);
			Point p = points.get(i);
			AbsoluteBendpoint abp = new AbsoluteBendpoint(p);
			figureConstraint.add(abp);
		}

		// EdgeFigure f = (EdgeFigure) getConnectionFigure();
		EdgeFigure f = (EdgeFigure) getFigure();
		// f.setRefPoint(((Edge) getModel()).getRefPoint());
		f.setRoutingConstraint(figureConstraint);
	}

	protected void refreshRefPoint() {
		EdgeFigure f = (EdgeFigure) getFigure();
		f.setRefPoint(((Edge) getModel()).getRefPoint());
	}

	@Override
	protected List getModelChildren() {
		return getEdgeModel().getContents();
	}

	public void setPreferredSourceOffset(Point preferredSourceOffset) {
		this.preferredSourceOffset = preferredSourceOffset;
	}

	public void setPreferredTargetOffset(Point preferredTargetOffset) {
		this.preferredTargetOffset = preferredTargetOffset;
	}

	@Override
	public void performRequest(Request req) {
		Edge edge = (Edge) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT)
			edge.selected(1);
		else if (request == RequestConstants.REQ_OPEN)
			edge.selected(2);
	}

	public void preferenceUpdate() {
		List children = getChildren();
		refreshColor();
		getEdgeFigure().preferenceUpdate();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
	}

	public String getModelIdentity() {
		Edge e = (Edge) getModel();
		return e.getIdentity();
	}

	public Point getRefPoint() {
		return ((EdgeFigure) this.getFigure()).getRefPoint();
	}
}