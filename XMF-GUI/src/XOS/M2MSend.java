package XOS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * send information
 * 
 * 
 */
public class M2MSend extends Thread {

	// current socket
	private Socket s;
	String file = "F:\\xmf\\code\\receive\\packet.txt";

	public M2MSend(Socket s) {
		this.s = s;
	}

	public void run() {

		// get ip
		String ip = s.getInetAddress().getHostAddress();

		try {

			// get information
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			// read packet
			while (true) {

				// receive information
				String line = br.readLine();
				;
				String cLine = "";
				// if not null
				while (line != null) {
					sb.append(line);
					sb.append('-');
					line = br.readLine();
				}
				cLine = sb.toString();

				PrintWriter pw;
				pw = new PrintWriter(s.getOutputStream());
				// write data
				pw.println(cLine);
				pw.flush();

				File f = new File(file);
				f.delete();

				File f2 = new File(file);
				try {
					f2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (IOException e1) {
			System.err.println(ip + " not connect");
		}

	}

}
