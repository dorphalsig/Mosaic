package uk.ac.mdx.xmf.swt.command;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;

import uk.ac.mdx.xmf.swt.client.ExampleClient;
import xos.Message;
import xos.Value;

public class ExampleCreateNodeCommand {

	String toolIdentity = "Class";
	String modelIdentity = "161";
	Point location = null;
	ExampleClient handler = new ExampleClient();

	public ExampleCreateNodeCommand() {

	}

	public void setLocation(Point position) {
		this.location = position;
	}

	public ExampleCreateNodeCommand(String toolIdentity, Point location) {
		this.toolIdentity = toolIdentity;
		this.location = location;
	}

	// public void raiseFocusGained() {
	// Message m = new Message("focusGained", 1);
	// Value v1 = new Value(modelIdentity);
	// m.args[0] = v1;
	// handler.sendMessage(m);
	//
	// }

	public void execute() {
		Message mes = new Message("focusGained", 1);
		Value v1 = new Value(modelIdentity);
		mes.args[0] = v1;

		Message m = new Message("newNode", 4);
		Value vv1 = new Value(toolIdentity);
		Value v2 = new Value(modelIdentity);
		Value v3 = new Value(location.x);
		Value v4 = new Value(location.y);
		m.args[0] = vv1;
		m.args[1] = v2;
		m.args[2] = v3;
		m.args[3] = v4;

		ArrayList<Message> ms = new ArrayList<Message>();
		ms.add(mes);
		ms.add(m);
		handler.sendMessage(ms);
	}

	public static void main(String[] args) {
		Point location = new Point(236, 114);
		// ExampleCreateNodeCommand c = new ExampleCreateNodeCommand(location);

		// c.execute();
	}
}