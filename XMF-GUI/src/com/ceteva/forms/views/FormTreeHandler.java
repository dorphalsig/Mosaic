package com.ceteva.forms.views;


public interface FormTreeHandler {

	public void doubleSelected(String identity);

	public void deselected(String identity);

	public void enableDrag();

	public void enableDrop();

	public void getEditableText(String identity);

	public void selected(String identity);

	public void treeExpanded(String identity);

	public void textChanged(String identity, String text);

}
