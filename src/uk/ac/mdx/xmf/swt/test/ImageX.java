package uk.ac.mdx.xmf.swt.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import uk.ac.mdx.xmf.swt.Palette;
import uk.ac.mdx.xmf.swt.View;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageX.
 */
public class ImageX {

	/** The sash form. */
	private static SashForm sashForm = null;
	
	/** The sash form1. */
	private static SashForm sashForm1 = null;
	
	/** The view. */
	private final View view = null;
	// private Overview overview = null;
	/** The palette. */
	private static Palette palette = null;
	
	/** The display. */
	static Display display = new Display();
	
	/** The screen height. */
	private int screenWidth, screenHeight;

	/**
	 * This method initializes sashForm1.
	 *
	 * @param display the display
	 */
	private static void createSashForm1(Display display) {
		sashForm1 = new SashForm(sashForm, SWT.BORDER);
		createPalette();

		final Image image = new Image(display, "images\\class_obj.gif");

		sashForm1.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event e) {
				GC gc = e.gc;
				int x = 10, y = 10;
				gc.drawImage(image, x, y);
				gc.dispose();
			}
		});

		sashForm1.setOrientation(org.eclipse.swt.SWT.VERTICAL);
		sashForm1.setBounds(new org.eclipse.swt.graphics.Rectangle(142, 87, 15,
				5));
	}

	/**
	 * This method initializes Palette.
	 */
	private static void createPalette() {
		// palette = new Palette(sashForm1, SWT.V_SCROLL, display);
		// // palette.createPartControl();
		// palette.setBounds(new org.eclipse.swt.graphics.Rectangle(88, 161, 50,
		// 50));
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setLayout(new FillLayout());

		sashForm = new SashForm(shell, SWT.BORDER);

		createSashForm1(display);
		// createView();

		// int weights[] = new int[2];
		// weights[0] = 30;
		// weights[1] = 70;
		// sashForm.setWeights(weights);

		shell.setSize(600, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

}
