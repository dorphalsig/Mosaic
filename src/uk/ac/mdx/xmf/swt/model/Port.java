package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class Port.
 */
public class Port extends DisplayWithDimension {

	/**
	 * Instantiates a new port.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 */
	public Port(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size) {
		super(parent, handler, identity, location, size, null, null);
		this.location = location;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#dispose()
	 */
	public void dispose() {
		super.dispose();
		PortRegistry.removePort(identity);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	public void delete() {
		super.delete();
		((Node) parent).removeDisplay(this);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#getLocation()
	 */
	public Point getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#getSize()
	 */
	public Dimension getSize() {
		return size;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#move(org.eclipse.draw2d.geometry.Point)
	 */
	public void move(Point location) {
		super.move(location);
		Node owner = (Node) parent;
		if (owner.isRendering())
			owner.firePropertyChange("refreshPorts", null, null);
		for (int i = 0; i < owner.sourceEdges().size(); i++) {
			Edge e = (Edge) owner.sourceEdges().elementAt(i);
			e.refresh();
		}
		for (int i = 0; i < owner.targetEdges().size(); i++) {
			Edge e = (Edge) owner.targetEdges().elementAt(i);
			e.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#resize(org.eclipse.draw2d.geometry.Dimension)
	 */
	public void resize(Dimension size) {
		super.resize(size);
		Node owner = (Node) parent;
		if (owner.isRendering())
			owner.firePropertyChange("refreshPorts", null, null);
		for (int i = 0; i < owner.sourceEdges().size(); i++) {
			Edge e = (Edge) owner.sourceEdges().elementAt(i);
			e.refresh();
		}
		for (int i = 0; i < owner.targetEdges().size(); i++) {
			Edge e = (Edge) owner.targetEdges().elementAt(i);
			e.refresh();
		}
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}
}