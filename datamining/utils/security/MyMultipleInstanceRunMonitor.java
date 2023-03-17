package datamining.utils.security;

import datamining.utils.message.MyMessageUtil;

import java.io.IOException;
import java.net.ServerSocket;

public class MyMultipleInstanceRunMonitor {
    private static ServerSocket serverSocket;
    private static int port = 4444;

    public static void monitorInstances() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            MyMessageUtil.showErrorMsg("Multiple instances of medousa are not allowed.");
            System.exit(100);
        }
    }

}
