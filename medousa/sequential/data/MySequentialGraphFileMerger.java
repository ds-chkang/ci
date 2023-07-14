package medousa.sequential.data;

import medousa.sequential.utils.MySequentialGraphVars;

import java.io.*;

public class MySequentialGraphFileMerger {

    private static String columnseparator = ",";

    public static File mergeFiles(File[] files, File mergedFile) {
        try {
            MySequentialGraphVars.mergedFileLocation = mergedFile.getAbsolutePath();
            FileWriter fstream = new FileWriter(mergedFile, false);
            BufferedWriter out = new BufferedWriter(fstream);
            for (File f : files) {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String aLine = "";
                while ((aLine = in.readLine()) != null) {
                    String [] dataColumnValues = aLine.split(columnseparator);

                    if (dataColumnValues.length < MySequentialGraphVars.numberOfInputDataColumns) {
                        continue;
                    }

                    boolean allIsFine = true;
                    for (String column : dataColumnValues) {
                        column = column.replaceAll(" ", "");
                        if (column.length() == 0) {
                            allIsFine = false;
                            break;
                        }
                    }

                    if (allIsFine) {
                        dataColumnValues[MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex("ITEM ID")] =
                                String.valueOf(dataColumnValues[MySequentialGraphVars.app.getSequentialGraphMsgBroker().getHeaderIndex("ITEM ID")]);
                        aLine = dataColumnValues[0];
                        for (int i = 1; i < dataColumnValues.length; i++) {
                            aLine = aLine + columnseparator + dataColumnValues[i];
                        }
                        out.write(aLine + "\n");
                    }
                }
                in.close();
            }
            out.close();
        }  catch (Exception ex) {}

        return mergedFile;
    }


}