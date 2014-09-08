package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Line.
 */
public class Line extends Display {

	/** The start. */
	private Point start; // start of the line
	
	/** The end. */
	private Point end; // end of the line
	
	/** The color. */
	private RGB color; // line colour

	/**
	 * Instantiates a new line.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param start the start
	 * @param end the end
	 * @param color the color
	 */
	public Line(ClientElement parent, EventHandler handler, String identity,
			Point start, Point end, RGB color) {
		super(parent, handler, identity);
		this.start = start;
		this.end = end;
		this.color = color;
	}

	/**
	 * Instantiates a new line.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param color the color
	 */
	public Line(ClientElement parent, EventHandler handler, String identity,
			int x1, int y1, int x2, int y2, RGB color) {
		this(parent, handler, identity, new Point(x1, y1), new Point(x2, y2),
				color);
	}

	/**
	 * Instantiates a new line.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 */
	public Line(ClientElement parent, EventHandler handler, String identity,
			int x1, int y1, int x2, int y2) {
		this(parent, handler, identity, new Point(x1, y1), new Point(x2, y2),
				null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		((Container) parent).removeDisplay(this);
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public RGB getColor() {
		return color;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * Resize.
	 *
	 * @param start the start
	 * @param end the end
	 */
	public void resize(Point start, Point end) {
		this.start = start;
		this.end = end;
		if (isRendering())
			firePropertyChange("locationSize", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Display#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("resizeLine")
				&& message.args[0].hasStrValue(identity) && message.arity == 5) {
			int x1 = message.args[1].intValue;
			int y1 = message.args[2].intValue;
			int x2 = message.args[3].intValue;
			int y2 = message.args[4].intValue;
			resize(new Point(x1, y1), new Point(x2, y2));
			return true;
		}
		if (message.hasName("setColor")
				&& message.args[0].hasStrValue(identity) && message.arity == 4) {
			int red = message.args[1].intValue;
			int green = message.args[2].intValue;
			int blue = message.args[3].intValue;
			setColor(red, green, blue);
			return true;
		}
		return false;
	}

	/**
	 * Sets the color.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setColor(int red, int green, int blue) {
		color = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element line) {
		int x1 = line.getInteger("x1");
		int y1 = line.getInteger("y1");
		int x2 = line.getInteger("x2");
		int y2 = line.getInteger("y2");
		start = new Point(x1, y1);
		end = new Point(x2, y2);
		super.synchronise(line);
	}
}