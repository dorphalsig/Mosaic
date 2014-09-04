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

public class DiagramClient extends XMLClient {

	boolean done = false;
	static private int globalRenderOff = 0;
	public EventHandler handler = null;
	public static DiagramView _view;

	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	public DiagramClient() {
		super("com.ceteva.diagram");
	}

	uk.ac.mdx.xmf.swt.model.Diagram diagram;

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

	public void setView(DiagramView view) {
		_view = view;
	}

	public uk.ac.mdx.xmf.swt.model.Diagram getDiagram() {
		return diagram;
	}

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

	public Value getTextDimensionWithFont(String text, String font) {
		Font f = FontManager.getFont(new FontData(font));
		Dimension d = FigureUtilities.getTextExtents(text, f);
		Value[] values = new Value[2];
		values[0] = new Value(d.width);
		values[1] = new Value(d.height);
		Value value = new Value(values);
		return value;
	}

	public static boolean isRendering() {
		return globalRenderOff == 0;
	}

	@Override
	public Value processCall(Message message) {
		return broadcastCall(message);
	}

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

	@Override
	public void processXML(Document xml) {
		// xml.printString();
		synchronise(xml);
	}

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

	public void synchronise(Element xml) {
		synchroniseDiagrams(xml);
	}

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