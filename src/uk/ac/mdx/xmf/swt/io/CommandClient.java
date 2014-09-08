package uk.ac.mdx.xmf.swt.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import xos.Message;

import com.google.gson.Gson;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandClient.
 */
public class CommandClient implements Runnable, Observer {

	/** The out. */
	OutputStream out;

	/** The in. */
	InputStream in;
	
	/** The socket. */
	Socket socket;
	
	/** The id. */
	String id;
	
	/** The flag. */
	boolean flag = false;

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
	 * Sets the id.
	 *
	 * @param mes the new id
	 */
	public void setId(ArrayList<Message> mes) {

		String str = "";

		Gson gson = new Gson();
		for (Message message : mes) {
			str += gson.toJson(message) + "-";
		}

		this.id = str;
		flag = true;
	}

	/**
	 * Connect.
	 *
	 * @param address the address
	 * @param port the port
	 * @return true, if successful
	 */
	public boolean connect(InetAddress address, int port) {

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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (flag) {
			connect(this.localHost(), 9999);
			flag = false;
		}

	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		id = (String) arg1;
	}
}