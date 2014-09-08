package com.ceteva.mosaic.misc;

// TODO: Auto-generated Javadoc
/**
 * The Class MosaicRun.
 */
public class MosaicRun {
	
  /** The running mosaic. */
  public static boolean runningMosaic = false;
  
  /**
   * Running mosaic.
   */
  public static void runningMosaic() {
  	runningMosaic = true;
  }
  
  /**
   * Mosaic is running.
   *
   * @return true, if successful
   */
  public static boolean mosaicIsRunning() {
  	return runningMosaic;
  }
}