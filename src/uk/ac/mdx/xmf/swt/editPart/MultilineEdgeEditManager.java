package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.figure.MultilineEdgeTextFigure;
import uk.ac.mdx.xmf.swt.misc.FontManager;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineEdgeEditManager.
 */
public class MultilineEdgeEditManager extends DirectEditManager {

	/** The scaled font. */
	Font scaledFont;

	/**
	 * Instantiates a new multiline edge edit manager.
	 *
	 * @param source the source
	 * @param editorType the editor type
	 * @param locator the locator
	 */
	public MultilineEdgeEditManager(GraphicalEditPart source, Class editorType,
			CellEditorLocator locator) {
		super(source, editorType, locator);
	}

	/*
	 * protected void bringDown() { Font disposeFont = scaledFont; scaledFont =
	 * null; super.bringDown(); if (disposeFont != null) disposeFont.dispose();
	 * }
	 */

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	protected void initCellEditor() {
		Text text = (Text) getCellEditor().getControl();
		MultilineEdgeTextFigure stickyNote = (MultilineEdgeTextFigure) getEditPart()
				.getFigure();
		String initialLabelText = stickyNote.getText();
		getCellEditor().setValue(initialLabelText);
		IFigure figure = getEditPart().getFigure();
		scaledFont = figure.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());
		stickyNote.translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		// scaledFont = new Font(null, data);
		scaledFont = FontManager.getFont(data);
		text.setFont(scaledFont);
		text.selectAll();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DirectEditManager#createCellEditorOn(org.eclipse.swt.widgets.Composite)
	 */
	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
	}

}