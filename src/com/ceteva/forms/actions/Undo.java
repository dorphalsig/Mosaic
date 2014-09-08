package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IUndoManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Undo.
 */
public class Undo extends Action {

	/** The undo manager. */
	IUndoManager undoManager;
	
	/**
	 * Instantiates a new undo.
	 *
	 * @param undoManager the undo manager
	 */
	public Undo(IUndoManager undoManager) {
	  this.undoManager = undoManager;
	  this.setText("Undo");
	  this.setActionDefinitionId("org.eclipse.ui.edit.undo");
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
      undoManager.undo(); 
    }
	
}
