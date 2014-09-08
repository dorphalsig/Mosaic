package uk.ac.mdx.xmf.swt.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// TODO: Auto-generated Javadoc
/**
 * ������Ϣ.
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

			// ����������
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));

			// ���ϵĽ�����Ϣ
			while (true) {

				String info = null;

				// ������Ϣ
				if ((info = br.readLine()) != null) {
					System.out.println(info);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
