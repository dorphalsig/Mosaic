package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.mdx.xmf.swt.diagram.preferences.IPreferenceConstants;
import uk.ac.mdx.xmf.swt.diagram.zoom.CustomZoomManager;
import uk.ac.mdx.xmf.swt.figure.ZoomableFigure;
import uk.ac.mdx.xmf.swt.misc.DiagramPlugin;

public abstract class ZoomableGraphicalEditPart extends DisplayEditPart {

	CustomZoomManager zoomManager;

	public ZoomManager getZoomManager() {
		if (zoomManager == null) {
			ZoomableFigure figure = (ZoomableFigure) getFigure();
			zoomManager = new CustomZoomManager(
					(ScalableFigure) figure.getFigure(),
					((Viewport) figure.getViewPort()));
			zoomManager.setZoomAnimationStyle(ZoomManager.ANIMATE_ZOOM_IN_OUT);
			refreshEnableZoomAnimation(zoomManager);
		}
		return zoomManager;
	}

	public void preferenceUpdate() {
		refreshEnableZoomAnimation(zoomManager);
	}

	private void refreshEnableZoomAnimation(ZoomManager zoomMangr) {
		IPreferenceStore preferenceStore = DiagramPlugin.getDefault()
				.getPreferenceStore();
		boolean animatedZoom = preferenceStore
				.getBoolean(IPreferenceConstants.ANIMATEZOOM);
		zoomMangr
				.setZoomAnimationStyle(animatedZoom ? ZoomManager.ANIMATE_ZOOM_IN_OUT
						: ZoomManager.ANIMATE_NEVER);
	}

	protected void register() {
		super.register();
		getViewer().setProperty(ZoomManager.class.toString(), getZoomManager());
	}

	protected void unregister() {
		super.unregister();
		getViewer().setProperty(ZoomManager.class.toString(), null);
	}
}
