package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.Edge;

// TODO: Auto-generated Javadoc
/**
 * The Class MoveRefPointCommand.
 */
public class MoveRefPointCommand extends Command {
	
	/** The model. */
	Edge model = null;
	
	/** The location. */
	Point location = null;

	/**
	 * Instantiates a new move ref point command.
	 *
	 * @param model the model
	 * @param location the location
	 */
	public MoveRefPointCommand(Edge model, Point location) {
		this.model = model;
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		model.setRefPoint(location);
	}
}