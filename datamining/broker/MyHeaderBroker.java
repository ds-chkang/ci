package datamining.broker;

import datamining.data.MyHeaderLoader;

import java.io.File;

public class MyHeaderBroker
extends MyFileBroker {

    private MyHeaderLoader headerLoader;

    public MyHeaderBroker() {
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
