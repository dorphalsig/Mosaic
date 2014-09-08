package uk.ac.mdx.xmf.swt;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IdManager;
import uk.ac.mdx.xmf.swt.client.XMLClient;
import uk.ac.mdx.xmf.swt.client.xml.Document;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.demo.Main;
import uk.ac.mdx.xmf.swt.misc.DiagramPlugin;
import uk.ac.mdx.xmf.swt.misc.FontManager;
import uk.ac.mdx.xmf.swt.model.AbstractDiagram;
import uk.ac.mdx.xmf.swt.model.XMLBindings;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class DiagramClient.
 */
public class DiagramClient extends XMLClient {

	/** The done. */
	boolean done = false;
	
	/** The global render off. */
	static private int globalRenderOff = 0;
	
	/** The handler. */
	public EventHandler handler = null;
	
	/** The _view. */
	public static DiagramView _view;

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#setEventHandler(uk.ac.mdx.xmf.swt.client.EventHandler)
	 */
	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	/**
	 * Instantiates a new diagram client.
	 */
	public DiagramClient() {
		super("com.ceteva.diagram");
	}

	/** The diagram. */
	uk.ac.mdx.xmf.swt.model.Diagram diagram;

	/**
	 * Close none displayed diagrams.
	 *
	 * @return true, if successful
	 */
	public boolean closeNoneDisplayedDiagrams() {
		Hashtable ids = IdManager.getIds();
		Iterator it = ids.values().iterator();
		while (it.hasNext()) {
			Object object = it.next();
			if (object instanceof uk.ac.mdx.xmf.swt.model.Diagram) {
				uk.ac.mdx.xmf.swt.model.Diagram diagram = (uk.ac.mdx.xmf.swt.model.Diagram) object;
				if (!diagram.shown()) {
					diagram.close();
				}
			}
		}
		return true;
	}

	/**
	 * Display diagram model.
	 *
	 * @param diagram the diagram
	 */
	public void displayDiagramModel(uk.ac.mdx.xmf.swt.model.Diagram diagram) {

		DiagramPlugin diagramManager = DiagramPlugin.getDefault();
		// IWorkbenchPage page = diagramManager.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// DiagramEditorInput input = new DiagramEditorInput(diagram);
		// try {
		// if (handler != null) {
		// Diagram newDiagram = (Diagram) page.openEditor(input,
		// "com.ceteva.diagram.Diagram");
		// newDiagram.buildPalette();
		// diagram.refreshZoom();
		// if (input.getModel().dropEnabled())
		// diagram.setDroppable();
		// }
		// } catch (PartInitException pie) {
		// System.out.println(pie);
		// }
	}

	/**
	 * New diagram model.
	 *
	 * @param identity the identity
	 * @param name the name
	 * @param show the show
	 * @return the uk.ac.mdx.xmf.swt.model. diagram
	 */
	public uk.ac.mdx.xmf.swt.model.Diagram newDiagramModel(String identity,
			String name, boolean show) {

		uk.ac.mdx.xmf.swt.model.Diagram diagram = new uk.ac.mdx.xmf.swt.model.Diagram(
				handler, identity);

		diagram.setName(name);
		diagram.setEventHandler(handler);
		diagram.open();
		// this.diagram = diagram;
		// if (show)
		// displayDiagramModel(diagram);
		Main.getInstance().startNewDiagram(identity, diagram);

		return diagram;
	}

	/**
	 * Sets the view.
	 *
	 * @param view the new view
	 */
	public void setView(DiagramView view) {
		_view = view;
	}

	/**
	 * Gets the diagram.
	 *
	 * @return the diagram
	 */
	public uk.ac.mdx.xmf.swt.model.Diagram getDiagram() {
		return diagram;
	}

	/**
	 * Sets the focus.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean setFocus(Message message) {
		if (message.arity == 1) {
			String identity = message.args[0].strValue();
			if (IdManager.has(identity)) {
				uk.ac.mdx.xmf.swt.model.Diagram diagram = (uk.ac.mdx.xmf.swt.model.Diagram) IdManager
						.get(identity);
				if (diagram.shown()) {
					// DiagramPlugin diagramManager =
					// DiagramPlugin.getDefault();
					// IWorkbenchPage page = diagramManager.getWorkbench()
					// .getActiveWorkbenchWindow().getActivePage();
					// page.activate(diagram.getOwner());
				} else {
					displayDiagramModel(diagram);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Broadcast call.
	 *
	 * @param message the message
	 * @return the value
	 */
	public Value broadcastCall(Message message) {
		if (message.hasName("getTextDimension") && message.arity == 2) {
			String text = message.args[0].strValue();
			boolean italicise = message.args[1].boolValue;
			return getTextDimension(text, italicise);
		}
		if (message.hasName("getTextDimensionWithFont") && message.arity == 2) {
			String text = message.args[0].strValue();
			String font = message.args[1].strValue();
			return getTextDimensionWithFont(text, font);
		}
		return new Value(false);
	}

	/**
	 * Gets the text dimension.
	 *
	 * @param text the text
	 * @param italicise the italicise
	 * @return the text dimension
	 */
	public Value getTextDimension(String text, boolean italicise) {
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// FontData fontData = PreferenceConverter.getFontData(preferences,
		// IPreferenceConstants.FONT);
		// if (italicise)
		// fontData.setStyle(SWT.ITALIC);
		FontData fontData = new FontData("Courier", 10, SWT.BOLD);
		Font f = FontManager.getFont(fontData);
		Dimension d = FigureUtilities.getTextExtents(text, f);
		Value[] values = new Value[2];
		values[0] = new Value(d.width);
		values[1] = new Value(d.height);
		Value value = new Value(values);
		return value;
	}

	/**
	 * Gets the text dimension with font.
	 *
	 * @param text the text
	 * @param font the font
	 * @return the text dimension with font
	 */
	public Value getTextDimensionWithFont(String text, String font) {
		Font f = FontManager.getFont(new FontData(font));
		Dimension d = FigureUtilities.getTextExtents(text, f);
		Value[] values = new Value[2];
		values[0] = new Value(d.width);
		values[1] = new Value(d.height);
		Value value = new Value(values);
		return value;
	}

	/**
	 * Checks if is rendering.
	 *
	 * @return true, if is rendering
	 */
	public static boolean isRendering() {
		return globalRenderOff == 0;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#processCall(xos.Message)
	 */
	@Override
	public Value processCall(Message message) {
		return broadcastCall(message);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.XMLClient#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (super.processMessage(message))
			return true;
		if (message.hasName("newDiagram")) {
			String identity = message.args[0].strValue();
			String name = message.args[1].strValue();
			newDiagramModel(identity, name, false);
			return true;
		}
		// else if (message.hasName("setFocus"))
		// return setFocus(message);
		// else if (message.hasName("closeNoneDisplayedDiagrams"))
		// return closeNoneDisplayedDiagrams();
		// else if (message.hasName("globalRenderOff"))
		//
		// globalRenderOff++;
		// else if (message.hasName("globalRenderOn")) {
		// globalRenderOff--;
		// if (globalRenderOff == 0)
		// refreshDiagrams();
		// }
		return IdManager.processMessage(message);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.XMLClient#processXML(uk.ac.mdx.xmf.swt.client.xml.Document)
	 */
	@Override
	public void processXML(Document xml) {
		// xml.printString();
		synchronise(xml);
	}

	/**
	 * Refresh diagrams.
	 */
	public void refreshDiagrams() {
		Hashtable ids = IdManager.getIds();
		Iterator it = ids.values().iterator();
		while (it.hasNext()) {
			Object object = it.next();
			if (object instanceof AbstractDiagram) {
				AbstractDiagram diagram = (AbstractDiagram) object;
				if (diagram.isRendering())
					diagram.startRender();
			}
		}
	}

	/**
	 * Synchronise.
	 *
	 * @param xml the xml
	 */
	public void synchronise(Element xml) {
		synchroniseDiagrams(xml);
	}

	/**
	 * Synchronise diagrams.
	 *
	 * @param xml the xml
	 */
	public void synchroniseDiagrams(Element xml) {

		// check that there is a diagram for each of the document's diagrams

		for (int i = 0; i < xml.childrenSize(); i++) {
			Element child = xml.getChild(i);
			if (child.hasName(XMLBindings.diagram)) {
				String identity = child.getString("identity");
				boolean show = child.getBoolean("isOpen");
				if (IdManager.has(identity)) {
					uk.ac.mdx.xmf.swt.model.Diagram diagram = (uk.ac.mdx.xmf.swt.model.Diagram) IdManager
							.get(identity);
					diagram.synchronise(child);
				} else {
					String name = child.getString("name");
					uk.ac.mdx.xmf.swt.model.Diagram diagram = newDiagramModel(
							identity, name, show);
					diagram.synchronise(child);
				}
			}
		}

		// check that there is a document diagram for each of the diagram

		// needs to be implemented
	}

}