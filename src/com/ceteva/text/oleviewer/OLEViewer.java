package com.ceteva.text.oleviewer;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import uk.ac.mdx.xmf.swt.client.EventHandler;
import xos.Message;
import xos.Value;

import com.ceteva.menus.MenuBuilder;
import com.ceteva.text.stubs.OLE;
import com.ceteva.text.stubs.OleClientSite;
import com.ceteva.text.stubs.OleFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class OLEViewer.
 */
public class OLEViewer extends EditorPart {

	/** The identity. */
	String identity = "";
	
	/** The file. */
	String file = "";
	
	/** The type. */
	String type = "";
	
	/** The site. */
	OleClientSite site = null;
	
	/** The model. */
	OLEViewerModel model = null;
	
	/** The handler. */
	EventHandler handler = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		Message m = handler.newMessage("oleClosed", 1);
		Value v1 = new Value(identity);
		m.args[0] = v1;
		handler.raiseEvent(m);
		MenuBuilder.dispose(getSite());
		model.dispose();
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor arg0) {
		System.out.println(file);
		File f = new File(file);
		site.save(f, true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		System.out.println(file);
		site.save(new File(file), true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite iSite, IEditorInput iInput)
			throws PartInitException {
		setSite(iSite);
		setInput(iInput);
		if (iInput instanceof OLEViewerInput) {
			OLEViewerInput input = (OLEViewerInput) iInput;
			this.identity = input.getIdentity();
			this.file = input.getFile();
			this.type = input.getType();
		}
		model = new OLEViewerModel(identity, null, this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		return site.isDirty();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new org.eclipse.swt.layout.FillLayout());
		OleFrame frame = new OleFrame(parent, SWT.NONE);
		if (!file.equals("")) {
			File f = new File(file);
			site = new OleClientSite(frame, org.eclipse.swt.SWT.NONE, type, f);
		} else
			site = new OleClientSite(frame, org.eclipse.swt.SWT.NONE, type);
		site.doVerb(OLE.OLEIVERB_SHOW);
	}

	/**
	 * Save as.
	 *
	 * @param filename the filename
	 */
	public void saveAs(String filename) {
		site.save(new File(filename), true);
	}

	/**
	 * Sets the event handler.
	 *
	 * @param handler the new event handler
	 */
	public void setEventHandler(EventHandler handler) {
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}

}