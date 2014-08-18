package com.ceteva.text;

import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.PartInitException;

import uk.ac.mdx.xmf.swt.client.Client;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.IconManager;
import uk.ac.mdx.xmf.swt.client.IdManager;
import uk.ac.mdx.xmf.swt.demo.Main;
import XOS.Message;
import XOS.Value;

import com.ceteva.text.texteditor.TextEditor;
import com.ceteva.text.texteditor.TextEditorInput;
import com.ceteva.text.texteditor.TextStorage;

public class EditorClient extends Client {
	public EventHandler handler = null;

	@Override
	public void setEventHandler(EventHandler eventsOut) {
		handler = eventsOut;
	}

	public EditorClient() {
		super("com.ceteva.text");
	}

	public boolean newBrowser(Message message) {
		String identity = message.args[0].strValue();
		String name = message.args[1].strValue();
		String tooltip = message.args[2].strValue();
		String url = message.args[3].strValue();
		showBrowser(identity, name, tooltip, tooltip);
		return true;
	}

	public boolean newOleEditor(Message message) {
		// TextPlugin textManager = TextPlugin.getDefault();
		// IWorkbenchPage page = textManager.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage();
		// String identity = message.args[0].strValue();
		// String type = message.args[1].strValue();
		// String file = message.args[2].strValue();
		// OLEViewerInput input = new OLEViewerInput(identity, type, file);
		// try {
		// OLEViewer viewer = (OLEViewer) page.openEditor(input,
		// "com.ceteva.text.OLEViewer");
		// viewer.setEventHandler(handler);
		// } catch (PartInitException pie) {
		// System.out.println(pie);
		// }
		return true;
	}

	public void showBrowser(String identity, String title, String tooltip,
			String urls) {
		// // Create a web browser
		if (urls.length() > 1) {
			CTabItem tabItem = new CTabItem(Main.tabFolderDiagram, SWT.BORDER);
			tabItem.setText(title);
			Canvas c = new Canvas(Main.tabFolderDiagram, SWT.BORDER);
			tabItem.setControl(c);

			Browser browser = new Browser(c, SWT.BORDER);
			browser.setUrl(urls);
			// browser.setText(urls);
			browser.setBounds(Main.tabFolderDiagram.getBounds());
			browser.setLocation(0, 0);
			browser.layout(true, true);

			Main.tabFolderDiagram.setSelection(tabItem);
		}
	}

	public void showBrowser(String url) {
		// Create a web browser

		CTabItem tabItem = new CTabItem(Main.tabFolderDiagram, SWT.BORDER);
		tabItem.setText("Welcome");
		Canvas c = new Canvas(Main.tabFolderDiagram, SWT.BORDER);
		tabItem.setControl(c);

		Browser browser = new Browser(c, SWT.BORDER);
		browser.setUrl(url);
		browser.setBounds(Main.tabFolderDiagram.getBounds());
		browser.setLocation(0, 0);
		browser.layout(true, true);

		Main.sectionTopLeft.setFocus();
		Main.sectionTopMiddle.setFocus();
	}

	public boolean newTextEditor(Message message) {
		if (message.arity == 4 || message.arity == 5) {
			String identity = message.args[0].strValue();
			String name = message.args[1].strValue();
			String tooltip = message.args[2].strValue();
			boolean editable = message.args[3].boolValue;
			// TextPlugin textManager = TextPlugin.getDefault();
			// IWorkbenchPage page = textManager.getWorkbench()
			// .getActiveWorkbenchWindow().getActivePage();
			// //
			TextStorage storage = new TextStorage(identity);
			TextEditorInput input = new TextEditorInput(storage);
			if (handler != null) {
				// TextEditor newEditor = (TextEditor) page.openEditor(input,
				// "com.ceteva.text.TextEditor");
				TextEditor newEditor = new TextEditor();
				try {
					newEditor.init(Main.sectionTopMiddle, input, name);
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newEditor.setName(name);

				newEditor.setToolTip(tooltip);
				newEditor.setEditable(editable);
				newEditor.setEventHandler(handler);
				if (message.arity == 5) {
					String icon = message.args[4].strValue();
					if (!icon.endsWith(".gif"))
						icon = icon + ".gif";
					newEditor.setImage(IconManager
							.getImageFromFileAbsolute(icon));
				}
			}
			return true;
		}
		return false;
	}

	public Value processCall(Message message) {
		if (message.hasName("getWelcomePage")) {

			// help page if it is available.
			URL location = EditorClient.class.getProtectionDomain()
					.getCodeSource().getLocation();
			String path = location.toString();
			path = path.substring(0, path.length() - 4); // delete "/bin" from
															// string
			path += "file/Welcome/welcome.html";
			showBrowser(path);
			return new Value(path);
		}
		return IdManager.processCall(message);
	}

	public boolean processMessage(Message message) {
		if (message.hasName("newTextEditor"))
			return newTextEditor(message);
		if (message.hasName("newBrowser"))
			return newBrowser(message);
		if (message.hasName("newOleEditor"))
			return this.newOleEditor(message);
		return IdManager.processMessage(message);
	}

}