package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineText.
 */
public class MultilineText extends DisplayWithDimension {

	/** The text. */
	private String text;
	
	/** The editable. */
	private boolean editable;
	
	/** The edit. */
	private boolean edit = false;
	
	/** The font. */
	private String font;

	/**
	 * Instantiates a new multiline text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param text the text
	 * @param location the location
	 * @param size the size
	 * @param editable the editable
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 * @param font the font
	 */
	public MultilineText(ClientElement parent, EventHandler handler,
			String identity, String text, Point location, Dimension size,
			boolean editable, RGB lineColor, RGB fillColor, String font) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
		this.text = text;
		this.editable = editable;
		this.font = font;
	}

	/**
	 * Instantiates a new multiline text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param text the text
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param editable the editable
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 * @param font the font
	 */
	public MultilineText(ClientElement parent, EventHandler handler,
			String identity, String text, int x, int y, int width, int height,
			boolean editable, RGB lineColor, RGB fillColor, String font) {
		this(parent, handler, identity, text, new Point(x, y), new Dimension(
				width, height), editable, lineColor, fillColor, font);
	}

	/**
	 * Instantiates a new multiline text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param text the text
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param editable the editable
	 * @param font the font
	 */
	public MultilineText(ClientElement parent, EventHandler handler,
			String identity, String text, int x, int y, int width, int height,
			boolean editable, String font) {
		this(parent, handler, identity, text, new Point(x, y), new Dimension(
				width, height), editable, null, null, font);
	}

	/**
	 * Change text.
	 *
	 * @param text the text
	 */
	public void changeText(String text) {
		Message m = handler.newMessage("textChanged", 2);
		Value v1 = new Value(identity);
		Value v2 = new Value(text);
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/**
	 * Edits the.
	 *
	 * @return true, if successful
	 */
	public boolean edit() {
		return edit;
	}

	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public String getFont() {
		return font;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("editText")
				&& message.args[0].hasStrValue(identity) && message.arity == 1) {

			// the edit may happen before the text is constructed. In which case
			// store the edit and the text's editpart will check after it has
			// constructed

			if (canFire())
				firePropertyChange("editText", null, null);
			else
				edit = true;
		}
		if (message.hasName("setText") && message.args[0].hasStrValue(identity)
				&& message.arity == 2) {
			text = message.args[1].strValue();
			if (isRendering())
				firePropertyChange("textChanged", null, null);
			return true;
		}
		if (message.hasName("setFont") && message.args[0].hasStrValue(identity)
				&& message.arity == 2) {
			font = message.args[1].strValue();
			if (isRendering())
				firePropertyChange("textChanged", null, null);
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Sets the edits the.
	 *
	 * @param edit the new edits the
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithDimension#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element multilineText) {
		text = multilineText.getString("text");
		font = multilineText.getString("font");
		editable = multilineText.getBoolean("editable");
		super.synchronise(multilineText);
	}
}