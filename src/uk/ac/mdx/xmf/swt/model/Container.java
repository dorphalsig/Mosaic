package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.demo.Main;
import XOS.Message;

public class Container extends DisplayWithDimension {

	Vector displays = new Vector();

	public Container(ClientElement parent, EventHandler handler,
			String identity, Point location, Dimension size, RGB lineColor,
			RGB fillColor) {
		super(parent, handler, identity, location, size, lineColor, fillColor);
	}

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

	public AbstractDiagram getDisplayedDiagram() {
		return null;
	}

	@Override
	public void close() {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			display.close();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		for (int i = 0; i < displays.size(); i++) {
			Display d = (Display) displays.elementAt(i);
			d.dispose();
		}
	}

	public Vector getContents() {
		return displays;
	}

	public Vector getDisplayContents() {
		return displays;
	}

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
			System.out.println("new  multilinetext:parent.identity-"
					+ parent.identity + "-" + identity + "-"
					+ "multilinetext identity:" + multilineText.identity);
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

	public void removeDisplay(Display d) {
		displays.removeElement(d);
		if (isRendering())
			firePropertyChange("displayChange", null, null);
	}

	@Override
	public void stopRender() {
		setRender(false);
		render(false);
	}

	@Override
	public void startRender() {
		setRender(true);
		render(true);
		if (isRendering()) {
			firePropertyChange("startRender", null, null);

		}
	}

	public void render(boolean render) {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			if (!render)
				display.stopRender();
			else
				display.startRender();
		}
	}

	@Override
	public void refreshZoom() {
		for (int i = 0; i < displays.size(); i++) {
			Display display = (Display) displays.elementAt(i);
			display.refreshZoom();
		}
	}

	@Override
	public void setEventHandler(EventHandler handler) {
		super.setEventHandler(handler);
		for (int i = 0; i < displays.size(); i++) {
			((Display) displays.elementAt(i)).setEventHandler(handler);
		}
	}

	@Override
	public void synchronise(Element container) {
		// System.out.println("Synchronising container");
		ContainerSynchroniser.synchronise(this, container);
	}
}