package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.diagram.tracker.DisplaySelectionTracker;
import uk.ac.mdx.xmf.swt.figure.EllipseFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.Display;
import uk.ac.mdx.xmf.swt.model.Ellipse;

// TODO: Auto-generated Javadoc
/**
 * The Class EllipseEditPart.
 */
public class EllipseEditPart extends DisplayEditPart {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		Ellipse ellipse = (Ellipse) getModel();
		Point location = ellipse.getLocation();
		Dimension size = ellipse.getSize();
		boolean outline = ellipse.getOutline();
		EllipseFigure ef = new EllipseFigure(location, size, outline);
		ef.setLayoutManager(new XYLayout());
		
		setFigure(ef);
		return ef;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public RGB getFillColor() {
		RGB fillColor = ((Ellipse) getModel()).getFillColor();
		if (fillColor != null)
			return fillColor;
		return null;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FILL_COLOR);
	}

	/**
	 * Gets the foreground color.
	 *
	 * @return the foreground color
	 */
	public RGB getForegroundColor() {
		RGB lineColor = ((Ellipse) getModel()).getForegroundColor();
		if (lineColor != null)
			return lineColor;
		return null;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FOREGROUND_COLOR);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return ((Ellipse) getModel()).getContents();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			this.refresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		else if (prop.equals("displayChange"))
			refreshChildren();
		else if (prop.equals("color"))
			refreshColor();
		else if (prop.equals("visibilityChange")) {
			this.getFigure().setVisible(!((Display) getModel()).hidden());
			this.getViewer().deselectAll();
		}
	}

	/**
	 * Refresh color.
	 */
	public void refreshColor() {
		getFigure().setForegroundColor(
				ColorManager.getColor(getForegroundColor()));
		getFigure().setBackgroundColor(ColorManager.getColor(getFillColor()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Ellipse model = (Ellipse) getModel();
		Point loc = ((Ellipse) getModel()).getLocation();
		Dimension size = ((Ellipse) getModel()).getSize();
		boolean fill = model.getfill();
		EllipseFigure f = (EllipseFigure) getFigure();
		f.setFill(fill);
		Rectangle r = new Rectangle(loc, size);
		f.setBounds(r);
//		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
//				getFigure(), r);
		refreshColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
	public void preferenceUpdate() {
		refreshColor();
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
	}
}