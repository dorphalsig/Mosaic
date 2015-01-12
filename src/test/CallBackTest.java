package test;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tool.clients.workbench.WorkbenchClient;
import xos.Value;

public class CallBackTest extends Frame implements ActionListener {

  String title;
  int    counter = 0;
  int    handler;

  public CallBackTest(String title, int x, int y, int width, int height, int handler) {
    this.title = title;
    this.handler = handler;
    Button button = new Button("Increment");
    add(button);
    button.addActionListener(this);
    setTitle(title);
    setLocation(x, y);
    setSize(width, height);
    setVisible(true);
  }

  public void inc() {
    counter++;
    setTitle(title + "[" + counter + "]");
  }

  public void actionPerformed(ActionEvent e) {
    inc();
    WorkbenchClient.theClient().send(handler, "showCounter", new Value(counter));
  }
}
