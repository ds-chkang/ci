package medousa.test;

import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class MyJarFileChecker {

    public MyJarFileChecker() {}

    public boolean isFileExists(String jarFilePath, String idfile) {
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            ZipEntry entry = jarFile.getEntry(idfile);
            jarFile.close();

            if (entry != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
