package uk.ac.mdx.xmf.swt.client;

import xos.Message;
import xos.Value;

public interface Commandable {

	public boolean processMessage(Message message);

	public Value processCall(Message message);

}
