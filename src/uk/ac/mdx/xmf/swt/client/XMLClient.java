package uk.ac.mdx.xmf.swt.client;

import java.util.Stack;

import uk.ac.mdx.xmf.swt.client.xml.Attribute;
import uk.ac.mdx.xmf.swt.client.xml.Document;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.client.xml.XML;
import xos.Message;
import xos.Value;
import xos.XData;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLClient.
 */
public abstract class XMLClient extends Client {

	/** The xml. */
	Stack xml = new Stack();

	/**
	 * Instantiates a new XML client.
	 *
	 * @param name the name
	 */
	public XMLClient(String name) {
		super(name);
	}

	/**
	 * End document.
	 *
	 * @param message the message
	 */
	public void endDocument(Message message) {
		XML element = popXML();
		if (element instanceof Document)
			processXML((Document) element);
	}

	/**
	 * End element.
	 *
	 * @param message the message
	 */
	public void endElement(Message message) {
		XML element = popXML();
		XML owner = popXML();
		owner.addChild(element);
		pushXML(owner);
	}

	/**
	 * Parses the xml.
	 *
	 * @param message the message
	 * @return true, if successful
	 */
	public boolean parseXML(Message message) {
		if (message.hasName(XMLData.START_DOCUMENT)) {
			startDocument(message);
			return true;
		} else if (message.hasName(XMLData.END_DOCUMENT)) {
			endDocument(message);
			return true;
		} else if (message.hasName(XMLData.START_ELEMENT)) {
			startElement(message);
			return true;
		} else if (message.hasName(XMLData.END_ELEMENT)) {
			endElement(message);
			return true;
		}
		return false;
	}

	/**
	 * Peek xml.
	 *
	 * @return the xml
	 */
	public XML peekXML() {
		return (XML) xml.peek();
	}

	/**
	 * Pop xml.
	 *
	 * @return the xml
	 */
	public XML popXML() {
		return (XML) xml.pop();
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.client.Client#processMessage(xos.Message)
	 */
	@Override
	public boolean processMessage(Message message) {
		return parseXML(message);
	}

	/**
	 * Process xml.
	 *
	 * @param xml the xml
	 */
	public abstract void processXML(Document xml);

	/**
	 * Push xml.
	 *
	 * @param element the element
	 */
	public void pushXML(XML element) {
		xml.push(element);
	}

	/**
	 * Start document.
	 *
	 * @param message the message
	 */
	public void startDocument(Message message) {
		xml.clear();
		pushXML(new Document());
	}

	/**
	 * Start element.
	 *
	 * @param message the message
	 */
	public void startElement(Message message) {
		String name = message.args[0].strValue();
		Element element = new Element();
		element.setName(name);

		// process attributes

		Value[] attributes = message.args[1].values;
		for (int i = 0; i < attributes.length; i++) {
			Value[] attribute = attributes[i].values;
			String aname = attribute[0].strValue();
			String avalue = attribute[1].strValue();
			if (attribute[1].type == XData.INT)
				avalue = String.valueOf(attribute[1].intValue);
			if (attribute[1].type == XData.BOOL)
				avalue = String.valueOf(attribute[1].boolValue);
			element.addAttribute(new Attribute(aname, avalue));
		}

		// push the element

		pushXML(element);
	}
}
