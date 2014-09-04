package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import XOS.Message;

public abstract class DisplayWithPosition extends Display {

	Point location;

	public DisplayWithPosition(ClientElement parent, EventHandler handler,
			String identity, Point location) {
		super(parent, handler, identity);
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}

	public String getParenIdentity() {
		return parent.identity;
	}

	public void move(Point location) {
		this.location = location;
		// if (isRendering())
		firePropertyChange("locationSize", null, null);
	}

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

	@Override
	public void synchronise(Element element) {
		location.x = element.getInteger("x");
		location.y = element.getInteger("y");
		super.synchronise(element);
	}
}