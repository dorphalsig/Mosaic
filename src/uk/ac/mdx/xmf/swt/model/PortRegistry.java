package uk.ac.mdx.xmf.swt.model;

import java.util.Hashtable;

// TODO: Auto-generated Javadoc
// this class provides a mapping between port names
// and their owning nodes

/**
 * The Class PortRegistry.
 */
class PortRegistry {

  /** The mappings. */
  private static Hashtable mappings = new Hashtable();
  
  /**
   * Adds the port.
   *
   * @param portName the port name
   * @param node the node
   */
  public static void addPort(String portName,Node node) {
  	 mappings.put(portName,node);
  }
  
  /**
   * Gets the node.
   *
   * @param portName the port name
   * @return the node
   */
  public static Node getNode(String portName) {
  	 return (Node)mappings.get(portName);
  }
  
  /**
   * Removes the port.
   *
   * @param portName the port name
   */
  public static void removePort(String portName) {
	 mappings.remove(portName);
  }
}