package uk.ac.mdx.xmf.swt.model;

// TODO: Auto-generated Javadoc
/**
 * The Class PaletteTool.
 */
public class PaletteTool {

	/** The name. */
	public String name;
	
	/** The identity. */
	public String identity;
	
	/** The connection. */
	public boolean connection;
	
	/** The icon. */
	public String icon;
    
    /**
     * Instantiates a new palette tool.
     *
     * @param name the name
     * @param identity the identity
     * @param connection the connection
     * @param icon the icon
     */
    public PaletteTool(String name,String identity,boolean connection,String icon) {
	  this.name = name;
	  this.identity = identity;
	  this.connection = connection;
	  this.icon = icon;
	}
	
}
