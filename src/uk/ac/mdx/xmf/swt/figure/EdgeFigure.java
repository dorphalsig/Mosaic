package uk.ac.mdx.xmf.swt.figure;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import uk.ac.mdx.xmf.swt.diagram.geometry.PointListUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class EdgeFigure.
 */
public class EdgeFigure extends PolylineConnection {

	/** The ref. */
	private Point ref;

	/** The type. */
	private String type = "normal";

	/** The smooth. */
	private final int smooth = 30; // 0 none, 15 some, 30 lots

	/** The Constant dimCheck. */
	private static final Dimension dimCheck = new Dimension(100, 100);

	/** The tolerance. */
	private static int TOLERANCE = 3;

	/** The linebounds. */
	private static Rectangle LINEBOUNDS = Rectangle.SINGLETON;

	/**
	 * Instantiates a new edge figure.
	 */
	public EdgeFigure() {
		getPreferences();
	}

	/**
	 * Calculate tolerance.
	 *
	 * @param isFeedbackLayer the is feedback layer
	 * @return the int
	 */
	private int calculateTolerance(boolean isFeedbackLayer) {
		Dimension absTol = new Dimension(TOLERANCE + lineWidth / 2, 0);
		return absTol.width;
	}

	// @Override
	/**
	 * Contains point.
	 *
	 * @param x the x
	 * @param y the y
	 * @param flag the flag
	 * @return true, if successful
	 */
	public boolean containsPoint(int x, int y, boolean flag) {
		// return true;
		// if (isSplined()) {
		//
		// } else {
		// return super.containsPoint(x, y);
		// }
		// if (isSplined())
		{
			boolean isFeedbackLayer = isFeedbackLayer();
			int calculatedTolerance = calculateTolerance(isFeedbackLayer);

			LINEBOUNDS.setBounds(getBounds());
			LINEBOUNDS.expand(calculatedTolerance, calculatedTolerance);
			if (!LINEBOUNDS.contains(x, y))
				return false;

			int ints[] = getSmoothPoints().toIntArray();
			for (int index = 0; index < ints.length - 3; index += 2) {
				if (lineContainsPoint(ints[index], ints[index + 1],
						ints[index + 2], ints[index + 3], x, y, isFeedbackLayer))
					return true;
			}
			List children = getChildren();
			for (int i = 0; i < children.size(); i++) {
				if (((IFigure) children.get(i)).containsPoint(x, y))
					return true;
			}

			return false;
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.PolylineConnection#getBounds()
	 */
	@Override
	public Rectangle getBounds() {
		if (isSplined()) {
			if (bounds == null) {
				if (getSmoothFactor() != 0) {
					bounds = getSmoothPoints().getBounds();
					bounds.expand(lineWidth / 2, lineWidth / 2);

					for (int i = 0; i < getChildren().size(); i++) {
						IFigure child = (IFigure) getChildren().get(i);
						bounds.union(child.getBounds());
					}
				} else
					super.getBounds();

				boolean isFeedbackLayer = isFeedbackLayer();
				int calculatedTolerance = calculateTolerance(isFeedbackLayer);
				bounds.expand(calculatedTolerance, calculatedTolerance);
			}
			return bounds;
		} else {
			return super.getBounds();
		}
	}

	/**
	 * Gets the smooth factor.
	 *
	 * @return the smooth factor
	 */
	public int getSmoothFactor() {
		return smooth;
	}

	/**
	 * Gets the smooth points.
	 *
	 * @return the smooth points
	 */
	public PointList getSmoothPoints() {
		if (getSmoothFactor() > 0) {
			return PointListUtilities.calcSmoothPolyline(getPoints(),
					getSmoothFactor(), PointListUtilities.DEFAULT_BEZIERLINES);
		} else {
			return PointListUtilities.copyPoints(getPoints());
		}
	}

	/**
	 * Checks if is feedback layer.
	 *
	 * @return true, if is feedback layer
	 */
	private boolean isFeedbackLayer() {
		Dimension copied = dimCheck.getCopy();
		translateToRelative(copied);
		return dimCheck.equals(copied);
	}

	/**
	 * Checks if is splined.
	 *
	 * @return true, if is splined
	 */
	private boolean isSplined() {
		if (type == null)
			return false;
		return type.equals("splined");
	}

	/**
	 * Line contains point.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param px the px
	 * @param py the py
	 * @param isFeedbackLayer the is feedback layer
	 * @return true, if successful
	 */
	private boolean lineContainsPoint(int x1, int y1, int x2, int y2, int px,
			int py, boolean isFeedbackLayer) {
		LINEBOUNDS.setSize(0, 0);
		LINEBOUNDS.setLocation(x1, y1);
		LINEBOUNDS.union(x2, y2);
		int calculatedTolerance = calculateTolerance(isFeedbackLayer);
		LINEBOUNDS.expand(calculatedTolerance, calculatedTolerance);
		if (!LINEBOUNDS.contains(px, py))
			return false;

		double v1x, v1y, v2x, v2y;
		double numerator, denominator;
		double result = 0;

		if (x1 != x2 && y1 != y2) {
			v1x = (double) x2 - x1;
			v1y = (double) y2 - y1;
			v2x = (double) px - x1;
			v2y = (double) py - y1;

			numerator = v2x * v1y - v1x * v2y;

			denominator = v1x * v1x + v1y * v1y;

			result = numerator * numerator / denominator;
		}

		// if it is the same point, and it passes the bounding box test,
		// the result is always true.
		return result <= calculatedTolerance * calculatedTolerance;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics g) {
		// if (Diagram.antialias)
		g.setAntialias(SWT.ON);
		super.paintFigure(g);
	}

	/**
	 * Sets the source head.
	 *
	 * @param sourceHead the new source head
	 */
	public void setSourceHead(int sourceHead) {
		if (sourceHead != 0) {
			RotatableDecoration head = HeadFactory.getHead(sourceHead);
			setSourceDecoration(head);
		}
	}

	/**
	 * Sets the target head.
	 *
	 * @param targetHead the new target head
	 */
	public void setTargetHead(int targetHead) {
		if (targetHead != 0) {
			RotatableDecoration head = HeadFactory.getHead(targetHead);
			setTargetDecoration(head);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Polyline#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	@Override
	protected void outlineShape(Graphics g) {

		// the built in dashed line is not distinct enough
		// override this

		if (getLineStyle() == SWT.LINE_DASH) {
			int dash[] = { 4, 4 };
			g.setLineDash(dash);
		}
		if (isSplined()) {
			PointList displayPoints = getSmoothPoints();
			g.drawPolyline(displayPoints);
		} else {
			super.outlineShape(g);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return false;
	}

	/**
	 * Sets the ref point.
	 *
	 * @param ref the new ref point
	 */
	public void setRefPoint(Point ref) {
		this.ref = ref;
	}

	/**
	 * Gets the ref point.
	 *
	 * @return the ref point
	 */
	public Point getRefPoint() {
		return ref;
	}

	/**
	 * Preference update.
	 */
	public void preferenceUpdate() {
		getPreferences();
	}

	/**
	 * Gets the preferences.
	 *
	 * @return the preferences
	 */
	public void getPreferences() {
		/*
		 * Preferences preferences = DiagramPlugin.getDefault()
		 * .getPluginPreferences(); splined =
		 * preferences.getBoolean(IPreferenceConstants.SPLINED);
		 */
	}

	/**
	 * Sets the edge type.
	 *
	 * @param type the new edge type
	 */
	public void setEdgeType(String type) {
		this.type = type;
	}
}