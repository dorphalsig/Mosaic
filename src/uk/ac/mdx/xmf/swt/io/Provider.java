package uk.ac.mdx.xmf.swt.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import xos.EventHandler;
import xos.Message;
import xos.MessageHandler;
import xos.MessagePacket;
import xos.Value;

import com.google.gson.Gson;

// TODO: Auto-generated Javadoc
/**
 * The Class Provider.
 */
public class Provider extends Observable implements EventHandler {
	
	/** The call. */
	String call = "F:\\xmf\\code\\receive\\call.txt";
	
	/** The packet. */
	String packet = "F:\\xmf\\code\\receive\\packet.txt";
	
	/** The ms. */
	ArrayList<Message> ms = new ArrayList<Message>();
	
	/** The handler. */
	private MessageHandler handler;

	/** The packets. */
	private MessagePacket packets;

	/**
	 * Update.
	 */
	public void update() {
		// Notify observers of change
		setChanged();
		notifyObservers(ms);
	}

	/**
	 * Read message.
	 *
	 * @param txt the txt
	 * @return the array list
	 */
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

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public ArrayList<Message> getMessage() {
		return ms;
	}

	/**
	 * Read txt.
	 *
	 * @param file the file
	 * @return the string[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * New message client.
	 *
	 * @param name the name
	 * @param handler the handler
	 * @return true, if successful
	 */
	public boolean newMessageClient(String name, MessageHandler handler) {

		// Creates and registers a new message client.
		this.handler = handler;

		return true;
	}

	/**
	 * Send message.
	 *
	 * @param message the message
	 */
	public void sendMessage(Message message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Send packet.
	 */
	public void sendPacket() {
		// TODO Auto-generated method stub
		handler.sendPacket(packets);
	}

	/**
	 * Call message.
	 *
	 * @param message the message
	 * @return the value
	 */
	public Value callMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see xos.EventHandler#raiseEvent(java.lang.String, xos.Message)
	 */
	@Override
	public void raiseEvent(String client, Message message) {
		// TODO Auto-generated method stub

	}
}
