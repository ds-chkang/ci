package medousa.sequential.data;


import medousa.sequential.utils.MySequentialGraphVars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Changhee Kang
 */
public class MyDataLoader
extends MyFileLoader {

    protected File mergedFile;
    protected ArrayList<ArrayList<String>> rawData;
    protected long recordCount = 0L;
    protected String mergedFileName = "/"+"mergedInputData.txt";

    public MyDataLoader() { super(); }
    public MyDataLoader(File [] inFiles) { super(inFiles); }

    @Override protected boolean readInputData() {
        boolean retVal = false;
        long dataCount = 0;
        mergedFile = MySequentialGraphFileMerger.mergeFiles(inFiles, new File(inFiles[0].getParent()+this.mergedFileName));
        rawData = new ArrayList<>();
        try {
            if (this.scanner == null) {
                this.scanner = new Scanner(mergedFile);
            }

            while (scanner.hasNextLine()) {
                String[] lineData = this.scanner.nextLine().split(MySequentialGraphVars.commaDelimeter);
                boolean allIsFine = true;
                for (String column : lineData) {
                    if (column.length() == 0) {
                        allIsFine = false;
                        break;
                    }
                }

                if (allIsFine) {
                    rawData.add(new ArrayList<>(Arrays.asList(lineData)));
                }
            }
            System.out.println(dataCount);
            retVal = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }


    @Override protected long getSize() {
        long fileSize = 0L;
        for ( File inFile : inFiles ) {
            fileSize = fileSize + inFile.length();
        }
        return getSize();
    }

    @Override protected long getLoadingTime() { return loadingTime; }
    public ArrayList<ArrayList<String>> getRawData() { return rawData; }
    public long getRecordCount() { return recordCount; }
}
