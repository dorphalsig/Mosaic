package tool.clients.forms;

import java.io.PrintStream;
import java.util.Vector;

import org.eclipse.swt.widgets.ToolBar;

public class FormTools {

  String              id;
  Vector<FormToolDef> tools = new Vector<FormToolDef>();

  public FormTools(String id) {
    super();
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public Vector<FormToolDef> getTools() {
    return tools;
  }

  public void addTool(String toolName, String id) {
    tools.add(new FormToolDef(eventName(toolName), id, iconFile(toolName)));
  }

  public void writeXML(PrintStream out) {
    out.print("<FormTools id='" + getId() + "'>");
    for (FormToolDef def : tools)
      def.writeXML(out);
    out.print("</FormTools>");
  }

  private String iconFile(String toolName) {
    if (toolName.equals("browseAndClearHistory"))
      return "icons/Clear.gif";
    else {
      System.out.println("unkown tool icon file for " + toolName);
      return "icons/Object.gif";
    }
  }

  public void populateToolBar(ToolBar toolBar) {
    for (FormToolDef def : tools)
      def.populateToolBar(toolBar);
  }

  private String eventName(String toolName) {
    if (toolName.equals("browseAndClearHistory"))
      return "clearHistory";
    else {
      System.out.println("unknown tool event name for " + toolName);
      return toolName;
    }
  }
}
