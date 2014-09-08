package com.ceteva.mosaic;



import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

// TODO: Auto-generated Javadoc
/**
 * The Class WindowAdvisor.
 */
public class WindowAdvisor extends WorkbenchWindowAdvisor {
	
    /**
     * Instantiates a new window advisor.
     *
     * @param configurer the configurer
     */
    public WindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    	configurer.setShowPerspectiveBar(true);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    public ActionBarAdvisor createActionBarAdvisor(
        IActionBarConfigurer actionBarConfigurer) {
        return new ActionAdvisor(actionBarConfigurer);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose()
     */
    public boolean preWindowShellClose() {
  	  	ActionAdvisor.exit.run();
  	  	return false;
  	}
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
     */
    public void preWindowOpen() {
        super.preWindowOpen();
    	IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
    }
}