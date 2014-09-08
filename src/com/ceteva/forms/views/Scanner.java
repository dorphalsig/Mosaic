package com.ceteva.forms.views;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;

import com.ceteva.forms.FormsPlugin;

// TODO: Auto-generated Javadoc
/**
 * The Class Scanner.
 */
class Scanner extends RuleBasedScanner {

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
  private ArrayList rules = new ArrayList();
  
  /** The red token. */
  private static IToken redToken = new Token(new TextAttribute(FormsPlugin.RED));
  
  /** The green token. */
  private static IToken greenToken = new Token(new TextAttribute(FormsPlugin.GREEN));
  
  /** The blue token. */
  private static IToken blueToken = new Token(new TextAttribute(FormsPlugin.BLUE));
  
  /** The default token. */
  private static IToken defaultToken = new Token(new TextAttribute(FormsPlugin.BLACK));
  
  /**
   * Instantiates a new scanner.
   */
  public Scanner() {
	setDefaultReturnToken(defaultToken);
    addRule("//","green"); // for some reason it needs at least one rule!
  }
  
  /**
   * Adds the rule.
   *
   * @param word the word
   * @param color the color
   */
  public void addRule(String word,String color) {
	if(word.length()>0) {
	  IToken token = getToken(color);
	  EvaluateWord rule = new EvaluateWord(new WordDector(word),token,word);
	  rules.add(rule);
	  updateRules();
	}
  }
  
  /**
   * Gets the token.
   *
   * @param color the color
   * @return the token
   */
  public IToken getToken(String color) {
    if(color.equals("red"))
      return redToken;
    else if(color.equals("green"))
      return greenToken;
    return blueToken;
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
	addRule("//","green"); 
    updateRules();
  }		
}
