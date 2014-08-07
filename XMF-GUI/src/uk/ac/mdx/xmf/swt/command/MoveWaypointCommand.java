package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.client.IdManager;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.Waypoint;
import XOS.Message;
import XOS.Value;

public class MoveWaypointCommand extends Command {

	private Edge edge = null;
	private final int index;
	private final Point newLocation;

	public MoveWaypointCommand(Edge edge, int index, Point newLocation) {
		this.edge = edge;
		this.index = index;
		this.newLocation = newLocation;
	}

	@Override
	public void execute() {
		String waypointID = edge.getWaypointIdentity(index);
		Message m = edge.handler.newMessage("move", 3);
		Value v1 = new Value(waypointID);
		Value v2 = new Value(newLocation.x);
		Value v3 = new Value(newLocation.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		Waypoint waypoint = (Waypoint) IdManager.get(waypointID);
		waypoint.move(new Point(newLocation.x, newLocation.y));
		edge.handler.raiseEvent(m);
	}
}