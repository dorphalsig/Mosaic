package de.unidue.icb.xmf.multilevel.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class SpecificDeletionDialogOld {

	private static SpecificDeletion specificDeletion;

	private static Display display;
	private static Shell shlIntrinsicDeletion;

	private static Text txExpression;

	private static Composite coM4;
	private static Composite coM3;
	private static Composite coM2;
	private static Composite coM1;

	private static Button btnDeleteSlot;
	private static Button radioAll;
	private static Button radioNull;
	private static Button radioExpression;

	private static Scale scLevel;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		int definitionLayer = 4;
		int instantiationLayer = 1;
		System.out.println(showDialog(definitionLayer, instantiationLayer));
	}

	/**
	 * @wbp.parser.entryPoint
	 * 
	 */
	public static SpecificDeletion showDialog(int definitionLayer,
			int instantiationLayer) {
		specificDeletion = new SpecificDeletion(definitionLayer,
				instantiationLayer);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				display = Display.getDefault();
				// display = new Display();
				shlIntrinsicDeletion = new Shell();
				shlIntrinsicDeletion.setSize(416, 300);
				shlIntrinsicDeletion.setText("intrinsic Deletion");
				shlIntrinsicDeletion.setLayout(new RowLayout(SWT.VERTICAL));

				Label lblDeletion = new Label(shlIntrinsicDeletion, SWT.NONE);
				lblDeletion
						.setText("Deletion attribute from M4, intrinsic on M1 ");

				Composite composite = new Composite(shlIntrinsicDeletion,
						SWT.NONE);
				composite.setLayout(new RowLayout(SWT.HORIZONTAL));
				composite.setLayoutData(new RowData(394, 209));

				Composite composite_1 = new Composite(composite, SWT.NONE);
				composite_1.setLayoutData(new RowData(25, 112));
				composite_1.setLayout(new FillLayout(SWT.VERTICAL));

				coM4 = new Composite(composite_1, SWT.NONE);
				coM4.setBackground(display.getSystemColor(SWT.COLOR_RED));

				coM3 = new Composite(composite_1, SWT.NONE);
				coM3.setBackground(display
						.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

				coM2 = new Composite(composite_1, SWT.NONE);
				coM2.setBackground(display
						.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

				coM1 = new Composite(composite_1, SWT.NONE);
				coM1.setBackground(display
						.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

				Composite composite_2 = new Composite(composite, SWT.NONE);
				composite_2.setLayout(new FillLayout(SWT.VERTICAL));
				composite_2.setLayoutData(new RowData(35, 113));

				Font font = new Font(Display.getCurrent(), new FontData(
						"Segoe UI", 14, SWT.NORMAL));

				Label lblNewLabel = new Label(composite_2, SWT.NONE);
				lblNewLabel.setFont(font);
				lblNewLabel.setText("M4");

				Label lblNewLabel_1 = new Label(composite_2, SWT.NONE);
				lblNewLabel_1.setFont(font);
				lblNewLabel_1.setText("M3");

				Label lblNewLabel_2 = new Label(composite_2, SWT.NONE);
				lblNewLabel_2.setFont(font);
				lblNewLabel_2.setText("M2");

				Label lblNewLabel_3 = new Label(composite_2, SWT.NONE);
				lblNewLabel_3.setFont(font);
				lblNewLabel_3.setText("M1");

				Composite composite_3 = new Composite(composite, SWT.NONE);
				composite_3.setLayout(new RowLayout(SWT.VERTICAL));
				composite_3.setLayoutData(new RowData(314, 195));

				Composite composite_8 = new Composite(composite_3, SWT.NONE);
				composite_8.setLayout(new RowLayout(SWT.HORIZONTAL));
				composite_8.setLayoutData(new RowData(306, 84));

				scLevel = new Scale(composite_8, SWT.VERTICAL);
				scLevel.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (scLevel.getSelection() == 2) {
							coM4.setBackground(display
									.getSystemColor(SWT.COLOR_RED));
							coM3.setBackground(display
									.getSystemColor(SWT.COLOR_RED));
							coM2.setBackground(display
									.getSystemColor(SWT.COLOR_RED));
							specificDeletion.setDeletionUntilLayer(4);
						} else {
							coM2.setBackground(display
									.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
							if (scLevel.getSelection() == 1) {
								coM4.setBackground(display
										.getSystemColor(SWT.COLOR_RED));
								coM3.setBackground(display
										.getSystemColor(SWT.COLOR_RED));
								specificDeletion.setDeletionUntilLayer(3);
							} else {
								coM3.setBackground(display
										.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
								if (scLevel.getSelection() == 0) {
									coM4.setBackground(display
											.getSystemColor(SWT.COLOR_RED));
									specificDeletion.setDeletionUntilLayer(2);
								}// Fall kann nicht eintreten
									// else{
									// coM4.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
								// }
							}
						}

					}
				});
				scLevel.setIncrement(2);
				scLevel.setPageIncrement(1);
				scLevel.setMaximum(2);
				scLevel.setLayoutData(new RowData(SWT.DEFAULT, 78));

				Composite composite_9 = new Composite(composite_3, SWT.NONE);
				composite_9.setLayout(new RowLayout(SWT.VERTICAL));
				composite_9.setLayoutData(new RowData(301, 100));

				btnDeleteSlot = new Button(composite_9, SWT.CHECK);
				btnDeleteSlot.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						specificDeletion
								.setDeleteSlotValues(SpecificDeletionDialogOld.btnDeleteSlot
										.getSelection());
						if (SpecificDeletionDialogOld.btnDeleteSlot.getSelection()) {
							coM1.setBackground(display
									.getSystemColor(SWT.COLOR_RED));
							SpecificDeletionDialogOld.radioAll.setEnabled(true);
							SpecificDeletionDialogOld.radioExpression
									.setEnabled(true);
							SpecificDeletionDialogOld.radioNull.setEnabled(true);
							if (SpecificDeletionDialogOld.radioExpression
									.getSelection()) {
								txExpression.setEnabled(true);
							} else {
								txExpression.setEnabled(false);
							}
						} else {
							coM1.setBackground(display
									.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
							SpecificDeletionDialogOld.radioAll.setEnabled(false);
							SpecificDeletionDialogOld.radioExpression
									.setEnabled(false);
							SpecificDeletionDialogOld.radioNull.setEnabled(false);
							txExpression.setEnabled(false);
						}
					}
				});
				btnDeleteSlot.setText("Delete Slot Values");

				radioAll = new Button(composite_9, SWT.RADIO);
				radioAll.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						specificDeletion
								.setDeleteMode(SpecificDeletion.deleteMode_All);
					}
				});
				radioAll.setSelection(true);
				radioAll.setText("All");
				radioAll.setEnabled(false);

				radioNull = new Button(composite_9, SWT.RADIO);
				radioNull.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						specificDeletion
								.setDeleteMode(SpecificDeletion.deleteMode_NullValue);
					}
				});
				radioNull.setText("Null Values");
				radioNull.setEnabled(false);

				radioExpression = new Button(composite_9, SWT.RADIO);
				radioExpression.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						specificDeletion
								.setDeleteMode(SpecificDeletion.deleteMode_Expression);
						if (SpecificDeletionDialogOld.radioExpression
								.getSelection()) {
							txExpression.setEnabled(true);
						} else {
							txExpression.setEnabled(false);
						}
					}
				});
				radioExpression.setText("With Expression");
				radioExpression.setEnabled(false);

				txExpression = new Text(composite_9, SWT.BORDER);
				txExpression.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						specificDeletion.setDeleteExpression(txExpression
								.getText());
					}
				});
				txExpression.setLayoutData(new RowData(195, SWT.DEFAULT));
				txExpression.setEnabled(false);

				Composite composite_10 = new Composite(shlIntrinsicDeletion,
						SWT.NONE);
				composite_10.setLayout(new FillLayout(SWT.HORIZONTAL));
				composite_10.setLayoutData(new RowData(241, SWT.DEFAULT));

				Button btnNewButton = new Button(composite_10, SWT.NONE);
				btnNewButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// e.display.dispose();
						shlIntrinsicDeletion.close();
					}
				});
				btnNewButton.setText("Ok");

				Button btnNewButton_1 = new Button(composite_10, SWT.NONE);
				btnNewButton_1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						// e.display.dispose();
						shlIntrinsicDeletion.close();
					}
				});
				btnNewButton_1.setText("Cancel");

				shlIntrinsicDeletion.open();
				shlIntrinsicDeletion.layout();
				while (!shlIntrinsicDeletion.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		});
		return specificDeletion;
	}
}
