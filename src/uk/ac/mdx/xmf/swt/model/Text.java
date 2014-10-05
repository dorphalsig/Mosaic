package uk.ac.mdx.xmf.swt.model;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Text.
 */
public class Text extends DisplayWithPosition {

  /** The text string. */
  private String        textString;  // the text to be displayed

  /** The editable. */
  private final boolean editable;    // denotes whether the text can be
                                      // edited

  /** The underline. */
  private boolean       underline;

  /** The italicise. */
  private boolean       italicise;

  /** The edit. */
  private boolean       edit = false;

  /** The color. */
  private RGB           color;

  /** The font. */
  private String        font = "";

  /**
   * Instantiates a new text.
   *
   * @param parent
   *          the parent
   * @param handler
   *          the handler
   * @param identity
   *          the identity
   * @param location
   *          the location
   * @param textString
   *          the text string
   * @param editable
   *          the editable
   * @param underline
   *          the underline
   * @param italicise
   *          the italicise
   * @param color
   *          the color
   * @param font
   *          the font
   */
  public Text(ClientElement parent, EventHandler handler, String identity, Point location, String textString, boolean editable,
      boolean underline, boolean italicise, RGB color, String font) {
    super(parent, handler, identity, location);
    this.textString = textString;
    this.editable = editable;
    this.underline = underline;
    this.italicise = italicise;
    this.color = color;
    this.font = font;
  }

  /**
   * Instantiates a new text.
   *
   * @param parent
   *          the parent
   * @param handler
   *          the handler
   * @param identity
   *          the identity
   * @param x
   *          the x
   * @param y
   *          the y
   * @param textString
   *          the text string
   * @param editable
   *          the editable
   * @param underline
   *          the underline
   * @param italicise
   *          the italicise
   * @param color
   *          the color
   * @param font
   *          the font
   */
  public Text(ClientElement parent, EventHandler handler, String identity, int x, int y, String textString, boolean editable,
      boolean underline, boolean italicise, RGB color, String font) {
    this(parent, handler, identity, new Point(x, y), textString, editable, underline, italicise, color, font);
  }

  /**
   * Instantiates a new text.
   *
   * @param parent
   *          the parent
   * @param handler
   *          the handler
   * @param identity
   *          the identity
   * @param x
   *          the x
   * @param y
   *          the y
   * @param textString
   *          the text string
   * @param editable
   *          the editable
   * @param underline
   *          the underline
   * @param italicise
   *          the italicise
   * @param font
   *          the font
   */
  public Text(ClientElement parent, EventHandler handler, String identity, int x, int y, String textString, boolean editable,
      boolean underline, boolean italicise, String font) {
    this(parent, handler, identity, new Point(x, y), textString, editable, underline, italicise, null, font);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
   */
  @Override
  public void delete() {
    super.delete();
    ((Container) parent).removeDisplay(this);
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
    return textString;
  }

  /**
   * Change text.
   *
   * @param text
   *          the text
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
   * Edits the.
   *
   * @return true, if successful
   */
  public boolean edit() {
    return edit;
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
   * Gets the italicise.
   *
   * @return the italicise
   */
  public boolean getItalicise() {
    return italicise;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.ac.mdx.xmf.swt.model.DisplayWithPosition#processMessage(xos.Message)
   */
  @Override
  public boolean processMessage(Message message) {
    if (message.hasName("editText") && message.args[0].hasStrValue(identity) && message.arity == 1) {

      // the edit may happen before the text is constructed. In which case
      // store the edit and the text's editpart will check after it has
      // constructed

      if (canFire())
        firePropertyChange("editText", null, null);
      else
        edit = true;
    }
    if (message.hasName("setText") && message.args[0].hasStrValue(identity) && message.arity == 2) {
      textString = message.args[1].strValue();
      // if (isRendering())
      firePropertyChange("textChanged", null, null);
      return true;
    }
    if (message.hasName("setFont") && message.args[0].hasStrValue(identity) && message.arity == 2) {
      font = message.args[1].strValue();
      if (isRendering())
        firePropertyChange("textChanged", null, null);
      return true;
    }
    if (message.hasName("underline") && message.args[0].hasStrValue(identity) && message.arity == 2) {
      underline = message.args[1].boolValue;
      if (isRendering())
        firePropertyChange("textChanged", null, null);
      return true;
    }
    if (message.hasName("italicise") && message.args[0].hasStrValue(identity) && message.arity == 2) {
      italicise = message.args[1].boolValue;
      if (isRendering())
        firePropertyChange("textChanged", null, null);
      return true;
    }
    if (message.hasName("setColor") && message.args[0].hasStrValue(identity) && message.arity == 4) {
      int red = message.args[1].intValue;
      int green = message.args[2].intValue;
      int blue = message.args[3].intValue;
      setColor(red, green, blue);
      return true;
    }
    return super.processMessage(message);
  }

  /**
   * Sets the edits the.
   *
   * @param edit
   *          the new edits the
   */
  public void setEdit(boolean edit) {
    this.edit = edit;
  }

  /**
   * Sets the color.
   *
   * @param red
   *          the red
   * @param green
   *          the green
   * @param blue
   *          the blue
   */
  public void setColor(int red, int green, int blue) {
    color = ModelFactory.getColor(red, green, blue);
    if (isRendering())
      firePropertyChange("color", null, null);
  }
}