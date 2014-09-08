package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Image.
 */
public class Image extends DisplayWithDimension {

	/** The filename. */
	String filename = "";
	
	/** The image. */
	org.eclipse.swt.graphics.Image image = null;

	/**
	 * Instantiates a new image.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param filename the filename
	 * @param location the location
	 * @param size the size
	 */
	public Image(ClientElement parent, EventHandler handler, String identity,
			String filename, Point location, Dimension size) {
		super(parent, handler, identity, location, size, null, null);
		// getImage(filename);
		this.filename = filename;
	}

	/**
	 * Instantiates a new image.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param filename the filename
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public Image(ClientElement parent, EventHandler handler, String identity,
			String filename, int x, int y, int width, int height) {
		this(parent, handler, identity, filename, new Point(x, y),
				new Dimension(width, height));
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		((Container) parent).removeDisplay(this);
	}

	/**
	 * Gets the image.
	 *
	 * @param filename the filename
	 * @return the image
	 */
	public org.eclipse.swt.graphics.Image getImage(String filename) {
		if (filename.equalsIgnoreCase("logo.jpg"))
			filename = "icons/" + filename;
		return ImageManager.getImage(filename);
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public org.eclipse.swt.graphics.Image getImage() {

		// figures dispose of their images when they are destroyed (i.e. as a
		// result of a zoom)
		// this checks to make sure that the image has not been disposed.

		if (image == null || image.isDisposed())
			image = getImage(filename);
		return image;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("setImage")
				&& message.args[0].hasStrValue(identity)) {
			filename = message.args[1].strValue();
			if (isRendering())
				firePropertyChange("imageChanged", null, null);
			return true;
		} else
			return super.processMessage(message);
	}
}