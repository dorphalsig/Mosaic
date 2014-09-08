package uk.ac.mdx.xmf.swt.client;

import java.util.Hashtable;

import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class IdManager.
 */
public class IdManager {

	/** The idbindings. */
	private static Hashtable<String, ClientElement> idbindings = new Hashtable<String, ClientElement>();

	/**
	 * Change id.
	 *
	 * @param oldIdentity the old identity
	 * @param newIdentity the new identity
	 * @return true, if successful
	 */
	public static boolean changeId(String oldIdentity, String newIdentity) {
		ClientElement element = (ClientElement) idbindings.remove(oldIdentity);
		if (element != null) {
			element.setIdentity(newIdentity);
			put(newIdentity, element);
			return true;
		} else
			return false;
	}

	/**
	 * Gets the.
	 *
	 * @param identity the identity
	 * @return the client element
	 */
	public static ClientElement get(String identity) {
		return (ClientElement) idbindings.get(identity);
	}

	/**
	 * Gets the ids.
	 *
	 * @return the ids
	 */
	public static Hashtable<String, ClientElement> getIds() {
		return idbindings;
	}

	/**
	 * Checks for.
	 *
	 * @param identity the identity
	 * @return true, if successful
	 */
	public static boolean has(String identity) {
		return idbindings.containsKey(identity);
	}

	/**
	 * Process call.
	 *
	 * @param message the message
	 * @return the value
	 */
	public static Value processCall(Message message) {
		if (message.arity > 0) {
			ClientElement target = get(message.args[0].strValue());
			return target.processCall(message);
		}
		return null;
	}

	/**
	 * Process message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
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

	/**
	 * Put.
	 *
	 * @param identity the identity
	 * @param element the element
	 */
	public static void put(String identity, ClientElement element) {
		idbindings.put(identity, element);
	}

	/**
	 * Removes the.
	 *
	 * @param identity the identity
	 */
	public static void remove(String identity) {
		idbindings.remove(identity);
	}
}