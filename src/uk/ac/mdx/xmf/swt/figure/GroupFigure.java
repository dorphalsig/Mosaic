package uk.ac.mdx.xmf.swt.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.LayerConstants;

import uk.ac.mdx.xmf.swt.editPart.ConnectionLayerManager;

// TODO: Auto-generated Javadoc
/**
 * The Class GroupFigure.
 */
public class GroupFigure extends Figure implements ZoomableFigure {

	/** The pane. */
	private final ScalableFreeformLayeredPane pane;
	
	/** The viewport. */
	private final FreeformViewport viewport;

	/**
	 * Instantiates a new group figure.
	 *
	 * @param identity the identity
	 * @param position the position
	 * @param size the size
	 * @param connectionManager the connection manager
	 */
	public GroupFigure(String identity, Point position, Dimension size,
			ConnectionLayerManager connectionManager) {
		setLocation(position);
		setSize(size);

		pane = new ScalableFreeformLayeredPane();
		pane.setLayoutManager(new FreeformLayout());
		connectionManager.addLayer(identity, pane);

		setLayoutManager(new StackLayout());

		ScrollPane scrollpane = new ScrollPane();
		add(scrollpane);

		viewport = new FreeformViewport();

		scrollpane.setViewport(viewport);
		scrollpane.setContents(pane);
		setOpaque(true);

		preferenceUpdate();
	}

	/**
	 * Gets the feedback layer.
	 *
	 * @return the feedback layer
	 */
	public IFigure getFeedbackLayer() {
		return pane.getLayer(LayerConstants.PRIMARY_LAYER);
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.figure.ZoomableFigure#getFigure()
	 */
	@Override
	public ScalableFigure getFigure() {
		return pane;
	}

	/* (non-Javadoc)
	 * @see uk.ac.mdx.xmf.swt.figure.ZoomableFigure#getViewPort()
	 */
	@Override
	public IFigure getViewPort() {
		return viewport;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
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
		// IPreferenceStore ipreferences =
		// DiagramPlugin.getDefault().getPreferenceStore();
		// RGB color =
		// PreferenceConverter.getColor(ipreferences,IPreferenceConstants.DIAGRAM_BACKGROUND_COLOR);
		// setBackgroundColor(ColorManager.getColor(color));
	}
}
