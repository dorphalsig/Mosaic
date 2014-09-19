package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.demo.Main;
import xos.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class Container.
 */
public class Container extends DisplayWithDimension {

	/** The displays. */
	Vector displays = new Vector();

	/**
	 * Instantiates a new container.
	 * 
	 * @param parent
	 *            the parent
	 * @param handler
	 *            the handler
	 * @param identity
	 *            the identity
	 * @param location
	 *            the location
	 * @param size
	 *            the size
	 * @param lineColor
	 *            the line color
	 * @param fillColor
	 *            the fill color
	 */
	public Container(ClientElement parent, EventHandler handler,
			String identity, Point location, Dimension size, RGB lineColor,
			RGB fillColor) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
	}

	/**
	 * Adds the display.
	 * 
	 * @param d
	 *            the d
	 */
	public void addDisplay(Display d) {
		// displays.addElement(d);
		displays.add(d);
		// if (isRendering())

		{
			diagramView = Main.getInstance().getView();
			if (diagramView != null)
				diagramView.refresh(displays);

			firePropertyChange("displayChange", null, null);

		}
	}

	/**
	 * Gets the displayed diagram.
	 * 
	 * @return the displayed diagram
	 */
	public AbstractDiagram getDisplayedDiagram() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.Display#close()
	 */
	@Override
	public void close() {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			display.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.client.ClientElement#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		for (int i = 0; i < displays.size(); i++) {
			Display d = (Display) displays.elementAt(i);
			d.dispose();
		}
	}

	/**
	 * Gets the contents.
	 * 
	 * @return the contents
	 */
	public Vector getContents() {
		return displays;
	}

	/**
	 * Gets the display contents.
	 * 
	 * @return the display contents
	 */
	public Vector getDisplayContents() {
		return displays;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.model.CommandEvent#identityExists(java.lang.String)
	 */
	@Override
	public boolean identityExists(String identity) {
		if (this.identity.equals(identity))
			return true;
		for (int i = 0; i < displays.size(); i++) {
			Display d = (Display) displays.elementAt(i);
			if (d.identityExists(identity))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.model.DisplayWithDimension#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		if (message.hasName("newBox") && message.args[0].hasStrValue(identity)) {
			Box box = ModelFactory.newBox(this, handler, message);
			// System.out.println("new box:" + parent.toString() + "-"
			// + parent.identity + "-" + "identity:" + identity + "-"
			// + "box identity:" + box.identity + box.getLocation() + "-"
			// + box.getSize().width + "-" + box.getSize().height);
			addDisplay(box);
			return true;
		} else if (message.hasName("newText")
				&& message.args[0].hasStrValue(identity)) {
			Text text = ModelFactory.newText(this, handler, message);
			// System.out.println("new text:parent.identity-" + parent.identity
			// + "-" + identity + "-" + "text identity:" + text.identity);
			addDisplay(text);
			return true;
		} else if (message.hasName("newMultilineText")
				&& message.args[0].hasStrValue(identity)) {
			MultilineText multilineText = ModelFactory.newMultilineText(this,
					handler, message);
			// System.out.println("new  multilinetext:parent.identity-"
			// + parent.identity + "-" + identity + "-"
			// + "multilinetext identity:" + multilineText.identity);
			addDisplay(multilineText);
			return true;
		} else if (message.hasName("newImage")
				&& message.args[0].hasStrValue(identity)) {
			addDisplay(ModelFactory.newImage(this, handler, message));
			return true;
		} else if (message.hasName("newGroup")
				&& message.args[0].hasStrValue(identity)) {
			addDisplay(ModelFactory.newGroup(this, handler, message));
			return true;
		} else if (message.hasName("newLine")
				&& message.args[0].hasStrValue(identity)) {
			addDisplay(ModelFactory.newLine(this, handler, message));
			return true;
		} else if (message.hasName("newEllipse")
				&& message.args[0].hasStrValue(identity)) {
			addDisplay(ModelFactory.newEllipse(this, handler, message));
			return true;
		} else if (message.hasName("newShape")
				&& message.args[0].hasStrValue(identity)) {
			addDisplay(ModelFactory.newShape(this, handler, message));
			return true;
		}
		return super.processMessage(message);
	}

	/**
	 * Removes the display.
	 * 
	 * @param d
	 *            the d
	 */
	public void removeDisplay(Display d) {
		displays.removeElement(d);
		if (isRendering())
			firePropertyChange("displayChange", null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#stopRender()
	 */
	@Override
	public void stopRender() {
		setRender(false);
		render(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.CommandEvent#startRender()
	 */
	@Override
	public void startRender() {
		setRender(true);
		render(true);
		if (isRendering()) {
			firePropertyChange("startRender", null, null);

		}
	}

	/**
	 * Render.
	 * 
	 * @param render
	 *            the render
	 */
	public void render(boolean render) {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			if (!render)
				display.stopRender();
			else
				display.startRender();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.mdx.xmf.swt.model.Display#refreshZoom()
	 */
	@Override
	public void refreshZoom() {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			display.refreshZoom();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.client.ClientElement#setEventHandler(uk.ac.mdx.xmf.
	 * swt.client.EventHandler)
	 */
	@Override
	public void setEventHandler(EventHandler handler) {
		super.setEventHandler(handler);
		for (int i = 0; i < displays.size(); i++) {
			((Display) displays.elementAt(i)).setEventHandler(handler);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.mdx.xmf.swt.model.DisplayWithDimension#synchronise(uk.ac.mdx.xmf
	 * .swt.client.xml.Element)
	 */
	@Override
	public void synchronise(Element container) {
		// System.out.println("Synchronising container");
		ContainerSynchroniser.synchronise(this, container);
	}
}