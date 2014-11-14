package tool.clients.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

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
    else super.sendMessage(message);
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
    System.out.println(this + " <- " + message);
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
    else return super.callMessage(message);
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