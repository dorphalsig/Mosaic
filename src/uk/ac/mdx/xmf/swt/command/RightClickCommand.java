package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.CommandEvent;
import xos.Message;
import xos.Value;

public class RightClickCommand extends Command {
	String identity = "";
	CommandEvent model = null;

	public RightClickCommand(CommandEvent model, String identity) {
		this.identity = identity;
		this.model = model;
	}

	@Override
	public void execute() {
		Message m = model.handler.newMessage("rightClickMenuSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		model.handler.raiseEvent(m);
	}

	@Override
	public void redo() {
	}

	@Override
	public void undo() {
	}
}