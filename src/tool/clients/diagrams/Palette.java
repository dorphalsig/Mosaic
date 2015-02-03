package tool.clients.diagrams;

import java.io.PrintStream;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;

import xos.Value;

public class Palette {

  ExpandBar                groupContainer;
  Hashtable<String, Group> groups            = new Hashtable<String, Group>();
  String                   currentTool       = "";
  boolean                  currentToolIsEdge = false;

  public Palette(Composite parent) {
    groupContainer = new ExpandBar(parent, SWT.V_SCROLL);
    newGroup("Diagram");
    newTool("Diagram", "Select", "Select", false, "Select.gif");
  }

  public String getCurrentTool() {
    return currentTool;
  }

  public boolean currentToolIsEdge() {
    return currentToolIsEdge;
  }

  public void newGroup(String name) {
    Group group = new Group(this, groupContainer, name);
    groups.put(name, group);
  }

  public void newTool(String groupName, String label, String toolId, boolean isEdge, String icon) {
    if (groups.containsKey(groupName)) {
      Group group = groups.get(groupName);
      group.newTool(label, toolId, isEdge, icon);
      groupContainer.layout();
    } else System.err.println("cannot find group " + groupName);
  }

  public void setCurrentTool(String toolId, boolean isEdge) {
    currentTool = toolId;
    currentToolIsEdge = isEdge;
  }

  public void reset() {
    setCurrentTool("Select", false);
    for (Group group : groups.values())
      group.resetButtons();
  }

  public void writeXML(PrintStream out) {
    out.print("<Palette>");
    for (String groupName : groups.keySet())
      if (!groupName.equals("Diagram")) groups.get(groupName).writeXML(groupName, out);
    out.print("</Palette>");
  }

  public Value asValue() {
    Value[] g = new Value[groups.size()];
    int i = 0;
    for (String name : groups.keySet())
      g[i++] = groups.get(name).asValue(name);
    return new Value(g);
  }

  public void deleteGroup(String name) {
    if (groups.containsKey(name)) {
      Group group = groups.get(name);
      groups.remove(name);
      group.delete();
    }

  }
}
