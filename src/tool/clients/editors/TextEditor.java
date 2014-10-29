package tool.clients.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import tool.xmodeler.XModeler;

public class TextEditor {

  StyledText      text;
  static FontData defaultFont = new FontData("Courier New", 16, SWT.NO);

  public StyledText getText() {
    return text;
  }

  public TextEditor(CTabFolder parent, boolean editable) {
    text = new StyledText(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    text.setEditable(editable);
    text.setFont(new Font(XModeler.getXModeler().getDisplay(), defaultFont));
    Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    text.setBackground(bg);
  }

  public void setText(String s) {
    text.setText(s);
  }
}