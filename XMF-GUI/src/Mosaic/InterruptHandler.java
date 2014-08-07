package Mosaic;

import XOS.OperatingSystem;

class InterruptHandler implements EscapeHandler {

	OperatingSystem XOS;

	public InterruptHandler(OperatingSystem XOS) {
		this.XOS = XOS;
	}

	@Override
	public void interrupt() {
		XOS.interrupt();
	}

}