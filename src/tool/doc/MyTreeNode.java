package tool.doc;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	
	String name;
	TreeNodeType type;
	String content;

	public MyTreeNode(Object userObject) {
		super(userObject);
	}

}
