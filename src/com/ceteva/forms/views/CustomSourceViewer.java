package com.ceteva.forms.views;

import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomSourceViewer.
 */
public class CustomSourceViewer extends SourceViewer {

	/**
	 * Instantiates a new custom source viewer.
	 *
	 * @param parent the parent
	 * @param ruler the ruler
	 * @param styles the styles
	 */
	public CustomSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		super(parent, ruler, styles);
	}

}