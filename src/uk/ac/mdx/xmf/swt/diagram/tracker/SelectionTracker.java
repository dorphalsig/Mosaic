package uk.ac.mdx.xmf.swt.diagram.tracker;

import org.eclipse.gef.tools.MarqueeDragTracker;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectionTracker.
 */
public class SelectionTracker extends MarqueeDragTracker {

	/**
	 * Instantiates a new selection tracker.
	 */
	public SelectionTracker() {
	  this.setMarqueeBehavior(BEHAVIOR_NODES_AND_CONNECTIONS);	
	}
	
}