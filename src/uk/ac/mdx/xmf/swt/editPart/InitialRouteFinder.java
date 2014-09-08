package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

// TODO: Auto-generated Javadoc
/**
 * The Class InitialRouteFinder.
 */
public class InitialRouteFinder {
	
  /**
   * Instantiates a new initial route finder.
   */
  public InitialRouteFinder() {	
  }
  
  // takes the two rectangles and finds a mutually convenient
  // position for both ports
	
  /**
   * Calculate initial port.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @param start the start
   * @param end the end
   */
  public void calculateInitialPort(Rectangle r1,Rectangle r2,Point start,Point end) {
	int orientation = getOrientation(r2,r1);
	if(orientation == 1)		// north
	  initializeNorth(r1,r2,start,end);
	else if(orientation == 2) // south
	  initializeSouth(r1,r2,start,end);  		  
	else if(orientation == 3) // west
	  initalizeWest(r1,r2,start,end);
	else if(orientation == 4) // east
	  initalizeEast(r1,r2,start,end);	  
  }
  
  /**
   * Gets the orientation.
   *
   * @param centre the centre
   * @param periphery the periphery
   * @return the orientation
   */
  public int getOrientation(Rectangle centre,Rectangle periphery) {
	if((periphery.y + periphery.height) <= centre.y)
	  return 1; // north
	else if(periphery.y >= (centre.y + centre.height))
	  return 2; // south
	else if((periphery.x + periphery.width) < centre.x)
	  return 3; // east
	else if(periphery.x > (centre.x + centre.width))
	  return 4; // west
	return 0;
  }
	
  /**
   * Initialize north.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @param start the start
   * @param end the end
   */
  public void initializeNorth(Rectangle r1,Rectangle r2,Point start,Point end) {
	start.y = r1.y + r1.height;
	end.y = r2.y; 
	if(xIntersects(r1,r2)) {	
	  int intersection = getXIntersection(r1,r2);
	  start.x = intersection;
	  end.x = intersection;
	}
	else if((r1.x + r1.width) < r2.x) {
	  start.x = r1.x + r1.width;
	  end.x = r2.x; 
	}
	else {
	  start.x = r1.x;
	  end.x = r2.x + r2.width;
	}		
  }
	
  /**
   * Initialize south.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @param start the start
   * @param end the end
   */
  public void initializeSouth(Rectangle r1,Rectangle r2,Point start,Point end) {
	start.y = r1.y;
	end.y = r2.y + r2.height;
	if(xIntersects(r1,r2)) {
	  int intersection = getXIntersection(r1,r2);
	  start.x = intersection;
	  end.x = intersection;
	}
	else if((r1.x + r1.width) < r2.x) {
	  start.x = r1.x + r1.width;
	  end.x = r2.x; 
	}
	else {
	  start.x = r1.x;
	  end.x = r2.x + r2.width;
	}
  }
	
  /**
   * Initalize west.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @param start the start
   * @param end the end
   */
  public void initalizeWest(Rectangle r1,Rectangle r2,Point start,Point end) {
	start.x = r1.x + r1.width;
	end.x = r2.x;
	if(yIntersects(r1,r2)) {	    	
	  int intersection = getYIntersection(r1,r2);
	  start.y = intersection;
	  end.y = intersection;
	}	    	
  }
	
  /**
   * Initalize east.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @param start the start
   * @param end the end
   */
  public void initalizeEast(Rectangle r1,Rectangle r2,Point start,Point end) {
	start.x = r1.x;
	end.x = r2.x + r2.width;
	if(yIntersects(r1,r2)) {	    	
	  int intersection = getYIntersection(r1,r2);
	  start.y = intersection;
	  end.y = intersection;
	}	    	
  }
	
  /**
   * X intersects.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @return true, if successful
   */
  public boolean xIntersects(Rectangle r1,Rectangle r2) {
	if(getXIntersection(r1,r2)!=-1)
	  return true;
	else return false;	
  }
	
  /**
   * Y intersects.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @return true, if successful
   */
  public boolean yIntersects(Rectangle r1,Rectangle r2) {
	if(getYIntersection(r1,r2)!=-1)
	  return true;
	else return false;	
  }
	
  /**
   * Gets the x intersection.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @return the x intersection
   */
  public int getXIntersection(Rectangle r1,Rectangle r2) {
	Rectangle r1copy = r1.getCopy();
	Rectangle r2copy = r2.getCopy();
	r1copy.y = r2copy.y;
	r1copy.intersect(r2copy);
	if(!r1copy.getLocation().equals(new Point(0,0)) && !r1copy.getSize().equals(new Dimension(0,0)))
	  return r1copy.getCenter().x;
	return -1;  	
  }
	
  /**
   * Gets the y intersection.
   *
   * @param r1 the r1
   * @param r2 the r2
   * @return the y intersection
   */
  public int getYIntersection(Rectangle r1,Rectangle r2) {
	Rectangle r1copy = r1.getCopy();
	Rectangle r2copy = r2.getCopy();
	r1copy.x = r2copy.x;
	r1copy.intersect(r2copy);
	if(!r1copy.getLocation().equals(new Point(0,0)) && !r1copy.getSize().equals(new Dimension(0,0)))
	  return r1copy.getCenter().y;
	return -1;	
  }
  
  /**
   * Absolute to local.
   *
   * @param p the p
   * @param port the port
   * @return the point
   */
  public Point absoluteToLocal(Point p,Figure port) {
	port.getParent().translateToRelative(p);
	Rectangle r = port.getBounds().getCopy();
	Rectangle portParent = port.getParent().getBounds();
	r.translate(portParent.getLocation());
	p.translate(r.getLocation().negate());
	return p;
  }
}