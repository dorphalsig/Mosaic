package uk.ac.mdx.xmf.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Composite;

import uk.ac.mdx.xmf.swt.editPart.EdgeEditPart;
import uk.ac.mdx.xmf.swt.editPart.EdgeTextEditPart;
import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.editPart.TextEditPart;
import uk.ac.mdx.xmf.swt.figure.BoxFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeLabelFigure;
import uk.ac.mdx.xmf.swt.figure.MultilineTextFigure;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.EdgeText;
import uk.ac.mdx.xmf.swt.model.Node;
import uk.ac.mdx.xmf.swt.model.Text;

public abstract class View extends Composite implements KeyListener,
		MouseListener, MouseMoveListener, MouseTrackListener, DisposeListener,
		PaintListener, SelectionListener, DragSourceListener,
		DropTargetListener, ControlListener, ShellListener {

	static Map<String, Node> nodeModels = new HashMap<String, Node>();
	static Map<String, NodeEditPart> nodeEditParts = new HashMap<String, NodeEditPart>();
	static Map<String, Figure> nodeShapes = new HashMap<String, Figure>();
	static Map<String, Figure> edgeShapes = new HashMap<String, Figure>();
	static Map<String, Figure> figureNodes = new HashMap<String, Figure>();
	static Map<String, TextEditPart> textEdits = new HashMap<String, TextEditPart>();
	static Map<String, Text> texts = new HashMap<String, Text>();
	static Map<String, Label> figureLabels = new HashMap<String, Label>();
	static Map<String, MultilineTextFigure> figureMulitLineTextLabels = new HashMap<String, MultilineTextFigure>();
	static Map<String, BoxFigure> figureBoxs = new HashMap<String, BoxFigure>();
	static Map<String, EdgeEditPart> edgeEditPartFigures = new HashMap<String, EdgeEditPart>();
	static Map<String, Edge> edgeModels = new HashMap<String, Edge>();
	static Map<String, EdgeFigure> edgeFigures = new HashMap<String, EdgeFigure>();
	static Map<String, EdgeLabelFigure> edgeLabelFigures = new HashMap<String, EdgeLabelFigure>();
	static Map<String, EdgeEditPart> edgeEditParts = new HashMap<String, EdgeEditPart>();
	static Map<String, EdgeText> edgeTexts = new HashMap<String, EdgeText>();
	static Map<String, EdgeTextEditPart> edgeTextEditParts = new HashMap<String, EdgeTextEditPart>();

	public View(Composite parent, int style) {
		super(parent, style);
		// parent.getShell().addMouseListener(this);
		// addPaintListener(this);
		// addMouseListener(this);
		// TODO Auto-generated constructor stub
	}

	public void updateView() {
		redraw();
	}

	// overvide the functiont to the visualization
	public void display() {

	}

	@Override
	public void shellActivated(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shellClosed(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shellDeactivated(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shellDeiconified(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shellIconified(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controlMoved(ControlEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controlResized(ControlEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragEnter(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragLeave(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOperationChanged(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropAccept(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragFinished(DragSourceEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("dragFinished click");
	}

	@Override
	public void dragSetData(DragSourceEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragStart(DragSourceEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paintControl(PaintEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetDisposed(DisposeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEnter(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExit(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseHover(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMove(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("view click");
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("view click");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
