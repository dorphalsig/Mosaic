package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.model.CommandEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandEventEditPart.
 */
public abstract class CommandEventEditPart extends AbstractGraphicalEditPart
		implements PropertyChangeListener {

	/** The parent. */
	CommandEvent parent;

	/** The _diagram view. */
	DiagramView _diagramView;

	/** The figure. */
	IFigure figure;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive() == false) {
			super.activate();
			parent.addPropertyChangeListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			getModel().removePropertyChangeListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#setFigure(org.eclipse
	 * .draw2d.IFigure)
	 */
	@Override
	public void setFigure(IFigure figure) {
		this.figure = figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getFigure()
	 */
	@Override
	public IFigure getFigure() {
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refresh()
	 */
	@Override
	public void refresh() {
		// System.out.print(getModel().identity + ":refresh");
		// _diagramView.update();
		// if (_diagramView != null)
		// _diagramView.display(true);
		// Main.getInstance().getView().display();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	@Override
	public void refreshChildren() {
	}

	/**
	 * Gets the model identity.
	 * 
	 * @return the model identity
	 */
	public String getModelIdentity() {
		return getModel().getIdentity();
	}

	/**
	 * Preference update.
	 */
	public void preferenceUpdate() {
	}

	/**
	 * Sets the model.
	 * 
	 * @param parent
	 *            the new model
	 */
	public void setModel(CommandEvent parent) {
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModel()
	 */
	@Override
	public CommandEvent getModel() {
		return parent;
	}

	/**
	 * Sets the diagram view.
	 * 
	 * @param view
	 *            the new diagram view
	 */
	public void setDiagramView(DiagramView view) {
		_diagramView = view;
	}

}