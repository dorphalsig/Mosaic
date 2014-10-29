package tool.clients.forms;

import java.util.Vector;

import org.eclipse.swt.widgets.ToolBar;

public class FormTools {

  Vector<FormToolDef> tools = new Vector<FormToolDef>();

  public void addTool(String toolName, String id) {
    tools.add(new FormToolDef(eventName(toolName), id, iconFile(toolName)));
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
