package uk.ac.mdx.xmf.swt.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import xos.Message;
import xos.MessagePacket;

import com.google.gson.Gson;

// Used to communicate messages to the GUI thread when we
// are not concerned about the result

public class PacketDisplayRunnable implements Runnable {

	protected MessagePacket packet;
	protected Client client;
	static Gson gson = new Gson();

	public PacketDisplayRunnable() {
	}

	public PacketDisplayRunnable(MessagePacket packet, Client client) {
		this.packet = packet;
		this.client = client;
	}

	public void setPacket(MessagePacket packet) {
		this.packet = packet;
	}

	public void setClient(Client client) {
		if (client.debug)
			System.out.println("PACKAGE: " + packet);
		this.client = client;
	}

	@Override
	public void run() {
		unpackPacket(packet);
	}

	public void unpackPacket(MessagePacket packet) {
		for (int i = 0; i < packet.getMessageCount(); i++) {
			Message message = packet.getMessage(i);
			if (message != null) {
				client.processMessage(message);
				writeMessageOut(message, "F:\\xmf\\code\\receive\\packet.txt");
				writeText(client.name, "F:\\xmf\\code\\receive\\packet.txt");
				// System.out.println("------send message client:" + client);
			}
		}
	}

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

	public static void writeMessageOut(Message message, String file) {
		if (message != null) {
			String messageString = "";
			messageString = gson.toJson(message);
			writeText(messageString, file);
		}
	}
}
