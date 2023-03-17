package datamining.utils.message;

import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;

/**
 * Created by changhee on 17. 7. 22.
 */
public class MyMessageUtil {


    public static void showInfoMsg(String message) {
        try {

                    JLabel label = new JLabel(message);
                    label.setFont(MyVars.f_pln_12);
                    JOptionPane.showMessageDialog(MyVars.app, label, MyVars.appFrameTitleMsg, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showInfoMsg(Component comp, String message) {
        try {

                    JLabel label = new JLabel(message);
                    label.setFont(MyVars.f_pln_12);
                    JOptionPane.showMessageDialog(comp, label, MyVars.appFrameTitleMsg, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showErrorMsg(String msg) {
        try {

                    JLabel message = new JLabel(msg);
                    message.setFont(MyVars.f_pln_12);
                    JOptionPane.showMessageDialog(
                            MyVars.app,
                            message,
                            MyVars.appFrameTitleMsg, JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showErrorMsg(Component comp, String msg) {
        try {

                    JLabel message = new JLabel(msg);
                    message.setFont(MyVars.f_pln_12);
                    JOptionPane.showMessageDialog(
                            comp,
                            message,
                            MyVars.appFrameTitleMsg, JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int showConfirmMessage(String msg) {
        try {
                    JLabel message = new JLabel(msg);
                    message.setFont(MyVars.f_pln_12);
                    int dialogResult = JOptionPane.showConfirmDialog(
                            MyVars.app,
                            message,
                            MyVars.appFrameTitleMsg,
                            JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        return 1;
                    }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int showConfirmMessage(Component comp, String msg) {
        try {
            JLabel message = new JLabel(msg);
            message.setFont(MyVars.f_pln_12);
            int dialogResult = JOptionPane.showConfirmDialog(
                    comp,
                    message,
                    MyVars.appFrameTitleMsg,
                    JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                return 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * prints out text messages to console.
     */
    public static void toConsole(String message) {
        System.err.print(message);
    }

}
