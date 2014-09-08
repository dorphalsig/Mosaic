package com.ceteva.mosaic.misc;

import xos.OperatingSystem;

// TODO: Auto-generated Javadoc
/**
 * The Class InterruptHandler.
 */
class InterruptHandler implements EscapeHandler {

	/** The xos. */
	OperatingSystem xos;

	/**
	 * Instantiates a new interrupt handler.
	 *
	 * @param xos the xos
	 */
	public InterruptHandler(OperatingSystem xos) {
		this.xos = xos;
	}

	/* (non-Javadoc)
	 * @see com.ceteva.mosaic.misc.EscapeHandler#interrupt()
	 */
	@Override
	public void interrupt() {
		xos.interrupt();
	}

}