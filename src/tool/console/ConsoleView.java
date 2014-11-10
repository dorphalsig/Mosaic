package tool.console;

import java.io.PrintStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabItem;

import tool.xmodeler.XModeler;
import uk.ac.mdx.xmf.swt.misc.ColorManager;

import com.ceteva.console.ConsolePlugin;
import com.ceteva.consoleInterface.EscapeHandler;
import com.ceteva.text.texteditor.ConsoleLineStyler;

public class ConsoleView {

  private class FlushWaterline extends Job {

    StyledTextContent styledTextContent;

    FlushWaterline() {
      super("Console Flush Waterline");
      styledTextContent = text.getContent();
    }

    protected IStatus run(IProgressMonitor monitor) {
      synchronized (overflowLock) {
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            if (!Display.getDefault().isDisposed()) {
              if (text != null) {
                int charCount = styledTextContent.getCharCount();
                if (charCount > waterMark) {
                  int difference = charCount - waterMark;
                  if (difference > 0) {
                    styledTextContent.replaceTextRange(0, difference, "");
                    inputStart = inputStart - difference;
                    goToEnd();
                  }
                }
              }
            }
          }
        });
      }
      return Status.OK_STATUS;
    }
  }

  protected static final int FONT_INC        = 2;
  protected static final int MAX_FONT_HEIGHT = 30;
  protected static final int MIN_FONT_HEIGHT = 4;

  public static void setEscapeHandler(EscapeHandler handler) {
    escape = handler;
  }

  boolean                lineNumbers     = true;
  StyledText             text            = null;
  History                history         = new History();
  int                    inputStart      = 0;
  Font                   textFont        = new Font(Display.getCurrent(), "Courier New", 14, SWT.NORMAL);
  Color                  backgroundColor = ColorManager.getColor(new RGB(255, 255, 255));
  Color                  foregroundColor = ColorManager.getColor(new RGB(0, 0, 0));
  int                    waterMark       = 1000;
  PrintStream            out             = null;
  static EscapeHandler   escape          = null;
  private Object         overflowLock    = new Object();
  private FlushWaterline waterlineJob;

  public ConsoleView(Composite parent, TabItem tabItemConsole) {
    Composite c1 = new Composite(parent, SWT.BORDER);
    c1.setLayout(new FillLayout());
    text = new StyledText(c1, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    text.setWordWrap(true);
    text.setBackground(backgroundColor);
    text.setFont(textFont);
    addVerifyListener(text);
    tabItemConsole.setControl(c1);
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
        if (e.keyCode == SWT.ESC) {
          if (escape != null) {
            escape.interrupt();
          }
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
        } else if (e.keyCode == SWT.CR) {
          goToEnd();
          appendText("\n");
          // waterlineJob.schedule();
          goToEnd();
          e.doit = false;
          String output = pushToHistory(text);
          if (out != null) {
            out.print(output + "\r");
            out.flush();
          }
          goToEnd();
          inputStart = text.getContent().getCharCount();
        }
      }
    });

    ConsoleLineStyler consoleLineStyper = new ConsoleLineStyler();
    text.addLineStyleListener(consoleLineStyper);
  }

  public void appendText(String string) {
    synchronized (overflowLock) {
      if (text != null) ConsolePlugin.writeToFile(string);
      text.append(string);
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

  public void processInput(String input) {
    appendText(input);
    // waterlineJob.schedule(250);
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
}