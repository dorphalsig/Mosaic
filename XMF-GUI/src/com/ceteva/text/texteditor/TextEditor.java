package com.ceteva.text.texteditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.CursorLinePainter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.model.ImageManager;
import XOS.Message;
import XOS.Value;

import com.ceteva.menus.MenuBuilder;
import com.ceteva.menus.MenuListener;
import com.ceteva.menus.MenuManager;
import com.ceteva.text.TextPlugin;
import com.ceteva.text.highlighting.SinglelineScanner;

public class TextEditor implements MenuListener, IPropertyChangeListener,
		IPartListener2 {

	public int partitionNumber = 0;

	TextEditorModel model;
	String identity = "";
	String tooltip = "";
	boolean editable = true;
	boolean changed = false;
	String originalName = "";
	DocumentProvider dprovider;
	SourceViewer viewer;
	CursorLinePainter cursorPainter;
	TextConfiguration configuration;
	SourceViewerDecorationSupport fSourceViewerDecorationSupport;
	SinglelineScanner scanner;
	EventHandler handler;
	Vector highlights = new Vector();
	public StyledText text;

	boolean showNumbers = false;
	Color currentLineColor = null;
	Color highlightedLineColor = null;
	public static CTabItem tabItem;

	Composite parent;
	private int length = 0;

	public TextEditor() {
		getPreferences();
		// registerAsListener();

	}

	public void delete() {
		TextPlugin textManager = TextPlugin.getDefault();
		// IWorkbenchPage page = textManager.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.closeEditor(this, false);
	}

	public void registerAsListener() {
		MenuManager.addMenuListener(this);
		addClickListener();
		// IWorkbenchPage page = PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.addPartListener(this);
		// IPreferenceStore preference = TextPlugin.getDefault()
		// .getPreferenceStore();
		// preference.addPropertyChangeListener(this);
	}

	public void addClickListener() {
		// Action: copy selected text.
		final Action actionCopy = new Action("&Copy",
				ImageDescriptor.createFromImage(ImageManager
						.getImage("icons/State.gif"))) {
			public void run() {
				text.copy();
			}
		};
		actionCopy.setAccelerator(SWT.CTRL + 'C');

		if (text != null) {
			text.addListener(SWT.MouseMove, new Listener() {
				public void handleEvent(Event e) {
					// System.out.println("mouse double click");
					MenuBuilder.resetKeyBindings(null);
					org.eclipse.jface.action.MenuManager menu = new org.eclipse.jface.action.MenuManager();
					MenuBuilder.calculateMenu(getIdentity(), menu, null);
					menu.add(new Separator("DocumentManagement"));

					menu.add(actionCopy);
					text.setMenu(menu.createContextMenu(text));
				}
			});
			text.addListener(SWT.MouseUp, new Listener() {
				public void handleEvent(Event e) {
					// System.out.println("mouse double click");

				}
			});
			text.addVerifyListener(new VerifyListener() {

				@Override
				public void verifyText(VerifyEvent e) {

					length = text.getText().length();
					System.out.println("txt length:" + length);
					System.out.println("etxt length:" + e.text.length());

					// check the input, if user input, thus enable user to save
					if (e.text.length() > 0 && e.text.length() < length) {
						tabItem.setImage(ImageManager
								.getImage("icons/Parser/asterisk.gif"));
					} else {
						tabItem.setImage(null);
					}
				}
			});
		}
	}

	protected void addAction(org.eclipse.jface.action.MenuManager menu,
			String groupCopy, String cut) {
		// TODO Auto-generated method stub

	}

	void raiseEvent(Message m) {
		if (true)
			handler.raiseEvent(m);
	}

	public void removeListener() {
		// getSite().getPage().removePartListener(this);
		// IPreferenceStore preference = TextPlugin.getDefault()
		// .getPreferenceStore();
		// preference.removePropertyChangeListener(this);
	}

	public void getPreferences() {
		// IPreferenceStore preference = TextPlugin.getDefault()
		// .getPreferenceStore();
		// showNumbers =
		// preference.getBoolean(IPreferenceConstants.LINE_NUMBERS);
		// IPreferenceStore ipreferences = TextPlugin.getDefault()
		// .getPreferenceStore();
		// RGB color = PreferenceConverter.getColor(ipreferences,
		// IPreferenceConstants.CURRENT_LINE_COLOR);
		// currentLineColor = ColorManager.getColor(color);
		// color = PreferenceConverter.getColor(ipreferences,
		// IPreferenceConstants.HIGHLIGHT_LINE_COLOR);
		// highlightedLineColor = new Color(Display.getDefault(), color);
	}

	public void init(Composite paren, IEditorInput iInput, String name)
			throws PartInitException {
		// super.init(iSite, iInput);
		// setSite(iSite);
		// setInput(iInput);
		this.parent = paren;
		parent.setToolTipText("Window");
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);
		JavaLineStyler lineStyler = new JavaLineStyler();

		int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION;
		if (iInput instanceof TextEditorInput) {
			TextEditorInput input = (TextEditorInput) iInput;
			try {
				TextStorage storage = (TextStorage) input.getStorage();
				BufferedReader d = new BufferedReader(new InputStreamReader(
						storage.getContents()));

				try {
					identity = d.readLine();
					model = new TextEditorModel(identity, null, this);

					tabItem = new CTabItem(Main.tabFolderDiagram, SWT.BORDER);
					tabItem.setText(name);

					text = new StyledText(tabItem.getParent(), SWT.BORDER
							| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
					GridData spec = new GridData();
					spec.horizontalAlignment = GridData.FILL;
					spec.grabExcessHorizontalSpace = true;
					spec.verticalAlignment = GridData.FILL;
					spec.grabExcessVerticalSpace = true;
					text.setLayoutData(spec);
					text.addLineStyleListener(lineStyler);
					text.setEditable(true);
					Color bg = Display.getDefault().getSystemColor(
							SWT.COLOR_WHITE);
					text.setBackground(bg);

					tabItem.setControl(text);

					// Main.sectionTopMiddle.layout(true);
					Main.tabFolderDiagram.setSelection(tabItem);

				} catch (IOException io) {
					System.out.println(io);
				}
			} catch (CoreException cx) {
				System.out.println(cx);
			}
		}
		registerAsListener();
	}

	public EventHandler getEventHandler() {
		return handler;
	}

	public void setDirty() {
		if (!changed) {
			changed = true;
			// originalName = getPartName();
			// setPartName(" " + originalName);
			Message m = handler.newMessage("textDirty", 2);
			Value v1 = new Value(getIdentity());
			Value v2 = new Value(true);
			m.args[0] = v1;
			m.args[1] = v2;
			handler.raiseEvent(m);
		}
	}

	public void setClean() {
		if (changed) {
			changed = false;
			// setPartName(originalName);
			tabItem.setImage(null);
			clearHighlights();
			Message m = handler.newMessage("textDirty", 2);
			Value v1 = new Value(getIdentity());
			Value v2 = new Value(false);
			m.args[0] = v1;
			m.args[1] = v2;
			handler.raiseEvent(m);
		}
	}

	public void addHighlight(int line) {
		int lines = viewer.getTextWidget().getLineCount();
		if (line >= 0 && line < lines) {
			// removeCursorPainter();
			HighlightLine h = new HighlightLine(viewer, line,
					highlightedLineColor);
			highlights.add(h);
			// addCursorPainter();
		}
	}

	public void setCursorPos(int position) {
		StyledText textWidget = viewer.getTextWidget();
		textWidget.setCaretOffset(position);
	}

	public int getCursorPos() {
		StyledText textWidget = viewer.getTextWidget();
		int pos = textWidget.getCaretOffset();
		return pos;
	}

	public void showLine(int line) {
		StyledText textWidget = viewer.getTextWidget();
		int lines = textWidget.getLineCount();
		if (line >= 0 && line < lines) {
			int offset = textWidget.getOffsetAtLine(line);
			// this.selectAndReveal(offset, 0);
		}
	}

	public void clearHighlights() {
		// removeCursorPainter();
		for (int i = 0; i < highlights.size(); i++) {
			HighlightLine h = (HighlightLine) highlights.elementAt(i);
			h.disable();
		}
		highlights = new Vector();
		// addCursorPainter();
	}

	/*
	 * public void addCursorPainter() { if(cursorPainter == null) {
	 * ITextViewerExtension2 extension= (ITextViewerExtension2)viewer;
	 * cursorPainter = new CursorLinePainter(viewer);
	 * cursorPainter.setHighlightColor(currentLineColor);
	 * extension.addPainter(cursorPainter); } else {
	 * cursorPainter.deactivate(false); } }
	 * 
	 * public void removeCursorPainter() { // ITextViewerExtension2 extension=
	 * (ITextViewerExtension2)viewer; // cursorPainter.deactivate(true); }
	 */

	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		viewer = new SourceViewer(parent, ruler, styles);
		configuration = new TextConfiguration(identity);
		// setSourceViewerConfiguration(configuration);
		// addCursorPainter();
		// viewer.addTextListener(new ITextListener() {
		// public void textChanged(TextEvent event) {
		// String currentText = viewer.getTextWidget().getText();
		// if (event.getText() != null) {
		// if (!event.getText().equals(currentText)) {
		// setDirty();
		// }
		// }
		// }
		// });
		return viewer;
	}

	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(
			ISourceViewer viewer) {
		if (fSourceViewerDecorationSupport == null) {
			fSourceViewerDecorationSupport = new SourceViewerDecorationSupport(
					viewer, null, null, null);
			configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
		}
		return fSourceViewerDecorationSupport;
	}

	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		// support.setSymbolicFontName(getFontPropertyPreferenceKey());
	}

	protected IVerticalRuler createVerticalRuler() {
		CompositeRuler ruler = new CompositeRuler();
		if (showNumbers)
			addLineNumberRuler(ruler);
		else
			addDummyRuler(ruler);
		return ruler;
	}

	public void addLineNumberRuler(CompositeRuler ruler) {
		// LineNumberRulerColumn fLineNumberRulerColumn = new
		// LineNumberRulerColumn();
		// ruler.addDecorator(1, fLineNumberRulerColumn);
	}

	public void addDummyRuler(CompositeRuler ruler) {
		// AnnotationRulerColumn ann = new AnnotationRulerColumn(10);
		// ruler.addDecorator(1, ann);
	}

	private void showLineNumberRuler() {
		// IVerticalRuler v = getVerticalRuler();
		// if (v instanceof CompositeRuler) {
		// CompositeRuler ruler = (CompositeRuler) v;
		// ruler.removeDecorator(0);
		// addLineNumberRuler(ruler);
		// }
	}

	private void hideLineNumberRuler() {
		// IVerticalRuler v = getVerticalRuler();
		// if (v instanceof CompositeRuler) {
		// CompositeRuler ruler = (CompositeRuler) v;
		// ruler.removeDecorator(0);
		// addDummyRuler(ruler);
		// }
	}

	public void setLineNumbers() {
		if (showNumbers)
			showLineNumberRuler();
		else
			hideLineNumberRuler();
	}

	public void setLineColor() {
		if (cursorPainter != null) {
			// cursorPainter.setHighlightColor(currentLineColor);
			// cursorPainter.paint(IPainter.CONFIGURATION);
		}
	}

	public void setHighlightColor() {
		// removeCursorPainter();
		for (int i = 0; i < highlights.size(); i++) {
			HighlightLine h = (HighlightLine) highlights.elementAt(i);
			h.changeColor(highlightedLineColor);
		}
		// addCursorPainter();
	}

	public IDocumentProvider getDocumentProvider() {
		if (dprovider == null)
			dprovider = new DocumentProvider();
		return dprovider;
	}

	public boolean isEditorInputModifiable() {
		return editable;
	}

	public boolean isEditorInputReadOnly() {
		return false;
	}

	public boolean isEditable() {
		return editable;
	}

	public boolean isDirty() {
		return changed;
	}

	public void setName(String name) {
		// setPartName(name);
	}

	public void setToolTip(String tooltip) {
		this.tooltip = tooltip;
		// this.setTitleToolTip(tooltip);
	}

	public String getTitleToolTip() {
		return tooltip;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		// this.validateEditorInputState();
	}

	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
		// configuration.setEventHandler(handler); // for undo events
		model.setEventHandler(handler);
	}

	public void setImage(Image icon) {
		// setTitleImage(icon);
	}

	public void setTextAt(String text, int cursorPosition, int length) {
		if (viewer != null) {
			Document document = (Document) viewer.getDocument();
			try {
				document.replace(cursorPosition, length, text);
			} catch (BadLocationException ex) {
				System.out.println(ex);
			}
		}
	}

	public void setText(String txt) {
		text.setText(txt);
		// if (viewer != null) {
		// Document document = (Document) viewer.getDocument();
		// try {
		// document.set(txt);
		// } catch (Exception ex) {
		// System.out.println(ex);
		// }
		// viewer.refresh();
		// }
	}

	public String getIdentity() {
		return identity;
	}

	public SinglelineScanner getScanner() {
		if (scanner == null)
			scanner = configuration.getTagScanner();
		return scanner;
	}

	public void addWordRule(String word, String color) {
		getScanner().addRule(word, color);
	}

	public void addMultilineRule(String start, String end, String color) {
		if (configuration != null) {
			String id = "partition" + (partitionNumber++);
			configuration.addPartition(viewer.getDocument(), id, start, end,
					color);
			dprovider.addRule(id, start, end);
		}
	}

	public void clearRules() {
		getScanner().clearRules();
	}

	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		// IWorkbenchPartSite iwps = this.getSite();
		MenuBuilder.calculateMenu(identity, menu, null);
		menu.add(new Separator("DocumentManagement"));
		menu.add(new Separator(ITextEditorActionConstants.GROUP_COPY));
		menu.add(new Separator(ITextEditorActionConstants.GROUP_FIND));
		menu.add(new Separator(ITextEditorActionConstants.GROUP_ADD));
		menu.add(new Separator(ITextEditorActionConstants.MB_ADDITIONS));
		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
		// ITextEditorActionConstants.CUT);
		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
		// ITextEditorActionConstants.COPY);
		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
		// ITextEditorActionConstants.PASTE);
		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
		// ITextEditorActionConstants.FIND);
		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
		// ITextEditorActionConstants.FIND_NEXT);
		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
		// ITextEditorActionConstants.FIND_PREVIOUS);
	}

	public void doSave(org.eclipse.core.runtime.IProgressMonitor progressMonitor) {
		Message m = handler.newMessage("saveText", 2);
		Value v1 = new Value(getIdentity());
		Value v2 = new Value(viewer.getDocument().get());
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	public void dispose() {
		removeListener();
		Message m = handler.newMessage("textClosed", 1);
		Value v1 = new Value(getIdentity());
		m.args[0] = v1;
		handler.raiseEvent(m);
		// MenuBuilder.dispose(getSite());
		model.dispose();
		// super.dispose();
	}

	public void undo() {
		// IAction action = getAction(ITextEditorActionConstants.UNDO);
		// action.run();
	}

	public void redo() {
		// IAction action = getAction(ITextEditorActionConstants.REDO);
		// action.run();
	}

	public void partActivated(IWorkbenchPartReference ref) {
		if (ref.getPart(false).equals(this) && handler != null) {
			Message m = handler.newMessage("focusGained", 1);
			Value v1 = new Value(getIdentity());
			m.args[0] = v1;
			handler.raiseEvent(m);
		}
	}

	public void partBroughtToTop(IWorkbenchPartReference ref) {
	}

	public void partClosed(IWorkbenchPartReference partRef) {
	}

	public void partDeactivated(IWorkbenchPartReference ref) {
		if (ref.getPart(false) != null && handler != null) {
			if (ref.getPart(false).equals(this) && handler != null) {
				Message m = handler.newMessage("focusLost", 1);
				Value v1 = new Value(getIdentity());
				m.args[0] = v1;
				handler.raiseEvent(m);
			}
		}
	}

	public void partOpened(IWorkbenchPartReference partRef) {
	}

	public void partHidden(IWorkbenchPartReference partRef) {
	}

	public void partVisible(IWorkbenchPartReference partRef) {
	}

	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

	public void setFocusInternal() {
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		// .activate(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newMenuAdded() {
		// calculateMenu();

	}
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ******************************************************************************/

class JavaLineStyler implements LineStyleListener {
	JavaScanner scanner = new JavaScanner();

	int[] tokenColors;

	Color[] colors;

	Vector blockComments = new Vector();

	public static final int EOF = -1;

	public static final int EOL = 10;

	public static final int WORD = 0;

	public static final int WHITE = 1;

	public static final int KEY = 2;

	public static final int COMMENT = 3;

	public static final int STRING = 5;

	public static final int OTHER = 6;

	public static final int NUMBER = 7;

	public static final int MAXIMUM_TOKEN = 8;

	public JavaLineStyler() {
		initializeColors();
		scanner = new JavaScanner();
	}

	Color getColor(int type) {
		if (type < 0 || type >= tokenColors.length) {
			return null;
		}
		return colors[tokenColors[type]];
	}

	boolean inBlockComment(int start, int end) {
		for (int i = 0; i < blockComments.size(); i++) {
			int[] offsets = (int[]) blockComments.elementAt(i);
			// start of comment in the line
			if ((offsets[0] >= start) && (offsets[0] <= end))
				return true;
			// end of comment in the line
			if ((offsets[1] >= start) && (offsets[1] <= end))
				return true;
			if ((offsets[0] <= start) && (offsets[1] >= end))
				return true;
		}
		return false;
	}

	void initializeColors() {
		Display display = Display.getDefault();
		colors = new Color[] { new Color(display, new RGB(0, 0, 0)), // black
				new Color(display, new RGB(255, 0, 0)), // red
				new Color(display, new RGB(0, 255, 0)), // green
				new Color(display, new RGB(0, 0, 255)) // blue
		};
		tokenColors = new int[MAXIMUM_TOKEN];
		tokenColors[WORD] = 0;
		tokenColors[WHITE] = 0;
		tokenColors[KEY] = 3;
		tokenColors[COMMENT] = 1;
		tokenColors[STRING] = 2;
		tokenColors[OTHER] = 0;
		tokenColors[NUMBER] = 0;
	}

	void disposeColors() {
		for (int i = 0; i < colors.length; i++) {
			colors[i].dispose();
		}
	}

	/**
	 * Event.detail line start offset (input) Event.text line text (input)
	 * LineStyleEvent.styles Enumeration of StyleRanges, need to be in order.
	 * (output) LineStyleEvent.background line background color (output)
	 */
	public void lineGetStyle(LineStyleEvent event) {
		Vector styles = new Vector();
		int token;
		StyleRange lastStyle;
		// If the line is part of a block comment, create one style for the
		// entire line.
		if (inBlockComment(event.lineOffset,
				event.lineOffset + event.lineText.length())) {
			styles.addElement(new StyleRange(event.lineOffset, event.lineText
					.length(), getColor(COMMENT), null));
			event.styles = new StyleRange[styles.size()];
			styles.copyInto(event.styles);
			return;
		}
		Color defaultFgColor = ((Control) event.widget).getForeground();
		scanner.setRange(event.lineText);
		token = scanner.nextToken();
		while (token != EOF) {
			if (token == OTHER) {
				// do nothing for non-colored tokens
			} else if (token != WHITE) {
				Color color = getColor(token);
				// Only create a style if the token color is different than the
				// widget's default foreground color and the token's style is
				// not
				// bold. Keywords are bolded.
				if ((!color.equals(defaultFgColor)) || (token == KEY)) {
					StyleRange style = new StyleRange(scanner.getStartOffset()
							+ event.lineOffset, scanner.getLength(), color,
							null);
					if (token == KEY) {
						style.fontStyle = SWT.BOLD;
					}
					if (styles.isEmpty()) {
						styles.addElement(style);
					} else {
						// Merge similar styles. Doing so will improve
						// performance.
						lastStyle = (StyleRange) styles.lastElement();
						if (lastStyle.similarTo(style)
								&& (lastStyle.start + lastStyle.length == style.start)) {
							lastStyle.length += style.length;
						} else {
							styles.addElement(style);
						}
					}
				}
			} else if ((!styles.isEmpty())
					&& ((lastStyle = (StyleRange) styles.lastElement()).fontStyle == SWT.BOLD)) {
				int start = scanner.getStartOffset() + event.lineOffset;
				lastStyle = (StyleRange) styles.lastElement();
				// A font style of SWT.BOLD implies that the last style
				// represents a java keyword.
				if (lastStyle.start + lastStyle.length == start) {
					// Have the white space take on the style before it to
					// minimize the number of style ranges created and the
					// number of font style changes during rendering.
					lastStyle.length += scanner.getLength();
				}
			}
			token = scanner.nextToken();
		}
		event.styles = new StyleRange[styles.size()];
		styles.copyInto(event.styles);
	}

	public void parseBlockComments(String text) {
		blockComments = new Vector();
		StringReader buffer = new StringReader(text);
		int ch;
		boolean blkComment = false;
		int cnt = 0;
		int[] offsets = new int[2];
		boolean done = false;

		try {
			while (!done) {
				switch (ch = buffer.read()) {
				case -1: {
					if (blkComment) {
						offsets[1] = cnt;
						blockComments.addElement(offsets);
					}
					done = true;
					break;
				}
				case '/': {
					ch = buffer.read();
					if ((ch == '*') && (!blkComment)) {
						offsets = new int[2];
						offsets[0] = cnt;
						blkComment = true;
						cnt++;
					} else {
						cnt++;
					}
					cnt++;
					break;
				}
				case '*': {
					if (blkComment) {
						ch = buffer.read();
						cnt++;
						if (ch == '/') {
							blkComment = false;
							offsets[1] = cnt;
							blockComments.addElement(offsets);
						}
					}
					cnt++;
					break;
				}
				default: {
					cnt++;
					break;
				}
				}
			}
		} catch (IOException e) {
			// ignore errors
		}
	}

	/**
	 * A simple fuzzy scanner for Java
	 */
	public class JavaScanner {

		protected Hashtable fgKeys = null;

		protected StringBuffer fBuffer = new StringBuffer();

		protected String fDoc;

		protected int fPos;

		protected int fEnd;

		protected int fStartToken;

		protected boolean fEofSeen = false;

		private String[] fgKeywords = { "abstract", "boolean", "break", "byte",
				"case", "catch", "char", "class", "continue", "default", "do",
				"double", "else", "extends", "false", "final", "finally",
				"float", "for", "if", "implements", "import", "instanceof",
				"int", "interface", "long", "native", "new", "null", "package",
				"private", "protected", "public", "return", "short", "static",
				"super", "switch", "synchronized", "this", "throw", "throws",
				"transient", "true", "try", "void", "volatile", "while" };

		public JavaScanner() {
			initialize();
		}

		/**
		 * Returns the ending location of the current token in the document.
		 */
		public final int getLength() {
			return fPos - fStartToken;
		}

		/**
		 * Initialize the lookup table.
		 */
		void initialize() {
			fgKeys = new Hashtable();
			Integer k = new Integer(KEY);
			for (int i = 0; i < fgKeywords.length; i++)
				fgKeys.put(fgKeywords[i], k);
		}

		/**
		 * Returns the starting location of the current token in the document.
		 */
		public final int getStartOffset() {
			return fStartToken;
		}

		/**
		 * Returns the next lexical token in the document.
		 */
		public int nextToken() {
			int c;
			fStartToken = fPos;
			while (true) {
				switch (c = read()) {
				case EOF:
					return EOF;
				case '/': // comment
					c = read();
					if (c == '/') {
						while (true) {
							c = read();
							if ((c == EOF) || (c == EOL)) {
								unread(c);
								return COMMENT;
							}
						}
					} else {
						unread(c);
					}
					return OTHER;
				case '\'': // char const
					character: for (;;) {
						c = read();
						switch (c) {
						case '\'':
							return STRING;
						case EOF:
							unread(c);
							return STRING;
						case '\\':
							c = read();
							break;
						}
					}

				case '"': // string
					string: for (;;) {
						c = read();
						switch (c) {
						case '"':
							return STRING;
						case EOF:
							unread(c);
							return STRING;
						case '\\':
							c = read();
							break;
						}
					}

				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					do {
						c = read();
					} while (Character.isDigit((char) c));
					unread(c);
					return NUMBER;
				default:
					if (Character.isWhitespace((char) c)) {
						do {
							c = read();
						} while (Character.isWhitespace((char) c));
						unread(c);
						return WHITE;
					}
					if (Character.isJavaIdentifierStart((char) c)) {
						fBuffer.setLength(0);
						do {
							fBuffer.append((char) c);
							c = read();
						} while (Character.isJavaIdentifierPart((char) c));
						unread(c);
						Integer i = (Integer) fgKeys.get(fBuffer.toString());
						if (i != null)
							return i.intValue();
						return WORD;
					}
					return OTHER;
				}
			}
		}

		/**
		 * Returns next character.
		 */
		protected int read() {
			if (fPos <= fEnd) {
				return fDoc.charAt(fPos++);
			}
			return EOF;
		}

		public void setRange(String text) {
			fDoc = text;
			fPos = 0;
			fEnd = fDoc.length() - 1;
		}

		protected void unread(int c) {
			if (c != EOF)
				fPos--;
		}
	}

}