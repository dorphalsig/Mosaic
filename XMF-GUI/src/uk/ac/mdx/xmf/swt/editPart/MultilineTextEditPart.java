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
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.MultilineText;

public class MultilineTextEditPart extends DisplayEditPart {

	static private MultilineEditManager manager = null;
	private MultilineText model = null;

	public void activate() {
		super.activate();
		MultilineText model = (MultilineText) getModel();
		if (model.edit())
			this.editText();
	}

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

	public void editText() {
		if (model.isEditable()) {
			performDirectEdit();
		}
	}

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

	public void refreshColor() {
		getFigure().setForegroundColor(ColorManager.getColor(getForeColor()));
		getFigure().setBackgroundColor(ColorManager.getColor(getFillColor()));
	}

	protected void refreshVisuals() {
		MultilineTextFigure figure = (MultilineTextFigure) getFigure();
		String string = model.getText();
		Point location = model.getLocation();
		Dimension size = model.getSize();

		MultilineTextFigure boxFigure = _diagramView
				.getfigureMulitLineTextLabels().get(model.getParenIdentity());
		if (boxFigure != null) {

			Point loc = model.getLocation();
			boxFigure.setLocation(loc);
			boxFigure.setText(string);
			boxFigure.setSize(boxFigure.getSize().width,
					boxFigure.getSize().height);
		}
		refreshColor();
	}

	protected void createEditPolicies() {
		// if (model.isEditable())
		// installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
		// new MultilineTextEditPolicy());
	}

	public void performRequest(Request request) {
		Object type = request.getType();
		if (type == RequestConstants.REQ_DIRECT_EDIT
				|| type == RequestConstants.REQ_OPEN) {
			if (model.isEditable())
				performDirectEdit();
		}
	}

	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

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

	public void preferenceUpdate() {
		refreshColor();
		MultilineTextFigure figure = (MultilineTextFigure) getFigure();
		figure.preferenceUpdate();
		figure.repaint();
	}
}