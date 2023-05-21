package medousa.direct.broker;

import java.io.File;

public class MyDirectGraphFileBroker
extends MyDirectGraphConfigBroker {

    protected File [] files;
    protected File file;

    public MyDirectGraphFileBroker() {}
    public MyDirectGraphFileBroker(final File [] files) {
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
