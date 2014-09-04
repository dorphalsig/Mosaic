package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeLabelFigure;
import uk.ac.mdx.xmf.swt.model.EdgeText;

public class EdgeTextEditPart extends CommandEventEditPart implements
		MouseListener {

	private TextEditManager manager = null;
	private EdgeText model = null;

	@Override
	public DragTracker getDragTracker(Request request) {
		return getDragTracker();
	}

	public DragTracker getDragTracker() {
		return null;
		// return new EdgeTextDragTracker(this, (EdgeEditPart) getParent());
	}

	@Override
	public IFigure createFigure() {
		model = (EdgeText) getModel();
		String textString = model.getText();
		boolean underline = model.getUnderline();
		EdgeLabelFigure label = new EdgeLabelFigure(textString, underline);
		setFigure(label);
		return label;
	}

	public RGB getColor() {
		RGB color = ((EdgeText) getModel()).getColor();
		return color;
		// if (color != null)
		// return color;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.UNSELECTED_FONT_COLOR);
	}

	public int getCondenseSize() {
		return model.getCondenseSize();
	}

	public Point getEdgePosition() {
		EdgeText model = (EdgeText) getModel();
		EdgeFigure edgeFigure = _diagramView.getEdgeFigure().get(
				model.getParenIdentity());
		String position = model.getPosition();
		if (position.equals("start"))
			return edgeFigure.getStart();
		else if (position.equals("end"))
			return edgeFigure.getEnd();
		else
			return edgeFigure.getPoints().getMidpoint();
	}

	public EdgeEditPart getEdgeEditPart() {
		return (EdgeEditPart) getParent();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			refresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		if (prop.equals("textChanged"))
			refreshVisuals();
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("visibilityChange")) {
			refreshVisuals();
			this.getViewer().deselectAll();
		}
	}

	public void refreshColor() {
		// getFigure().setForegroundColor(ColorManager.getColor(getColor()));
	}

	@Override
	protected void refreshVisuals() {
		EdgeLabelFigure figure = (EdgeLabelFigure) getFigure();
		String text = model.getText();
		figure.setText(text);
		figure.setFont(model.getFont());
		figure.setVisible(!model.hidden());
		figure.setSize(100, 30);
		Point offset = ((EdgeText) getModel()).getLocation();
		figure.setLocation(offset);
		EdgeEditPart parent = Main.getInstance().getView().getEdgeParts()
				.get(model.parent.getIdentity());
		EdgeTextConstraint constraint = new EdgeTextConstraint(this,
				model.getText(), getFigure(), (EdgeEditPart) parent,
				model.getPosition(), offset);
		parent.setLayoutConstraint(this, getFigure(), constraint);
		refreshColor();
	}

	@Override
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
		// new EdgeTextMoveEditPolicy());
		// installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
		// new EdgeTextSelectionPolicy());
		// if (model.isEditable())
		// installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
		// new EdgeTextEditPolicy());
	}

	@Override
	public void preferenceUpdate() {
		refreshColor();
		EdgeLabelFigure figure = (EdgeLabelFigure) getFigure();
		figure.preferenceUpdate();
		figure.repaint();
	}

	@Override
	public void performRequest(Request request) {
		// Object type = request.getType();
		// if (type == RequestConstants.REQ_DIRECT_EDIT
		// || type == RequestConstants.REQ_OPEN) {
		// if (model.isEditable())
		// performDirectEdit();
		// }
	}

	public void performDirectEdit(EdgeText model,
			org.eclipse.swt.graphics.Point p, Dimension d) {
		// if (manager == null)
		// manager = new TextEditManager();
		// manager.show();
		manager = new TextEditManager();

		manager.show(model, p, d);
	}

	public EdgeLabelFigure getEdgeFigure() {
		return (EdgeLabelFigure) getFigure();
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent me) {
		me.consume();
		performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT));
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}