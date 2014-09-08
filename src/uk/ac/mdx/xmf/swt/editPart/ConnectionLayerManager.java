package uk.ac.mdx.xmf.swt.editPart;

import java.util.Hashtable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionLayerManager.
 */
public class ConnectionLayerManager {
	
	/** The layers. */
	private Hashtable layers = new Hashtable();
	
	/** The printable layers. */
	private LayeredPane printableLayers = null;
	
	/** The diagram id. */
	private String diagramId = ""; 
	
	/**
	 * Adds the layer.
	 *
	 * @param identity the identity
	 * @param layer the layer
	 */
	public void addLayer(String identity,IFigure layer) {
		layers.put(identity,layer);
		if(printableLayers != null)
		  printableLayers.add(layer);
	}
	
	/**
	 * Gets the layer.
	 *
	 * @param identity the identity
	 * @return the layer
	 */
	public IFigure getLayer(String identity) {
		if(identity.equals(diagramId))
	      return printableLayers;
		return (IFigure)layers.get(identity);
	}
	
	/**
	 * Checks if is rendering parent.
	 *
	 * @param identity the identity
	 * @return true, if is rendering parent
	 */
	public boolean isRenderingParent(String identity) {
		return identity.equals(diagramId) || layers.containsKey(identity);
	}
	
	/**
	 * Reset.
	 */
	public void reset() {
		resetPrintableLayers();
		layers = new Hashtable();
	}
	
	/**
	 * Reset printable layers.
	 */
	public void resetPrintableLayers() {
		printableLayers = null;
	}
	
	/**
	 * Sets the printable layers.
	 *
	 * @param pl the new printable layers
	 */
	public void setPrintableLayers(LayeredPane pl) {
		printableLayers = pl;
	}
	
	/**
	 * Sets the diagram id.
	 *
	 * @param identity the new diagram id
	 */
	public void setDiagramId(String identity) {
		diagramId = identity;
	}
}