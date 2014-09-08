package com.ceteva.forms.actions;

import org.eclipse.jface.action.Action;

import uk.ac.mdx.xmf.swt.client.IconManager;

import com.ceteva.forms.views.FormView;

// TODO: Auto-generated Javadoc
/**
 * The Class LockForm.
 */
public class LockForm extends Action {

	/** The form. */
	FormView form = null;

	/**
	 * Instantiates a new lock form.
	 *
	 * @param form the form
	 */
	public LockForm(FormView form) {
		setId("com.ceteva.forms.actions.LockForm");
		this.form = form;
		update();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		form.toggleLock();
	}

	/**
	 * Update.
	 */
	public void update() {
		if (form != null) {
			if (form.isLocked()) {
				setText("Unlock Form");
				setToolTipText("Form is currently locked. Click to unlock.");
				setImageDescriptor(IconManager
						.getImageDescriptorAbsolute("icons/Locked.gif"));
			} else {
				setText("Lock Form");
				setToolTipText("Form is currently unlocked. Click to lock.");
				setImageDescriptor(IconManager
						.getImageDescriptorAbsolute("icons/Unlocked.gif"));
			}
			this.setEnabled(true);
		} else
			this.setEnabled(false);
		setChecked(form.isLocked());
	}
}