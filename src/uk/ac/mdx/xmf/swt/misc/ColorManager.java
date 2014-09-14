package uk.ac.mdx.xmf.swt.misc;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

// TODO: Auto-generated Javadoc
/**
 * The Class ColorManager.
 */
public class ColorManager {
	
	// keys are the colour's RGB value
	
	/** The colorbinding. */
	private static Hashtable colorbinding = new Hashtable();
	
	/**
	 * Gets the color.
	 *
	 * @param rgb the rgb
	 * @return the color
	 */
	public static Color getColor(RGB rgb) {
	  if (rgb==null){
		  return ColorConstants.lightBlue;
	  }
	  if(colorbinding.containsKey(rgb))
	  	return (Color)colorbinding.get(rgb);
	  else {
	  	Color c = new Color(Display.getCurrent(),rgb);
	  	colorbinding.put(rgb,c);
	  	return c;
	  }
	}
	
	/**
	 * Dispose.
	 */
	public static void dispose() {
	  Enumeration e = colorbinding.elements();
	  while(e.hasMoreElements()) {
	  	Color f = (Color)e.nextElement();
	  	f.dispose();
	  }
	}
}