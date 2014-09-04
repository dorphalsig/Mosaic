package uk.ac.mdx.xmf.swt.demo;

import java.util.Vector;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import uk.ac.mdx.xmf.swt.DiagramClient;
import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.Overview;
import uk.ac.mdx.xmf.swt.Palette;
import uk.ac.mdx.xmf.swt.io.Provider;
import uk.ac.mdx.xmf.swt.misc.DisplayHelper;
import xos.OperatingSystem;

import com.ceteva.console.views.ConsoleView;
import com.ceteva.dialogs.DialogsClient;
import com.ceteva.forms.FormsClient;
import com.ceteva.forms.views.FormView;
import com.ceteva.menus.MenusClient;
import com.ceteva.modelBrowser.ModelBrowserClient;
import com.ceteva.mosaic.WorkbenchClient;
import com.ceteva.mosaic.actions.ShowPres;
import com.ceteva.oleBridge.OleBridgeClient;
import com.ceteva.text.EditorClient;
import com.ceteva.undo.UndoClient;

public class Main {

	public static org.eclipse.swt.widgets.Shell shell = null;
	public static MenuManager mb;
	private SashForm sashForm = null;
	private SashForm sectionToolBar = null;
	public static SashForm sectionTop = null;
	public static SashForm sectionTopLeft = null;
	public static SashForm sectionTopMiddle = null;
	public static SashForm sectionBottom = null;
	public static SashForm sectionBottomMiddle = null;
	public static SashForm sectionBottomRight = null;

	public static CTabFolder tabFolderOutline;
	public static CTabFolder tabFolderDiagram;
	public static CTabFolder tabFolderOverview;
	public static CTabFolder tabFolderProperty;
	public static TabFolder tabFolderConsole;

	public static DiagramView view = null;
	public static Palette palette = null;
	public static Overview overview = null;
	public static ConsoleView consoleView;
	public static FormView propertyView;
	private int screenWidth, screenHeight;
	public static Display display;
	public static Provider provider;
	public static boolean antialias = true;

	public static boolean isOpen = true;

	public Vector<DiagramView> views = new Vector<DiagramView>();
	public Vector<Palette> palettes = new Vector<Palette>();
	private DiagramClient diagramClient;

	private volatile static Main instance = null;

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}

	/**
	 * This method initializes shell
	 */

	@SuppressWarnings("deprecation")
	private void createshell() {
		shell = new Shell(SWT.BORDER | SWT.SHELL_TRIM);
		shell.setText("GUI2-XMF2");
		shell.setLayout(new FillLayout());

		shell.setLocation(0, 0);
		screenWidth = DisplayHelper.getScreenWidth();
		screenHeight = DisplayHelper.getScreenHeight();
		shell.setSize(new org.eclipse.swt.graphics.Point(screenWidth,
				screenHeight));

		sashForm = new SashForm(shell, SWT.VERTICAL);
		sectionToolBar = new SashForm(sashForm, SWT.HORIZONTAL);
		sectionTop = new SashForm(sashForm, SWT.HORIZONTAL);
		sectionBottom = new SashForm(sashForm, SWT.HORIZONTAL);
		sashForm.setWeights(new int[] { 3, 77, 22 });

		sectionTopLeft = new SashForm(sectionTop, SWT.HORIZONTAL);
		sectionTopMiddle = new SashForm(sectionTop, SWT.HORIZONTAL);
		sectionTop.setWeights(new int[] { 20, 80 }); // ini size of each
														// editor part

		sectionBottomMiddle = new SashForm(sectionBottom, SWT.HORIZONTAL);
		sectionBottomRight = new SashForm(sectionBottom, SWT.HORIZONTAL);
		sectionBottom.setWeights(new int[] { 50, 50 }); // ini size of

		// Create the outline tabs
		tabFolderOutline = new CTabFolder(sectionTopLeft, SWT.BORDER);
		tabFolderOutline.setBorderVisible(true);
		tabFolderOutline.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
			}
		});

		// Create the diagram tabs
		tabFolderDiagram = new CTabFolder(sectionTopMiddle, SWT.BORDER);
		tabFolderDiagram.setBorderVisible(true);
		tabFolderDiagram.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
			}
		});
		tabFolderDiagram.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent event) {
				int index = tabFolderDiagram.getSelectionIndex();
				if (views.size() > 0 && index != 0) {
					views.get(index - 1).setFocus(true, views);
					palettes.get(index - 1).setFocus(true, palettes);
				}
			}
		});

		// create overview
		// tabFolderOverview = new CTabFolder(sectionBottomLeft, SWT.BORDER);
		// tabFolderOverview.setBorderVisible(true);
		// tabFolderOverview.addCTabFolderListener(new CTabFolderAdapter() {
		// public void itemClosed(CTabFolderEvent event) {
		// sectionBottom.setWeights(new int[] { 0, 50, 50 });
		// }
		// });
		// dynamic show the contents of overview after the discussion with users

		// CTabItem tabItemOverview = new CTabItem(tabFolderOverview,
		// SWT.BORDER);
		// tabItemOverview.setText("");
		// overview = new Overview(tabFolderOverview, SWT.BORDER,
		// tabItemOverview);

		// create propertyView
		// Create the tabs
		tabFolderProperty = new CTabFolder(sectionBottomMiddle, SWT.BORDER);
		tabFolderProperty.setBorderVisible(true);
		tabFolderProperty.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
			}
		});
		CTabItem tabItemProperty = new CTabItem(tabFolderProperty, SWT.BORDER);
		tabItemProperty.setText("Property");
		propertyView = new FormView(tabFolderProperty, SWT.BORDER,
				tabItemProperty);
		tabFolderProperty.setVisible(true);// set invisiable for debug
		// create console view
		// Create the tabs
		tabFolderConsole = new TabFolder(sectionBottomRight, SWT.BORDER);
		tabFolderConsole.setVisible(true);
		TabItem tabItemConsole = new TabItem(tabFolderConsole, SWT.BORDER);
		tabItemConsole.setText("Console");
		consoleView = new ConsoleView(tabFolderConsole, SWT.BORDER,
				tabItemConsole);

		final OperatingSystem xos = new OperatingSystem();

		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					xos.init();
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		t.start();

		final EditorClient editorClient = new EditorClient();

		Thread tEditorClient = new Thread() {
			@Override
			public void run() {
				try {

					xos.newMessageClient("com.ceteva.text", editorClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tEditorClient.start();

		final WorkbenchClient workbenchClient = new WorkbenchClient();

		Thread tWorkbenchClient = new Thread() {
			@Override
			public void run() {
				try {

					xos.newMessageClient("com.ceteva.mosaic", workbenchClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tWorkbenchClient.start();

		final MenusClient menusClient = new MenusClient();

		Thread tmenusClient = new Thread() {
			@Override
			public void run() {
				try {

					xos.newMessageClient("com.ceteva.menus", menusClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tmenusClient.start();

		final ModelBrowserClient modelBrowserClient = new ModelBrowserClient();
		Thread tmodelBrowserClient = new Thread() {
			@Override
			public void run() {
				try {

					xos.newMessageClient("com.ceteva.modelBrowser",
							modelBrowserClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tmodelBrowserClient.start();

		diagramClient = new DiagramClient();
		Thread tdiagramClient = new Thread() {
			@Override
			public void run() {
				try {
					xos.newMessageClient("com.ceteva.diagram", diagramClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tdiagramClient.start();

		final DialogsClient dialogClient = new DialogsClient();

		Thread tdialogClient = new Thread() {
			@Override
			public void run() {
				try {
					xos.newMessageClient("com.ceteva.dialogs", dialogClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tdialogClient.start();

		final FormsClient formsClient = new FormsClient();

		Thread tformsClient = new Thread() {
			@Override
			public void run() {
				try {
					xos.newMessageClient("com.ceteva.forms", formsClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tformsClient.start();

		final UndoClient undoClient = new UndoClient();
		Thread tundoClient = new Thread() {
			@Override
			public void run() {
				try {
					xos.newMessageClient("com.ceteva.undo", undoClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		tundoClient.start();

		final OleBridgeClient oldBridgeClient = new OleBridgeClient();

		Thread toldBridgeClient = new Thread() {
			@Override
			public void run() {
				try {
					xos.newMessageClient("com.ceteva.oleBridge",
							oldBridgeClient);
				} catch (Throwable t) {
					System.out.println(t);
					t.printStackTrace();
				}
			}
		};
		toldBridgeClient.start();

		mb = new MenuManager();
		// initialize root menu
		MenuManager windowsMenuManager = new MenuManager("&Windows", "&windows");
		MenuManager helpMenuManager = new MenuManager("&Help");

		mb.add(windowsMenuManager);
		windowsMenuManager.add(new ShowPres());

		mb.add(helpMenuManager);
		helpMenuManager.add(new ShowPres());

		mb.updateAll(true);
		shell.setMenuBar(mb.createMenuBar((Decorations) shell));

		final ToolBar treeToolBar = new ToolBar(sectionToolBar, SWT.NONE);
		treeToolBar
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final ToolItem item1 = new ToolItem(treeToolBar, SWT.PUSH | SWT.CENTER);
		item1.setImage(new Image(display, "icons/Tools/box.gif"));

		final ToolItem item2 = new ToolItem(treeToolBar, SWT.PUSH | SWT.CENTER);
		item2.setImage(new Image(display, "icons/user/Arrow2Left.gif"));

		final ToolItem item3 = new ToolItem(treeToolBar, SWT.PUSH | SWT.CENTER);
		item3.setImage(new Image(display, "icons/user/Arrow2Left.gif"));

		treeToolBar.pack();
		//
		treeToolBar.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}

		});

	}

	public void startNewDiagram(String identity,
			uk.ac.mdx.xmf.swt.model.Diagram diagram) {
		CTabItem tabItem = new CTabItem(tabFolderDiagram, SWT.BORDER);
		tabItem.setText(diagram.getName());

		SashForm sashFormDiagram;
		sashFormDiagram = new SashForm(tabFolderDiagram, SWT.BORDER);
		sashFormDiagram.setBounds(0, 0, tabFolderDiagram.getBounds().width,
				tabFolderDiagram.getBounds().height);
		sashFormDiagram.setBackground(ColorConstants.white);
		tabItem.setControl(sashFormDiagram);

		palette = new Palette(sashFormDiagram, SWT.BORDER, display);
		palettes.add(palette);
		palette.setFocus(true, palettes);

		DiagramView view = new DiagramView(sashFormDiagram, SWT.NONE, palette,
				display, diagramClient, diagram, tabItem);
		views.add(view);
		view.setIdentity(identity);
		view.setFocus(true, views);

		diagram.setDisplayedDiagram(diagram);
		diagram.setDiagramView(view);
		diagram.setOwner(view);
		view.display();

		sashFormDiagram.setWeights(new int[] { 15, 85 });
	}

	public DiagramView getView() {
		for (DiagramView view : views) {
			if (view.isFocus())
				return view;
		}
		return null;
	}

	public Palette getPalette() {
		for (Palette palette : palettes) {
			if (palette.isFocus())
				return palette;
		}
		return null;
	}

	// public void createExampleShell() {
	// shell = new Shell(SWT.BORDER | SWT.SHELL_TRIM);
	// shell.setText("SWT GUI");
	// // shell.setLayout(new FillLayout());
	//
	// shell.setLocation(0, 0);
	// screenWidth = DisplayHelper.getScreenWidth();
	// screenHeight = DisplayHelper.getScreenHeight();
	// shell.setSize(new org.eclipse.swt.graphics.Point(screenWidth,
	// screenHeight));
	//
	// double barRatio = 0.03;
	// double topRatio = 0.7;
	// double bottomRatio = 0.27;
	// double leftRatio = 0.2;
	//
	// ViewForm toolBar = new ViewForm(shell, SWT.BORDER);
	//
	// ViewForm outlineView = new ViewForm(shell, SWT.BORDER);
	// ViewForm diagramView = new ViewForm(shell, SWT.BORDER);
	// ViewForm overView = new ViewForm(shell, SWT.BORDER);
	// ViewForm editorView = new ViewForm(shell, SWT.BORDER);
	//
	// toolBar.setBounds(0, 0, screenWidth, (int) (screenHeight * barRatio));
	// outlineView.setBounds(0, (int) (screenHeight * barRatio),
	// (int) (screenWidth * leftRatio),
	// (int) (screenHeight * topRatio));
	// diagramView.setBounds((int) (screenHeight * barRatio) + 1,
	// (int) (screenHeight * barRatio),
	// (int) (screenWidth * (1 - leftRatio)),
	// (int) (screenHeight * topRatio));
	//
	// overView.setBounds(0, (int) (screenHeight * (barRatio + topRatio)),
	// (int) (screenWidth * leftRatio),
	// (int) (screenHeight * bottomRatio));
	//
	// final TabFolder tabFolder = new TabFolder(outlineView, SWT.BORDER);
	// for (int i = 0; i < 2; i++) {
	// // ViewForm viewForm=new ViewForm(tabFolder,SWT.NORMAL);
	// TabItem item = new TabItem(tabFolder, SWT.NONE);
	// item.setText("TabItem " + i);
	//
	// SashForm sashForm = new SashForm(tabFolder, SWT.NORMAL);
	// sashForm.setBounds(0, (int) (screenHeight * barRatio),
	// (int) (screenWidth * leftRatio),
	// (int) (screenHeight * topRatio));
	// item.setControl(sashForm);
	//
	// }
	// tabFolder.pack();
	// }

	public static void main(String[] args) {
		/*
		 * Before this is run, be sure to set up correct SWT library path, and
		 * SWT type for different OS
		 */
		display = new Display();
		// SplashStartup splash = new SplashStartup();
		// splash.iniSplash(); // make user choose previous work

		Main.getInstance().createshell();
		Main.getInstance();
		Main.shell.open();
		while (!Main.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		System.exit(0); // exit successful
	}
}
