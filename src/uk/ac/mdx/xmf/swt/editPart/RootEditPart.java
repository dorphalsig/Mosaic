package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;

import uk.ac.mdx.xmf.swt.diagram.zoom.CustomZoomManager;
import uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart;

// TODO: Auto-generated Javadoc
/**
 * The Class RootEditPart.
 */
public class RootEditPart extends ScalableFreeformRootEditPart implements
		ZoomableEditPart {

	/** The zoom manager. */
	private CustomZoomManager zoomManager;

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.FreeformGraphicalRootEditPart#getPrintableLayers()
	 */
	public LayeredPane getPrintableLayers() {
		return super.getPrintableLayers();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ScalableFreeformRootEditPart#getZoomManager()
	 */
	public ZoomManager getZoomManager() {
		if (zoomManager == null) {
			zoomManager = new CustomZoomManager(
					(ScalableFigure) getScaledLayers(),
					((Viewport) getFigure()));
			refreshEnableZoomAnimation(zoomManager);
		}
		return zoomManager;
	}

	/**
	 * Preference update.
	 */
	public void preferenceUpdate() {
		refreshEnableZoomAnimation(zoomManager);
	}

	/**
	 * Refresh enable zoom animation.
	 *
	 * @param zoomMangr the zoom mangr
	 */
	private void refreshEnableZoomAnimation(ZoomManager zoomMangr) {
		// IPreferenceStore preferenceStore =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// boolean animatedZoom =
		// preferenceStore.getBoolean(IPreferenceConstants.ANIMATEZOOM);
		// zoomMangr.setZoomAnimationStyle(animatedZoom ?
		// ZoomManager.ANIMATE_ZOOM_IN_OUT : ZoomManager.ANIMATE_NEVER);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomTo(double, org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomTo(double zoom, Point center) {
		zoomManager.zoomTo(zoom, center);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomTo(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void zoomTo(Rectangle rect) {
		zoomManager.zoomTo(rect);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomIn()
	 */
	public void zoomIn() {
		zoomManager.zoomIn();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomIn(org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomIn(Point center) {
		zoomManager.zoomTo(zoomManager.getNextZoomLevel(), center);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomOut()
	 */
	public void zoomOut() {
		zoomManager.zoomOut();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.diagram.zoom.ZoomableEditPart#zoomOut(org.eclipse.draw2d.geometry.Point)
	 */
	public void zoomOut(Point center) {
		zoomManager.zoomTo(zoomManager.getPreviousZoomLevel(), center);
	}

}
