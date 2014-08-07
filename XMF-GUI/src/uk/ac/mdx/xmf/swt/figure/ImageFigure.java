package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.swt.graphics.Image;

import uk.ac.mdx.xmf.swt.model.ImageManager;

public class ImageFigure extends org.eclipse.draw2d.ImageFigure {

	public ImageFigure(Image image) {
		super(image);
	}

	@Override
	protected boolean useLocalCoordinates() {
		return false;
	}

	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		this.setImage(ImageManager.resizeImage(this.getImage(), w, h));
	}
}