package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.editPart.ConnectionLayerManager;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class Diagram.
 */
public class Diagram extends AbstractDiagram {

	/** The name. */
	private String name;

	/** The owner. */
	private uk.ac.mdx.xmf.swt.DiagramView owner;

	/** The displayed diagram. */
	private AbstractDiagram displayedDiagram;

	/** The connection manager. */
	private final ConnectionLayerManager connectionManager = new ConnectionLayerManager();

	/** The tool groups. */
	private Vector toolGroups = new Vector();

	/**
	 * Instantiates a new diagram.
	 * 
	 * @param handler
	 *            the handler
	 * @param identity
	 *            the identity
	 */
	public Diagram(EventHandler handler, String identity) {
		super(null, handler, identity);
		displayedDiagram = this;
		connectionManager.setDiagramId(identity);
	}

	/**
	 * Adds the tool.
	 * 
	 * @param parent
	 *            the parent
	 * @param name
	 *            the name
	 * @param identity
	 *            the identity
	 * @param connection
	 *            the connection
	 * @param icon
	 *            the icon
	 */
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

	public void deleteTool(String parent, String name, String identity,
			boolean connection, String icon) {
		if (owner != null) {
			owner.delTool(parent, name, identity, connection, icon);
		}
	}

	/**
	 * Adds the tool group.
	 * 
	 * @param name
	 *            the name
	 */
	public void addToolGroup(String name) {
		toolGroups.add(new PaletteToolGroup(name));
		if (owner != null) {
			owner.newToolGroup(name);
		}
	}

	public void deleteToolGroup(String name) {
		if (owner != null) {
			owner.deleteToolGroup(name);
		}
	}

	/**
	 * Clear tool palette.
	 */
	public void clearToolPalette() {
		toolGroups = new Vector();
		if (owner != null) {
			owner.clearToolPalette();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#delete()
	 */
	@Override
	public void delete() {
		/*
		 * Message m = handler.newMessage("delete",1); Value v1 = new
		 * Value(identity); m.args[0] = v1; handler.raiseEvent(m);
		 */
		if (owner != null)
			owner.delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#getConnectionManager()
	 */
	@Override
	public ConnectionLayerManager getConnectionManager() {
		// return owner.getConnectionManager();
		return connectionManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.Container#getDisplayedDiagram()
	 */
	@Override
	public AbstractDiagram getDisplayedDiagram() {
		return displayedDiagram;
	}

	/**
	 * Gets the owner.
	 * 
	 * @return the owner
	 */
	public uk.ac.mdx.xmf.swt.DiagramView getOwner() {
		return owner;
	}

	/**
	 * Gets the tool groups.
	 * 
	 * @return the tool groups
	 */
	public Vector getToolGroups() {
		return toolGroups;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * New tool.
	 * 
	 * @param groupID
	 *            the group id
	 * @param label
	 *            the label
	 * @param toolID
	 *            the tool id
	 * @param connection
	 *            the connection
	 * @param icon
	 *            the icon
	 */
	public void newTool(String groupID, String label, String toolID,
			boolean connection, String icon) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#processCall(xos.Message)
	 */
	@Override
	public Value processCall(Message message) {
		return owner.processCall(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.AbstractDiagram#processMessage(xos.Message)
	 */
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
		} else if (message.hasName("deleteToolGroup") && message.arity == 2) {
			String name = message.args[1].strValue();
			deleteToolGroup(name);
			return true;
		} else if (message.hasName("deleteTool") && message.arity == 6) {
			String groupID = message.args[1].strValue();
			String label = message.args[2].strValue();
			String toolID = message.args[3].strValue();
			boolean connection = message.args[4].boolValue;
			String icon = message.args[5].strValue();
			deleteTool(groupID, label, toolID, connection, icon);
			return true;
		}
		return false;
	}

	/**
	 * Sets the displayed diagram.
	 * 
	 * @param diagram
	 *            the new displayed diagram
	 */
	public void setDisplayedDiagram(AbstractDiagram diagram) {
		displayedDiagram = diagram;
	}

	/**
	 * Sets the owner.
	 * 
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(uk.ac.mdx.xmf.swt.DiagramView owner) {
		this.owner = owner;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
		if (owner != null)
			owner.setName(name);
	}

	/**
	 * Shown.
	 * 
	 * @return true, if successful
	 */
	public boolean shown() {
		return owner != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.AbstractDiagram#setDroppable()
	 */
	@Override
	public void setDroppable() {
		super.setDroppable();
		if (owner != null)
			owner.setDroppable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.model.AbstractDiagram#synchronise(uk.ac.mdx.xmf.swt
	 * .client.xml.Element)
	 */
	@Override
	public void synchronise(Element element) {
		String name = element.getString("name");
		this.name = name;
		super.synchronise(element);
	}

	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		if (zoomTo(this, true))
			refreshZoom();
	}

	/**
	 * Zoom to.
	 * 
	 * @param newDiagram
	 *            the new diagram
	 * @param swap
	 *            the swap
	 * @return true, if successful
	 */
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