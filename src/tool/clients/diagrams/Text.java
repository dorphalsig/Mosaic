package tool.clients.diagrams;

import java.io.PrintStream;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import tool.clients.dialogs.notifier.NotificationType;
import tool.clients.dialogs.notifier.NotifierDialog;
import tool.xmodeler.XModeler;
import uk.ac.mdx.xmf.swt.misc.ColorManager;
import xos.Message;
import xos.Value;

public class Text implements Display {

  String  id;
  String  text;
  int     x;
  int     y;
  boolean editable;
  boolean underline;
  boolean italicise;
  int     red;
  int     green;
  int     blue;
  String  fontData = "";
  Font    font     = null;

  public Text(String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
    super();
    this.id = id;
    this.text = text;
    this.x = x;
    this.y = y;
    this.editable = editable;
    this.underline = underline;
    this.italicise = italicise;
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public boolean contains(int x, int y) {
    return x >= getX() && y >= getY() && x <= getX() + getWidth() && y <= getY() + getHeight();
  }

  public void doubleClick(GC gc, final Diagram diagram, int dx, int dy, int mouseX, int mouseY) {
    if (editable && contains(mouseX - dx, mouseY - dy)) {
      final org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(diagram.getCanvas(), SWT.BORDER);
//      text.setFont(DiagramClient.diagramFont);
	  Font baseFont = italicise ? DiagramClient.diagramItalicFont : DiagramClient.diagramFont;
	  FontDescriptor myDescriptor = FontDescriptor.createFrom(baseFont).setHeight(12);// * 100 / XModeler.getDeviceZoomPercent());
	  Font zoomFont = myDescriptor.createFont(XModeler.getXModeler().getDisplay());
	  text.setFont(zoomFont);
      text.setText(this.text);
      Point p = diagram.scaleinv(dx + getX(), dy + getY());
      text.setLocation(p.x, p.y);
      text.setSize(getWidth() + 10, getHeight() + 10);
      text.setVisible(true);
      text.selectAll();
      //text.setFocus(); - done delayed to not loose focus on Linux, see below
      NotifierDialog.notify("Edit Text", "Type text then RET to update.\nType ESC to cancel.", NotificationType.values()[3]);
      Listener listener = new Listener() {
        public void handleEvent(Event event) {
          org.eclipse.swt.widgets.Text t;
          switch (event.type) {
          case SWT.FocusOut:
			t = (org.eclipse.swt.widgets.Text) event.widget;
			t.setVisible(false);
			t.dispose();
			diagram.redraw();
            break;
          case SWT.Verify:
            t = (org.eclipse.swt.widgets.Text) event.widget;
            GC gc = new GC(t);
            Point size = gc.textExtent(t.getText() + event.text);
            t.setSize(size.x + 10, getHeight() + 10);
            break;
          case SWT.Traverse:
            switch (event.detail) {
            case SWT.TRAVERSE_RETURN:
              t = (org.eclipse.swt.widgets.Text) event.widget;
              textChangedEvent(t.getText());
              t.setVisible(false);
              t.dispose();
              diagram.redraw();
              event.doit = false;
              break;
            case SWT.TRAVERSE_ESCAPE:
              t = (org.eclipse.swt.widgets.Text) event.widget;
              t.setVisible(false);
              t.dispose();
              diagram.redraw();
              event.doit = false;
              break;
            }
            break;
          }
        }
      };
      text.addListener(SWT.FocusOut, listener);
      text.addListener(SWT.Verify, listener);
      text.addListener(SWT.Traverse, listener);

      XModeler.getXModeler().getDisplay().timerExec(100, new Runnable() {
          public void run() {
          	  text.setFocus();
          }
      });
    }
  }

  public void editText(String id) {
    if (id.equals(getId())) editable = true;
  }

  public int getBlue() {
    return blue;
  }

  public int getGreen() {
    return green;
  }

  public Font getFont() {
    if (font == null) {
      if (fontData.equals("")) {
        return DiagramClient.diagramFont;
      } else {
        font = new Font(XModeler.getXModeler().getDisplay(), new FontData(fontData));
        return font;
      }
    } else return font;
  }

  public int getHeight() {
    Point extent = DiagramClient.theClient().textDimension(text, getFont());
    return extent.y;// * 100 / XModeler.getDeviceZoomPercent();
  }

  public String getId() {
    return id;
  }

  public int getRed() {
    return red;
  }

  public String getText() {
    return text;
  }

  public int getWidth() {
    Point extent = DiagramClient.theClient().textDimension(text, getFont());
    return extent.x;// * 100 / XModeler.getDeviceZoomPercent();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isEditable() {
    return editable;
  }

  public boolean isItalicise() {
    return italicise;
  }

  public boolean isUnderline() {
    return underline;
  }

  public void italicise(String id, boolean italics) {
    if (id.equals(getId())) italicise = italics;
  }

  public void move(String id, int x, int y) {
    if (getId().equals(id)) {
      this.x = x;
      this.y = y;
    }
  }

  public void newBox(String parentId, String id, int x, int y, int width, int height, int curve, boolean top, boolean right, boolean bottom, boolean left, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {
  }

  public void newMultilineText(String parentId, String id, String text, int x, int y, int width, int height, boolean editable, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, String font) {
  }

  public void newText(String parentId, String id, String text, int x, int y, boolean editable, boolean underline, boolean italicise, int red, int green, int blue) {
  }
  
  public void newNestedDiagram(String parentId, String id, int x, int y, int width, int height, org.eclipse.swt.widgets.Composite canvas) {}


  public void paint(GC gc, int x, int y) {
	    Font font = gc.getFont();
	    Color c = gc.getForeground(); //Bj�rn
	    gc.setFont(italicise ? DiagramClient.diagramItalicFont : DiagramClient.diagramFont);
	    //Check if a color is set
	    if(getRed() >=0 && getGreen() >= 0 && getBlue() >= 0){ //Bj�rn
	    	gc.setForeground(ColorManager.getColor(new RGB(getRed(), getGreen(), getBlue())));
	    }
	    gc.drawText(text, x + getX(), y + getY(), true);
	    gc.setFont(font);
	    gc.setForeground(c); //Bj�rn
	  }

  public void paintHover(GC gc, int x, int y, int dx, int dy) {
    if (editable && contains(x - dx, y - dy)) paintSelectableOutline(gc, dx, dy);
  }

  private void paintSelectableOutline(GC gc, int dx, int dy) {
    Color c = gc.getForeground();
    gc.setForeground(Diagram.GREY);
    gc.drawRectangle(dx + getX(), dy + getY(), getWidth(), getHeight());
    gc.setForeground(c);
  }

  public void remove(String id) {
  }

  public void resize(String id, int width, int height) {
  }

  public void setFillColor(String id, int red, int green, int blue) {
	  //Bj�rn
	  if (id.equals(getId())){
		this.red = red;  
		this.green = green;
		this.blue = blue;
	  }
  }  

  public void setText(String text) {
    this.text = text;
  }

  public void setText(String id, String text) {
    if (id.equals(getId())) this.text = text;
  }

  public void textChangedEvent(String text) {
    Message message = DiagramClient.theClient().getHandler().newMessage("textChanged", 2);
    message.args[0] = new Value(id);
    message.args[1] = new Value(text);
    //System.out.println("textChanged: " + message);
    DiagramClient.theClient().getHandler().raiseEvent(message);
  }

  public String toString() {
    return "Text(" + id + "," + x + "," + y + "," + text + ")";
  }

  public void writeXML(PrintStream out) {
    out.print("<Text ");
    out.print("id='" + getId() + "' ");
    out.print("text='" + XModeler.encodeXmlAttribute(getText()) + "' ");
    out.print("x='" + getX() + "' ");
    out.print("y='" + getY() + "' ");
    out.print("editable='" + isEditable() + "' ");
    out.print("underline='" + isUnderline() + "' ");
    out.print("italicise='" + isItalicise() + "' ");
    out.print("red='" + getRed() + "' ");
    out.print("green='" + getGreen() + "' ");
    out.print("blue='" + getBlue() + "'/>");
  }

  public void newEllipse(String parentId, String id, int x, int y, int width, int height, boolean showOutline, int lineRed, int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue) {

  }

  public void newImage(String parentId, String id, String fileName, int x, int y, int width, int height) {

  }

  public void setFont(String id, String fontData) {
    if (getId().equals(id)) {
      this.fontData = fontData;
      font = null;
    }
  }
  
  public void setEditable(String id, boolean editable){
	  if (getId().equals(id)) {
	      this.editable = editable;
	      
	   }  
  }

@Override
public void showEdges(String id, boolean top, boolean bottom, boolean left,
		boolean right) {
	// TODO Auto-generated method stub
	
}

@Override
public void newShape(String parentId, String id, int x, int y, int width, int height, boolean showOutline, int lineRed,
		int lineGreen, int lineBlue, int fillRed, int fillGreen, int fillBlue, int[] points) {
	// TODO Auto-generated method stub
	
}
}
