package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.MultilineText;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineTextChangeCommand.
 */
public class MultilineTextChangeCommand extends Command {

	/** The new name. */
	private String newName;
	
	/** The text. */
	private final MultilineText text;

	/**
	 * Instantiates a new multiline text change command.
	 *
	 * @param text the text
	 * @param string the string
	 */
	public MultilineTextChangeCommand(MultilineText text, String string) {
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