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
import uk.ac.mdx.xmf.swt.editPart.MultilineTextEditPart;
import uk.ac.mdx.xmf.swt.editPart.NodeEditPart;
import uk.ac.mdx.xmf.swt.editPart.TextEditPart;
import uk.ac.mdx.xmf.swt.figure.BoxFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeFigure;
import uk.ac.mdx.xmf.swt.figure.EdgeLabelFigure;
import uk.ac.mdx.xmf.swt.figure.MultilineTextFigure;
import uk.ac.mdx.xmf.swt.model.Edge;
import uk.ac.mdx.xmf.swt.model.EdgeText;
import uk.ac.mdx.xmf.swt.model.MultilineText;
import uk.ac.mdx.xmf.swt.model.Node;
import uk.ac.mdx.xmf.swt.model.Text;

// TODO: Auto-generated Javadoc
/**
 * The Class View.
 */
public abstract class View extends Composite implements KeyListener,
		MouseListener, MouseMoveListener, MouseTrackListener, DisposeListener,
		PaintListener, SelectionListener, DragSourceListener,
		DropTargetListener, ControlListener, ShellListener {

	/** The node models. */
	static Map<String, Node> nodeModels = new HashMap<String, Node>();
	
	/** The node edit parts. */
	static Map<String, NodeEditPart> nodeEditParts = new HashMap<String, NodeEditPart>();
	
	/** The node shapes. */
	static Map<String, Figure> nodeShapes = new HashMap<String, Figure>();
	
	/** The edge shapes. */
	static Map<String, Figure> edgeShapes = new HashMap<String, Figure>();
	
	/** The figure nodes. */
	static Map<String, Figure> figureNodes = new HashMap<String, Figure>();
	
	/** The text edits. */
	static Map<String, TextEditPart> textEdits = new HashMap<String, TextEditPart>();
	
	/** The multiline text edits. */
	static Map<String, MultilineTextEditPart> multilineTextEdits = new HashMap<String, MultilineTextEditPart>();
	
	/** The texts. */
	static Map<String, Text> texts = new HashMap<String, Text>();
	
	/** The multiline texts. */
	static Map<String, MultilineText> multilineTexts = new HashMap<String, MultilineText>();
	
	/** The figure labels. */
	static Map<String, Label> figureLabels = new HashMap<String, Label>();
	
	/** The figure mulit line text labels. */
	static Map<String, MultilineTextFigure> figureMulitLineTextLabels = new HashMap<String, MultilineTextFigure>();
	
	/** The figure boxs. */
	static Map<String, BoxFigure> figureBoxs = new HashMap<String, BoxFigure>();
	
	/** The edge edit part figures. */
	static Map<String, EdgeEditPart> edgeEditPartFigures = new HashMap<String, EdgeEditPart>();
	
	/** The edge models. */
	static Map<String, Edge> edgeModels = new HashMap<String, Edge>();
	
	/** The edge figures. */
	static Map<String, EdgeFigure> edgeFigures = new HashMap<String, EdgeFigure>();
	
	/** The edge label figures. */
	static Map<String, EdgeLabelFigure> edgeLabelFigures = new HashMap<String, EdgeLabelFigure>();
	
	/** The edge edit parts. */
	static Map<String, EdgeEditPart> edgeEditParts = new HashMap<String, EdgeEditPart>();
	
	/** The edge texts. */
	static Map<String, EdgeText> edgeTexts = new HashMap<String, EdgeText>();
	
	/** The edge text edit parts. */
	static Map<String, EdgeTextEditPart> edgeTextEditParts = new HashMap<String, EdgeTextEditPart>();

	/**
	 * Instantiates a new view.
	 *
	 * @param parent the parent
	 * @param style the style
	 */
	public View(Composite parent, int style) {
		super(parent, style);
		// parent.getShell().addMouseListener(this);
		// addPaintListener(this);
		// addMouseListener(this);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Update view.
	 */
	public void updateView() {
		redraw();
	}

	// overvide the functiont to the visualization
	/**
	 * Display.
	 */
	public void display() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
	 */
	@Override
	public void shellActivated(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#shellClosed(org.eclipse.swt.events.ShellEvent)
	 */
	@Override
	public void shellClosed(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#shellDeactivated(org.eclipse.swt.events.ShellEvent)
	 */
	@Override
	public void shellDeactivated(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#shellDeiconified(org.eclipse.swt.events.ShellEvent)
	 */
	@Override
	public void shellDeiconified(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#shellIconified(org.eclipse.swt.events.ShellEvent)
	 */
	@Override
	public void shellIconified(ShellEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	@Override
	public void controlMoved(ControlEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	@Override
	public void controlResized(ControlEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragEnter(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragLeave(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOperationChanged(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dropAccept(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("dragFinished click");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	@Override
	public void paintControl(PaintEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	public void widgetDisposed(DisposeEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseEnter(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseExit(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseHover(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("view click");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub
		// System.out.println("view click");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
