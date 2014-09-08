package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Edge;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class WaypointDeleteCommand.
 */
public class WaypointDeleteCommand extends org.eclipse.gef.commands.Command {

	/** The edge. */
	Edge edge = null;
	
	/** The identity. */
	String identity;
	
	/** The location. */
	Point location;

	/**
	 * Instantiates a new waypoint delete command.
	 *
	 * @param edge the edge
	 * @param identity the identity
	 * @param location the location
	 */
	public WaypointDeleteCommand(Edge edge, String identity, Point location) {
		this.edge = edge;
		this.identity = identity;
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		Message m = edge.handler.newMessage("deleteWaypoint", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		edge.setRefPoint(location);
		edge.handler.raiseEvent(m);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
	}
}