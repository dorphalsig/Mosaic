package uk.ac.mdx.xmf.swt.diagram.clipboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import uk.ac.mdx.xmf.swt.diagram.stubs.COM;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageDataTransfer.
 */
public class ImageDataTransfer extends ByteArrayTransfer {

	/** The Constant INSTANCE. */
	private static final ImageDataTransfer INSTANCE = new ImageDataTransfer();
	
	/** The Constant CF_DIB. */
	private static final String CF_DIB = "CF_DIB";
	
	/** The Constant CF_DIBID. */
	private static final int CF_DIBID = COM.CF_DIB;

	/**
	 * Instantiates a new image data transfer.
	 */
	private ImageDataTransfer() {
		// Singleton
	}

	/**
	 * Gets the single instance of ImageDataTransfer.
	 *
	 * @return single instance of ImageDataTransfer
	 */
	public static ImageDataTransfer getInstance() {
		return ImageDataTransfer.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected final int[] getTypeIds() {
		return new int[] { ImageDataTransfer.CF_DIBID };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected final String[] getTypeNames() {
		return new String[] { ImageDataTransfer.CF_DIB };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	protected Object nativeToJava(TransferData transferData) {
		final Object o = super.nativeToJava(transferData);
		final byte[] bytes = (byte[]) o;

		try {
			final InputStream bis = new PrependWinBMPHeaderFilterInputStream(
					new UncompressDibFilterInputStream(
							new ByteArrayInputStream(bytes)));
			final ImageData[] data = new ImageLoader().load(bis);
			if (data.length < 1) {
				return null;
			}
			return data[0];
		} catch (IOException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	protected void javaToNative(Object object, TransferData transferData) {
		final ImageData imgData = (ImageData) object;
		final ImageLoader loader = new ImageLoader();
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final byte[] bytes;

		loader.data = new ImageData[] { imgData };
		loader.save(new RemoveWinBMPHeaderFilterOutputStream(bos),
				SWT.IMAGE_BMP);
		bytes = bos.toByteArray();
		super.javaToNative(bytes, transferData);
	}
}
