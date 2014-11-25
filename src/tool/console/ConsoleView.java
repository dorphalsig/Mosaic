package tool.console;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import tool.clients.dialogs.notifier.NotificationType;
import tool.clients.dialogs.notifier.NotifierDialog;
import tool.clients.workbench.WorkbenchClient;
import tool.xmodeler.XModeler;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import xos.Message;
import xos.Value;

import com.ceteva.consoleInterface.EscapeHandler;
import com.ceteva.text.texteditor.ConsoleLineStyler;

public class ConsoleView {

  protected static final int FONT_INC        = 2;
  protected static final int MAX_FONT_HEIGHT = 30;
  protected static final int MIN_FONT_HEIGHT = 4;

  public static void setEscapeHandler(EscapeHandler handler) {
    escape = handler;
  }

  static EscapeHandler escape          = null;

  StyledText           text            = null;
  History              history         = new History();
  int                  inputStart      = 0;
  Font                 textFont        = new Font(Display.getCurrent(), "Courier New", 14, SWT.NORMAL);
  Color                backgroundColor = ColorManager.getColor(new RGB(255, 255, 255));
  Color                foregroundColor = ColorManager.getColor(new RGB(0, 0, 0));
  int                  waterMark       = 1000;
  PrintStream          out             = null;
  Object               overflowLock    = new Object();
  boolean              autoComplete    = true;

  public ConsoleView(Composite parent, CTabItem tabItem) {
    Composite c1 = new Composite(parent, SWT.BORDER);
    c1.setLayout(new FillLayout());
    text = new StyledText(c1, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    text.setWordWrap(true);
    text.setBackground(backgroundColor);
    text.setFont(textFont);
    addVerifyListener(text);
    tabItem.setControl(c1);
  }

  public void addCommand(StyledText text, String command) {
    int length = text.getCharCount() - inputStart;
    text.replaceTextRange(inputStart, length, command);
    goToEnd();
  }

  public void addVerifyListener(final StyledText text) {
    text.addVerifyListener(new VerifyListener() {
      public void verifyText(VerifyEvent e) {
        int start = e.start;
        int end = e.end;
        if (start < inputStart || end < inputStart) {
          goToEnd();
          appendText(e.text);
          goToEnd();
          e.doit = false;
        } else e.doit = true;
      }
    });
    text.addVerifyKeyListener(new VerifyKeyListener() {
      public void verifyKey(VerifyEvent e) {
        if (overwriting(e.character)) {
          text.setCaretOffset(text.getCaretOffset() + 1);
          e.doit = false;
        } else if (e.keyCode == SWT.ESC) {
          if (escape != null) escape.interrupt();
          e.doit = false;
        } else if (e.keyCode == SWT.ARROW_UP) {
          String command = recallFromHistoryForward();
          if (command != "") addCommand(text, command);
          e.doit = false;
        } else if (e.keyCode == SWT.ARROW_DOWN) {
          String command = recallFromHistoryBackward();
          if (command != "") addCommand(text, command);
          e.doit = false;
        } else if (e.keyCode == '=' && ((e.stateMask & SWT.CTRL) == SWT.CTRL) && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT)) {
          textFont = new Font(Display.getCurrent(), textFont.getFontData()[0].getName(), Math.min(MAX_FONT_HEIGHT, textFont.getFontData()[0].getHeight() + FONT_INC), SWT.NORMAL);
          text.setFont(textFont);
          e.doit = false;
        } else if (e.keyCode == '-' && ((e.stateMask & SWT.CTRL) == SWT.CTRL) && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT)) {
          textFont = new Font(Display.getCurrent(), textFont.getFontData()[0].getName(), Math.max(MIN_FONT_HEIGHT, textFont.getFontData()[0].getHeight() - FONT_INC), SWT.NORMAL);
          text.setFont(textFont);
          e.doit = false;
        } else if (e.keyCode == 'a' && ((e.stateMask & SWT.CTRL) == SWT.CTRL)) {
          autoComplete = !autoComplete;
          showStatus();
          e.doit = false;
        } else if (e.keyCode == SWT.CR) {
          goToEnd();
          appendText("\n");
          goToEnd();
          e.doit = false;
          String output = pushToHistory(text);
          if (out != null) {
            out.print(output + "\r");
            out.flush();
          }
          goToEnd();
          inputStart = text.getContent().getCharCount();
        } else if (e.keyCode == '.' && ((e.stateMask & SWT.SHIFT) != SWT.SHIFT) && autoComplete) {
          // Display options based on the type of the input.
          StyledTextContent content = text.getContent();
          String command = content.getTextRange(inputStart, text.getCaretOffset() - inputStart);
          WorkbenchClient.theClient().dotConsole(command);
        } else if (e.keyCode == ';' && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && autoComplete) {
          // We might have a :: where there is a path to the left ...
          StyledTextContent content = text.getContent();
          String command = content.getTextRange(inputStart, text.getCaretOffset() - inputStart);
          if (command.endsWith(":")) {
            WorkbenchClient.theClient().nameLookup(command.substring(0, command.length() - 1));
          }
        } else if (e.keyCode == '.' && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && autoComplete) {
          // We might have a -> and can fill in the standard patterns ...
          StyledTextContent content = text.getContent();
          String command = content.getTextRange(inputStart, text.getCaretOffset() - inputStart);
          if (command.endsWith("-")) {
            completeArrow();
          }
        } else if (e.keyCode == '[' && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && autoComplete) {
          // Are we starting a collection?
          StyledTextContent content = text.getContent();
          String command = content.getTextRange(inputStart, text.getCaretOffset() - inputStart);
          if (command.endsWith("Set") || command.endsWith("Seq")) {
            insert("{}");
            backup(1);
            e.doit = false;
          }
        } else if (e.keyCode == '9' && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && autoComplete) {
          // Insert the corresponding parenthesis...
          insert("()");
          backup(1);
          e.doit = false;
        } else if (e.keyCode == '\'' && ((e.stateMask & SWT.SHIFT) == SWT.SHIFT) && autoComplete) {
          // Insert the corresponding close string...
          insert("\"\"");
          backup(1);
          e.doit = false;
        } else prepareTopLevelCommand();
      }
    });

    ConsoleLineStyler consoleLineStyper = new ConsoleLineStyler();
    text.addLineStyleListener(consoleLineStyper);
  }

  private void showStatus() {
    String status = "AutoComplete = " + autoComplete;
    NotifierDialog.notify("Console Status", status, NotificationType.values()[2]);
  }

  public void appendText(String string) {
    synchronized (overflowLock) {
      text.append(string);
    }
  }

  public void prepareTopLevelCommand() {
    if (text.getText().length() == text.getCaretOffset() && !text.getText().endsWith(";")) {
      insert(";");
      backup(1);
    }
  }

  public void insert(String string) {
    synchronized (overflowLock) {
      text.insert(string);
      text.setCaretOffset(text.getCaretOffset() + string.length());
    }
  }

  public void createPartControl(Composite parent) {
  }

  public void dispose() {
    textFont.dispose();
    backgroundColor.dispose();
    foregroundColor.dispose();
  }

  public PrintStream getOutput() {
    return out;
  }

  public void getPreferences() {
    if (textFont != null) textFont.dispose();
    if (backgroundColor != null) backgroundColor.dispose();
    if (foregroundColor != null) backgroundColor.dispose();
  }

  public void goToEnd() {
    int end = text.getCharCount();
    text.setSelection(end, end);
  }

  public boolean overwriting(char c) {
    int end = text.getCharCount();
    int caret = text.getCaretOffset();
    return caret < end && text.getText().charAt(caret) == c;
  }

  public void backup(int backup) {
    text.setCaretOffset(text.getCaretOffset() - backup);
  }

  public void processInput(String input) {
    appendText(input);
    goToEnd();
    inputStart = text.getContent().getCharCount();
  }

  public void propertyChange(PropertyChangeEvent event) {
    getPreferences();
  }

  public String pushToHistory(StyledText text) {
    StyledTextContent content = text.getContent();
    String command = content.getTextRange(inputStart, content.getCharCount() - inputStart - 1);
    history.add(command);
    return command;
  }

  public String recallFromHistoryBackward() {
    return history.getNext();
  }

  public String recallFromHistoryForward() {
    return history.getPrevious();
  }

  public void registerAsListener() {
  }

  public void registerWithPreferences() {
  }

  public void setOutput(PrintStream out) {
    this.out = out;
  }

  public void writeHistory(PrintStream out) {
    for (String command : history)
      out.print("<Command text='" + XModeler.encodeXmlAttribute(command) + "'/>");
  }

  public void addCommand(String command) {
    history.add(command);
  }

  private void completeArrow() {
    Menu menu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
    SelectionListener listener = new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent event) {
      }

      public void widgetSelected(SelectionEvent event) {
        MenuItem item = (MenuItem) event.widget;
        String label = item.getText().substring(2);
        insert(label);
      }
    };
    Point p = text.getCaret().getLocation();
    Point displayPoint = text.toDisplay(p);
    menu.setLocation(displayPoint);
    MenuItem asSeq = new MenuItem(menu, SWT.NONE);
    asSeq.setText("->asSeq");
    asSeq.addSelectionListener(listener);
    MenuItem asSet = new MenuItem(menu, SWT.NONE);
    asSet.setText("->asSet");
    asSet.addSelectionListener(listener);
    MenuItem collect = new MenuItem(menu, SWT.NONE);
    collect.setText("->collect(element | exp)");
    collect.addSelectionListener(listener);
    MenuItem exists = new MenuItem(menu, SWT.NONE);
    exists.setText("->exists(element | condition)");
    exists.addSelectionListener(listener);
    MenuItem forall = new MenuItem(menu, SWT.NONE);
    forall.setText("->forall(element | condition)");
    forall.addSelectionListener(listener);
    MenuItem isEmpty = new MenuItem(menu, SWT.NONE);
    isEmpty.setText("->isEmpty");
    isEmpty.addSelectionListener(listener);
    MenuItem reject = new MenuItem(menu, SWT.NONE);
    reject.setText("->reject(element | condition)");
    reject.addSelectionListener(listener);
    MenuItem select = new MenuItem(menu, SWT.NONE);
    select.setText("->select(element | condition)");
    select.addSelectionListener(listener);
    MenuItem size = new MenuItem(menu, SWT.NONE);
    size.setText("->size");
    size.addSelectionListener(listener);
    menu.setVisible(true);
  }

  public void dot(final Message message) {
    XModeler.getXModeler().getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          Menu menu = getDotPopup(message);
          Point p = text.getCaret().getLocation();
          Point displayPoint = text.toDisplay(p);
          menu.setLocation(displayPoint);
          menu.setVisible(true);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  public Menu getDotPopup(Message message) {
    HashSet<String> labels = new HashSet<String>();
    Value[] pairs = message.args[0].values;
    for (Value value : pairs) {
      Value[] pair = value.values;
      String type = pair[0].strValue();
      String label = pair[1].strValue();
      labels.add(label);
    }
    Vector<String> sortedLabels = new Vector<String>(labels);
    Collections.sort(sortedLabels);
    if (labels.size() < 26)
      return getShortDotPopup(sortedLabels);
    else return getLongDotPopup(sortedLabels);
  }

  private Menu getLongDotPopup(Vector<String> labels) {
    Menu mainMenu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
    for (int i = 0; i < 26; i++) {
      char c = (char) ('a' + i);
      MenuItem nestedItem = new MenuItem(mainMenu, SWT.CASCADE);
      nestedItem.setText("" + c);
      Menu menu = new Menu(mainMenu);
      nestedItem.setMenu(menu);
      for (final String label : labels) {
        if (label.charAt(0) == c || label.charAt(0) == (c + 26)) {
          MenuItem item = new MenuItem(menu, SWT.NONE);
          item.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent event) {
            }

            public void widgetSelected(SelectionEvent event) {
              insert(label);
            }
          });
          item.setText(label);
        }
      }
      if (menu.getItemCount() == 0) nestedItem.dispose();
    }
    return mainMenu;
  }

  private Menu getShortDotPopup(Vector<String> labels) {
    Menu menu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
    for (final String label : labels) {
      MenuItem item = new MenuItem(menu, SWT.NONE);
      item.addSelectionListener(new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent event) {
        }

        public void widgetSelected(SelectionEvent event) {
          insert(label);
        }
      });
      item.setText(label);
    }
    return menu;
  }

  private Menu getNameSpacePopup(Message message) {
    HashSet<String> unsortedNames = new HashSet<String>();
    Value[] names = message.args[0].values;
    for (Value name : names) {
      unsortedNames.add(name.strValue());
    }
    Vector<String> sortedNames = new Vector<String>(unsortedNames);
    Collections.sort(sortedNames);
    Menu menu = new Menu(XModeler.getXModeler(), SWT.POP_UP);
    for (final String label : sortedNames) {
      MenuItem item = new MenuItem(menu, SWT.NONE);
      item.addSelectionListener(new SelectionListener() {
        public void widgetDefaultSelected(SelectionEvent event) {
        }

        public void widgetSelected(SelectionEvent event) {
          insert(label);
        }
      });
      item.setText(label);
    }
    return menu;
  }

  public void namespace(final Message message) {
    XModeler.getXModeler().getDisplay().syncExec(new Runnable() {
      public void run() {
        try {
          Menu menu = getNameSpacePopup(message);
          Point p = text.getCaret().getLocation();
          Point displayPoint = text.toDisplay(p);
          menu.setLocation(displayPoint);
          menu.setVisible(true);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }
}