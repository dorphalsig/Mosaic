package uk.ac.mdx.xmf.swt.command;

import org.eclipse.draw2d.geometry.Point;

import uk.ac.mdx.xmf.swt.model.AbstractDiagram;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class DropCommand.
 */
public class DropCommand extends org.eclipse.gef.commands.Command {

	/** The type. */
	String type;
	
	/** The source. */
	String source;
	
	/** The diagram. */
	AbstractDiagram diagram;
	
	/** The location. */
	Point location;

	/**
	 * Instantiates a new drop command.
	 *
	 * @param type the type
	 * @param source the source
	 * @param diagram the diagram
	 * @param location the location
	 */
	public DropCommand(String type, String source, AbstractDiagram diagram,
			Point location) {
		this.type = type;
		this.source = source;
		this.diagram = diagram;
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		// System.out.println("Drop Command(" + type + "," + source + "," +
		// diagram.identity + "," + location + ")");
		Message m = diagram.handler.newMessage("dragAndDrop", 5);
		Value v1 = new Value(diagram.getIdentity());
		Value v2 = new Value(type);
		Value v3 = new Value(source);
		Value v4 = new Value(location.x);
		Value v5 = new Value(location.y);
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		m.args[3] = v4;
		m.args[4] = v5;
		diagram.handler.raiseEvent(m);
	}
}
