package tool.clients.diagrams;

import org.eclipse.swt.graphics.GC;

public interface Selectable {

  void paintSelected(GC gc);

  void moveEvent();

  void moveBy(int dx, int dy);

}
