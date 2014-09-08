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

// TODO: Auto-generated Javadoc
/**
 * The Class BoxEditPart.
 */
public class BoxEditPart extends DisplayEditPart {

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#activate()
	 */
	public void activate() {
		super.activate();
		// Text model = (Text) getModel();
		// if (model.edit())
		// this.editText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
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

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
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

	/**
	 * Gets the foreground color.
	 *
	 * @return the foreground color
	 */
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

	/**
	 * Gets the gradient.
	 *
	 * @return the gradient
	 */
	public boolean getGradient() {
		return false;
		// Preferences preferences = DiagramPlugin.getDefault()
		// .getPluginPreferences();
		// return preferences.getBoolean(IPreferenceConstants.GRADIENT_FILL);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {
		return ((Box) getModel()).getContents();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new DisplaySelectionTracker(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
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

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#refreshChildren()
	 */
	public void refreshChildren() {
		// System.out.println("boxeditpart refreshchildren");
	}

	/**
	 * Refresh color.
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		Box box = (Box) getModel();
		return !(box.parent instanceof AbstractDiagram);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.editPart.CommandEventEditPart#preferenceUpdate()
	 */
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