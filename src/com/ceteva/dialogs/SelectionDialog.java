package com.ceteva.dialogs;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.demo.Main;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectionDialog.
 */
public class SelectionDialog {

	/**
	 * Contained in default.
	 *
	 * @param string the string
	 * @param defaults the defaults
	 * @return true, if successful
	 */
	private static boolean containedInDefault(String string, Object[] defaults) {
		for (int i = 0; i < defaults.length; i++) {
			String def = (String) defaults[i];
			if (def.equals(string))
				return true;
		}
		return false;
	}

	/**
	 * Count all options.
	 *
	 * @param declaredOptions the declared options
	 * @return the int
	 */
	private static int countAllOptions(Object[] declaredOptions) {
		int count = 0;
		for (int i = 0; i < declaredOptions.length; i++) {
			String option = (String) declaredOptions[i];
			if (option.startsWith("!"))
				count++;
		}
		return count;
	}

	/**
	 * Gets the result array.
	 *
	 * @param strings the strings
	 * @param defaults the defaults
	 * @return the result array
	 */
	private static Value[] getResultArray(Object[] strings, Object[] defaults) {
		Value[] values = new Value[strings.length];
		for (int i = 0; i < strings.length; i++) {
			String string = (String) strings[i];
			if (containedInDefault(string, defaults))
				string = "!" + string;
			values[i] = new Value(string);
		}
		return values;
	}

	/**
	 * Gets the shell.
	 *
	 * @return the shell
	 */
	private static Shell getShell() {
		return Main.getInstance().shell;
	}

	/**
	 * Open multi selection dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param options the options
	 * @param handler the handler
	 * @return the value
	 */
	public static Value openMultiSelectionDialog(String title, String message,
			Object[] options, EventHandler handler) {
		// String[] stringOptions = objectsToStrings(options);
		// Object[] allOptions = processAllOptions(stringOptions);
		// Object[] defaultOptions = processDefaultOptions(stringOptions);
		// ListSelectionDialog lsd = new ListSelectionDialog(getShell(),
		// allOptions, new ArrayContentProvider(), new LabelProvider(),
		// message);
		// lsd.setTitle(title);
		// lsd.setInitialSelections(defaultOptions);
		// // if (lsd.open() != 0)
		// {
		// lsd.open();
		// Object[] result = lsd.getResult();
		// if (result != null)
		// return new Value(getResultArray(result, defaultOptions));
		// }
		// return new Value("-1");

		String[] stringOptions = objectsToStrings(options);
		Object[] allOptions = processAllOptions(stringOptions);
		Object[] defaultOptions = processDefaultOptions(stringOptions);
		Shell shell = getShell();
		ListDialog ld = new ListDialog(shell);
		ld.setInput(allOptions);
		ld.setContentProvider(new ArrayContentProvider());
		ld.setLabelProvider(new LabelProvider());
		ld.setMessage(message);
		ld.setTitle(title);
		ld.setInitialSelections(defaultOptions);
		if (ld.open() != SWT.CANCEL) {
			Object[] result = ld.getResult();
			if (result != null && result.length > 0)
				return new Value(getResultArray(result, defaultOptions));
		}
		return new Value("-1");
	}

	/**
	 * Open selection dialog.
	 *
	 * @param title the title
	 * @param message the message
	 * @param options the options
	 * @param handler the handler
	 * @return the value
	 */
	public static Value openSelectionDialog(String title, String message,
			Object[] options, EventHandler handler) {
		String[] stringOptions = objectsToStrings(options);
		Object[] allOptions = processAllOptions(stringOptions);
		Object[] defaultOptions = processDefaultOptions(stringOptions);
		Shell shell = getShell();
		ListDialog ld = new ListDialog(shell);
		ld.setInput(allOptions);
		ld.setContentProvider(new ArrayContentProvider());
		ld.setLabelProvider(new LabelProvider());
		ld.setMessage(message);
		ld.setTitle(title);
		ld.setInitialSelections(defaultOptions);
		if (ld.open() != SWT.CANCEL) {
			Object[] result = ld.getResult();
			if (result != null && result.length > 0)
				return getResultArray(result, defaultOptions)[0];
		}
		return new Value("");
	}

	/**
	 * Objects to strings.
	 *
	 * @param options the options
	 * @return the string[]
	 */
	private static String[] objectsToStrings(Object[] options) {
		if (options == null) {
			return new String[0];
		} else {
			String[] stringOptions = new String[options.length];
			for (int i = 0; i < options.length; i++) {
				stringOptions[i] = ((Value) options[i]).strValue();
			}
			return stringOptions;
		}
	}

	/**
	 * Process all options.
	 *
	 * @param declaredOptions the declared options
	 * @return the object[]
	 */
	private static Object[] processAllOptions(Object[] declaredOptions) {
		if (declaredOptions == null) {
			return new Object[0];
		} else {
			int oi = 0;
			Object[] options = new Object[declaredOptions.length];
			for (int i = 0; i < declaredOptions.length; i++) {
				String option = (String) declaredOptions[i];
				if (option.startsWith("!"))
					options[oi++] = option.substring(1, option.length());
				else
					options[oi++] = declaredOptions[i];
			}
			return options;
		}
	}

	/**
	 * Process default options.
	 *
	 * @param declaredOptions the declared options
	 * @return the object[]
	 */
	private static Object[] processDefaultOptions(Object[] declaredOptions) {
		if (declaredOptions == null) {
			return new Object[0];
		} else {
			int oi = 0;
			Object[] setOptions = new Object[countAllOptions(declaredOptions)];
			for (int i = 0; i < declaredOptions.length; i++) {
				String option = (String) declaredOptions[i];
				if (option.startsWith("!"))
					setOptions[oi++] = option.substring(1, option.length());
			}
			return setOptions;
		}
	}
}