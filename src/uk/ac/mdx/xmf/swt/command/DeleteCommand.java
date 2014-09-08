package uk.ac.mdx.xmf.swt.command;

import uk.ac.mdx.xmf.swt.model.CommandEvent;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class DeleteCommand.
 */
public class DeleteCommand extends org.eclipse.gef.commands.Command {

	/** The identity. */
	String identity = "";
	
	/** The model. */
	CommandEvent model = null;

	/**
	 * Instantiates a new delete command.
	 *
	 * @param identity the identity
	 * @param model the model
	 */
	public DeleteCommand(String identity, CommandEvent model) {
		this.identity = identity;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		Message m = model.handler.newMessage("delete", 1);
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