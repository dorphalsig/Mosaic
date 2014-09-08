package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;

import uk.ac.mdx.xmf.swt.client.IconManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ClearForms.
 */
public class ClearForms extends Action {

	/** The forms. */
	CTabFolder forms;

	/**
	 * Instantiates a new clear forms.
	 *
	 * @param forms the forms
	 */
	public ClearForms(CTabFolder forms) {
		setId("com.ceteva.forms.actions.ClearForms");
		setText("Clear Forms");
		setToolTipText("Clear all open forms");
		setImageDescriptor(IconManager
				.getImageDescriptorAbsolute("icons/Clear.gif"));
		this.forms = forms;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		/*
		 * if(forms != null) { CTabItem[] fs = forms.getItems(); for(int
		 * i=0;i<fs.length;i++) { Form f = (Form)fs[i]; if(f.changesPending()) {
		 * if(f.closeForm()) { f.formClosed(); f.dispose(); } } else {
		 * f.formClosed(); f.dispose(); } } }
		 */
	}

	/**
	 * Update.
	 */
	public void update() {
		this.setEnabled(forms.getItemCount() > 0);
	}
}