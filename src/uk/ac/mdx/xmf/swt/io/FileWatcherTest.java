package uk.ac.mdx.xmf.swt.io;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// TODO: Auto-generated Javadoc
/**
 * The Class FileWatcherTest.
 */
public class FileWatcherTest {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String args[]) {
		// monitor a single file
		TimerTask task = new FileWatcher(new File("c:/temp/text.txt")) {
			protected void onChange(File file) {
				// here we code the action on a change
				System.out.println("File " + file.getName() + " have change !");
			}
		};

		Timer timer = new Timer();
		// repeat the check every second
		timer.schedule(task, new Date(), 1000);
	}
}
