package uk.ac.mdx.xmf.swt.client;

import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Interface Commandable.
 */
public interface Commandable {

	/**
	 * Process message.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean processMessage(Message message);

	/**
	 * Process call.
	 *
	 * @param message the message
	 * @return the value
	 */
	public Value processCall(Message message);

}
