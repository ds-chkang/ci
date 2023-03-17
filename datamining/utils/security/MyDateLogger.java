package datamining.utils.security;

import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDateLogger {

    private File security_file = null;

    public MyDateLogger() {}

    public void createInitialDate() {
        try {
            // Read file content inside jar file.
            //BufferedReader txtReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/images/thomas.sec")));

            String security_file_name = "." + MySysUtil.getDirectorySlash()+MyVars.dateSecurityFileName;
            this.security_file = new File(security_file_name);
            if (!security_file.exists()) {
                security_file.createNewFile();
                this.writeDate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(MyVars.dateSecurityFormat, Locale.ENGLISH);
            Date date = new Date();
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.security_file, false));
            bw.write(dateFormat.format(date));
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String [] args) {
        MyDateLogger fc = new MyDateLogger();
        fc.createInitialDate();
    }

}
