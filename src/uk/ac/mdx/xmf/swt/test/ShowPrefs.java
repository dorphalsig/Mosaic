package uk.ac.mdx.xmf.swt.test;

import java.io.IOException;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.demo.Main;

import com.ceteva.console.preferences.IPreferenceConstants;

// TODO: Auto-generated Javadoc
/**
 * This class demonstrates JFace preferences.
 */
public class ShowPrefs {
	
	/**
	 * Runs the application.
	 */
	public void run() {
		Display display = Main.display;

		// Create the preference manager
		PreferenceManager mgr = new PreferenceManager();

		// Create the nodes
		// PreferenceNode one = new PreferenceNode("one", "One",
		// ImageDescriptor.createFromFile(PrefPageOne.class, "Tree.gif"),
		// "PrefPageOne");
		PreferenceNode one = new PreferenceNode("one", new PrefPageOne());
		PreferenceNode two = new PreferenceNode("Console", new PrefPageTwo());

		// Add the nodes
		mgr.addToRoot(one);
		mgr.addTo(one.getId(), two);

		// Create the preferences dialog
		PreferenceDialog dlg = new PreferenceDialog(null, mgr);

		// Set the preference store
		PreferenceStore ps = new PreferenceStore("showprefs.properties");
		try {
			ps.load();
		} catch (IOException e) {
			// Ignore
		}
		dlg.setPreferenceStore(ps);

		// Open the dialog
		dlg.open();

		try {
			// Save the preferences
			ps.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// display.dispose();
	}

	/**
	 * The application entry point.
	 *
	 * @param args            the command line arguments
	 */
	public static void main(String[] args) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				new ShowPrefs().run();
			}
		});

	}
}

/**
 * This class creates a preference page
 */
class PrefPageOne extends PreferencePage {
	// Names for preferences
	private static final String ONE = "one.one";
	private static final String TWO = "one.two";
	private static final String THREE = "one.three";

	// Text fields for user to enter preferences
	private Text fieldOne;
	private Text fieldTwo;
	private Text fieldThree;

	/**
	 * Creates the controls for this page
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Create three text fields.
		// Set the text in each from the preference store
		// new Label(composite, SWT.LEFT).setText("Field One:");
		// fieldOne = new Text(composite, SWT.BORDER);
		// fieldOne.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// fieldOne.setText(preferenceStore.getString(ONE));
		//
		// new Label(composite, SWT.LEFT).setText("Field Two:");
		// fieldTwo = new Text(composite, SWT.BORDER);
		// fieldTwo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// fieldTwo.setText(preferenceStore.getString(TWO));
		//
		// new Label(composite, SWT.LEFT).setText("Field Three:");
		// fieldThree = new Text(composite, SWT.BORDER);
		// fieldThree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// fieldThree.setText(preferenceStore.getString(THREE));

		return composite;
	}

	/**
	 * Called when user clicks Restore Defaults
	 */
	protected void performDefaults() {
		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Reset the fields to the defaults
		fieldOne.setText(preferenceStore.getDefaultString(ONE));
		fieldTwo.setText(preferenceStore.getDefaultString(TWO));
		fieldThree.setText(preferenceStore.getDefaultString(THREE));
	}

	/**
	 * Called when user clicks Apply or OK
	 * 
	 * @return boolean
	 */
	public boolean performOk() {
		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Set the values from the fields
		if (fieldOne != null)
			preferenceStore.setValue(ONE, fieldOne.getText());
		if (fieldTwo != null)
			preferenceStore.setValue(TWO, fieldTwo.getText());
		if (fieldThree != null)
			preferenceStore.setValue(THREE, fieldThree.getText());

		// Return true to allow dialog to close
		return true;
	}
}

/**
 * This class creates a preference page
 */
class PrefPageTwo extends PreferencePage {
	// Names for preferences
	/**
	 * PrefPageTwo constructor
	 */
	public PrefPageTwo() {
		super("Console");
		// setDescription("Console");
	}

	/**
	 * Creates the controls for this page
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));

		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		FontFieldEditor font = new FontFieldEditor(
				IPreferenceConstants.CONSOLE_FONT, "Font", parent);

		ColorFieldEditor fontColour = new ColorFieldEditor(
				IPreferenceConstants.CONSOLE_FONT_COLOUR,
				"Selected font colour", parent);

		ColorFieldEditor backgroundColour = new ColorFieldEditor(
				IPreferenceConstants.CONSOLE_BACKGROUND, "Background colour",
				parent);

		IntegerFieldEditor lineLimit = new IntegerFieldEditor(
				IPreferenceConstants.LINE_LIMIT,
				"Output history limit (chars)", parent);
		lineLimit.setValidRange(1000, 999999);

		IntegerFieldEditor commandHistoryLimit = new IntegerFieldEditor(
				IPreferenceConstants.COMMAND_HISTORY_LIMIT,
				"Command history limit", parent);
		// lineLimit.setValidRange(5,100);

		return composite;
	}

	/**
	 * Change the description label
	 */
	protected Label createDescriptionLabel(Composite parent) {
		Label label = null;
		String description = getDescription();
		if (description != null) {
			// Upper case the description
			description = description.toUpperCase();

			// Right-align the label
			label = new Label(parent, SWT.RIGHT);
			label.setText(description);
		}
		return label;
	}

	/**
	 * Called when user clicks Restore Defaults
	 */
	protected void performDefaults() {
		// Get the preference store
		IPreferenceStore preferenceStore = getPreferenceStore();

		// Reset the fields to the defaults
		// checkOne.setSelection(preferenceStore.getDefaultBoolean(ONE));
		// checkTwo.setSelection(preferenceStore.getDefaultBoolean(TWO));
		// checkThree.setSelection(preferenceStore.getDefaultBoolean(THREE));
	}

	/**
	 * Called when user clicks Apply or OK
	 * 
	 * @return boolean
	 */
	// public boolean performOk() {
	// // Get the preference store
	// IPreferenceStore preferenceStore = getPreferenceStore();
	//
	// // Set the values from the fields
	// // if (checkOne != null)
	// // preferenceStore.setValue(ONE, checkOne.getSelection());
	// // if (checkTwo != null)
	// // preferenceStore.setValue(TWO, checkTwo.getSelection());
	// // if (checkThree != null)
	// // preferenceStore.setValue(THREE, checkThree.getSelection());
	//
	// // Return true to allow dialog to close
	// return true;
	// }
}