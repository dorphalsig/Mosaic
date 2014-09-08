package com.ceteva.mosaic.splash;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageCanvas.
 */
public class ImageCanvas implements PaintListener {

	/** The image. */
	Image image;
	
	/**
	 * Instantiates a new image canvas.
	 *
	 * @param image the image
	 */
	public ImageCanvas(Image image) {
		this.image = image;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		e.gc.drawImage(image, 0, 0);
	}

}
