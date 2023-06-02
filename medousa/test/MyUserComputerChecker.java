package medousa.test;

import medousa.message.MyMessageUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.jar.*;

public class MyUserComputerChecker {

    public MyUserComputerChecker() {}

    private static boolean isSecurityJarExists(String jarFilePath) {
        try {
            File f = new File(jarFilePath);
            if (f.exists()) {
                return true;
            }
            return false;
        } catch (Exception ex) {

        }
        return false;
    }

    private static void addSecurityFileToJar(String jarFilePath) {
        try {
            JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFilePath));
            File file = new File(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "id.txt");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(getMacAddress() + "\n");
            bw.close();

            String entryName = file.getName();
            jos.putNextEntry(new JarEntry(entryName));

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                jos.write(buffer, 0, bytesRead);
            }

            jos.closeEntry();
            jos.close();
            file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isSecurityEntryExists(String jarFilePath, String entryName) {
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            JarEntry entry = jarFile.getJarEntry(entryName);
            if (entry == null) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static String readIDContent(String jarFilePath, String targetFileInJar) {
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            JarEntry entry = jarFile.getJarEntry(targetFileInJar);

            if (entry != null) {
                InputStream inputStream = jarFile.getInputStream(entry);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String macAddr = reader.readLine();
                reader.close();
                jarFile.close();
                return macAddr;
            }
        } catch (Exception ex) {}
        return "";
    }

    public static void run() {
        String jarPath = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sec.jar";
        try {
            if (!isSecurityJarExists(jarPath)) {
                MyMessageUtil.showErrorMsg(MySequentialGraphVars.app, "Place the security jar file in the same directory.");
                System.exit(0);
            } else if (!isSecurityEntryExists(jarPath, "id.txt")) {
                addSecurityFileToJar(jarPath);
            } else {
                String ipAddrAndMacAddr = getMacAddress();
                String exIDContent = readIDContent(jarPath, "id.txt");
                if (!ipAddrAndMacAddr.equals(exIDContent)) {
                    MyMessageUtil.showErrorMsg(MySequentialGraphVars.app, "<html><body>medousa can only run on a licensed computer.<br> medousa will terminate.");
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getMacAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte [] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}