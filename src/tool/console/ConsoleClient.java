package tool.console;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;

import uk.ac.mdx.xmf.swt.demo.Main;

public class ConsoleClient extends Thread {

  ConsoleView          view        = null;
  BufferedReader       in;
  PrintStream          out;
  StringBuffer         queuedInput = new StringBuffer();
  Display              display;
  static ConsoleClient theConsole;

  public ConsoleClient(InputStream in, OutputStream out) {
    this.in = new BufferedReader(new InputStreamReader(in));
    this.out = new PrintStream(new BufferedOutputStream(out));
    theConsole = this;
  }

  public void setDisplay(Display display) {
    this.display = display;
  }

  public void setView(ConsoleView view) {
    this.view = view;
  }

  public static ConsoleClient theConsole() {
    return theConsole;
  }

  public void run() {
    char[] buffer = new char[1000];
    while (true) {
      try {
        int size = in.read(buffer);
        if (size > 0)
          sendInput(new String(buffer).substring(0, size));
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }

  public void debug(String message) {
    System.err.println(java.lang.Thread.currentThread() + ": " + message);
    System.err.flush();
  }

  public boolean tryConnecting() {
    while ((view = Console.getConsoleView()) == null)
      try {
        // We might not have got everything set up just yet...
        Thread.currentThread().sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    view.setOutput(out);
    view.processInput(queuedInput.toString());
    return true;
  }

  public void sendInput(final String input) {
    Display.getDefault().syncExec(new Runnable() {
      public void run() {
        if (view != null && view.getOutput() != null)
          view.processInput(input);
        else if (tryConnecting())
          view.processInput(input);
        else
          queueInput(input);
      }
    });
  }

  public void queueInput(String input) {
    queuedInput.append(input);
  }
}