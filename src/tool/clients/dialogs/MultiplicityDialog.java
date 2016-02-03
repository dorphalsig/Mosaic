package tool.clients.dialogs;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MultiplicityDialog extends org.eclipse.jface.dialogs.Dialog{

	private Text txtMul;
	private Text txtMin;
	private Text txtMax;
	private Button boxNoUpperBound;
	private Button boxOrdered;
	private Object listenerBlockedBy;
	
	public static void main(String[] args) {
		MultiplicityDialog m = new MultiplicityDialog((Shell)null);
		m.open();
		
	}

	protected MultiplicityDialog(Shell parentShell) { super(parentShell); }
    protected MultiplicityDialog(IShellProvider parentShell) { super(parentShell); }
    
    @Override
    protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    GridLayout layout = new GridLayout(5, false);
	    container.setLayout(layout);

	    Label labelMin = new Label(container, SWT.NONE); labelMin.setText("Lower bound");
	    txtMin = new Text(container, SWT.BORDER);
	    txtMin.setText("1");
	    
	    boxNoUpperBound = new Button(container, SWT.CHECK); boxNoUpperBound.setText("No upper bound");
	    Label dummy1 = new Label(container, SWT.NONE); dummy1.setText(" ");
	    boxNoUpperBound.setSelection(true);
	    
	    Label labelMax = new Label(container, SWT.NONE); labelMax.setText("Upper bound");
	    txtMax = new Text(container, SWT.BORDER);
	    txtMax.setEnabled(false);
	    txtMax.setText("*");
	    
	    boxOrdered = new Button(container, SWT.CHECK); boxOrdered.setText("Ordered");
	    Label dummy2 = new Label(container, SWT.NONE); dummy2.setText(" ");
	    boxOrdered.setSelection(true);
	    
	    Label labelMul = new Label(container, SWT.NONE); labelMul.setText("Multiplicity");
	    txtMul = new Text(container, SWT.BORDER);
	    txtMul.setText("$1..*");
	    
	    Label dummy3 = new Label(container, SWT.NONE); dummy3.setText("Quick Select:");
	    
	    Button default1 = new Button(container, SWT.PUSH); default1.setText("     1     ");
	    Button default2 = new Button(container, SWT.PUSH); default2.setText("     !     ");
	    Button default3 = new Button(container, SWT.PUSH); default3.setText("     *     ");
	    Button default4 = new Button(container, SWT.PUSH); default4.setText("     $     ");
	    
	    GridData textFieldGridData = new GridData();
	    textFieldGridData.grabExcessHorizontalSpace = true;
	    textFieldGridData.horizontalAlignment = GridData.FILL;
	    textFieldGridData.horizontalSpan = 4;
	    txtMin.setLayoutData(textFieldGridData);
	    txtMax.setLayoutData(textFieldGridData);
	    txtMul.setLayoutData(textFieldGridData);
	    dummy1.setLayoutData(textFieldGridData);
	    dummy2.setLayoutData(textFieldGridData);

	    default1.addListener(SWT.Selection, new MyButtonListener("1"));	
	    default2.addListener(SWT.Selection, new MyButtonListener("2"));	
	    default3.addListener(SWT.Selection, new MyButtonListener("3"));	
	    default4.addListener(SWT.Selection, new MyButtonListener("4"));

//	    MyListener myListener = new MyListener();
	    
	    txtMin.addModifyListener(new MyListener(txtMin));
	    boxNoUpperBound.addListener(SWT.Selection, new MyListener(boxNoUpperBound));
	    txtMax.addModifyListener(new MyListener(txtMax));
	    boxOrdered.addListener(SWT.Selection, new MyListener(boxOrdered));
	    txtMul.addModifyListener(new MyListener(txtMul));
      return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    protected void configureShell(Shell newShell) {
      super.configureShell(newShell);
      newShell.setText("Selection dialog");
    }

    @Override
    protected Point getInitialSize() {
      return new Point(350, 270);
    }		
    
	private void updateValues(Object whoDidIt) {
		if(listenerBlockedBy != null) {
//			System.out.println("listener blocked by " + listenerBlockedBy);
			return;
		}
		listenerBlockedBy = whoDidIt;
		if(whoDidIt == txtMin) {
			updateMul();
		} else if(whoDidIt == txtMax) {
			updateMul();
		} else if(whoDidIt == txtMul) {
			updateOthers();
		} else if(whoDidIt == boxNoUpperBound) {
			txtMax.setEnabled(!boxNoUpperBound.getSelection());
			txtMax.setText(boxNoUpperBound.getSelection()?"*":txtMin.getText());
			updateMul();
		} else if(whoDidIt == boxOrdered) {
			updateMul();
		} else throw new IllegalStateException();
		listenerBlockedBy = null;
	}
    
    private void updateOthers() {
		// TODO Auto-generated method stub
	}

	private void updateMul() {
		try{
			Integer min = Integer.parseInt(txtMin.getText());
			String s = min+"";
			if(boxNoUpperBound.getSelection()) {
				// no upper bound
				s += "..*";
			} else {
				Integer max = Integer.parseInt(txtMax.getText());
				if(min < max) {
					s += ".." +max;
				} else if(max < min) {
					throw new IllegalArgumentException("min ("+min+") > max ("+max+") not allowed");
				}
			}
			if(boxOrdered.getSelection()) {
				s = "$" + s;
			}
			txtMul.setText(s);
		} catch (Exception e) {
			txtMul.setText(e.getMessage());
		}
	}

	private final class MyListener implements Listener, ModifyListener {
    	
    	private final Object whoDidIt;
    	
    	private MyListener(Object whoDidIt) {
    		this.whoDidIt = whoDidIt;
    	}

		@Override
		public void handleEvent(Event e) {
			updateValues(whoDidIt);
		}

		@Override
		public void modifyText(ModifyEvent e) {
			updateValues(whoDidIt);
		}
    }
    
    private final class MyButtonListener implements Listener {

    	final String value;
    	
    	private MyButtonListener(String value) {
    		this.value = value;
    	}
    	
		@Override
		public void handleEvent(Event e) {
			switch (e.type) {
			case SWT.Selection:
				txtMul.setText(value);
				break;
			}
		}
    }

}
