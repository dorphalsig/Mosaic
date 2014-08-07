package uk.ac.mdx.xmf.swt.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import XOS.EventHandler;
import XOS.Message;
import XOS.MessageHandler;
import XOS.MessagePacket;
import XOS.Value;

import com.google.gson.Gson;

public class Provider extends Observable implements EventHandler {
	String call = "F:\\xmf\\code\\receive\\call.txt";
	String packet = "F:\\xmf\\code\\receive\\packet.txt";
	ArrayList<Message> ms = new ArrayList<Message>();
	private MessageHandler handler;

	private MessagePacket packets;

	public void update() {
		// Notify observers of change
		setChanged();
		notifyObservers(ms);
	}

	public ArrayList<Message> readMessage(String txt) {

		ms.clear();

		String[] strs = null;

		try {
			strs = readTxt(txt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		for (int i = 0; i < strs.length; i++) {

			String str = strs[i];

			Message obj = gson.fromJson(str, Message.class);
			if (obj != null) {
				// if (obj.hasName("newNode") || obj.hasName("newEdge")
				// || obj.hasName("newBox"))
				{
					ms.add(obj);
				}
			}
		}

		packets = new MessagePacket(ms.size());
		for (int i = 0; i < ms.size(); i++) {
			packets.addMessage(i, ms.get(i));
		}
		// File f = new File(packet);
		// f.delete();
		//
		// File f2 = new File(packet);
		// try {
		// f2.createNewFile();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// if (ms.size() > 0) {
		// update();
		// }

		return ms;
	}

	public ArrayList<Message> getMessage() {
		return ms;
	}

	public static String[] readTxt(String file) throws IOException {

		String[] data = null;

		String cLine = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			cLine = sb.toString();
			data = cLine.split("\n");
		} finally {
			br.close();
		}

		return data;
	}

	public boolean newMessageClient(String name, MessageHandler handler) {

		// Creates and registers a new message client.
		this.handler = handler;

		return true;
	}

	public void sendMessage(Message message) {
		// TODO Auto-generated method stub

	}

	public void sendPacket() {
		// TODO Auto-generated method stub
		handler.sendPacket(packets);
	}

	public Value callMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void raiseEvent(String client, Message message) {
		// TODO Auto-generated method stub

	}
}
