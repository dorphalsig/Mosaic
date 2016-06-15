package tool.doc;

import java.awt.Color;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	
	String name;
	TreeNodeType type;
	String content;

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

	public String getType() {
		return "default";
	}

	public JPanel createPanel() {
		JPanel p = new JPanel();
//		p.setBackground(new Color((int)(Math.random()*0x1000000)));
		return p;
	}

	public ImageIcon getIcon() {return null;}

	public void storeValues() {}

	public void save(PrintStream out) {}
}
