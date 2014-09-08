package com.ceteva.mosaic.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.test.ShowPrefs;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowPres.
 */
public class ShowPres extends Action {

	/**
	 * Instantiates a new show pres.
	 */
	public ShowPres() {
		super("Preferences");
		this.setId("Preferences");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		new ShowPrefs().run();
	}

}
