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
 * The Class Box.
 */
public class Box extends Container {

	/** The corner curve. */
	int cornerCurve; // the extent to which the border is curved (0 = none)
	
	/** The top. */
	boolean top;
	
	/** The left. */
	boolean left;
	
	/** The right. */
	boolean right;
	
	/** The bottom. */
	boolean bottom;

	/**
	 * Instantiates a new box.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 * @param borderCurve the border curve
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 * @param left the left
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 */
	public Box(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size, int borderCurve, boolean top,
			boolean right, boolean bottom, boolean left, RGB lineColor,
			RGB fillColor) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
		this.cornerCurve = borderCurve;
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	/**
	 * Instantiates a new box.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param borderCurve the border curve
	 * @param top the top
	 * @param right the right
	 * @param left the left
	 * @param bottom the bottom
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 */
	public Box(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, int borderCurve, boolean top,
			boolean right, boolean left, boolean bottom, RGB lineColor,
			RGB fillColor) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), borderCurve, top, right, left, bottom, lineColor,
				fillColor);
	}

	/**
	 * Instantiates a new box.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param borderCurve the border curve
	 * @param top the top
	 * @param right the right
	 * @param left the left
	 * @param bottom the bottom
	 */
	public Box(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, int borderCurve, boolean top,
			boolean right, boolean left, boolean bottom) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), borderCurve, top, right, left, bottom, null, null);
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
	 * Gets the border curve.
	 *
	 * @return the border curve
	 */
	public int getBorderCurve() {
		return cornerCurve;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("showEdges")
				&& message.args[0].hasStrValue(identity)) {
			this.top = message.args[1].boolValue;
			this.bottom = message.args[2].boolValue;
			this.left = message.args[3].boolValue;
			this.right = message.args[4].boolValue;
			if (isRendering())
				firePropertyChange("containerSize", null, null);
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Show top.
	 *
	 * @return true, if successful
	 */
	public boolean showTop() {
		return top;
	}

	/**
	 * Show left.
	 *
	 * @return true, if successful
	 */
	public boolean showLeft() {
		return left;
	}

	/**
	 * Show right.
	 *
	 * @return true, if successful
	 */
	public boolean showRight() {
		return right;
	}

	/**
	 * Show bottom.
	 *
	 * @return true, if successful
	 */
	public boolean showBottom() {
		return bottom;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element box) {
		cornerCurve = box.getInteger("cornerCurve");
		top = box.getBoolean("showTop");
		bottom = box.getBoolean("showBottom");
		left = box.getBoolean("showLeft");
		right = box.getBoolean("showRight");
		super.synchronise(box);
	}
}