package XOS;

import com.google.gson.Gson;

public class MessageClient extends Client {

	// A message client allows synchronous communication between XVM and
	// arbitrary
	// Java via XOS.
	Gson gson = new Gson();
	private MessageQueue messages = new MessageQueue();

	private MessageHandler handler;
	private OperatingSystem XOS;

	public MessageClient(OperatingSystem XOS, String name,
			MessageHandler handler) {
		super(XOS, name);
		this.handler = handler;
		this.XOS = XOS;
	}

	public Value call(Message message) {
		Value value = null;
		try {
			// XOS.deletFile( "F:\\xmf\\code\\receive\\call.txt");
			// XOS.writeMessageOut(message, "F:\\xmf\\code\\receive\\call.txt");
			value = handler.callMessage(message);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public MessageQueue messages() {
		return messages;
	}

	public synchronized void queueMessage(Message message) {

		// Some external Java code has supplied a message for XVM. Queue
		// up the message and notify any threads that are currently waiting
		// on the client.

		messages.insert(message);
		notifyAll();
	}

	public Message readMessage() {
		if (messages.isEmpty())
			return null;
		else
			return messages.next();
	}

	public synchronized boolean ready() {
		return !messages.isEmpty();
	}

	public void sendMessage(Message message) {
		try {
			// XOS.deletFile( "F:\\xmf\\code\\receive\\message.txt");
			handler.sendMessage(message);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(MessagePacket packet) {
		try {
			// XOS.deletFile( "F:\\xmf\\code\\receive\\packet.txt");
			for (int i = 0; i < packet.getMessageCount(); i++) {
				Message message = packet.getMessage(i);
				if (message != null) {
					// XOS.writeMessageOut(message,
					// "F:\\xmf\\code\\receive\\packet.txt");
					// XOS.writeText(name,
					// "F:\\xmf\\code\\receive\\packet.txt");
				}
			}
			handler.sendPacket(packet);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

}
