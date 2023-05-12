package medousa.sequential.broker;

import medousa.sequential.data.MyHeaderLoader;

import java.io.File;

public class MySequentialGraphHeaderBroker
extends MySequentialGraphFileBroker {

    private MyHeaderLoader headerLoader;

    public MySequentialGraphHeaderBroker() {
        super();
        this.headerLoader = new MyHeaderLoader();
    }
    public String [] loadHeader(final File headerFile) {
        this.headerLoader = new MyHeaderLoader();
        String [] headers = this.headerLoader.load(headerFile);
        return headers;
    }
    public int getHeaderIndex(String columnName) {
        return this.headerLoader.getHeaderIndex(columnName);
    }
}
