package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart;
import XOS.Message;
import XOS.Value;

public class CreateNodeCommand extends org.eclipse.gef.commands.Command {

	CommandEventEditPart parent;
	String toolIdentity;
	Point location;

	public CreateNodeCommand(CommandEventEditPart parent, String toolIdentity,
			Point location) {
		this.parent = parent;
		this.toolIdentity = toolIdentity;
		this.location = location;
	}

	@Override
	public void execute() {
		EventHandler handler = parent.getModel().handler;
		Message m = handler.newMessage("newNode", 4);
		Value v1 = new Value(toolIdentity);
		Value v2 = new Value(parent.getModelIdentity());
		Value v3 = new Value(location.x);
		Value v4 = new Value(location.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		m.args[3] = v4;
		handler.raiseEvent(m);
	}
}