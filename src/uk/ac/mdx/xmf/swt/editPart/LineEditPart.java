package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.figure.LineFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.model.Line;

// TODO: Auto-generated Javadoc
/**
 * The Class LineEditPart.
 */
public class LineEditPart extends DisplayEditPart {

	/** The model. */
	Line model;
	
	/** The line. */
	LineFigure line;

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		model = (Line) getModel();
		Point start = model.getStart();
		Point end = model.getEnd();
		line = new LineFigure(start, end);
		return line;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return false;
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
		if (prop.equals("color"))
			refreshColor();
		if (prop.equals("visibilityChange")) {
			// this.getFigure().setVisible(
			// !((com.ceteva.diagram.model.Display) getModel()).hidden());
			this.getViewer().deselectAll();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Point start = ((Line) getModel()).getStart();
		Point end = ((Line) getModel()).getEnd();
		line.setStart(start);
		line.setEnd(end);
		refreshColor();
	}

	/**
	 * Refresh color.
	 */
	public void refreshColor() {
		getFigure().setForegroundColor(ColorManager.getColor(getColor()));
	}
}