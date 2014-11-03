package tool.console;

import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class Console {

  static ConsoleView consoleView;
  static String      CONSOLE_LABEL = "XMF Console";

  public static void start(Composite parent, int style) {
    ConsoleClient.theConsole().setDisplay(Display.getDefault());
    TabFolder tabFolder = new TabFolder(parent, style);
    tabFolder.setVisible(true);
    TabItem tabItem = new TabItem(tabFolder, SWT.BORDER);
    tabItem.setText(CONSOLE_LABEL);
    ConsoleView consoleView = new ConsoleView(tabFolder, tabItem);
    ConsoleClient.theConsole().setView(consoleView);
    setConsoleView(consoleView);
  }

  public static ConsoleView getConsoleView() {
    return consoleView;
  }

  public static void setConsoleView(ConsoleView consoleView) {
    Console.consoleView = consoleView;
  }

  public static void writeHistory(PrintStream out) {
    consoleView.writeHistory(out);
  }

  public static void addCommand(String command) {
    consoleView.addCommand(command);
  }
}
