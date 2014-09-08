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

// TODO: Auto-generated Javadoc
/**
 * The Class CommandEvent.
 */
public abstract class CommandEvent extends ClientElement {

	/** The render. */
	private boolean render = true;
	
	/** The diagram view. */
	DiagramView diagramView;

	/** The listeners. */
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	/**
	 * Instantiates a new command event.
	 *
	 * @param parent the parent
	 * @param handler the handler
	 * @param identity the identity
	 */
	public CommandEvent(ClientElement parent, EventHandler handler,
			String identity) {
		super(parent, handler, identity);
		if (parent instanceof CommandEvent)
			this.render = ((CommandEvent) parent).render;
	}

	/**
	 * Sets the diagram view.
	 *
	 * @param view the new diagram view
	 */
	public void setDiagramView(DiagramView view) {
		diagramView = view;
	}

	/**
	 * Adds the property change listener.
	 *
	 * @param l the l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	/**
	 * Can fire.
	 *
	 * @return true, if successful
	 */
	public boolean canFire() {
		return listeners.getPropertyChangeListeners().length != 0;
	}

	/**
	 * Removes the property change listener.
	 *
	 * @param l the l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	/**
	 * Fire property change.
	 *
	 * @param prop the prop
	 * @param old the old
	 * @param newValue the new value
	 */
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

	/**
	 * Gets the connection manager.
	 *
	 * @return the connection manager
	 */
	public ConnectionLayerManager getConnectionManager() {
		return ((CommandEvent) parent).getConnectionManager();
	}

	/**
	 * Identity exists.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public boolean identityExists(String identity) {
		return this.identity.equals(identity);
	}

	/**
	 * Checks if is rendering.
	 *
	 * @return true, if is rendering
	 */
	public boolean isRendering() {
		return render && DiagramClient.isRendering();
	}

	/**
	 * Fire start render.
	 */
	public void fireStartRender() {
		if (isRendering())
			firePropertyChange("startRender", null, null);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#processCall(xos.Message)
	 */
	@Override
	public Value processCall(Message message) {
		return null;
	}

	/**
	 * Sets the render.
	 *
	 * @param render the new render
	 */
	public void setRender(boolean render) {
		this.render = render;
	}

	/**
	 * Stop render.
	 */
	public void stopRender() {
		render = false;
	}

	/**
	 * Start render.
	 */
	public void startRender() {
		render = true;
		fireStartRender();
	}

}