package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class LabelFigure.
 */
public class LabelFigure extends Label {

	/**
	 * Instantiates a new label figure.
	 *
	 * @param position the position
	 * @param labelText the label text
	 * @param underline the underline
	 * @param italicise the italicise
	 */
	public LabelFigure(Point position, String labelText, boolean underline,
			boolean italicise) {
		super(labelText);
		// getPreferences();
		setLocation(position);
		setPreferredSize(new Dimension(-1, -1));
		if (underline)
			setUnderline(true);
		if (italicise)
			setItalicise(true);
	}

	/**
	 * Sets the font.
	 *
	 * @param font the new font
	 */
	public void setFont(String font) {
		if (!font.equals("")) {
			FontData fd = new FontData(font);
			this.setFont(FontManager.getFont(fd));
			Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
			this.setFont(classFont);
		}
	}

	// the following method is platform dependent

	/**
	 * Sets the underline.
	 *
	 * @param underline the new underline
	 */
	public void setUnderline(boolean underline) {
		Font f = this.getFont();
		FontData fd = f.getFontData()[0];
		if (underline) {
		}// fd.data.lfUnderline = 1;
		else {
		}// fd.data.lfUnderline = 0;
			// this.setFont(new Font(Display.getCurrent(), fd));

		this.setFont(FontManager.getFont(fd));
		this.repaint();
	}

	/**
	 * Sets the italicise.
	 *
	 * @param italicise the new italicise
	 */
	public void setItalicise(boolean italicise) {
		Font f = this.getFont();
		FontData fd = f.getFontData()[0];
		if (italicise)
			fd.setStyle(SWT.ITALIC);
		else
			fd.setStyle(SWT.NORMAL);
		this.setFont(FontManager.getFont(fd));
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
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
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// FontData fontData =
		// PreferenceConverter.getFontData(preferences,IPreferenceConstants.FONT);
		// setFont(FontManager.getFont(fontData));
		// setPreferredSize(new Dimension(-1,-1));
	}
}