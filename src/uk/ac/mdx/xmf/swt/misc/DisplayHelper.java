package uk.ac.mdx.xmf.swt.misc;

import java.awt.Dimension;
import java.awt.Toolkit;

// TODO: Auto-generated Javadoc
/**
 * Contains utility methods related to the display screen.
 * 
 * @author Yong
 *
 */
public class DisplayHelper {
	
	/** The _screen dimension. */
	private static Dimension _screenDimension = null;

	/**
	 * Compute dimension.
	 */
	private static void computeDimension() {
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
		_screenDimension = toolkit.getScreenSize();
	}
	
	/**
	 * Returns the width of the display screen.
	 * 
	 * @return the width of the display screen in pixels.
	 */
	public static int getScreenWidth() {
		if (_screenDimension == null) {
			computeDimension();
		}
		
		return (int)_screenDimension.getWidth();
	}
	
	/**
	 * Returns the height of the display screen.
	 * 
	 * @return the height of the display screen in pixels.
	 */
	public static int getScreenHeight() {
		if (_screenDimension == null) {
			computeDimension();
		}
		
		return (int)_screenDimension.getHeight();
	}
}
