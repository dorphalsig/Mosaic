package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.CommandEvent;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class RightClickCommand.
 */
public class RightClickCommand extends Command {
	
	/** The identity. */
	String identity = "";
	
	/** The model. */
	CommandEvent model = null;

	/**
	 * Instantiates a new right click command.
	 *
	 * @param model the model
	 * @param identity the identity
	 */
	public RightClickCommand(CommandEvent model, String identity) {
		this.identity = identity;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		Message m = model.handler.newMessage("rightClickMenuSelected", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		model.handler.raiseEvent(m);
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