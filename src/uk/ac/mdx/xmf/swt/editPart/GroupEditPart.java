package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.mdx.xmf.swt.diagram.tracker.DisplaySelectionTracker;
import uk.ac.mdx.xmf.swt.figure.GroupFigure;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import uk.ac.mdx.xmf.swt.misc.ImageProducer;
import uk.ac.mdx.xmf.swt.model.AbstractDiagram;
import uk.ac.mdx.xmf.swt.model.Group;

// TODO: Auto-generated Javadoc
/**
 * The Class GroupEditPart.
 */
public class GroupEditPart extends ZoomableGraphicalEditPart {

	/**
	 * Instantiates a new group edit part.
	 */
	public GroupEditPart() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Group group = getGroupModel();
		Point location = group.getLocation();
		Dimension size = group.getSize();
		ConnectionLayerManager connectionManager = group.getConnectionManager();
		GroupFigure groupFigure = new GroupFigure(group.getIdentity(),
				location, size, connectionManager);

		// Set in case this figure is being redrawn
		group.setQueuedZoom(true);
		return groupFigure;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return getGroupModel().getContents();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// installEditPolicy(EditPolicy.LAYOUT_ROLE, new LayoutPolicy());
		// installEditPolicy("PopupPalette", new PopupPalette());
	}

	/**
	 * Gets the group model.
	 *
	 * @return the group model
	 */
	public Group getGroupModel() {
		return (Group) getModel();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.ZoomableGraphicalEditPart#getZoomManager()
	 */
	public ZoomManager getZoomManager() {
		// nested diagrams do not animate their zoom since this is expensive
		// when redrawing a diagram with many sub diagrams.
		return null;
		// if(zoomManager == null) {
		// ZoomableFigure figure = (ZoomableFigure)getFigure();
		// zoomManager = new
		// CustomZoomManager((ScalableFigure)figure.getFigure(),((Viewport)figure.getViewPort()));
		// zoomManager.setZoomAnimationStyle(ZoomManager.ANIMATE_NEVER);
		// }
		// return zoomManager;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("newNode"))
			refreshChildren();
		if (prop.equals("delete"))
			refreshChildren();
		if (prop.equals("backgroundColor"))
			refreshBackgroundColor();
		if (prop.equals("locationSize"))
			refreshVisuals();
		if (prop.equals("startRender")) {
			this.refresh();
			this.refreshZoom();
		}
		if (prop.equals("zoom"))
			zoom();
		if (prop.equals("exportImage")) {
			AbstractDiagram group = (AbstractDiagram) getModel();
			String filename = group.getFilename();
			String type = group.getExportType();
			GroupFigure figure = (GroupFigure) getFigure();
			ImageProducer.createImage(filename, figure.getFigure(), type);
		}
		if (prop.equals("copyToClipboard"))
			ImageProducer
					.copyToClipboard(getLayer(LayerConstants.PRINTABLE_LAYERS));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		Group group = (Group) getModel();
		Point loc = group.getLocation();
		Dimension size = group.getSize();
		Rectangle r = new Rectangle(loc, size);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), r);
	}

	/**
	 * Refresh zoom.
	 */
	public void refreshZoom() {
		Group group = (Group) getModel();
		if (group.getQueuedZoom()) {
			group.setQueuedZoom(false);
			zoom();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return ((GroupFigure) getFigure()).getFigure();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.ZoomableGraphicalEditPart#preferenceUpdate()
	 */
	public void preferenceUpdate() {
		GroupFigure figure = (GroupFigure) getFigure();
		figure.preferenceUpdate();
		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
		figure.repaint();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.DisplayEditPart#performRequest(org.eclipse.gef.Request)
	 */
	public void performRequest(Request req) {
		Group group = (Group) getModel();
		Object request = req.getType();
		if (request == RequestConstants.REQ_DIRECT_EDIT) {
			group.selected(1);
		} else if (request == RequestConstants.REQ_OPEN) {
			group.selected(2);
		}
	}

	/**
	 * Zoom.
	 */
	public void zoom() {
		Group group = getGroupModel();
		this.getViewer().flush();
		getZoomManager().setZoomAsText(group.getNestedZoom());
	}

	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	public Shell getShell() {
		return Display.getCurrent().getActiveShell();
	}

	/**
	 * Gets the background color.
	 *
	 * @return the background color
	 */
	public RGB getBackgroundColor() {
		RGB color = ((AbstractDiagram) getModel()).getColor();
		if (color != null)
			return color;
		return color;
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// return
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.DIAGRAM_BACKGROUND_COLOR);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#refresh()
	 */
	public void refresh() {
		refreshBackgroundColor();
		refreshZoom();
		super.refresh();
	}

	/**
	 * Refresh background color.
	 */
	public void refreshBackgroundColor() {
		getFigure().setBackgroundColor(
				ColorManager.getColor(getBackgroundColor()));
	}

}