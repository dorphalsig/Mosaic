package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;

public class Ellipse extends Container {

	boolean outline;

	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			Point location, Dimension size, boolean outline, RGB lineColor,
			RGB fillColor) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
		this.outline = outline;
	}

	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean outline,
			RGB lineColor, RGB fillColor) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), outline, lineColor, fillColor);
	}

	public Ellipse(ClientElement parent, EventHandler handler, String identity,
			int x, int y, int width, int height, boolean outline) {
		this(parent, handler, identity, new Point(x, y), new Dimension(width,
				height), outline, null, null);
	}

	public void delete() {
		super.delete();
		((Container) parent).removeDisplay(this);
	}

	public boolean getOutline() {
		return outline;
	}

	public void synchronise(Element ellipse) {
		outline = ellipse.getBoolean("showOutline");
		super.synchronise(ellipse);
	}
}