package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class EllipseFigure.
 */
public class EllipseFigure extends Ellipse {
	
  /** The outline. */
  boolean outline;	
	
  /**
   * Instantiates a new ellipse figure.
   *
   * @param location the location
   * @param size the size
   * @param outline the outline
   */
  public EllipseFigure(Point location,Dimension size,boolean outline) {
  	setLocation(location);
    setSize(size);
    this.outline = outline;  	
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
   */
  protected boolean useLocalCoordinates() {
	return true;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.draw2d.Ellipse#outlineShape(org.eclipse.draw2d.Graphics)
   */
  protected void outlineShape(Graphics graphics){
    if(outline)
      super.outlineShape(graphics);	
  }	
}