package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class RoundedBoxFigure.
 */
public class RoundedBoxFigure extends RoundedRectangle {

	/** The gradient. */
	boolean gradient = true;

	/**
	 * Instantiates a new rounded box figure.
	 *
	 * @param position the position
	 * @param size the size
	 * @param curve the curve
	 */
	public RoundedBoxFigure(Point position, Dimension size, int curve) {
		this.setLocation(position);
		this.setSize(size);
		this.setCornerDimensions(new Dimension(curve, curve));
		preferenceUpdate();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.RoundedRectangle#fillShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	/**
	 * Preference update.
	 */
	public void preferenceUpdate() {
		getPreferences();
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	public void getPreferences() {
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// RGB color =
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.FILL_COLOR);
		// setBackgroundColor(ColorManager.getColor(color));
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