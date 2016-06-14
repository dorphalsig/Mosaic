package tool.doc;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component defaultResult = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		JLabel l = (JLabel) defaultResult;
		l.setForeground(Color.RED);
		return defaultResult;
	}

}
