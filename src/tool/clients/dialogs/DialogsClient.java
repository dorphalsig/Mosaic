package tool.clients.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import tool.clients.Client;
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

  private void newMessageDialog(final Message message) {
    runOnDisplay(new Runnable() {
      public void run() {
        Value id = message.args[0];
        Value info = message.args[1];
        MessageDialog.openInformation(XModeler.getXModeler(), "Question", info.strValue());
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
    else return super.callMessage(message);
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