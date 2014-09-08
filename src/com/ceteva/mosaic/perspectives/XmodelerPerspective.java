package com.ceteva.mosaic.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class XmodelerPerspective.
 */
public class XmodelerPerspective implements IPerspectiveFactory {

	/** The Constant ModelBrowserViewID. */
	private static final String ModelBrowserViewID = "com.ceteva.modelBrowser.view";
	
	/** The Constant PRODUCTBOTTOM. */
	private static final String PRODUCTBOTTOM = "ProductBottom";
	
	/** The Constant LEFTBAR. */
	private static final String LEFTBAR = "LeftBar";
	
	/** The Constant BOTTOMLEFT. */
	private static final String BOTTOMLEFT = "BottomLeft";
	
	/** The Constant BOTTOMRIGHT. */
	private static final String BOTTOMRIGHT = "BottomRight";

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {

		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		// bottom
		layout.createFolder(PRODUCTBOTTOM, IPageLayout.BOTTOM, 0.66f,
				layout.getEditorArea());

		// outline
		IFolderLayout bottomLeft = layout.createFolder(BOTTOMLEFT,
				IPageLayout.LEFT, 0.25f, PRODUCTBOTTOM);
		bottomLeft.addView("org.eclipse.ui.views.ContentOutline");
		
		// windows forms,console
		IFolderLayout bottom = layout.createFolder(BOTTOMRIGHT,
				IPageLayout.RIGHT, 0.66f, PRODUCTBOTTOM);
		bottom.addView("com.ceteva.console.view");
		IViewLayout out = layout.getViewLayout("com.ceteva.console.view");
		out.setCloseable(false);
		out.setMoveable(true);
		bottom.addPlaceholder("com.ceteva.forms.view:*");

		
		// Left --> Browser
		IFolderLayout left = layout.createFolder(LEFTBAR, IPageLayout.LEFT,
				0.25f, layout.getEditorArea());
		//A Bug modelbrowsers are not shown in the right place!
		left.addPlaceholder(ModelBrowserViewID + ":*");
		//left.addView(ModelBrowserViewID);
		
	}
}