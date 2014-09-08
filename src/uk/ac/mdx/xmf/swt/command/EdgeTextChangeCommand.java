package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.EdgeText;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeTextChangeCommand.
 */
public class EdgeTextChangeCommand extends Command {

	/** The new name. */
	private String newName;
	
	/** The text. */
	private final EdgeText text;

	/**
	 * Instantiates a new edge text change command.
	 *
	 * @param text the text
	 * @param string the string
	 */
	public EdgeTextChangeCommand(EdgeText text, String string) {
		this.text = text;
		if (string != null)
			newName = string;
		else
			newName = ""; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		text.changeText(newName);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
	}
}