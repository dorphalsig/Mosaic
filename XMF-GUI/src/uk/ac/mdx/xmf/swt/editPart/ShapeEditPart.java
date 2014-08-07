package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.figure.ShapeFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.Shape;

public class ShapeEditPart extends DisplayEditPart {

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

	public boolean isSelectable() {
		return false;
	}

	protected void createEditPolicies() {
	}

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

	public void refreshColor() {
		getFigure().setForegroundColor(
				ColorManager.getColor(getForegroundColor()));
		getFigure().setBackgroundColor(ColorManager.getColor(getFillColor()));
	}

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

	public void preferenceUpdate() {
		refreshColor();
	}
}