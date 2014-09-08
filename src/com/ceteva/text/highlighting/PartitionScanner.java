package com.ceteva.text.highlighting;

import java.util.ArrayList;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

// TODO: Auto-generated Javadoc
/**
 * The Class PartitionScanner.
 */
public class PartitionScanner extends RuleBasedPartitionScanner {
	
  /** The a. */
  private IPredicateRule[] a = new IPredicateRule[1];
  
  /** The rules. */
  private ArrayList rules;
 
  /**
   * Instantiates a new partition scanner.
   */
  public PartitionScanner() {
  	super();
  	clearRules();
  }
  
  /**
   * Adds the rule.
   *
   * @param id the id
   * @param start the start
   * @param end the end
   */
  public void addRule(String id,String start,String end) {
  	if(start.length()>0 && end.length()>0) {
  	  IToken token = new Token(id);
  	  MultiLineRule rule = new MultiLineRule(start,end,token,(char)0,true);
  	  rules.add(rule);
  	  updateRules();
  	}
  }
  
  /**
   * Update rules.
   */
  public void updateRules() {
	setPredicateRules((IPredicateRule[])rules.toArray(a));	
  }
  
  /**
   * Clear rules.
   */
  public void clearRules() {
    rules = new ArrayList();
    // addRule(PartitionTypes.MULTI_LINE,"/*","*/");
  }
}