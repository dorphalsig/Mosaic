package uk.ac.mdx.xmf.swt.diagram.clipboard;

import java.io.IOException;
import java.io.OutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class RemoveWinBMPHeaderFilterOutputStream.
 */
class RemoveWinBMPHeaderFilterOutputStream extends OutputStream {
	
	/** The out. */
	private final OutputStream out;
	
	/** The counter. */
	private int counter = 0;

	/**
	 * Instantiates a new removes the win bmp header filter output stream.
	 *
	 * @param out the out
	 */
	public RemoveWinBMPHeaderFilterOutputStream(OutputStream out) {
		this.out = out;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		// ignore the bmp file header
		if (this.counter < PrependWinBMPHeaderFilterInputStream.BITMAPFILEHEADER_SIZEOF) {
			this.counter++;
		} else {
			this.out.write(b);
		}
	}
}