package medousa.test;

import medousa.message.MyMessageUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.jar.*;

public class MyUserComputerChecker {

    public MyUserComputerChecker() {}

    private static void createIDFile(String filePath) {
        try {
            File idFile = new File(filePath);
            if (idFile.exists()) idFile.delete();
            idFile.createNewFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isIDFileExistsInJar(String exJarPath, String targetFileInJar) {
        try {
            JarFile jarFile = new JarFile(exJarPath);
            JarEntry entry = jarFile.getJarEntry(targetFileInJar);
            if (entry != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static String readIDContent(String exJarPath, String targetFileInJar) {
        try {
            JarFile jarFile = new JarFile(exJarPath);
            JarEntry entry = jarFile.getJarEntry(targetFileInJar);
            if (entry != null) {
                InputStream inputStream = jarFile.getInputStream(entry);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();
                reader.close();
                jarFile.close();
                return line;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static void run() {
        String jarPath = "c:\\temp\\medousa.jar";
        String filePath = "c:\\temp\\id.txt";

        //String jarPath = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "medousa.jar";
        //String filePath = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "id.txt";
        String entryName = "id.txt";

        try {
            if (!isIDFileExistsInJar(jarPath, entryName)) {
                // Create ID File.
                File idFile = new File(filePath);
                if (idFile.exists()) idFile.delete();
                idFile.createNewFile();

                String ipAndMacAddr = getIpAddress() + "*" + getMacAddress();
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)));
                bw.write(ipAndMacAddr + "\n");
                bw.close();

                // Add id file to jar.
                File tempJar = File.createTempFile("temp", ".jar");
                tempJar.deleteOnExit();

                JarOutputStream jarOutput = new JarOutputStream(new FileOutputStream(tempJar));
                JarFile jarFile = new JarFile(jarPath);

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    jarOutput.putNextEntry(entry);

                    InputStream entryInput = jarFile.getInputStream(entry);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = entryInput.read(buffer)) != -1) {
                        jarOutput.write(buffer, 0, bytesRead);
                    }

                    jarOutput.closeEntry();
                    entryInput.close();
                }

                File file = new File(filePath);
                JarEntry newEntry = new JarEntry(entryName);
                jarOutput.putNextEntry(newEntry);

                FileInputStream fileInput = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                    jarOutput.write(buffer, 0, bytesRead);
                }

                jarOutput.closeEntry();
                fileInput.close();

                jarOutput.close();

                jarFile.close();

                File originalJar = new File(jarPath);
                originalJar.delete();

                tempJar.renameTo(originalJar);

                // Remove ID File.
                idFile.delete();
            } else {
                // Verify whether the user has switched to a different computer from the one previously in use..
                String ipAddrAndMacAddr = getIpAddress() + "*" + getMacAddress();
                String exIDContent = readIDContent(jarPath, "id.txt");
                if (!ipAddrAndMacAddr.equals(exIDContent)) {
                    MyMessageUtil.showErrorMsg(MySequentialGraphVars.app, "<html><body>medousa can only run on a licensed computer.<br> medousa will terminate.");
                    System.exit(0);
                } else {
                    //System.out.println("The application has not been bleached.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getIpAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            String ipAddr =  ip.getHostAddress();
            return ipAddr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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