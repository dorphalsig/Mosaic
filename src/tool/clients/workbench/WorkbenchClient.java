package tool.clients.workbench;

import tool.clients.Client;
import tool.clients.diagrams.DiagramClient;
import tool.console.ConsoleClient;
import tool.xmodeler.XModeler;
import xos.Message;
import xos.Value;

public class WorkbenchClient extends Client {

  static WorkbenchClient theClient;

  public WorkbenchClient() {
    super("com.ceteva.mosaic");
    theClient = this;
  }

  public void sendMessage(final Message message) {
    if (message.hasName("shutdown"))
      shutdown(message);
    else if (message.hasName("saveInflater"))
      saveInflater(message);
    else if (message.hasName("inflate"))
      inflate(message);
    else if (message.hasName("consoleDot"))
      consoleDot(message);
    else if (message.hasName("namespace"))
      consoleNamespace(message);
    else super.sendMessage(message);
  }

  private void consoleDot(Message message) {
    ConsoleClient.theConsole().dot(message);
  }

  private void consoleNamespace(Message message) {
    ConsoleClient.theConsole().namespace(message);
  }

  private void inflate(Message message) {
    Value inflationPath = message.args[0];
    XModeler.inflate(inflationPath.strValue());
  }

  private void saveInflater(Message message) {
    Value inflationPath = message.args[0];
    XModeler.saveInflator(inflationPath.strValue());
  }

  private void shutdown(Message message) {
    XModeler.shutdown();
  }

  public boolean processMessage(Message message) {
    return false;
  }

  public void shutdownEvent() {
    Message message = getHandler().newMessage("shutdownRequest", 0);
    getHandler().raiseEvent(message);
  }

  public void shutdownAndSaveEvent(String imagePath, String inflationPath) {
    Message message = getHandler().newMessage("shutDownAndSave", 2);
    message.args[0] = new Value(imagePath);
    message.args[1] = new Value(inflationPath);
    getHandler().raiseEvent(message);
  }

  public static WorkbenchClient theClient() {
    return theClient;
  }

  public void dotConsole(String command) {
    Message message = getHandler().newMessage("consoleDot", 1);
    message.args[0] = new Value(command);
    getHandler().raiseEvent(message);
  }

  public void nameLookup(String command) {
    Message message = getHandler().newMessage("nameLookup", 1);
    message.args[0] = new Value(command);
    getHandler().raiseEvent(message);
  }
}