package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.RGB;

import uk.ac.mdx.xmf.swt.diagram.tracker.DisplaySelectionTracker;
import uk.ac.mdx.xmf.swt.figure.BoxFigure;
import uk.ac.mdx.xmf.swt.figure.RoundedBoxFigure;
import uk.ac.mdx.xmf.swt.model.AbstractDiagram;
import uk.ac.mdx.xmf.swt.model.Box;

public class BoxEditPart extends DisplayEditPart {

	public void activate() {
		super.activate();
		// Text model = (Text) getModel();
		// if (model.edit())
		// this.editText();
	}

	@Override
	public IFigure createFigure() {
		// Box box = (Box) getModel();
		// Figure rectangle = null;
		// int borderCurve = box.getBorderCurve();
		// if (borderCurve == 0)
		// rectangle = new BoxFigure(box.getLocation(), box.getSize(),
		// box.showTop(), box.showRight(), box.showBottom(),
		// box.showLeft());
		// else
		// rectangle = new RoundedBoxFigure(box.getLocation(), box.getSize(),
		// borderCurve);
		//
		// Label label = new org.eclipse.draw2d.Label("Class");
		// Font classFont = new Font(null, "Arial", 8, SWT.BOLD);
		// label.setFont(classFont);
		// label.setTextAlignment(PositionConstants.CENTER);
		// label.setBackgroundColor(ColorConstants.white);
		// label.setForegroundColor(ColorConstants.gray);
		// label.setOpaque(true);
		//
		// label.setSize(label.getTextBounds().resize(20, 10).getSize());
		// label.setLocation(rectangle.getLocation().translate(5, 2));
		//
		// rectangle.add(label);
		//
		// rectangle.setLayoutManager(new XYLayout());
		// return rectangle;
		Box box = (Box) getModel();
		Figure rectangle = null;
		int borderCurve = box.getBorderCurve();

		{

			if (borderCurve == 0)
				rectangle = new BoxFigure(box.getLocation(), box.getSize(),
						box.showTop(), box.showRight(), box.showBottom(),
						box.showLeft());
			else
				rectangle = new RoundedBoxFigure(box.getLocation(),
						box.getSize(), borderCurve);
			rectangle.setLayoutManager(new XYLayout());
		}

		// rectangle.setBackgroundColor(ColorConstants.lightBlue);
		// rectangle.setForegroundColor(ColorConstants.black);
		// rectangle.repaint();
		// rectangle.setOpaque(true);
		setFigure(rectangle);
		return rectangle;
	}

	public RGB getFillColor() {
		RGB fillColor = new RGB(160, 240, 199);
		return fillColor;
		// if (fillColor != null)
		// return fillColor;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FILL_COLOR);
	}

	public RGB getForegroundColor() {
		RGB lineColor = new RGB(160, 240, 199);
		return lineColor;
		// if (lineColor != null)
		// return lineColor;
		// IPreferenceStore preferences = DiagramPlugin.getDefault()
		// .getPreferenceStore();
		// return PreferenceConverter.getColor(preferences,
		// IPreferenceConstants.FOREGROUND_COLOR);
	}

	public boolean getGradient() {
		return false;
		// Preferences preferences = DiagramPlugin.getDefault()
		// .getPluginPreferences();
		// return preferences.getBoolean(IPreferenceConstants.GRADIENT_FILL);
	}

	@Override
	protected List getModelChildren() {
		return ((Box) getModel()).getContents();
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

	@Override
	protected void createEditPolicies() {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop.equals("startRender"))
			this.refresh();
		if (prop.equals("locationSize"))
			refreshVisuals();
		else if (prop.equals("color"))
			refreshColor();
		else if (prop.equals("displayChange")) {
			refreshChildren();
			// this.refresh();

		} else if (prop.equals("visibilityChange")) {
			refreshVisuals();
		}
	}

	public void refreshChildren() {
		// System.out.println("boxeditpart refreshchildren");
	}

	public void refreshColor() {
		IFigure f = getFigure();
		if (f != null) {
			if (f instanceof BoxFigure)
				((BoxFigure) getFigure()).setGradientFill(getGradient());
			else
				((RoundedBoxFigure) getFigure()).setGradientFill(getGradient());
			// getFigure().setForegroundColor(
			// ColorManager.getColor(getForegroundColor()));
			// getFigure().setBackgroundColor(
			// ColorManager.getColor(getFillColor()));
		}
	}

	@Override
	protected void refreshVisuals() {
		Box model = (Box) getModel();
		Point loc = model.getLocation();
		Dimension size = model.getSize();
		boolean fill = model.getfill();
		Figure f = (Figure) getFigure();

		if (f != null) {
			f.setLocation(loc);
			f.setBounds(new Rectangle(loc, size));
			if (size.height <= 1 || size.width <= 1)
				f.setVisible(false);
			else
				f.setVisible(!model.hidden());
		}

		// if the figure is either <=1 depth or <=1 height
		// don't display it (hack)
		// DiagramView _diagramView = GUIDemo.getInstance().getView();
		// if (_diagramView != null)
		// for (int i = 0; i < _diagramView.getFigureBoxs().size(); i++) {
		// f = _diagramView.getFigureBoxs().get(model.identity);
		// f.setLocation(loc);
		// if (f != null) {
		// if (size.height <= 1 || size.width <= 1)
		// f.setVisible(false);
		// else
		// f.setVisible(!model.hidden());
		// }
		// }
		// if (size.height <= 1 || size.width <= 1)
		// f.setVisible(false);
		// else
		// f.setVisible(!model.hidden());
		// this.getViewer().deselectAll();
		if (f instanceof BoxFigure) {
			// BoxFigure fig = (BoxFigure) f;
			// fig.setFill(fill);
			// fig.top = model.showTop();
			// fig.bottom = model.showBottom();
			// fig.left = model.showLeft();
			// fig.right = model.showRight();
		} else
			// ((RoundedBoxFigure) f).setFill(fill);
			// Rectangle r = new Rectangle(loc, size);
			// ((GraphicalEditPart) getParent()).setLayoutConstraint(this,
			// getFigure(), r);
			refreshColor();
	}

	@Override
	public boolean isSelectable() {
		Box box = (Box) getModel();
		return !(box.parent instanceof AbstractDiagram);
	}

	@Override
	public void preferenceUpdate() {
		// IFigure figure = getFigure();
		refreshColor();
		/*
		 * if(figure instanceof BoxFigure) { BoxFigure box = (BoxFigure)figure;
		 * box.preferenceUpdate(); } else { RoundedBoxFigure rounded =
		 * (RoundedBoxFigure)figure; rounded.preferenceUpdate(); }
		 */

		List children = getChildren();
		for (int i = 0; i < children.size(); i++) {
			CommandEventEditPart part = (CommandEventEditPart) children.get(i);
			part.preferenceUpdate();
		}
		this.deactivate();
	}

}