package com.ceteva.text.texteditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
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
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
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
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.misc.FontManager;
import uk.ac.mdx.xmf.swt.model.ImageManager;
import xos.Message;
import xos.Value;

import com.ceteva.menus.MenuBuilder;
import com.ceteva.menus.MenuListener;
import com.ceteva.menus.MenuManager;
import com.ceteva.text.TextPlugin;
import com.ceteva.text.highlighting.SinglelineScanner;
import com.ceteva.text.texteditor.JavaLineStyler.JavaScanner;

// TODO: Auto-generated Javadoc
/**
 * The Class TextEditor.
 */
public class TextEditor   implements MenuListener, IPropertyChangeListener,
		IPartListener2 {

	/** The partition number. */
	public int partitionNumber = 0;

	/** The model. */
	TextEditorModel model;
	
	/** The identity. */
	String identity = "";
	
	/** The tooltip. */
	String tooltip = "";
	
	/** The editable. */
	boolean editable = true;
	
	/** The changed. */
	boolean changed = false;
	
	/** The original name. */
	String originalName = "";
	
	/** The dprovider. */
	DocumentProvider dprovider;
	
	/** The viewer. */
	SourceViewer viewer;
	
	/** The cursor painter. */
	CursorLinePainter cursorPainter;
	
	/** The configuration. */
	TextConfiguration configuration;
	
	/** The source viewer decoration support. */
	SourceViewerDecorationSupport fSourceViewerDecorationSupport;
	
	/** The scanner. */
	SinglelineScanner scanner;
	
	/** The handler. */
	private EventHandler handler;
	
	/** The highlights. */
	Vector highlights = new Vector();
	
	/** The text. */
	public StyledText text;

	/** The show numbers. */
	boolean showNumbers = false;
	
	/** The current line color. */
	Color currentLineColor = null;
	
	/** The highlighted line color. */
	Color highlightedLineColor = null;
	
	/** The tab item. */
	public static CTabItem tabItem;

	/** The parent. */
	Composite parent;
	
	/** The length. */
	private int length = 0;
	
	/** The default font. */
	static FontData defaultFont = new FontData("Courier New", 16, SWT.NO);
	JavaLineStyler lineStyler ;
	/**
	 * Instantiates a new text editor.
	 */
	public TextEditor() {
		getPreferences();
		// registerAsListener();

	}

	/**
	 * Delete.
	 */
	public void delete() {
		TextPlugin textManager = TextPlugin.getDefault();
		// IWorkbenchPage page = textManager.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// page.closeEditor(this, false);
	}

	/**
	 * Register as listener.
	 */
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

	/**
	 * Adds the click listener.
	 */
	public void addClickListener() {
		// Action: copy selected text.
		final Action actionCut = new Action("&Cut",
				null) {
			public void run() {
				text.cut();
			}
		};
		
		final Action actionCopy = new Action("&Copy",
				null) {
			public void run() {
				text.copy();
			}
		};
		
		actionCopy.setAccelerator(SWT.CTRL + 'C');
		final Action actionPast = new Action("&Past",
				null) {
			public void run() {
				text.paste();
			}
		};
		
		actionCut.setAccelerator(SWT.CTRL + 'X');
		actionCopy.setAccelerator(SWT.CTRL + 'C');
		actionPast.setAccelerator(SWT.CTRL + 'V');

		if (text != null) {
			text.addListener(SWT.MouseMove, new Listener() {
				public void handleEvent(Event e) {
					// System.out.println("mouse double click");
					MenuBuilder.resetKeyBindings(null);
					org.eclipse.jface.action.MenuManager menu = new org.eclipse.jface.action.MenuManager();
					MenuBuilder.calculateMenu(getIdentity(), menu, null);
					menu.add(new Separator("DocumentManagement"));
					
					menu.add(actionCut);
					menu.add(actionCopy);
					menu.add(actionPast);
					
					
//					addAction(menu, ITextEditorActionConstants.GROUP_FIND, ITextEditorActionConstants.FIND);
//					addAction(menu, ITextEditorActionConstants.GROUP_FIND, ITextEditorActionConstants.FIND_NEXT);
//					addAction(menu, ITextEditorActionConstants.GROUP_FIND, ITextEditorActionConstants.FIND_PREVIOUS);
					
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
					// System.out.println("txt length:" + length);
					// System.out.println("etxt length:" + e.text.length());

					// check the input, if user input, thus enable user to save
					if (e.text.length() > 0 && e.text.length() < length) {
						setDirty();
					} 
				}
			});
			// add line number
			text.addLineStyleListener(new LineStyleListener()
			{
			    public void lineGetStyle(LineStyleEvent e)
			    {
			        //Set the line number
			        e.bulletIndex = text.getLineAtOffset(e.lineOffset);

			        //Set the style, 12 pixles wide for each digit
			        StyleRange style = new StyleRange();
			        style.metrics = new GlyphMetrics(0, 0, Integer.toString(text.getLineCount()+1).length()*12);

			        //Create and set the bullet
			        e.bullet = new Bullet(ST.BULLET_NUMBER,style);
			    }
			});
		}
	}

	/**
	 * Adds the action.
	 *
	 * @param menu the menu
	 * @param groupCopy the group copy
	 * @param cut the cut
	 */

	/**
	 * Raise event.
	 *
	 * @param m the m
	 */
	void raiseEvent(Message m) {
		if (true)
			handler.raiseEvent(m);
	}

	/**
	 * Removes the listener.
	 */
	public void removeListener() {
		// getSite().getPage().removePartListener(this);
		// IPreferenceStore preference = TextPlugin.getDefault()
		// .getPreferenceStore();
		// preference.removePropertyChangeListener(this);
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
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
		if (Main.getInstance().newFont!=null)
			defaultFont=Main.getInstance().newFont;
	}

	/**
	 * Inits the.
	 *
	 * @param paren the paren
	 * @param iInput the i input
	 * @param name the name
	 * @throws PartInitException the part init exception
	 */
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
					text.setEditable(true);
					text.setFont(FontManager.getFont(defaultFont, true));
					Color bg = Display.getDefault().getSystemColor(
							SWT.COLOR_WHITE);
					text.setBackground(bg);

					tabItem.setControl(text);

					tabItem.addDisposeListener(new DisposeListener() {

						@Override
						public void widgetDisposed(DisposeEvent arg0) {
							partDeactivated();
							dispose();
							Main.numberOfAddingItem--;
						}

					});

					Main.tabFolderDiagram.setSelection(tabItem);
					Main.numberOfAddingItem++;

				} catch (IOException io) {
					System.out.println(io);
				}
			} catch (CoreException cx) {
				System.out.println(cx);
			}
		}
		registerAsListener();
	}

	/**
	 * Gets the event handler.
	 *
	 * @return the event handler
	 */
	public EventHandler getEventHandler() {
		return handler;
	}

	/**
	 * Sets the dirty.
	 */
	public void setDirty() {
		if (!changed) {
			changed = true;
			// originalName = getPartName();
			// setPartName(" " + originalName);
			tabItem.setImage(ImageManager
					.getImage("icons/Parser/asterisk.gif"));
			Message m = handler.newMessage("textDirty", 2);
			Value v1 = new Value(getIdentity());
			Value v2 = new Value(true);
			m.args[0] = v1;
			m.args[1] = v2;
			handler.raiseEvent(m);
		}
	}

	/**
	 * Sets the clean.
	 */
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

	/**
	 * Adds the highlight.
	 *
	 * @param line the line
	 */
	public void addHighlight(int line) {
		if (viewer!=null){
		int lines = viewer.getTextWidget().getLineCount();
		if (line >= 0 && line < lines) {
			// removeCursorPainter();
			HighlightLine h = new HighlightLine(viewer, line,
					highlightedLineColor);
			highlights.add(h);
			// addCursorPainter();
		}
		}
	}

	/**
	 * Sets the cursor pos.
	 *
	 * @param position the new cursor pos
	 */
	public void setCursorPos(int position) {
		StyledText textWidget = viewer.getTextWidget();
		textWidget.setCaretOffset(position);
	}

	/**
	 * Gets the cursor pos.
	 *
	 * @return the cursor pos
	 */
	public int getCursorPos() {
		StyledText textWidget = viewer.getTextWidget();
		int pos = textWidget.getCaretOffset();
		return pos;
	}

	/**
	 * Show line.
	 *
	 * @param line the line
	 */
	public void showLine(int line) {
		if (viewer!=null){
		StyledText textWidget = viewer.getTextWidget();
		int lines = textWidget.getLineCount();
		if (line >= 0 && line < lines) {
			int offset = textWidget.getOffsetAtLine(line);
			// this.selectAndReveal(offset, 0);
		}
		}
	}

	/**
	 * Clear highlights.
	 */
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

	/**
	 * Creates the source viewer.
	 *
	 * @param parent the parent
	 * @param ruler the ruler
	 * @param styles the styles
	 * @return the i source viewer
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

	/**
	 * Gets the source viewer decoration support.
	 *
	 * @param viewer the viewer
	 * @return the source viewer decoration support
	 */
	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(
			ISourceViewer viewer) {
		if (fSourceViewerDecorationSupport == null) {
			fSourceViewerDecorationSupport = new SourceViewerDecorationSupport(
					viewer, null, null, null);
			configureSourceViewerDecorationSupport(fSourceViewerDecorationSupport);
		}
		return fSourceViewerDecorationSupport;
	}

	/**
	 * Configure source viewer decoration support.
	 *
	 * @param support the support
	 */
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		// support.setSymbolicFontName(getFontPropertyPreferenceKey());
	}

	/**
	 * Creates the vertical ruler.
	 *
	 * @return the i vertical ruler
	 */
	protected IVerticalRuler createVerticalRuler() {
		CompositeRuler ruler = new CompositeRuler();
		if (showNumbers)
			addLineNumberRuler(ruler);
		else
			addDummyRuler(ruler);
		return ruler;
	}

	/**
	 * Adds the line number ruler.
	 *
	 * @param ruler the ruler
	 */
	public void addLineNumberRuler(CompositeRuler ruler) {
		// LineNumberRulerColumn fLineNumberRulerColumn = new
		// LineNumberRulerColumn();
		// ruler.addDecorator(1, fLineNumberRulerColumn);
	}

	/**
	 * Adds the dummy ruler.
	 *
	 * @param ruler the ruler
	 */
	public void addDummyRuler(CompositeRuler ruler) {
		// AnnotationRulerColumn ann = new AnnotationRulerColumn(10);
		// ruler.addDecorator(1, ann);
	}

	/**
	 * Show line number ruler.
	 */
	private void showLineNumberRuler() {
		// IVerticalRuler v = getVerticalRuler();
		// if (v instanceof CompositeRuler) {
		// CompositeRuler ruler = (CompositeRuler) v;
		// ruler.removeDecorator(0);
		// addLineNumberRuler(ruler);
		// }
	}

	/**
	 * Hide line number ruler.
	 */
	private void hideLineNumberRuler() {
		// IVerticalRuler v = getVerticalRuler();
		// if (v instanceof CompositeRuler) {
		// CompositeRuler ruler = (CompositeRuler) v;
		// ruler.removeDecorator(0);
		// addDummyRuler(ruler);
		// }
	}

	/**
	 * Sets the line numbers.
	 */
	public void setLineNumbers() {
		if (showNumbers)
			showLineNumberRuler();
		else
			hideLineNumberRuler();
	}

	/**
	 * Sets the line color.
	 */
	public void setLineColor() {
		if (cursorPainter != null) {
			// cursorPainter.setHighlightColor(currentLineColor);
			// cursorPainter.paint(IPainter.CONFIGURATION);
		}
	}

	/**
	 * Sets the highlight color.
	 */
	public void setHighlightColor() {
		// removeCursorPainter();
		for (int i = 0; i < highlights.size(); i++) {
			HighlightLine h = (HighlightLine) highlights.elementAt(i);
			h.changeColor(highlightedLineColor);
		}
		// addCursorPainter();
	}

	/**
	 * Gets the document provider.
	 *
	 * @return the document provider
	 */
	public IDocumentProvider getDocumentProvider() {
		if (dprovider == null)
			dprovider = new DocumentProvider();
		return dprovider;
	}

	/**
	 * Checks if is editor input modifiable.
	 *
	 * @return true, if is editor input modifiable
	 */
	public boolean isEditorInputModifiable() {
		return editable;
	}

	/**
	 * Checks if is editor input read only.
	 *
	 * @return true, if is editor input read only
	 */
	public boolean isEditorInputReadOnly() {
		return false;
	}

	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty() {
		return changed;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		// setPartName(name);
	}

	/**
	 * Sets the tool tip.
	 *
	 * @param tooltip the new tool tip
	 */
	public void setToolTip(String tooltip) {
		this.tooltip = tooltip;
		// this.setTitleToolTip(tooltip);
	}

	/**
	 * Gets the title tool tip.
	 *
	 * @return the title tool tip
	 */
	public String getTitleToolTip() {
		return tooltip;
	}

	/**
	 * Sets the editable.
	 *
	 * @param editable the new editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		// this.validateEditorInputState();
	}

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
		// configuration.setEventHandler(handler); // for undo events
		model.setEventHandler(handler);
	}

	/**
	 * Sets the image.
	 *
	 * @param icon the new image
	 */
	public void setImage(Image icon) {
		// setTitleImage(icon);
	}

	/**
	 * Sets the text at.
	 *
	 * @param text the text
	 * @param cursorPosition the cursor position
	 * @param length the length
	 */
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

	/**
	 * Sets the text.
	 *
	 * @param txt the new text
	 */
	public void setText(final String txt) {
		
		Main.display.asyncExec( new java.lang.Runnable(){
            public void run()
            {
                text.setText( txt );
            }
        } );
		
//		text.setText(txt);
//		String textt=text.getText();
		if (lineStyler!=null)
		lineStyler.parseBlockComments(txt);
//		scanner.
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

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Gets the scanner.
	 *
	 * @return the scanner
	 */
	public SinglelineScanner getScanner() {
		configuration = new TextConfiguration(identity);
		if (scanner == null)
			scanner = configuration.getTagScanner();
		return scanner;
	}

	/**
	 * Adds the word rule.
	 *
	 * @param word the word
	 * @param color the color
	 */
	public void addWordRule(String word, String color) {
//		getScanner().addRule(word, color);
		JavaScanner.setKeywords(word, color);
		
	}

	/**
	 * Adds the multiline rule.
	 *
	 * @param start the start
	 * @param end the end
	 * @param color the color
	 */
	public void addMultilineRule(String start, String end, String color) {
		JavaScanner.initialize();
		lineStyler= new JavaLineStyler();
		text.addLineStyleListener(lineStyler);
//		if (configuration != null) {
//			String id = "partition" + (partitionNumber++);
//			configuration.addPartition(viewer.getDocument(), id, start, end,
//					color);
//			dprovider.addRule(id, start, end);
//		}
	}

	/**
	 * Clear rules.
	 */
	public void clearRules() {
		getScanner().clearRules();
	}

//	/**
//	 * Editor context menu about to show.
//	 *
//	 * @param menu the menu
//	 */
//	protected void editorContextMenuAboutToShow(IMenuManager menu) {
//		// IWorkbenchPartSite iwps = this.getSite();
//		MenuBuilder.calculateMenu(identity, menu, null);
//		menu.add(new Separator("DocumentManagement"));
//		menu.add(new Separator(ITextEditorActionConstants.GROUP_COPY));
//		menu.add(new Separator(ITextEditorActionConstants.GROUP_FIND));
//		menu.add(new Separator(ITextEditorActionConstants.GROUP_ADD));
//		menu.add(new Separator(ITextEditorActionConstants.MB_ADDITIONS));
//		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
//		// ITextEditorActionConstants.CUT);
//		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
//		// ITextEditorActionConstants.COPY);
//		// addAction(menu, ITextEditorActionConstants.GROUP_COPY,
//		// ITextEditorActionConstants.PASTE);
//		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
//		// ITextEditorActionConstants.FIND);
//		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
//		// ITextEditorActionConstants.FIND_NEXT);
//		// addAction(menu, ITextEditorActionConstants.GROUP_FIND,
//		// ITextEditorActionConstants.FIND_PREVIOUS);
//	}

	/**
	 * Do save.
	 *
	 * @param progressMonitor the progress monitor
	 */
	public void doSave(org.eclipse.core.runtime.IProgressMonitor progressMonitor) {
		Message m = handler.newMessage("saveText", 2);
		Value v1 = new Value(getIdentity());
		Value v2 = new Value(viewer.getDocument().get());
		m.args[0] = v1;
		m.args[1] = v2;
		handler.raiseEvent(m);
	}

	/**
	 * Dispose.
	 */
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

	/**
	 * Undo.
	 */
	public void undo() {
		// IAction action = getAction(ITextEditorActionConstants.UNDO);
		// action.run();
	}

	/**
	 * Redo.
	 */
	public void redo() {
		// IAction action = getAction(ITextEditorActionConstants.REDO);
		// action.run();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partActivated(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partActivated(IWorkbenchPartReference ref) {
		if (ref.getPart(false).equals(this) && handler != null) {
			Message m = handler.newMessage("focusGained", 1);
			Value v1 = new Value(getIdentity());
			m.args[0] = v1;
			handler.raiseEvent(m);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partBroughtToTop(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partBroughtToTop(IWorkbenchPartReference ref) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partClosed(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	/**
	 * Part deactivated.
	 */
	public void partDeactivated() {

		if (handler != null) {
			Message m = handler.newMessage("focusLost", 1);
			Value v1 = new Value(getIdentity());
			m.args[0] = v1;
			handler.raiseEvent(m);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partOpened(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partOpened(IWorkbenchPartReference partRef) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partHidden(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partVisible(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partVisible(IWorkbenchPartReference partRef) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partInputChanged(org.eclipse.ui.IWorkbenchPartReference)
	 */
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

	/**
	 * Sets the focus internal.
	 */
	public void setFocusInternal() {
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
		// .activate(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ceteva.menus.MenuListener#newMenuAdded()
	 */
	@Override
	public void newMenuAdded() {
		// calculateMenu();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener2#partDeactivated(org.eclipse.ui.IWorkbenchPartReference)
	 */
	@Override
	public void partDeactivated(IWorkbenchPartReference arg0) {
		// TODO Auto-generated method stub

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
				new Color(display, new RGB(0, 55, 0)), // green
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
				case '@': {
					ch = buffer.read();
					if ((ch == 'D') && (!blkComment)) {
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
				case 'n': {
					if (blkComment) {
						ch = buffer.read();
						cnt++;
						if (ch == 'd') {
							blkComment = false;
							offsets[1] = cnt;
							blockComments.addElement(offsets);
						}
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
	public static class JavaScanner {

		protected static Hashtable fgKeys = null;

		protected StringBuffer fBuffer = new StringBuffer();

		protected String fDoc;

		protected int fPos;

		protected int fEnd;

		protected int fStartToken;

		protected boolean fEofSeen = false;

//		private static String[] fgKeywords = { "@",":=",":","import"};
		private static final ArrayList<String> fgKeywords=new ArrayList<String>();

		public JavaScanner() {
			initialize();
		}
        public static void setKeywords(String word, String color){
        	   fgKeywords.add(word);
        	  
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
		static void initialize() {
			fgKeys = new Hashtable();
			Integer k = new Integer(KEY);
			for (int i = 0; i < fgKeywords.size(); i++)
				fgKeys.put(fgKeywords.get(i), k);
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
					if (Character.isJavaIdentifierStart((char) c))
					{
						fBuffer.setLength(0);
						do 
						{
							fBuffer.append((char) c);
							c = read();
						} 
						while (Character.isJavaIdentifierPart((char) c));
						unread(c);
						Integer i = (Integer) fgKeys.get(fBuffer.toString());
						if (i != null)
							return i.intValue();
						return WORD;
					}
//					if (Character.isJavaIdentifierStart((char) c))
					{
						fBuffer.setLength(0);
//						do 
						{
							fBuffer.append((char) c);
							c = read();
						} 
//						while (Character.isSpace((char) c));
						unread(c);
						Integer i = (Integer) fgKeys.get(fBuffer.toString());
						if (i != null){
							return i.intValue();
//						return WORD;
					}
					}
//					return OTHER;
					
					
//					return OTHER;
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