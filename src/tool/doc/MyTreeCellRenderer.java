package tool.doc;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component defaultResult = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		JLabel l = (JLabel) defaultResult;
		
		if(! (value instanceof MyTreeNode)) {
			return defaultResult;
		}
		
		MyTreeNode node = (MyTreeNode) value;
		ImageIcon icon = node.getIcon();
		if(icon != null) {l.setIcon(icon);}
		
		return defaultResult;
	}

}
