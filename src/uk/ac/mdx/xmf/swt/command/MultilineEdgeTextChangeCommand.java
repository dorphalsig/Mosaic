package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.MultilineEdgeText;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineEdgeTextChangeCommand.
 */
public class MultilineEdgeTextChangeCommand extends Command {

	/** The new name. */
	private String newName;
	
	/** The text. */
	private final MultilineEdgeText text;

	/**
	 * Instantiates a new multiline edge text change command.
	 *
	 * @param text the text
	 * @param string the string
	 */
	public MultilineEdgeTextChangeCommand(MultilineEdgeText text, String string) {
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