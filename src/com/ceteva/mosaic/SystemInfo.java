package com.ceteva.mosaic;

import java.io.PrintWriter;

import org.eclipse.ui.about.ISystemSummarySection;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemInfo.
 */
public class SystemInfo implements ISystemSummarySection {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.about.ISystemSummarySection#write(java.io.PrintWriter)
	 */
	public void write(PrintWriter writer) {
	  writer.write("XMF-Mosaic");	
	}
}
