package datamining.broker;

import java.io.File;

public class MyFileBroker
extends MyConfigBroker {

    protected File [] files;
    protected File file;

    public MyFileBroker() {}
    public MyFileBroker(final File [] files) {
        this.files = files;
    }
    public void setInputFiles(final File [] files) {
        this.files = files;
    }
    public void setOutputFile(final File file) {
        this.file = file;
    }
    public File getOutputFile() {
        return file;
    }
    public File [] getInputFiles() {
        return files;
    }
}
