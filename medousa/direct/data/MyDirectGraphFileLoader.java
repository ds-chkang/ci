package medousa.direct.data;

import java.io.File;
import java.util.Scanner;

public abstract class MyDirectGraphFileLoader {

    public File [] inFiles;
    protected Scanner scanner;
    protected long loadingTime = 0L;
    protected long recordCount = 0L;

    public MyDirectGraphFileLoader() {}
    protected MyDirectGraphFileLoader(File[] files) { inFiles = files; }
    protected abstract boolean readInputData();
    protected abstract long getSize();
    public long getRecordCount() { return recordCount; }
    protected abstract long getLoadingTime();


}
