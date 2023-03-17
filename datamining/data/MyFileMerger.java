package datamining.data;

import datamining.utils.system.MyVars;

import java.io.*;

public class MyFileMerger {


    public static File mergeFiles(File[] files, File mergedFile) {
        try {
            FileWriter fstream = new FileWriter(mergedFile, false);
            BufferedWriter out = new BufferedWriter(fstream);
            for (File f : files) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String aLine = "";
                while ((aLine = in.readLine()) != null) {
                    String [] dataColumnValues = aLine.split(MyVars.commaDelimeter);
                    dataColumnValues[MyVars.app.getMsgBroker().getHeaderIndex("ITEM ID")] =
                        String.valueOf(dataColumnValues[MyVars.app.getMsgBroker().getHeaderIndex("ITEM ID")]);
                    aLine = dataColumnValues[0];
                    for (int i = 1; i < dataColumnValues.length; i++) {
                        aLine = aLine + MyVars.commaDelimeter + dataColumnValues[i];
                    }
                    //System.out.println("After: " + aLine);
                    out.write(aLine + "\n");
                }
                in.close();
            }
            out.close();
        }  catch (Exception ex) {}

        return mergedFile;
    }

    public static File mergeDirectRelationFiles(File[] files, File mergedFile) {
        try {
            FileWriter fstream = new FileWriter(mergedFile, false);
            BufferedWriter out = new BufferedWriter(fstream);
            for (File f : files) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String aLine = "";
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine + "\n");
                }
                in.close();
            }
            out.close();
        }  catch (Exception ex) {}

        return mergedFile;
    }

}