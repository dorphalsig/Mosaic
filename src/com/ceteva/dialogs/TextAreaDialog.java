package com.ceteva.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class TextAreaDialog.
 */
public class TextAreaDialog extends MessageDialog {

	/** The display text. */
	String displayText;
	
	/** The background color. */
	static Color backgroundColor = Display.getCurrent().getSystemColor(
			SWT.COLOR_WHITE);
	
	/** The default font. */
	static FontData defaultFont = new FontData(
			"1|Courier New|9|0|WINDOWS|1|-13|0|0|0|400|0|0|0|0|3|2|1|49|Courier New");

	/**
	 * Instantiates a new text area dialog.
	 *
	 * @param parentShell the parent shell
	 * @param dialogTitle the dialog title
	 * @param dialogTitleImage the dialog title image
	 * @param message the message
	 * @param displayText the display text
	 * @param dialogImageType the dialog image type
	 * @param dialogButtonLabels the dialog button labels
	 * @param defaultIndex the default index
	 */
	public TextAreaDialog(Shell parentShell, String dialogTitle,
			Image dialogTitleImage, String message, String displayText,
			int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, message,
				dialogImageType, dialogButtonLabels, defaultIndex);
		this.displayText = displayText;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	public Point getInitialSize() {
		return new Point(450, 300);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createCustomArea(Composite parent) {
		Text text = new Text(parent, SWT.READ_ONLY | SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL);
		text.setFont(FontManager.getFont(defaultFont));
		text.setBackground(backgroundColor);
		text.setText(displayText);
		parent.setLayout(new FillLayout());
		return text;
	}
}