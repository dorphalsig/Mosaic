package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.swt.graphics.Image;

import uk.ac.mdx.xmf.swt.model.ImageManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageFigure.
 */
public class ImageFigure extends org.eclipse.draw2d.ImageFigure {

	/**
	 * Instantiates a new image figure.
	 *
	 * @param image the image
	 */
	public ImageFigure(Image image) {
		super(image);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setSize(int, int)
	 */
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		this.setImage(ImageManager.resizeImage(this.getImage(), w, h));
	}
}