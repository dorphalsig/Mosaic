package com.ceteva.mosaic.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.test.ShowPrefs;

public class ShowPres extends Action {

	public ShowPres() {
		super("Preferences");
		this.setId("Preferences");
	}

	public void run() {
		new ShowPrefs().run();
	}

}
