package datamining.main;

import datamining.utils.message.MyTimerThread;
import datamining.utils.system.MyVars;

import javax.swing.*;

/**
 * 
 * AssItemStatusBar is a project level status bar placed at the bottom of the application.
 * 
 * 
 * @author Changhee Kang
 * @first  01/04/2016
 * @last   14/04/2016
 * @category Main
 * @location Seoul, Korea
 * 
 */

public class
MyStatusBar
extends MyJStatusBar {

    private static JProgressBar _jp = new JProgressBar();

    // Default constructor creates status bar.
	public MyStatusBar() {
		createStatusBar(MyVars.statusAppVerMsg);
	}
	
	private void createStatusBar(final String msg) {
        final JLabel date = new JLabel();

        date.setFont(MyVars.tahomaPlainFont12);
		date.setHorizontalAlignment(JLabel.CENTER);
		date.setVerticalAlignment(JLabel.BOTTOM);

        final JLabel time = new JLabel();
        time.setFont(MyVars.tahomaPlainFont12);
		time.setHorizontalAlignment(JLabel.CENTER);
		time.setVerticalAlignment(JLabel.BOTTOM);

    	// Run time thread.
        MyTimerThread timerThread = new MyTimerThread(date, time);
        timerThread.start();
        
        JLabel title = new JLabel(msg);
        title.setFont(MyVars.tahomaPlainFont12);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setVerticalAlignment(JLabel.CENTER);

        setLeftComponentToWEST(title);
        addRightComponent(time);
        addRightComponent(date);
	}

	// Exposed methods to be better controlled with ease.
	public static JProgressBar getProgressBar () {
		return _jp;
	}

	public static void updateProcessStatus(String msg) {
		//_jp.setStringPainted(true);
		//_jp.setForeground(Color.GREEN);
		//_jp.setString(msg);
	}

	public static void showProgressBar() {
		_jp.setVisible(true);
	}

	public static void hideProgressBar() {
		_jp.setVisible(false);
	}

}
