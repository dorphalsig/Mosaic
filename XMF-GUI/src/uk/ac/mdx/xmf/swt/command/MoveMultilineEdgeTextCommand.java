package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.MultilineEdgeText;

public class MoveMultilineEdgeTextCommand extends Command {
	MultilineEdgeText model = null;
	Point delta = null;
	Figure parent = null;

	public MoveMultilineEdgeTextCommand(MultilineEdgeText model, Figure parent,
			Point delta) {
		this.model = model;
		this.parent = parent;
		this.delta = delta;
	}

	@Override
	public void execute() {
		Point newLocation = model.getLocation().getCopy();
		parent.translateToAbsolute(newLocation);
		newLocation.translate(delta);
		parent.translateToRelative(newLocation);
		model.raiseMoveEvent(newLocation);
	}
}