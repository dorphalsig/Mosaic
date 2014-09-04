package uk.ac.mdx.xmf.swt.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

class ReadText {
	static String[] test = { "-arg", "user:XModeler", "-arg",
			"home:/F:/xmf/XMF-GUI/com.ceteva.xmf", "-image",
			"/F:/xmf/XMF-GUI/com.ceteva.xmf/Images/mosaic.img", "-arg",
			"projects:", "-arg", "demos:", "-arg", "images:", "-message",
			":com.ceteva.mosaic:wait", "-message", ":com.ceteva.menus:wait",
			"-message", ":com.ceteva.dialogs:wait", "-message",
			":com.ceteva.forms:wait", "-message",
			":com.ceteva.modelBrowser:wait", "-message",
			":com.ceteva.undo:wait", "-message", ":com.ceteva.diagram:wait",
			"-message", ":com.ceteva.text:wait", "-message",
			":com.ceteva.oleBridge:wait", "-internal",
			"com.ceteva.console.ConsoleClient:com.ceteva.console", "-debug",
			"-heapSize", "80000", "-freeHeap", "800" };

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		// PrintWriter writer = new PrintWriter("ini.txt", "UTF-8");
		// for (String s : test)
		// writer.println(s);
		// writer.close();
		ArrayList<String> ini = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("ini.txt"))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				ini.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] values = new String[ini.size()];
		values = ini.toArray(values);
	}
}
