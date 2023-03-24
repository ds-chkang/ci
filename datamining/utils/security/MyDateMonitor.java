package datamining.utils.security;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyDateMonitor {

    private static String donate_msg =
            "<html><body><br>Your free-trial-period has exceeded " + MyVars.freeTrialDays + " days. Make a donation to support medousa.net." +
            " get a 1-year-license for it. <br>Email at info@medousa.net for how to make a donation and get a 1-year-license.</body></html>";

    public MyDateMonitor() {}
    public static void checkDate() {
        try {
           MyDateLogger dateLogger = new MyDateLogger();
           dateLogger.createInitialDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat(MyVars.dateSecurityFormat, Locale.ENGLISH);
            Date today_date = new Date();

            BufferedReader br = new BufferedReader(new FileReader("." + MySysUtil.getDirectorySlash() + MyVars.dateSecurityFileName));
            String init_date_str = br.readLine();
            Date init_date = dateFormat.parse(init_date_str);

            long diffInMillies = Math.abs(today_date.getTime() - init_date.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff == MyVars.freeTrialDays) {
                MyMessageUtil.showInfoMsg(getDonateMsg());
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getDonateMsg() { return donate_msg; }

}