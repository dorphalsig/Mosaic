package com.ceteva.text.highlighting;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.graphics.Color;

import com.ceteva.text.TextPlugin;

// TODO: Auto-generated Javadoc
/**
 * The Class ScannerTokens.
 */
public class ScannerTokens {
	
  /** The red token. */
  private static IToken redToken = new Token(new TextAttribute(TextPlugin.RED));
  
  /** The green token. */
  private static IToken greenToken = new Token(new TextAttribute(TextPlugin.GREEN));
  
  /** The blue token. */
  private static IToken blueToken = new Token(new TextAttribute(TextPlugin.BLUE));
  
  /** The default token. */
  private static IToken defaultToken = new Token(new TextAttribute(TextPlugin.BLACK));

  /**
   * Gets the token.
   *
   * @param color the color
   * @return the token
   */
  public static IToken getToken(String color) {
    if(color.equals("red"))
      return redToken;
    else if(color.equals("green"))
      return greenToken;
    return blueToken;
  }
  
  /**
   * Gets the colour.
   *
   * @param color the color
   * @return the colour
   */
  public static Color getColour(String color) {
  	if(color.equals("red"))
      return TextPlugin.RED;
    else if(color.equals("green"))
      return TextPlugin.GREEN;
    return TextPlugin.BLUE;
  }
  
  /**
   * Gets the default token.
   *
   * @return the default token
   */
  public static IToken getDefaultToken() {
  	return defaultToken;
  }
}