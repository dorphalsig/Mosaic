package uk.ac.mdx.xmf.swt.diagram.clipboard;

import java.io.IOException;
import java.io.InputStream;

import uk.ac.mdx.xmf.swt.diagram.stubs.BITMAPINFOHEADER;

// TODO: Auto-generated Javadoc
/**
 * The Class PrependWinBMPHeaderFilterInputStream.
 */
class PrependWinBMPHeaderFilterInputStream extends InputStream {

	/** The Constant BITMAPFILEHEADER_SIZEOF. */
	public static final int BITMAPFILEHEADER_SIZEOF = 14;
	
	/** The in. */
	private final InputStream in;
	
	/** The buffer. */
	private final byte[] buffer;
	
	/** The index. */
	private int index = 0;

	/**
	 * Instantiates a new prepend win bmp header filter input stream.
	 *
	 * @param dibStream the dib stream
	 */
	public PrependWinBMPHeaderFilterInputStream(InputStream dibStream) {
		// defined as 54
		final int offset = PrependWinBMPHeaderFilterInputStream.BITMAPFILEHEADER_SIZEOF
				+ BITMAPINFOHEADER.sizeof;

		this.in = dibStream;
		this.buffer = new byte[PrependWinBMPHeaderFilterInputStream.BITMAPFILEHEADER_SIZEOF];

		// see BITMAPFILEHEADER in windows documentation
		this.buffer[0] = 'B';
		this.buffer[1] = 'M';
		// write the offset in byte format
		// this.buffer[10] = offset;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		if (this.index < this.buffer.length) {
			return 0xff & this.buffer[this.index++];
		}
		return this.in.read();
	}
}
