package medousa.security;

import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyDateMonitor {

    private static String freeTrialOverMsg =
            "<html><body><br>Your free-trial has exceeded " + MyDirectGraphVars.freeTrialDays + " days. Make a donation to support medousa.net and" +
            " get 1-year-license for it..</body></html>";

    public MyDateMonitor() {}
    public static void checkDate() {
        try {
           MyDateLogger dateLogger = new MyDateLogger();
           dateLogger.createInitialDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat(MyDirectGraphVars.dateSecurityFormat, Locale.ENGLISH);
            Date today_date = new Date();

            BufferedReader br = new BufferedReader(new FileReader("." + MyDirectGraphSysUtil.getDirectorySlash() + MyDirectGraphVars.dateSecurityFileName));
            String init_date_str = br.readLine();
            Date init_date = dateFormat.parse(init_date_str);

            long diffInMillies = Math.abs(today_date.getTime() - init_date.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff == MyDirectGraphVars.freeTrialDays) {
                MyMessageUtil.showInfoMsg(getDonateMsg());
                System.exit(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getDonateMsg() { return freeTrialOverMsg; }

}