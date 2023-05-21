package medousa.direct.data;

import medousa.direct.utils.MyDirectGraphVars;

import java.io.*;

public class MyDirectGraphFileMerger {


    public static File mergeFiles(File[] files, File mergedFile) {
        try {
            FileWriter fstream = new FileWriter(mergedFile, false);
            BufferedWriter out = new BufferedWriter(fstream);
            for (File f : files) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String aLine = "";
                while ((aLine = in.readLine()) != null) {
                    String [] dataColumnValues = aLine.split(MyDirectGraphVars.commaDelimeter);
                    dataColumnValues[MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex("ITEM ID")] =
                        String.valueOf(dataColumnValues[MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex("ITEM ID")]);
                    aLine = dataColumnValues[0];
                    for (int i = 1; i < dataColumnValues.length; i++) {
                        aLine = aLine + MyDirectGraphVars.commaDelimeter + dataColumnValues[i];
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