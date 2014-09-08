package com.ceteva.text.texteditor;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;

import com.ceteva.text.TextPlugin;
import com.ceteva.text.highlighting.PartitionScanner;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentProvider.
 */
class DocumentProvider extends AbstractDocumentProvider {
	
	/** The partitioner. */
	private FastPartitioner partitioner;
	
	/** The document. */
	private Document document = new Document();
	
	/** The scanner. */
	private PartitionScanner scanner = new PartitionScanner();
	
	/** The partition types. */
	private Vector partitionTypes = new Vector();
	
	/**
	 * Adds the rule.
	 *
	 * @param id the id
	 * @param start the start
	 * @param end the end
	 */
	public void addRule(String id,String start,String end) {
		partitionTypes.addElement(id);
		setDocumentPartitioner();
		scanner.addRule(id,start,end);
	}
	
	/**
	 * Sets the document partitioner.
	 */
	public void setDocumentPartitioner() {
		if (document instanceof IDocumentExtension3) {
		  IDocumentExtension3 extension3= (IDocumentExtension3) document;
		  String[] s = new String[partitionTypes.size()];
		  for(int i=0;i<partitionTypes.size();i++) {
		    String t = (String)partitionTypes.elementAt(i);
		  	s[i] = t;
		  }
		  partitioner = new FastPartitioner(scanner,s);
		  extension3.setDocumentPartitioner(TextPlugin.PARTITIONER,partitioner);
		  partitioner.connect(document);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(java.lang.Object)
	 */
	protected IDocument createDocument(Object element) {
		partitionTypes.add(IDocument.DEFAULT_CONTENT_TYPE);
		setDocumentPartitioner();
		return document;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
	 */
	protected void doSaveDocument(IProgressMonitor monitor,
		Object element,
        IDocument document,
        boolean overwrite) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createAnnotationModel(java.lang.Object)
	 */
	protected IAnnotationModel createAnnotationModel(Object element) {
		AnnotationModel model = new AnnotationModel(); 
		Annotation a = new Annotation("com.ceteva.text.marker", true, "com.ceteva.text.marker");
		model.addAnnotation(a,new Position(5,10));
		return model;
	}

}