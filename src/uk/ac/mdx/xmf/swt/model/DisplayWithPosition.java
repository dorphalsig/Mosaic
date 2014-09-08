package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayWithPosition.
 */
public abstract class DisplayWithPosition extends Display {

	/** The location. */
	Point location;

	/**
	 * Instantiates a new display with position.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 */
	public DisplayWithPosition(ClientElement parent, EventHandler handler,
			String identity, Point location) {
		super(parent, handler, identity);
		this.location = location;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Gets the paren identity.
	 *
	 * @return the paren identity
	 */
	public String getParenIdentity() {
		return parent.identity;
	}

	/**
	 * Move.
	 *
	 * @param location the location
	 */
	public void move(Point location) {
		this.location = location;
		// if (isRendering())
		firePropertyChange("locationSize", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Display#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("move") && message.args[0].hasStrValue(identity)
				&& message.arity == 3) {
			int newX = message.args[1].intValue;
			int newY = message.args[2].intValue;
			move(new Point(newX, newY));
			// System.out.println("move:" + parent.identity + "-" + "-" +
			// identity
			// + "     " + new Point(newX, newY));
			return true;
		}
		return super.processMessage(message);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element element) {
		location.x = element.getInteger("x");
		location.y = element.getInteger("y");
		super.synchronise(element);
	}
}