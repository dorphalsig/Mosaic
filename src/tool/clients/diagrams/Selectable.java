package tool.clients.diagrams;

import org.eclipse.swt.graphics.GC;

public interface Selectable {

  void paintSelected(GC gc, int x, int y);

  void moveEvent();

  void moveBy(int dx, int dy);

  void rightClick(int x, int y);

  void deselect();
  
  void select();

}
