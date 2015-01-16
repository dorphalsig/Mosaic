package uk.ac.mdx.xmf.swt.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import xos.Message;
import xos.MessagePacket;

import com.google.gson.Gson;

// TODO: Auto-generated Javadoc
// Used to communicate messages to the GUI thread when we
// are not concerned about the result

/**
 * The Class PacketDisplayRunnable.
 */
public class PacketDisplayRunnable implements Runnable {

	/** The packet. */
	protected MessagePacket packet;
	
	/** The client. */
	protected Client client;
	
	/** The gson. */
	static Gson gson = new Gson();

	/**
	 * Instantiates a new packet display runnable.
	 */
	public PacketDisplayRunnable() {
	}

	/**
	 * Instantiates a new packet display runnable.
	 *
	 * @param packet the packet
	 * @param client the client
	 */
	public PacketDisplayRunnable(MessagePacket packet, Client client) {
		this.packet = packet;
		this.client = client;
	}

	/**
	 * Sets the packet.
	 *
	 * @param packet the new packet
	 */
	public void setPacket(MessagePacket packet) {
		this.packet = packet;
	}

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(Client client) {
		if (client.debug)
			System.out.println("PACKAGE: " + packet);
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		unpackPacket(packet);
	}

	/**
	 * Unpack packet.
	 *
	 * @param packet the packet
	 */
	public void unpackPacket(MessagePacket packet) {
		for (int i = 0; i < packet.getMessageCount(); i++) {
			Message message = packet.getMessage(i);
			if (message != null) {
				client.processMessage(message);
				//writeMessageOut(message, "F:\\xmf\\code\\receive\\packet.txt");
				//writeText(client.name, "F:\\xmf\\code\\receive\\packet.txt");
				// System.out.println("------send message client:" + client);
			}
		}
	}

	/**
	 * Write text.
	 *
	 * @param str the str
	 * @param file the file
	 */
	public static void writeText(String str, String file) {

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, true)));
			out.println(str);
			out.close();
		} catch (IOException e) {
			// oh noes!
		}
	}

	/**
	 * Write message out.
	 *
	 * @param message the message
	 * @param file the file
	 */
	public static void writeMessageOut(Message message, String file) {
		if (message != null) {
			String messageString = "";
			messageString = gson.toJson(message);
			writeText(messageString, file);
		}
	}
}
