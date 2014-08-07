package uk.ac.mdx.xmf.swt.editPart;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import uk.ac.mdx.xmf.swt.DiagramView;
import uk.ac.mdx.xmf.swt.demo.GUIDemo;
import uk.ac.mdx.xmf.swt.model.CommandEvent;

public abstract class CommandEventEditPart extends AbstractGraphicalEditPart
		implements PropertyChangeListener {
	CommandEvent parent;
	DiagramView _diagramView;

	IFigure figure;

	@Override
	public void activate() {
		if (isActive() == false) {
			super.activate();
			parent.addPropertyChangeListener(this);
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			getModel().removePropertyChangeListener(this);
		}
	}

	@Override
	public void setFigure(IFigure figure) {
		this.figure = figure;
	}

	@Override
	public IFigure getFigure() {
		return figure;
	}

	@Override
	public void refresh() {
		// System.out.print(getModel().identity + ":refresh");
		// _diagramView.update();
		// if (_diagramView != null)
		// _diagramView.display(true);
		GUIDemo.getInstance().getView().rootFigure.repaint();
	}

	@Override
	public void refreshChildren() {
	}

	public String getModelIdentity() {
		return getModel().getIdentity();
	}

	public void preferenceUpdate() {
	}

	public void setModel(CommandEvent parent) {
		this.parent = parent;
	}

	@Override
	public CommandEvent getModel() {
		return parent;
	}

	public void setDiagramView(DiagramView view) {
		_diagramView = view;
	}

}