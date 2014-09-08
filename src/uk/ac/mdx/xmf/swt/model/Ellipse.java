package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Ellipse.
 */
public class Ellipse extends Container {

	/** The outline. */
	boolean outline;

	/**
	 * Instantiates a new ellipse.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param location the location
	 * @param size the size
	 * @param outline the outline
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 */
	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size, boolean outline, RGB lineColor,
			RGB fillColor) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
		this.outline = outline;
	}

	/**
	 * Instantiates a new ellipse.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param outline the outline
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 */
	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean outline,
			RGB lineColor, RGB fillColor) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), outline, lineColor, fillColor);
	}

	/**
	 * Instantiates a new ellipse.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param outline the outline
	 */
	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean outline) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), outline, null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	public void delete() {
		super.delete();
		((Container) parent).removeDisplay(this);
	}

	/**
	 * Gets the outline.
	 *
	 * @return the outline
	 */
	public boolean getOutline() {
		return outline;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.Container#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	public void synchronise(Element ellipse) {
		outline = ellipse.getBoolean("showOutline");
		super.synchronise(ellipse);
	}
}