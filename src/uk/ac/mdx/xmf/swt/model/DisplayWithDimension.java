package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayWithDimension.
 */
public abstract class DisplayWithDimension extends DisplayWithPosition {

	/** The fill. */
	boolean fill = true;
	
	/** The fill color. */
	RGB fillColor = null;
	
	/** The foreground color. */
	RGB foregroundColor = null;
	
	/** The size. */
	Dimension size;

	/**
	 * Instantiates a new display with dimension.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 * @param foregroundColor the foreground color
	 * @param fillColor the fill color
	 */
	public DisplayWithDimension(ClientElement parent, EventHandler handler,
			String identity, Point location, Dimension size,
			RGB foregroundColor, RGB fillColor) {
		super(parent, handler, identity, location);
		this.size = size;
		this.foregroundColor = foregroundColor;
		this.fillColor = fillColor;
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public RGB getFillColor() {
		return fillColor;
	}

	/**
	 * Gets the foreground color.
	 *
	 * @return the foreground color
	 */
	public RGB getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * Gets the fill.
	 *
	 * @return the fill
	 */
	public boolean getfill() {
		return fill;
	}

	/**
	 * Resize.
	 *
	 * @param size the size
	 */
	public void resize(Dimension size) {
		this.size = size;
		// if (isRendering())
		firePropertyChange("locationSize", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("resize") && message.args[0].hasStrValue(identity)
				&& message.arity == 3) {
			int width = message.args[1].intValue;
			int height = message.args[2].intValue;
			// System.out.println("resize:" + parent.identity + "-" + "-"
			// + identity + "     " + width + "      " + height);
			resize(new Dimension(width, height));
			return true;
		}
		if (message.hasName("setFill") && message.args[0].hasStrValue(identity)
				&& message.arity == 2) {
			setFill(message.args[1].boolValue);
			return true;
		}
		if (message.hasName("setFillColor")
				&& message.args[0].hasStrValue(identity) && message.arity == 4) {
			int red = message.args[1].intValue;
			int green = message.args[2].intValue;
			int blue = message.args[3].intValue;
			setFillColor(red, green, blue);
			return true;
		}
		if (message.hasName("setLineColor")
				&& message.args[0].hasStrValue(identity) && message.arity == 4) {
			int red = message.args[1].intValue;
			int green = message.args[2].intValue;
			int blue = message.args[3].intValue;
			setForegroundColor(red, green, blue);
			return true;
		}
		return super.processMessage(message);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element element) {
		size.width = element.getInteger("width");
		size.height = element.getInteger("height");
		super.synchronise(element);
	}

	/**
	 * Sets the fill.
	 *
	 * @param fill the new fill
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
		if (isRendering())
			firePropertyChange("locationSize", null, null);
	}

	/**
	 * Sets the fill color.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setFillColor(int red, int green, int blue) {
		fillColor = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}

	/**
	 * Sets the foreground color.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setForegroundColor(int red, int green, int blue) {
		foregroundColor = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}
}