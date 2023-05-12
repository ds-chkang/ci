package medousa.direct.data;


import medousa.direct.utils.MyDirectGraphVars;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Changhee Kang
 */
public class MyDirectGraphDirectGraphDataLoader
extends MyDirectGraphFileLoader {

    protected File mergedFile;
    protected ArrayList<ArrayList<String>> rawData;
    protected long recordCount = 0L;
    protected String mergedFileName = "/"+"mergedInputData.txt";

    public MyDirectGraphDirectGraphDataLoader() { super(); }
    public MyDirectGraphDirectGraphDataLoader(File [] inFiles) { super(inFiles); }

    @Override
    protected boolean readInputData() {
        boolean retVal = false;
        mergedFile = MyDirectGraphFileMerger.mergeFiles(inFiles, new File(inFiles[0].getParent()+this.mergedFileName));
        rawData = new ArrayList<>();
        try {
            if (this.scanner == null) this.scanner = new Scanner(mergedFile);
            while (scanner.hasNextLine()) {
                String[] lineData = this.scanner.nextLine().split(MyDirectGraphVars.commaDelimeter);
                rawData.add(new ArrayList<>(Arrays.asList(lineData)));
            }
            retVal = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }


    @Override
    protected long getSize() {
        long fileSize = 0L;
        for ( File inFile : inFiles ) {
            fileSize = fileSize + inFile.length();
        }
        return getSize();
    }

    @Override
    protected long getLoadingTime() { return loadingTime; }
    public ArrayList<ArrayList<String>> getRawData() { return rawData; }
    public long getRecordCount() { return recordCount; }
}
