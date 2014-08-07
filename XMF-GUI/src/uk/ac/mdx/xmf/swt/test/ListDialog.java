package uk.ac.mdx.xmf.swt.test;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class ListDialog {
	public static void main(String[] args) {
		Shell shell = new Shell(SWT.BORDER | SWT.SHELL_TRIM);
		Object[] allOptions = null;
		ListSelectionDialog lsd = new ListSelectionDialog(shell, new String[] {
				"Choice 1", "Choice 2", "Choice 3" },
				new ArrayContentProvider(), new LabelProvider(), "message");
		lsd.open();
	}
}
