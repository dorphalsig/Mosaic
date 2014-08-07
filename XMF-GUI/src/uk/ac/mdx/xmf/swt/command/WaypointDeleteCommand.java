package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Edge;
import XOS.Message;
import XOS.Value;

public class WaypointDeleteCommand extends org.eclipse.gef.commands.Command {

	Edge edge = null;
	String identity;
	Point location;

	public WaypointDeleteCommand(Edge edge, String identity, Point location) {
		this.edge = edge;
		this.identity = identity;
		this.location = location;
	}

	@Override
	public void execute() {
		Message m = edge.handler.newMessage("deleteWaypoint", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		edge.setRefPoint(location);
		edge.handler.raiseEvent(m);
	}

	@Override
	public void redo() {
	}

	@Override
	public void undo() {
	}
}