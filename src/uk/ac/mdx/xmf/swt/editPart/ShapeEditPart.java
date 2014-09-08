package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.figure.ShapeFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.Shape;

// TODO: Auto-generated Javadoc
/**
 * The Class ShapeEditPart.
 */
public class ShapeEditPart extends DisplayEditPart {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		Shape model = (Shape) getModel();
		boolean outline = model.outline();
		Vector points = model.getPoints();
		ShapeFigure figure = new ShapeFigure(points, outline);
		figure.setBounds(new Rectangle(model.x(), model.y(), model.width(),
				model.height()));

		setFigure(figure);
		return figure;
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public RGB getFillColor() {
		RGB fillColor = ((Shape) getModel()).getFillColor();
		if (fillColor != null)
			return fillColor;
		fillColor = new RGB(157, 157, 255);
		return fillColor;
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FILL_COLOR);
	}

	/**
	 * Gets the foreground color.
	 *
	 * @return the foreground color
	 */
	public RGB getForegroundColor() {
		RGB lineColor = ((Shape) getModel()).getForegroundColor();
		if (lineColor != null)
			return lineColor;
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FOREGROUND_COLOR);
		lineColor = new RGB(255, 255, 255);
		return lineColor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return false;
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
		if (prop.equals("redrawShape"))
			refreshVisuals();
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("locationSize"))
			refreshVisuals();
		if (prop.equals("visibilityChange")) {
			// this.getFigure().setVisible(!(getModel()).hidden());
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
	public void refreshVisuals() {
		Shape model = (Shape) getModel();
		ShapeFigure figure = (ShapeFigure) getFigure();
		boolean outline = model.outline();
		Vector points = model.getPoints();
		figure.setBounds(new Rectangle(model.x(), model.y(), model.width(),
				model.height()));
		figure.refresh(points, outline);
		refreshColor();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
	public void preferenceUpdate() {
		refreshColor();
	}
}