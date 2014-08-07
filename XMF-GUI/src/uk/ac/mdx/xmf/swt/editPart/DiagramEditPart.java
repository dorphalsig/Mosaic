package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.diagram.tracker.SelectionTracker;
import uk.ac.mdx.xmf.swt.diagram.zoom.AnimatableZoomManager;
import uk.ac.mdx.xmf.swt.figure.DiagramFigure;
import uk.ac.mdx.xmf.swt.model.AbstractDiagram;

public class DiagramEditPart extends CommandEventEditPart {

	@Override
	public void activate() {
		super.activate();
		// AnimatableZoomManager azm = (AnimatableZoomManager) getZoomManager();
		// azm.addZoomListener(this);
		// getViewer().addDropTargetListener(new
		// DropTargetListener(getViewer(),TextTransfer.getInstance()));;
	}

	@Override
	public void deactivate() {
		super.deactivate();
		// AnimatableZoomManager azm = (AnimatableZoomManager) getZoomManager();
		// azm.removeZoomListener(this);
	}

	@Override
	protected IFigure createFigure() {
		DiagramFigure f = new DiagramFigure();
		f.setLayoutManager(new FreeformLayout());
		f.setOpaque(true);

		// Set in case this figure is being redrawn

		AbstractDiagram diagram = (AbstractDiagram) getModel();
		diagram.setQueuedZoom(true);
		return f;
	}

	@Override
	public Command getCommand(Request request) {
		return super.getCommand(request);
	}

	@Override
	protected List getModelChildren() {
		return ((AbstractDiagram) getModel()).getContents();
	}

	@Override
	public DragTracker getDragTracker(Request req) {
		return new SelectionTracker();
	}

	@Override
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutPolicy());
		// installEditPolicy("PopupPalette", new PopupPalette());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			this.refresh();
		if (prop.equals("newNode") || prop.equals("newEdge"))
			refreshChildren();
		if (prop.equals("preferenceChange"))
			preferenceUpdate();
		if (prop.equals("delete"))
			refreshChildren();
		if (prop.equals("backgroundColor"))
			refreshBackgroundColor();
		if (prop.equals("exportImage")) {
			AbstractDiagram diagram = (AbstractDiagram) getModel();
			String filename = diagram.getFilename();
			String type = diagram.getExportType();
			// ImageProducer.createImage(filename,
			// getLayer(LayerConstants.PRINTABLE_LAYERS), type);
		}
		if (prop.equals("copyToClipboard"))
			// ImageProducer
			// .copyToClipboard(getLayer(LayerConstants.PRINTABLE_LAYERS));
			if (prop.equals("zoom"))
				zoom();
	}

	public void refreshChildren() {

	}

	@Override
	public void preferenceUpdate() {
		DiagramFigure figure = (DiagramFigure) getFigure();
		figure.preferenceUpdate();
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
		figure.repaint();
	}

	@Override
	public void performRequest(Request req) {
		AbstractDiagram diagram = (AbstractDiagram) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT)
			diagram.selected(1);
		else if (request == RequestConstants.REQ_OPEN)
			diagram.selected(2);
	}

	public RGB getBackgroundColor() {
		RGB color = ((AbstractDiagram) getModel()).getColor();
		if (color != null)
			return color;
		return null;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.DIAGRAM_BACKGROUND_COLOR);
	}

	public ZoomManager getZoomManager() {
		RootEditPart rep = (RootEditPart) this.getParent();
		return null;
		// return rep.getZoomManager();
	}

	@Override
	public void refresh() {
		refreshBackgroundColor();
		refreshZoom();
		super.refresh();
	}

	public void refreshBackgroundColor() {
		// getFigure().setBackgroundColor(
		// ColorManager.getColor(getBackgroundColor()));
	}

	public void refreshZoom() {
		AbstractDiagram diagram = (AbstractDiagram) getModel();
		if (diagram.getQueuedZoom()) {
			diagram.setQueuedZoom(false);
			zoom();
		}
	}

	public void zoom() {
		AbstractDiagram diagram = (AbstractDiagram) getModel();
		// getZoomManager().setZoomAsText(diagram.getZoom());
	}

	public void zoomChanged(double zoom) {
	}

	public void animatedZoomEnded() {
		AnimatableZoomManager azm = (AnimatableZoomManager) getZoomManager();
		double zoom = azm.getZoom();
		azm.getZoom();
		AbstractDiagram diagram = (AbstractDiagram) getModel();
		diagram.zoomChanged(new Double(zoom * 100).intValue());
	}

	public void animatedZoomStarted() {
		// AbstractDiagram diagram = (AbstractDiagram)getModel();
	}

}