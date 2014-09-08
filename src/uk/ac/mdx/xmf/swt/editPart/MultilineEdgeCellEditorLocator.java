package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.figure.MultilineEdgeTextFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class MultilineEdgeCellEditorLocator.
 */
final public class MultilineEdgeCellEditorLocator implements CellEditorLocator {

	/** The label. */
	private MultilineEdgeTextFigure label;

	/**
	 * Instantiates a new multiline edge cell editor locator.
	 *
	 * @param label the label
	 */
	public MultilineEdgeCellEditorLocator(MultilineEdgeTextFigure label) {
		setLabel(label);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Point sel = text.getSelection();
		Point pref = text.computeSize(-1, -1);
		Rectangle rect = label.getBounds().getCopy();
		label.translateToAbsolute(rect);
		text.setBounds(rect.x - 4, rect.y - 1, pref.x + 1, pref.y + 1);
		text.setSelection(0);
		text.setSelection(sel);
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	protected MultilineEdgeTextFigure getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 *
	 * @param label the new label
	 */
	protected void setLabel(MultilineEdgeTextFigure label) {
		this.label = label;
	}

}
