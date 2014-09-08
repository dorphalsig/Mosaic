package uk.ac.mdx.xmf.swt.test;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.Overview;
import uk.ac.mdx.xmf.swt.Palette;
import uk.ac.mdx.xmf.swt.io.Provider;
import uk.ac.mdx.xmf.swt.misc.DisplayHelper;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Flowchart.
 */
public class Flowchart {
	
	/** The sash form. */
	private static SashForm sashForm = null;
	
	/** The sash form1. */
	private final SashForm sashForm1 = null;
	
	/** The view. */
	private final DiagramView view = null;
	
	/** The nodes. */
	static ArrayList<ProcessFigure> nodes = new ArrayList<ProcessFigure>();
	// private Overview overview = null;
	/** The palette. */
	private final Palette palette = null;
	
	/** The overview. */
	private final Overview overview = null;
	
	/** The screen width. */
	private static int screenWidth;
	
	/** The screen height. */
	private static int screenHeight;

	/**
	 * Process message.
	 *
	 * @param ms the ms
	 * @return true, if successful
	 */
	public static boolean processMessage(ArrayList<Message> ms) {
		ProcessFigure node;
		int i = 0;
		System.out.println("m:" + ms.size());
		for (Message message : ms) {
			if (message.hasName("newNode")) {
				int x = message.args[2].intValue;
				int y = message.args[3].intValue;
				int width = message.args[4].intValue;
				int height = message.args[5].intValue;
				boolean isSelectable = message.args[6].boolValue;
				node = new ProcessFigure();
				node.setName("class1");
				node.setBounds(new Rectangle(x, y, width + i * 10, height));
				nodes.add(node);
				i++;

			}

		}

		return false;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		Shell shell = new Shell();
		screenWidth = DisplayHelper.getScreenWidth();
		screenHeight = DisplayHelper.getScreenHeight();
		shell.setSize(screenWidth, screenHeight);
		shell.open();
		shell.setText("Flowchart");

		// sashForm = new SashForm(shell, SWT.BORDER);
		Canvas canvas = new Canvas(shell, SWT.NULL);
		canvas.setSize(screenWidth, screenHeight);
		canvas.setLocation(0, 0);

		GridData data = new GridData(GridData.FILL_BOTH);
		canvas.setLayoutData(data);

		LightweightSystem lws = new LightweightSystem(canvas);
		ChartFigure flowchart = new ChartFigure();
		lws.setContents(flowchart);

		Provider provider = new Provider();
		ArrayList<Message> ms = new ArrayList<Message>();
		// ms = provider.readMessage();
		processMessage(ms);

		for (ProcessFigure node : nodes) {
			flowchart.add(node);
			new Dnd(node);
		}
		System.out.println("nodes:" + nodes.size());
		Display display = Display.getDefault();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		// TerminatorFigure start = new TerminatorFigure();
		// start.setName("Start");
		// start.setBounds(new Rectangle(40, 20, 80, 20));
		// DecisionFigure dec = new DecisionFigure();
		// dec.setName("Should I?");
		// dec.setBounds(new Rectangle(30, 60, 100, 60));
		// ProcessFigure proc = new ProcessFigure();
		// proc.setName("Do it!");
		// proc.setBounds(new Rectangle(40, 140, 80, 40));
		// TerminatorFigure stop = new TerminatorFigure();
		// stop.setName("End");
		// stop.setBounds(new Rectangle(40, 200, 80, 20));
		//
		// PathFigure path1 = new PathFigure();
		// path1.setSourceAnchor(start.outAnchor);
		// path1.setTargetAnchor(dec.inAnchor);
		// PathFigure path2 = new PathFigure();
		// path2.setSourceAnchor(dec.yesAnchor);
		// path2.setTargetAnchor(proc.inAnchor);
		// PathFigure path3 = new PathFigure();
		// path3.setSourceAnchor(dec.noAnchor);
		// path3.setTargetAnchor(stop.inAnchor);
		// PathFigure path4 = new PathFigure();
		// path4.setSourceAnchor(proc.outAnchor);
		// path4.setTargetAnchor(stop.inAnchor);
		//
		// flowchart.add(start);
		// flowchart.add(dec);
		// flowchart.add(proc);
		// flowchart.add(stop);
		// flowchart.add(path1);
		// flowchart.add(path2);
		// flowchart.add(path3);
		// flowchart.add(path4);
		//
		// new Dnd(start);
		// new Dnd(proc);
		// new Dnd(dec);
		// new Dnd(stop);

	}
}

class ChartFigure extends FreeformLayeredPane {
	public ChartFigure() {
		setLayoutManager(new FreeformLayout());
		setBorder(new MarginBorder(5));
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}
}

class Dnd extends MouseMotionListener.Stub implements MouseListener {
	public Dnd(IFigure figure) {
		figure.addMouseMotionListener(this);
		figure.addMouseListener(this);
	}

	Point start;

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		start = e.getLocation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getLocation();
		Dimension d = p.getDifference(start);
		start = p;
		Figure f = ((Figure) e.getSource());
		f.setBounds(f.getBounds().getTranslated(d.width, d.height));
	}
}

class TerminatorFigure extends ActivityFigure {
	FixedAnchor inAnchor, outAnchor;

	public TerminatorFigure() {
		inAnchor = new FixedAnchor(this);
		inAnchor.place = new Point(1, 0);
		targetAnchors.put("in_term", inAnchor);
		outAnchor = new FixedAnchor(this);
		outAnchor.place = new Point(1, 2);
		sourceAnchors.put("out_term", outAnchor);
	}

	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = bounds;
		g.drawArc(r.x + r.width / 8, r.y, r.width / 4, r.height - 1, 90, 180);
		g.drawLine(r.x + r.width / 4, r.y, r.x + 3 * r.width / 4, r.y);
		g.drawLine(r.x + r.width / 4, r.y + r.height - 1,
				r.x + 3 * r.width / 4, r.y + r.height - 1);
		g.drawArc(r.x + 5 * r.width / 8, r.y, r.width / 4, r.height - 1, 270,
				180);
		g.drawText(message, r.x + 3 * r.width / 8, r.y + r.height / 8);
	}
}

class FixedAnchor extends AbstractConnectionAnchor {
	Point place;

	public FixedAnchor(IFigure owner) {
		super(owner);
	}

	@Override
	public Point getLocation(Point loc) {
		Rectangle r = getOwner().getBounds();
		int x = r.x + place.x * r.width / 2;
		int y = r.y + place.y * r.height / 2;
		Point p = new PrecisionPoint(x, y);
		getOwner().translateToAbsolute(p);
		return p;
	}
}

class DecisionFigure extends ActivityFigure {
	FixedAnchor inAnchor, yesAnchor, noAnchor;

	public DecisionFigure() {
		inAnchor = new FixedAnchor(this);
		inAnchor.place = new Point(1, 0);
		targetAnchors.put("in_dec", inAnchor);
		noAnchor = new FixedAnchor(this);
		noAnchor.place = new Point(2, 1);
		sourceAnchors.put("no", noAnchor);
		yesAnchor = new FixedAnchor(this);
		yesAnchor.place = new Point(1, 2);
		sourceAnchors.put("yes", yesAnchor);
	}

	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = bounds;
		PointList pl = new PointList(4);
		pl.addPoint(r.x + r.width / 2, r.y);
		pl.addPoint(r.x, r.y + r.height / 2);
		pl.addPoint(r.x + r.width / 2, r.y + r.height - 1);
		pl.addPoint(r.x + r.width, r.y + r.height / 2);
		g.drawPolygon(pl);
		g.drawText(message, r.x + r.width / 4 + 5, r.y + 3 * r.height / 8);
		g.drawText("N", r.x + 7 * r.width / 8, r.y + 3 * r.height / 8);
		g.drawText("Y", r.x + r.width / 2 - 2, r.y + 3 * r.height / 4);
	}
}

class PathFigure extends PolylineConnection {
	public PathFigure() {
		setTargetDecoration(new PolylineDecoration());
		setConnectionRouter(new ManhattanConnectionRouter());
	}
}

class ProcessFigure extends ActivityFigure {
	FixedAnchor inAnchor, outAnchor;

	public ProcessFigure() {
		inAnchor = new FixedAnchor(this);
		inAnchor.place = new Point(1, 0);
		targetAnchors.put("in_proc", inAnchor);
		outAnchor = new FixedAnchor(this);
		outAnchor.place = new Point(1, 2);
		sourceAnchors.put("out_proc", outAnchor);
	}

	@Override
	public void paintFigure(Graphics g) {
		Rectangle r = bounds;
		g.drawText(message, r.x + r.width / 4, r.y + r.height / 4);
		g.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
	}
}

abstract class ActivityFigure extends Figure {
	Rectangle r = new Rectangle();

	Hashtable targetAnchors = new Hashtable();

	Hashtable sourceAnchors = new Hashtable();

	String message = new String();

	public void setName(String msg) {
		message = msg;
		repaint();
	}

	public ConnectionAnchor ConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
		Hashtable conn = getSourceConnectionAnchors();
		conn.putAll(getTargetConnectionAnchors());
		Enumeration e = conn.elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public ConnectionAnchor getSourceConnectionAnchor(String name) {
		return (ConnectionAnchor) sourceAnchors.get(name);
	}

	public ConnectionAnchor getTargetConnectionAnchor(String name) {
		return (ConnectionAnchor) targetAnchors.get(name);
	}

	public String getSourceAnchorName(ConnectionAnchor c) {
		Enumeration e = sourceAnchors.keys();
		String name;
		while (e.hasMoreElements()) {
			name = (String) e.nextElement();
			if (sourceAnchors.get(name).equals(c))
				return name;
		}
		return null;
	}

	public String getTargetAnchorName(ConnectionAnchor c) {
		Enumeration e = targetAnchors.keys();
		String name;
		while (e.hasMoreElements()) {
			name = (String) e.nextElement();
			if (targetAnchors.get(name).equals(c))
				return name;
		}
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
		Enumeration e = getSourceConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public Hashtable getSourceConnectionAnchors() {
		return sourceAnchors;
	}

	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;
		Enumeration e = getTargetConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public Hashtable getTargetConnectionAnchors() {
		return targetAnchors;
	}
}

class FigureFactory {
	public static IFigure createTerminatorFigure() {
		return new TerminatorFigure();
	}

	public static IFigure createDecisionFigure() {
		return new DecisionFigure();
	}

	public static IFigure createProcessFigure() {
		return new ProcessFigure();
	}

	public static PathFigure createPathFigure() {
		return new PathFigure();
	}

	public static ChartFigure createChartFigure() {
		return new ChartFigure();
	}
}
