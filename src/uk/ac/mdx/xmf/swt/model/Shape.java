package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Shape.
 */
public class Shape extends DisplayWithDimension {

	/** The points. */
	Vector points;

	/** The outline. */
	boolean outline;

	/**
	 * Instantiates a new shape.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param outline the outline
	 * @param points the points
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 */
	public Shape(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean outline,
			Vector points, RGB lineColor, RGB fillColor) {
		super(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), lineColor, fillColor);
		this.points = points;
		this.outline = outline;
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
	 * Redraw shape.
	 *
	 * @param message the message
	 */
	public void redrawShape(Message message) {
		int x = message.args[1].intValue;
		int y = message.args[2].intValue;
		int width = message.args[3].intValue;
		int height = message.args[4].intValue;
		location = new Point(x, y);
		size = new Dimension(width, height);
		outline = message.args[5].boolValue;
		Value[] rawPoints = message.args[6].values;
		points = new Vector();
		for (int i = 0; i < rawPoints.length; i = i + 2) {
			int xPos = rawPoints[i].intValue;
			int yPos = rawPoints[i + 1].intValue;
			points.addElement(new Point(xPos, yPos));
		}
		if (isRendering())
			firePropertyChange("redrawShape", null, null);
	}

	/**
	 * X.
	 *
	 * @return the int
	 */
	public int x() {
		return location.x;
	}

	/**
	 * Y.
	 *
	 * @return the int
	 */
	public int y() {
		return location.y;
	}

	/**
	 * Width.
	 *
	 * @return the int
	 */
	public int width() {
		return size.width;
	}

	/**
	 * Height.
	 *
	 * @return the int
	 */
	public int height() {
		return size.height;
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public Vector getPoints() {
		return points;
	}

	/**
	 * Outline.
	 *
	 * @return true, if successful
	 */
	public boolean outline() {
		return outline;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (super.processMessage(message))
			return true;
		if (message.hasName("redrawShape")
				&& message.args[0].hasStrValue(identity) && message.arity == 7) {
			redrawShape(message);
			return true;
		}
		return false;
	}

	/**
	 * Parses the bool.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean parseBool(String value) {
		if (value.equals("true"))
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#setFillColor(int, int, int)
	 */
	@Override
	public void setFillColor(int red, int green, int blue) {
		fillColor = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}

	/**
	 * Sets the line color.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setLineColor(int red, int green, int blue) {
		foregroundColor = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}
}