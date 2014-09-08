package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Edge;

// TODO: Auto-generated Javadoc
/**
 * The Class SetRefPointCommand.
 */
public class SetRefPointCommand extends org.eclipse.gef.commands.Command {

	/** The edge. */
	Edge edge;
	
	/** The position. */
	Point position;

	/**
	 * Instantiates a new sets the ref point command.
	 *
	 * @param edge the edge
	 * @param position the position
	 */
	public SetRefPointCommand(Edge edge, Point position) {
		this.edge = edge;
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		edge.setRefPoint(position);
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
