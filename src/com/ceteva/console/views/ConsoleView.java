package com.ceteva.console.views;

import java.io.PrintStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabItem;

import uk.ac.mdx.xmf.swt.misc.ColorManager;

import com.ceteva.console.ConsolePlugin;
import com.ceteva.consoleInterface.EscapeHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ConsoleView.
 */
public class ConsoleView {

	/**
	 * Instantiates a new console view.
	 *
	 * @param parent the parent
	 * @param style the style
	 * @param tabItemConsole the tab item console
	 */
	public ConsoleView(Composite parent, int style, TabItem tabItemConsole) {
		Composite c1 = new Composite(parent, SWT.BORDER);
		c1.setLayout(new FillLayout());
		text = new StyledText(c1, SWT.MULTI | SWT.V_SCROLL);
		text.setWordWrap(true);
		text.setBackground(backgroundColor);
		addVerifyListener(text);

		tabItemConsole.setControl(c1);
	}

	/** The text. */
	StyledText text = null;
	// Document document = null;
	/** The history. */
	History history = new History();
	
	/** The input start. */
	int inputStart = 0;
	
	/** The text font. */
	Font textFont = null;
	
	/** The background color. */
	Color backgroundColor = ColorManager.getColor(new RGB(221, 171, 160));
	
	/** The foreground color. */
	Color foregroundColor = null;
	
	/** The water mark. */
	int waterMark = 1000;
	
	/** The out. */
	PrintStream out = null;
	
	/** The escape. */
	static EscapeHandler escape = null;

	// Used to synchronise the adding of text to the document with the flushing
	// of waterline

	/** The overflow lock. */
	private Object overflowLock = new Object();
	
	/** The waterline job. */
	private FlushWaterline waterlineJob;

	/**
	 * The Class FlushWaterline.
	 */
	private class FlushWaterline extends Job {

		/** The styled text content. */
		StyledTextContent styledTextContent;

		/**
		 * Instantiates a new flush waterline.
		 */
		FlushWaterline() {
			super("Console Flush Waterline");
			styledTextContent = text.getContent();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		protected IStatus run(IProgressMonitor monitor) {
			synchronized (overflowLock) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						if (!Display.getDefault().isDisposed()) {
							if (text != null) {
								int charCount = styledTextContent
										.getCharCount();
								if (charCount > waterMark) {
									int difference = charCount - waterMark;
									if (difference > 0) {
										styledTextContent.replaceTextRange(0,
												difference, "");
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

	// public ConsoleView() {
	// registerAsListener();
	// }

	/**
	 * Creates the part control.
	 *
	 * @param parent the parent
	 */
	public void createPartControl(Composite parent) {
		// parent.setLayout(new FillLayout());
		// text = new StyledText(parent, SWT.MULTI | SWT.V_SCROLL);
		// text.setWordWrap(true);
		// text.setBackground(backgroundColor);
		// addVerifyListener(text);

		// add the job to reduce the text size as it flows over
		// the waterline

		// waterlineJob = new FlushWaterline();
		// waterlineJob.setSystem(true);
		// waterlineJob.setPriority(Job.INTERACTIVE);

		// get preference details and monitor for future changes

		// registerWithPreferences();
		// getPreferences();
	}

	/**
	 * Append text.
	 *
	 * @param string the string
	 */
	public void appendText(String string) {
		synchronized (overflowLock) {
			if (text != null)
				ConsolePlugin.writeToFile(string);
			text.append(string);
		}
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	public void getPreferences() {
		if (textFont != null)
			textFont.dispose();
		if (backgroundColor != null)
			backgroundColor.dispose();
		if (foregroundColor != null)
			backgroundColor.dispose();
		// IPreferenceStore ipreferences = ConsolePlugin.getDefault()
		// // .getPreferenceStore();
		// RGB fontColour = PreferenceConverter.getColor(ipreferences,
		// IPreferenceConstants.CONSOLE_FONT_COLOUR);
		// RGB backgroundColour = PreferenceConverter.getColor(ipreferences,
		// IPreferenceConstants.CONSOLE_BACKGROUND);
		// // FontData font = PreferenceConverter.getFontData(ipreferences,
		// // IPreferenceConstants.CONSOLE_FONT);
		// text.setBackground(backgroundColor = new Color(Display.getDefault(),
		// backgroundColour));
		// text.setForeground(foregroundColor = new Color(Display.getDefault(),
		// fontColour));
		// text.setFont(textFont = new Font(Display.getCurrent(), font));
		// this.waterMark =
		// ipreferences.getInt(IPreferenceConstants.LINE_LIMIT);
		// history.setSize(ipreferences
		// .getInt(IPreferenceConstants.COMMAND_HISTORY_LIMIT));

	}

	/**
	 * Push to history.
	 *
	 * @param text the text
	 * @return the string
	 */
	public String pushToHistory(StyledText text) {
		StyledTextContent content = text.getContent();
		String command = content.getTextRange(inputStart,
				content.getCharCount() - inputStart - 1);
		history.add(command);
		return command;
	}

	/**
	 * Recall from history forward.
	 *
	 * @return the string
	 */
	public String recallFromHistoryForward() {
		return history.getPrevious();
	}

	/**
	 * Recall from history backward.
	 *
	 * @return the string
	 */
	public String recallFromHistoryBackward() {
		return history.getNext();
	}

	/**
	 * Adds the command.
	 *
	 * @param text the text
	 * @param command the command
	 */
	public void addCommand(StyledText text, String command) {
		int length = text.getCharCount() - inputStart;
		text.replaceTextRange(inputStart, length, command);
		goToEnd();
	}

	/**
	 * Adds the verify listener.
	 *
	 * @param text the text
	 */
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
				} else
					e.doit = true;
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
					if (command != "")
						addCommand(text, command);
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					String command = recallFromHistoryBackward();
					if (command != "")
						addCommand(text, command);
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
	}

	/**
	 * Register with preferences.
	 */
	public void registerWithPreferences() {
		// IPreferenceStore preference = ConsolePlugin.getDefault()
		// .getPreferenceStore();
		// preference.addPropertyChangeListener(this);
	}

	/**
	 * Sets the output.
	 *
	 * @param out the new output
	 */
	public void setOutput(PrintStream out) {
		this.out = out;
	}

	/**
	 * Process input.
	 *
	 * @param input the input
	 */
	public void processInput(String input) {
		appendText(input);
		// waterlineJob.schedule(250);
		goToEnd();
		inputStart = text.getContent().getCharCount();
	}

	/**
	 * Property change.
	 *
	 * @param event the event
	 */
	public void propertyChange(PropertyChangeEvent event) {
		getPreferences();
	}

	// public void setFocus() {
	// // goToEnd();
	// // text.setFocus();
	// }

	/**
	 * Go to end.
	 */
	public void goToEnd() {
		int end = text.getCharCount();
		text.setSelection(end, end);
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		textFont.dispose();
		backgroundColor.dispose();
		foregroundColor.dispose();
	}

	/**
	 * Sets the escape handler.
	 *
	 * @param handler the new escape handler
	 */
	public static void setEscapeHandler(EscapeHandler handler) {
		escape = handler;
	}

	/**
	 * Register as listener.
	 */
	public void registerAsListener() {
		// IWorkbenchPage page = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.addPartListener(this);
	}

	// public void partActivated(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partBroughtToTop(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partClosed(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partDeactivated(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partOpened(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partHidden(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partVisible(IWorkbenchPartReference partRef) {
	// }
	//
	// public void partInputChanged(IWorkbenchPartReference partRef) {
	// }

}