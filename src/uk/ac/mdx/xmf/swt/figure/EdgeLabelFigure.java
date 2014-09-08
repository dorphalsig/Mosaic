package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeLabelFigure.
 */
public class EdgeLabelFigure extends Label {

	/** The tooltip. */
	Label tooltip;

	/**
	 * Instantiates a new edge label figure.
	 *
	 * @param labelText the label text
	 * @param underline the underline
	 */
	public EdgeLabelFigure(String labelText, boolean underline) {
		super(labelText);
		getPreferences();
		tooltip = new Label(labelText);
		this.setToolTip(tooltip);
		if (underline)
			underline();

	}

	// unfortunately the following method is platform dependent

	/**
	 * Underline.
	 */
	public void underline() {
		Font f = this.getFont();
		FontData fd = f.getFontData()[0];

		// fd.data.lfUnderline = 1;
		this.setFont(FontManager.getFont(fd));
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
	 * Sets the font.
	 *
	 * @param font the new font
	 */
	public void setFont(String font) {
		if (!font.equals("")) {
			FontData fd = new FontData(font);
			this.setFont(FontManager.getFont(fd));
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Label#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		super.setText(text);
		if (tooltip == null)
			tooltip = new Label(text);
		else
			tooltip.setText(text);
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	public void getPreferences() {
		// IPreferenceStore preferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// RGB fontColor =
		// PreferenceConverter.getColor(preferences,IPreferenceConstants.UNSELECTED_FONT_COLOR);
		// FontData fd =
		// PreferenceConverter.getFontData(preferences,IPreferenceConstants.FONT);
		// setFont(FontManager.getFont(fd));
		// setForegroundColor(ColorManager.getColor(fontColor));
	}
}