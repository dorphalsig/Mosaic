package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.misc.ColorManager;

// TODO: Auto-generated Javadoc
/**
 * The Class DiagramFigure.
 */
public class DiagramFigure extends FreeformLayer {

	/** The gridlines. */
	boolean gridlines = false;
	
	/** The distance. */
	int distance = 20;
	
	/** The grid color. */
	Color gridColor = ColorManager.getColor(new RGB(206, 206, 206));

	/**
	 * Instantiates a new diagram figure.
	 */
	public DiagramFigure() {
		getPreferences();
	}

	/** The image. */
	org.eclipse.swt.graphics.Image image = null;

	/*
	 * public org.eclipse.swt.graphics.Image getImage() { if(image == null) {
	 * image = new
	 * org.eclipse.swt.graphics.Image(Display.getDefault(),1000,500); GC gc =
	 * new GC(image,SWT.NONE); Graphics canvas = new SWTGraphics(gc);
	 * canvas.setForegroundColor(gridColor);
	 * canvas.drawRectangle(30,30,200,200); } return image; }
	 */

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void paintFigure(Graphics g) {
		// if(Diagram.antialias)
		// g.setAntialias(SWT.ON);
		super.paintFigure(g);
		// g.drawImage(getImage(),0,0);
		if (gridlines)
			drawGridlines(distance, g);
	}

	/**
	 * Draw gridlines.
	 *
	 * @param distance the distance
	 * @param g the g
	 */
	public void drawGridlines(int distance, Graphics g) {
		g.setForegroundColor(gridColor);
		Rectangle region = new Rectangle();
		g.getClip(region);
		g.fillRectangle(region);
		g.setLineWidth(1);
		for (int i = region.x - 2; i < region.right() + 2; i++)
			if (i % distance == 0)
				g.drawLine(i, region.y - 2, i, region.bottom() + 2);
		for (int i = region.y - 2; i < region.bottom() + 2; i++)
			if (i % distance == 0)
				g.drawLine(region.x - 2, i, region.right() + 2, i);
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
		// Preferences preferences =
		// DiagramPlugin.getDefault().getPluginPreferences();
		// gridlines = preferences.getBoolean(IPreferenceConstants.GRIDLINES);
		// distance = preferences.getInt(IPreferenceConstants.GRIDSIZE);
		// IPreferenceStore ipreferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// RGB color =
		// PreferenceConverter.getColor(ipreferences,IPreferenceConstants.DIAGRAM_BACKGROUND_COLOR);
		// setBackgroundColor(ColorManager.getColor(color));
	}
}