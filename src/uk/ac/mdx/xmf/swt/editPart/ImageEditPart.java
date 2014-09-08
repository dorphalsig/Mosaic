package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;

import uk.ac.mdx.xmf.swt.diagram.tracker.DisplaySelectionTracker;
import uk.ac.mdx.xmf.swt.figure.ImageFigure;
import uk.ac.mdx.xmf.swt.model.Display;
import uk.ac.mdx.xmf.swt.model.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageEditPart.
 */
public class ImageEditPart extends DisplayEditPart {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	public IFigure createFigure() {
		Image image = (Image) getModel();
		org.eclipse.swt.graphics.Image i = image.getImage();
		Point location = image.getLocation();
		Dimension size = image.getSize();
		ImageFigure imageFigure = new ImageFigure(i);
		imageFigure.setLocation(location);
		imageFigure.setSize(size);
		return imageFigure;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
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
		if (prop.equals("imageChanged"))
			imageChanged();
		else if (prop.equals("visibilityChange"))
			this.getFigure().setVisible(!((Display) getModel()).hidden());
		this.getViewer().deselectAll();
	}

	/**
	 * Image changed.
	 */
	public void imageChanged() {
		ImageFigure figure = (ImageFigure) getFigure();
		Image image = (Image) getModel();
		org.eclipse.swt.graphics.Image i = image.getImage();
		figure.setImage(i);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	public boolean isSelectable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Image image = (Image) getModel();
		Point location = image.getLocation();
		Dimension size = image.getSize();
		Rectangle r = new Rectangle(location, size);

		// this is a hack

		ImageFigure figure = (ImageFigure) getFigure();
		figure.setSize(size);

		// end of hack

		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), r);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	}
}
