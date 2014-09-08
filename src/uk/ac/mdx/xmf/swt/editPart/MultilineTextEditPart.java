package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.diagram.tracker.DisplaySelectionTracker;
import uk.ac.mdx.xmf.swt.figure.MultilineTextFigure;
import uk.ac.mdx.xmf.swt.model.MultilineText;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineTextEditPart.
 */
public class MultilineTextEditPart extends DisplayEditPart {

	/** The manager. */
	static private TextEditManager manager = null;
	
	/** The model. */
	private MultilineText model = null;

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#activate()
	 */
	public void activate() {
		super.activate();
		MultilineText model = (MultilineText) getModel();
		if (model.edit())
			this.editText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		model = (MultilineText) getModel();
		String text = model.getText();
		Point position = model.getLocation();
		Dimension size = model.getSize();
		RGB forecolor = getForeColor();
		RGB backcolor = getFillColor();
		MultilineTextFigure mulilinetext = new MultilineTextFigure(position,
				size, forecolor, backcolor);
		mulilinetext.setText(text);
		setFigure(mulilinetext);
		return mulilinetext;
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public RGB getFillColor() {
		RGB backColor = model.getFillColor();
		if (backColor != null)
			return backColor;
		return new RGB(232, 242, 254);
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// return
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.FILL_COLOR);
	}

	/**
	 * Gets the fore color.
	 *
	 * @return the fore color
	 */
	public RGB getForeColor() {
		RGB foreColor = model.getForegroundColor();
		if (foreColor != null)
			return foreColor;
		return new RGB(0, 242, 254);
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// return
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.UNSELECTED_FONT_COLOR);
	}

	/**
	 * Edits the text.
	 */
	public void editText() {
		if (model.isEditable()) {
			performDirectEdit();
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("editText"))
			editText();
		if (prop.equals("startRender"))
			refresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		if (prop.equals("textChanged"))
			refreshVisuals();
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("visibilityChange")) {
			// this.getFigure().setVisible(!((com.ceteva.diagram.model.Display)getModel()).hidden());
			this.getViewer().deselectAll();
		}
	}

	/**
	 * Refresh color.
	 */
	public void refreshColor() {
		// getFigure().setForegroundColor(ColorManager.getColor(getForeColor()));
		// getFigure().setBackgroundColor(ColorManager.getColor(getFillColor()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		MultilineTextFigure figure = (MultilineTextFigure) getFigure();
		String string = model.getText();
		Point location = model.getLocation();
		Dimension size = model.getSize();

		MultilineTextFigure boxFigure = _diagramView
				.getfigureMulitLineTextLabels().get(model.getIdentity());
		if (boxFigure != null) {

			Point loc = model.getLocation();
			boxFigure.setLocation(loc);
			boxFigure.setText(string);
			boxFigure.setSize(boxFigure.getSize().width,
					boxFigure.getSize().height);
		}
		refreshColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// if (model.isEditable())
		// installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
		// new MultilineTextEditPolicy());
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.DisplayEditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request request) {
		Object type = request.getType();
		if (type == RequestConstants.REQ_DIRECT_EDIT
				|| type == RequestConstants.REQ_OPEN) {
			if (model.isEditable())
				performDirectEdit();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

	/**
	 * Perform direct edit.
	 */
	private void performDirectEdit() {
		// if(manager != null) {
		// manager.cancel();
		// manager = null;
		// }
		// MultilineCellEditorLocator mcel = new
		// MultilineCellEditorLocator((MultilineTextFigure)getFigure());
		// manager = new MultilineEditManager(this,TextCellEditor.class, mcel);
		// manager.show();
	}

	/**
	 * Perform direct edit.
	 *
	 * @param model the model
	 * @param p the p
	 * @param d the d
	 */
	public void performDirectEdit(MultilineText model,
			org.eclipse.swt.graphics.Point p, Dimension d) {
		// if (manager != null) {
		// manager.cancel();
		// manager = null;
		// }
		// TextCellEditorLocator tcel = new TextCellEditorLocator(
		// (Label) createFigure());
		manager = new TextEditManager();

		manager.show(model, p, d);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
	public void preferenceUpdate() {
		refreshColor();
		MultilineTextFigure figure = (MultilineTextFigure) getFigure();
		figure.preferenceUpdate();
		figure.repaint();
	}
}