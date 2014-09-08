package uk.ac.mdx.xmf.swt.dnd;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;

// TODO: Auto-generated Javadoc
/**
 * The Class DropRequest.
 */
public class DropRequest extends Request {
	
	/** The Constant ID. */
	public static final String ID = "Drop Request";
	
	/** The Constant COPY. */
	public static final String COPY = "copy";
	
	/** The Constant MOVE. */
	public static final String MOVE = "move";
	
	/** The source. */
	private String source;
	
	/** The location. */
	private Point location;
	
	/** The type. */
	private String type;
	
	/**
	 * Instantiates a new drop request.
	 *
	 * @param source the source
	 * @param type the type
	 */
	public DropRequest(String source,String type) {
		super(ID);
		this.source = source;
		this.type = type;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Gets the drop type.
	 *
	 * @return the drop type
	 */
	public String getDropType() {
		return type;
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

}