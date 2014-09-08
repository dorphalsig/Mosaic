package com.ceteva.text.highlighting;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;


// TODO: Auto-generated Javadoc
/**
 * The Class SinglelineScanner.
 */
public class SinglelineScanner extends RuleBasedScanner {

  /**
   * The Class WordDector.
   */
  class WordDector implements IWordDetector {
  	
  	/** The word. */
	  private String word = "";
  	
  	/**
	   * Instantiates a new word dector.
	   *
	   * @param word the word
	   */
	  public WordDector(String word) {
  	  this.word = word;
  	}
  	
  	/* (non-Javadoc)
	   * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
	   */
	  public boolean isWordStart(char c) {
  	  if(word.length()>0)
  	    return c == word.charAt(0);
  	  return false;	
  	}
  	
  	/* (non-Javadoc)
	   * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
	   */
	  public boolean isWordPart(char c) {
  	  for(int i=0;i<word.length();i++) {
  	  	if(c == word.charAt(i))
  	  	  return true;	
  	  }
  	  return false;
  	}
  }

  /** The a. */
  private IRule[] a = new IRule[1];
  
  /** The rules. */
  private ArrayList rules;
  
  /**
   * Instantiates a new singleline scanner.
   */
  public SinglelineScanner() {
	setDefaultReturnToken(ScannerTokens.getDefaultToken());
	clearRules(); 
  }
  
  /**
   * Adds the rule.
   *
   * @param word the word
   * @param color the color
   */
  public void addRule(String word,String color) {
	if(word.length()>0) {
	  IToken token = ScannerTokens.getToken(color);
	  EvaluateWord rule = new EvaluateWord(new WordDector(word),token,word);
	  rules.add(rule);
	  updateRules();
	}
  }
  
  /**
   * Update rules.
   */
  public void updateRules() {
	setRules((IRule[])rules.toArray(a));	
  }	
  
  /**
   * Clear rules.
   */
  public void clearRules() {
    rules = new ArrayList();
    // addRule("//","green");  // for some reason it needs at least one rule! 
    rules.add(new EndOfLineRule("//",ScannerTokens.getToken("red")));
    updateRules();
  }		
}
