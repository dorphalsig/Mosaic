package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.client.ClientElement;
import uk.ac.mdx.xmf.swt.client.Droppable;
import uk.ac.mdx.xmf.swt.client.EventHandler;
import uk.ac.mdx.xmf.swt.client.xml.Element;
import uk.ac.mdx.xmf.swt.misc.ImageProducer;
import xos.Message;
import xos.Value;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractDiagram.
 */
public class AbstractDiagram extends Container implements Droppable {

  /** The graph. */
  public final Graph graph       = new Graph(this);

  /** The filename. */
  private String     filename    = "";

  /** The type. */
  private String     type        = "";

  /** The color. */
  private RGB        color;

  /** The zoom. */
  private String     zoom        = "100";

  /** The queued zoom. */
  boolean            queuedZoom  = false;

  /** The drop enabled. */
  private boolean    dropEnabled = false;

  /**
   * Instantiates a new abstract diagram.
   *
   * @param parent
   *          the parent
   * @param handler
   *          the handler
   * @param identity
   *          the identity
   */
  public AbstractDiagram(ClientElement parent, EventHandler handler, String identity) {
    super(parent, handler, identity, new Point(0, 0), new Dimension(0, 0), null, null);
  }

  /**
   * Instantiates a new abstract diagram.
   *
   * @param parent
   *          the parent
   * @param handler
   *          the handler
   * @param identity
   *          the identity
   * @param location
   *          the location
   * @param size
   *          the size
   */
  public AbstractDiagram(ClientElement parent, EventHandler handler, String identity, Point location, Dimension size) {
    super(parent, handler, identity, location, size, null, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#close()
   */
  @Override
  public void close() {
    graph.close();
    Message m = handler.newMessage("diagramClosed", 1);
    Value v1 = new Value(getIdentity());
    m.args[0] = v1;
    handler.raiseEvent(m);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();
    graph.dispose();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Droppable#dropEnabled()
   */
  @Override
  public boolean dropEnabled() {
    return dropEnabled;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Droppable#enableDrop()
   */
  @Override
  public void enableDrop() {
    // if (!dropEnabled()) {
    setDroppable();
    // }
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.client.Droppable#setDroppable()
   */
  @Override
  public void setDroppable() {
    // Over-ride as necessary
    dropEnabled = true;
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public RGB getColor() {
    return color;
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#getContents()
   */
  @Override
  public Vector getContents() {
    Vector contents = (Vector) super.getContents().clone();
    contents.addAll(graph.getNodes());
    // if (diagramView != null)
    // diagramView.display();
    return contents;
  }

  /**
   * Gets the node contents.
   *
   * @return the node contents
   */
  public Vector getNodeContents() {
    return graph.getNodes();
  }

  /**
   * Gets the edge contents.
   *
   * @return the edge contents
   */
  public Vector getEdgeContents() {
    return graph.getEdges();
  }

  /**
   * Gets the filename.
   *
   * @return the filename
   */
  public String getFilename() {
    return filename;
  }

  /**
   * Gets the export type.
   *
   * @return the export type
   */
  public String getExportType() {
    return type;
  }

  /**
   * Gets the queued zoom.
   *
   * @return the queued zoom
   */
  public boolean getQueuedZoom() {
    return queuedZoom;
  }

  /**
   * Gets the zoom.
   *
   * @return the zoom
   */
  public String getZoom() {
    return zoom;
  }

  /**
   * Open.
   */
  public void open() {
    Message m = handler.newMessage("diagramOpen", 1);
    Value v1 = new Value(getIdentity());
    m.args[0] = v1;
    handler.raiseEvent(m);
  }

  /**
   * Preference change.
   */
  public void preferenceChange() {
    firePropertyChange("preferenceChange", null, null);

    // if the change is to the font size then we need to raise
    // and event to the diagram client so that it can adjust the
    // diagram to take into account the font change

    Message m = handler.newMessage("preferenceChange", 1);
    Value v = new Value(identity);
    m.args[0] = v;
    handler.raiseEvent(m);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#processMessage(xos.Message)
   */
  @Override
  public boolean processMessage(Message message) {
    if (super.processMessage(message))
      return true;
    if (message.hasName("exportImage") && message.arity == 3 && message.args[0].hasStrValue(identity)) {
      filename = message.args[1].strValue();
      type = message.args[2].strValue();
      ImageProducer.createImage(filename, this, type);
      return true;
    }
    if (message.hasName("copyToClipboard") && message.args[0].hasStrValue(identity) && message.arity == 1) {
      firePropertyChange("copyToClipboard", null, null);
      return true;
    }
    if (message.hasName("setColor") && message.args[0].hasStrValue(identity) && message.arity == 4) {
      int red = message.args[1].intValue;
      int green = message.args[2].intValue;
      int blue = message.args[3].intValue;
      setBackgroundColor(red, green, blue);
      return true;
    }
    if (message.hasName("zoomTo") && message.args[0].hasStrValue(identity) && message.arity == 2) {
      int percent = message.args[1].intValue;
      zoomTo(percent);
      return true;
    } else if (message.args[0].hasStrValue(identity) && (message.hasName("enableDrop") && message.arity == 1)) {
      enableDrop();
      return true;
    }
    return graph.processMessage(message);
  }

  /**
   * Removes the edge.
   *
   * @param edge
   *          the edge
   */
  public void removeEdge(Edge edge) {
    graph.removeEdge(edge);
    if (isRendering())
      firePropertyChange("newEdge", null, null);
  }

  /**
   * Removes the node.
   *
   * @param node
   *          the node
   */
  public void removeNode(Node node) {
    graph.removeNode(node);
    if (isRendering())
      firePropertyChange("newNode", null, null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#render(boolean)
   */
  @Override
  public void render(boolean render) {
    super.render(render);
    if (!render)
      graph.stopRender();
    else
      graph.startRender();
  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.ac.mdx.xmf.swt.model.Container#refreshZoom()
   */
  @Override
  public void refreshZoom() {
    graph.refreshZoom();
    if (isRendering())
      firePropertyChange("zoom", null, null);
    else
      queuedZoom = true;
    super.refreshZoom();
  }

  /**
   * Sets the background color.
   *
   * @param red
   *          the red
   * @param green
   *          the green
   * @param blue
   *          the blue
   */
  public void setBackgroundColor(int red, int green, int blue) {
    this.color = ModelFactory.getColor(red, green, blue);
    if (isRendering())
      firePropertyChange("backgroundColor", null, null);
  }

  /**
   * Sets the queued zoom.
   *
   * @param queuedZoom
   *          the new queued zoom
   */
  public void setQueuedZoom(boolean queuedZoom) {
    this.queuedZoom = queuedZoom;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * uk.ac.mdx.xmf.swt.model.Container#synchronise(uk.ac.mdx.xmf.swt.client.
   * xml.Element)
   */
  @Override
  public void synchronise(Element element) {
    String zoom = element.getString("zoom");
    this.zoom = zoom;
    graph.synchronise(element);
    super.synchronise(element);
  }

  /**
   * Zoom changed.
   *
   * @param zoom
   *          the zoom
   */
  public void zoomChanged(int zoom) {
    Integer z = new Integer(zoom);
    this.zoom = z.toString();
    Message m = handler.newMessage("zoomChanged", 2);
    Value v1 = new Value(identity);
    Value v2 = new Value(z.intValue());
    m.args[0] = v1;
    m.args[1] = v2;
    handler.raiseEvent(m);
  }

  /**
   * Zoom to.
   *
   * @param percent
   *          the percent
   */
  public void zoomTo(int percent) {
    zoom = new Integer(percent).toString();
    if (isRendering())
      firePropertyChange("zoom", null, null);
    else
      queuedZoom = true;
  }
}