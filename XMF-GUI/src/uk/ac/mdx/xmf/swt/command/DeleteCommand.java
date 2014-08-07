package uk.ac.mdx.xmf.swt.command;

import uk.ac.mdx.xmf.swt.model.CommandEvent;
import XOS.Message;
import XOS.Value;

public class DeleteCommand extends org.eclipse.gef.commands.Command {

	String identity = "";
	CommandEvent model = null;

	public DeleteCommand(String identity, CommandEvent model) {
		this.identity = identity;
		this.model = model;
	}

	@Override
	public void execute() {
		Message m = model.handler.newMessage("delete", 1);
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