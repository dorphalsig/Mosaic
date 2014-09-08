package uk.ac.mdx.xmf.swt.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// TODO: Auto-generated Javadoc
/**
 * 接收信息.
 */
public class M2MReceive extends Thread {

	/** The s. */
	private Socket s;

	/**
	 * Instantiates a new m2 m receive.
	 *
	 * @param s the s
	 */
	public M2MReceive(Socket s) {
		this.s = s;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		try {

			// 构建输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));

			// 不断的接收信息
			while (true) {

				String info = null;

				// 接收信息
				if ((info = br.readLine()) != null) {
					System.out.println(info);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
