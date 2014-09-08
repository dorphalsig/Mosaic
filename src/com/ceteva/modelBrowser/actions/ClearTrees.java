package com.ceteva.modelBrowser.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;

import uk.ac.mdx.xmf.swt.client.IconManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ClearTrees.
 */
public class ClearTrees extends Action {

	/** The trees. */
	CTabFolder trees;

	/**
	 * Instantiates a new clear trees.
	 *
	 * @param trees the trees
	 */
	public ClearTrees(CTabFolder trees) {
		this.setId("com.ceteva.modelBrowser.actions.ClearTrees");
		this.trees = trees;
		setText("Clear trees");
		setToolTipText("Clear all open trees");
		setImageDescriptor(IconManager.getImageDescriptorAbsolute("Clear.gif"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		/*
		 * if(trees != null) { CTabItem[] fs = trees.getItems(); for(int
		 * i=0;i<fs.length;i++) { ModelBrowser t = (ModelBrowser)fs[i];
		 * if(t.isClosable()) { t.treeClosed(); t.dispose(); } } }
		 */
	}

	/**
	 * Update.
	 */
	public void update() {
		this.setEnabled(trees.getItemCount() > 0);
	}
}