package com.ceteva.text.texteditor;

import java.util.Vector;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.ceteva.text.TextPlugin;
import com.ceteva.text.highlighting.ScannerTokens;
import com.ceteva.text.highlighting.SinglelineScanner;

// TODO: Auto-generated Javadoc
/**
 * The Class TextConfiguration.
 */
class TextConfiguration extends SourceViewerConfiguration {

  /** The reconciler. */
  private PresentationReconciler reconciler = new PresentationReconciler();
  
  /** The partition types. */
  private Vector partitionTypes = new Vector();	
  
  /** The scanner. */
  private SinglelineScanner scanner = null;
  
  /** The identity. */
  private String identity = "";
  // private CustomUndoManager undoManager;
  
	
  /**
   * The Class SingleTokenScanner.
   */
  static class SingleTokenScanner extends BufferedRuleBasedScanner {
    
    /**
     * Instantiates a new single token scanner.
     *
     * @param attribute the attribute
     */
    public SingleTokenScanner(TextAttribute attribute) {
	  setDefaultReturnToken(new Token(attribute));
	}
  }

  /**
   * Instantiates a new text configuration.
   *
   * @param identity the identity
   */
  public TextConfiguration(String identity) {
  	partitionTypes.add(IDocument.DEFAULT_CONTENT_TYPE);
  	this.identity = identity;
  }
  
  /**
   * Gets the tag scanner.
   *
   * @return the tag scanner
   */
  protected SinglelineScanner getTagScanner() {
  	if(scanner == null)
  	  scanner = new SinglelineScanner();
  	return scanner;
  }
  
  /**
   * Adds the partition.
   *
   * @param document the document
   * @param id the id
   * @param start the start
   * @param end the end
   * @param color the color
   */
  public void addPartition(IDocument document,String id,String start,String end,String color) {
  	if(reconciler!=null) {
  	  partitionTypes.addElement(id);
  	  DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new SingleTokenScanner(new TextAttribute(ScannerTokens.getColour(color))));
  	  dr.setDocument(document);
  	  reconciler.setDamager(dr,id);
      reconciler.setRepairer(dr,id);
  	}
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
   */
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceView) {
  	reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceView));
  	DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTagScanner());
  	reconciler.setDamager(dr,IDocument.DEFAULT_CONTENT_TYPE);
  	reconciler.setRepairer(dr,IDocument.DEFAULT_CONTENT_TYPE);
  	return reconciler;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
   */
  public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
    return TextPlugin.PARTITIONER;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
   */
  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
  	String[] s = new String[partitionTypes.size()];
  	for(int i=0;i<partitionTypes.size();i++) {
  	  String t = (String)partitionTypes.elementAt(i);
  	  s[i] = t;
  	}
  	return s;
  }
  
  /* public void setEventHandler(EventHandler handler) {
  	if(undoManager==null)
  		undoManager = new CustomUndoManager(10,identity);
  	undoManager.setEventHandler(handler);
  } */
  
  /* public IUndoManager getUndoManager(ISourceViewer sourceViewer) {
  	if(undoManager==null) {
  		undoManager = new CustomUndoManager(10,identity);
  	}
  	return undoManager;
  } */
}
