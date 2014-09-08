package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Edge;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateWaypointCommand.
 */
public class CreateWaypointCommand extends org.eclipse.gef.commands.Command {

	/** The edge. */
	Edge edge = null;
	
	/** The index. */
	int index;
	
	/** The location. */
	Point location;

	/**
	 * Instantiates a new creates the waypoint command.
	 *
	 * @param edge the edge
	 * @param index the index
	 * @param location the location
	 */
	public CreateWaypointCommand(Edge edge, int index, Point location) {
		this.edge = edge;
		this.index = index;
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		edge.addDummyWaypoint(index, location);
		Message m = edge.handler.newMessage("newWaypoint", 4);
		Value v1 = new Value(edge.getIdentity());
		Value v2 = new Value(index);
		Value v3 = new Value(location.x);
		Value v4 = new Value(location.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		m.args[3] = v4;
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