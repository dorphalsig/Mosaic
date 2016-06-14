package tool.doc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class DocFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		DocFrame frame = new DocFrame();
		MyTree tree = new MyTree();
		JScrollPane left = new JScrollPane(tree);
		JPanel right = new JPanel();
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		split.setDividerLocation(300);
		frame.setContentPane(split);

		frame.pack();
		
		frame.setLocation(400, 200);
		frame.setSize(900,600);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final MyTreeModel model = new MyTreeModel(frame);
		tree.setModel(model);

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuItem1 = new JMenuItem("Save");
		menuItem1.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {model.save();}
		});
		menu.add(menuItem1);
		JMenuItem menuItem2 = new JMenuItem("Load");
		menuItem2.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {model.load();}
		});
		menu.add(menuItem2);
		menubar.add(menu);

		frame.setJMenuBar(menubar);

		frame.setVisible(true);
		
	}

}
