package uk.ac.mdx.xmf.swt.diagram.tracker;

import org.eclipse.gef.tools.MarqueeDragTracker;

public class SelectionTracker extends MarqueeDragTracker {

	public SelectionTracker() {
	  this.setMarqueeBehavior(BEHAVIOR_NODES_AND_CONNECTIONS);	
	}
	
}