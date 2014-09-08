package com.ceteva.text.texteditor;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;

// TODO: Auto-generated Javadoc
/**
 * The Class HighlightLine.
 */
public class HighlightLine {
	
	/** The viewer. */
	SourceViewer viewer;
	
	/** The color. */
	Color color;
	
	/** The old color. */
	Color oldColor;
	
	/** The line. */
	int line;
	
	/**
	 * Instantiates a new highlight line.
	 *
	 * @param viewer the viewer
	 * @param line the line
	 * @param color the color
	 */
	public HighlightLine(SourceViewer viewer,int line,Color color) {
	  this.viewer = viewer;
	  this.line = line;
	  this.color = color;
	  enable();
	}
	
	/**
	 * Enable.
	 */
	public void enable() {
      StyledText textWidget = viewer.getTextWidget();
      oldColor = textWidget.getLineBackground(line);
	  textWidget.setLineBackground(line,1,color);
	}
	
	/**
	 * Disable.
	 */
	public void disable() {
	  StyledText textWidget = viewer.getTextWidget();
	  textWidget.setLineBackground(line,1,oldColor);
	}
	
	/**
	 * Change color.
	 *
	 * @param color the color
	 */
	public void changeColor(Color color) {
	  this.color = color;
	  this.enable();
	}
}