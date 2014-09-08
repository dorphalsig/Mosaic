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

// TODO: Auto-generated Javadoc
/**
 * The Class ClientEventHandler.
 */
public class ClientEventHandler extends java.lang.Thread {

	/** The out. */
	OutputStream out;

	/** The in. */
	InputStream in;
	
	/** The socket. */
	Socket socket;

	/** The port. */
	static int port = 9999;// default

	/**
	 * Local host.
	 *
	 * @return the inet address
	 */
	public static InetAddress localHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * Address.
	 *
	 * @param address the address
	 * @return the inet address
	 */
	public static InetAddress address(String address) {
		try {
			return InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + address);
			return null;
		}
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Close.
	 */
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

	/**
	 * Connect.
	 *
	 * @param address the address
	 * @param port the port
	 * @param id the id
	 * @return true, if successful
	 */
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

	/**
	 * Inits the.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public void init(InputStream in, OutputStream out) {

		// Init as an internal client.

		this.in = in;
		this.out = out;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String id = "good";
		ClientEventHandler client = new ClientEventHandler();
		client.initExternal(port, id);

	}

	// send message to xmf engine, default port is 9999
	/**
	 * Send message.
	 *
	 * @param mes the mes
	 */
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

	/**
	 * Inits the external.
	 *
	 * @param port the port
	 * @param id the id
	 */
	public void initExternal(int port, String id) {
		connect(localHost(), port, id);
		new IOThread(System.in, out).start();
		new IOThread(in, System.out).start();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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

	/**
	 * Usage.
	 */
	public static void usage() {
		System.out
				.println("java ExampleClient PORT ID { * | stdin } { * | stdout }");
	}

}