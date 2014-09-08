package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IUndoManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Redo.
 */
public class Redo extends Action {

	/** The undo manager. */
	IUndoManager undoManager;
	
	/**
	 * Instantiates a new redo.
	 *
	 * @param undoManager the undo manager
	 */
	public Redo(IUndoManager undoManager) {
	  this.undoManager = undoManager;
	  this.setText("Redo");
	  this.setActionDefinitionId("org.eclipse.ui.edit.redo");
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
      undoManager.redo(); 
    }
	
}
