package medousa.test;

import medousa.sequential.utils.MySequentialGraphSysUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarTest {
    /**
     * Add or remove specified files to existing jar file. If a specified file
     * to be updated or added does not exist, the jar entry will be created
     * with the file name itself as the content.
     *
     * @param src the original jar file name
     * @param dest the new jar file name
     * @param files the files to update. The list is broken into 2 groups
     *              by a "-" string. The files before in the 1st group will
     *              be either updated or added. The files in the 2nd group
     *              will be removed. If no "-" exists, all files belong to
     *              the 1st group.
     */
    public static void updateJar(String src, String dest, String [] files) throws IOException {
        try (JarOutputStream jos = new JarOutputStream(
                new FileOutputStream(dest))) {

            // copy each old entry into destination unless the entry name
            // is in the updated list
            List<String> updatedFiles = new ArrayList<>();
            try (JarFile srcJarFile = new JarFile(src)) {
                Enumeration<JarEntry> entries = srcJarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    boolean found = false;
                    boolean update = true;
                    for (String file : files) {
                        if (file.equals("-")) {
                            update = false;
                        } else if (name.equals(file)) {
                            updatedFiles.add(file);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        if (update) {
                            System.out.println(String.format(
                                    "Updating %s with %s", dest, name));
                            jos.putNextEntry(new JarEntry(name));
                            try (FileInputStream fis = new FileInputStream(
                                    name)) {
                                fis.transferTo(jos);
                            } catch (FileNotFoundException e) {
                                jos.write(name.getBytes());
                            }
                        } else {
                            System.out.println(String.format("Removing %s from %s", name, dest));
                        }
                    } else {
                        System.out.println(String.format(
                                "Copying %s to %s", name, dest));
                        jos.putNextEntry(entry);
                        srcJarFile.getInputStream(entry).transferTo(jos);
                    }
                }
            }

            // append new files
            for (String file : files) {
                if (file.equals("-")) {
                    break;
                }
                if (!updatedFiles.contains(file)) {
                    System.out.println(String.format("Adding %s with %s",
                            dest, file));
                    jos.putNextEntry(new JarEntry(file));
                    try (FileInputStream fis = new FileInputStream(file)) {
                        fis.transferTo(jos);
                    } catch (FileNotFoundException e) {
                        jos.write(file.getBytes());
                    }
                }
            }
        }
        System.out.println();
    }

    public static void main(String [] args) {
        try {
            File [] files = MySequentialGraphSysUtil.getFileList(MySequentialGraphSysUtil.getWorkingDir()+MySequentialGraphSysUtil.getDirectorySlash()+"elements");
            String [] fileNames = new String[files.length];
            for (int i=0; i < files.length; i++) {
                System.out.println(files[i].getAbsolutePath());
                fileNames[i] = files[i].getAbsolutePath();
            }
            updateJar(MySequentialGraphSysUtil.getWorkingDir()+MySequentialGraphSysUtil.getDirectorySlash()+"test.jar",
                    MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() +"test2.jar",
                    fileNames);
        } catch (Exception ex) {

        }
    }
}