package uk.ac.mdx.xmf.swt;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import uk.ac.mdx.xmf.swt.demo.GUIDemo;

public class Overview {
	private FigureCanvas canvas;
	private Figure rootFigure = new Figure();

	public Overview(Composite parent, int style, CTabItem tabItem) {
		canvas = new FigureCanvas(parent, SWT.H_SCROLL & SWT.V_SCROLL);
		canvas.setBounds(0, 0, parent.getBounds().width,
				parent.getBounds().height);
		canvas.setBackground(ColorConstants.white);
		canvas.setScrollBarVisibility(FigureCanvas.ALWAYS);

		tabItem.setControl(canvas);

		parent.layout(true);
	}

	public void display() {

		canvas.setContents(rootFigure);
		rootFigure.repaint();

		canvas.update();
		canvas.layout(true);
		canvas.pack();

		GUIDemo.getInstance().tabFolderOverview.layout(true);
	}

	public void setRootFigure(Figure rootFigure) {
		this.rootFigure = rootFigure;
	}
}
