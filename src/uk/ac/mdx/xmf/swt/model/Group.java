package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.SharedMessages;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Group.
 */
public class Group extends AbstractDiagram {

	/** The nested zoom. */
	String nestedZoom = "100";
	
	/** The is top level. */
	private boolean isTopLevel = false;

	/**
	 * Instantiates a new group.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 */
	public Group(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size) {
		super(parent, handler, identity, location, size);
	}

	/**
	 * Instantiates a new group.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public Group(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height));
	}

	/**
	 * Gets the nested zoom.
	 *
	 * @return the nested zoom
	 */
	public String getNestedZoom() {
		return nestedZoom;
	}

	/**
	 * Checks if is top level.
	 *
	 * @return true, if is top level
	 */
	public boolean isTopLevel() {
		return isTopLevel;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.AbstractDiagram#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (super.processMessage(message))
			return true;
		if (message.hasName("zoomIn") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			zoomIn();
			return true;
		}
		if (message.hasName("zoomOut") && message.args[0].hasStrValue(identity)
				&& message.arity == 1) {
			zoomOut();
			return true;
		}
		if (message.hasName("nestedZoomTo")
				&& message.args[0].hasStrValue(identity) && message.arity == 2) {
			int percent = message.args[1].intValue;
			if (percent == -1)
				zoomToFit();
			else
				nestedZoomTo(percent);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.AbstractDiagram#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element element) {
		String nestedZoom = element.getString("nestedZoom");
		this.nestedZoom = nestedZoom;
		super.synchronise(element);
	}

	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		ClientElement parent = getParent();
		while (!(parent instanceof Diagram))
			parent = parent.getParent();
		Diagram diagram = (Diagram) parent;
		diagram.zoomTo(this, true);
	}

	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		ClientElement parent = getParent();
		while (!(parent instanceof Diagram))
			parent = parent.getParent();
		ClientElement levelUp = getParent();
		while (!(levelUp instanceof AbstractDiagram))
			levelUp = levelUp.getParent();
		Diagram diagram = (Diagram) parent;
		AbstractDiagram target = (AbstractDiagram) levelUp;
		if (diagram.zoomTo(target, true))
			diagram.refreshZoom();
	}

	/**
	 * Nested zoom to.
	 *
	 * @param percent the percent
	 */
	public void nestedZoomTo(int percent) {
		nestedZoom = new Integer(percent).toString();
		if (isRendering())
			firePropertyChange("zoom", null, null);
		else
			queuedZoom = true;
	}

	/**
	 * Sets the top level.
	 *
	 * @param isTopLevel the new top level
	 */
	public void setTopLevel(boolean isTopLevel) {
		this.isTopLevel = isTopLevel;
	}

	/**
	 * Zoom to fit.
	 */
	public void zoomToFit() {
		nestedZoom = SharedMessages.FitAllAction_Label;
		if (isRendering())
			firePropertyChange("zoom", null, null);
		else
			queuedZoom = true;
	}
}