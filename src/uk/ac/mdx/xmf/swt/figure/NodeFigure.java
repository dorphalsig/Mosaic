package uk.ac.mdx.xmf.swt.figure;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Port;

// TODO: Auto-generated Javadoc
/**
 * The Class NodeFigure.
 */
public class NodeFigure extends Figure {

	/** The fixed anchors. */
	Hashtable fixedAnchors = new Hashtable();

	/**
	 * Instantiates a new node figure.
	 *
	 * @param location the location
	 * @param size the size
	 * @param ports the ports
	 */
	public NodeFigure(Point location, Dimension size, Vector ports) {
		setLocation(location);
		setSize(size);
		createFixedAnchors(ports);
	}

	/**
	 * Contains group figure.
	 *
	 * @return true, if successful
	 */
	public boolean containsGroupFigure() {
		return containsGroupFigure(this);
	}

	/**
	 * Contains group figure.
	 *
	 * @param parent the parent
	 * @return true, if successful
	 */
	public boolean containsGroupFigure(Figure parent) {
		List children = parent.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Figure child = (Figure) children.get(i);
			if (child instanceof GroupFigure)
				return true;
			else
				return containsGroupFigure(child);
		}
		return false;
	}

	/**
	 * Creates the fixed anchors.
	 *
	 * @param ports the ports
	 */
	protected void createFixedAnchors(Vector ports) {
		for (int i = 0; i < ports.size(); i++) {
			Port p = (Port) ports.elementAt(i);
			Figure figure = new Figure();
			figure.setLocation(p.getLocation());
			figure.setSize(p.getSize());
			figure.setParent(this);
			figure.getParent().translateToRelative(figure.getBounds());
			figure.getParent().translateToAbsolute(figure.getBounds());
			FixedAnchor anchor = new FixedAnchor(figure, p.getIdentity());
			fixedAnchors.put(p.getIdentity(), anchor);
		}
	}

	/**
	 * Gets the connection anchor.
	 *
	 * @param name the name
	 * @return the connection anchor
	 */
	public AbstractConnectionAnchor getConnectionAnchor(String name) {
		return getFixedConnectionAnchor(name);
	}

	/**
	 * Gets the fixed connection anchor.
	 *
	 * @param name the name
	 * @return the fixed connection anchor
	 */
	public FixedAnchor getFixedConnectionAnchor(String name) {
		if (fixedAnchors.containsKey(name))
			return (FixedAnchor) fixedAnchors.get(name);
		else
			return null;
	}

	/**
	 * Gets the anchor.
	 *
	 * @param p the p
	 * @return the anchor
	 */
	public AbstractConnectionAnchor getAnchor(Point p) {
		return getFixedAnchor(p);
	}

	/**
	 * Gets the fixed anchor.
	 *
	 * @param p the p
	 * @return the fixed anchor
	 */
	public FixedAnchor getFixedAnchor(Point p) {
		Enumeration e = fixedAnchors.elements();
		while (e.hasMoreElements()) {
			FixedAnchor anchor = (FixedAnchor) e.nextElement();
			if (anchor.containsPoint(p))
				return anchor;
		}
		return null;
	}

	/** The count. */
	static int count = 0;

	/**
	 * Reset fixed ports.
	 *
	 * @param ports the ports
	 */
	public void resetFixedPorts(Vector ports) {
		fixedAnchors = new Hashtable();
		createFixedAnchors(ports);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

}