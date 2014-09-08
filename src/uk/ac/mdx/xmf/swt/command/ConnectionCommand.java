package uk.ac.mdx.xmf.swt.command;

import org.eclipse.gef.commands.Command;

import uk.ac.mdx.xmf.swt.model.CommandEvent;
import uk.ac.mdx.xmf.swt.model.Edge;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionCommand.
 */
public class ConnectionCommand extends Command {
	
	/** The tool identity. */
	private String toolIdentity = "";
	
	/** The parent. */
	private CommandEvent parent = null;
	
	/** The edge. */
	private Edge edge = null;
	
	/** The source. */
	private String source = null;
	
	/** The target. */
	private String target = null;

	/**
	 * Instantiates a new connection command.
	 */
	public ConnectionCommand() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (edge == null) {
			Message m = parent.handler.newMessage("newEdge", 3);
			Value v1 = new Value(toolIdentity);
			Value v2 = new Value(source);
			Value v3 = new Value(target);
			m.args[0] = v1;
			m.args[1] = v2;
			m.args[2] = v3;
			parent.handler.raiseEvent(m);
		} else if (source != null && !edge.getSourcePort().equals(source)) {
			Message m = parent.handler.newMessage("edgeSourceReconnected", 2);
			Value v1 = new Value(edge.getIdentity());
			Value v2 = new Value(source);
			m.args[0] = v1;
			m.args[1] = v2;
			parent.handler.raiseEvent(m);
		} else if (target != null && !edge.getTargetPort().equals(target)) {
			Message m = parent.handler.newMessage("edgeTargetReconnected", 2);
			Value v1 = new Value(edge.getIdentity());
			Value v2 = new Value(target);
			m.args[0] = v1;
			m.args[1] = v2;
			parent.handler.raiseEvent(m);
		}
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Sets the target.
	 *
	 * @param target the new target
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Sets the edge.
	 *
	 * @param edge the new edge
	 */
	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the new parent
	 */
	public void setParent(CommandEvent parent) {
		this.parent = parent;
	}

	/**
	 * Sets the tool identity.
	 *
	 * @param toolIdentity the new tool identity
	 */
	public void setToolIdentity(String toolIdentity) {
		this.toolIdentity = toolIdentity;
	}
}