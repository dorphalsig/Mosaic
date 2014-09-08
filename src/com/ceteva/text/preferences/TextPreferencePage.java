package com.ceteva.text.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

// TODO: Auto-generated Javadoc
/**
 * The Class TextPreferencePage.
 */
public class TextPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * Instantiates a new text preference page.
	 */
	public TextPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		// IPreferenceStore store =
		// TextPlugin.getDefault().getPreferenceStore();
		// setPreferenceStore(store);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		FontFieldEditor font = new FontFieldEditor(JFaceResources.TEXT_FONT,
				"Editor text font", getFieldEditorParent());
		addField(font);

		BooleanFieldEditor linenumbers = new BooleanFieldEditor(
				IPreferenceConstants.LINE_NUMBERS, "Show line numbers",
				getFieldEditorParent());
		addField(linenumbers);

		ColorFieldEditor currentlinecolor = new ColorFieldEditor(
				IPreferenceConstants.CURRENT_LINE_COLOR, "Current line colour",
				getFieldEditorParent());
		addField(currentlinecolor);

		ColorFieldEditor highlightlinecolor = new ColorFieldEditor(
				IPreferenceConstants.HIGHLIGHT_LINE_COLOR,
				"Highlight line color", getFieldEditorParent());
		addField(highlightlinecolor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}