package uk.ac.mdx.xmf.swt.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// TODO: Auto-generated Javadoc
/**
 * The Class HelloWorld2.
 */
public class HelloWorld2 {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		Shell shell = new Shell();
		shell.setText("Draw2d Hello World");
		shell.setSize(300, 300);
		shell.open();

		// create content 4 shell.
		createContent4Shell(shell);

		while (!shell.isDisposed()) {
			if (!Display.getDefault().readAndDispatch())
				Display.getDefault().sleep();
		}
	}

	/**
	 * Creates the content4 shell.
	 *
	 * @param shell the shell
	 */
	private static void createContent4Shell(Shell shell) {
		Panel rootFigure = new Panel();
		rootFigure.setLayoutManager(new XYLayout());

		IFigure figure1 = new Ellipse();
		Ellipse figure2 = new Ellipse();

		// --------------------------------------------------------
		// add connection
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new ChopboxAnchor(figure1));
		connection.setTargetAnchor(new EllipseAnchor(figure2));

		// --------------------------------------------------------
		// add Decoration to Connection
		connection.setSourceDecoration(new PolygonDecoration());

		// --------------------------------------------------------
		// add Labels to Connection
		ConnectionEndpointLocator Locator1 = new ConnectionEndpointLocator(
				connection, true);
		Locator1.setVDistance(30);
		connection.add(new Label("1..*"), Locator1);

		ConnectionEndpointLocator Locator2 = new ConnectionEndpointLocator(
				connection, false);
		Locator2.setVDistance(15);
		connection.add(new Label("22..*"), Locator2);

		ConnectionEndpointLocator Locator3 = new ConnectionEndpointLocator(
				connection, true);
		Locator3.setUDistance(80);
		Locator3.setVDistance(-20);
		connection.add(new Label("3333..*"), Locator3);

		// --------------------------------------------------------
		// add Router to Connection
		BendpointConnectionRouter router = new BendpointConnectionRouter();
		connection.setConnectionRouter(router);

		figure1.setBounds(new Rectangle(10, 10, 60, 30));
		figure2.setBounds(new Rectangle(170, 170, 90, 90));

		List list = new ArrayList();
		list.add(new AbsoluteBendpoint(170, 20));
		connection.setRoutingConstraint(list);
		connection.setOutline(true);

		// rootFigure.add(figure1, new Rectangle(10, 10, 60, 30)); // the reason
		// why can not drag, because add(figure, constraint, -1); adding
		// constraint
		// rootFigure.add(figure2, new Rectangle(170, 170, 90, 90));
		rootFigure.add(figure1);
		rootFigure.add(figure2);

		rootFigure.add(connection);

		new D(figure1);
		new D(figure2);
		new D(connection);

		LightweightSystem lws = new LightweightSystem(shell);
		lws.setContents(rootFigure);
	}
}

class D extends MouseMotionListener.Stub implements MouseListener {
	Point start;

	public D(IFigure figure) {
		figure.addMouseMotionListener(this);
		figure.addMouseListener(this);
	}

	public void mouseReleased(MouseEvent e) {
		Figure f = ((Figure) e.getSource());
		f.setCursor(null);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDoubleClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Figure f = ((Figure) e.getSource());
		f.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_SIZEALL));
		start = e.getLocation();
	}

	public void mouseDragged(MouseEvent e) {
		if (start == null) {
			return;
		}
		Point p = e.getLocation();
		Dimension d = p.getDifference(start);
		start = p;
		Figure f = ((Figure) e.getSource());
		f.setBounds(f.getBounds().getTranslated(d.width, d.height));
	}
}