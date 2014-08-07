package uk.ac.mdx.xmf.swt.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import uk.ac.mdx.xmf.swt.io.IOThread;
import uk.ac.mdx.xmf.swt.io.Provider;
import XOS.Message;
import XOS.Value;

import com.google.gson.Gson;

public class ExampleClient extends java.lang.Thread {

	OutputStream out;

	InputStream in;
	Socket socket;

	static int port = 9999;// default

	public static InetAddress localHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static InetAddress address(String address) {
		try {
			return InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + address);
			return null;
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean connect(InetAddress address, int port, String id) {
		BufferedReader input;
		try {
			socket = new Socket(address, port);
			out = socket.getOutputStream();
			in = socket.getInputStream();

			M2MReceive receive = new M2MReceive(socket);
			receive.start();

			for (int i = 0; i < id.length(); i++)
				out.write((byte) id.charAt(i));
			out.write(0);
			out.flush();

			System.out.println("Connected: " + id + " success = " + in.read());

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return false;
		}
		return true;
	}

	public void init(InputStream in, OutputStream out) {

		// Init as an internal client.

		this.in = in;
		this.out = out;
	}

	public static void main(String[] args) {
		String id = "good";
		Gson gson = new Gson();

		Message mes = new Message("focusGained", 1);
		Value vv1 = new Value("161");
		mes.args[0] = vv1;
		id = gson.toJson(mes);

		Message m = new Message("newNode", 4);
		Value v1 = new Value("Class");
		Value v2 = new Value("161");
		Value v3 = new Value("367");
		Value v4 = new Value("400");
		m.args[0] = v1;
		m.args[1] = v2;
		m.args[2] = v3;
		m.args[3] = v4;

		String iid = gson.toJson(m);
		// id = "";
		id = id + "-" + iid;

		Provider provider = new Provider();
		ArrayList<Message> ms = new ArrayList<Message>();
		ms = provider.readMessage("setting\\guiStartup.txt");

		String s = gson.toJson(ms.get(0)) + "-" + gson.toJson(ms.get(1)) + "-"
				+ gson.toJson(ms.get(2)) + "-" + gson.toJson(ms.get(3)) + "-"
				+ gson.toJson(ms.get(4)) + "-" + gson.toJson(ms.get(5)) + "-"
				+ gson.toJson(ms.get(6));
		// + "-" + gson.toJson(ms.get(7))
		// + gson.toJson(ms.get(8)) + "-" + gson.toJson(ms.get(9)) + "-"
		// + gson.toJson(ms.get(10)) + "-" + gson.toJson(ms.get(11));

		// s = gson.toJson(ms.get(8)) + "-" + gson.toJson(ms.get(9)) + "-"
		// + gson.toJson(ms.get(10)) + "-" + gson.toJson(ms.get(11));
		ExampleClient client = new ExampleClient();
		client.initExternal(port, s);

	}

	// send message to xmf engine, default port is 9999
	public void sendMessage(ArrayList<Message> mes) {
		String id = "";

		Gson gson = new Gson();
		for (Message message : mes) {
			id += gson.toJson(message) + "-";
		}
		connect(localHost(), port, id);
		// out.close();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// new IOThread(System.in, out).start();
		// new IOThread(in, System.out).start();
	}

	public void sendSingleMessage(Message mes) {
		String id = "";

		Gson gson = new Gson();

		id = gson.toJson(mes);
		connect(localHost(), port, id);
		new IOThread(System.in, out).start();
		new IOThread(in, System.out).start();
	}

	public void initExternal(int port, String id) {
		connect(localHost(), port, id);
		// new IOThread(System.in, out).start();
		// new IOThread(in, System.out).start();
	}

	@Override
	public synchronized void run() {
		System.out.println("Start client.");
		while (true) {
			try {
				synchronized (out) {
					for (int i = 0; i < 5000; i++)
						out.write('x');
					out.write('\n');
					out.flush();
					out.notifyAll();
					wait(1000);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void usage() {
		System.out
				.println("java ExampleClient PORT ID { * | stdin } { * | stdout }");
	}

}