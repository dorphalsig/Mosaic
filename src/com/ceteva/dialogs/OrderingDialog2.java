package com.ceteva.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

// TODO: Auto-generated Javadoc
/**
 * The Class OrderingDialog2.
 */
public class OrderingDialog2 extends Dialog {

    // This dialog allows a list of elements to be ordered.
	
    /** The text. */
    private String text;
    
    /** The elements. */
    private String[] elements;

    /** The list. */
    private List list;

    /**
     * Instantiates a new ordering dialog2.
     *
     * @param shell the shell
     * @param text the text
     * @param elements the elements
     */
    public OrderingDialog2(Shell shell, String text, String[] elements) {
        super(shell);
        this.text = text;
        this.elements = elements;
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(text);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	public void okPressed() {
		elements = list.getItems();
		super.okPressed();
		
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
    	Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = false;
        layout.horizontalSpacing = 5;
        layout.verticalSpacing = 10;
        container.setLayout(layout);
        createButtonArea(container);
        createList(container).setLayoutData(new GridData(GridData.FILL_BOTH));
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        Dialog.applyDialogFont(container);
        return parent;
    }

    /**
     * Creates the list.
     *
     * @param parent the parent
     * @return the composite
     */
    private Composite createList(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        container.setLayout(layout);
        container.setLayoutData(new GridData());
        Label label = new Label(container, SWT.NONE);
        label.setText("Source List");
        list = new List(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 225;
        gd.heightHint = 200;
        list.setLayoutData(gd);
        list.setItems(elements);
        return container;
    }

    /**
     * Creates the button area.
     *
     * @param parent the parent
     * @return the composite
     */
    private Composite createButtonArea(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        comp.setLayout(layout);
        comp.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        Composite container = new Composite(comp, SWT.NONE);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 30;
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
   
        new Label(container, SWT.NONE);

        Button up = new Button(container, SWT.PUSH);
        up.setText("Move up");
        up.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        up.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              int selectedIndex = list.getSelectionIndex();
              if(selectedIndex-1>=0) {
                String selectedText = list.getItem(selectedIndex);
                list.remove(selectedIndex);
                list.add(selectedText,selectedIndex-1);
                list.select(selectedIndex-1);
              }
            }
        });

        Button down = new Button(container, SWT.PUSH);
        down.setText("Move down");
        down.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        down.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              int selectedIndex = list.getSelectionIndex();
              if(selectedIndex+1<list.getItemCount()) {
                String selectedText = list.getItem(selectedIndex);
                list.remove(selectedIndex);
                list.add(selectedText,selectedIndex+1);
                list.select(selectedIndex+1);
              }
            }
        });
        return container;
    }
    
    /**
     * Gets the choice.
     *
     * @return the choice
     */
    public String[] getChoice() {
      return elements;	
    }
}
