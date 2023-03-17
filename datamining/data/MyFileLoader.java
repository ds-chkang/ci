package datamining.data;

import java.io.File;
import java.util.Scanner;

public abstract class MyFileLoader {

    public File [] inFiles;
    protected Scanner scanner;
    protected long loadingTime = 0L;
    protected long recordCount = 0L;

    public MyFileLoader() {}
    protected MyFileLoader(File[] files) { inFiles = files; }
    protected abstract boolean readInputData();
    protected abstract long getSize();
    public long getRecordCount() { return recordCount; }
    protected abstract long getLoadingTime();


}
