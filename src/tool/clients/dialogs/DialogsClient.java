package tool.clients.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

import tool.clients.Client;
import tool.clients.dialogs.notifier.NotificationType;
import tool.clients.dialogs.notifier.NotifierDialog;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class DialogsClient extends Client {

  static DialogsClient theClient;
  static Cursor        cursor = null;

  public DialogsClient() {
    super("com.ceteva.dialogs");
    theClient = this;
  }

  public static DialogsClient theClient() {
    return theClient;
  }

  public void sendMessage(final Message message) {
    if (message.hasName("newBusyDialog"))
      newBusyDialog(message);
    else if (message.hasName("noLongerBusy"))
      noLongerBusy(message);
    else if (message.hasName("newMessageDialog"))
      newMessageDialog(message);
    else if (message.hasName("newWarningDialog"))
      newMessageDialog(message);
    else if (message.hasName("newTextAreaDialog"))
      newTextAreaDialog(message);
    else super.sendMessage(message);
  }

  private void newTextAreaDialog(Message message) {
    String id = message.args[0].strValue();
    String type = message.args[1].strValue();
    final String title = message.args[2].strValue();
    final String info = message.args[3].strValue();
    runOnDisplay(new Runnable() {
      public void run() {
        MessageDialog dialog = new MessageDialog(XModeler.getXModeler(), title, null, info, MessageDialog.INFORMATION, new String[] { "OK" }, 0);
        dialog.open();
      }
    });
  }

  private Value newDirectoryDialog(final Message message) {
    final Value[] result = new Value[1];
    runOnDisplay(new Runnable() {
      public void run() {
        String path = message.args[0].strValue();
        DirectoryDialog dialog = new DirectoryDialog(XModeler.getXModeler());
        if (new File(path).exists()) dialog.setFilterPath(path);
        path = dialog.open();
        path = path == null ? "" : path;
        result[0] = new Value(path);
      }
    });
    return result[0];
  }

  private Value newFileDialog(final Message message) {
    final Value[] result = new Value[1];
    runOnDisplay(new Runnable() {
      public void run() {
        String type = message.args[0].strValue();
        String path = message.args[1].strValue();
        String pattern = message.args[2].strValue();
        String def = message.args[3].strValue();
        FileDialog dialog = new FileDialog(XModeler.getXModeler(), type.equals("open") ? SWT.OPEN : SWT.SAVE);
        dialog.setFilterExtensions(new String[] { pattern });
        dialog.setFileName(def);
        dialog.setFilterPath(path);
        if (new File(path).exists()) dialog.setFilterPath(path);
        path = dialog.open();
        path = path == null ? "" : path;
        result[0] = new Value(path);
      }
    });
    return result[0];
  }

  private void newMessageDialog(final Message message) {
    runOnDisplay(new Runnable() {
      public void run() {
        Value id = message.args[0];
        Value info = message.args[1];
        NotifierDialog.notify("Message", info.strValue(), NotificationType.values()[5]);
      }
    });
  }

  private void noLongerBusy(final Message message) {
    runOnDisplay(new Runnable() {
      public void run() {
        Value id = message.args[0];
        XModeler.removeBusyInformation();
        XModeler.getXModeler().setCursor(cursor);
        cursor = null;
      }
    });
  }

  public boolean processMessage(Message message) {
    return false;
  }

  public Value callMessage(Message message) {
    if (message.hasName("newQuestionDialog"))
      return newQuestionDialog(message);
    else if (message.hasName("newDirectoryDialog"))
      return newDirectoryDialog(message);
    else if (message.hasName("newFileDialog"))
      return newFileDialog(message);
    else if (message.hasName("newInputDialog"))
      return newInputDialog(message);
    else if (message.hasName("newSelectionDialog"))
      return selectionDialog(message);
    else return super.callMessage(message);
  }

  public Value selectionDialog(Message message) {
    final boolean multi = message.args[0].boolValue;
    final String title = message.args[1].strValue();
    final String message_ = message.args[2].strValue();
    final Value[] options = message.args[3].values;
    final Value[] result = new Value[1];
    runOnDisplay(new Runnable() {
      public void run() {
        if (multi)
          result[0] = openMultiSelectionDialog(title, message_, options);
        else result[0] = openSelectionDialog(title, message_, options);
      }
    });
    return result[0];
  }

  public static Value openSelectionDialog(String title, String message, Value[] options) {
    String[] stringOptions = objectsToStrings(options);
    String[] allOptions = processAllOptions(stringOptions);
    String[] defaultOptions = processDefaultOptions(stringOptions);
    Shell shell = XModeler.getXModeler();
    ListDialog ld = new ListDialog(shell);
    ld.setInput(allOptions);
    ld.setContentProvider(new ArrayContentProvider());
    ld.setLabelProvider(new LabelProvider());
    ld.setMessage(message);
    ld.setTitle(title);
    ld.setInitialSelections(defaultOptions);
    if (ld.open() != SWT.CANCEL) {
      Object[] result = ld.getResult();
      if (result != null && result.length > 0) return getResultArray(result, defaultOptions)[0];
    }
    return new Value("");
  }

  public static Value openMultiSelectionDialog(String title, String message, Value[] options) {
    String[] stringOptions = objectsToStrings(options);
    String[] allOptions = processAllOptions(stringOptions);
    String[] defaultOptions = processDefaultOptions(stringOptions);
    Shell shell = XModeler.getXModeler();
    ListDialog ld = new ListDialog(shell);
    ld.setInput(allOptions);
    ld.setContentProvider(new ArrayContentProvider());
    ld.setLabelProvider(new LabelProvider());
    ld.setMessage(message);
    ld.setTitle(title);
    ld.setInitialSelections(defaultOptions);
    if (ld.open() != SWT.CANCEL) {
      Object[] result = ld.getResult();
      if (result != null && result.length > 0) return new Value(getResultArray(result, defaultOptions));
    }
    return new Value("-1");
  }

  private static Value[] getResultArray(Object[] strings, String[] defaults) {
    Value[] values = new Value[strings.length];
    for (int i = 0; i < strings.length; i++) {
      String string = (String) strings[i];
      if (containedInDefault(string, defaults)) string = "!" + string;
      values[i] = new Value(string);
    }
    return values;
  }

  private static boolean containedInDefault(String string, String[] defaults) {
    for (int i = 0; i < defaults.length; i++) {
      String def = defaults[i];
      if (def.equals(string)) return true;
    }
    return false;
  }

  private static int countAllOptions(Object[] declaredOptions) {
    int count = 0;
    for (int i = 0; i < declaredOptions.length; i++) {
      String option = (String) declaredOptions[i];
      if (option.startsWith("!")) count++;
    }
    return count;
  }

  private static String[] processDefaultOptions(String[] declaredOptions) {
    if (declaredOptions == null) {
      return new String[0];
    } else {
      int oi = 0;
      String[] setOptions = new String[countAllOptions(declaredOptions)];
      for (int i = 0; i < declaredOptions.length; i++) {
        String option = declaredOptions[i];
        if (option.startsWith("!")) setOptions[oi++] = option.substring(1, option.length());
      }
      return setOptions;
    }
  }

  private static String[] processAllOptions(String[] declaredOptions) {
    if (declaredOptions == null) {
      return new String[0];
    } else {
      int oi = 0;
      String[] options = new String[declaredOptions.length];
      for (int i = 0; i < declaredOptions.length; i++) {
        String option = declaredOptions[i];
        if (option.startsWith("!"))
          options[oi++] = option.substring(1, option.length());
        else options[oi++] = declaredOptions[i];
      }
      return options;
    }
  }

  private static String[] objectsToStrings(Value[] options) {
    if (options == null) {
      return new String[0];
    } else {
      String[] stringOptions = new String[options.length];
      for (int i = 0; i < options.length; i++) {
        stringOptions[i] = (options[i]).strValue();
      }
      return stringOptions;
    }
  }

  private Value newInputDialog(Message message) {
    String title = message.args[0].strValue();
    String command = message.args[1].strValue();
    String value = message.args[2].strValue();
    return newInputDialog(title, command, value);
  }

  public static String[] orderingDialog(final String title, final String question, final String[] strings) {
    final Object[] result = new Object[] { null };
    DialogsClient.theClient().runOnDisplay(new Runnable() {
      public void run() {
        OrderingDialog d = new OrderingDialog(XModeler.getXModeler(), title, question, strings);
        if (d.open() != SWT.CANCEL)
          result[0] = d.getChoice();
        else result[0] = new String[0];
      }
    });
    return (String[]) result[0];
  }

  public static Value newInputDialog(final String title, final String message, final String value) {
    final String[] result = new String[] { "-1" };
    DialogsClient.theClient().runOnDisplay(new Runnable() {
      public void run() {
        InputDialog dialog = new InputDialog(XModeler.getXModeler(), title, message, value, null);
        dialog.open();
        if (dialog.getValue() != null && !dialog.getValue().equals("")) result[0] = dialog.getValue();
      }
    });
    return new Value(result[0]);
  }

  private void newBusyDialog(final Message message) {
    runOnDisplay(new Runnable() {
      public void run() {
        Value id = message.args[0];
        Value info = message.args[1];
        Value ignore = message.args[2];
        XModeler.showBusyInformation(info.strValue());
        Cursor busy = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
        cursor = XModeler.getXModeler().getCursor();
        XModeler.getXModeler().setCursor(busy);
      }
    });
  }

  private Value newQuestionDialog(final Message message) {
    final Value[] values = new Value[1];
    runOnDisplay(new Runnable() {
      public void run() {
        Value question = message.args[0];
        Value defaultResponse = message.args[1];
        Value icon = message.args[2];
        values[0] = new Value(MessageDialog.openQuestion(XModeler.getXModeler(), "Question", question.strValue()) ? "Yes" : "No");
      }
    });
    return values[0];
  }
}