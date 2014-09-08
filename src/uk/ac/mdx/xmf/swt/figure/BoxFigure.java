package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

// TODO: Auto-generated Javadoc
// public class BoxFigure extends Shape { // RectangleFigure {

/**
 * The Class BoxFigure.
 */
public class BoxFigure extends RectangleFigure {

	/** The gradient. */
	public boolean gradient = true;
	
	/** The top. */
	public boolean top = true;
	
	/** The left. */
	public boolean left = true;
	
	/** The right. */
	public boolean right = true;
	
	/** The bottom. */
	public boolean bottom = true;

	/**
	 * Instantiates a new box figure.
	 *
	 * @param position the position
	 * @param size the size
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 * @param left the left
	 */
	public BoxFigure(Point position, Dimension size, boolean top,
			boolean right, boolean bottom, boolean left) {
		this.setBounds(new Rectangle(position, size));
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;

		this.setBackgroundColor(ColorConstants.lightBlue);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.RectangleFigure#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		graphics.fillRectangle(getBounds());
		if (!gradient)
			graphics.fillGradient(getBounds(), false);
		else
			graphics.fillRectangle(getBounds());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.RectangleFigure#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		Rectangle r = getBounds();
		int x = r.x + lineWidth / 2;
		int y = r.y + lineWidth / 2;
		// int w = r.width - lineWidth;
		// int h = r.height - lineWidth;
		int w = r.width - Math.max(1, lineWidth);
		int h = r.height - Math.max(1, lineWidth);

		if (top)
			graphics.drawLine(x, y, x + w, y);
		if (left)
			graphics.drawLine(x, y, x, y + h);
		if (right)
			graphics.drawLine(x + w, y, x + w, y + h);
		if (bottom)
			graphics.drawLine(x, y + h, x + w, y + h);
	}

	/**
	 * Sets the gradient fill.
	 *
	 * @param b the new gradient fill
	 */
	public void setGradientFill(boolean b) {
		this.gradient = b;
	}
}