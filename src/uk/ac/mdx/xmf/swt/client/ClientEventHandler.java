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
import xos.Message;

import com.google.gson.Gson;

public class ClientEventHandler extends java.lang.Thread {

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
		ClientEventHandler client = new ClientEventHandler();
		client.initExternal(port, id);

	}

	// send message to xmf engine, default port is 9999
	public void sendMessage(ArrayList<Message> mes) {
		String id = "";

		Gson gson = new Gson();
		for (Message message : mes) {
			id += gson.toJson(message) + "-";
		}
		connect(localHost(), port, id);
		new IOThread(System.in, out).start();
		new IOThread(in, System.out).start();
	}

	public void initExternal(int port, String id) {
		connect(localHost(), port, id);
		new IOThread(System.in, out).start();
		new IOThread(in, System.out).start();
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