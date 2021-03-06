package uk.ac.mdx.xmf.swt.diagram.zoom;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomZoomManager.
 */
public class CustomZoomManager extends AnimatableZoomManager {

	/** The Constant FIT_ALL_FIGURES. */
	public static final String FIT_ALL_FIGURES = "FIT_ALL_FIGURES";

	/**
	 * Instantiates a new custom zoom manager.
	 *
	 * @param pane the pane
	 * @param viewport the viewport
	 */
	public CustomZoomManager(ScalableFigure pane, Viewport viewport) {
		super(pane, viewport);
	}

	/**
	 * Gets the fit x figure viewport.
	 *
	 * @param fig the fig
	 * @return the fit x figure viewport
	 */
	private Rectangle getFitXFigureViewport(IFigure fig) {
		int topLeft_x = 1000;
		int topLeft_y = 1000;
		int bottomRight_x = 0;
		int bottomRight_y = 0;
		List children = fig.getChildren();

		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			Rectangle bounds = child.getBounds();
			topLeft_x = Math.min(topLeft_x, bounds.x);
			topLeft_y = Math.min(topLeft_y, bounds.y);
			bottomRight_x = Math.max(bottomRight_x, bounds.x + bounds.width);
			bottomRight_y = Math.max(bottomRight_y, bounds.y + bounds.height);
		}
		int width = bottomRight_x - topLeft_x;
		int height = bottomRight_y - topLeft_y;
		Rectangle r = new Rectangle(topLeft_x, topLeft_y, width, height);
		return r;
	}

	/**
	 * Gets the fit x figure level.
	 *
	 * @param which the which
	 * @param fig the fig
	 * @param vp the vp
	 * @return the fit x figure level
	 */
	private double getFitXFigureLevel(int which, IFigure fig, Rectangle vp) {
		Dimension available = getViewport().getClientArea().getSize();
		Dimension desired = vp.getSize();

		desired.width -= fig.getInsets().getWidth();
		desired.height -= fig.getInsets().getHeight();

		while (fig != getViewport()) {
			available.width -= fig.getInsets().getWidth();
			available.height -= fig.getInsets().getHeight();
			fig = fig.getParent();
		}

		double scaleX = Math.min(available.width * getZoom() / desired.width,
				getMaxZoom());
		double scaleY = Math.min(available.height * getZoom() / desired.height,
				getMaxZoom());

		if (which == 0)
			return scaleX;
		if (which == 1)
			return scaleY;
		return Math.min(scaleX, scaleY);
	}

	/**
	 * Gets the fit figues zoom level.
	 *
	 * @param fig the fig
	 * @param vp the vp
	 * @return the fit figues zoom level
	 */
	protected double getFitFiguesZoomLevel(IFigure fig, Rectangle vp) {
		return getFitXFigureLevel(2, fig, vp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ZoomManager#setZoomAsText(java.lang.String)
	 */
	public void setZoomAsText(String zoomString) {
		if (zoomString.equalsIgnoreCase(FIT_ALL_FIGURES)) {
			IFigure fig = getScalableFigure();
			Rectangle vp = getFitXFigureViewport(fig);
			primSetZoom(getFitFiguesZoomLevel(fig, vp));
			setViewLocation(vp.getLocation());
		} else
			super.setZoomAsText(zoomString);
	}
}
