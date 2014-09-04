package com.ceteva.mosaic.misc;

import xos.OperatingSystem;

class InterruptHandler implements EscapeHandler {

	OperatingSystem xos;

	public InterruptHandler(OperatingSystem xos) {
		this.xos = xos;
	}

	@Override
	public void interrupt() {
		xos.interrupt();
	}

}