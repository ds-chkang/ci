package medousa.message;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTimerThread
extends Thread {
	 
    protected boolean isRunning;

    protected JLabel dateLabel;
    protected JLabel timeLabel;

    protected SimpleDateFormat dateFormat = 
            new SimpleDateFormat("yyyy/MM/dd");
    protected SimpleDateFormat timeFormat =
            new SimpleDateFormat("hh:mm:ss");

    public MyTimerThread(JLabel dateLabel, JLabel timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        this.isRunning = true;
    }

    @Override public void run() {
        new Thread(new Runnable() {
            @Override public void run() {
                while (isRunning) {
                    try {
                        Calendar currentCalendar = Calendar.getInstance();
                        Date currentTime = currentCalendar.getTime();
                        dateLabel.setText(dateFormat.format(currentTime));
                        timeLabel.setText(timeFormat.format(currentTime));
                        Thread.sleep(5000L);
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
