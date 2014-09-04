package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.Edge;

public class SetRefPointCommand extends org.eclipse.gef.commands.Command {

	Edge edge;
	Point position;

	public SetRefPointCommand(Edge edge, Point position) {
		this.edge = edge;
		this.position = position;
	}

	@Override
	public void execute() {
		edge.setRefPoint(position);
	}

	@Override
	public void redo() {
	}

	@Override
	public void undo() {
	}
}
