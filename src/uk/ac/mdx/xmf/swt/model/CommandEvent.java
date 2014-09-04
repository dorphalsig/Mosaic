package uk.ac.mdx.xmf.swt.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import uk.ac.mdx.xmf.swt.DiagramClient;
import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.editPart.ConnectionLayerManager;
import xos.Message;
import xos.Value;

public abstract class CommandEvent extends ClientElement {

	private boolean render = true;
	DiagramView diagramView;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public CommandEvent(ClientElement parent, EventHandler handler,
			String identity) {
		super(parent, handler, identity);
		if (parent instanceof CommandEvent)
			this.render = ((CommandEvent) parent).render;
	}

	public void setDiagramView(DiagramView view) {
		diagramView = view;
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public boolean canFire() {
		return listeners.getPropertyChangeListeners().length != 0;
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue) {
		// if(!isRendering())
		// System.out.println("Warning: render off and firing - " + prop);
		listeners.firePropertyChange(prop, old, newValue);
		// if (diagramView != null)
		// diagramView.update();
		// if (prop.contains("Render"))
		// GUIDemo.getInstance().view.update();
		// System.out.println(prop);

	}

	public ConnectionLayerManager getConnectionManager() {
		return ((CommandEvent) parent).getConnectionManager();
	}

	public boolean identityExists(String identity) {
		return this.identity.equals(identity);
	}

	public boolean isRendering() {
		return render && DiagramClient.isRendering();
	}

	public void fireStartRender() {
		if (isRendering())
			firePropertyChange("startRender", null, null);
	}

	@Override
	public Value processCall(Message message) {
		return null;
	}

	public void setRender(boolean render) {
		this.render = render;
	}

	public void stopRender() {
		render = false;
	}

	public void startRender() {
		render = true;
		fireStartRender();
	}

}