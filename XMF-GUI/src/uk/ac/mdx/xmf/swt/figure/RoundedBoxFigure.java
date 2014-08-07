package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class RoundedBoxFigure extends RoundedRectangle {

	boolean gradient = true;

	public RoundedBoxFigure(Point position, Dimension size, int curve) {
		this.setLocation(position);
		this.setSize(size);
		this.setCornerDimensions(new Dimension(curve, curve));
		preferenceUpdate();
	}

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.fillRoundRectangle(getBounds(), corner.width, corner.height);
	}

	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	public void preferenceUpdate() {
		getPreferences();
	}

	public void getPreferences() {
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// RGB color =
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.FILL_COLOR);
		// setBackgroundColor(ColorManager.getColor(color));
	}

	public void setGradientFill(boolean b) {
		this.gradient = b;
	}
}