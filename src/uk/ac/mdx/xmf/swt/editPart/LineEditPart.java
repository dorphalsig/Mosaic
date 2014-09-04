package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.figure.LineFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.Line;

public class LineEditPart extends DisplayEditPart {

	Line model;
	LineFigure line;

	protected IFigure createFigure() {
		model = (Line) getModel();
		Point start = model.getStart();
		Point end = model.getEnd();
		line = new LineFigure(start, end);
		return line;
	}

	protected void createEditPolicies() {
	}

	public RGB getColor() {
		RGB lineColor = ((Line) getModel()).getColor();
		if (lineColor != null)
			return lineColor;
		return new RGB(12, 255, 255);
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.EDGE_COLOR);
	}

	public boolean isSelectable() {
		return false;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			this.refresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("visibilityChange")) {
			// this.getFigure().setVisible(
			// !((com.ceteva.diagram.model.Display) getModel()).hidden());
			this.getViewer().deselectAll();
		}
	}

	protected void refreshVisuals() {
		Point start = ((Line) getModel()).getStart();
		Point end = ((Line) getModel()).getEnd();
		line.setStart(start);
		line.setEnd(end);
		refreshColor();
	}

	public void refreshColor() {
		getFigure().setForegroundColor(ColorManager.getColor(getColor()));
	}
}