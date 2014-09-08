package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineTextFigure.
 */
public class MultilineTextFigure extends Figure {

	/** The text flow. */
	public TextFlow textFlow;

	/**
	 * Instantiates a new multiline text figure.
	 *
	 * @param position the position
	 * @param size the size
	 * @param forecolor the forecolor
	 * @param backcolor the backcolor
	 */
	public MultilineTextFigure(Point position, Dimension size, RGB forecolor,
			RGB backcolor) {
		this.setBounds(new Rectangle(position, size));
		buildFlowpage(3);
		textFlow.setBackgroundColor(ColorConstants.black);
	}

	/**
	 * Builds the flowpage.
	 *
	 * @param border the border
	 */
	public void buildFlowpage(int border) {
		setBorder(new MarginBorder(border));
		FlowPage flowPage = new FlowPage();
		textFlow = new TextFlow();
		textFlow.setLayoutManager(new ParagraphTextLayout(textFlow,
				ParagraphTextLayout.WORD_WRAP_SOFT));
		flowPage.add(textFlow);
		setLayoutManager(new StackLayout());
		add(flowPage);
	}

	/**
	 * Gets the figure.
	 *
	 * @return the figure
	 */
	public Figure getFigure() {
		return textFlow;
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
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return textFlow.getText();
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
			textFlow.setFont(FontManager.getFont(fd));
		}
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		textFlow.setText(text);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

}