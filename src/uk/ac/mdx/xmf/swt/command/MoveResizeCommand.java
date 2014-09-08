package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class MoveResizeCommand.
 */
public class MoveResizeCommand extends Command {
	
	/** The model. */
	Node model = null;
	
	/** The location. */
	Point location = null;
	
	/** The size. */
	Dimension size = null;

	/** The count. */
	public static int count = 0;

	/**
	 * Instantiates a new move resize command.
	 *
	 * @param model the model
	 * @param location the location
	 * @param size the size
	 */
	public MoveResizeCommand(Node model, Point location, Dimension size) {
		this.model = model;
		this.location = location;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		model.moveResize(location, size);
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