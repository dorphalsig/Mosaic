package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

// TODO: Auto-generated Javadoc
/**
 * The Class FixedAnchor.
 */
public class FixedAnchor extends AbstractConnectionAnchor {

  /** The border size. */
  private int borderSize = 20;
  
  /** The identity. */
  private String identity;
  
  /**
   * Instantiates a new fixed anchor.
   *
   * @param owner the owner
   * @param identity the identity
   */
  public FixedAnchor(IFigure owner,String identity) {
	super(owner);
	this.identity = identity;
  }

  /**
   * Contains point.
   *
   * @param point the point
   * @return true, if successful
   */
  public boolean containsPoint(Point point) {
	Figure f = (Figure)getOwner();
	NodeFigure node = (NodeFigure)f.getParent();
	
	Rectangle r = getOwner().getBounds().getCopy();
	r.translate(getOwner().getParent().getBounds().getLocation());
	getOwner().getParent().translateToAbsolute(r);
	
	if(node.containsGroupFigure()) {
	  return 
	     ((point.x > r.x && point.x < r.x + borderSize) || (point.x < (r.x + r.width) && point.x > (r.x + r.width - borderSize))) ||
	     ((point.y > r.y && point.y < r.y + borderSize) || (point.y < (r.y + r.height) && point.y > (r.y + r.height - borderSize)));
	}
	return r.contains(point);
  }

  /* (non-Javadoc)
   * @see org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry.Point)
   */
  public Point getLocation(Point reference) {
	if(reference!=null)
	  return chopBoxStyle(reference);
	else return null;
  }
  
  /**
   * Chop box style.
   *
   * @param reference the reference
   * @return the point
   */
  public Point chopBoxStyle(Point reference) {
	  Rectangle r = Rectangle.SINGLETON;
	  r.setBounds(getBox());
	  r.translate(-1, -1);
	  r.resize(1, 1);

	  getOwner().getParent().translateToAbsolute(r);
	  float centerX = (float)r.x + 0.5f * (float)r.width;
	  float centerY = (float)r.y + 0.5f * (float)r.height;
    
	  if (r.isEmpty() || (reference.x == (int)centerX && reference.y == (int)centerY))
		  return new Point((int)centerX, (int)centerY);  //This avoids divide-by-zero

	  float dx = (float)reference.x - centerX;
	  float dy = (float)reference.y - centerY;
    
	  //r.width, r.height, dx, and dy are guaranteed to be non-zero. 
	  float scale = 0.5f / Math.max(Math.abs(dx) / r.width, Math.abs(dy) / r.height);

	  dx *= scale;
	  dy *= scale;
	  centerX += dx;
	  centerY += dy;
	  return new Point(Math.round(centerX), Math.round(centerY));
  }
  
  /**
   * Gets the box.
   *
   * @return the box
   */
  protected Rectangle getBox() {
	Rectangle r = getOwner().getBounds().getCopy();
	r.translate(getOwner().getParent().getBounds().getLocation());
	return r;
  }

  /* (non-Javadoc)
   * @see org.eclipse.draw2d.AbstractConnectionAnchor#getReferencePoint()
   */
  public Point getReferencePoint() {
	Rectangle r = getBox();
	getOwner().getParent().translateToAbsolute(r);
	return r.getCenter();
  }
  
  /**
   * Gets the reference rectangle.
   *
   * @return the reference rectangle
   */
  public Rectangle getReferenceRectangle() {
    Rectangle r = getBox();
    getOwner().getParent().translateToAbsolute(r);
	return r;
  }
  
  /**
   * Gets the identity.
   *
   * @return the identity
   */
  public String getIdentity() {
  	return identity;
  }
}