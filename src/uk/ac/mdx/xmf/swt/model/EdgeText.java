package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeText.
 */
public class EdgeText extends DisplayWithPosition {

	/** The text. */
	private String text; // the text to be displayed
	
	/** The position. */
	private String position; // the position of the text relative to the edge
								// (start,middle,end)
	/** The editable. */
								private boolean editable; // denotes whether the text can be edited
	
	/** The underline. */
	private boolean underline;
	
	/** The truncate. */
	private int truncate;
	
	/** The color. */
	private RGB color;
	
	/** The font. */
	private String font;

	/**
	 * Instantiates a new edge text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param position the position
	 * @param location the location
	 * @param textString the text string
	 * @param editable the editable
	 * @param underline the underline
	 * @param condense the condense
	 * @param color the color
	 * @param font the font
	 */
	public EdgeText(ClientElement parent, EventHandler handler,
			String identity, String position, Point location,
			String textString, boolean editable, boolean underline,
			int condense, RGB color, String font) {
		super(parent, handler, identity, location);
		this.position = position;
		this.text = textString;
		this.editable = editable;
		this.underline = underline;
		this.truncate = condense;
		this.color = color;
		this.font = font;
	}

	/**
	 * Instantiates a new edge text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param position the position
	 * @param x the x
	 * @param y the y
	 * @param textString the text string
	 * @param editable the editable
	 * @param underline the underline
	 * @param condense the condense
	 * @param color the color
	 * @param font the font
	 */
	public EdgeText(ClientElement parent, EventHandler handler,
			String identity, String position, int x, int y, String textString,
			boolean editable, boolean underline, int condense, RGB color,
			String font) {
		this(parent, handler, identity, position, new Point(x, y), textString,
				editable, underline, condense, color, font);
	}

	/**
	 * Instantiates a new edge text.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 * @param position the position
	 * @param x the x
	 * @param y the y
	 * @param textString the text string
	 * @param editable the editable
	 * @param underline the underline
	 * @param condense the condense
	 * @param font the font
	 */
	public EdgeText(ClientElement parent, EventHandler handler,
			String identity, String position, int x, int y, String textString,
			boolean editable, boolean underline, int condense, String font) {
		this(parent, handler, identity, position, new Point(x, y), textString,
				editable, underline, condense, null, font);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		super.delete();
		((Edge) parent).removeEdgeText(this);
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public RGB getColor() {
		return color;
	}

	/**
	 * Gets the condense size.
	 *
	 * @return the condense size
	 */
	public int getCondenseSize() {
		return truncate;
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
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Gets the underline.
	 *
	 * @return the underline
	 */
	public boolean getUnderline() {
		return underline;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("setColor")
				&& message.args[0].hasStrValue(identity) && message.arity == 4) {
			int red = message.args[1].intValue;
			int green = message.args[2].intValue;
			int blue = message.args[3].intValue;
			setColor(red, green, blue);
			return true;
		}
		if (message.hasName("setText") && message.args[0].hasStrValue(identity)
				&& message.arity == 2) {
			text = message.args[1].strValue();
			// if (isRendering())
			firePropertyChange("textChanged", null, null);
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Raise move event.
	 *
	 * @param location the location
	 */
	public void raiseMoveEvent(Point location) {
		Message m = handler.newMessage("move", 3);
		Value v1 = new Value(getIdentity());
		Value v2 = new Value(location.x);
		Value v3 = new Value(location.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		handler.raiseEvent(m);
	}

	/**
	 * Sets the color.
	 *
	 * @param red the red
	 * @param green the green
	 * @param blue the blue
	 */
	public void setColor(int red, int green, int blue) {
		color = ModelFactory.getColor(red, green, blue);
		if (isRendering())
			firePropertyChange("color", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.model.DisplayWithPosition#synchronise(uk.ac.mdx.xmf.swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element edgeText) {
		text = edgeText.getString("text");
		position = edgeText.getString("attachedTo");
		editable = edgeText.getBoolean("editable");
		underline = edgeText.getBoolean("underline");
		truncate = edgeText.getInteger("truncate");
		font = edgeText.getString("font");
		super.synchronise(edgeText);
	}
}