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

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeTextEditPart.
 */
public class EdgeTextEditPart extends CommandEventEditPart implements
		MouseListener {

	/** The manager. */
	private TextEditManager manager = null;
	
	/** The model. */
	private EdgeText model = null;

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return getDragTracker();
	}

	/**
	 * Gets the drag tracker.
	 *
	 * @return the drag tracker
	 */
	public DragTracker getDragTracker() {
		return null;
		// return new EdgeTextDragTracker(this, (EdgeEditPart) getParent());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	public IFigure createFigure() {
		model = (EdgeText) getModel();
		String textString = model.getText();
		boolean underline = model.getUnderline();
		EdgeLabelFigure label = new EdgeLabelFigure(textString, underline);
		setFigure(label);
		return label;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
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

	/**
	 * Gets the condense size.
	 *
	 * @return the condense size
	 */
	public int getCondenseSize() {
		return model.getCondenseSize();
	}

	/**
	 * Gets the edge position.
	 *
	 * @return the edge position
	 */
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

	/**
	 * Gets the edge edit part.
	 *
	 * @return the edge edit part
	 */
	public EdgeEditPart getEdgeEditPart() {
		return (EdgeEditPart) getParent();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
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

	/**
	 * Refresh color.
	 */
	public void refreshColor() {
		// getFigure().setForegroundColor(ColorManager.getColor(getColor()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
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

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
	@Override
	public void preferenceUpdate() {
		refreshColor();
		EdgeLabelFigure figure = (EdgeLabelFigure) getFigure();
		figure.preferenceUpdate();
		figure.repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	@Override
	public void performRequest(Request request) {
		// Object type = request.getType();
		// if (type == RequestConstants.REQ_DIRECT_EDIT
		// || type == RequestConstants.REQ_OPEN) {
		// if (model.isEditable())
		// performDirectEdit();
		// }
	}

	/**
	 * Perform direct edit.
	 *
	 * @param model the model
	 * @param p the p
	 * @param d the d
	 */
	public void performDirectEdit(EdgeText model,
			org.eclipse.swt.graphics.Point p, Dimension d) {
		// if (manager == null)
		// manager = new TextEditManager();
		// manager.show();
		manager = new TextEditManager();

		manager.show(model, p, d);
	}

	/**
	 * Gets the edge figure.
	 *
	 * @return the edge figure
	 */
	public EdgeLabelFigure getEdgeFigure() {
		return (EdgeLabelFigure) getFigure();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mousePressed(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		me.consume();
		performRequest(new Request(RequestConstants.REQ_DIRECT_EDIT));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}