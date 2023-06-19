package medousa.test;

import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class FTPFileReader {
    public static void main(String[] args) {
        String server = "medousa.net";
        int port = 21;
        String user = "medousa";
        String password = "new21story";
        String remoteFilePath = "index.html";
        String localFilePath = MySequentialGraphSysUtil.getWorkingDir()+MySequentialGraphSysUtil.getDirectorySlash() + "index.html";

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            OutputStream outputStream = new FileOutputStream(localFilePath);
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.close();

            if (success) {
                System.out.println("File downloaded successfully.");
            } else {
                System.out.println("File download failed.");
            }

            File localFile = new File(localFilePath);
            InputStream inputStream = new FileInputStream(localFile);

            success = ftpClient.storeFile("hello.txt", inputStream);
            inputStream.close();

            if (success) {
                System.out.println("File uploaded successfully.");
            } else {
                System.out.println("File upload failed.");
            }

            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
