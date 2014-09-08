package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.figure.MultilineTextFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineCellEditorLocator.
 */
final public class MultilineCellEditorLocator implements CellEditorLocator {

	/** The multiline text figure. */
	private MultilineTextFigure multilineTextFigure;

	/**
	 * Instantiates a new multiline cell editor locator.
	 *
	 * @param stickyNote the sticky note
	 */
	public MultilineCellEditorLocator(MultilineTextFigure stickyNote) {
		setLabel(stickyNote);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Point sel = text.getSelection();
		Point pref = text.computeSize(-1, -1);
		Rectangle rect = multilineTextFigure.getBounds().getCopy();
		multilineTextFigure.translateToAbsolute(rect);
		text.setBounds(rect.x - 4, rect.y - 1, pref.x + 1, pref.y + 1);
		text.setSelection(0);
		text.setSelection(sel);
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	protected MultilineTextFigure getLabel() {
		return multilineTextFigure;
	}

	/**
	 * Sets the label.
	 *
	 * @param stickyNote the new label
	 */
	protected void setLabel(MultilineTextFigure stickyNote) {
		this.multilineTextFigure = stickyNote;
	}

}
