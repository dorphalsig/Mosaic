package com.ceteva.forms.views;


// TODO: Auto-generated Javadoc
/**
 * The Interface FormTreeHandler.
 */
public interface FormTreeHandler {

	/**
	 * Double selected.
	 *
	 * @param identity the identity
	 */
	public void doubleSelected(String identity);

	/**
	 * Deselected.
	 *
	 * @param identity the identity
	 */
	public void deselected(String identity);

	/**
	 * Enable drag.
	 */
	public void enableDrag();

	/**
	 * Enable drop.
	 */
	public void enableDrop();

	/**
	 * Gets the editable text.
	 *
	 * @param identity the identity
	 * @return the editable text
	 */
	public void getEditableText(String identity);

	/**
	 * Selected.
	 *
	 * @param identity the identity
	 */
	public void selected(String identity);

	/**
	 * Tree expanded.
	 *
	 * @param identity the identity
	 */
	public void treeExpanded(String identity);

	/**
	 * Text changed.
	 *
	 * @param identity the identity
	 * @param text the text
	 */
	public void textChanged(String identity, String text);

}
