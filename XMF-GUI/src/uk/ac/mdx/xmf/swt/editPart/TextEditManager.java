package uk.ac.mdx.xmf.swt.editPart;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uk.ac.mdx.xmf.swt.command.EdgeTextChangeCommand;
import uk.ac.mdx.xmf.swt.command.MultilineTextChangeCommand;
import uk.ac.mdx.xmf.swt.command.TextChangeCommand;
import uk.ac.mdx.xmf.swt.demo.Main;

public class TextEditManager {

	Font scaledFont;
	private Text text;
	Label label;
	boolean mouseEnter = false;
	uk.ac.mdx.xmf.swt.model.Text model = null;
	uk.ac.mdx.xmf.swt.model.EdgeText modelEdgeText = null;
	uk.ac.mdx.xmf.swt.model.MultilineText modelMultilineText = null;

	String choice = "";

	// public TextEditManager(GraphicalEditPart source, Class editorType,
	// CellEditorLocator locator) {
	// // super(source, editorType, locator);
	//
	// }

	// public void cancel() {
	// bringDown();
	// }

	protected void initCellEditor() {
		// text = (Text) getCellEditor().getControl();
		// label = (Label) getEditPart().getFigure();
		// String initialLabelText = label.getText();
		// getCellEditor().setValue(initialLabelText);
		// scaledFont = label.getFont();
		// FontData data = scaledFont.getFontData()[0];
		// Dimension fontSize = new Dimension(0, data.getHeight());
		// label.translateToAbsolute(fontSize);
		// data.setHeight(fontSize.height);
		// // scaledFont = new Font(null, data);
		// scaledFont = FontManager.getFont(data);
		// text.setFont(scaledFont);
		// text.selectAll();
	}

	public void show(Object object, org.eclipse.swt.graphics.Point p,
			Dimension d) {
		choice = "";
		if (object instanceof uk.ac.mdx.xmf.swt.model.Text) {
			model = (uk.ac.mdx.xmf.swt.model.Text) object;
			text = new Text(Main.getInstance().getView().canvas, SWT.SINGLE
					| SWT.NORMAL);

			text.setLocation(p.x, p.y + 2);
			text.setText(model.getText());
			text.setVisible(true);
			text.setSize(d.width, d.height);
			text.selectAll();
			text.setVisible(true);
			text.setBackground(ColorConstants.lightGray);
			choice = "text";
		} else if (object instanceof uk.ac.mdx.xmf.swt.model.EdgeText) {
			modelEdgeText = (uk.ac.mdx.xmf.swt.model.EdgeText) object;
			text = new Text(Main.getInstance().getView().canvas, SWT.SINGLE
					| SWT.NORMAL);

			text.setLocation(p.x, p.y + 2);
			text.setText(modelEdgeText.getText());
			text.setVisible(true);
			text.setSize(d.width, d.height);
			text.selectAll();
			text.setVisible(true);
			text.setBackground(ColorConstants.lightGray);
			choice = "modelEdgeText";
		} else if (object instanceof uk.ac.mdx.xmf.swt.model.MultilineText) {
			modelMultilineText = (uk.ac.mdx.xmf.swt.model.MultilineText) object;
			text = new Text(Main.getInstance().getView().canvas, SWT.SINGLE
					| SWT.NORMAL);

			text.setLocation(p.x, p.y + 2);
			text.setText(modelMultilineText.getText());
			text.setVisible(true);
			text.setSize(d.width, d.height);
			text.selectAll();
			text.setVisible(true);
			text.setBackground(ColorConstants.lightGray);
			choice = "modelMultilineText";
		}
		text.addListener(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)
					if (choice.equalsIgnoreCase("text"))
						fireChangeText();
					else if (choice.equalsIgnoreCase("modelEdgeText"))
						fireChangeEdgeText();
					else if (choice.equalsIgnoreCase("modelMultilineText"))
						fireChangeMultilineText();
			}
		});
	}

	private void fireChangeText() {
		// System.out.println("text area changed model id:" +
		// model.getIdentity());

		TextChangeCommand command = new TextChangeCommand(model, text.getText());
		command.execute();
		text.setVisible(false);
		text.dispose();
	}

	private void fireChangeEdgeText() {
		// System.out.println("text area changed model id:"
		// + modelEdgeText.getIdentity());

		EdgeTextChangeCommand command = new EdgeTextChangeCommand(
				modelEdgeText, text.getText());
		command.execute();
		text.setVisible(false);
		text.dispose();
	}

	private void fireChangeMultilineText() {
		// System.out.println("text area changed model id:"
		// + modelEdgeText.getIdentity());

		MultilineTextChangeCommand command = new MultilineTextChangeCommand(
				modelMultilineText, text.getText());
		command.execute();
		text.setVisible(false);
		text.dispose();
	}
}
