package uk.ac.mdx.xmf.swt.model;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class PaletteToolGroup.
 */
public class PaletteToolGroup {

	/** The name. */
	public String name;
	
	/** The tools. */
	public Vector tools = new Vector();
	
	/**
	 * Instantiates a new palette tool group.
	 *
	 * @param name the name
	 */
	public PaletteToolGroup(String name) {
		this.name = name;
	}
	
	/**
	 * Adds the.
	 *
	 * @param parent the parent
	 * @param tool the tool
	 */
	public void add(String parent,PaletteTool tool) {
	  if(parent.equals(name))
	    tools.addElement(tool);
	}
	
}
