package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.EdgeText;

public class EdgeTextChangeCommand extends Command {

	private String newName;
	private final EdgeText text;

	public EdgeTextChangeCommand(EdgeText text, String string) {
		this.text = text;
		if (string != null)
			newName = string;
		else
			newName = ""; //$NON-NLS-1$
	}

	@Override
	public void execute() {
		text.changeText(newName);
	}

	@Override
	public void undo() {
	}
}