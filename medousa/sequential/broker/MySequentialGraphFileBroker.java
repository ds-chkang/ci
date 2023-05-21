package medousa.sequential.broker;

import java.io.File;

public class MySequentialGraphFileBroker
extends MySequentialGraphConfigBroker {

    protected File [] files;
    protected File file;

    public MySequentialGraphFileBroker() {}
    public MySequentialGraphFileBroker(final File [] files) {
        this.files = files;
    }
    public void setInputFiles(final File [] files) {
        for (File f : files) {
            //System.out.println(f.getName());
        }
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
