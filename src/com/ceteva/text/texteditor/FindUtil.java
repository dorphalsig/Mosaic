package com.ceteva.text.texteditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FindUtil extends TrayDialog {
	protected FindUtil(Shell shell) {
		super(shell);
		this.shell = shell;
		// TODO Auto-generated constructor stub
	}

	static String selectedText = "initial xxx";
	static StyledText keywordText;
	static String keyword;
	static Button doFind;
	static Shell shell;

	public String show(final StyledText styledText) {
		final Shell dialog = new Shell(shell);
		dialog.setText("Find Text");
		createContents(dialog);
		// dialog.pack();
		// dialog.open();
		dialog.setDefaultButton(doFind);

		dialog.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				// don't dispose of the shell, just hide it for later use
				e.doit = false;
				dialog.setVisible(false);
			}
		});
		Display display = dialog.getDisplay();

		keywordText = styledText;
		// keywordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// Font font = new Font(shell.getDisplay(), "Courier New", 12,
		// SWT.NORMAL);

		doFind = new Button(dialog, SWT.PUSH);
		doFind.setText("Find");

		StyleRange style3 = new StyleRange();
		style3.start = 0;
		style3.length = 3;
		style3.underline = true;
		style3.strikeout = true;
		style3 = getHighlightStyle(0, 10, shell);
		styledText.setStyleRange(style3);

		// JavaScanner.setKeywords("Diagrams", null);
		// JavaScanner.initialize();

		// styledText.setLineBackground(0, 1,
		// shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		// styledText.setLineBackground(1, 1,
		// shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

		styledText.redraw();

		// styledText.addLineStyleListener(new LineStyleListener() {
		// public void lineGetStyle(LineStyleEvent event) {
		// if (keyword == null || keyword.length() == 0) {
		// event.styles = new StyleRange[0];
		// return;
		// }
		//
		// String line = event.lineText;
		// int cursor = -1;
		//
		// LinkedList list = new LinkedList();
		// while ((cursor = line.indexOf(keyword, cursor + 1)) >= 0) {
		// list.add(getHighlightStyle(event.lineOffset + cursor,
		// keyword.length(), dialog));
		// System.out.println("cursor = " + cursor + " " + keyword);
		// }
		//
		// event.styles = (StyleRange[]) list.toArray(new StyleRange[list
		// .size()]);
		// }
		// });

		open();

		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return selectedText;
		/*
		 * while (!dialog.isDisposed() ) { if (!dialog.readAndDispatch() ) {
		 * display } }
		 */
	}

	private static StyleRange getHighlightStyle(int startOffset, int length,
			Shell shell) {
		StyleRange styleRange = new StyleRange();
		styleRange.start = startOffset;
		styleRange.length = length;
		styleRange.background = shell.getDisplay().getSystemColor(
				SWT.COLOR_YELLOW);
		return styleRange;
	}

	/**
	 * Creates the dialog's contents.
	 * 
	 * @param parent
	 *            the parent
	 * @return the control
	 */

	/** The scroll. */
	private ScrollBar scroll;
	// The find and replace buttons

	/** The do replace. */
	private static Button doReplace;

	/** The do replace find. */
	private static Button doReplaceFind;
	/** The changable component list. */
	private static List<Control> changableComponentList = new ArrayList<Control>();

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		composite.setLayout(new GridLayout(2, false));
		// Add the text input fields
		Composite text = new Composite(composite, SWT.NONE);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setLayout(new GridLayout(3, true));
		changableComponentList.add(text);

		new Label(text, SWT.LEFT).setText("&Find:");
		final Text findText = new Text(text, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		findText.setLayoutData(data);

		new Label(text, SWT.LEFT).setText("R&eplace With:");
		final Text replaceText = new Text(text, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		replaceText.setLayoutData(data);

		// Add the match case checkbox
		final Button match = new Button(text, SWT.CHECK);
		match.setText("&Match Case");

		// Add the whole word checkbox
		final Button wholeWord = new Button(text, SWT.CHECK);
		wholeWord.setText("&Whole Word");

		// Add the regular expression checkbox
		final Button regexp = new Button(text, SWT.CHECK);
		regexp.setText("RegE&xp");

		// Add the direction radio buttons
		final Button down = new Button(text, SWT.RADIO);
		down.setText("D&own");

		final Button up = new Button(text, SWT.RADIO);
		up.setText("&Up");
		// wrap....
		final Button wrap = new Button(text, SWT.CHECK);
		wrap.setText("Wrap the &Search");

		// Add the buttons
		Composite buttons = new Composite(composite, SWT.NONE);
		buttons.setLayout(new GridLayout());

		// Create the Find button
		doFind = new Button(buttons, SWT.PUSH);
		doFind.setText("Fi&nd");
		doFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// Set the initial find operation to FIND_FIRST
		// doFind.setData(FindReplaceOperationCode.FIND_FIRST);
		// Create the Replace button
		doReplace = new Button(buttons, SWT.PUSH);
		doReplace.setText("&Replace");
		doReplace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create the Replace/Find button
		doReplaceFind = new Button(buttons, SWT.PUSH);
		doReplaceFind.setText("Replace/Fin&d");
		doReplaceFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		doReplaceFind.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				System.out.println("it found selection");
				doReplace(replaceText.getText());
				// doFind(findText.getText(), down.getSelection(), match
				// .getSelection(), wholeWord.getSelection(), regexp
				// .getSelection(),wrap.getSelection());
			}
		});

		// Create the Close button
		Button close = new Button(buttons, SWT.PUSH);
		close.setText("Close");
		close.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		close.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// close();
			}
		});

		// Disable the replace button when find text is modified
		findText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				enableReplaceButtons(false);
			}
		});

		// Do a find
		doFind.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				// doFind(findText.getText(), down.getSelection(), match
				// .getSelection(), wholeWord.getSelection(), regexp
				// .getSelection(), wrap.getSelection());
				keyword = findText.getText();
				keywordText.redraw();
				System.out.println(keyword);
			}
		});

		// Replace loses "find" state, so disable buttons
		doReplace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				doReplace(replaceText.getText());
				enableReplaceButtons(false);
			}
		});

		// adding components to be changed
		changableComponentList.add(text);
		changableComponentList.add(doFind);
		changableComponentList.add(doReplace);
		changableComponentList.add(doReplaceFind);
		changableComponentList.add(findText);

		// Set defaults
		down.setSelection(true);
		findText.setFocus();
		enableReplaceButtons(false);
		// setDefaultButton(doFind);
		return composite;
	}

	/**
	 * Performs a replace.
	 * 
	 * @param replaceText
	 *            the replacement text
	 */
	protected static void doReplace(String replaceText) {
		System.out.println("replace::");
	}

	/**
	 * Enables/disables the Replace and Replace/Find buttons.
	 * 
	 * @param enable
	 *            whether to enable or disable
	 */
	protected static void enableReplaceButtons(boolean enable) {
		doReplace.setEnabled(enable);
		doReplaceFind.setEnabled(enable);
	}

	/**
	 * Shows an error.
	 * 
	 * @param message
	 *            the error message
	 */
	protected static void showError(String message) {
		MessageDialog.openError(shell, "Error", message);
	}
}
