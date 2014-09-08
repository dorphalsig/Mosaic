package com.ceteva.modelBrowser.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

// TODO: Auto-generated Javadoc
/**
 * The Class ModelBrowserPreferencePage.
 */
public class ModelBrowserPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	/**
	 * Instantiates a new model browser preference page.
	 */
	public ModelBrowserPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		// IPreferenceStore store =
		// ModelBrowserPlugin.getDefault().getPreferenceStore();
		// setPreferenceStore(store);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		RadioGroupFieldEditor invokePropertyEditor = new RadioGroupFieldEditor(
				IPreferenceConstants.INVOKE_PROPERTY_EDITOR,
				"Action required to invoke property editor",
				1,
				new String[][] { { "Double click", "doubleClick" },
						{ "Single Click", "singleClick" }, { "None", "none" }, },
				getFieldEditorParent(), true);
		addField(invokePropertyEditor);

		RadioGroupFieldEditor invokeDiagramEditor = new RadioGroupFieldEditor(
				IPreferenceConstants.INVOKE_DIAGRAM_EDITOR,
				"Action required to view diagram (where appropriate)",
				1,
				new String[][] { { "Double click", "doubleClick" },
						{ "Single Click", "singleClick" }, { "None", "none" }, },
				getFieldEditorParent(), true);
		addField(invokeDiagramEditor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
}