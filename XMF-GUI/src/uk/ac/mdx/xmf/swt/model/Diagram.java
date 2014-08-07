package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.editPart.ConnectionLayerManager;
import XOS.Message;
import XOS.Value;

public class Diagram extends AbstractDiagram {

	private String name;
	private uk.ac.mdx.xmf.swt.DiagramView owner;
	private AbstractDiagram displayedDiagram;
	private final ConnectionLayerManager connectionManager = new ConnectionLayerManager();
	private Vector toolGroups = new Vector();

	public Diagram(EventHandler handler, String identity) {
		super(null, handler, identity);
		displayedDiagram = this;
		connectionManager.setDiagramId(identity);
	}

	public void addTool(String parent, String name, String identity,
			boolean connection, String icon) {
		PaletteTool tool = new PaletteTool(name, identity, connection, icon);
		for (int i = 0; i < toolGroups.size(); i++) {
			PaletteToolGroup ptg = (PaletteToolGroup) toolGroups.elementAt(i);
			ptg.add(parent, tool);
		}
		if (owner != null) {
			owner.newTool(parent, name, identity, connection, icon);
		}
	}

	public void addToolGroup(String name) {
		toolGroups.add(new PaletteToolGroup(name));
		if (owner != null) {
			owner.newToolGroup(name);
		}
	}

	public void clearToolPalette() {
		toolGroups = new Vector();
		if (owner != null) {
			owner.clearToolPalette();
		}
	}

	@Override
	public void delete() {
		/*
		 * Message m = handler.newMessage("delete",1); Value v1 = new
		 * Value(identity); m.args[0] = v1; handler.raiseEvent(m);
		 */
		if (owner != null)
			owner.delete();
	}

	@Override
	public ConnectionLayerManager getConnectionManager() {
		// return owner.getConnectionManager();
		return connectionManager;
	}

	@Override
	public AbstractDiagram getDisplayedDiagram() {
		return displayedDiagram;
	}

	public uk.ac.mdx.xmf.swt.DiagramView getOwner() {
		return owner;
	}

	public Vector getToolGroups() {
		return toolGroups;
	}

	public String getName() {
		return name;
	}

	public void newTool(String groupID, String label, String toolID,
			boolean connection, String icon) {

	}

	@Override
	public Value processCall(Message message) {
		return owner.processCall(message);
	}

	@Override
	public boolean processMessage(Message message) {
		if (super.processMessage(message))
			return true;
		else if (message.hasName("zoomIn")
				&& message.args[0].hasStrValue(identity) && message.arity == 1) {
			zoomIn();
			return true;
		} else if (message.hasName("refreshZoom")) {
			refreshZoom();
			return true;
		} else if (message.hasName("setName") && message.arity == 2) {
			String name = message.args[1].strValue();
			setName(name);
			return true;
		} else if (message.hasName("clearToolPalette") && message.arity == 1) {
			clearToolPalette();
			return true;
		} else if (message.hasName("newToolGroup") && message.arity == 2) {
			String name = message.args[1].strValue();
			addToolGroup(name);
			return true;
		} else if (message.hasName("newTool") && message.arity == 6) {
			String groupID = message.args[1].strValue();
			String label = message.args[2].strValue();
			String toolID = message.args[3].strValue();
			boolean connection = message.args[4].boolValue;
			String icon = message.args[5].strValue();
			addTool(groupID, label, toolID, connection, icon);
			return true;
		}
		return false;
	}

	public void setDisplayedDiagram(AbstractDiagram diagram) {
		displayedDiagram = diagram;
	}

	public void setOwner(uk.ac.mdx.xmf.swt.DiagramView owner) {
		this.owner = owner;
	}

	public void setName(String name) {
		this.name = name;
		if (owner != null)
			owner.setName(name);
	}

	public boolean shown() {
		return owner != null;
	}

	@Override
	public void setDroppable() {
		super.setDroppable();
		if (owner != null)
			owner.setDroppable();
	}

	@Override
	public void synchronise(Element element) {
		String name = element.getString("name");
		this.name = name;
		super.synchronise(element);
	}

	public void zoomIn() {
		if (zoomTo(this, true))
			refreshZoom();
	}

	public boolean zoomTo(AbstractDiagram newDiagram, boolean swap) {
		if (newDiagram != displayedDiagram) {
			connectionManager.setDiagramId(newDiagram.getIdentity());
			if (displayedDiagram instanceof Group) {
				Group group = (Group) displayedDiagram;
				group.setTopLevel(false);
			}
			if (newDiagram instanceof Group) {
				Group group = (Group) newDiagram;
				group.setTopLevel(true);
			}
			setDisplayedDiagram(newDiagram);
			if (owner != null && swap)
				owner.setViewerModel(newDiagram);
			return true;
		}
		return false;
	}
}