package uk.ac.mdx.xmf.swt.client;

import java.util.Hashtable;

import xos.Message;
import xos.Value;

public class IdManager {

	private static Hashtable<String, ClientElement> idbindings = new Hashtable<String, ClientElement>();

	public static boolean changeId(String oldIdentity, String newIdentity) {
		ClientElement element = (ClientElement) idbindings.remove(oldIdentity);
		if (element != null) {
			element.setIdentity(newIdentity);
			put(newIdentity, element);
			return true;
		} else
			return false;
	}

	public static ClientElement get(String identity) {
		return (ClientElement) idbindings.get(identity);
	}

	public static Hashtable<String, ClientElement> getIds() {
		return idbindings;
	}

	public static boolean has(String identity) {
		return idbindings.containsKey(identity);
	}

	public static Value processCall(Message message) {
		if (message.arity > 0) {
			ClientElement target = get(message.args[0].strValue());
			return target.processCall(message);
		}
		return null;
	}

	public static boolean processMessage(Message message) {

		if (message.arity > 0) {
			ClientElement target = get(message.args[0].strValue());
			if (target != null)
				return target.processMessage(message);
			else {
				PacketDisplayRunnable.writeMessageOut(message,
						"F:\\xmf\\code\\receive\\processmessage.txt");
				PacketDisplayRunnable.writeText(message.name(),
						"F:\\xmf\\code\\receive\\processmessage.txt");
				// System.out.println("Failed: " + message.args[0].strValue()
				// + "---" + message.toString());
			}

		}
		return false;
	}

	public static void put(String identity, ClientElement element) {
		idbindings.put(identity, element);
	}

	public static void remove(String identity) {
		idbindings.remove(identity);
	}
}