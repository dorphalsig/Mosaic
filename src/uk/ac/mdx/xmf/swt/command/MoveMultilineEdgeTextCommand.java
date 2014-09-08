package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.MultilineEdgeText;

// TODO: Auto-generated Javadoc
/**
 * The Class MoveMultilineEdgeTextCommand.
 */
public class MoveMultilineEdgeTextCommand extends Command {
	
	/** The model. */
	MultilineEdgeText model = null;
	
	/** The delta. */
	Point delta = null;
	
	/** The parent. */
	Figure parent = null;

	/**
	 * Instantiates a new move multiline edge text command.
	 *
	 * @param model the model
	 * @param parent the parent
	 * @param delta the delta
	 */
	public MoveMultilineEdgeTextCommand(MultilineEdgeText model, Figure parent,
			Point delta) {
		this.model = model;
		this.parent = parent;
		this.delta = delta;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		Point newLocation = model.getLocation().getCopy();
		parent.translateToAbsolute(newLocation);
		newLocation.translate(delta);
		parent.translateToRelative(newLocation);
		model.raiseMoveEvent(newLocation);
	}
}