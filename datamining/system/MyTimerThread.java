package datamining.system;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTimerThread extends Thread {
	 
    protected boolean isRunning;

    protected JLabel dateLabel;
    protected JLabel timeLabel;

    protected SimpleDateFormat dateFormat = 
            new SimpleDateFormat("EEE, d/MMM/yyyy");
    protected SimpleDateFormat timeFormat =
            new SimpleDateFormat("h:mm a");

    public MyTimerThread(JLabel dateLabel, JLabel timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Calendar currentCalendar = 
                        Calendar.getInstance();
                    Date currentTime = 
                        currentCalendar.getTime();
                    dateLabel.setText(dateFormat
                        .format(currentTime));
                    timeLabel.setText(timeFormat
                        .format(currentTime));
                }
            });

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
